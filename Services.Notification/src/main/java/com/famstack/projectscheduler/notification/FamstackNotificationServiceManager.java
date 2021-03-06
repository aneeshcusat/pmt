package com.famstack.projectscheduler.notification;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.famstack.email.contants.Templates;
import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AppConfValueDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.notification.bean.EmailNotificationItem;
import com.famstack.projectscheduler.notification.services.FamstackBaseNotificationService;
import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;
import com.famstack.projectscheduler.util.StringUtils;
import com.famstack.projectscheduler.utils.FamstackUtils;

@Service
public class FamstackNotificationServiceManager extends BaseFamstackService
{

    private List<FamstackBaseNotificationService> notificationServices;

    @Async
    public void notifyAll(NotificationType notificationType, Object object, UserItem currentUserItem)
    {
        logDebug("notification " + notificationType);
        EmailNotificationItem notificationEmailItem = null;

        switch (notificationType) {
            case USER_REGISTRAION:
                notificationEmailItem = getUserNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.USER_REGISTRAION);
                break;
            case RESET_PASSWORD:
                notificationEmailItem = getUserNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.RESET_PASSWORD);
                break;
            case USER_UPDATE:
                notificationEmailItem = getUserNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.USER_UPDATE);
                break;
            case FORGOT_PASSWORD:
                notificationEmailItem = getUserNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.FORGOT_PASSWORD);
                break;
            case PROJECT_CREATE:
                notificationEmailItem = getProjectStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.PROJECT_CREATE);
                break;
            case PROJECT_UPDATE:
                notificationEmailItem = getProjectStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.PROJECT_UPDATE);
                break;
            case PROJECT_DELETE:
                notificationEmailItem = getProjectStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.PROJECT_DELETE);
                break;
            case PROJECT_COMMENT_ADDED:
                notificationEmailItem = getProjectStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.PROJECT_COMMENT_ADDED);
                break;
            case PROJECT_END_REMINDER:
                notificationEmailItem = getProjectStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.PROJECT_END_REMINDER);
                break;
            case PROJECT_START_REMINDER:
                notificationEmailItem = getProjectStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.PROJECT_START_REMINDER);
                break;
            case PROJECT_DEADLINE_MISSED:
                notificationEmailItem = getProjectStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.PROJECT_DEADLINE_MISSED);
                break;
            case TASK_CREATED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_CREATED);
                break;
            case TASK_CREATED_ASSIGNED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_CREATED_ASSIGNED);
                break;
            case TASK_UPDATED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_UPDATED);
                break;
            case TASK_ASSIGNED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_ASSIGNED);
                break;
            case TASK_RE_ASSIGNED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_RE_ASSIGNED);
                break;
            case TASK_DELETED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_DELETED);
                break;
            case TASK_AUTO_PAUSED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_AUTO_PAUSED);
                break;

            case TASK_COMPLETED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_COMPLETED);
                break;
            case TASK_INPROGRESS:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_INPROGRESS);
                break;
            case TASK_CLOSED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_CLOSED);
                break;
            case TASK_DEADLINE_MISSED:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_DEADLINE_MISSED);
                break;
            case TASK_END_REMINDER:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_END_REMINDER);
                break;
            case TASK_START_REMINDER:
                notificationEmailItem = getProjectTaskStatusNotificationItem(object);
                notificationEmailItem.setTemplates(Templates.TASK_START_REMINDER);
                break;
            case USER_ACTIVITY_REPORT:
                notificationEmailItem = getUserActivityReportData(object);
                notificationEmailItem.setTemplates(Templates.USER_ACTIVITY_REPORT);
                break;
            case USER_UTILIZATION_REPORT:
                notificationEmailItem = getUserUtilizationReportData(object);
                notificationEmailItem.setTemplates(Templates.USER_UTILIZATION_REPORT);
                break;
            case USER_ACTIVITY_REPORT_DEFAULTER:
                notificationEmailItem = getUserActivityReportDefaulterData(object);
                notificationEmailItem.setTemplates(Templates.USER_ACTIVITY_REPORT_DEFAULTER);
                break;
            case USER_UTILIZATION_REPORT_DEFAULTER:
                notificationEmailItem = getUserUtilizationReportDefaulterData(object);
                notificationEmailItem.setTemplates(Templates.USER_UTILIZATION_REPORT_DEFAULTER);
                break;
            case WEEKWISE_USER_UTILIZATION_MONTHLY:
                notificationEmailItem = getUserUtilizationReportData(object);
                notificationEmailItem.setTemplates(Templates.WEEKWISE_USER_UTILIZATION_MONTHLY);
                break;    
            case WEEKWISE_USER_UTILIZATION_MONTHLY_DEFAULTER:
                notificationEmailItem = getUserUtilizationReportData(object);
                notificationEmailItem.setTemplates(Templates.WEEKWISE_USER_UTILIZATION_MONTHLY_DEFAULTER);
                break;  
            case WEEKLY_PO_ESTIMATION_REPORT:
                notificationEmailItem = getPOEstimationReportData(object);
                notificationEmailItem.setTemplates(Templates.WEEKLY_PO_ESTIMATION_REPORT);
                break;  
            default:
                break;
        }

        if (notificationEmailItem != null) {
            if (currentUserItem != null) {
                notificationEmailItem.getToList().add(currentUserItem.getUserId());
                notificationEmailItem.getSubscriberList().add(currentUserItem.getId());
                notificationEmailItem.setOriginUserId(currentUserItem.getId());
                notificationEmailItem.getData().put("firstName", currentUserItem.getFirstName());
            }
            notificationEmailItem.getData()
                .put("notificationKey", notificationEmailItem.getTemplates().getSubjectkey());
            notificationEmailItem.setNotificationType(notificationType);
            for (FamstackBaseNotificationService notificationService : notificationServices) {

                if (notificationService.isEnabled() || allowNotification(notificationType)) {
                    logDebug("Sending notification...");
                    notificationService.notify(notificationEmailItem);
                }
            }
        } else {
            logError("Email type " + notificationType + "has not configured! ");
        }
    }

    private EmailNotificationItem getPOEstimationReportData(Object object) {
    	EmailNotificationItem notificationEmailItem = getReportingData(object);
        Map<String, Object> poEstimationReportData = (Map<String, Object>) object;
		notificationEmailItem.getData().put("estimationData",
				poEstimationReportData.get("ESTIMATION_DATA"));
        return notificationEmailItem;
	}

	private EmailNotificationItem getUserUtilizationReportDefaulterData(
			Object object) {
    	EmailNotificationItem notificationEmailItem = new EmailNotificationItem();
        Map<String, Object> userUtilizationReportData = getReportDefaultersData(
				object, notificationEmailItem);
        notificationEmailItem.getData().put("utilizationData", userUtilizationReportData.get("UTILIZATION_DATA"));
		
        return notificationEmailItem;
	}

	private EmailNotificationItem getUserActivityReportDefaulterData(
			Object object) {
		EmailNotificationItem notificationEmailItem = new EmailNotificationItem();
        Map<String, Object> userActivityReportData = getReportDefaultersData(
				object, notificationEmailItem);
        notificationEmailItem.getData().put("userActivityData", userActivityReportData.get("ACTIVITY_DATA"));
        notificationEmailItem.getData().put("dateList",
        		userActivityReportData.get("DATE_LIST"));
        return notificationEmailItem;
	}

	private Map<String, Object> getReportDefaultersData(Object object,
			EmailNotificationItem notificationEmailItem) {
		Map<String, Object> userDefaulterReportData = (Map<String, Object>) object;
        notificationEmailItem.setToList((Set<String>) userDefaulterReportData.get("TO_LIST"));
        notificationEmailItem.setCcList((Set<String>) userDefaulterReportData.get("CC_LIST"));
        notificationEmailItem.setSubject((String) userDefaulterReportData.get("subject"));
        notificationEmailItem.getData().put("teamName", userDefaulterReportData.get("TEAM_NAME"));
        notificationEmailItem.getData().put("reportDate", userDefaulterReportData.get("REPORT_DATE"));
		return userDefaulterReportData;
	}

	private EmailNotificationItem getUserUtilizationReportData(Object object) {
    	EmailNotificationItem notificationEmailItem = getReportingData(object);
        Map<String, Object> userUtilizationReportData = (Map<String, Object>) object;
		notificationEmailItem.getData().put("utilizationData",
				userUtilizationReportData.get("UTILIZATION_DATA"));
        return notificationEmailItem;
	}

    private EmailNotificationItem getReportingData(Object object) {
    	EmailNotificationItem notificationEmailItem = new EmailNotificationItem();
        Map<String, Object> userUtilizationReportData = (Map<String, Object>) object;
        notificationEmailItem.setToList((Set<String>) userUtilizationReportData.get("TO_LIST"));
        notificationEmailItem.setCcList((Set<String>) userUtilizationReportData.get("CC_LIST"));
        notificationEmailItem.setSubject((String) userUtilizationReportData.get("subject"));
        notificationEmailItem.getData().put("teamName", userUtilizationReportData.get("TEAM_NAME"));
        notificationEmailItem.getData().put("reportDate", userUtilizationReportData.get("REPORT_DATE"));
        notificationEmailItem.getData().put("weekNumberList",
				userUtilizationReportData.get("WEEK_LIST"));
        
		notificationEmailItem.getData().put("dateList",
				userUtilizationReportData.get("DATE_LIST"));
        return notificationEmailItem;
    }
	private EmailNotificationItem getUserActivityReportData(Object object) {
		EmailNotificationItem notificationEmailItem = getReportingData(object);
        Map<String, Object> userUtilizationReportData = (Map<String, Object>) object;
		notificationEmailItem.getData().put("userActivityData",
				userUtilizationReportData.get("ACTIVITY_DATA"));
        return notificationEmailItem;
	}

	private boolean allowNotification(NotificationType notificationType)
    {
        return (notificationType == NotificationType.RESET_PASSWORD
            || notificationType == NotificationType.FORGOT_PASSWORD
				|| notificationType == NotificationType.USER_REGISTRAION
				|| notificationType == NotificationType.USER_UPDATE
				|| notificationType == NotificationType.USER_ACTIVITY_REPORT 
				|| notificationType == NotificationType.USER_UTILIZATION_REPORT
				|| notificationType == NotificationType.USER_UTILIZATION_REPORT_DEFAULTER
				|| notificationType == NotificationType.USER_ACTIVITY_REPORT_DEFAULTER
				|| notificationType == NotificationType.WEEKWISE_USER_UTILIZATION_MONTHLY
				|| notificationType == NotificationType.WEEKWISE_USER_UTILIZATION_MONTHLY_DEFAULTER
				|| notificationType == NotificationType.WEEKLY_PO_ESTIMATION_REPORT
				|| notificationType == NotificationType.PROJECT_CREATE
				|| notificationType == NotificationType.PROJECT_UPDATE
        		)
            ? true : false;
    }

    private EmailNotificationItem getProjectStatusNotificationItem(Object object)
    {
        EmailNotificationItem notificationEmailItem;
        notificationEmailItem = new EmailNotificationItem();
        ProjectDetails projectDetails = (ProjectDetails) object;
        notificationEmailItem.getData().put("url", getFamstackApplicationConfiguration().getUrl());
        notificationEmailItem.setToList(getToListForProjectUpdates(projectDetails, true));
        notificationEmailItem.setSubscriberList(getSubscribersIdForProjectUpdates(projectDetails));
        notificationEmailItem.getData().put("projectId", projectDetails.getId());
        notificationEmailItem.getData().put("name", projectDetails.getName());
        notificationEmailItem.getData().put("code", projectDetails.getCode());
        notificationEmailItem.getData().put("description", projectDetails.getDescription());
        notificationEmailItem.getData().put("createdDate", projectDetails.getCreatedDate());
        notificationEmailItem.getData().put("lastModifiedDate", projectDetails.getLastModifiedDate());
        notificationEmailItem.getData().put("projectType", projectDetails.getType());
        notificationEmailItem.getData().put("projectSubType", projectDetails.getProjectSubType());
        notificationEmailItem.getData().put("complexity", projectDetails.getComplexity());
        notificationEmailItem.getData().put("accountName", projectDetails.getAccountName());
        notificationEmailItem.getData().put("quantity", projectDetails.getQuantity());
        notificationEmailItem.getData().put("reporterName", projectDetails.getReporterName());
        notificationEmailItem.getData().put("category", projectDetails.getCategory());
        notificationEmailItem.getData().put("priority", projectDetails.getPriority());
        notificationEmailItem.getData().put("startTime", projectDetails.getStartTime());
        notificationEmailItem.getData().put("completionTime", projectDetails.getCompletionTime());
        notificationEmailItem.getData().put("duration", projectDetails.getDurationHrs());
        notificationEmailItem.getData().put("status", projectDetails.getStatus());
        notificationEmailItem.getData().put("PONumber", projectDetails.getPONumber());
        notificationEmailItem.getData().put("clientName", projectDetails.getClientName());
        notificationEmailItem.getData().put("teamName", projectDetails.getTeamName());
        notificationEmailItem.getData().put("subTeamName", projectDetails.getSubTeamName());
        notificationEmailItem.getData().put("ppi", projectDetails.getPpi());
        notificationEmailItem.getData().put("newCategory", projectDetails.getNewCategory());
        notificationEmailItem.getData().put("sowLineItem", projectDetails.getSowLineItem());
        notificationEmailItem.getData().put("orderBookRefNo", projectDetails.getOrderBookRefNo());
        notificationEmailItem.getData().put("proposalNo", projectDetails.getProposalNo());
        notificationEmailItem.getData().put("projectLocation", projectDetails.getProjectLocation());
        notificationEmailItem.getData().put("clientPartner", projectDetails.getClientPartner());
        notificationEmailItem.getData().put("hoursUserSkillMonthly", FamstackUtils.getJsonObjectFromJson(projectDetails.getHoursUserSkillMonthlySplitJson()));

        return notificationEmailItem;
    }

    private EmailNotificationItem getProjectTaskStatusNotificationItem(Object object)
    {
        EmailNotificationItem notificationEmailItem = null;
        Map<String, Object> dataMap = (Map<String, Object>) object;

        if (dataMap != null && !dataMap.isEmpty()) {
            notificationEmailItem = new EmailNotificationItem();
            notificationEmailItem.getData().put("url", getFamstackApplicationConfiguration().getUrl());
            TaskDetails taskDetails = (TaskDetails) dataMap.get("taskDetails");
            ProjectDetails projectDetails = (ProjectDetails) dataMap.get("projectDetails");

            if (projectDetails != null) {
                notificationEmailItem.setToList(getToListForProjectUpdates(projectDetails, false));
                notificationEmailItem.setSubscriberList(getSubscribersIdForProjectUpdates(projectDetails));
            }
            notificationEmailItem.getToList().addAll(getToListForTaskUpdate(taskDetails));

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

    private Set<String> getToListForTaskUpdate(TaskDetails taskDetails)
    {
        Set<String> toList = new HashSet<>();
        String helpers = taskDetails.getHelpersList();
        if (StringUtils.isNotBlank(helpers)) {
            String[] helperArray = helpers.split(",");
            for (String helper : helperArray) {
                int userId = Integer.parseInt(helper.trim());
                EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getUserMap().get(userId);
                addToToList(toList, employeeDetails, userId);
            }
        }

        EmployeeDetails employeeDetails =
            getFamstackApplicationConfiguration().getUserMap().get(taskDetails.getAssignee());
        int taskAssignee = taskDetails.getAssignee();
        addToToList(toList, employeeDetails, taskAssignee);

        logDebug("getToListForTaskUpdate" + toList);
        return toList;
    }

    private void addToToList(Set<String> toList, EmployeeDetails employeeDetails, int taskAssignee)
    {
        if (employeeDetails != null) {
            if (taskAssignee != employeeDetails.getId()) {
                logError("Invalid user found for emailing : assignee - " + taskAssignee + "  emp id -"
                    + employeeDetails.getId());
            } else {
                toList.add(employeeDetails.getEmail());
            }
        }
    }

    private Set<String> getToListForProjectUpdates(ProjectDetails projectDetails, boolean targetToGroup)
    {
        Set<String> toList = new HashSet<>();
        String reporterEmail = null;
        if (projectDetails !=null && projectDetails.getEmployeeDetails() != null) {
         reporterEmail = projectDetails.getEmployeeDetails().getEmail();
        }
        String watchers = projectDetails.getWatchers();
        if (StringUtils.isNotBlank(watchers)) {
            String[] watchersArray = watchers.split(",");
            toList.addAll(Arrays.asList(watchersArray));
        }
        
        if(targetToGroup) {
        List<AppConfValueDetails> appConfValueDetails = getFamstackApplicationConfiguration().getProjectNotifications();
        if (appConfValueDetails != null) {
			for(AppConfValueDetails appConfValueDetail : appConfValueDetails) {
				toList.add(appConfValueDetail.getValue());
			}
		  }
        }
        if (reporterEmail !=null) {
        	toList.add(reporterEmail);
        }
        logDebug("getToListForProjectUpdates" + toList);
        return toList;
    }

    private Set<Integer> getSubscribersIdForProjectUpdates(ProjectDetails projectDetails)
    {
        Set<Integer> toList = new HashSet<>();
        Integer reporterId = null;
        if (projectDetails !=null && projectDetails.getEmployeeDetails() != null) {
        	reporterId = projectDetails.getEmployeeDetails().getId();
         }

        String watchers = projectDetails.getWatchers();
        if (StringUtils.isNotBlank(watchers)) {
            String[] watchersArray = watchers.split(",");
            for (String watcher : watchersArray) {
                Integer watcherId = FamstackApplicationConfiguration.getUserIdMap().get(watcher.toLowerCase());

                if (watcherId != null) {
                    toList.add(watcherId);
                }
            }
        }
        if (reporterId != null) {
        	toList.add(reporterId);
        }
        logDebug("getSubscribersIdForProjectUpdates" + toList);
        return toList;
    }

    private EmailNotificationItem getUserNotificationItem(Object object)
    {
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
        String key = "";
		try {
			key = URLEncoder.encode(FamstackSecurityTokenManager.encryptStringWithDate(employeeDetails.getHashKey(),
					employeeDetails.getHashKey()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        notificationEmailItem.getData().put("url", getFamstackApplicationConfiguration().getUrl() + "/resetpassword?key="+key+"&uid="+employeeDetails.getId());
        return notificationEmailItem;
    }

    public List<FamstackBaseNotificationService> getNotificationServices()
    {
        return notificationServices;
    }

    public void setNotificationServices(List<FamstackBaseNotificationService> notificationServices)
    {
        this.notificationServices = notificationServices;
    }
}
