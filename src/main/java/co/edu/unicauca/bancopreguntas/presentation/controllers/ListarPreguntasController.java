package co.edu.unicauca.bancopreguntas.presentation.controllers;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;
import java.util.List;

public class ListarPreguntasController {
    private final PreguntaRepository repository;
    private int paginaActual = 0;
    private final int LIMITE_POR_PAGINA = 10;

    public ListarPreguntasController(PreguntaRepository repository) {
        this.repository = repository;
    }

    public List<Pregunta> obtenerPagina(int autorId, List<EstadoPregunta> estadosFiltro, String nivelDificultad) {
        int offset = paginaActual * LIMITE_POR_PAGINA;
        return repository.listarPorAutor(autorId, estadosFiltro, nivelDificultad, offset, LIMITE_POR_PAGINA);
    }

    public int obtenerTotalPaginas(int autorId, List<EstadoPregunta> estadosFiltro, String nivelDificultad) {
        int total = repository.contarPorAutor(autorId, estadosFiltro, nivelDificultad);
        return (int) Math.ceil((double) total / LIMITE_POR_PAGINA);
    }

    public void irAPaginaSiguiente(int totalPaginas) {
        if (paginaActual < totalPaginas - 1) paginaActual++;
    }

    public void irAPaginaAnterior() {
        if (paginaActual > 0) paginaActual--;
    }

    public void reiniciarPaginacion() {
        this.paginaActual = 0;
    }
    
    public int getPaginaActual() {
        return paginaActual;
    }
}