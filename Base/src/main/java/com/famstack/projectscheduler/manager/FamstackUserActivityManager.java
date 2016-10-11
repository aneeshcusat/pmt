package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.datatransferobject.UserActivityItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;

@Component
public class FamstackUserActivityManager extends BaseFamstackManager {

	@Resource
	FamstackProjectActivityManager famstackProjectActivityManager;

	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	public void createUserActivityItem(int userId, Date startTime, int taskId, String taskName, int duration,
			UserTaskType userTaskType) {
		UserItem assigneeUserItem = famstackUserProfileManager.getUserItemById(userId);
		Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startTime, 0);
		Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, startTime, 0);

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		dataMap.put("calenderDateEnd", dayEndDate);
		dataMap.put("userId", userId);

		List<?> userActivityItemlist = getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString("getUserActivityByDate"), dataMap);
		UserActivityItem userActivityItem = null;
		if (!userActivityItemlist.isEmpty()) {
			userActivityItem = (UserActivityItem) userActivityItemlist.get(0);
		}

		if (userActivityItem == null) {
			userActivityItem = new UserActivityItem();
			userActivityItem.setCalenderDate(new Timestamp(dayStartDate.getTime()));
			userActivityItem.setLeave(false);
			userActivityItem.setUserItem(assigneeUserItem);
		}

		getFamstackDataAccessObjectManager().saveOrUpdateItem(userActivityItem);
		setUserTaskActivity(userActivityItem, taskId, taskName, duration, startTime, userTaskType);
	}

	public void deleteAllUserTaskActivities(int taskId) {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("taskId", taskId);

		getFamstackDataAccessObjectManager().executeUpdate(HQLStrings.getString("deleteAllUserTaskItemsByTaskId"),
				dataMap);

	}

	public UserTaskActivityItem setUserTaskActivity(UserActivityItem userActivityItem, int taskId, String taskName,
			int duration, Date startDate, UserTaskType userTaskType) {
		UserTaskActivityItem userTaskActivityItem = new UserTaskActivityItem();
		userTaskActivityItem.setTaskId(taskId);
		userTaskActivityItem.setTaskName(taskName);
		userTaskActivityItem.setDuration(duration);
		userTaskActivityItem.setStartHour(startDate.getHours());
		userTaskActivityItem.setStartTime(new Timestamp(startDate.getTime()));
		userTaskActivityItem.setType(userTaskType);
		userTaskActivityItem.setCreatedDate(new Timestamp(new Date().getTime()));
		userTaskActivityItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
		userTaskActivityItem.setUserActivityItem(userActivityItem);
		getFamstackDataAccessObjectManager().saveOrUpdateItem(userTaskActivityItem);
		return userTaskActivityItem;

	}

	public List<TaskActivityDetails> getAllTaskActivities(String startDateString, String completionDateString) {
		List<TaskActivityDetails> taskActivitiesList = new ArrayList<>();
		Date startDate = DateUtils.tryParse(startDateString, DateUtils.DATE_FORMAT_CALENDER);
		Date completionDate = DateUtils.tryParse(completionDateString, DateUtils.DATE_FORMAT_CALENDER);
		logDebug("startDateString" + startDateString);
		logDebug("completionDateString" + completionDateString);
		logDebug("startDate" + startDate);
		logDebug("completionDate" + completionDate);

		Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startDate, -1);
		Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, completionDate, 1);

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		dataMap.put("calenderDateEnd", dayEndDate);
		logDebug("dayStartDate : " + dayStartDate);

		List<?> userActivityItems = getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString("allUserActivityItemsFromDatetoDate"), dataMap);

		getAllTaskActivitiesFromUserActivity(taskActivitiesList, userActivityItems);
		return taskActivitiesList;
	}

	public List<TaskActivityDetails> getAllTaskActivities() {
		Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), -2);
		List<TaskActivityDetails> taskActivitiesList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		logDebug("dayStartDate : " + dayStartDate);

		List<?> userActivityItems = getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString("allUserActivityItemsFromToday"), dataMap);

		getAllTaskActivitiesFromUserActivity(taskActivitiesList, userActivityItems);
		return taskActivitiesList;
	}

	private void getAllTaskActivitiesFromUserActivity(List<TaskActivityDetails> taskActivitiesList,
			List<?> userActivityItems) {
		logDebug("userActivityItems :" + userActivityItems);

		if (!userActivityItems.isEmpty()) {
			for (Object userActivityItemObj : userActivityItems) {

				UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;
				Set<UserTaskActivityItem> userTaskActivityItems = userActivityItem.getUserTaskActivities();
				logDebug("userActivityItem.getId() :" + userActivityItem.getId());
				if (!userActivityItems.isEmpty()) {
					for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
						TaskActivityDetails taskActivityDetails = getTaskActivityDetailsFromItem(userActivityItem,
								userTaskActivityItem);
						taskActivitiesList.add(taskActivityDetails);
						taskActivityDetails
								.setDateId(DateUtils.format(userActivityItem.getCalenderDate(), DateUtils.DATE_FORMAT));
						taskActivityDetails.setUserId(userActivityItem.getUserItem().getId());
					}
				}

			}
		}
	}

	private TaskActivityDetails getTaskActivityDetailsFromItem(UserActivityItem userActivityItem,
			UserTaskActivityItem userTaskActivityItem) {
		TaskActivityDetails taskActivityDetails = new TaskActivityDetails();
		taskActivityDetails.setTaskId(userTaskActivityItem.getTaskId());
		taskActivityDetails.setTaskName(userTaskActivityItem.getTaskName());
		taskActivityDetails.setDuration(userTaskActivityItem.getDuration());
		taskActivityDetails.setUserTaskType(userTaskActivityItem.getType());
		taskActivityDetails.setStartHour(userTaskActivityItem.getStartHour());
		taskActivityDetails.setStartTime(userTaskActivityItem.getStartTime());

		return taskActivityDetails;
	}

	public UserTaskActivityItem getUserTaskActivityItem(int taskActivityItemId) {
		return (UserTaskActivityItem) getFamstackDataAccessObjectManager().getItemById(taskActivityItemId,
				UserTaskActivityItem.class);
	}

	public void setProjectTaskActivityActualTime(int taskActivityId, Date date) {
		UserTaskActivityItem userTaskActivityItem = getUserTaskActivityItem(taskActivityId);

		if (userTaskActivityItem != null) {
			userTaskActivityItem.setActualStartTime(new Timestamp(date.getTime()));
		}

		getFamstackDataAccessObjectManager().updateItem(userTaskActivityItem);

	}

	public void setProjectTaskActivityEndTime(int taskActivityId, Date date) {
		UserTaskActivityItem userTaskActivityItem = getUserTaskActivityItem(taskActivityId);

		if (userTaskActivityItem != null) {
			userTaskActivityItem.setActualEndTime(new Timestamp(date.getTime()));
		}

		getFamstackDataAccessObjectManager().updateItem(userTaskActivityItem);

	}
}
