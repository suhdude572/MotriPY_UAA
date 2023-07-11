package com.example;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final String EMAIL_USERNAME = "motripyprueba";
    private static final String EMAIL_PASSWORD = "vxqiroblbjineoae";

    public void sendConfirmationEmail(String email, String confirmationToken) {
        // Configurar las propiedades para la conexión SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Crear una sesión de correo electrónico autenticada
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            // Crear un objeto MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Establecer el remitente y el destinatario
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // Establecer el asunto y el contenido del mensaje
            message.setSubject("Confirmación de registro");
            message.setText("Para confirmar tu registro, utiliza el siguiente token: " + confirmationToken );

            // Enviar el mensaje
            Transport.send(message);

            System.out.println("Correo de confirmación enviado a " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void sendPasswordResetEmail(String email, String resetToken) {
        // Configure the email sending properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    
        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });
    
        try {
            // Create a new email message
            MimeMessage message = new MimeMessage(session);
    
            // Set the recipient email address
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
    
            // Set the email subject
            message.setSubject("Password Reset");
    
            // Set the email content
            String content = "Querido Usuario,\n\n"
                    + "para resetear tu contraseña utliza el siguiente token:\n"
                    +  resetToken + "\n\n"
                    + "Si usted no solicitó ningun reseteo de contraseña, favor ignorar este mensaje.\n\n"
                    + "Saludos,\n"
                    + "MotriPy";
            message.setText(content);
    
            // Send the email
            Transport.send(message);
    
            System.out.println("Password reset email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send password reset email.");
        }
    }
}