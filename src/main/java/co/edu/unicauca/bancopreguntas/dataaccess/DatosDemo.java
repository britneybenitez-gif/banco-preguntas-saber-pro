package co.edu.unicauca.bancopreguntas.dataaccess;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.PreguntaBuilder;
import co.edu.unicauca.bancopreguntas.domain.services.PreguntaService;

/**
 * Carga preguntas de ejemplo para poder probar paginación, filtros y estados
 * sin tener que crearlas a mano. Pasa por {@link PreguntaService}, así que
 * cumplen la misma validación estructural que las preguntas reales.
 * Se puede quitar la llamada en {@code App} sin afectar nada más.
 */
public final class DatosDemo {

    private static final String[][] TEMAS = {
        {"Razonamiento cuantitativo", "Álgebra", "Ecuaciones lineales"},
        {"Lectura crítica", "Comprensión", "Inferencia de ideas"},
        {"Ingeniería de software", "Diseño", "Patrones de diseño"},
        {"Comunicación escrita", "Redacción", "Cohesión textual"},
        {"Inglés", "Gramática", "Tiempos verbales"},
        {"Competencias ciudadanas", "Constitución", "Derechos fundamentales"}
    };
    private static final String[] NIVELES = {"BAJA", "MEDIA", "ALTA"};

    private DatosDemo() { }

    public static void cargar(PreguntaService service, int autorUno, int autorDos) {
        for (int i = 0; i < 14; i++) {
            String[] t = TEMAS[i % TEMAS.length];
            int n = i + 1;
            Pregunta p = new PreguntaBuilder()
                    .conContexto("Contexto de ejemplo " + n + " sobre " + t[1].toLowerCase()
                            + " en un escenario académico.")
                    .conPreguntaDirecta("¿Cuál es la mejor respuesta para el caso " + n + " de " + t[2] + "?")
                    .conDistractor1("Primera alternativa plausible del caso " + n)
                    .conDistractor2("Segunda alternativa plausible del caso " + n)
                    .conDistractor3("Tercera alternativa plausible del caso " + n)
                    .conDistractor4("Cuarta alternativa plausible del caso " + n)
                    .conRespuestaCorrecta(String.valueOf((char) ('A' + (i % 4))))
                    .conJustificacion("La opción correcta aplica el concepto de " + t[2] + ".")
                    .conBibliografia("Autor, A. (2024). Manual de " + t[1] + ". Editorial Ejemplo.")
                    .conCompetencia(t[0])
                    .conTema(t[1])
                    .conSubtema(t[2])
                    .conNivelDificultad(NIVELES[i % NIVELES.length])
                    .build();
            int autor = (i % 5 == 4) ? autorDos : autorUno;
            service.crearPregunta(p, autor);
            // Las preguntas con id par y de un mismo autor se envían a revisión
            if (i % 2 == 1) {
                service.enviarARevision(p.getId(), autor);
            }
        }
    }
}
