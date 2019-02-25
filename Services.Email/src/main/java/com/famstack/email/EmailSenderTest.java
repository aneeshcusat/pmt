package com.famstack.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;

public class EmailSenderTest
{

    public static void main(String[] args)
    {

        final String username = "famstack.support@course5i.com";//"famstack.bops@gmail.com";
        final String password = FamstackSecurityTokenManager.decrypt("0VC6ba6pleMdt+fg7emvWMkPBbpnVVLQNVHCSK3GGnc=", "NGNW#zcc+N@RY%kSK#46DO+Rzt@j)Ylm");//"Th]zzU{<ucZT/7\"{";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.smtp.host", "Smtp.office365.com");//"smtp.gmail.com");//
        props.put("mail.smtp.host", "m.outlook.com");
        props.put("mail.smtp.port", "587");//"25");//
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.class", 
                "javax.net.ssl.SSLSocketFactory");  

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
            message.setFrom(new InternetAddress(username));
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
