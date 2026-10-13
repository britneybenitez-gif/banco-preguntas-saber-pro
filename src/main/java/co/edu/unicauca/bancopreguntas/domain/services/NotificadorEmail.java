package co.edu.unicauca.bancopreguntas.domain.services;

/**
 * Puerto de salida para enviar emails. El dominio depende de esta abstracción (DIP);
 * la implementación concreta (SMTP, simulada, etc.) vive en la capa de acceso a datos.
 */
public interface NotificadorEmail {

    /** @throws NotificacionException si el mensaje no pudo enviarse */
    void enviar(String destinatario, String asunto, String cuerpo);
}
