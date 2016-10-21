package com.famstack.projectscheduler.notification.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.famstack.email.contants.EmailTemplates;

public class EmailNotificationItem extends NotificationItem {

	private List<String> toList;
	private Map<String, Object> data;
	private EmailTemplates emailTemplate;
	private final boolean emailEnabled = true;

	public EmailNotificationItem() {
		setCreatedTme(new Date());
	}

	public List<String> getToList() {
		if (toList == null) {
			toList = new ArrayList<>();
		}
		return toList;
	}

	public void setToList(List<String> toList) {
		this.toList = toList;
	}

	public Map<String, Object> getData() {
		if (data == null) {
			data = new HashMap<>();
		}
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public EmailTemplates getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplates emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public boolean isEmailEnabled() {
		return emailEnabled;
	}

}
