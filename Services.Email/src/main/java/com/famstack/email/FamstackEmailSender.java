package com.famstack.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.email.velocity.FamstackEmailTemplateRendererService;
import com.famstack.projectscheduler.BaseFamstackService;

public class FamstackEmailSender extends BaseFamstackService {

	/** The java mail sender. */
	@Autowired
	private JavaMailSender javaMailSender;

	/** The delivery interface email template renderer service. */
	@Autowired
	private FamstackEmailTemplateRendererService famstackEmailTemplateRendererService;

	/**
	 * Send email.
	 *
	 * @param emailInfo
	 *            the email info
	 */
	public void sendEmail(FamstackTemplateEmailInfo emailInfo, String... emailsToList) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		emailInfo.setMimeMessage(mimeMessage);

		javaMailSender.send(famstackEmailTemplateRendererService.createMessage(emailInfo, emailsToList));
	}

}
