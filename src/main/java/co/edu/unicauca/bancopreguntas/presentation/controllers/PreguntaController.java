package co.edu.unicauca.bancopreguntas.presentation.controllers;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.PreguntaBuilder;
import co.edu.unicauca.bancopreguntas.domain.services.PreguntaService;

public class PreguntaController {

    private final PreguntaService preguntaService;

    public PreguntaController(PreguntaService preguntaService) {
        if (preguntaService == null) {
            throw new IllegalArgumentException("El servicio de preguntas no puede ser nulo.");
        }
        this.preguntaService = preguntaService;
    }

    public void guardarPregunta(
            String contexto,
            String preguntaDirecta,
            String distractor1,
            String distractor2,
            String distractor3,
            String distractor4,
            String respuestaCorrecta,
            String justificacion,
            String bibliografia,
            String competencia,
            String tema,
            String subtema,
            String nivelDificultad,
            int autorId
    ) {
        try {
            // 1. Construir la entidad utilizando el Patrón Builder del dominio
            Pregunta nuevaPregunta = new PreguntaBuilder()
                    .conContexto(contexto)
                    .conPreguntaDirecta(preguntaDirecta)
                    .conDistractor1(distractor1)
                    .conDistractor2(distractor2)
                    .conDistractor3(distractor3)
                    .conDistractor4(distractor4)
                    .conRespuestaCorrecta(respuestaCorrecta)
                    .conJustificacion(justificacion)
                    .conBibliografia(bibliografia)
                    .conCompetencia(competencia)
                    .conTema(tema)
                    .conSubtema(subtema)
                    .conNivelDificultad(nivelDificultad)
                    .build();

            // 2. Invocar el método del servicio (este aplicará todas las reglas de negocio y lanzará excepciones si algo falla)
            preguntaService.crearPregunta(nuevaPregunta, autorId);

        } catch (IllegalArgumentException e) {
            // Re-lanzamos la excepción para que la vista (CrearPreguntaPanel) la capture y muestre el mensaje claro al usuario
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar la pregunta: " + e.getMessage());
        }
    }
}