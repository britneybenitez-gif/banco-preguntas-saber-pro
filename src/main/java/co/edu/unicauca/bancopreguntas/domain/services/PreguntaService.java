
package co.edu.unicauca.bancopreguntas.domain.services;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;

import java.util.List;

public class PreguntaService {

    private final PreguntaRepository preguntaRepository;

    public PreguntaService(PreguntaRepository preguntaRepository) {
        if (preguntaRepository == null) {
            throw new IllegalArgumentException(
                "El repositorio de preguntas es obligatorio"
            );
        }
        this.preguntaRepository = preguntaRepository;
    }

    public void crearPregunta(Pregunta pregunta, int autorId) {
        validarPregunta(pregunta);

        if (autorId <= 0) {
            throw new IllegalArgumentException(
                "El autor debe ser valido"
            );
        }

        pregunta.setAutorId(autorId);
        preguntaRepository.guardar(pregunta);
    }

    private void validarPregunta(Pregunta pregunta) {

        if (pregunta == null) {
            throw new IllegalArgumentException(
                "La pregunta no puede ser nula"
            );
        }

        validarObligatorio(pregunta.getContexto(), "Contexto");
        validarObligatorio(
            pregunta.getPreguntaDirecta(), "Pregunta directa"
        );

        List<String> distractores = List.of(
            pregunta.getDistractor1(),
            pregunta.getDistractor2(),
            pregunta.getDistractor3(),
            pregunta.getDistractor4()
        );

        for (int i = 0; i < distractores.size(); i++) {
            validarObligatorio(
                distractores.get(i), "Distractor " + (i + 1)
            );
        }

        validarObligatorio(
            pregunta.getRespuestaCorrecta(), "Respuesta correcta"
        );

        String respuesta = pregunta.getRespuestaCorrecta().trim();

        if (!List.of("A", "B", "C", "D").contains(respuesta)) {
            throw new IllegalArgumentException(
                "La respuesta correcta debe ser A, B, C o D"
            );
        }

        validarObligatorio(
            pregunta.getJustificacion(), "Justificacion"
        );
        validarObligatorio(
            pregunta.getBibliografia(), "Bibliografia"
        );
        validarObligatorio(
            pregunta.getCompetencia(), "Competencia"
        );
        validarObligatorio(pregunta.getTema(), "Tema");
        validarObligatorio(pregunta.getSubtema(), "Subtema");
        validarObligatorio(
            pregunta.getNivelDificultad(), "Nivel de dificultad"
        );
    }

    private void validarObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                "El campo " + campo + " es obligatorio"
            );
        }
    }
}
