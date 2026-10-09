
package co.edu.unicauca.bancopreguntas.domain.services;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PreguntaService {

    private final PreguntaRepository preguntaRepository;

    private static final int LONGITUD_MINIMA_DISTRACTOR = 10;

    // Inyección de dependencias
    public PreguntaService(PreguntaRepository preguntaRepository) {
        if (preguntaRepository == null) {
            throw new IllegalArgumentException(
                "El repositorio de preguntas es obligatorio"
            );
        }

        this.preguntaRepository = preguntaRepository;
    }

    // HU-01: Registrar una pregunta
    public void crearPregunta(Pregunta pregunta, int autorId) {

        if (autorId <= 0) {
            throw new IllegalArgumentException(
                "El autor debe ser valido"
            );
        }

        validarPregunta(pregunta);

        pregunta.setAutorId(autorId);

        preguntaRepository.guardar(pregunta);
    }

    // Validaciones estructurales
    private void validarPregunta(Pregunta pregunta) {

        if (pregunta == null) {
            throw new IllegalArgumentException(
                "La pregunta no puede ser nula"
            );
        }

        // Campos obligatorios
        validarObligatorio(
            pregunta.getContexto(), "Contexto"
        );

        validarObligatorio(
            pregunta.getPreguntaDirecta(), "Pregunta directa"
        );

        List<String> distractores = Arrays.asList(
            pregunta.getDistractor1(),
            pregunta.getDistractor2(),
            pregunta.getDistractor3(),
            pregunta.getDistractor4()
        );

        // Validar los cuatro distractores
        for (int i = 0; i < distractores.size(); i++) {
            validarObligatorio(
                distractores.get(i),
                "Distractor " + (i + 1)
            );
        }

        validarObligatorio(
            pregunta.getRespuestaCorrecta(),
            "Respuesta correcta"
        );

        // La respuesta correcta debe ser A, B, C o D
        String respuesta = pregunta.getRespuestaCorrecta()
                                   .trim()
                                   .toUpperCase(Locale.ROOT);

        if (!List.of("A", "B", "C", "D").contains(respuesta)) {
            throw new IllegalArgumentException(
                "La respuesta correcta debe ser A, B, C o D"
            );
        }

        pregunta.setRespuestaCorrecta(respuesta);

        validarObligatorio(
            pregunta.getJustificacion(), "Justificacion"
        );

        validarObligatorio(
            pregunta.getBibliografia(), "Bibliografia"
        );

        validarObligatorio(
            pregunta.getCompetencia(), "Competencia"
        );

        validarObligatorio(
            pregunta.getTema(), "Tema"
        );

        validarObligatorio(
            pregunta.getSubtema(), "Subtema"
        );

        validarObligatorio(
            pregunta.getNivelDificultad(), "Nivel de dificultad"
        );

        // Validar expresiones prohibidas
        validarExpresionesProhibidas(
            pregunta.getPreguntaDirecta()
        );

        for (String distractor : distractores) {
            validarExpresionesProhibidas(distractor);
        }

        // Validar longitud y estructura de distractores
        for (String distractor : distractores) {
            validarEstructuraDistractor(distractor);
        }

        // Evitar distractores repetidos
        validarDistractoresDuplicados(distractores);

        // Evitar que un distractor sea igual a la pregunta
        for (String distractor : distractores) {
            if (normalizar(distractor).equals(
                    normalizar(pregunta.getPreguntaDirecta()))) {

                throw new IllegalArgumentException(
                    "Un distractor no puede ser igual a la pregunta directa"
                );
            }
        }
    }

    // Comprobar que un campo tenga contenido
    private void validarObligatorio(String valor, String campo) {

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                "El campo " + campo + " es obligatorio"
            );
        }
    }

    // Normalizar texto para comparaciones
    private String normalizar(String texto) {

        String normalizado = Normalizer.normalize(
            texto.trim().toLowerCase(Locale.ROOT),
            Normalizer.Form.NFD
        );

        return normalizado.replaceAll("\\p{M}+", "");
    }

    // Validar expresiones no permitidas
    private void validarExpresionesProhibidas(String texto) {

        String normalizado = normalizar(texto);

        if (normalizado.contains("todas las anteriores") ||
            normalizado.contains("ninguna de las anteriores")) {

            throw new IllegalArgumentException(
                "No se permiten expresiones como " +
                "'Todas las anteriores' o 'Ninguna de las anteriores'"
            );
        }
    }

    // Validar longitud y estructura mínima
    private void validarEstructuraDistractor(String distractor) {

        String texto = distractor.trim();

        if (texto.length() < LONGITUD_MINIMA_DISTRACTOR) {
            throw new IllegalArgumentException(
                "Cada distractor debe tener al menos " +
                LONGITUD_MINIMA_DISTRACTOR + " caracteres"
            );
        }

        boolean palabraValida = Arrays.stream(texto.split("\\s+"))
            .anyMatch(palabra -> palabra.length() > 2);

        if (!palabraValida) {
            throw new IllegalArgumentException(
                "El distractor debe contener una palabra significativa"
            );
        }
    }

    // Validar que los distractores sean diferentes
    private void validarDistractoresDuplicados(
            List<String> distractores) {

        Set<String> valoresUnicos = new HashSet<>();

        for (String distractor : distractores) {

            String normalizado = normalizar(distractor);

            if (!valoresUnicos.add(normalizado)) {
                throw new IllegalArgumentException(
                    "Los distractores no pueden estar duplicados"
                );
            }
        }
    }
}
