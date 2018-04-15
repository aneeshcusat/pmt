package com.famstack.email.util;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.famstack.projectscheduler.BaseFamstackService;

/**
 * The Class famstackTemplateEmailInfo.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class FamstackTemplateEmailInfo extends BaseFamstackService
{

    /** The mail from. */
    private String mailFrom;

    /** The mail to. */
    private String mailTo;

    /** The mail cc. */
    private String mailCc;

    /** The mail bcc. */
    private String mailBcc;

    /** The velocity template name. */
    private String velocityTemplateName;

    /** The mail subject. */
    private String mailSubject;

    /** The mime message. */
    private MimeMessage mimeMessage;

    /** The template parameters. */
    Map<String, Object> templateParameters;

    public String getMailFrom()
    {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom)
    {
        this.mailFrom = mailFrom;
    }

    public String getMailTo()
    {
        return mailTo;
    }

    public void setMailTo(String mailTo)
    {
        this.mailTo = mailTo;
    }

    public String getMailCc()
    {
        return mailCc;
    }

    public void setMailCc(String mailCc)
    {
        this.mailCc = mailCc;
    }

    public String getMailBcc()
    {
        return mailBcc;
    }

    public void setMailBcc(String mailBcc)
    {
        this.mailBcc = mailBcc;
    }

    public String getVelocityTemplateName()
    {
        return velocityTemplateName;
    }

    public void setVelocityTemplateName(String velocityTemplateName)
    {
        this.velocityTemplateName = velocityTemplateName;
    }

    public String getMailSubject()
    {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject)
    {
        this.mailSubject = mailSubject;
    }

    public MimeMessage getMimeMessage()
    {
        return mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage)
    {
        this.mimeMessage = mimeMessage;
    }

    public Map<String, Object> getTemplateParameters()
    {
        return templateParameters;
    }

    public void setTemplateParameters(Map<String, Object> templateParameters)
    {
        this.templateParameters = templateParameters;
    }

}
