package co.edu.unicauca.bancopreguntas;

import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;
import co.edu.unicauca.bancopreguntas.domain.services.PreguntaService;
import co.edu.unicauca.bancopreguntas.presentation.controllers.PreguntaController;
import co.edu.unicauca.bancopreguntas.presentation.views.CrearPreguntaPanel;

public class App {

    public static void main(String[] args) {
        // Configurar la apariencia visual para que coincida con el sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Ejecutar en el hilo de despacho de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            // 1. Repositorio temporal (Stub) para probar la interfaz visual
            PreguntaRepository repoTemp = new PreguntaRepository() {
                @Override
                public void guardar(Pregunta p) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "¡Pregunta guardada con éxito en el sistema!", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }

                @Override
                public void actualizarEstado(int preguntaId, EstadoPregunta nuevoEstado) {}

                @Override
                public Optional<Pregunta> buscarPorId(int id) {
                    return Optional.empty();
                }
            };

            // 2. Instanciar el servicio y el controlador
            PreguntaService service = new PreguntaService(repoTemp);
            PreguntaController controller = new PreguntaController(service);

            // 3. Crear la ventana principal (JFrame)
            JFrame frame = new JFrame("Banco de Preguntas Saber Pro - Gestión de Preguntas (HU-01)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(750, 850);
            frame.setLocationRelativeTo(null);

            // 4. Agregar el panel de creación de preguntas
            CrearPreguntaPanel panelCrear = new CrearPreguntaPanel(controller);
            frame.add(panelCrear);

            // 5. Hacer visible la ventana
            frame.setVisible(true);
        });
    }
}