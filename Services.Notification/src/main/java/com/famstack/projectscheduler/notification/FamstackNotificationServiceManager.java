package com.famstack.projectscheduler.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.famstack.email.contants.EmailTemplates;
import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.notification.bean.EmailNotificationItem;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.notification.services.FamstackBaseNotificationService;
import com.famstack.projectscheduler.util.StringUtils;

@Service
public class FamstackNotificationServiceManager extends BaseFamstackService {

	private List<FamstackBaseNotificationService> notificationServices;

	// @Async
	public void notifyAll(NotificationType notificationType, Object object) {
		logDebug("notification " + notificationType);
		NotificationItem notificationEmailItem = null;

		switch (notificationType) {
		case USER_REGISTRAION:
			notificationEmailItem = getNotificationItemForRegistraion(object);
			break;
		case RESET_PASSWORD:
			notificationEmailItem = getNotificationItemForResetPassword(object);
			break;
		case USER_UPDATE:
			notificationEmailItem = getNotificationItemForUserUpdate(object);
			break;
		case PROJECT_CREATE:
			notificationEmailItem = getNotificationItemForProjectCreate(object);
			break;
		case PROJECT_UPDATE:
			notificationEmailItem = getNotificationItemForProjectUpdate(object);
			break;
		case PROJECT_DELETE:
			notificationEmailItem = getNotificationItemForProjectDelete(object);
			break;
		case TASK_CREATED:
			notificationEmailItem = getNotificationItemForProjectTaskCreated(object);
			break;
		case TASK_CREATED_ASSIGNED:
			notificationEmailItem = getNotificationItemForProjectTaskCreatedAssigned(object);
			break;
		case TASK_UPDATED:
			notificationEmailItem = getNotificationItemForProjectTaskUpdated(object);
			break;
		case TASK_ASSIGNED:
			notificationEmailItem = getNotificationItemForProjectTaskAssigned(object);
			break;
		case TASK_DELETED:
			notificationEmailItem = getNotificationItemForProjectTaskDeleted(object);
			break;
		case TASK_COMPLETED:
			notificationEmailItem = getNotificationItemForProjectTaskDeleted(object);
			break;
		case TASK_INPROGRESS:
			notificationEmailItem = getNotificationItemForProjectTaskDeleted(object);
			break;
		case TASK_CLOSED:
			notificationEmailItem = getNotificationItemForProjectTaskDeleted(object);
			break;
		default:
			break;
		}

		if (notificationEmailItem != null) {
			notificationEmailItem.setOriginUserId(0);
			notificationEmailItem.setNotificationType(notificationType);
			for (FamstackBaseNotificationService notificationService : notificationServices) {

				if (notificationService.isEnabled()) {
					logDebug("Sending notification...");
					notificationService.notify(notificationEmailItem);
				}
			}
		} else {
			logError("Email type " + notificationType + "has not configured! ");
		}
	}

	private NotificationItem getNotificationItemForProjectCreate(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.PROJECT_CREATE);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForProjectUpdate(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.PROJECT_UPDATE);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForProjectDelete(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.PROJECT_DELETE);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForProjectTaskCreated(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectTaskStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.TASK_CREATED);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForProjectTaskCreatedAssigned(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectTaskStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.TASK_CREATED_ASSIGNED);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForProjectTaskAssigned(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectTaskStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.TASK_ASSIGNED);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForProjectTaskUpdated(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectTaskStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.TASK_UPDATED);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForProjectTaskDeleted(Object object) {
		EmailNotificationItem notificationEmailItem = getProjectTaskStatusNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.TASK_DELETED);
		return notificationEmailItem;
	}

	private EmailNotificationItem getProjectStatusNotificationItem(Object object) {
		EmailNotificationItem notificationEmailItem;
		notificationEmailItem = new EmailNotificationItem();
		ProjectDetails projectDetails = (ProjectDetails) object;
		notificationEmailItem.getData().put("url", getFamstackApplicationConfiguration().getUrl());
		notificationEmailItem.setToList(getToListForProjectUpdates(projectDetails));
		notificationEmailItem.setSubscriberList(getSubscribersIdForProjectUpdates(projectDetails));
		notificationEmailItem.getData().put("id", projectDetails.getId());
		notificationEmailItem.getData().put("name", projectDetails.getName());
		notificationEmailItem.getData().put("code", projectDetails.getCode());
		notificationEmailItem.getData().put("description", projectDetails.getDescription());
		notificationEmailItem.getData().put("createdDate", projectDetails.getCreatedDate());
		notificationEmailItem.getData().put("lastModifiedDate", projectDetails.getLastModifiedDate());
		notificationEmailItem.getData().put("projectType", projectDetails.getType());
		notificationEmailItem.getData().put("complexity", projectDetails.getComplexity());
		notificationEmailItem.getData().put("accountName", projectDetails.getAccountName());
		notificationEmailItem.getData().put("quantity", projectDetails.getQuantity());
		notificationEmailItem.getData().put("reporterName", projectDetails.getReporterName());
		notificationEmailItem.getData().put("category", projectDetails.getCategory());
		notificationEmailItem.getData().put("priority", projectDetails.getPriority());
		notificationEmailItem.getData().put("startTime", projectDetails.getStartTime());
		notificationEmailItem.getData().put("completionTime", projectDetails.getCompletionTime());
		notificationEmailItem.getData().put("duration", projectDetails.getDuration());
		notificationEmailItem.getData().put("status", projectDetails.getStatus());
		notificationEmailItem.getData().put("PONumber", projectDetails.getPONumber());
		notificationEmailItem.getData().put("clientName", projectDetails.getClientName());
		notificationEmailItem.getData().put("teamName", projectDetails.getTeamName());
		notificationEmailItem.getData().put("subTeamName", projectDetails.getSubTeamName());

		return notificationEmailItem;
	}

	private EmailNotificationItem getProjectTaskStatusNotificationItem(Object object) {
		EmailNotificationItem notificationEmailItem = null;
		Map<String, Object> dataMap = (Map<String, Object>) object;

		if (dataMap != null && !dataMap.isEmpty()) {
			notificationEmailItem = new EmailNotificationItem();
			notificationEmailItem.getData().put("url", getFamstackApplicationConfiguration().getUrl());
			TaskDetails taskDetails = (TaskDetails) dataMap.get("taskDetails");
			ProjectDetails projectDetails = (ProjectDetails) dataMap.get("projectDetails");

			notificationEmailItem.setToList(getToListForProjectUpdates(projectDetails));
			notificationEmailItem.getToList().addAll(getToListForTaskUpdate(taskDetails));

			notificationEmailItem.setSubscriberList(getSubscribersIdForProjectUpdates(projectDetails));
			notificationEmailItem.getSubscriberList().addAll(getSubscribersIdForProjectUpdates(taskDetails));

			notificationEmailItem.getData().put("id", taskDetails.getTaskId());
			notificationEmailItem.getData().put("projectId", taskDetails.getProjectId());
			notificationEmailItem.getData().put("name", taskDetails.getName());
			notificationEmailItem.getData().put("description", taskDetails.getDescription());
			notificationEmailItem.getData().put("createdDate", taskDetails.getCreatedDate());
			notificationEmailItem.getData().put("lastModifiedDate", taskDetails.getLastModifiedDate());
			notificationEmailItem.getData().put("reporterName", taskDetails.getReporterName());
			notificationEmailItem.getData().put("startTime", taskDetails.getStartTime());
			notificationEmailItem.getData().put("completionTime", taskDetails.getCompletionTime());
			notificationEmailItem.getData().put("duration", taskDetails.getDuration());
			notificationEmailItem.getData().put("status", taskDetails.getStatus());
		}
		return notificationEmailItem;
	}

	private List<String> getToListForTaskUpdate(TaskDetails taskDetails) {
		List<String> toList = new ArrayList<>();
		String helpers = taskDetails.getHelpersList();
		if (StringUtils.isNotBlank(helpers)) {
			String[] helperArray = helpers.split(",");
			for (String helper : helperArray) {
				EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getUserMap()
						.get(Integer.parseInt(helper));

				if (employeeDetails != null) {
					toList.add(employeeDetails.getEmail());
				}
			}

			EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getUserMap()
					.get(taskDetails.getAssignee());

			if (employeeDetails != null) {
				toList.add(employeeDetails.getEmail());
			}
		}
		logDebug("getToListForTaskUpdate" + toList);
		return toList;
	}

	private List<String> getToListForProjectUpdates(ProjectDetails projectDetails) {
		List<String> toList = new ArrayList<>();
		String reporterEmail = projectDetails.getReporter().getUserId();
		String watchers = projectDetails.getWatchers();
		if (StringUtils.isNotBlank(watchers)) {
			String[] watchersArray = watchers.split(",");
			toList.addAll(Arrays.asList(watchersArray));
		}
		toList.add(reporterEmail);
		logDebug("getToListForProjectUpdates" + toList);
		return toList;
	}

	private List<Integer> getSubscribersIdForProjectUpdates(ProjectDetails projectDetails) {
		List<Integer> toList = new ArrayList<>();
		int reporterId = projectDetails.getReporter().getId();
		String watchers = projectDetails.getWatchers();
		if (StringUtils.isNotBlank(watchers)) {
			String[] watchersArray = watchers.split(",");
			for (String watcher : watchersArray) {
				Integer watcherId = FamstackApplicationConfiguration.getUserIdMap().get(watcher);

				if (watcherId != null) {
					toList.add(watcherId);
				}
			}
		}
		toList.add(reporterId);
		logDebug("getSubscribersIdForProjectUpdates" + toList);
		return toList;
	}

	private List<Integer> getSubscribersIdForProjectUpdates(TaskDetails taskDetails) {
		List<Integer> toList = new ArrayList<>();
		String helpers = taskDetails.getHelpersList();
		if (StringUtils.isNotBlank(helpers)) {
			String[] helperArray = helpers.split(",");
			for (String helper : helperArray) {
				toList.add(Integer.parseInt(helper));
			}
			toList.add(taskDetails.getAssignee());
		}
		logDebug("getSubscribersIdForProjectUpdates" + toList);
		return toList;
	}

	private NotificationItem getNotificationItemForRegistraion(Object object) {
		EmailNotificationItem notificationEmailItem = getUserNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.USER_REGISTRAION);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForResetPassword(Object object) {
		EmailNotificationItem notificationEmailItem = getUserNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.RESET_PASSWORD);
		return notificationEmailItem;
	}

	private NotificationItem getNotificationItemForUserUpdate(Object object) {
		EmailNotificationItem notificationEmailItem = getUserNotificationItem(object);
		notificationEmailItem.setEmailTemplate(EmailTemplates.USER_UPDATE);
		return notificationEmailItem;
	}

	private EmailNotificationItem getUserNotificationItem(Object object) {
		if (object == null) {
			return null;
		}
		EmailNotificationItem notificationEmailItem;
		notificationEmailItem = new EmailNotificationItem();
		EmployeeDetails employeeDetails = (EmployeeDetails) object;
		notificationEmailItem.setOriginUserId(employeeDetails.getId());
		notificationEmailItem.getToList().add(employeeDetails.getEmail());
		notificationEmailItem.getData().put("userId", String.valueOf(employeeDetails.getId()));
		notificationEmailItem.getData().put("emailId", employeeDetails.getEmail());
		notificationEmailItem.getData().put("firstName", employeeDetails.getFirstName());
		notificationEmailItem.getData().put("password", employeeDetails.getPassword());
		notificationEmailItem.getData().put("phoneNumber", employeeDetails.getMobileNumber());
		notificationEmailItem.getData().put("url", getFamstackApplicationConfiguration().getUrl());
		return notificationEmailItem;
	}

	public List<FamstackBaseNotificationService> getNotificationServices() {
		return notificationServices;
	}

	public void setNotificationServices(List<FamstackBaseNotificationService> notificationServices) {
		this.notificationServices = notificationServices;
	}

}
