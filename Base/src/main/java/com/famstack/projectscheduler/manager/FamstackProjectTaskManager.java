package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.datatransferobject.UserActivityItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.utils.FamstackUtils;

@Component
public class FamstackProjectTaskManager extends BaseFamstackManager {

	@Resource
	FamstackProjectActivityManager famstackProjectActivityManager;

	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	@Resource
	FamstackUserActivityManager famstackUserActivityManager;

	public void createTaskItem(TaskDetails taskDetails, ProjectItem projectItem) {
		TaskItem taskItem = new TaskItem();

		taskItem.setStatus(TaskStatus.NEW);
		taskItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());

		saveTask(taskDetails, projectItem, taskItem);
		famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.TASK_ADDED,
				taskItem.getName());
	}

	public void updateTask(TaskDetails taskDetails, ProjectItem projectItem) {
		TaskItem taskItem = (TaskItem) famstackDataAccessObjectManager.getItemById(taskDetails.getTaskId(),
				TaskItem.class);
		if (taskItem != null) {
			saveTask(taskDetails, projectItem, taskItem);
			famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.TASK_UPDTED,
					taskItem.getName());
		}
	}

	private void saveTask(TaskDetails taskDetails, ProjectItem projectItem, TaskItem taskItem) {
		taskItem.setDescription(taskDetails.getDescription());
		taskItem.setName(taskDetails.getName());
		taskItem.setPriority(taskDetails.getPriority());
		taskItem.setProjectItem(projectItem);
		Date startDate = DateUtils.tryParse(taskDetails.getStartTime(), DateUtils.DATE_TIME_FORMAT);
		Date completionDate = DateUtils.tryParse(taskDetails.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
		Timestamp startTimeStamp = null;
		if (startDate != null) {
			startTimeStamp = new Timestamp(startDate.getTime());
		}

		Timestamp completionTimeStamp = null;
		if (completionDate != null) {
			completionTimeStamp = new Timestamp(completionDate.getTime());
		}

		taskItem.setAssignee(taskDetails.getAssignee());
		taskItem.setHelpers(Arrays.toString(taskDetails.getHelper()));

		if (taskDetails.getAssignee() > 0) {
			taskItem.setStatus(TaskStatus.ASSIGNED);
		} else {
			taskItem.setStatus(TaskStatus.NEW);
		}
		taskItem.setStartTime(startTimeStamp);
		taskItem.setCompletionTime(completionTimeStamp);
		taskItem.setDuration(taskDetails.getDuration());

		famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);
		taskDetails.setTaskId(taskItem.getTaskId());
		updateUserActivity(taskDetails, startDate);
	}

	private void updateUserActivity(TaskDetails taskDetails, Date startDate) {

		logDebug("task assignee :" + taskDetails.getAssignee());

		famstackUserActivityManager.deleteAllUserTaskActivities(taskDetails.getTaskId());

		if (taskDetails.getAssignee() > 0) {
			famstackUserActivityManager.createUserActivityItem(taskDetails.getAssignee(), startDate,
					taskDetails.getTaskId(), taskDetails.getName(), taskDetails.getDuration(), UserTaskType.PROJECT);
		}

		logDebug("helpers :" + taskDetails.getHelper());
		if (taskDetails.getHelper() != null && taskDetails.getHelper().length > 0) {
			for (int helperId : taskDetails.getHelper()) {
				famstackUserActivityManager.createUserActivityItem(helperId, startDate, taskDetails.getTaskId(),
						taskDetails.getName(), taskDetails.getDuration(), UserTaskType.PROJECT_HELPER);
			}
		}
	}

	public void deleteTaskItem(int taskId) {
		TaskItem taskItem = (TaskItem) famstackDataAccessObjectManager.getItemById(taskId, TaskItem.class);
		if (taskItem != null) {
			famstackUserActivityManager.deleteAllUserTaskActivities(taskItem.getTaskId());
			famstackDataAccessObjectManager.deleteItem(taskItem);
		}
	}

	public void deleteAllTaskActivitiesItem(int taskId) {
		famstackUserActivityManager.deleteAllUserTaskActivities(taskId);
	}

	public TaskItem getTaskItemById(int taskId) {
		return (TaskItem) famstackDataAccessObjectManager.getItemById(taskId, TaskItem.class);
	}

	public Set<TaskDetails> mapProjectTaskDetails(Set<TaskItem> taskItems) {
		Set<TaskDetails> taskDetails = new HashSet<TaskDetails>();
		if (taskItems != null) {
			for (TaskItem taskItem : taskItems) {
				TaskDetails taskDetail = mapProjectItemToProjectDetails(taskItem);
				taskDetails.add(taskDetail);
			}
		}

		return taskDetails;
	}

	public TaskDetails mapProjectItemToProjectDetails(TaskItem taskItem) {

		if (taskItem != null) {
			TaskDetails taskDetails = new TaskDetails();

			taskDetails.setDescription(taskItem.getDescription());
			taskDetails.setDuration(taskItem.getDuration());
			taskDetails.setName(taskItem.getName());
			taskDetails.setPriority(taskItem.getPriority());
			String startDateString = DateUtils.format(taskItem.getStartTime(), DateUtils.DATE_TIME_FORMAT);
			String completionDateString = DateUtils.format(taskItem.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
			taskDetails.setStartTime(startDateString);
			taskDetails.setCompletionTime(completionDateString);

			taskDetails.setStatus(taskItem.getStatus());
			if (taskItem.getReporter() != null) {
				taskDetails.setReporterName(
						taskItem.getReporter().getFirstName() + " " + taskItem.getReporter().getLastName());
			}
			taskDetails.setCreatedDate(taskItem.getCreatedDate());
			taskDetails.setLastModifiedDate(taskItem.getLastModifiedDate());
			taskDetails.setTaskId(taskItem.getTaskId());
			taskDetails.setProjectId(taskItem.getProjectItem().getProjectId());
			taskDetails.setAssignee(taskItem.getAssignee());
			taskDetails.setHelpersList(taskItem.getHelpers());

			return taskDetails;

		}
		return null;
	}

	public String getUserTaskActivityJson() {
		return FamstackUtils.getJsonFromObject(famstackUserActivityManager.getAllTaskActivities());
	}

	public Map<String, ArrayList<TaskDetails>> getAllProjectTask(int userId) {
		Map<Integer, TaskItem> taskItemMap = new HashMap<>();
		Map<String, ArrayList<TaskDetails>> userTaskActivityItemMap = new HashMap<>();

		userTaskActivityItemMap.put(TaskStatus.ASSIGNED.value(), new ArrayList<TaskDetails>());
		userTaskActivityItemMap.put(TaskStatus.INPROGRESS.value(), new ArrayList<TaskDetails>());
		userTaskActivityItemMap.put(TaskStatus.COMPLETED.value(), new ArrayList<TaskDetails>());

		Map<String, Object> dataMap = new HashMap<>();

		dataMap.put("userId", userId);
		dataMap.put("startTime", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), -2));

		List<?> userActivityItemlist = getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString("getUserActivities"), dataMap);

		logDebug("userActivityItemlist : " + userActivityItemlist);

		if (!userActivityItemlist.isEmpty()) {
			for (Object userActivityItemObj : userActivityItemlist) {
				UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;

				logDebug("userProjectTasklist : " + userActivityItem.getUserTaskActivities());

				if (!userActivityItem.getUserTaskActivities().isEmpty()) {
					for (UserTaskActivityItem userTaskActivityItem : userActivityItem.getUserTaskActivities()) {
						int taskId = userTaskActivityItem.getTaskId();
						TaskItem taskItem = taskItemMap.get(taskId);
						if (taskItem == null) {
							taskItem = getTaskItemById(taskId);
							taskItemMap.put(taskId, taskItem);
						}
						TaskDetails taskDetails = mapProjectItemToProjectDetails(taskItem);
						userTaskActivityItemMap.get(taskItem.getStatus().value()).add(taskDetails);
					}
				}
			}
		}

		return userTaskActivityItemMap;
	}
}
