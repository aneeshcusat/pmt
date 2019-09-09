package com.famstack.email.velocity;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.famstack.email.util.EmailMessage;
import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.projectscheduler.BaseFamstackService;

/**
 * The Class famstackEmailTemplateRendererService.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class FamstackEmailTemplateRendererService extends BaseFamstackService {

	/** The Constant VELOCITY_TEMPLATES_EMAIL. */
	private static final String VELOCITY_TEMPLATES_EMAIL = "/velocity/templates/";

	/** The Constant TEMPLATE_NAME_PLACEHOLDER. */
	private static final String TEMPLATE_NAME_PLACEHOLDER = "@templateName@";

	/** The html template. */
	private final String htmlTemplate = VELOCITY_TEMPLATES_EMAIL + "email/@templateName@.vm";

	/** The text template. */
	private final String textTemplate = VELOCITY_TEMPLATES_EMAIL + "plaintext_email/@templateName@-pt.vm";

	/** The delivery interface velocity service. */
	@Autowired
	private FamstackVelocityService famstackVelocityService;

	/**
	 * Creates the message.
	 *
	 * @param emailInfo
	 *            the email info
	 * @param emailsToList
	 *            the emails to
	 * @return the mime message preparator
	 */
	public MimeMessagePreparator createMessage(FamstackTemplateEmailInfo emailInfo, String[] emailsCCList, String[] emailsBCCList, String... emailsToList) {

		String[] emailsTo = emailsToList;
		String[] emailsCCs = emailsCCList;
		String[] emailsBCCs = emailsBCCList;
		
		try {

			EmailMessage emailMessage = renderEmailMessage(emailInfo);
			
			if (emailsTo == null || emailsTo.length == 0) {
				emailsTo = new String[1];
				emailsTo[0] = emailInfo.getMailTo();
			}
			
			if ((emailsCCs == null || emailsCCs.length == 0 ) && emailInfo.getMailCc() != null && emailInfo.getMailCc().length() > 0) {
				emailsCCs = new String[1];
				emailsCCs[0] = emailInfo.getMailCc();
			}
			
			if ((emailsBCCs == null || emailsBCCs.length == 0 ) && emailInfo.getMailBcc() != null && emailInfo.getMailBcc().length() > 0) {
				emailsBCCs = new String[1];
				emailsBCCs[0] = emailInfo.getMailBcc();
			}
			return mapPropertiesToMessage(emailInfo.getMailFrom(), emailMessage, emailsCCs,
					emailsBCCs, emailsTo);
			
		} catch (Exception e) {
			logError("unable to render the template " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Render email message.
	 *
	 * @param emailInfo
	 *            the email info
	 * @return the email message
	 */
	public EmailMessage renderEmailMessage(FamstackTemplateEmailInfo emailInfo) {
		try {
			Map<String, Object> data = emailInfo.getTemplateParameters();
			String templateURL = emailInfo.getVelocityTemplateName();
			String body = "";
			try {
				body = famstackVelocityService
						.evaluateTemplate(getTextTemplate().replaceFirst(TEMPLATE_NAME_PLACEHOLDER, templateURL), data);
			} catch (ResourceNotFoundException re) {
				logError("Unable to load template");
			}

			String htmlBody = famstackVelocityService
					.evaluateTemplate(getHtmlTemplate().replaceFirst(TEMPLATE_NAME_PLACEHOLDER, templateURL), data);
			String subject = emailInfo.getMailSubject();
			return new EmailMessage(famstackVelocityService.evaluateString(subject, data),
					famstackVelocityService.evaluateString(body, data),
					famstackVelocityService.evaluateString(htmlBody, data));
		} catch (Exception e) {
			logError("unable to render velocity template " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Map properties to message.
	 *
	 * @param messageFrom
	 *            the message from
	 * @param emailMessage
	 *            the email message
	 * @param emailsCC
	 *            the emails CC
	 * @param emailsBCC
	 *            the emails BCC
	 * @param emailsTo
	 *            the emails to
	 * @return the mime message preparator
	 * @throws MessagingException
	 *             the messaging exception
	 */
	protected MimeMessagePreparator mapPropertiesToMessage(final String messageFrom, final EmailMessage emailMessage,
			final String[] emailsCCs, final String[] emailsBCCs, final String... emailsTo) throws MessagingException {

		return new MimeMessagePreparator() {
			@Override
			public void prepare(final MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				messageHelper.setFrom(new InternetAddress(messageFrom));
				for (String emailTo : emailsTo) {
					messageHelper.addTo(emailTo);
				}
				messageHelper.setText(emailMessage.getBody(), emailMessage.getHtmlBody());
				messageHelper.setSubject(emailMessage.getSubject());
				
				if(emailsCCs != null) {
					for (String emailCC : emailsCCs) {
						messageHelper.addCc(emailCC);
					}
				}
				if(emailsBCCs != null) {
					for (String emailBCC : emailsBCCs) {
						messageHelper.addBcc(emailBCC);
					}
				}
			}
		};
	}

	/**
	 * Gets the html template.
	 *
	 * @return the html template
	 */
	public String getHtmlTemplate() {
		return htmlTemplate;
	}

	/**
	 * Gets the text template.
	 *
	 * @return the text template
	 */
	public String getTextTemplate() {
		return textTemplate;
	}
}
