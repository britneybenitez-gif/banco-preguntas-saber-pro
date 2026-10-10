package co.edu.unicauca.bancopreguntas.dataaccess;

import co.edu.unicauca.bancopreguntas.domain.services.NotificadorAsignacion;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailNotificador implements NotificadorAsignacion {

    @Override
    public void enviarNotificacion(String destinatario, String asunto, String cuerpo) {
        // Leer credenciales de variables de entorno de Windows por seguridad
        final String remitente = System.getenv("MAIL_USER");
        final String password = System.getenv("MAIL_PASSWORD");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // O tu servidor SMTP
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + destinatario);

        } catch (MessagingException e) {
            throw new RuntimeException("Fallo al enviar el correo electrónico: " + e.getMessage(), e);
        }
    }
}