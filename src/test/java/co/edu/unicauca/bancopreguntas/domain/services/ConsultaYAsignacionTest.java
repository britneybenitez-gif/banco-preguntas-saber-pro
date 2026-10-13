package co.edu.unicauca.bancopreguntas.domain.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unicauca.bancopreguntas.dataaccess.PreguntaRepositoryMemoria;
import co.edu.unicauca.bancopreguntas.dataaccess.UsuarioRepositoryMemoria;
import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.PreguntaBuilder;
import co.edu.unicauca.bancopreguntas.domain.entities.SolicitudPagina;
import co.edu.unicauca.bancopreguntas.domain.services.AsignacionRevisorService.ResultadoAsignacion;

/** Pruebas de HU-03 (listado paginado y filtrado) y HU-04 (asignación de revisores). */
class ConsultaYAsignacionTest {

    private static final int AUTOR = 1;
    private static final int OTRO_AUTOR = 6;
    private static final int ADMIN = 2;

    private PreguntaRepositoryMemoria repo;
    private PreguntaService preguntaService;
    private ConsultaPreguntaService consulta;
    private AsignacionRevisorService asignacion;
    private List<String> emails;
    private boolean fallaEmail;

    @BeforeEach
    void configurar() {
        repo = new PreguntaRepositoryMemoria();
        emails = new ArrayList<>();
        fallaEmail = false;
        NotificadorEmail notificador = (dest, asunto, cuerpo) -> {
            if (fallaEmail) {
                throw new NotificacionException("SMTP caído");
            }
            emails.add(dest);
        };
        preguntaService = new PreguntaService(repo);
        consulta = new ConsultaPreguntaService(repo);
        asignacion = new AsignacionRevisorService(
                repo, repo, repo, new UsuarioRepositoryMemoria(), notificador);
    }

    private Pregunta crear(int autor, int n, String tema, String dificultad) {
        Pregunta p = new PreguntaBuilder()
                .conContexto("Contexto " + n)
                .conPreguntaDirecta("¿Pregunta número " + n + "?")
                .conDistractor1("Alternativa uno del caso " + n)
                .conDistractor2("Alternativa dos del caso " + n)
                .conDistractor3("Alternativa tres del caso " + n)
                .conDistractor4("Alternativa cuatro del caso " + n)
                .conRespuestaCorrecta("A")
                .conJustificacion("Justificación " + n)
                .conBibliografia("Bibliografía " + n)
                .conCompetencia("Competencia X")
                .conTema(tema)
                .conSubtema("Subtema " + n)
                .conNivelDificultad(dificultad)
                .build();
        preguntaService.crearPregunta(p, autor);
        return p;
    }

    // ---------- HU-03 ----------

    @Test
    void listaSoloLasPreguntasDelAutorConPaginacion() {
        for (int i = 1; i <= 12; i++) {
            crear(AUTOR, i, "Álgebra", "MEDIA");
        }
        crear(OTRO_AUTOR, 99, "Álgebra", "MEDIA");

        Pagina<Pregunta> p1 = consulta.listarDeAutor(AUTOR, null, new SolicitudPagina(1, 5));
        Pagina<Pregunta> p3 = consulta.listarDeAutor(AUTOR, null, new SolicitudPagina(3, 5));

        assertEquals(12, p1.totalElementos());
        assertEquals(3, p1.totalPaginas());
        assertEquals(5, p1.elementos().size());
        assertEquals(2, p3.elementos().size());
        assertTrue(p1.elementos().stream().allMatch(p -> p.getAutorId() == AUTOR));
        // Más recientes primero
        assertTrue(p1.elementos().get(0).getId() > p1.elementos().get(1).getId());
    }

    @Test
    void elFiltroNoPermiteVerPreguntasDeOtroAutor() {
        crear(OTRO_AUTOR, 1, "Álgebra", "BAJA");
        FiltroPregunta intento = FiltroPregunta.vacio().conAutor(OTRO_AUTOR);

        Pagina<Pregunta> res = consulta.listarDeAutor(AUTOR, intento, new SolicitudPagina(1, 10));

        assertEquals(0, res.totalElementos());
    }

    @Test
    void filtraPorEstadoDificultadTemaYTexto() {
        Pregunta a = crear(AUTOR, 1, "Álgebra", "BAJA");
        crear(AUTOR, 2, "Geometría", "ALTA");
        preguntaService.enviarARevision(a.getId(), AUTOR);
        SolicitudPagina todas = new SolicitudPagina(1, 10);

        assertEquals(1, consulta.listarDeAutor(AUTOR,
                FiltroPregunta.vacio().conEstado(EstadoPregunta.PENDIENTE_REVISION), todas).totalElementos());
        assertEquals(1, consulta.listarDeAutor(AUTOR,
                new FiltroPregunta(null, null, "ALTA", null, null, null), todas).totalElementos());
        // sin tildes ni mayúsculas
        assertEquals(1, consulta.listarDeAutor(AUTOR,
                new FiltroPregunta(null, null, null, null, "algebra", null), todas).totalElementos());
        assertEquals(1, consulta.listarDeAutor(AUTOR,
                new FiltroPregunta(null, null, null, null, null, "NÚMERO 2"), todas).totalElementos());
    }

    @Test
    void siLaPaginaSeSaleDeRangoDevuelveLaUltima() {
        for (int i = 1; i <= 6; i++) {
            crear(AUTOR, i, "Álgebra", "MEDIA");
        }
        Pagina<Pregunta> res = consulta.listarDeAutor(AUTOR, null, new SolicitudPagina(9, 5));

        assertEquals(2, res.pagina());
        assertEquals(1, res.elementos().size());
    }

    // ---------- HU-04 ----------

    @Test
    void asignaRevisoresCambiaEstadoYNotificaPorEmail() {
        Pregunta p = crear(AUTOR, 1, "Álgebra", "MEDIA");
        preguntaService.enviarARevision(p.getId(), AUTOR);

        ResultadoAsignacion r = asignacion.asignarRevisores(p.getId(), ADMIN, List.of(3, 4));

        assertEquals(EstadoPregunta.EN_REVISION, repo.buscarPorId(p.getId()).orElseThrow().getEstado());
        assertEquals(2, repo.buscarPorId(p.getId()).orElseThrow().getRevisores().size());
        assertEquals(List.of("carlos.gomez@example.com", "laura.ramirez@example.com"), emails);
        assertEquals(2, r.notificados().size());
        assertTrue(r.sinNotificar().isEmpty());
    }

    @Test
    void exigeAlMenosUnRevisor() {
        Pregunta p = crear(AUTOR, 1, "Álgebra", "MEDIA");
        preguntaService.enviarARevision(p.getId(), AUTOR);

        assertThrows(IllegalArgumentException.class,
                () -> asignacion.asignarRevisores(p.getId(), ADMIN, List.of()));
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, repo.buscarPorId(p.getId()).orElseThrow().getEstado());
        assertTrue(emails.isEmpty());
    }

    @Test
    void soloSePuedeAsignarSiEstaPendienteDeRevision() {
        Pregunta borrador = crear(AUTOR, 1, "Álgebra", "MEDIA");

        assertThrows(IllegalArgumentException.class,
                () -> asignacion.asignarRevisores(borrador.getId(), ADMIN, List.of(3)));
        assertTrue(emails.isEmpty());
    }

    @Test
    void soloUnAdministradorPuedeAsignarYSoloUsuariosRevisores() {
        Pregunta p = crear(AUTOR, 1, "Álgebra", "MEDIA");
        preguntaService.enviarARevision(p.getId(), AUTOR);

        assertThrows(IllegalArgumentException.class,
                () -> asignacion.asignarRevisores(p.getId(), AUTOR, List.of(3)));
        // el usuario 1 es autor, no revisor
        assertThrows(IllegalArgumentException.class,
                () -> asignacion.asignarRevisores(p.getId(), ADMIN, List.of(1)));
        assertThrows(IllegalArgumentException.class,
                () -> asignacion.asignarRevisores(p.getId(), ADMIN, List.of(999)));
    }

    @Test
    void siFallaElEmailLaAsignacionSeMantieneYSeInforma() {
        Pregunta p = crear(AUTOR, 1, "Álgebra", "MEDIA");
        preguntaService.enviarARevision(p.getId(), AUTOR);
        fallaEmail = true;

        ResultadoAsignacion r = asignacion.asignarRevisores(p.getId(), ADMIN, List.of(3));

        assertEquals(EstadoPregunta.EN_REVISION, repo.buscarPorId(p.getId()).orElseThrow().getEstado());
        assertEquals(1, r.sinNotificar().size());
        assertTrue(r.notificados().isEmpty());
    }

    @Test
    void listaSoloPendientesDeTodosLosAutores() {
        Pregunta a = crear(AUTOR, 1, "Álgebra", "MEDIA");
        Pregunta b = crear(OTRO_AUTOR, 2, "Álgebra", "MEDIA");
        crear(AUTOR, 3, "Álgebra", "MEDIA"); // queda en borrador
        preguntaService.enviarARevision(a.getId(), AUTOR);
        preguntaService.enviarARevision(b.getId(), OTRO_AUTOR);

        Pagina<Pregunta> res = asignacion.listarPendientes(ADMIN, null, new SolicitudPagina(1, 10));

        assertEquals(2, res.totalElementos());
        assertThrows(IllegalArgumentException.class,
                () -> asignacion.listarPendientes(AUTOR, null, new SolicitudPagina(1, 10)));
    }
}
