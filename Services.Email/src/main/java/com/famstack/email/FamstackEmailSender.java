package com.famstack.email;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.email.velocity.FamstackEmailTemplateRendererService;
import com.famstack.projectscheduler.BaseFamstackService;

public class FamstackEmailSender extends BaseFamstackService
{

    /** The java mail sender. */
    @Autowired
    private JavaMailSender javaMailSender;

    /** The delivery interface email template renderer service. */
    @Autowired
    private FamstackEmailTemplateRendererService famstackEmailTemplateRendererService;

    /**
     * Send email.
     * 
     * @param emailInfo the email info
     */
    public void sendEmail(FamstackTemplateEmailInfo emailInfo, String... emailsToList)
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        emailInfo.setMimeMessage(mimeMessage);
        logDebug("emailsToList" + emailsToList);
        javaMailSender.send(famstackEmailTemplateRendererService.createMessage(emailInfo, emailsToList));
    }

    public void sendTextMessage(String from, String to, String subject, String messageBody)
    {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageBody);
            javaMailSender.send(message);
        } catch (Exception e) {
            logError(e.getMessage());
        }

    }
}
