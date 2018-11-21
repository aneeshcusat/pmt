package com.famstack.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSenderTest
{

    public static void main(String[] args)
    {

        final String username = "famstack.bops@gmail.com";
        final String password = "Famstack123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");//Smtp.office365.com");
        props.put("mail.smtp.port", "25");//587");
        props.put("mail.debug", "true");

        System.out.println("Sending email");
        Session session = Session.getInstance(props, new javax.mail.Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("eshopper@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("famstack.bops@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
