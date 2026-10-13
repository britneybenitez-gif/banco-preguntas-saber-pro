package co.edu.unicauca.bancopreguntas.presentation.utils;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;

public class UIUtils {

    private UIUtils() { }

    /** Recorta un texto largo para mostrarlo en una celda de tabla. */
    public static String recortar(String texto, int max) {
        if (texto == null) {
            return "";
        }
        String limpio = texto.replaceAll("\\s+", " ").trim();
        return limpio.length() <= max ? limpio : limpio.substring(0, max - 1) + "…";
    }

    /** Etiqueta con el color del estado (misma paleta que {@link EstadoPregunta}). */
    public static JLabel insigniaEstado(EstadoPregunta estado) {
        JLabel l = new JLabel(estado.getLabel(), SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(estado.getColor());
        l.setForeground(estado.getTextColor());
        l.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        l.setPreferredSize(new Dimension(l.getPreferredSize().width, 24));
        return l;
    }

    public static final Color COLOR_ERROR = new Color(220, 53, 69);
}
