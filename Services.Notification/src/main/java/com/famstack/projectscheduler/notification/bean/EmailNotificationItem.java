package com.famstack.projectscheduler.notification.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.famstack.email.contants.EmailTemplates;

public class EmailNotificationItem extends NotificationItem {

	private Set<String> toList;
	private Map<String, Object> data;
	private EmailTemplates emailTemplate;
	private final boolean emailEnabled = true;

	public EmailNotificationItem() {
		setCreatedTme(new Date());
	}

	public Set<String> getToList() {
		if (toList == null) {
			toList = new HashSet<>();
		}
		return toList;
	}

	public void setToList(Set<String> toList) {
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
