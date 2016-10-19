package com.famstack.projectscheduler.notification.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.util.LimitedQueue;

@Component
public class FamstackDesktopNotificationService extends FamstackBaseNotificationService {

	private final Map<Integer, LimitedQueue<NotificationItem>> userNotificationMap = new HashMap<>();

	private final int notificatioSize = 20;

	@Override
	public void notify(NotificationItem notificationItem) {

		if (notificationItem != null) {
			for (Integer userId : notificationItem.getSubscriberList()) {
				LimitedQueue<NotificationItem> notificationQueue = userNotificationMap.get(userId);
				if (notificationQueue == null) {
					notificationQueue = new LimitedQueue(notificatioSize);
				}
				notificationQueue.add(notificationItem);
			}
		}
	}

	public LimitedQueue<NotificationItem> getNotificatioItems(int userId) {
		LimitedQueue<NotificationItem> notificationQueue = userNotificationMap.get(userId);
		if (notificationQueue != null) {
			return notificationQueue;
		}
		return null;
	}

}
