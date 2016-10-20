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

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.datatransferobject.UserActivityItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
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
		TaskItem taskItem = getTaskItemById(taskId);
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

	public TaskDetails getTaskDetailsById(int taskId) {
		return mapTask(getTaskItemById(taskId));
	}

	public Set<TaskDetails> mapProjectTaskDetails(Set<TaskItem> taskItems) {
		Set<TaskDetails> taskDetails = new HashSet<TaskDetails>();
		if (taskItems != null) {
			for (TaskItem taskItem : taskItems) {
				TaskDetails taskDetail = mapTask(taskItem);
				taskDetails.add(taskDetail);
			}
		}

		return taskDetails;
	}

	public TaskDetails mapTask(TaskItem taskItem) {

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
			taskDetails.setTaskActivityDetails(
					famstackUserActivityManager.getUserTaskActivityItemByTaskId(taskItem.getTaskId()));
			return taskDetails;

		}
		return null;
	}

	public String getUserTaskActivityJson() {
		return FamstackUtils.getJsonFromObject(famstackUserActivityManager.getAllTaskActivities());
	}

	public String getUserTaskActivityJson(String startDate, String endDate) {
		JSONArray jsonArray = new JSONArray();
		for (TaskActivityDetails taskActivityDetails : famstackUserActivityManager.getAllTaskActivities(startDate,
				endDate)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("title", taskActivityDetails.getTaskName());
			jsonObject.put("start",
					DateUtils.format(taskActivityDetails.getStartTime(), DateUtils.DATE_TIME_FORMAT_CALENDER));
			Date completionDate = DateUtils.getNextPreviousDate(DateTimePeriod.HOUR, taskActivityDetails.getStartTime(),
					taskActivityDetails.getDuration());

			jsonObject.put("end", DateUtils.format(completionDate, DateUtils.DATE_TIME_FORMAT_CALENDER));
			jsonObject.put("tip", taskActivityDetails.getUserTaskType());
			jsonArray.put(jsonObject);
		}

		return jsonArray.toString();
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
						TaskActivityDetails taskActivityDetails = famstackUserActivityManager
								.mapUserTaskActivityItem(userTaskActivityItem);
						TaskDetails taskDetails = mapTask(taskItem);
						taskDetails.setTaskActivityDetails(taskActivityDetails);
						userTaskActivityItemMap.get(taskItem.getStatus().value()).add(taskDetails);
					}
				}
			}
		}

		return userTaskActivityItemMap;
	}

	public TaskItem updateTaskStatus(int taskId, int taskActivityId, TaskStatus taskStatus, String comments) {
		TaskItem taskItem = getTaskItemById(taskId);

		if (taskItem != null) {
			taskItem.setStatus(taskStatus);
			if (taskStatus == TaskStatus.INPROGRESS) {
				famstackUserActivityManager.setProjectTaskActivityActualTime(taskActivityId, new Date());
			} else if (taskStatus == TaskStatus.COMPLETED) {
				famstackUserActivityManager.setProjectTaskActivityEndTime(taskActivityId, new Date());
			}
		}

		getFamstackDataAccessObjectManager().updateItem(taskItem);
		return taskItem;
	}
}
