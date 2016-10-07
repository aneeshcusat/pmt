package com.famstack.projectscheduler.notification;

import java.util.List;

import org.springframework.stereotype.Component;

import com.famstack.email.contants.EmailTemplates;
import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.notification.bean.NotificationItem;

@Component
public class FamstackNotificationServiceManager extends BaseFamstackService {

	private List<FamstackBaseNotificationService> notificationServices;

	public void notifyAll(NotificationType notificationType, Object object) {
		NotificationItem notificationEmailItem = null;
		if (notificationType == NotificationType.USER_REGISTRAION) {
			EmployeeDetails employeeDetails = (EmployeeDetails) object;
			notificationEmailItem.setEmailTemplate(EmailTemplates.USER_REGISTRAION);
			notificationEmailItem.getToList().add(employeeDetails.getEmail());
			notificationEmailItem.getData().put("userId", String.valueOf(employeeDetails.getId()));
			notificationEmailItem.getData().put("emailId", employeeDetails.getEmail());
			notificationEmailItem.getData().put("password", employeeDetails.getConfirmPassword());
		}
		for (FamstackBaseNotificationService notificationService : notificationServices) {
			notificationService.notify(notificationEmailItem);
		}
	}
}
