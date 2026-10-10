package co.edu.unicauca.bancopreguntas.domain.repositories;

import java.util.Optional;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;

public interface PreguntaRepository {

    void guardar(Pregunta pregunta);

    void actualizarEstado(int preguntaId, EstadoPregunta nuevoEstado);

    Optional<Pregunta> buscarPorId(int id);

}