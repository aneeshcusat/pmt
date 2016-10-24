package com.famstack.projectscheduler.notification.services;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.famstack.email.FamstackEmailSender;
import com.famstack.email.contants.EmailMessages;
import com.famstack.email.contants.EmailTemplates;
import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.projectscheduler.notification.bean.EmailNotificationItem;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.util.StringUtils;

@Component
public class FamstackEmailNotificationService extends FamstackBaseNotificationService {

	@Resource
	FamstackEmailSender famstackEmailSender;

	@Resource
	FamstackTemplateEmailInfo famstackTemplateEmailInfo;

	/**
	 * Send email notification for error.
	 *
	 * @param orderExceptionState
	 *            the order exception state
	 * @param dataObjMap
	 *            the data map
	 * @param deliveryOrderItem
	 *            the delivery order item
	 */
	@Async
	@Override
	public void notify(NotificationItem notificationItem) {

		EmailNotificationItem emailNotificationItem = (EmailNotificationItem) notificationItem;
		if (!emailNotificationItem.getToList().isEmpty() && emailNotificationItem.isEmailEnabled()) {
			Map<String, Object> dataMap = emailNotificationItem.getData();

			String templateName = emailNotificationItem.getEmailTemplate().getTemplate();

			if (!StringUtils.isNotBlank(templateName)) {
				templateName = EmailTemplates.DEFAULT.getTemplate();
			}

			String mailSubject = getNotificationSubject(emailNotificationItem.getEmailTemplate());

			famstackTemplateEmailInfo.setMailSubject(mailSubject);
			famstackTemplateEmailInfo.setVelocityTemplateName(templateName);
			famstackTemplateEmailInfo.setTemplateParameters(dataMap);
			famstackEmailSender.sendEmail(famstackTemplateEmailInfo,
					emailNotificationItem.getToList().toArray(new String[0]));
		} else {
			logDebug("Unable to send emails" + emailNotificationItem.getToList());
		}
	}

	private String getNotificationSubject(EmailTemplates emailTemplate) {
		return EmailMessages.getString(emailTemplate.getSubjectkey());
	}

}
