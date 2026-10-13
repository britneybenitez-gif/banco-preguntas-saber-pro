package co.edu.unicauca.bancopreguntas.domain.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Rol;
import co.edu.unicauca.bancopreguntas.domain.entities.SolicitudPagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.repositories.AsignacionRevisorRepository;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaConsultaRepository;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;
import co.edu.unicauca.bancopreguntas.domain.repositories.UsuarioRepository;

/**
 * HU-04: el administrador asigna al menos un revisor a las preguntas en
 * "Pendiente de revisión" y el sistema notifica a cada revisor por email.
 */
public class AsignacionRevisorService {

    /** Resultado de la asignación: a quiénes se les pudo (o no) notificar. */
    public record ResultadoAsignacion(Pregunta pregunta,
                                      List<Usuario> notificados,
                                      List<Usuario> sinNotificar) { }

    private final PreguntaRepository preguntaRepository;
    private final PreguntaConsultaRepository consultaRepository;
    private final AsignacionRevisorRepository asignacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificadorEmail notificador;

    public AsignacionRevisorService(PreguntaRepository preguntaRepository,
                                    PreguntaConsultaRepository consultaRepository,
                                    AsignacionRevisorRepository asignacionRepository,
                                    UsuarioRepository usuarioRepository,
                                    NotificadorEmail notificador) {
        if (preguntaRepository == null || consultaRepository == null || asignacionRepository == null
                || usuarioRepository == null || notificador == null) {
            throw new IllegalArgumentException("Todas las dependencias son obligatorias");
        }
        this.preguntaRepository = preguntaRepository;
        this.consultaRepository = consultaRepository;
        this.asignacionRepository = asignacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificador = notificador;
    }

    /** Preguntas en estado "Pendiente de revisión" de todos los autores. */
    public Pagina<Pregunta> listarPendientes(int administradorId, FiltroPregunta filtro,
                                             SolicitudPagina solicitud) {
        exigirAdministrador(administradorId);
        FiltroPregunta f = (filtro == null ? FiltroPregunta.vacio() : filtro)
                .conAutor(null)
                .conEstado(EstadoPregunta.PENDIENTE_REVISION);
        return ConsultaPreguntaService.buscarAjustandoPagina(consultaRepository, f, solicitud);
    }

    public List<Usuario> listarRevisores() {
        return usuarioRepository.listarPorRol(Rol.REVISOR);
    }

    public ResultadoAsignacion asignarRevisores(int preguntaId, int administradorId,
                                                List<Integer> revisorIds) {
        exigirAdministrador(administradorId);

        if (revisorIds == null || revisorIds.isEmpty()) {
            throw new IllegalArgumentException("Debe asignar al menos un revisor");
        }

        List<Usuario> revisores = new ArrayList<>();
        Set<Integer> sinRepetir = new LinkedHashSet<>(revisorIds);
        for (int id : sinRepetir) {
            Usuario u = usuarioRepository.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("El revisor " + id + " no existe"));
            if (u.rol() != Rol.REVISOR) {
                throw new IllegalArgumentException(u.nombre() + " no tiene el rol de Revisor");
            }
            revisores.add(u);
        }

        // Reutiliza el Template Method: buscar → validar → ejecutar → persistir
        new AsignarRevisoresTransicion(preguntaRepository, asignacionRepository, revisores)
                .ejecutar(preguntaId, administradorId);

        Pregunta pregunta = preguntaRepository.buscarPorId(preguntaId).orElseThrow();
        return notificarRevisores(pregunta, revisores);
    }

    private ResultadoAsignacion notificarRevisores(Pregunta pregunta, List<Usuario> revisores) {
        List<Usuario> ok = new ArrayList<>();
        List<Usuario> fallidos = new ArrayList<>();
        for (Usuario r : revisores) {
            try {
                notificador.enviar(r.email(),
                        "Nueva pregunta asignada para revisión (#" + pregunta.getId() + ")",
                        construirCuerpo(r, pregunta));
                ok.add(r);
            } catch (NotificacionException e) {
                // La asignación ya quedó guardada: un fallo de email no la revierte.
                fallidos.add(r);
            }
        }
        return new ResultadoAsignacion(pregunta, ok, fallidos);
    }

    private String construirCuerpo(Usuario revisor, Pregunta p) {
        return "Hola " + revisor.nombre() + ",\n\n"
                + "Se te asignó la pregunta #" + p.getId() + " para revisión.\n"
                + "Competencia: " + p.getCompetencia() + "\n"
                + "Tema: " + p.getTema() + " / " + p.getSubtema() + "\n"
                + "Nivel de dificultad: " + p.getNivelDificultad() + "\n\n"
                + "Ingresa al Banco de Preguntas Saber Pro para revisarla.";
    }

    private void exigirAdministrador(int administradorId) {
        Usuario u = usuarioRepository.buscarPorId(administradorId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        if (u.rol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo un administrador puede asignar revisores");
        }
    }
}
