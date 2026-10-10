package co.edu.unicauca.bancopreguntas.domain.services;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.AsignacionRevisorRepository;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;

public class AsignacionRevisorService {
    private AsignacionRevisorRepository asignacionRepository;
    private PreguntaRepository preguntaRepository;
    private NotificadorAsignacion notificador;

    public AsignacionRevisorService(AsignacionRevisorRepository asignacionRepository, 
                                     PreguntaRepository preguntaRepository, 
                                     NotificadorAsignacion notificador) {
        this.asignacionRepository = asignacionRepository;
        this.preguntaRepository = preguntaRepository;
        this.notificador = notificador;
    }

    public boolean asignarRevisorAPregunta(int preguntaId, String correoRevisor) {
        // 1. Buscar la pregunta manejando el Optional
        Pregunta pregunta = preguntaRepository.buscarPorId(preguntaId)
            .orElseThrow(() -> new IllegalArgumentException("La pregunta con ID " + preguntaId + " no existe."));

        // 2. Validar que esté en PENDIENTE_REVISION
        if (pregunta.getEstado() != EstadoPregunta.PENDIENTE_REVISION) {
            throw new IllegalStateException("Solo se pueden asignar revisores a preguntas en estado PENDIENTE_REVISION.");
        }

        // 3. Evitar duplicados
        if (asignacionRepository.existeAsignacion(preguntaId, correoRevisor)) {
            throw new IllegalArgumentException("Este revisor ya fue asignado a esta pregunta.");
        }

        // 4. Guardar asignación
        boolean guardado = asignacionRepository.asignarRevisor(preguntaId, correoRevisor);
        if (guardado) {
            // 5. Enviar correo real de notificación
            notificador.enviarNotificacion(correoRevisor, "Nueva pregunta asignada para revisión", 
                "Hola, se te ha asignado la pregunta con ID: " + preguntaId + " para su respectiva revisión.");
        }

        return guardado;
    }
}