package com.famstack.email.util;

/**
 * The Class EmailMessage.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class EmailMessage {

    /** The subject. */
    private final String subject;

    /** The body. */
    private final String body;

    /** The html body. */
    private final String htmlBody;

    /**
     * Instantiates a new email message.
     *
     * @param subject the subject
     * @param htmlBody the html body
     */
    public EmailMessage(String subject, String htmlBody) {
        this.subject = subject;
        this.body = null;
        this.htmlBody = htmlBody;
    }

    /**
     * Instantiates a new email message.
     *
     * @param subject the subject
     * @param body the body
     * @param htmlBody the html body
     */
    public EmailMessage(String subject, String body, String htmlBody) {
        this.subject = subject;
        this.body = body;
        this.htmlBody = htmlBody;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Gets the html body.
     *
     * @return the html body
     */
    public String getHtmlBody() {
        return htmlBody;
    }

}
