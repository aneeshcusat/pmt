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
			notificationEmailItem = getNotificationItemForRegistraion(object);
		}

		if (notificationEmailItem != null) {
			for (FamstackBaseNotificationService notificationService : notificationServices) {
				if (notificationService.isEnabled()) {
					notificationService.notify(notificationEmailItem);
				}
			}
		} else {
			logError("Email type " + notificationType + "has not configured! ");
		}
	}

	private NotificationItem getNotificationItemForRegistraion(Object object) {
		NotificationItem notificationEmailItem;
		notificationEmailItem = new NotificationItem();
		EmployeeDetails employeeDetails = (EmployeeDetails) object;
		notificationEmailItem.setEmailTemplate(EmailTemplates.USER_REGISTRAION);
		notificationEmailItem.getToList().add(employeeDetails.getEmail());
		notificationEmailItem.getData().put("userId", String.valueOf(employeeDetails.getId()));
		notificationEmailItem.getData().put("emailId", employeeDetails.getEmail());
		notificationEmailItem.getData().put("password", employeeDetails.getPassword());
		notificationEmailItem.getData().put("phoneNumber", employeeDetails.getMobileNumber());
		return notificationEmailItem;
	}

	public List<FamstackBaseNotificationService> getNotificationServices() {
		return notificationServices;
	}

	public void setNotificationServices(List<FamstackBaseNotificationService> notificationServices) {
		this.notificationServices = notificationServices;
	}

}
