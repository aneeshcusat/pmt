package com.famstack.projectscheduler.scheduling;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.manager.FamstackProjectManager;
import com.famstack.projectscheduler.manager.FamstackProjectTaskManager;
import com.famstack.projectscheduler.manager.FamstackUserActivityManager;
import com.famstack.projectscheduler.notification.FamstackNotificationServiceManager;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;

public class FamstackScheduler extends BaseFamstackService {

	@Resource
	private FamstackUserActivityManager famstackUserActivityManager;

	@Resource
	private FamstackProjectManager famstackProjectManager;

	@Resource
	private FamstackProjectTaskManager famstackProjectTaskManager;

	private FamstackNotificationServiceManager famstackNotificationServiceManager;

	public void scheduleJob() {
		logDebug("Running scheduler");
		userTaskStatusRefresh();
		checkProjectDeadlineMissed();
		checkProjectEndTimeReminder();
		checkTaskStartTimeReminder();
		checkTaskEndTimeReminder();
		checkTaskDeadlineMissed();
	}

	public void userTaskStatusRefresh() {
		logDebug("Running scheduler - userTaskStatusRefresh");
		famstackUserActivityManager.updateAllUserAvailableTime();
	}

	public void checkProjectDeadlineMissed() {
		logDebug("Running scheduler - checkProjectDeadlineMissed");
		List<ProjectDetails> projectDetailsList = famstackProjectManager.getAllMissedTimeLineProjectDetails(new Date());

		for (ProjectDetails prodDetails : projectDetailsList) {
			famstackNotificationServiceManager.notifyAll(NotificationType.PROJECT_DEADLINE_MISSED, prodDetails, null);
		}
	}

	public void checkProjectStartTimeReminder() {
		logDebug("Running scheduler - checkProjectStartTimeReminder");
		List<ProjectDetails> projectDetailsList = famstackProjectManager.getAllProjectDetailsWithIn(new Date(),
				DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), -5));

		for (ProjectDetails prodDetails : projectDetailsList) {
			famstackNotificationServiceManager.notifyAll(NotificationType.PROJECT_START_REMINDER, prodDetails, null);
		}
	}

	public void checkProjectEndTimeReminder() {
		logDebug("Running scheduler - checkProjectEndTimeReminder");
		List<ProjectDetails> projectDetailsList = famstackProjectManager.getAllProjectDetailsEndTimeWithIn(new Date(),
				DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), -5));

		for (ProjectDetails prodDetails : projectDetailsList) {
			famstackNotificationServiceManager.notifyAll(NotificationType.PROJECT_END_REMINDER, prodDetails, null);
		}
	}

	public void checkTaskStartTimeReminder() {
		logDebug("Running scheduler - checkTaskStartTimeReminder");
		List<TaskDetails> taskDetailsList = famstackProjectTaskManager.getAllTaskStartWithin(new Date(),
				DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), -5));

		for (TaskDetails taskDetails : taskDetailsList) {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("taskDetails", taskDetails);
			famstackNotificationServiceManager.notifyAll(NotificationType.TASK_START_REMINDER, dataMap, null);
		}
	}

	public void checkTaskEndTimeReminder() {
		logDebug("Running scheduler - checkTaskEndTimeReminder");
		List<TaskDetails> taskDetailsList = famstackProjectTaskManager.getAllTaskEndWithin(new Date(),
				DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, new Date(), -5));

		for (TaskDetails taskDetails : taskDetailsList) {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("taskDetails", taskDetails);
			famstackNotificationServiceManager.notifyAll(NotificationType.TASK_END_REMINDER, dataMap, null);
		}
	}

	public void checkTaskDeadlineMissed() {
		logDebug("Running scheduler - checkTaskDeadlineMissed");
		List<TaskDetails> taskDetailsList = famstackProjectTaskManager.getAllProjectTaskMissedDeadLine(new Date());

		for (TaskDetails taskDetails : taskDetailsList) {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("taskDetails", taskDetails);
			famstackNotificationServiceManager.notifyAll(NotificationType.TASK_DEADLINE_MISSED, dataMap, null);
		}
	}
}
