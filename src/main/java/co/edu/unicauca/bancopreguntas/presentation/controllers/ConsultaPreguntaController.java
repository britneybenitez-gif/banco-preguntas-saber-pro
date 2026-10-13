package co.edu.unicauca.bancopreguntas.presentation.controllers;

import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.SolicitudPagina;
import co.edu.unicauca.bancopreguntas.domain.services.ConsultaPreguntaService;

/** HU-03: conecta la pantalla "Mis preguntas" con el servicio de consulta. */
public class ConsultaPreguntaController {

    private final ConsultaPreguntaService servicio;
    private final int autorId;

    public ConsultaPreguntaController(ConsultaPreguntaService servicio, int autorId) {
        this.servicio = servicio;
        this.autorId = autorId;
    }

    public Pagina<Pregunta> listar(FiltroPregunta filtro, int pagina, int tamanio) {
        return servicio.listarDeAutor(autorId, filtro, new SolicitudPagina(pagina, tamanio));
    }
}
