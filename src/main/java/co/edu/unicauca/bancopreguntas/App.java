package co.edu.unicauca.bancopreguntas;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import co.edu.unicauca.bancopreguntas.dataaccess.DatosDemo;
import co.edu.unicauca.bancopreguntas.dataaccess.NotificadorEmailSimulado;
import co.edu.unicauca.bancopreguntas.dataaccess.PreguntaRepositoryMemoria;
import co.edu.unicauca.bancopreguntas.dataaccess.UsuarioRepositoryMemoria;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.services.AsignacionRevisorService;
import co.edu.unicauca.bancopreguntas.domain.services.ConsultaPreguntaService;
import co.edu.unicauca.bancopreguntas.domain.services.PreguntaService;
import co.edu.unicauca.bancopreguntas.presentation.views.VentanaPrincipal;

/** Raíz de composición: crea los repositorios, servicios y abre la ventana principal. */
public class App {

    public static void main(String[] args) {
        // Configurar la apariencia visual para que coincida con el sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Ejecutar en el hilo de despacho de eventos de Swing (EDT)
        SwingUtilities.invokeLater(App::iniciar);
    }

    private static void iniciar() {
        // 1. Capa de acceso a datos (en memoria; se puede cambiar por JDBC/SQLite sin tocar el dominio)
        PreguntaRepositoryMemoria preguntaRepo = new PreguntaRepositoryMemoria();
        UsuarioRepositoryMemoria usuarioRepo = new UsuarioRepositoryMemoria();

        // 2. Capa de dominio (servicios)
        PreguntaService preguntaService = new PreguntaService(preguntaRepo);
        ConsultaPreguntaService consultaService = new ConsultaPreguntaService(preguntaRepo);
        AsignacionRevisorService asignacionService = new AsignacionRevisorService(
                preguntaRepo, preguntaRepo, preguntaRepo, usuarioRepo, new NotificadorEmailSimulado());

        // Preguntas de ejemplo para probar paginación, filtros y estados (opcional)
        DatosDemo.cargar(preguntaService, 1, 6);

        // 3. Capa de presentación
        iniciarSesion(usuarioRepo.listarTodos(), preguntaService, consultaService, asignacionService);
    }

    private static void iniciarSesion(java.util.List<Usuario> usuarios,
                                      PreguntaService preguntaService,
                                      ConsultaPreguntaService consultaService,
                                      AsignacionRevisorService asignacionService) {
        JComboBox<Usuario> combo = new JComboBox<>(usuarios.toArray(new Usuario[0]));
        int opcion = JOptionPane.showConfirmDialog(null, combo, "Ingresar como",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            System.exit(0);
        }
        Usuario usuario = (Usuario) combo.getSelectedItem();

        new VentanaPrincipal(usuario, preguntaService, consultaService, asignacionService,
                () -> iniciarSesion(usuarios, preguntaService, consultaService, asignacionService))
                .setVisible(true);
    }
}
