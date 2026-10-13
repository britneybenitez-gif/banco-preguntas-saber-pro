package co.edu.unicauca.bancopreguntas.domain.repositories;

import java.util.List;

import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;

/** HU-04: persistencia de los revisores asignados a una pregunta. */
public interface AsignacionRevisorRepository {

    void asignarRevisores(int preguntaId, List<Usuario> revisores);
}
