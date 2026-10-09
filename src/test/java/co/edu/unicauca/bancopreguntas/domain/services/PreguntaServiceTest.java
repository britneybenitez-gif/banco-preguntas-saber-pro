
package co.edu.unicauca.bancopreguntas.domain.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.PreguntaBuilder;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;

class PreguntaServiceTest {

    private PreguntaService preguntaService;
    private RepositorioSimulado repositorio;

    // Repositorio falso para probar sin SQLite
    private static class RepositorioSimulado
            implements PreguntaRepository {

        private Pregunta preguntaGuardada;
        private int cantidadGuardados = 0;

                @Override
        public void guardar(Pregunta pregunta) {
            this.preguntaGuardada = pregunta;
            cantidadGuardados++;
        }

        @Override
        public void actualizarEstado(int preguntaId, EstadoPregunta nuevoEstado) {
            if (preguntaGuardada != null && preguntaGuardada.getId() == preguntaId) {
                preguntaGuardada.setEstado(nuevoEstado);
            }
        }

        @Override
        public Optional<Pregunta> buscarPorId(int id) {
            if (preguntaGuardada != null && preguntaGuardada.getId() == id) {
                return Optional.of(preguntaGuardada);
            }
            return Optional.empty();
        }
    }

    @BeforeEach
    void configurar() {
        repositorio = new RepositorioSimulado();
        preguntaService = new PreguntaService(repositorio);
    }

    // Construye una pregunta válida para las pruebas
    private Pregunta crearPreguntaValida() {
        return new PreguntaBuilder()
            .conContexto("Una empresa desarrolla software educativo.")
            .conPreguntaDirecta(
                "¿Qué principio SOLID facilita la extensibilidad?"
            )
            .conDistractor1(
                "Principio de responsabilidad unica"
            )
            .conDistractor2(
                "Principio abierto y cerrado"
            )
            .conDistractor3(
                "Principio de sustitucion de Liskov"
            )
            .conDistractor4(
                "Principio de inversion de dependencias"
            )
            .conRespuestaCorrecta("B")
            .conJustificacion(
                "El principio abierto y cerrado permite extender comportamientos."
            )
            .conBibliografia(
                "Robert C. Martin, Clean Architecture"
            )
            .conCompetencia("Diseno de software")
            .conTema("Principios SOLID")
            .conSubtema("Open Closed Principle")
            .conNivelDificultad("Medio")
            .build();
    }

    @Test
    void debeRegistrarPreguntaValida() {
        Pregunta pregunta = crearPreguntaValida();

        preguntaService.crearPregunta(pregunta, 1);

        assertEquals(1, repositorio.cantidadGuardados);
        assertSame(pregunta, repositorio.preguntaGuardada);
        assertEquals(1, pregunta.getAutorId());
        assertEquals("B", pregunta.getRespuestaCorrecta());
    }

    @Test
    void noDebeRegistrarPreguntaNula() {
        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(null, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebeRegistrarPreguntaSinContexto() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setContexto("");

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebeRegistrarPreguntaSinJustificacion() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setJustificacion(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebeRegistrarPreguntaConRespuestaIncorrecta() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setRespuestaCorrecta("E");

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void debeNormalizarRespuestaCorrecta() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setRespuestaCorrecta("b");

        preguntaService.crearPregunta(pregunta, 1);

        assertEquals("B", pregunta.getRespuestaCorrecta());
        assertEquals(1, repositorio.cantidadGuardados);
    }

    @Test
    void noDebeRegistrarDistractoresDuplicados() {
        Pregunta pregunta = crearPreguntaValida();

        pregunta.setDistractor2(pregunta.getDistractor1());

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebeRegistrarDistractorMuyCorto() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setDistractor1("Corto");

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebePermitirTodasLasAnteriores() {
        Pregunta pregunta = crearPreguntaValida();

        pregunta.setDistractor1("Todas las anteriores");

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebePermitirNingunaDeLasAnteriores() {
        Pregunta pregunta = crearPreguntaValida();

        pregunta.setDistractor2("Ninguna de las anteriores");

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebePermitirDistractorIgualAlEnunciado() {
        Pregunta pregunta = crearPreguntaValida();

        pregunta.setDistractor1(pregunta.getPreguntaDirecta());

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebeRegistrarAutorInvalido() {
        Pregunta pregunta = crearPreguntaValida();

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 0)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    @Test
    void noDebeAceptarRepositorioNulo() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new PreguntaService(null)
        );
    }

        @Test
    void noDebeRegistrarDistractorNulo() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setDistractor1(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.crearPregunta(pregunta, 1)
        );

        assertEquals(0, repositorio.cantidadGuardados);
    }

    // ---------- HU-02: Enviar a revisión ----------

    @Test
    void enviarARevision_autorNoPropietario_lanzaExcepcion() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setId(100);
        pregunta.setAutorId(1);
        repositorio.guardar(pregunta);

        Exception e = assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.enviarARevision(100, 2)
        );

        assertEquals(
            "No tiene permisos para modificar el estado de esta pregunta",
            e.getMessage()
        );
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
    }

    @Test
    void enviarARevision_estadoNoBorrador_lanzaExcepcion() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setId(100);
        pregunta.setAutorId(1);
        pregunta.setEstado(EstadoPregunta.PENDIENTE_REVISION);
        repositorio.guardar(pregunta);

        Exception e = assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.enviarARevision(100, 1)
        );

        assertEquals(
            "Solo las preguntas en estado Borrador pueden enviarse a revisión",
            e.getMessage()
        );
    }

    @Test
    void enviarARevision_desdeBorrador_cambiaAPendiente() {
        Pregunta pregunta = crearPreguntaValida();
        pregunta.setId(100);
        pregunta.setAutorId(1);
        repositorio.guardar(pregunta);

        preguntaService.enviarARevision(100, 1);

        assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());
    }

    @Test
    void enviarARevision_preguntaInexistente_lanzaExcepcion() {
        Exception e = assertThrows(
            IllegalArgumentException.class,
            () -> preguntaService.enviarARevision(999, 1)
        );

        assertEquals("La pregunta no existe", e.getMessage());
    }

    @Test
    void crearPregunta_dejaEstadoEnBorrador() {
        Pregunta pregunta = crearPreguntaValida();

        preguntaService.crearPregunta(pregunta, 1);

        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
    }
}