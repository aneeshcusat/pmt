package com.famstack.projectscheduler.notification.services;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.notification.bean.NotificationItem;

public abstract class FamstackBaseNotificationService extends BaseFamstackService {

	public abstract void notify(NotificationItem notificationItem);

	public boolean enabled;

	public boolean isEnabled() {
		return getFamstackApplicationConfiguration().isEmailEnabled();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
