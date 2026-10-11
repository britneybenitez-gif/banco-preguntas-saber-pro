package co.edu.unicauca.bancopreguntas.domain.repositories;

import java.util.Optional;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import java.util.List;

public interface PreguntaRepository {

    void guardar(Pregunta pregunta);

    void actualizarEstado(int preguntaId, EstadoPregunta nuevoEstado);

    Optional<Pregunta> buscarPorId(int id);

    List<Pregunta> listarPorAutor(int autorId, List<EstadoPregunta> estadosFiltro, String nivelDificultad, int offset, int limit);

    int contarPorAutor(int autorId, List<EstadoPregunta> estadosFiltro, String nivelDificultad);
}