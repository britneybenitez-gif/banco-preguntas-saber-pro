package co.edu.unicauca.bancopreguntas.presentation.views;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import co.edu.unicauca.bancopreguntas.domain.entities.Rol;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.services.AsignacionRevisorService;
import co.edu.unicauca.bancopreguntas.domain.services.ConsultaPreguntaService;
import co.edu.unicauca.bancopreguntas.domain.services.PreguntaService;
import co.edu.unicauca.bancopreguntas.presentation.controllers.AsignacionRevisorController;
import co.edu.unicauca.bancopreguntas.presentation.controllers.ConsultaPreguntaController;
import co.edu.unicauca.bancopreguntas.presentation.controllers.EnviarARevisionController;
import co.edu.unicauca.bancopreguntas.presentation.controllers.PreguntaController;

/**
 * Ventana principal. Muestra las pestañas que corresponden al rol del usuario:
 * <ul>
 *   <li>Autor: crear pregunta (HU-01) y mis preguntas (HU-02, HU-03).</li>
 *   <li>Administrador: asignar revisores (HU-04).</li>
 * </ul>
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(Usuario usuario,
                            PreguntaService preguntaService,
                            ConsultaPreguntaService consultaService,
                            AsignacionRevisorService asignacionService,
                            Runnable cambiarUsuario) {
        super("Banco de Preguntas Saber Pro — " + usuario);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 640);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        if (usuario.rol() == Rol.AUTOR) {
            construirPestanasAutor(pestanas, usuario, preguntaService, consultaService);
        } else if (usuario.rol() == Rol.ADMINISTRADOR) {
            AsignarRevisoresPanel panel = new AsignarRevisoresPanel(
                    new AsignacionRevisorController(asignacionService, usuario.id()));
            pestanas.addTab("Asignar revisores", panel);
            pestanas.addChangeListener(e -> panel.recargar());
        }
        add(pestanas);

        setJMenuBar(crearMenu(cambiarUsuario));
    }

    private void construirPestanasAutor(JTabbedPane pestanas, Usuario autor,
                                        PreguntaService preguntaService,
                                        ConsultaPreguntaService consultaService) {
        PreguntaController preguntaController = new PreguntaController(preguntaService);
        MisPreguntasPanel misPreguntas = new MisPreguntasPanel(
                new ConsultaPreguntaController(consultaService, autor.id()),
                new EnviarARevisionController(preguntaService, autor.id()));

        pestanas.addTab("Crear pregunta", new CrearPreguntaPanel(preguntaController, autor.id()));
        pestanas.addTab("Mis preguntas", misPreguntas);
        // Al volver a "Mis preguntas" se ve la pregunta recién creada
        pestanas.addChangeListener(e -> {
            if (pestanas.getSelectedComponent() == misPreguntas) {
                misPreguntas.recargar();
            }
        });
    }

    private JMenuBar crearMenu(Runnable cambiarUsuario) {
        JMenu menu = new JMenu("Sesión");
        JMenuItem cambiar = new JMenuItem("Cambiar de usuario…");
        cambiar.addActionListener(e -> {
            dispose();
            cambiarUsuario.run();
        });
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        menu.add(cambiar);
        menu.add(salir);
        JMenuBar barra = new JMenuBar();
        barra.add(menu);
        return barra;
    }
}
