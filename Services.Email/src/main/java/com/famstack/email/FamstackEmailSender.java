package com.famstack.email;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.email.velocity.FamstackEmailTemplateRendererService;
import com.famstack.projectscheduler.BaseFamstackService;

public class FamstackEmailSender extends BaseFamstackService
{

    /** The java mail sender. */
    @Autowired
    private FamstackJavaMailSenderImpl javaMailSender;

    /** The delivery interface email template renderer service. */
    @Autowired
    private FamstackEmailTemplateRendererService famstackEmailTemplateRendererService;

    /**
     * Send email.
     * 
     * @param emailInfo the email info
     */
    public void sendEmail(FamstackTemplateEmailInfo emailInfo,  String[] emailsCCList, String[] emailsBCCList, String... emailsToList)
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        emailInfo.setMimeMessage(mimeMessage);
        javaMailSender.send(famstackEmailTemplateRendererService.createMessage(emailInfo, emailsCCList, emailsBCCList, emailsToList));
    }

    public void sendTextMessage(String subject, String messageBody)
    {
    	String from = "famstack.support@course5i.com";
    	String to = "famstack.bops@gmail.com";
    	 try {
    		MimeMessage message = javaMailSender.createMimeMessage();
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
