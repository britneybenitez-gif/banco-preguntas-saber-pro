package co.edu.unicauca.bancopreguntas.presentation.views;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.presentation.controllers.ListarPreguntasController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ListarPreguntasPanel extends JPanel {
    private final ListarPreguntasController controller;
    private final int autorIdActual;
    private JTable tablaPreguntas;
    private DefaultTableModel modeloTabla;
    private JLabel lblPagina;
    private JComboBox<String> cbDificultad;
    private JComboBox<String> cbEstado;

    public ListarPreguntasPanel(ListarPreguntasController controller, int autorIdActual) {
        this.controller = controller;
        this.autorIdActual = autorIdActual;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Panel de Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbDificultad = new JComboBox<>(new String[]{"Todas", "Baja", "Media", "Alta"});
        cbEstado = new JComboBox<>(new String[]{"Todos", "BORRADOR", "PENDIENTE_REVISION", "EN_REVISION", "APROBADA"});
        JButton btnFiltrar = new JButton("Aplicar Filtros");
        
        panelFiltros.add(new JLabel("Dificultad:"));
        panelFiltros.add(cbDificultad);
        panelFiltros.add(new JLabel("Estado:"));
        panelFiltros.add(cbEstado);
        panelFiltros.add(btnFiltrar);
        add(panelFiltros, BorderLayout.NORTH);

        // 2. Tabla
        String[] columnas = {"ID", "Pregunta Directa", "Competencia", "Tema", "Dificultad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaPreguntas = new JTable(modeloTabla);
        add(new JScrollPane(tablaPreguntas), BorderLayout.CENTER);

        // 3. Panel de Paginación
        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAnterior = new JButton("Anterior");
        lblPagina = new JLabel("Página 1 de 1");
        JButton btnSiguiente = new JButton("Siguiente");
        
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPagina);
        panelPaginacion.add(btnSiguiente);
        add(panelPaginacion, BorderLayout.SOUTH);

        // 4. Eventos
        btnFiltrar.addActionListener(e -> {
            controller.reiniciarPaginacion();
            cargarDatos();
        });
        btnAnterior.addActionListener(e -> {
            controller.irAPaginaAnterior();
            cargarDatos();
        });
        btnSiguiente.addActionListener(e -> {
            int totalPaginas = controller.obtenerTotalPaginas(autorIdActual, obtenerEstadosFiltro(), obtenerDificultadFiltro());
            controller.irAPaginaSiguiente(totalPaginas);
            cargarDatos();
        });

        cargarDatos();
    }

    private List<EstadoPregunta> obtenerEstadosFiltro() {
        String estadoSel = (String) cbEstado.getSelectedItem();
        if (estadoSel == null || estadoSel.equals("Todos")) return null;
        return Arrays.asList(EstadoPregunta.valueOf(estadoSel));
    }

    private String obtenerDificultadFiltro() {
        String difSel = (String) cbDificultad.getSelectedItem();
        return (difSel == null || difSel.equals("Todas")) ? null : difSel;
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<EstadoPregunta> estadosFiltro = obtenerEstadosFiltro();
        String dificultadFiltro = obtenerDificultadFiltro();

        List<Pregunta> preguntas = controller.obtenerPagina(autorIdActual, estadosFiltro, dificultadFiltro);
        int totalPaginas = Math.max(1, controller.obtenerTotalPaginas(autorIdActual, estadosFiltro, dificultadFiltro));

        for (Pregunta p : preguntas) {
            String preg = p.getPreguntaDirecta();
            if (preg != null && preg.length() > 40) preg = preg.substring(0, 40) + "...";
            modeloTabla.addRow(new Object[]{p.getId(), preg, p.getCompetencia(), p.getTema(), p.getNivelDificultad(), p.getEstado().name()});
        }

        lblPagina.setText("Página " + (controller.getPaginaActual() + 1) + " de " + totalPaginas);
        if (preguntas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se ha encontrado ninguna pregunta", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}