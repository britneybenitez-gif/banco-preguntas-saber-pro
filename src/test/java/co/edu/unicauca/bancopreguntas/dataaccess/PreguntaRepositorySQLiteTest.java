package co.edu.unicauca.bancopreguntas.dataaccess;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.PreguntaBuilder;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PreguntaRepositorySQLiteTest {
    private PreguntaRepository repository;
    
 
    private static final String TEST_DB_URL = "jdbc:sqlite:target/test.db";

    @BeforeEach
    public void setUp() {
        repository = new PreguntaRepositorySQLite(TEST_DB_URL);
        
        Pregunta p = new PreguntaBuilder()
            .conContexto("Contexto de prueba para evaluación.")
            .conPreguntaDirecta("¿Cuál es la capital de Colombia?")
            .conDistractor1("Medellín")
            .conDistractor2("Cali")
            .conDistractor3("Barranquilla")
            .conDistractor4("Cartagena")
            .conRespuestaCorrecta("A")
            .conJustificacion("Bogotá es la capital de la república.")
            .conBibliografia("Geografía de Colombia, 2023")
            .conCompetencia("Conocimiento general")
            .conTema("Geografía")
            .conSubtema("Capitales")
            .conNivelDificultad("Baja")
            .build();
            
        p.setAutorId(1);
        repository.guardar(p);
    }

    @AfterEach
    public void tearDown() {
        // Eliminamos el archivo de prueba al terminar para dejar todo limpio
        File dbFile = new File("target/test.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    public void testListarPorAutorConPaginacion() {
        List<Pregunta> resultado = repository.listarPorAutor(1, null, null, 0, 10);
        assertEquals(1, resultado.size());
        assertEquals("¿Cuál es la capital de Colombia?", resultado.get(0).getPreguntaDirecta());
    }

    @Test
    public void testContarPorAutor() {
        int total = repository.contarPorAutor(1, null, null);
        assertEquals(1, total);
    }

    @Test
    public void testActualizarEstado() {
        // Obtenemos la pregunta guardada para saber su ID real generado por SQLite
        List<Pregunta> lista = repository.listarPorAutor(1, null, null, 0, 10);
        int idGuardado = lista.get(0).getId();
        
        repository.actualizarEstado(idGuardado, EstadoPregunta.PENDIENTE_REVISION);
        
        var p = repository.buscarPorId(idGuardado);
        assertTrue(p.isPresent());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, p.get().getEstado());
    }
}