package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.famstack.projectscheduler.contants.FamstackConstants;
import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.LeaveType;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.datatransferobject.UserActivityItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.datatransferobject.UserUsageActivityItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.UserSiteActivityDetails;
import com.famstack.projectscheduler.employees.bean.UserSiteActivityStatus;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.security.user.UserRole;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.TimeInType;

public class FamstackUserActivityManager extends BaseFamstackManager {

	private static final String TASK_CONTRIBUTERS = "TASK_CONTRIBUTERS";

	private static final String TASK_ACTUAL_DURATION = "TASK_ACTUAL_DURATION";

	@Resource
	FamstackProjectActivityManager famstackProjectActivityManager;

	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	public void createCompletedUserActivityItem(int userId, Date startTime,
			int taskId, String taskName, int durationInMinutes,
			UserTaskType userTaskType, String taskActCategory,
			ProjectType projectType, String comment, String clientName, String teamName, String clientPartner,
			String division, String account, String orderBookNumber, String referenceNo, String actProjectName
			) {
		UserTaskActivityItem userTaskActivityItem = createUserActivityItem(
				userId, startTime, taskId, taskName, durationInMinutes,
				userTaskType, taskActCategory, projectType, null);
		userTaskActivityItem.setCompletionComment(comment);
		userTaskActivityItem.setRecordedStartTime(new Timestamp(startTime
				.getTime()));
		userTaskActivityItem.setActualStartTime(new Timestamp(startTime
				.getTime()));
		Long endTime = DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE,
				startTime, durationInMinutes).getTime();
		userTaskActivityItem.setActualEndTime(new Timestamp(endTime));
		userTaskActivityItem.setRecordedEndTime(new Timestamp(endTime));
		
		
		userTaskActivityItem.setDivision(division);
		userTaskActivityItem.setAccount(account);
		userTaskActivityItem.setOrderBookNumber(orderBookNumber);
		userTaskActivityItem.setReferenceNo(referenceNo);
		userTaskActivityItem.setActProjectName(actProjectName);
		
		userTaskActivityItem.setTeamName(teamName);
		
		userTaskActivityItem.setClientPartner(clientPartner);
		
		
		userTaskActivityItem
				.setModifiedBy(getFamstackApplicationConfiguration()
						.getCurrentUserId());
		userTaskActivityItem.setClientName(clientName);
		getFamstackDataAccessObjectManager().saveOrUpdateItem(
				userTaskActivityItem);
	}

	public UserTaskActivityItem createUserActivityItem(int userId,
			Date startTime, int taskId, String taskName, int durationInMinutes,
			UserTaskType userTaskType, String taskActCategory,
			ProjectType projectType, String userGroupId) {

		UserItem assigneeUserItem = famstackUserProfileManager
				.getUserItemById(userId);
		Date dayStartDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, startTime, 0);
		Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END,
				startTime, 0);
		Date calenderDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.CALENDER_DAY_START, startTime, 0);
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		dataMap.put("calenderDateEnd", dayEndDate);
		dataMap.put("userId", userId);
		List<?> userActivityItemlist = null;
		if (userGroupId == null) {
			userActivityItemlist = getFamstackDataAccessObjectManager()
					.executeQuery(
							HQLStrings.getString("getUserActivityByDate"),
							dataMap);
		} else {
			userActivityItemlist = getFamstackDataAccessObjectManager()
					.executeQueryWithGroupId(
							HQLStrings.getString("getUserActivityByDate"),
							dataMap, userGroupId);
		}

		UserActivityItem userActivityItem = null;
		if (!userActivityItemlist.isEmpty()) {
			userActivityItem = (UserActivityItem) userActivityItemlist.get(0);
		}

		if (userActivityItem == null) {
			userActivityItem = new UserActivityItem();
			userActivityItem.setCalenderDate(new Timestamp(calenderDate
					.getTime()));
			userActivityItem.setUserItem(assigneeUserItem);
		}

		if (userTaskType == UserTaskType.LEAVE) {
			int leaveMins = userActivityItem.getLeaveMins();
			leaveMins += (durationInMinutes);
			userActivityItem.setLeaveMins(leaveMins);
		} else if (userTaskType == UserTaskType.OTHER) {
			int nonBillabaleMins = userActivityItem.getNonBillableMins();
			nonBillabaleMins += (durationInMinutes);
			userActivityItem.setNonBillableMins(nonBillabaleMins);
		}

		userActivityItem.setUserGroupId(userGroupId);
		getFamstackDataAccessObjectManager().saveOrUpdateItem(userActivityItem);
		return setUserTaskActivity(userActivityItem, taskId, taskName,
				durationInMinutes, startTime, userTaskType, taskActCategory,
				projectType);
	}

	public List<?> getTodaysUserActivity() {

		Date dayStartDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, new Date(), 0);
		Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END,
				new Date(), 0);

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		dataMap.put("calenderDateEnd", dayEndDate);

		String queryKey = "allUserActivityItemsFromDatetoDate";

		List<?> userActivityItems = getFamstackDataAccessObjectManager()
				.executeAllGroupQuery(HQLStrings.getString(queryKey), dataMap);

		return userActivityItems;
	}

	public void initialize() {
		updateAllUserAvailableTime();
	}

	public void updateAllUserAvailableTime() {
		logDebug("updated available time for users ");
		for (Object userActivityItemObj : getTodaysUserActivity()) {
			UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;

			EmployeeDetails employeeDetails = getFamstackApplicationConfiguration()
					.getUserMap().get(userActivityItem.getUserItem().getId());

			if (userActivityItem.getLeave() != null) {
				employeeDetails.setLeave(LeaveType.FULL);
				logDebug("User is on leave ");
				return;
			}
			Timestamp availableTime = getUserAvailableTime(userActivityItem
					.getUserTaskActivities());

			if (employeeDetails != null) {
				employeeDetails.setUserAvailableTime(availableTime);
			}
		}
	}

	private Timestamp getUserAvailableTime(
			Set<UserTaskActivityItem> userTaskActivityItems) {
		Timestamp userAvailableTime = null;
		for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
			userAvailableTime = getUserAvaliableTime(userAvailableTime,
					userTaskActivityItem);
		}

		return userAvailableTime;

	}

	private Timestamp getUserAvaliableTime(Timestamp userAvailableTime,
			UserTaskActivityItem userTaskActivityItem) {
		Timestamp actualEndTime = userTaskActivityItem.getActualEndTime();
		Timestamp startTime = userTaskActivityItem.getStartTime();
		int durationInMinutes = userTaskActivityItem.getDurationInMinutes();
		startTime
				.setTime(startTime.getTime() + (durationInMinutes * 60 * 1000));
		if (actualEndTime != null) {
			if (userAvailableTime != null) {
				if (userAvailableTime.getTime() < actualEndTime.getTime()) {
					userAvailableTime = actualEndTime;
				}
			} else {
				userAvailableTime = actualEndTime;
			}
		} else if (startTime != null) {
			if (userAvailableTime != null) {
				if (userAvailableTime.getTime() < startTime.getTime()) {
					userAvailableTime = startTime;
				}
			} else {
				userAvailableTime = startTime;
			}

		}
		return userAvailableTime;
	}

	public void deleteAllUserTaskActivities(int taskId) {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("taskId", taskId);

		getFamstackDataAccessObjectManager()
				.executeUpdate(
						HQLStrings.getString("deleteAllUserTaskItemsByTaskId"),
						dataMap);

	}

	public UserTaskActivityItem setUserTaskActivity(
			UserActivityItem userActivityItem, int taskId, String taskName,
			int durationInMinutes, Date startDate, UserTaskType userTaskType,
			String taskActCategory, ProjectType projectType) {
		UserTaskActivityItem userTaskActivityItem = new UserTaskActivityItem();
		userTaskActivityItem.setTaskId(taskId);
		userTaskActivityItem.setTaskName(taskName);
		userTaskActivityItem.setDurationInMinutes(durationInMinutes);
		userTaskActivityItem.setStartHour(startDate.getHours());
		userTaskActivityItem.setStartTime(new Timestamp(startDate.getTime()));
		userTaskActivityItem.setType(userTaskType);
		userTaskActivityItem.setTaskActCategory(taskActCategory);
		userTaskActivityItem.setProjectType(projectType);
		userTaskActivityItem
				.setCreatedDate(new Timestamp(new Date().getTime()));
		userTaskActivityItem.setLastModifiedDate(new Timestamp(new Date()
				.getTime()));
		userTaskActivityItem.setUserGroupId(userActivityItem.getUserGroupId());
		if (userActivityItem.getUserItem() != null) {
			userTaskActivityItem.setUserId(userActivityItem.getUserItem()
					.getId());
		}
		try {
			userTaskActivityItem
					.setModifiedBy(getFamstackApplicationConfiguration()
							.getCurrentUserId());
		} catch (Exception e) {

		}
		userTaskActivityItem.setUserActivityItem(userActivityItem);
		getFamstackDataAccessObjectManager().saveOrUpdateItem(
				userTaskActivityItem);
		return userTaskActivityItem;

	}

	public List<TaskActivityDetails> getAllTaskActivities(
			String startDateString, String completionDateString, int userId) {
		List<TaskActivityDetails> taskActivitiesList = new ArrayList<>();
		int currentUserId = userId;
		if (userId == 0) {
			currentUserId = getFamstackApplicationConfiguration()
					.getCurrentUserId();
			if (getFamstackApplicationConfiguration().getCurrentUser()
					.getUserGroupId() != getFamstackApplicationConfiguration()
					.getCurrentUserGroupId()) {
				currentUserId = -1;
			}
		}
		List<?> userActivityItems = getAllTaskActivityItems(startDateString,
				completionDateString, currentUserId, true);

		getAllTaskActivitiesFromUserActivity(taskActivitiesList,
				userActivityItems);
		return taskActivitiesList;
	}

	private List<?> getAllTaskActivityItems(String startDateString,
			String completionDateString, int currentUserId, boolean isFullLoad) {
		Date startDate = DateUtils.tryParse(startDateString,
				DateUtils.DATE_FORMAT_CALENDER);
		Date completionDate = DateUtils.tryParse(completionDateString,
				DateUtils.DATE_FORMAT_CALENDER);
		logDebug("startDateString" + startDateString);
		logDebug("completionDateString" + completionDateString);
		logDebug("startDate" + startDate);
		logDebug("completionDate" + completionDate);

		Date dayStartDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, startDate, 0);
		Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END,
				completionDate, 0);

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		dataMap.put("calenderDateEnd", dayEndDate);
		logDebug("dayStartDate : " + dayStartDate);

		UserRole userRole = getFamstackApplicationConfiguration()
				.getCurrentUser().getUserRole();

		String queryKey = "userActivityItemsFromDatetoDate";
		if ((userRole == UserRole.ADMIN || userRole == UserRole.SUPERADMIN || userRole == UserRole.TEAMLEAD)
				&& isFullLoad && currentUserId == -1) {
			queryKey = "allUserActivityItemsFromDatetoDate";
		} else {
			dataMap.put("userId", currentUserId);
		}

		List<?> userActivityItems = getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString(queryKey), dataMap);
		return userActivityItems;
	}

	public List<TaskActivityDetails> getAllTaskActivities(Integer userId,
			int dayfilter) {
		String queryTobeExecuted = "allUserActivityItemsFromToday";

		Date dayStartDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, new Date(), -1 * dayfilter);
		List<TaskActivityDetails> taskActivitiesList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		logDebug("dayStartDate : " + dayStartDate);

		if (userId != null) {
			dataMap.put("userId", userId);
			queryTobeExecuted = "userActivityItemsFromToday";
		}

		List<?> userActivityItems = getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString(queryTobeExecuted), dataMap);

		getAllTaskActivitiesFromUserActivity(taskActivitiesList,
				userActivityItems);
		return taskActivitiesList;
	}

	public List<TaskActivityDetails> getAllTaskActivities(Integer userId,
			String monthFilter) {
		String queryTobeExecuted = "allUserActivityItemsFromDatetoDate";
		Date dayStartDate = DateUtils.tryParse("01-" + monthFilter,
				DateUtils.DAY_MONTH_YEAR);
		Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END,
				DateUtils.getLastDayOfThisMonth(dayStartDate), 0);
		;
		List<TaskActivityDetails> taskActivitiesList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", dayStartDate);
		dataMap.put("calenderDateEnd", dayEndDate);

		logDebug("dayStartDate : " + dayStartDate);
		List<?> userActivityItems = null;

		boolean isCurrentMonth = DateUtils.isCurrentMonth(dayStartDate);

		if (isCurrentMonth) {
			dataMap.put("todaysDate", DateUtils.getNextPreviousDate(
					DateTimePeriod.DAY_START, new Date(), 0));
		}

		if (userId != null) {
			dataMap.put("userId", userId);
			queryTobeExecuted = "userActivityItemsFromDatetoDate";
			if (isCurrentMonth) {
				queryTobeExecuted += "AndUpcoming";
			}
			userActivityItems = getFamstackDataAccessObjectManager()
					.executeAllGroupQuery(
							HQLStrings.getString(queryTobeExecuted), dataMap);
		} else {
			if (isCurrentMonth) {
				queryTobeExecuted += "AndUpcoming";
			}
			userActivityItems = getFamstackDataAccessObjectManager()
					.executeQuery(HQLStrings.getString(queryTobeExecuted),
							dataMap);
		}

		getAllTaskActivitiesFromUserActivity(taskActivitiesList,
				userActivityItems);
		return taskActivitiesList;
	}

	private void getAllTaskActivitiesFromUserActivity(
			List<TaskActivityDetails> taskActivitiesList,
			List<?> userActivityItems) {
		logDebug("userActivityItems :" + userActivityItems);

		if (!userActivityItems.isEmpty()) {
			for (Object userActivityItemObj : userActivityItems) {

				UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;
				Set<UserTaskActivityItem> userTaskActivityItems = userActivityItem
						.getUserTaskActivities();
				if (!userActivityItems.isEmpty()) {
					for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
						TaskActivityDetails taskActivityDetails = mapUserTaskActivityItem(userTaskActivityItem);
						taskActivitiesList.add(taskActivityDetails);
						taskActivityDetails.setDateId(DateUtils.format(
								userActivityItem.getCalenderDate(),
								DateUtils.DATE_FORMAT));
						if (userActivityItem.getUserItem() != null) {
							taskActivityDetails.setUserId(userActivityItem
									.getUserItem().getId());
						}
					}
				}

			}
		}
	}

	public TaskActivityDetails mapUserTaskActivityItem(
			UserTaskActivityItem userTaskActivityItem) {
		TaskActivityDetails taskActivityDetails = new TaskActivityDetails();
		taskActivityDetails.setTaskActivityId(userTaskActivityItem.getId());
		taskActivityDetails.setTaskName(userTaskActivityItem.getTaskName());
		taskActivityDetails.setTaskId(userTaskActivityItem.getTaskId());
		taskActivityDetails.setStartTime(userTaskActivityItem.getStartTime());
		taskActivityDetails.setActualEndTime(userTaskActivityItem
				.getActualEndTime());
		taskActivityDetails.setDurationInMinutes(userTaskActivityItem
				.getDurationInMinutes());
		taskActivityDetails.setUserTaskType(userTaskActivityItem.getType());
		taskActivityDetails.setProjectType(userTaskActivityItem
				.getProjectType());
		taskActivityDetails.setTaskActCategory(userTaskActivityItem
				.getTaskActCategory());
		taskActivityDetails.setStartHour(userTaskActivityItem.getStartHour());
		taskActivityDetails.setInprogressComment((userTaskActivityItem
				.getInprogressComment()));
		taskActivityDetails.setCompletionComment(userTaskActivityItem
				.getCompletionComment());
		taskActivityDetails.setActualStartTime(userTaskActivityItem
				.getActualStartTime());
		taskActivityDetails.setRecordedStartTime(userTaskActivityItem
				.getRecordedStartTime());
		taskActivityDetails.setRecordedEndTime(userTaskActivityItem
				.getRecordedEndTime());
		
		taskActivityDetails.setTeamName(userTaskActivityItem.getTeamName());
		taskActivityDetails.setClientPartner(userTaskActivityItem.getClientPartner());;
		
		taskActivityDetails.setDivision(userTaskActivityItem.getDivision());
		taskActivityDetails.setAccount(userTaskActivityItem.getAccount());
		taskActivityDetails.setOrderBookNumber(userTaskActivityItem.getOrderBookNumber());
		taskActivityDetails.setReferenceNo(userTaskActivityItem.getReferenceNo());
		taskActivityDetails.setActProjectName(userTaskActivityItem.getActProjectName());
		
		taskActivityDetails.setClientName(userTaskActivityItem.getClientName());
		if (userTaskActivityItem.getUserActivityItem() != null
				&& userTaskActivityItem.getUserActivityItem().getUserItem() != null) {
			taskActivityDetails.setUserId(userTaskActivityItem
					.getUserActivityItem().getUserItem().getId());
		}
		return taskActivityDetails;

	}

	public UserTaskActivityItem getUserTaskActivityItem(int taskActivityItemId) {
		return (UserTaskActivityItem) getFamstackDataAccessObjectManager()
				.getItemById(taskActivityItemId, UserTaskActivityItem.class);
	}

	public List<?> getUserTaskActivityItemByTaskId(int taskId) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("taskId", taskId);

		List<?> userTaskActivityItems = getFamstackDataAccessObjectManager()
				.executeAllGroupQuery(
						HQLStrings.getString("userTaskActivityItemByTaskId"),
						dataMap);

		return userTaskActivityItems;
	}

	public List<TaskActivityDetails> getUserTaskActivityDetailsByTaskId(
			int taskId) {

		List<TaskActivityDetails> taskActivityDetails = new ArrayList<>();
		List<UserTaskActivityItem> userTaskActivityItems = (List<UserTaskActivityItem>) getUserTaskActivityItemByTaskId(taskId);

		if (!userTaskActivityItems.isEmpty()) {
			for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
				taskActivityDetails
						.add(mapUserTaskActivityItem(userTaskActivityItem));
			}
			return taskActivityDetails;
		}
		return null;
	}

	public Map<String, Object> setProjectTaskActivityActualTime(int taskId,
			String comment, TaskStatus taskStatus, Date adjustStartTime,
			Date adjustCompletionTimeDate) {
		Integer actualDuration = 0;

		Date currentDate = new Date();

		List<?> userTaskActivityItems = getUserTaskActivityItemByTaskId(taskId);
		Set<Integer> contributersList = new HashSet<>();

		if (adjustStartTime == null) {
			adjustStartTime = currentDate;
		}
		if (adjustCompletionTimeDate == null) {
			adjustCompletionTimeDate = currentDate;
		}
		for (Object userTaskActivityItemObj : userTaskActivityItems) {
			UserTaskActivityItem userTaskActivityItem = (UserTaskActivityItem) userTaskActivityItemObj;
			UserActivityItem userActivityItem = userTaskActivityItem
					.getUserActivityItem();
			if (userActivityItem != null
					&& userActivityItem.getUserItem() != null) {
				contributersList.add(userActivityItem.getUserItem().getId());
			}

			if (userTaskActivityItem.getRecordedEndTime() == null) {
				if (taskStatus == TaskStatus.INPROGRESS) {
					userTaskActivityItem.setInprogressComment(comment);
					userTaskActivityItem.setActualStartTime(new Timestamp(
							adjustStartTime.getTime()));
					userTaskActivityItem.setRecordedStartTime(new Timestamp(
							currentDate.getTime()));
				} else if (taskStatus == TaskStatus.COMPLETED) {
					userTaskActivityItem.setCompletionComment(comment);
					userTaskActivityItem.setActualStartTime(new Timestamp(
							adjustStartTime.getTime()));
					userTaskActivityItem.setActualEndTime(new Timestamp(
							adjustCompletionTimeDate.getTime()));

					if (userTaskActivityItem.getRecordedStartTime() == null) {
						userTaskActivityItem
								.setRecordedStartTime(new Timestamp(currentDate
										.getTime()));
					}
					userTaskActivityItem
							.setModifiedBy(getFamstackApplicationConfiguration()
									.getCurrentUserId());
					userTaskActivityItem.setRecordedEndTime(new Timestamp(
							currentDate.getTime()));
						
					Date completionTime = userTaskActivityItem
							.getActualEndTime();
					Date startTime = userTaskActivityItem.getActualStartTime();

					if (completionTime != null && startTime != null) {
						userTaskActivityItem.setDurationInMinutes(DateUtils
								.getTimeDifference(TimeInType.MINS,
										completionTime.getTime(),
										startTime.getTime()));
					}

					actualDuration += DateUtils
							.getTimeDifference(TimeInType.MINS,
									userTaskActivityItem.getActualEndTime()
											.getTime(), userTaskActivityItem
											.getActualStartTime().getTime());

				}

			} else {
				actualDuration += userTaskActivityItem.getDurationInMinutes();
			}

			getFamstackDataAccessObjectManager().updateItem(
					userTaskActivityItem);
		}
		Map<String, Object> taskActivities = new HashMap<>();

		taskActivities.put(TASK_ACTUAL_DURATION, actualDuration);
		taskActivities.put(TASK_CONTRIBUTERS, contributersList);
		return taskActivities;
	}

	public UserTaskActivityItem completeTaskActivityAndStartNewTaskActivity(
			int taskActivityId, TaskItem taskItem) {
		UserTaskActivityItem currentUserTaskActivityItem = getUserTaskActivityItem(taskActivityId);
		int previousTaskDuration = currentUserTaskActivityItem
				.getDurationInMinutes();
		long previousTaskStartTime = currentUserTaskActivityItem
				.getActualStartTime().getTime();
		long previousTaskPausedTime = taskItem.getTaskPausedTime().getTime();
		int prevoisTaskActualDuration = DateUtils.getTimeDifference(
				TimeInType.MINS, previousTaskPausedTime, previousTaskStartTime);

		int newTaskDuration = previousTaskDuration - prevoisTaskActualDuration;
		Date startDate = new Date();

		currentUserTaskActivityItem.setActualEndTime(taskItem
				.getTaskPausedTime());
		currentUserTaskActivityItem.setRecordedEndTime(taskItem
				.getTaskPausedTime());
		currentUserTaskActivityItem
				.setCompletionComment("Paused task Completed");
		currentUserTaskActivityItem
				.setDurationInMinutes(prevoisTaskActualDuration);
		currentUserTaskActivityItem
				.setModifiedBy(getFamstackApplicationConfiguration()
						.getCurrentUserId());
		famstackDataAccessObjectManager
				.saveOrUpdateItem(currentUserTaskActivityItem);

		UserTaskActivityItem userTaskActivityItem = createUserActivityItem(
				taskItem.getAssignee(), startDate, taskItem.getTaskId(),
				taskItem.getName(), newTaskDuration,
				currentUserTaskActivityItem.getType(), null, taskItem
						.getProjectItem().getType(), null);
		taskItem.setTaskPausedTime(null);
		famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);

		userTaskActivityItem.setActualStartTime(new Timestamp(new Date()
				.getTime()));
		userTaskActivityItem.setRecordedStartTime(new Timestamp(new Date()
				.getTime()));
		famstackDataAccessObjectManager.saveOrUpdateItem(userTaskActivityItem);
		return userTaskActivityItem;
	}

	public Map<Object, UserWorkDetails> getUserBillableProductiveHours(
			Date startTime, Date endTime) {

		Map<Object, UserWorkDetails> userWorkDetailsMap = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", startTime);
		dataMap.put("calenderDateEnd", endTime);

		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeSQLQuery(
						HQLStrings.getString("userBillableProductiveHoursSQL"),
						dataMap);

		for (int i = 0; i < result.size(); i++) {
			UserWorkDetails userWorkDetails = new UserWorkDetails();
			Object[] data = result.get(i);
			userWorkDetails.setBillableMins(data[2]);
			userWorkDetails.setCount(data[0]);
			userWorkDetails.setNonBillableMins(data[3]);
			userWorkDetails.setUserId((Integer) data[1]);

			logDebug("day count " + data[0]);
			logDebug("User ID " + data[1]);
			logDebug("billableHrs " + data[2]);
			logDebug("non billable hours " + data[3]);
			userWorkDetailsMap.put(data[1], userWorkDetails);
		}
		return userWorkDetailsMap;
	}

	public Map<String, Map<Integer, UserWorkDetails>> getUserUtilizationHours(
			Date startDate, Date endDate) {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", startDate);
		dataMap.put("calenderDateEnd", DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_END, endDate, 0));
		List<UserTaskActivityItem> userTaskActivityItems = (List<UserTaskActivityItem>) getFamstackDataAccessObjectManager()
				.executeAllGroupQuery(
						HQLStrings
								.getString("allUserTaskActivityItemsFromDatetoDate"),
						dataMap);

		Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationMap = new HashMap<>();

		for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
			String dateStringKey = DateUtils.format(
					userTaskActivityItem.getActualStartTime(),
					DateUtils.DATE_FORMAT_DP);

			Map<Integer, UserWorkDetails> userWorkList = employeeUtilizationMap
					.get(dateStringKey);

			if (userWorkList == null) {
				userWorkList = new HashMap<>();
				employeeUtilizationMap.put(dateStringKey, userWorkList);
			}
			Integer userId = 0;
			if (userTaskActivityItem.getUserActivityItem() != null
					&& userTaskActivityItem.getUserActivityItem().getUserItem() != null) {
				userId = userTaskActivityItem.getUserActivityItem()
						.getUserItem().getId();
			}
			UserWorkDetails userWorkDetails = userWorkList.get(userId);
			if (userWorkDetails == null) {
				userWorkDetails = new UserWorkDetails();
				userWorkList.put(userId, userWorkDetails);
				userWorkDetails.setUserId(userId);
				userWorkDetails.setCalenderDate(userTaskActivityItem
						.getActualStartTime());
			}
			ProjectType projectType = userTaskActivityItem.getProjectType();
			Integer durationInMinutes = userTaskActivityItem
					.getDurationInMinutes();
			durationInMinutes = durationInMinutes == null ? 0
					: durationInMinutes;
			if (projectType == ProjectType.BILLABLE) {
				userWorkDetails.setBillableMins(durationInMinutes
						+ userWorkDetails.getBillableMins());
			} else if (projectType == ProjectType.NON_BILLABLE) {
				if (UserTaskType.LEAVE == userTaskActivityItem.getType()) {
					userWorkDetails.setLeaveMins(durationInMinutes
							+ userWorkDetails.getLeaveMins());
				} else {
					userWorkDetails.setNonBillableMins(durationInMinutes
							+ userWorkDetails.getNonBillableMins());
				}
			}
		}
		return employeeUtilizationMap;
	}

	public Map<String, Map<Integer, Integer>> getAllNonBillableTaskActivityList(
			Date startDate, Date endDate) {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", startDate);
		dataMap.put("calenderDateEnd", DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_END, endDate, 0));
		List<UserTaskActivityItem> userTaskActivityItems = (List<UserTaskActivityItem>) getFamstackDataAccessObjectManager()
				.executeQuery(
						HQLStrings
								.getString("allUnBilledUserActivityItemsFromDatetoDate"),
						dataMap);
		logDebug("Non billabletask activity items" + userTaskActivityItems);
		Map<String, Map<Integer, Integer>> nonBillableTaskActivityItems = new HashMap<>();

		for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
			Map<Integer, Integer> userTaskTimeMap = nonBillableTaskActivityItems
					.get(userTaskActivityItem.getTaskActCategory());
			if (userTaskTimeMap == null) {
				userTaskTimeMap = new HashMap<>();
			}
			nonBillableTaskActivityItems.put(
					userTaskActivityItem.getTaskActCategory(), userTaskTimeMap);

			int userId = userTaskActivityItem.getUserActivityItem()
					.getUserItem().getId();

			Integer userTaskActTime = userTaskTimeMap.get(userId);

			if (userTaskActTime != null) {
				userTaskActTime += userTaskActivityItem.getDurationInMinutes();
			} else {
				userTaskActTime = userTaskActivityItem.getDurationInMinutes();
			}
			userTaskTimeMap.put(userId, userTaskActTime);
		}

		return nonBillableTaskActivityItems;
	}

	public Map<Integer, Map<String, UserTaskActivityItem>> getAllNonBillabileActivities(
			Date startDate, Date endDate) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", startDate);
		dataMap.put("calenderDateEnd", DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_END, endDate, 0));
		@SuppressWarnings("unchecked")
		List<UserTaskActivityItem> userTaskActivityItems = (List<UserTaskActivityItem>) getFamstackDataAccessObjectManager()
				.executeAllGroupQuery(
						HQLStrings
								.getString("allUnBilledUserActivityItemsFromDatetoDate"),
						dataMap);
		logDebug("Non billabletask activity items" + userTaskActivityItems);
		Map<Integer, Map<String, UserTaskActivityItem>> nonBillableTaskActivityItems = new HashMap<>();

		for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
			Integer userId = userTaskActivityItem.getUserActivityItem()
					.getUserItem().getId();
			String dateString = DateUtils.format(new Date(userTaskActivityItem
					.getActualStartTime().getTime()), DateUtils.DATE_FORMAT);

			Map<String, UserTaskActivityItem> userTaskDateMap = nonBillableTaskActivityItems
					.get(userId);
			if (userTaskDateMap == null) {
				userTaskDateMap = new HashMap<>();
			}
			nonBillableTaskActivityItems.put(userId, userTaskDateMap);

			if (FamstackConstants.LEAVE
							.equalsIgnoreCase(userTaskActivityItem
									.getTaskActCategory())
							|| FamstackConstants.HOLIDAY
									.equalsIgnoreCase(userTaskActivityItem
											.getTaskActCategory()) || FamstackConstants.LEAVE_OR_HOLIDAY
								.equalsIgnoreCase(userTaskActivityItem
									.getTaskActCategory())) {
				userTaskDateMap.put(dateString, userTaskActivityItem);
			}
		}

		return nonBillableTaskActivityItems;
	}

	public void getAllNonBillabileTaskActivities(Date startDate, Date endDate,
			List<ProjectTaskActivityDetails> projectDetailsList,
			List<ProjectTaskActivityDetails> projectDetailsUniqueTasksList,
			List<ProjectTaskActivityDetails> allTaskActivityProjectDetailsList,
			Integer currentUserId) {
		getAllNonBillabileTaskActivities(startDate, endDate,
				projectDetailsList, projectDetailsUniqueTasksList,
				allTaskActivityProjectDetailsList, currentUserId, null);
	}
	@SuppressWarnings("unchecked")
	public void getAllNonBillabileTaskActivities(Date startDate, Date endDate,
			List<ProjectTaskActivityDetails> projectDetailsList,
			List<ProjectTaskActivityDetails> projectDetailsUniqueTasksList,
			List<ProjectTaskActivityDetails> allTaskActivityProjectDetailsList,
			Integer currentUserId, String userGroupId) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("calenderDateStart", DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, startDate, 0));
		dataMap.put("calenderDateEnd", DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_END, endDate, 0));

		String hqlQuery = HQLStrings
				.getString("allUnBilledUserActivityItemsFromDatetoDate");

		if (userGroupId == null) {
			userGroupId = getFamstackApplicationConfiguration()
					.getCurrentUserGroupId();
		}
		
		if (currentUserId == null) {
			hqlQuery += "and userGroupId=" + userGroupId;
		}

		List<UserTaskActivityItem> userTaskActivityItems = (List<UserTaskActivityItem>) getFamstackDataAccessObjectManager()
				.executeAllGroupQuery(hqlQuery, dataMap);

		logDebug("Non billabletask activity items" + userTaskActivityItems);
		List<ProjectTaskActivityDetails> projectTaskActivityDetailsList = new ArrayList<>();
		Map<String, ProjectTaskActivityDetails> projectCacheMap = new HashMap<>();
		Map<String, ProjectTaskActivityDetails> projectUniqueItemCacheMap = new HashMap<>();

		ProjectTaskActivityDetails projectTaskActivityDetailsTmp;
		ProjectTaskActivityDetails projectUniqueItemDetails;
		for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {

			if (currentUserId != null
					&& userTaskActivityItem.getUserActivityItem().getUserItem()
							.getId() != currentUserId) {
				continue;
			}
			ProjectTaskActivityDetails projectTaskActivityDetails = new ProjectTaskActivityDetails();
			projectTaskActivityDetails.setTaskActivityId(userTaskActivityItem
					.getId());
			projectTaskActivityDetails
					.setTaskActivityStartTime(userTaskActivityItem
							.getActualStartTime());
			projectTaskActivityDetails.setUserId(userTaskActivityItem
					.getUserActivityItem().getUserItem().getId());
			projectTaskActivityDetails.setProjectId(0);
			projectTaskActivityDetails.setProjectName("NON BILLABLE");
			//projectTaskActivityDetails.setClientName("");
			projectTaskActivityDetails.setProjectCategory(userTaskActivityItem
					.getTaskActCategory());
			projectTaskActivityDetails.setTaskActCategory(userTaskActivityItem
					.getTaskActCategory());
			projectTaskActivityDetails.setProjectType(ProjectType.NON_BILLABLE);
			projectTaskActivityDetails.setTaskName(userTaskActivityItem
					.getTaskName());
			projectTaskActivityDetails
					.setTaskActivityDuration(userTaskActivityItem
							.getDurationInMinutes());
			projectTaskActivityDetails
					.setTaskCompletionComments(userTaskActivityItem
							.getCompletionComment());
			projectTaskActivityDetails.setClientName(userTaskActivityItem.getClientName());
			projectTaskActivityDetailsList.add(projectTaskActivityDetails);

			String key = "D" + userTaskActivityItem.getActualStartTime();
			key += "T" + userTaskActivityItem.getTaskActCategory();
			key += "U"
					+ userTaskActivityItem.getUserActivityItem().getUserItem()
							.getId();

			String uniqueItemKey = "T"
					+ userTaskActivityItem.getTaskActCategory();
			uniqueItemKey += "U"
					+ userTaskActivityItem.getUserActivityItem().getUserItem()
							.getId();

			projectTaskActivityDetailsTmp = projectCacheMap.get(key);
			projectUniqueItemDetails = projectUniqueItemCacheMap
					.get(uniqueItemKey);
			allTaskActivityProjectDetailsList.add(projectTaskActivityDetails);
			if (projectTaskActivityDetailsTmp != null) {
				projectTaskActivityDetailsTmp
						.addToChildProjectActivityDetailsMap(projectTaskActivityDetails);
				projectTaskActivityDetailsTmp
						.setTaskActivityDuration(projectTaskActivityDetailsTmp
								.getTaskActivityDuration()
								+ projectTaskActivityDetails
										.getTaskActivityDuration());
			} else {
				projectCacheMap.put(key, projectTaskActivityDetails);
				projectDetailsList.add(projectTaskActivityDetails);

				if (projectUniqueItemDetails != null) {
					projectUniqueItemDetails.getSubItems().add(
							projectTaskActivityDetails);
				} else {
					projectUniqueItemCacheMap.put(uniqueItemKey,
							projectTaskActivityDetails);
					projectDetailsUniqueTasksList
							.add(projectTaskActivityDetails);
				}
			}

		}
	}

	public UserTaskActivityItem deleteTaskActivity(int activityId) {
		UserTaskActivityItem userTaskActivityItem = (UserTaskActivityItem) getFamstackDataAccessObjectManager()
				.getItemById(activityId, UserTaskActivityItem.class);
		if (userTaskActivityItem != null) {
			int durationInMinutes = userTaskActivityItem.getDurationInMinutes();
			UserActivityItem userActivityItem = userTaskActivityItem
					.getUserActivityItem();
			if (userTaskActivityItem.getRecordedEndTime() != null) {
				if (userTaskActivityItem.getType() == UserTaskType.LEAVE) {
					int leaveMins = userActivityItem.getLeaveMins();
					userActivityItem
							.setLeaveMins(leaveMins - durationInMinutes);
				} else {
					if (userTaskActivityItem.getProjectType() == ProjectType.BILLABLE) {
						int billableMins = userActivityItem.getBillableMins();
						userActivityItem.setBillableMins(billableMins
								- durationInMinutes);
					} else {
						int nonBillableMins = userActivityItem
								.getNonBillableMins();
						userActivityItem.setNonBillableMins(nonBillableMins
								- durationInMinutes);
					}
				}

				famstackDataAccessObjectManager.updateItem(userActivityItem);
			}

			getFamstackDataAccessObjectManager().deleteItem(
					userTaskActivityItem);
		}
		return userTaskActivityItem;
	}

	public Date getAssigneeSlot(int assigneeId, String startDateString,
			String completionDateString) {

		Date startDate = DateUtils.tryParse(startDateString,
				DateUtils.DATE_TIME_FORMAT);
		Date endDate = DateUtils.tryParse(completionDateString,
				DateUtils.DATE_TIME_FORMAT);

		List<UserActivityItem> userActivityItems = (List<UserActivityItem>) getAllTaskActivityItems(
				DateUtils.format(startDate, DateUtils.DATE_FORMAT_CALENDER),
				DateUtils.format(endDate, DateUtils.DATE_FORMAT_CALENDER),
				assigneeId, false);

		Timestamp userAvailableTime = null;

		if (userActivityItems != null && !userActivityItems.isEmpty()) {
			for (UserActivityItem userActivityItem : userActivityItems) {
				userAvailableTime = getUserAvailableTime(userActivityItem
						.getUserTaskActivities());

				if (userAvailableTime != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(userAvailableTime);
					int hour = cal.get(Calendar.HOUR_OF_DAY);
					if (hour > 8 && hour < 21) {
						return userAvailableTime;
					}
				}
			}
		} else {
			return DateUtils.tryParse(startDateString,
					DateUtils.DATE_TIME_FORMAT);
		}

		return null;
	}

	public UserTaskActivityItem adjustTaskActivityTime(int taskActivityItemId,
			int newDurationInMins, String startTime, String endTime) {
		UserTaskActivityItem userTaskActivityItem = getUserTaskActivityItem(taskActivityItemId);

		if (userTaskActivityItem != null) {
			userTaskActivityItem.setDurationInMinutes(newDurationInMins);
			famstackDataAccessObjectManager
					.saveOrUpdateItem(userTaskActivityItem);
		}

		return userTaskActivityItem;
	}

	public void createUserSiteActivities(Integer userId, Date activityDate,
			boolean status) {
		Date startCalenderDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, activityDate, 0);
		Date endCalenderDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_END, activityDate, 0);

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startCalenderDate", startCalenderDate);
		dataMap.put("endCalenderDate", endCalenderDate);
		dataMap.put("userId", userId);

		List<UserUsageActivityItem> userUsageActivityItems = (List<UserUsageActivityItem>) getFamstackDataAccessObjectManager()
				.executeAllGroupQuery(
						HQLStrings.getString("getUserSiteActivity"), dataMap);

		if (userUsageActivityItems == null
				|| userUsageActivityItems.size() == 0) {
			logInfo("Recording user site activity userId : " + userId
					+ ", Date : " + DateUtils.formatTime(activityDate));
			UserUsageActivityItem userUsageActivityItem = new UserUsageActivityItem();
			userUsageActivityItem.setUserId(userId);
			userUsageActivityItem.setCalenderDate(new Timestamp(activityDate
					.getTime()));
			famstackDataAccessObjectManager
					.saveOrUpdateItem(userUsageActivityItem);
			getFamstackUserSessionConfiguration().setUserLastActivityDate(
					new Date());
		} else if (!status) {
			famstackDataAccessObjectManager.deleteItem(userUsageActivityItems
					.get(0));
		}

	}

	public Map<Integer, Map<String, String>> getAllUserSiteActivities(
			Date startDate, Date endDate, String userGroupId) {
		Date startCalenderDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, startDate, 0);
		Date endCalenderDate = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_END, endDate, 0);
		Map<Integer, Map<String, String>> userSiteActivityMap = new HashMap<>();

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startCalenderDate", startCalenderDate);
		dataMap.put("endCalenderDate", endCalenderDate);
		String hqlQuery = HQLStrings.getString("getUserSiteActivityAllByDate");

		if (userGroupId != null) {
			dataMap.put("userGroupId", userGroupId);
			hqlQuery = HQLStrings.getString("getUserSiteActivityByDate");
		}

		List<UserUsageActivityItem> userUsageActivityItems = null;

		userUsageActivityItems = (List<UserUsageActivityItem>) getFamstackDataAccessObjectManager()
				.executeAllGroupQuery(hqlQuery, dataMap);

		for (UserUsageActivityItem userUsageActivityItem : userUsageActivityItems) {
			Map<String, String> userSiteDateActivity = userSiteActivityMap
					.get(userUsageActivityItem.getUserId());

			if (userSiteDateActivity == null) {
				userSiteDateActivity = new HashMap<>();
				userSiteActivityMap.put(userUsageActivityItem.getUserId(),
						userSiteDateActivity);
			}
			String dateString = DateUtils.format(new Date(userUsageActivityItem
					.getCalenderDate().getTime()), DateUtils.DATE_FORMAT);
			logDebug("User activity user id : "
					+ userUsageActivityItem.getUserId() + " Date :"
					+ dateString);
			userSiteDateActivity.put(dateString, "Active");

		}

		return userSiteActivityMap;

	}

	public List<UserSiteActivityDetails> getUserSiteActivityForReport(
			Map<Integer, Map<String, String>> userSiteActivityMap,
			Map<Integer, Map<String, UserTaskActivityItem>> nonBillativityMap,
			List<EmployeeDetails> employeesList, List<String> dateList, List<String> excludeMailList) {

		List<UserSiteActivityDetails> activeUserList = new ArrayList<>();
		List<UserSiteActivityDetails> inActiveUserList = new ArrayList<>();
		List<UserSiteActivityDetails> leaveOrHoliday = new ArrayList<>();

		if (employeesList != null) {
			for (EmployeeDetails employeeDetails : employeesList) {

				if(excludeMailList != null && excludeMailList.contains(employeeDetails.getEmail())) {
					continue;
				}
				
				UserSiteActivityDetails userSiteActivityDetails = new UserSiteActivityDetails();
				userSiteActivityDetails.setEmployeeName(employeeDetails.getFirstName());
				userSiteActivityDetails.setEmailId(employeeDetails.getEmail());
				try{
					if (employeeDetails.getReportertingManagerEmailId() != null) {
						Integer reportingMangerId = getFamstackApplicationConfiguration().getUserIdMap().get(employeeDetails.getReportertingManagerEmailId());
						if (reportingMangerId != null) {
							EmployeeDetails reportingEmployeeDetail = getFamstackApplicationConfiguration().getAllUsersMap().get(reportingMangerId);
							if (reportingEmployeeDetail != null) {
								userSiteActivityDetails.setReportingManager(reportingEmployeeDetail.getFirstName());
							}
						}
					}
				}catch(Exception e){
				}
				Map<String, String> activeStatusMap = userSiteActivityMap.get(employeeDetails.getId());
				Map<String, UserTaskActivityItem> inActiveStatusMap = nonBillativityMap.get(employeeDetails.getId());
				boolean isInActive = false;
				boolean isHolidayOrLeave = false;
				int index = 0;
				for (String dateString : dateList) {
					UserSiteActivityStatus userSiteActivityStatus = new UserSiteActivityStatus();
					userSiteActivityDetails.getStatusList().add(
							userSiteActivityStatus);
					userSiteActivityStatus.setDateString(dateString);
					String activeStatus = activeStatusMap != null ? activeStatusMap
							.get(dateString) : null;
					if (activeStatus != null) {
						userSiteActivityStatus.setStatus(activeStatus);
					} else {
						UserTaskActivityItem userTaskActivityItem = inActiveStatusMap != null ? inActiveStatusMap
								.get(dateString) : null;
						if (userTaskActivityItem != null) {
							userSiteActivityStatus.setStatus(userTaskActivityItem.getTaskActCategory());
							if (index == 0) {
								isHolidayOrLeave = true;
							}
						} else {
							userSiteActivityStatus.setStatus("Inactive");
							userSiteActivityDetails.setIncludeInactive(true);
							if (index == 0) {
								isInActive = true;
							}
						}
					}

					index++;
				}

				if (isInActive) {
					inActiveUserList.add(userSiteActivityDetails);
				} else if (isHolidayOrLeave) {
					leaveOrHoliday.add(userSiteActivityDetails);
				} else {
					activeUserList.add(userSiteActivityDetails);
				}
			}
		}

		inActiveUserList.addAll(activeUserList);
		inActiveUserList.addAll(leaveOrHoliday);

		return inActiveUserList;
	}

	public List<ProjectTaskActivityDetails> getAllNonBillableTaskActivities(
			Date startDate, Date endDate, boolean uniqueList,
			boolean addSameTaskActTime, Integer currentUserId) {
		return getAllNonBillableTaskActivities(startDate, endDate, uniqueList,
				addSameTaskActTime, currentUserId, null);
	}

	public List<ProjectTaskActivityDetails> getAllNonBillableTaskActivities(
			Date startDate, Date endDate, boolean uniqueList,
			boolean addSameTaskActTime, Integer currentUserId,
			String userGroupId) {
		List<ProjectTaskActivityDetails> projectDetailsUniqueTasksList = new ArrayList<>();
		List<ProjectTaskActivityDetails> projectDetailsList = new ArrayList<>();
		List<ProjectTaskActivityDetails> allTaskActProjectDetailsList = new ArrayList<>();
		getAllNonBillabileTaskActivities(startDate,
				endDate, projectDetailsList, projectDetailsUniqueTasksList,
				allTaskActProjectDetailsList, currentUserId, userGroupId);

		if (!addSameTaskActTime) {
			return allTaskActProjectDetailsList;
		}

		if (uniqueList) {
			return projectDetailsUniqueTasksList;
		}

		return projectDetailsList;
	}
}
