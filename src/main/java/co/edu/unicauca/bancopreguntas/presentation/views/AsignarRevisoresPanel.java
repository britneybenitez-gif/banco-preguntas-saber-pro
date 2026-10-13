package co.edu.unicauca.bancopreguntas.presentation.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import co.edu.unicauca.bancopreguntas.domain.entities.FiltroPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.domain.services.AsignacionRevisorService.ResultadoAsignacion;
import co.edu.unicauca.bancopreguntas.presentation.controllers.AsignacionRevisorController;
import co.edu.unicauca.bancopreguntas.presentation.utils.UIUtils;

/**
 * HU-04: el administrador ve las preguntas "Pendiente de revisión", marca uno o
 * más revisores y los asigna; el sistema notifica por email a cada revisor.
 */
public class AsignarRevisoresPanel extends JPanel {

    private final AsignacionRevisorController controller;

    private final JTextField txtBuscar = new JTextField(18);
    private final PreguntasTableModel modeloPreguntas = new PreguntasTableModel();
    private final JTable tablaPreguntas = new JTable(modeloPreguntas);
    private final PaginadorPanel paginador = new PaginadorPanel(this::recargar);
    private final RevisoresTableModel modeloRevisores;
    private final JLabel lblSeleccion = new JLabel("Seleccione una pregunta pendiente de revisión.");

    private FiltroPregunta filtroAplicado = FiltroPregunta.vacio();

    public AsignarRevisoresPanel(AsignacionRevisorController controller) {
        this.controller = controller;
        this.modeloRevisores = new RevisoresTableModel(controller.listarRevisores());

        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(crearFiltro(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                crearPanelPreguntas(), crearPanelRevisores());
        split.setResizeWeight(0.55);
        add(split, BorderLayout.CENTER);

        recargar();
    }

    private JPanel crearFiltro() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Preguntas en estado «Pendiente de revisión»"));
        panel.add(new JLabel("Buscar:"));
        panel.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        btnBuscar.addActionListener(e -> buscar());
        txtBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            buscar();
        });
        return panel;
    }

    private JPanel crearPanelPreguntas() {
        tablaPreguntas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPreguntas.setRowHeight(26);
        tablaPreguntas.setFillsViewportHeight(true);
        tablaPreguntas.getTableHeader().setReorderingAllowed(false);
        tablaPreguntas.getColumnModel().getColumn(PreguntasTableModel.COL_ESTADO)
                .setCellRenderer(new EstadoCellRenderer());
        PreguntasTableModel.ajustarColumnas(tablaPreguntas);
        tablaPreguntas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                alCambiarSeleccion();
            }
        });
        tablaPreguntas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verDetalle();
                }
            }
        });

        JButton btnDetalle = new JButton("Ver detalle");
        btnDetalle.addActionListener(e -> verDetalle());
        JPanel barra = new JPanel(new BorderLayout());
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        izq.add(btnDetalle);
        barra.add(izq, BorderLayout.WEST);
        barra.add(paginador, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tablaPreguntas), BorderLayout.CENTER);
        panel.add(barra, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelRevisores() {
        JTable tablaRevisores = new JTable(modeloRevisores);
        tablaRevisores.setRowHeight(26);
        tablaRevisores.setFillsViewportHeight(true);
        tablaRevisores.getColumnModel().getColumn(0).setMaxWidth(80);

        JButton btnAsignar = new JButton("Asignar revisores y notificar por email");
        btnAsignar.addActionListener(e -> asignar());

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(lblSeleccion, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        botones.add(btnAsignar);
        sur.add(botones, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Revisores disponibles (marque al menos uno)"));
        panel.add(new JScrollPane(tablaRevisores), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private void buscar() {
        filtroAplicado = new FiltroPregunta(null, null, null, null, null, txtBuscar.getText());
        paginador.reiniciar();
        recargar();
    }

    /** Vuelve a consultar con el filtro aplicado y la página actual. */
    public void recargar() {
        try {
            Pagina<Pregunta> resultado =
                    controller.listarPendientes(filtroAplicado, paginador.getPagina(), paginador.getTamanio());
            modeloPreguntas.setDatos(resultado.elementos());
            paginador.actualizar(resultado);
            alCambiarSeleccion();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alCambiarSeleccion() {
        int fila = tablaPreguntas.getSelectedRow();
        lblSeleccion.setText(fila < 0
                ? "Seleccione una pregunta pendiente de revisión."
                : "Pregunta #" + modeloPreguntas.getPregunta(fila).getId() + ": "
                  + UIUtils.recortar(modeloPreguntas.getPregunta(fila).getPreguntaDirecta(), 60));
    }

    private void verDetalle() {
        int fila = tablaPreguntas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta para ver su detalle.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new PreguntaDetalleDialog(this, modeloPreguntas.getPregunta(fila)).setVisible(true);
    }

    private void asignar() {
        int fila = tablaPreguntas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta pendiente de revisión.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Integer> ids = modeloRevisores.idsMarcados();
        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe asignar al menos un revisor.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pregunta pregunta = modeloPreguntas.getPregunta(fila);
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Asignar " + ids.size() + " revisor(es) a la pregunta #" + pregunta.getId()
                        + " y notificarlos por email?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ResultadoAsignacion r = controller.asignar(pregunta.getId(), ids);
            modeloRevisores.limpiar();
            mostrarResultado(r);
            recargar();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResultado(ResultadoAsignacion r) {
        StringBuilder sb = new StringBuilder("Revisores asignados a la pregunta #")
                .append(r.pregunta().getId()).append(". La pregunta pasó a «")
                .append(r.pregunta().getEstado().getLabel()).append("».\n\n");
        if (!r.notificados().isEmpty()) {
            sb.append("Email enviado a:\n");
            r.notificados().forEach(u -> sb.append(" • ").append(u.nombre())
                    .append(" <").append(u.email()).append(">\n"));
        }
        if (!r.sinNotificar().isEmpty()) {
            sb.append("\nNo se pudo enviar el email a:\n");
            r.sinNotificar().forEach(u -> sb.append(" • ").append(u.nombre())
                    .append(" <").append(u.email()).append(">\n"));
        }
        JOptionPane.showMessageDialog(this, sb.toString(),
                r.sinNotificar().isEmpty() ? "Éxito" : "Asignado con advertencias",
                r.sinNotificar().isEmpty() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    /** Tabla de revisores con casilla de selección. */
    private static class RevisoresTableModel extends AbstractTableModel {

        private final List<Usuario> revisores;
        private final boolean[] marcados;

        RevisoresTableModel(List<Usuario> revisores) {
            this.revisores = revisores;
            this.marcados = new boolean[revisores.size()];
        }

        List<Integer> idsMarcados() {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < marcados.length; i++) {
                if (marcados[i]) {
                    ids.add(revisores.get(i).id());
                }
            }
            return ids;
        }

        void limpiar() {
            java.util.Arrays.fill(marcados, false);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return revisores.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int c) {
            return new String[]{"Asignar", "Revisor", "Email"}[c];
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == 0 ? Boolean.class : String.class;
        }

        @Override
        public boolean isCellEditable(int fila, int col) {
            return col == 0;
        }

        @Override
        public Object getValueAt(int fila, int col) {
            Usuario u = revisores.get(fila);
            return switch (col) {
                case 0 -> marcados[fila];
                case 1 -> u.nombre();
                default -> u.email();
            };
        }

        @Override
        public void setValueAt(Object valor, int fila, int col) {
            if (col == 0) {
                marcados[fila] = (Boolean) valor;
                fireTableCellUpdated(fila, col);
            }
        }
    }
}
