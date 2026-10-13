package co.edu.presentation.controllers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;
import co.edu.unicauca.bancopreguntas.domain.services.PreguntaService;
import co.edu.unicauca.bancopreguntas.presentation.controllers.PreguntaController;

class PreguntaControllerTest {

    private PreguntaController preguntaController;
    private DummyPreguntaService preguntaServiceStub;

    @BeforeEach
    void setUp() {
        // Implementación anónima correcta de la interfaz PreguntaRepository
        PreguntaRepository repoDummy = new PreguntaRepository() {
            @Override
            public void guardar(Pregunta pregunta) {
                // Sin acción para la prueba unitaria
            }

            @Override
            public void actualizarEstado(int preguntaId, EstadoPregunta nuevoEstado) {
                // Sin acción para la prueba unitaria
            }

            @Override
            public Optional<Pregunta> buscarPorId(int id) {
                return Optional.empty();
            }
        };
        
        preguntaServiceStub = new DummyPreguntaService(repoDummy);
        preguntaController = new PreguntaController(preguntaServiceStub);
    }

    @Test
    void testGuardarPreguntaExitoso() {
        String contexto = "Contexto de prueba sobre ingenieria de software y arquitectura.";
        String preguntaDirecta = "¿Cuál es el patrón que separa la vista de la lógica?";
        String distractor1 = "Patrón Modelo Vista Controlador que organiza componentes gráficos.";
        String distractor2 = "Patrón de diseño arquitectónico basado en microservicios web.";
        String distractor3 = "Un componente exclusivamente visual para formularios de usuario.";
        String distractor4 = "Una estructura de datos avanzada para bases relacionales.";
        String respuestaCorrecta = "A";
        String justificacion = "El patrón MVC separa responsabilidades adecuadamente.";
        String bibliografia = "Libro de arquitectura limpia de software.";
        String competencia = "Diseño de Software";
        String tema = "Arquitectura";
        String subtema = "MVC";
        String nivelDificultad = "MEDIA";
        int autorId = 1;

        assertDoesNotThrow(() -> {
            preguntaController.guardarPregunta(
                contexto, preguntaDirecta, distractor1, distractor2, 
                distractor3, distractor4, respuestaCorrecta, justificacion, 
                bibliografia, competencia, tema, subtema, nivelDificultad, autorId
            );
        });

        assertTrue(preguntaServiceStub.fueLlamado, "El servicio debió ser invocado con éxito.");
    }

    @Test
    void testGuardarPreguntaConErrorPorValidacion() {
        String contexto = "Contexto corto";
        String preguntaDirecta = "Pregunta";
        String distractor1 = "Corto"; // Menor a 10 caracteres para simular el fallo
        String distractor2 = "Corto 2";
        String distractor3 = "Corto 3";
        String distractor4 = "Corto 4";
        String respuestaCorrecta = "A";
        String justificacion = "Justificación";
        String bibliografia = "Biblio";
        String competencia = "Comp";
        String tema = "Tema";
        String subtema = "Sub";
        String nivelDificultad = "BAJA";
        int autorId = 1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            preguntaController.guardarPregunta(
                contexto, preguntaDirecta, distractor1, distractor2, 
                distractor3, distractor4, respuestaCorrecta, justificacion, 
                bibliografia, competencia, tema, subtema, nivelDificultad, autorId
            );
        });

        assertNotNull(exception.getMessage());
    }

    private static class DummyPreguntaService extends PreguntaService {
        public boolean fueLlamado = false;

        public DummyPreguntaService(PreguntaRepository repository) {
            super(repository);
        }

        @Override
        public void crearPregunta(Pregunta pregunta, int autorId) {
            if (pregunta.getDistractor1() != null && pregunta.getDistractor1().length() < 10) {
                throw new IllegalArgumentException("Cada distractor debe tener al menos 10 caracteres");
            }
            fueLlamado = true;
        }
    }
}