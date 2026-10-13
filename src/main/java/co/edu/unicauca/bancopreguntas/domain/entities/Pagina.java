package co.edu.unicauca.bancopreguntas.domain.entities;

import java.util.List;

/** Resultado paginado de una consulta. {@code pagina} es base 1. */
public record Pagina<T>(List<T> elementos, int pagina, int tamanio, long totalElementos) {

    public Pagina {
        elementos = List.copyOf(elementos);
    }

    public int totalPaginas() {
        return (int) Math.max(1, (totalElementos + tamanio - 1) / tamanio);
    }

    public boolean hayAnterior() {
        return pagina > 1;
    }

    public boolean haySiguiente() {
        return pagina < totalPaginas();
    }
}
