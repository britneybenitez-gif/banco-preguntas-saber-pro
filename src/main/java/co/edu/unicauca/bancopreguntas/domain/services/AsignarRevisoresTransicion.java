package co.edu.unicauca.bancopreguntas.domain.services;

import java.util.List;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.repositories.AsignacionRevisorRepository;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;

/**
 * Transición concreta "Asignar revisores" (HU-04): una pregunta en
 * {@code PENDIENTE_REVISION} con al menos un revisor pasa a {@code EN_REVISION}.
 * Reutiliza el Template Method de las transiciones de estado.
 */
public class AsignarRevisoresTransicion extends TransicionEstadoPreguntaTemplate {

    private final AsignacionRevisorRepository asignacionRepository;
    private final List<Usuario> revisores;

    public AsignarRevisoresTransicion(PreguntaRepository preguntaRepository,
                                      AsignacionRevisorRepository asignacionRepository,
                                      List<Usuario> revisores) {
        super(preguntaRepository);
        this.asignacionRepository = asignacionRepository;
        this.revisores = List.copyOf(revisores);
    }

    @Override
    protected void validarPrecondiciones(Pregunta pregunta, int usuarioId) {
        if (pregunta.getEstado() != EstadoPregunta.PENDIENTE_REVISION) {
            throw new IllegalArgumentException(
                    "Solo se pueden asignar revisores a preguntas en estado Pendiente de revisión");
        }
        if (revisores.isEmpty()) {
            throw new IllegalArgumentException("Debe asignar al menos un revisor");
        }
    }

    @Override
    protected void ejecutarTransicion(Pregunta pregunta, int usuarioId) {
        pregunta.setRevisores(revisores);
        pregunta.setEstado(EstadoPregunta.EN_REVISION);
    }

    @Override
    protected void persistir(Pregunta pregunta) {
        super.persistir(pregunta);
        asignacionRepository.asignarRevisores(pregunta.getId(), revisores);
    }
}
