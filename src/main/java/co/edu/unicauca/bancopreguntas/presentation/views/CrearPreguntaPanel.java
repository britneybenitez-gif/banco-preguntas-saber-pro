package co.edu.unicauca.bancopreguntas.presentation.views;

import  java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import co.edu.unicauca.bancopreguntas.presentation.controllers.PreguntaController;

public class CrearPreguntaPanel extends JPanel {

    private final PreguntaController controller;

    private JTextArea txtContexto;
    private JTextArea txtPreguntaDirecta;
    private JTextField txtDistractor1;
    private JTextField txtDistractor2;
    private JTextField txtDistractor3;
    private JTextField txtDistractor4;
    private JTextField txtRespuestaCorrecta;
    private JTextArea txtJustificacion;
    private JTextField txtBibliografia;
    private JTextField txtCompetencia;
    private JTextField txtTema;
    private JTextField txtSubtema;
    private JComboBox<String> cmbDificultad;
    private JButton btnGuardar;

    public CrearPreguntaPanel(PreguntaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        addField(formPanel, gbc, row++, "Contexto:", txtContexto = new JTextArea(3, 20));
        addField(formPanel, gbc, row++, "Pregunta Directa:", txtPreguntaDirecta = new JTextArea(3, 20));
        addField(formPanel, gbc, row++, "Distractor 1 (Opción A):", txtDistractor1 = new JTextField(20));
        addField(formPanel, gbc, row++, "Distractor 2 (Opción B):", txtDistractor2 = new JTextField(20));
        addField(formPanel, gbc, row++, "Distractor 3 (Opción C):", txtDistractor3 = new JTextField(20));
        addField(formPanel, gbc, row++, "Distractor 4 (Opción D):", txtDistractor4 = new JTextField(20));
        addField(formPanel, gbc, row++, "Respuesta Correcta:", txtRespuestaCorrecta = new JTextField(20));
        addField(formPanel, gbc, row++, "Justificación:", txtJustificacion = new JTextArea(3, 20));
        addField(formPanel, gbc, row++, "Bibliografía:", txtBibliografia = new JTextField(20));
        addField(formPanel, gbc, row++, "Competencia:", txtCompetencia = new JTextField(20));
        addField(formPanel, gbc, row++, "Tema:", txtTema = new JTextField(20));
        addField(formPanel, gbc, row++, "Subtema:", txtSubtema = new JTextField(20));

        // Selector de Dificultad
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nivel Dificultad:"), gbc);
        gbc.gridx = 1;
        cmbDificultad = new JComboBox<>(new String[]{"BAJA", "MEDIA", "ALTA"});
        formPanel.add(cmbDificultad, gbc);
        row++;

        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para el botón Guardar
        JPanel bottomPanel = new JPanel();
        btnGuardar = new JButton("Guardar Pregunta");
        bottomPanel.add(btnGuardar);
        add(bottomPanel, BorderLayout.SOUTH);

        // Acción del Botón
        btnGuardar.addActionListener(e -> ejecutarGuardado());
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        if (field instanceof JTextArea) {
            panel.add(new JScrollPane(field), gbc);
        } else {
            panel.add(field, gbc);
        }
    }

    private void ejecutarGuardado() {
        try {
            // Pasamos un ID de autor fijo por defecto (ej. 1) mientras se integra la sesión de usuario
            controller.guardarPregunta(
                txtContexto.getText(),
                txtPreguntaDirecta.getText(),
                txtDistractor1.getText(),
                txtDistractor2.getText(),
                txtDistractor3.getText(),
                txtDistractor4.getText(),
                txtRespuestaCorrecta.getText(),
                txtJustificacion.getText(),
                txtBibliografia.getText(),
                txtCompetencia.getText(),
                txtTema.getText(),
                txtSubtema.getText(),
                (String) cmbDificultad.getSelectedItem(),
                1 
            );

            JOptionPane.showMessageDialog(this, "¡Pregunta guardada en borrador exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtContexto.setText("");
        txtPreguntaDirecta.setText("");
        txtDistractor1.setText("");
        txtDistractor2.setText("");
        txtDistractor3.setText("");
        txtDistractor4.setText("");
        txtRespuestaCorrecta.setText("");
        txtJustificacion.setText("");
        txtBibliografia.setText("");
        txtCompetencia.setText("");
        txtTema.setText("");
        txtSubtema.setText("");
        cmbDificultad.setSelectedIndex(0);
    }
}
