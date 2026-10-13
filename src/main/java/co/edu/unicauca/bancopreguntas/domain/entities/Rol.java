package co.edu.unicauca.bancopreguntas.domain.entities;

/** Roles de usuario del banco de preguntas. */
public enum Rol {
    AUTOR("Autor"),
    REVISOR("Revisor"),
    ADMINISTRADOR("Administrador");

    private final String label;

    Rol(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
