package co.edu.unicauca.bancopreguntas.dataaccess;

import java.util.logging.Logger;

import co.edu.unicauca.bancopreguntas.domain.services.NotificacionException;
import co.edu.unicauca.bancopreguntas.domain.services.NotificadorEmail;

/**
 * Notificador de email simulado: escribe el mensaje en el log en lugar de
 * conectarse a un servidor SMTP. Para enviar emails reales, crear otra
 * implementación de {@link NotificadorEmail} (p. ej. con Jakarta Mail) y
 * cambiarla en {@code App}; ninguna otra clase cambia.
 */
public class NotificadorEmailSimulado implements NotificadorEmail {

    private static final Logger LOG = Logger.getLogger(NotificadorEmailSimulado.class.getName());

    @Override
    public void enviar(String destinatario, String asunto, String cuerpo) {
        if (destinatario == null || !destinatario.contains("@")) {
            throw new NotificacionException("Dirección de email inválida: " + destinatario);
        }
        LOG.info(() -> "EMAIL a " + destinatario + " | " + asunto + "\n" + cuerpo);
    }
}
