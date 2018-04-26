package com.codecool.shop.order;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private String to;

    // Sender's email ID needs to be mentioned
    private String from = "avajshop@gmail.com";
    final String username = "avajshop";//change accordingly
    final String password = "Sitteske420";//change accordingly

    // Assuming you are sending email through relay.jangosmtp.net
    String host = "smtp.gmail.com";

    public EmailSender(String to) {
        this.to = to;
    }

    public void sendMail(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.user", from);

        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);

        try {

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Successful order from aVajShőp!");

            // Now set the actual message
            message.setText("Congratulations, your order was successful " +
                    "on aVajShop. ");

            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
