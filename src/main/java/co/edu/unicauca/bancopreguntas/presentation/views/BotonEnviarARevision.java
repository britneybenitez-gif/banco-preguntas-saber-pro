package co.edu.unicauca.bancopreguntas.presentation.views;

import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import co.edu.unicauca.bancopreguntas.presentation.controllers.EnviarARevisionController;

/**
 * HU-02: botón "Enviar a revisión" con confirmación y manejo de errores.
 * <p>
 * Uso en cualquier panel:
 * <pre>
 * add(new BotonEnviarARevision(controller, () -> idSeleccionado(), () -> cargarDatos()));
 * </pre>
 */
public class BotonEnviarARevision extends JButton {

    /**
     * @param controller           controlador de la HU-02
     * @param preguntaSeleccionada devuelve el id de la pregunta seleccionada, o null si no hay
     * @param alTerminar           se ejecuta tras enviar con éxito (por ejemplo, refrescar la tabla)
     */
    public BotonEnviarARevision(EnviarARevisionController controller,
                                Supplier<Integer> preguntaSeleccionada,
                                Runnable alTerminar) {
        super("Enviar a Revisión");

        addActionListener(e -> {
            Integer id = preguntaSeleccionada.get();
            if (id == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una pregunta para enviar a revisión.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea enviar la pregunta a revisión?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                controller.enviarARevision(id);
                JOptionPane.showMessageDialog(this,
                        "La pregunta ha sido enviada a revisión exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                alTerminar.run();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}