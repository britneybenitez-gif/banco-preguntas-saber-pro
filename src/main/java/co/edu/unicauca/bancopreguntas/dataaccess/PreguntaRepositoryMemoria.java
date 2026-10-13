package co.edu.unicauca.bancopreguntas.dataaccess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.SolicitudPagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.repositories.AsignacionRevisorRepository;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaConsultaRepository;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;

/**
 * Implementación en memoria de los repositorios de preguntas. Reemplaza al stub
 * que tenía {@code App}; para persistir de verdad basta con otra implementación
 * de las mismas interfaces (por ejemplo, JDBC/SQLite).
 */
public class PreguntaRepositoryMemoria
        implements PreguntaRepository, PreguntaConsultaRepository, AsignacionRevisorRepository {

    private final Map<Integer, Pregunta> preguntas = new LinkedHashMap<>();
    private int siguienteId = 1;

    @Override
    public synchronized void guardar(Pregunta pregunta) {
        if (pregunta.getId() <= 0) {
            pregunta.setId(siguienteId++);
        }
        preguntas.put(pregunta.getId(), pregunta);
    }

    @Override
    public synchronized void actualizarEstado(int preguntaId, EstadoPregunta nuevoEstado) {
        Pregunta p = preguntas.get(preguntaId);
        if (p != null) {
            p.setEstado(nuevoEstado);
        }
    }

    @Override
    public synchronized Optional<Pregunta> buscarPorId(int id) {
        return Optional.ofNullable(preguntas.get(id));
    }

    @Override
    public synchronized Pagina<Pregunta> buscar(FiltroPregunta filtro, SolicitudPagina solicitud) {
        List<Pregunta> coincidencias = new ArrayList<>();
        for (Pregunta p : preguntas.values()) {
            if (filtro.coincide(p)) {
                coincidencias.add(p);
            }
        }
        coincidencias.sort(Comparator.comparingInt(Pregunta::getId).reversed());

        int desde = Math.min(solicitud.desplazamiento(), coincidencias.size());
        int hasta = Math.min(desde + solicitud.tamanio(), coincidencias.size());
        return new Pagina<>(coincidencias.subList(desde, hasta),
                solicitud.pagina(), solicitud.tamanio(), coincidencias.size());
    }

    @Override
    public synchronized void asignarRevisores(int preguntaId, List<Usuario> revisores) {
        Pregunta p = preguntas.get(preguntaId);
        if (p != null) {
            p.setRevisores(revisores);
        }
    }
}
