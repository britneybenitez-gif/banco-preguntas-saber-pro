package co.edu.unicauca.bancopreguntas.domain.repositories;

import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.SolicitudPagina;

/** HU-03 / HU-04: consulta paginada y filtrada de preguntas (más recientes primero). */
public interface PreguntaConsultaRepository {

    Pagina<Pregunta> buscar(FiltroPregunta filtro, SolicitudPagina solicitud);
}
