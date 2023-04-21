package org.example.Database.Classes.HandlerClasses;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailMessage {
    public static Boolean isSentMessage(String messageText, String mail, String messageTheme) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sankevich2003@mail.ru", "w9PmQnnACdPXzxGNgeaH");
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sankevich2003@mail.ru"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject(messageTheme);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("Send");

            return true;

        } catch (MessagingException e) {
            return false;
        }
    }
}
