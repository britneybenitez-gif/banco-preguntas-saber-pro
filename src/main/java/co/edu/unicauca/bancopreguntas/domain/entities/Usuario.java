package co.edu.unicauca.bancopreguntas.domain.entities;

/** Usuario del sistema (autor, revisor o administrador). */
public record Usuario(int id, String nombre, String email, Rol rol) {

    @Override
    public String toString() {
        return nombre + " (" + rol.getLabel() + ")";
    }
}
