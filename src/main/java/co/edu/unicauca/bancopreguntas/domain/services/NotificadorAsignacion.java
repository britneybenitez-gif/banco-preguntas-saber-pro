package co.edu.unicauca.bancopreguntas.domain.services;

public interface NotificadorAsignacion {
    void enviarNotificacion(String destinatario, String asunto, String mensaje);
}