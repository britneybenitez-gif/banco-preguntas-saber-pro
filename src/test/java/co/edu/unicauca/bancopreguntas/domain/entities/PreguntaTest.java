
package co.edu.unicauca.bancopreguntas.domain.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PreguntaTest {

    @Test
    void debeGuardarDatosDePregunta() {

        Pregunta pregunta = new Pregunta();

        pregunta.setId(1);
        pregunta.setContexto("Contexto de ejemplo");
        pregunta.setPreguntaDirecta("Pregunta de ejemplo");
        pregunta.setDistractor1("Primera opcion");
        pregunta.setDistractor2("Segunda opcion");
        pregunta.setDistractor3("Tercera opcion");
        pregunta.setDistractor4("Cuarta opcion");
        pregunta.setRespuestaCorrecta("B");
        pregunta.setJustificacion("Justificacion de ejemplo");
        pregunta.setBibliografia("Bibliografia de ejemplo");
        pregunta.setCompetencia("Competencia generica");
        pregunta.setTema("Ingenieria de software");
        pregunta.setSubtema("Principios SOLID");
        pregunta.setNivelDificultad("Medio");
        pregunta.setAutorId(1);

        assertAll(
            () -> assertEquals(1, pregunta.getId()),
            () -> assertEquals("Contexto de ejemplo",
                    pregunta.getContexto()),
            () -> assertEquals("Pregunta de ejemplo",
                    pregunta.getPreguntaDirecta()),
            () -> assertEquals("Primera opcion",
                    pregunta.getDistractor1()),
            () -> assertEquals("Segunda opcion",
                    pregunta.getDistractor2()),
            () -> assertEquals("Tercera opcion",
                    pregunta.getDistractor3()),
            () -> assertEquals("Cuarta opcion",
                    pregunta.getDistractor4()),
            () -> assertEquals("B",
                    pregunta.getRespuestaCorrecta()),
            () -> assertEquals("Justificacion de ejemplo",
                    pregunta.getJustificacion()),
            () -> assertEquals("Bibliografia de ejemplo",
                    pregunta.getBibliografia()),
            () -> assertEquals("Competencia generica",
                    pregunta.getCompetencia()),
            () -> assertEquals("Ingenieria de software",
                    pregunta.getTema()),
            () -> assertEquals("Principios SOLID",
                    pregunta.getSubtema()),
            () -> assertEquals("Medio",
                    pregunta.getNivelDificultad()),
            () -> assertEquals(1, pregunta.getAutorId())
        );
    }
    
    @Test
    void debeGuardarEstadoDePregunta() {
        Pregunta pregunta = new Pregunta();

        pregunta.setEstado(EstadoPregunta.PENDIENTE_REVISION);

        assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());
    }
}
