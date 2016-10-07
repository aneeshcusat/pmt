package com.famstack.projectscheduler.notification;

import com.famstack.email.manager.FamstackEmailNotificationManager;
import com.famstack.projectscheduler.notification.bean.NotificationItem;

public class FamstackEmailNotificationService extends FamstackBaseNotificationService {

	FamstackEmailNotificationManager FamstackEmailNotificationManager;

	@Override
	public void notify(NotificationItem notificationItem) {

		FamstackEmailNotificationManager.sendEmailNotificationForError(null);

	}

}
