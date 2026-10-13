package co.edu.unicauca.bancopreguntas.domain.services;

import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.SolicitudPagina;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaConsultaRepository;

/** HU-03: listado paginado y filtrado de las preguntas de un autor. */
public class ConsultaPreguntaService {

    private final PreguntaConsultaRepository consultaRepository;

    public ConsultaPreguntaService(PreguntaConsultaRepository consultaRepository) {
        if (consultaRepository == null) {
            throw new IllegalArgumentException("El repositorio de consulta es obligatorio");
        }
        this.consultaRepository = consultaRepository;
    }

    /**
     * Lista solo las preguntas del autor indicado (el filtro nunca puede ampliar
     * el alcance a preguntas de otros autores).
     */
    public Pagina<Pregunta> listarDeAutor(int autorId, FiltroPregunta filtro, SolicitudPagina solicitud) {
        if (autorId <= 0) {
            throw new IllegalArgumentException("El autor debe ser valido");
        }
        FiltroPregunta f = (filtro == null ? FiltroPregunta.vacio() : filtro).conAutor(autorId);
        return buscarAjustandoPagina(f, solicitud);
    }

    /** Si la página pedida quedó fuera de rango (p. ej. tras filtrar), devuelve la última. */
    static Pagina<Pregunta> buscarAjustandoPagina(PreguntaConsultaRepository repo,
                                                  FiltroPregunta filtro, SolicitudPagina solicitud) {
        Pagina<Pregunta> resultado = repo.buscar(filtro, solicitud);
        if (resultado.elementos().isEmpty() && resultado.totalElementos() > 0) {
            SolicitudPagina ultima = new SolicitudPagina(resultado.totalPaginas(), solicitud.tamanio());
            return repo.buscar(filtro, ultima);
        }
        return resultado;
    }

    private Pagina<Pregunta> buscarAjustandoPagina(FiltroPregunta filtro, SolicitudPagina solicitud) {
        return buscarAjustandoPagina(consultaRepository, filtro, solicitud);
    }
}
