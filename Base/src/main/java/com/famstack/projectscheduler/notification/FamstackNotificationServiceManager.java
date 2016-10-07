package com.famstack.projectscheduler.notification;

import java.util.List;

import com.famstack.projectscheduler.BaseFamstackService;
import com.walmart.axon.NotificationType;

public class FamstackNotificationServiceManager extends BaseFamstackService {

	private List<FamstackBaseNotificationService> notificationServices;

	public void notifyAll(NotificationType notificationType, Object object) {

		for (FamstackBaseNotificationService notificationService : notificationServices) {
			notificationService.notify();
		}
	}
}
