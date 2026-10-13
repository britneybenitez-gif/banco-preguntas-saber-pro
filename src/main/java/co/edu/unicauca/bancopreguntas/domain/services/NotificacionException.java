package co.edu.unicauca.bancopreguntas.domain.services;

/** Error al enviar una notificación (por ejemplo, un email). */
public class NotificacionException extends RuntimeException {

    public NotificacionException(String mensaje) {
        super(mensaje);
    }

    public NotificacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
