package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.datatransferobject.UserActivityItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;

@Component
public class FamstackUserActivityManager extends BaseFamstackManager {

	@Resource
	FamstackProjectActivityManager famstackProjectActivityManager;

	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	public void createUserActivityItem(int userId, Date startTime, int taskId, int duration,
			UserTaskType userTaskType) {
		UserItem assigneeUserItem = famstackUserProfileManager.getUserItemById(userId);
		Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startTime, 0);
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
		setUserTaskActivity(userActivityItem, taskId, duration, userTaskType);
	}

	public void deleteAllUserTaskActivities(int taskId) {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("taskId", taskId);

		getFamstackDataAccessObjectManager().executeUpdate(HQLStrings.getString("deleteAllUserTaskItemsByTaskId"),
				dataMap);

	}

	public UserTaskActivityItem setUserTaskActivity(UserActivityItem userActivityItem, int taskId, int duration,
			UserTaskType userTaskType) {
		UserTaskActivityItem userTaskActivityItem = new UserTaskActivityItem();
		userTaskActivityItem.setTaskId(taskId);
		userTaskActivityItem.setDuration(duration);
		userTaskActivityItem.setType(userTaskType);
		userTaskActivityItem.setCreatedDate(new Timestamp(new Date().getTime()));
		userTaskActivityItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
		userTaskActivityItem.setUserActivityItem(userActivityItem);
		getFamstackDataAccessObjectManager().saveOrUpdateItem(userTaskActivityItem);
		return userTaskActivityItem;

	}
}
