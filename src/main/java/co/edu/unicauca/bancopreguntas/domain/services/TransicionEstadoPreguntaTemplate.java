package co.edu.unicauca.bancopreguntas.domain.services;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;

/**
 * Patrón Template Method para transiciones de estado de una pregunta.
 * Define el orden fijo: buscar → validar → ejecutar → persistir.
 */
public abstract class TransicionEstadoPreguntaTemplate {

    protected final PreguntaRepository preguntaRepository;

    protected TransicionEstadoPreguntaTemplate(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    /**
     * Ejecuta la transición de estado. Método final: no puede sobreescribirse.
     *
     * @param preguntaId el ID de la pregunta a transicionar
     * @param usuarioId  el ID del usuario que ejecuta la acción
     */
    public final void ejecutar(int preguntaId, int usuarioId) {
        Pregunta pregunta = buscarPregunta(preguntaId);
        validarPrecondiciones(pregunta, usuarioId);
        ejecutarTransicion(pregunta, usuarioId);
        persistir(pregunta);
    }

    /** Paso invariante: busca la pregunta o lanza excepción si no existe. */
    private Pregunta buscarPregunta(int id) {
        return preguntaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("La pregunta no existe"));
    }

    /**
     * Paso variable: valida que la transición sea permitida para este usuario
     * y estado actual.
     *
     * @throws IllegalArgumentException si las precondiciones no se cumplen
     */
    protected abstract void validarPrecondiciones(Pregunta pregunta, int usuarioId);

    /** Paso variable: aplica el cambio de estado a la entidad en memoria. */
    protected abstract void ejecutarTransicion(Pregunta pregunta, int usuarioId);

    /** Paso invariante por defecto: persiste el nuevo estado de la pregunta. */
    protected void persistir(Pregunta pregunta) {
        preguntaRepository.actualizarEstado(pregunta.getId(), pregunta.getEstado());
    }
}