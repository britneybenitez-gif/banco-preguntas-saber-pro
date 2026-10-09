
package co.edu.unicauca.bancopreguntas.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PreguntaBuilderTest {

    @Test
    void debeConstruirPreguntaConBuilder() {

        Pregunta pregunta = new PreguntaBuilder()
            .conContexto("Contexto de ejemplo")
            .conPreguntaDirecta("Pregunta de ejemplo")
            .conDistractor1("Primera alternativa")
            .conDistractor2("Segunda alternativa")
            .conDistractor3("Tercera alternativa")
            .conDistractor4("Cuarta alternativa")
            .conRespuestaCorrecta("B")
            .conJustificacion("Justificacion de ejemplo")
            .conBibliografia("Libro de referencia")
            .conCompetencia("Diseno de software")
            .conTema("SOLID")
            .conSubtema("Responsabilidad unica")
            .conNivelDificultad("Medio")
            .conAutorId(1)
            .build();

        assertAll(
            () -> assertNotNull(pregunta),
            () -> assertEquals("Contexto de ejemplo",
                    pregunta.getContexto()),
            () -> assertEquals("Pregunta de ejemplo",
                    pregunta.getPreguntaDirecta()),
            () -> assertEquals("Primera alternativa",
                    pregunta.getDistractor1()),
            () -> assertEquals("Segunda alternativa",
                    pregunta.getDistractor2()),
            () -> assertEquals("Tercera alternativa",
                    pregunta.getDistractor3()),
            () -> assertEquals("Cuarta alternativa",
                    pregunta.getDistractor4()),
            () -> assertEquals("B",
                    pregunta.getRespuestaCorrecta()),
            () -> assertEquals("Justificacion de ejemplo",
                    pregunta.getJustificacion()),
            () -> assertEquals("Libro de referencia",
                    pregunta.getBibliografia()),
            () -> assertEquals("Diseno de software",
                    pregunta.getCompetencia()),
            () -> assertEquals("SOLID",
                    pregunta.getTema()),
            () -> assertEquals("Responsabilidad unica",
                    pregunta.getSubtema()),
            () -> assertEquals("Medio",
                    pregunta.getNivelDificultad()),
            () -> assertEquals(1, pregunta.getAutorId())
        );
    }
}
