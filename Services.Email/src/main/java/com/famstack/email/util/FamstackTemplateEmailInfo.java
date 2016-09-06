package com.famstack.email.util;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.famstack.projectscheduler.BaseFamstackService;

/**
 * The Class DeliveryInterfaceTemplateEmailInfo.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class FamstackTemplateEmailInfo extends BaseFamstackService {

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

    /**
     * Gets the mail from.
     *
     * @return the mail from
     */
    public String getMailFrom() {
        return getConfigValue("emailFrom") != null ? getConfigValue("emailFrom") : mailFrom;
    }

    /**
     * Sets the mail from.
     *
     * @param mailFrom the new mail from
     */
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    /**
     * Gets the mail to.
     *
     * @return the mail to
     */
    public String getMailTo() {
        return getConfigValue("emailTo") != null ? getConfigValue("emailTo") : mailTo;
    }

    /**
     * Sets the mail to.
     *
     * @param mailTo the new mail to
     */
    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    /**
     * Gets the mail cc.
     *
     * @return the mail cc
     */
    public String getMailCc() {
        return getConfigValue("emailCc") != null ? getConfigValue("emailCc") : mailCc;
    }

    /**
     * Sets the mail cc.
     *
     * @param mailCc the new mail cc
     */
    public void setMailCc(String mailCc) {
        this.mailCc = mailCc;
    }

    /**
     * Gets the mail bcc.
     *
     * @return the mail bcc
     */
    public String getMailBcc() {
        return getConfigValue("emailBcc") != null ? getConfigValue("emailBcc") : mailBcc;
    }

    /**
     * Sets the mail bcc.
     *
     * @param mailBcc the new mail bcc
     */
    public void setMailBcc(String mailBcc) {
        this.mailBcc = mailBcc;
    }

    /**
     * Gets the velocity template name.
     *
     * @return the velocity template name
     */
    public String getVelocityTemplateName() {
        return velocityTemplateName;
    }

    /**
     * Sets the velocity template name.
     *
     * @param velocityTemplateName the new velocity template name
     */
    public void setVelocityTemplateName(String velocityTemplateName) {
        this.velocityTemplateName = velocityTemplateName;
    }

    /**
     * Gets the mail subject.
     *
     * @return the mail subject
     */
    public String getMailSubject() {
        return getConfigValue("emailSubject") != null ? getConfigValue("emailSubject") : mailSubject;
    }

    /**
     * Sets the mail subject.
     *
     * @param mailSubject the new mail subject
     */
    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    /**
     * Copy properties to.
     *
     * @param pCopy the copy
     */
    public void copyPropertiesTo(FamstackTemplateEmailInfo pCopy) {
        pCopy.velocityTemplateName = velocityTemplateName;
        pCopy.mailSubject = mailSubject;
    }

    /**
     * Gets the template parameters.
     *
     * @return the template parameters
     */
    public Map<String, Object> getTemplateParameters() {
        return templateParameters;
    }

    /**
     * Sets the template parameters.
     *
     * @param templateParameters the template parameters
     */
    public void setTemplateParameters(Map<String, Object> templateParameters) {
        this.templateParameters = templateParameters;
    }

    /**
     * Gets the mime message.
     *
     * @return the mime message
     */
    public MimeMessage getMimeMessage() {
        return mimeMessage;
    }

    /**
     * Sets the mime message.
     *
     * @param mimeMessage the new mime message
     */
    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    /**
     * Gets the config value.
     *
     * @param propertyName the property name
     * @return the config value
     */
    private String getConfigValue(String propertyName) {
        return getFamstackApplicationConfiguration().getConfigSettings().get(propertyName);
    }

}
