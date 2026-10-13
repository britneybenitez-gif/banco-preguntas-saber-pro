package co.edu.unicauca.bancopreguntas.domain.entities;

/** Parámetros de paginación: página (base 1) y tamaño de página. */
public record SolicitudPagina(int pagina, int tamanio) {

    public SolicitudPagina {
        if (pagina < 1) {
            throw new IllegalArgumentException("La página debe ser mayor o igual a 1");
        }
        if (tamanio < 1) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor o igual a 1");
        }
    }

    public int desplazamiento() {
        return (pagina - 1) * tamanio;
    }
}
