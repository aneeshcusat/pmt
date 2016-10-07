package com.famstack.email.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.email.FamstackEmailSender;
import com.famstack.email.contants.EmailMessages;
import com.famstack.email.contants.EmailTemplates;
import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.projectscheduler.notification.FamstackBaseNotificationService;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.util.StringUtils;

@Component
public class FamstackEmailNotificationService extends FamstackBaseNotificationService {

	/** The delivery interface email sender. */
	@Resource
	FamstackEmailSender famstackEmailSender;

	/** The delivery interface template email info. */
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
	@Override
	public void notify(NotificationItem notificationItem) {
		Map<String, Object> dataMap = notificationItem.getData();

		String templateName = notificationItem.getEmailTemplate().getValue();

		if (!StringUtils.isNotBlank(templateName)) {
			templateName = EmailTemplates.DEFAULT.getValue();
		}

		String mailSubject = getNotificationSubject(notificationItem.getEmailTemplate());

		famstackTemplateEmailInfo.setMailSubject(mailSubject);
		famstackTemplateEmailInfo.setVelocityTemplateName(templateName);
		famstackTemplateEmailInfo.setTemplateParameters(dataMap);
		famstackEmailSender.sendEmail(famstackTemplateEmailInfo, notificationItem.getToList().toArray(new String[0]));
	}

	private String getNotificationSubject(EmailTemplates emailTemplate) {
		return EmailMessages.getString(emailTemplate.getValue());
	}

}
