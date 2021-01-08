package pl.edu.agh.iisg.to.battleships.model.email;


import pl.edu.agh.iisg.to.battleships.Main;
import pl.edu.agh.iisg.to.battleships.model.EasyConfigParser;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    static private final String emailConfigPathResource = "/emailConfig";

    static EasyConfigParser parser = new EasyConfigParser( Main.class.getResource(emailConfigPathResource).getPath().replace("%20", " ") );
    // .replace() jest tu po to, by program działał, gdy w ścieżce do pliku znajdą się spacje

    public EmailSender(){
        sendEmail("kubakub2@wp.pl", "title", "testowy");
    }

    public void sendEmail(String recipient_address, String subject, String data){
        System.out.println("Sending an email to '" + recipient_address + "'.");
        Properties session_properties = System.getProperties();

        String smtp_host = parser.getFromKey("smtp_server");
        String sender_address = parser.getFromKey("email");
        String sender_password = parser.getFromKey("password");
        String smtp_port = parser.getFromKey("smtp_port");

        session_properties.put("mail.smtp.host", smtp_host);
        session_properties.put("mail.smtp.user", sender_address);
        session_properties.put("mail.smtp.password", sender_password);
        session_properties.put("mail.smtp.port", smtp_port);
        session_properties.put("mail.smtp.auth", "true");
        session_properties.put("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(session_properties);
            MimeMessage message = new MimeMessage(session);

            Transport transport = session.getTransport("smtp");
            transport.close();
            transport.connect(smtp_host, sender_address, sender_password);

            message.setFrom(new InternetAddress(sender_address));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient_address));
            message.setSubject(subject);
            message.setText(data);

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException | IllegalStateException e) {
            e.printStackTrace();
        }
    }



}
