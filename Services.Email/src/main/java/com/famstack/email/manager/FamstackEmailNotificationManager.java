package com.famstack.email.manager;

import java.util.Map;

import javax.annotation.Resource;

import com.famstack.email.FamstackEmailSender;
import com.famstack.email.contants.EmailTemplates;
import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.projectscheduler.util.StringUtils;

public class FamstackEmailNotificationManager {

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
	public void sendEmailNotificationForError(Map<String, Object> dataObjMap) {
		Map<String, Object> dataMap = dataObjMap;

		String templateName = (String) dataMap.get("templateName");

		if (!StringUtils.isNotBlank(templateName)) {
			templateName = EmailTemplates.DEFAULT.getValue();
		}
		famstackTemplateEmailInfo.setVelocityTemplateName(templateName);
		famstackTemplateEmailInfo.setTemplateParameters(dataMap);
		famstackEmailSender.sendEmail(famstackTemplateEmailInfo);
	}

}
