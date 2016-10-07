package com.famstack.projectscheduler.notification.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.famstack.email.contants.EmailTemplates;

public class NotificationItem {

	private List<String> toList;
	private Map<String, Object> data;
	private EmailTemplates emailTemplate;

	public List<String> getToList() {
		if (toList == null) {
			return new ArrayList<>();
		}
		return toList;
	}

	public void setToList(List<String> toList) {
		this.toList = toList;
	}

	public Map<String, Object> getData() {
		if (data == null) {
			return new HashMap<>();
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

}
