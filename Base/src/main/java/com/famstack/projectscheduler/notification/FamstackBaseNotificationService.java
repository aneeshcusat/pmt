package com.famstack.projectscheduler.notification;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.notification.bean.NotificationItem;

public abstract class FamstackBaseNotificationService extends BaseFamstackService {

	public abstract void notify(NotificationItem notificationItem);

}
