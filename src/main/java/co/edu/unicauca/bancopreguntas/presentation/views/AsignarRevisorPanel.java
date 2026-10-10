package co.edu.unicauca.bancopreguntas.presentation.views;

import co.edu.unicauca.bancopreguntas.domain.services.AsignacionRevisorService;
import javax.swing.*;
import java.awt.*;

public class AsignarRevisorPanel extends JPanel {
    private JTextField txtPreguntaId;
    private JTextField txtCorreoRevisor;
    private JButton btnAsignar;
    private AsignacionRevisorService servicio;

    public AsignarRevisorPanel(AsignacionRevisorService servicio) {
        this.servicio = servicio;
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("ID de la Pregunta:"));
        txtPreguntaId = new JTextField();
        add(txtPreguntaId);

        add(new JLabel("Correo del Revisor:"));
        txtCorreoRevisor = new JTextField();
        add(txtCorreoRevisor);

        btnAsignar = new JButton("Asignar y Notificar");
        add(btnAsignar);

        btnAsignar.addActionListener(e -> ejecutarAsignacion());
    }

    private void ejecutarAsignacion() {
        try {
            int id = Integer.parseInt(txtPreguntaId.getText().trim());
            String correo = txtCorreoRevisor.getText().trim();
            
            boolean exito = servicio.asignarRevisorAPregunta(id, correo);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Revisor asignado y correo enviado exitosamente.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }
}