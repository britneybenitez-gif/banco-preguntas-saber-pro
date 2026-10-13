package co.edu.unicauca.bancopreguntas.presentation.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.presentation.controllers.ConsultaPreguntaController;
import co.edu.unicauca.bancopreguntas.presentation.controllers.EnviarARevisionController;
import co.edu.unicauca.bancopreguntas.presentation.utils.UIUtils;

/**
 * HU-03 y HU-02: lista las preguntas del autor con filtros y paginación, muestra
 * el estado con colores y permite enviar un borrador a revisión.
 */
public class MisPreguntasPanel extends JPanel {

    private static final String TODOS = "Todos";

    private final ConsultaPreguntaController controller;

    private final JTextField txtBuscar = new JTextField(16);
    private final JComboBox<Object> cmbEstado = new JComboBox<>();
    private final JComboBox<String> cmbDificultad =
            new JComboBox<>(new String[]{TODOS, "BAJA", "MEDIA", "ALTA"});
    private final JTextField txtCompetencia = new JTextField(12);
    private final JTextField txtTema = new JTextField(12);

    private final PreguntasTableModel modelo = new PreguntasTableModel();
    private final JTable tabla = new JTable(modelo);
    private final PaginadorPanel paginador = new PaginadorPanel(this::recargar);

    /** Filtro con el que se hizo la última búsqueda (el que usa el paginador). */
    private FiltroPregunta filtroAplicado = FiltroPregunta.vacio();

    public MisPreguntasPanel(ConsultaPreguntaController controller,
                             EnviarARevisionController enviarController) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(crearPanelFiltros(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearPanelInferior(enviarController), BorderLayout.SOUTH);

        recargar();
    }

    private JPanel crearPanelFiltros() {
        cmbEstado.addItem(TODOS);
        for (EstadoPregunta e : EstadoPregunta.values()) {
            cmbEstado.addItem(e);
        }

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fila1.add(new JLabel("Buscar:"));
        fila1.add(txtBuscar);
        fila1.add(new JLabel("Estado:"));
        fila1.add(cmbEstado);
        fila1.add(new JLabel("Dificultad:"));
        fila1.add(cmbDificultad);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fila2.add(new JLabel("Competencia:"));
        fila2.add(txtCompetencia);
        fila2.add(new JLabel("Tema:"));
        fila2.add(txtTema);
        fila2.add(btnBuscar);
        fila2.add(btnLimpiar);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        panel.add(fila1);
        panel.add(fila2);

        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        // Enter en cualquier campo de texto también busca
        txtBuscar.addActionListener(e -> buscar());
        txtCompetencia.addActionListener(e -> buscar());
        txtTema.addActionListener(e -> buscar());
        return panel;
    }

    private JScrollPane crearTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(PreguntasTableModel.COL_ESTADO)
                .setCellRenderer(new EstadoCellRenderer());

        PreguntasTableModel.ajustarColumnas(tabla);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verDetalle();
                }
            }
        });
        return new JScrollPane(tabla);
    }

    private JPanel crearPanelInferior(EnviarARevisionController enviarController) {
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton btnDetalle = new JButton("Ver detalle");
        btnDetalle.addActionListener(e -> verDetalle());
        acciones.add(btnDetalle);
        // HU-02: botón ya existente, ahora conectado a la tabla
        acciones.add(new BotonEnviarARevision(enviarController, this::idSeleccionado, this::recargar));

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        leyenda.add(new JLabel("Estados:"));
        for (EstadoPregunta e : EstadoPregunta.values()) {
            leyenda.add(UIUtils.insigniaEstado(e));
        }

        JPanel fila1 = new JPanel(new BorderLayout());
        fila1.add(acciones, BorderLayout.WEST);
        fila1.add(paginador, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(fila1, BorderLayout.NORTH);
        panel.add(leyenda, BorderLayout.SOUTH);
        panel.add(Box.createVerticalStrut(2), BorderLayout.CENTER);
        return panel;
    }

    private void buscar() {
        filtroAplicado = leerFiltro();
        paginador.reiniciar();
        recargar();
    }

    private void limpiarFiltros() {
        txtBuscar.setText("");
        txtCompetencia.setText("");
        txtTema.setText("");
        cmbEstado.setSelectedIndex(0);
        cmbDificultad.setSelectedIndex(0);
        buscar();
    }

    private FiltroPregunta leerFiltro() {
        Object estado = cmbEstado.getSelectedItem();
        String dificultad = (String) cmbDificultad.getSelectedItem();
        return new FiltroPregunta(
                null,
                estado instanceof EstadoPregunta e ? e : null,
                TODOS.equals(dificultad) ? null : dificultad,
                txtCompetencia.getText(),
                txtTema.getText(),
                txtBuscar.getText());
    }

    /** Vuelve a consultar con el filtro aplicado y la página actual (también la llama la ventana al cambiar de pestaña). */
    public void recargar() {
        try {
            Pagina<Pregunta> resultado =
                    controller.listar(filtroAplicado, paginador.getPagina(), paginador.getTamanio());
            modelo.setDatos(resultado.elementos());
            paginador.actualizar(resultado);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer idSeleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : modelo.getPregunta(fila).getId();
    }

    private void verDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta para ver su detalle.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new PreguntaDetalleDialog(this, modelo.getPregunta(fila)).setVisible(true);
    }
}
