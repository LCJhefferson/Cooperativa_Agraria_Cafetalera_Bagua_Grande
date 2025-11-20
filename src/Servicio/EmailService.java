package Servicio;

import com.mysql.cj.Session;
import com.mysql.cj.protocol.Message;
import com.sun.jdi.connect.Transport;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import javax.mail.*;//error dice package javax.mail does not exist
import javax.mail.internet.*;//error dice package javax.mail.internet does not exist

public class EmailService {

    public static void enviarCorreo(String destino, String asunto, String mensaje) {

        final String remitente = "7469163631@untrm.edu.pe";   // CORREO VERIFICADO
        final String usuarioSMTP = "untrm";                  // USUARIO SMTP2GO
        final String claveSMTP = "Asiatic74691636";          // CONTRASEÑA SMTP2GO

        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.smtp2go.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        
        //Session session donde esta por que getInstance creo que no esta creado 
        Session session = Session.getInstance(props, new Authenticator() {// marca error en getInstance
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuarioSMTP, claveSMTP);// marca error en claveSMTP
            }
        });

        try {
            Message message = new MimeMessage(session);// marca error en MimeMessage
            message.setFrom(new InternetAddress(remitente));// marca error en InternetAddress
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
// marca error en RecipientType y en InternetAddress
            message.setSubject(asunto);// marca error en setSubject
            message.setText(mensaje);// marca error en setText

            Transport.send(message);// marca error en send
            System.out.println("Correo enviado correctamente");

        } catch (MessagingException e) {// marca error en MessagingException
            e.printStackTrace();
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
}
