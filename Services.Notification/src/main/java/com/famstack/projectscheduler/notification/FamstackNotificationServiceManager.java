package com.famstack.projectscheduler.notification;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.famstack.email.contants.EmailTemplates;
import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.notification.bean.EmailNotificationItem;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.notification.services.FamstackBaseNotificationService;

@Service
public class FamstackNotificationServiceManager extends BaseFamstackService {

	private List<FamstackBaseNotificationService> notificationServices;

	@Async
	public void notifyAll(NotificationType notificationType, Object object) {
		logDebug("notification " + notificationType);
		NotificationItem notificationEmailItem = null;

		switch (notificationType) {
		case USER_REGISTRAION:
			notificationEmailItem = getNotificationItemForRegistraion(object);
			break;
		case RESET_PASSWORD:
			break;
		case PROJECT_CREATE:
			break;
		case PROJECT_DELETE:
			break;
		case PROJECT_UPDATE:
			break;
		case TASK_ASSIGNED:
			break;
		case TASK_CREATED:
			break;
		case TASK_CREATED_ASSIGNED:
			break;
		case TASK_DELETED:
			break;
		case TASK_UPDATED:
			break;
		case USER_UPDATE:
			break;
		default:
			break;
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
		EmailNotificationItem notificationEmailItem;
		notificationEmailItem = new EmailNotificationItem();
		EmployeeDetails employeeDetails = (EmployeeDetails) object;
		notificationEmailItem.setOriginUserId(employeeDetails.getId());
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
