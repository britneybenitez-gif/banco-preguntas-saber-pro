package co.edu.unicauca.bancopreguntas.domain.repositories;

public interface AsignacionRevisorRepository {
    boolean asignarRevisor(int preguntaId, String correoRevisor);
    boolean existeAsignacion(int preguntaId, String correoRevisor);
}