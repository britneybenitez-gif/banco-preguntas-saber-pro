package co.edu.unicauca.bancopreguntas.presentation.views;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.presentation.utils.UIUtils;

/** Modelo de tabla para listar preguntas (solo lectura). */
public class PreguntasTableModel extends AbstractTableModel {

    public static final int COL_ESTADO = 5;

    private static final String[] COLUMNAS =
            {"ID", "Pregunta", "Competencia", "Tema", "Dificultad", "Estado", "Revisores"};

    /** Anchos de columna comunes a todas las tablas de preguntas. */
    public static void ajustarColumnas(JTable tabla) {
        int[] anchos = {45, 330, 150, 110, 80, 140, 160};
        tabla.getColumnModel().getColumn(0).setMaxWidth(60);
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private List<Pregunta> filas = new ArrayList<>();

    public void setDatos(List<Pregunta> datos) {
        this.filas = new ArrayList<>(datos);
        fireTableDataChanged();
    }

    public Pregunta getPregunta(int fila) {
        return filas.get(fila);
    }

    @Override
    public int getRowCount() {
        return filas.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int c) {
        return COLUMNAS[c];
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return c == 0 ? Integer.class : Object.class;
    }

    @Override
    public Object getValueAt(int fila, int col) {
        Pregunta p = filas.get(fila);
        return switch (col) {
            case 0 -> p.getId();
            case 1 -> UIUtils.recortar(p.getPreguntaDirecta(), 70);
            case 2 -> p.getCompetencia();
            case 3 -> p.getTema();
            case 4 -> p.getNivelDificultad();
            case 5 -> p.getEstado();
            case 6 -> p.getRevisores().isEmpty() ? "—"
                    : p.getRevisores().stream().map(Usuario::nombre).collect(Collectors.joining(", "));
            default -> "";
        };
    }
}
