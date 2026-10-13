package co.edu.unicauca.bancopreguntas.presentation.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Usuario;
import co.edu.unicauca.bancopreguntas.presentation.utils.UIUtils;

/** Vista de solo lectura con todos los datos de una pregunta. */
public class PreguntaDetalleDialog extends JDialog {

    public PreguntaDetalleDialog(Component padre, Pregunta p) {
        super(SwingUtilities.getWindowAncestor(padre), "Pregunta #" + p.getId(),
                ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int fila = 0;
        agregar(form, gbc, fila++, "Estado:", UIUtils.insigniaEstado(p.getEstado()));
        agregar(form, gbc, fila++, "Contexto:", texto(p.getContexto()));
        agregar(form, gbc, fila++, "Pregunta directa:", texto(p.getPreguntaDirecta()));
        agregar(form, gbc, fila++, "Opción A:", texto(p.getDistractor1()));
        agregar(form, gbc, fila++, "Opción B:", texto(p.getDistractor2()));
        agregar(form, gbc, fila++, "Opción C:", texto(p.getDistractor3()));
        agregar(form, gbc, fila++, "Opción D:", texto(p.getDistractor4()));
        agregar(form, gbc, fila++, "Respuesta correcta:", new JLabel(p.getRespuestaCorrecta()));
        agregar(form, gbc, fila++, "Justificación:", texto(p.getJustificacion()));
        agregar(form, gbc, fila++, "Bibliografía:", texto(p.getBibliografia()));
        agregar(form, gbc, fila++, "Competencia:", new JLabel(p.getCompetencia()));
        agregar(form, gbc, fila++, "Tema / Subtema:", new JLabel(p.getTema() + " / " + p.getSubtema()));
        agregar(form, gbc, fila++, "Nivel de dificultad:", new JLabel(p.getNivelDificultad()));
        agregar(form, gbc, fila++, "Revisores:", new JLabel(p.getRevisores().isEmpty()
                ? "Sin asignar"
                : p.getRevisores().stream().map(Usuario::nombre).collect(Collectors.joining(", "))));

        add(new JScrollPane(form), BorderLayout.CENTER);

        JButton cerrar = new JButton("Cerrar");
        cerrar.addActionListener(e -> dispose());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(cerrar);
        add(sur, BorderLayout.SOUTH);

        setSize(640, 620);
        setLocationRelativeTo(padre);
    }

    private static JScrollPane texto(String contenido) {
        JTextArea a = new JTextArea(contenido == null ? "" : contenido);
        a.setEditable(false);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setRows(Math.min(5, Math.max(1, a.getText().length() / 70 + 1)));
        JScrollPane sp = new JScrollPane(a);
        sp.setPreferredSize(new Dimension(420, sp.getPreferredSize().height));
        return sp;
    }

    private static void agregar(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, Component c) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.25;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.75;
        panel.add(c, gbc);
    }
}
