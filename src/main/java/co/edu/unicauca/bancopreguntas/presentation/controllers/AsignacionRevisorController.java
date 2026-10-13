package co.edu.unicauca.bancopreguntas.presentation.controllers;

import java.util.List;

import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.SolicitudPagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.services.AsignacionRevisorService;
import co.edu.unicauca.bancopreguntas.domain.services.AsignacionRevisorService.ResultadoAsignacion;

/** HU-04: conecta la pantalla del administrador con la asignación de revisores. */
public class AsignacionRevisorController {

    private final AsignacionRevisorService servicio;
    private final int administradorId;

    public AsignacionRevisorController(AsignacionRevisorService servicio, int administradorId) {
        this.servicio = servicio;
        this.administradorId = administradorId;
    }

    public Pagina<Pregunta> listarPendientes(FiltroPregunta filtro, int pagina, int tamanio) {
        return servicio.listarPendientes(administradorId, filtro, new SolicitudPagina(pagina, tamanio));
    }

    public List<Usuario> listarRevisores() {
        return servicio.listarRevisores();
    }

    public ResultadoAsignacion asignar(int preguntaId, List<Integer> revisorIds) {
        return servicio.asignarRevisores(preguntaId, administradorId, revisorIds);
    }
}
