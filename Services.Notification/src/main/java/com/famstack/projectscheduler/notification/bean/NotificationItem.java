package com.famstack.projectscheduler.notification.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.famstack.projectscheduler.contants.NotificationType;

public abstract class NotificationItem {
	private int messageId;
	private NotificationType notificationType;
	private boolean read = false;
	private String message;
	private Date createdTme;

	private int originUserId;

	private List<Integer> subscriberList;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public String getMessage() {
		read = true;
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getOriginUserId() {
		return originUserId;
	}

	public void setOriginUserId(int originUserId) {
		this.originUserId = originUserId;
	}

	public List<Integer> getSubscriberList() {
		if (subscriberList == null) {
			subscriberList = new ArrayList<>();
		}
		return subscriberList;
	}

	public void setSubscriberList(List<Integer> subscriberList) {
		this.subscriberList = subscriberList;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Date getCreatedTme() {
		return createdTme;
	}

	public void setCreatedTme(Date createdTme) {
		this.createdTme = createdTme;
	}

}
