package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.LeaveType;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.datatransferobject.UserActivityItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.security.user.UserRole;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.TimeInType;

public class FamstackUserActivityManager extends BaseFamstackManager
{

    private static final String TASK_CONTRIBUTERS = "TASK_CONTRIBUTERS";

    private static final String TASK_ACTUAL_DURATION = "TASK_ACTUAL_DURATION";

    @Resource
    FamstackProjectActivityManager famstackProjectActivityManager;

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    public void createCompletedUserActivityItem(int userId, Date startTime, int taskId, String taskName,
        int durationInMinutes, UserTaskType userTaskType, ProjectType projectType, String comment)
    {
        UserTaskActivityItem userTaskActivityItem =
            createUserActivityItem(userId, startTime, taskId, taskName, durationInMinutes, userTaskType, projectType,
                null);
        userTaskActivityItem.setCompletionComment(comment);
        userTaskActivityItem.setRecordedStartTime(new Timestamp(startTime.getTime()));
        userTaskActivityItem.setActualStartTime(new Timestamp(startTime.getTime()));
        Long endTime = DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, startTime, durationInMinutes).getTime();
        userTaskActivityItem.setActualEndTime(new Timestamp(endTime));
        userTaskActivityItem.setRecordedEndTime(new Timestamp(endTime));
        getFamstackDataAccessObjectManager().saveOrUpdateItem(userTaskActivityItem);
    }

    public UserTaskActivityItem createUserActivityItem(int userId, Date startTime, int taskId, String taskName,
        int durationInMinutes, UserTaskType userTaskType, ProjectType projectType, String userGroupId)
    {

        UserItem assigneeUserItem = famstackUserProfileManager.getUserItemById(userId);
        Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startTime, 0);
        Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, startTime, 0);
        Date calenderDate = DateUtils.getNextPreviousDate(DateTimePeriod.CALENDER_DAY_START, startTime, 0);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("calenderDateStart", dayStartDate);
        dataMap.put("calenderDateEnd", dayEndDate);
        dataMap.put("userId", userId);
        List<?> userActivityItemlist = null;
        if (userGroupId == null) {
            userActivityItemlist =
                getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString("getUserActivityByDate"),
                    dataMap);
        } else {
            userActivityItemlist =
                getFamstackDataAccessObjectManager().executeQueryWithGroupId(
                    HQLStrings.getString("getUserActivityByDate"), dataMap, userGroupId);
        }

        UserActivityItem userActivityItem = null;
        if (!userActivityItemlist.isEmpty()) {
            userActivityItem = (UserActivityItem) userActivityItemlist.get(0);
        }

        if (userActivityItem == null) {
            userActivityItem = new UserActivityItem();
            userActivityItem.setCalenderDate(new Timestamp(calenderDate.getTime()));
            userActivityItem.setUserItem(assigneeUserItem);
        }

        if (userTaskType == UserTaskType.LEAVE) {
            int leaveMins = userActivityItem.getLeaveMins();
            leaveMins += (durationInMinutes);
            userActivityItem.setLeaveMins(leaveMins);
        } else {
            userActivityItem.setLeave(LeaveType.FULL);
            int nonBillableHrs = userActivityItem.getNonBillableMins();
            int billableHours = userActivityItem.getBillableMins();
            if (projectType == ProjectType.BILLABLE) {
                billableHours += (durationInMinutes);
            } else {
                nonBillableHrs += (durationInMinutes);
            }
            userActivityItem.setNonBillableMins(nonBillableHrs);
            userActivityItem.setBillableMins(billableHours);
        }

        userActivityItem.setUserGroupId(userGroupId);
        getFamstackDataAccessObjectManager().saveOrUpdateItem(userActivityItem);
        return setUserTaskActivity(userActivityItem, taskId, taskName, durationInMinutes, startTime, userTaskType,
            projectType);
    }

    public List<?> getTodaysUserActivity()
    {

        Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), 0);
        Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, new Date(), 0);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("calenderDateStart", dayStartDate);
        dataMap.put("calenderDateEnd", dayEndDate);

        String queryKey = "allUserActivityItemsFromDatetoDate";

        List<?> userActivityItems =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(HQLStrings.getString(queryKey), dataMap);

        return userActivityItems;
    }

    public void initialize()
    {
        updateAllUserAvailableTime();
    }

    public void updateAllUserAvailableTime()
    {
        for (Object userActivityItemObj : getTodaysUserActivity()) {
            UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;

            EmployeeDetails employeeDetails =
                getFamstackApplicationConfiguration().getUserMap().get(userActivityItem.getUserItem().getId());

            if (userActivityItem.getLeave() != null) {
                employeeDetails.setLeave(LeaveType.FULL);
                logDebug("User is on leave ");
                return;
            }
            Timestamp availableTime = getUserAvailableTime(userActivityItem.getUserTaskActivities());

            if (employeeDetails != null) {
                logDebug("updated available time for user " + userActivityItem.getId());
                employeeDetails.setUserAvailableTime(availableTime);
            }
        }
    }

    private Timestamp getUserAvailableTime(Set<UserTaskActivityItem> userTaskActivityItems)
    {
        Timestamp userAvailableTime = null;
        logDebug("updating user available time ");
        for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
            userAvailableTime = getUserAvaliableTime(userAvailableTime, userTaskActivityItem);
        }

        return userAvailableTime;

    }

    private Timestamp getUserAvaliableTime(Timestamp userAvailableTime, UserTaskActivityItem userTaskActivityItem)
    {
        logDebug("getUserAvaliableTime task : " + userTaskActivityItem.getTaskId());
        Timestamp actualEndTime = userTaskActivityItem.getActualEndTime();
        Timestamp startTime = userTaskActivityItem.getStartTime();
        int durationInMinutes = userTaskActivityItem.getDurationInMinutes();
        startTime.setTime(startTime.getTime() + (durationInMinutes * 60 * 1000));
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
        logDebug("getUserAvaliableTime time : " + userAvailableTime);
        return userAvailableTime;
    }

    public void deleteAllUserTaskActivities(int taskId)
    {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskId", taskId);

        getFamstackDataAccessObjectManager().executeUpdate(HQLStrings.getString("deleteAllUserTaskItemsByTaskId"),
            dataMap);

    }

    public UserTaskActivityItem setUserTaskActivity(UserActivityItem userActivityItem, int taskId, String taskName,
        int durationInMinutes, Date startDate, UserTaskType userTaskType, ProjectType projectType)
    {
        UserTaskActivityItem userTaskActivityItem = new UserTaskActivityItem();
        userTaskActivityItem.setTaskId(taskId);
        userTaskActivityItem.setTaskName(taskName);
        userTaskActivityItem.setDurationInMinutes(durationInMinutes);
        userTaskActivityItem.setStartHour(startDate.getHours());
        userTaskActivityItem.setStartTime(new Timestamp(startDate.getTime()));
        userTaskActivityItem.setType(userTaskType);
        userTaskActivityItem.setProjectType(projectType);
        userTaskActivityItem.setCreatedDate(new Timestamp(new Date().getTime()));
        userTaskActivityItem.setLastModifiedDate(new Timestamp(new Date().getTime()));
        userTaskActivityItem.setUserGroupId(userActivityItem.getUserGroupId());
        userTaskActivityItem.setUserActivityItem(userActivityItem);
        getFamstackDataAccessObjectManager().saveOrUpdateItem(userTaskActivityItem);
        return userTaskActivityItem;

    }

    public List<TaskActivityDetails> getAllTaskActivities(String startDateString, String completionDateString)
    {
        List<TaskActivityDetails> taskActivitiesList = new ArrayList<>();
        int currentUserId = getFamstackApplicationConfiguration().getCurrentUserId();
        List<?> userActivityItems = getAllTaskActivityItems(startDateString, completionDateString, currentUserId, true);

        getAllTaskActivitiesFromUserActivity(taskActivitiesList, userActivityItems);
        return taskActivitiesList;
    }

    private List<?> getAllTaskActivityItems(String startDateString, String completionDateString, int currentUserId,
        boolean isFullLoad)
    {
        Date startDate = DateUtils.tryParse(startDateString, DateUtils.DATE_FORMAT_CALENDER);
        Date completionDate = DateUtils.tryParse(completionDateString, DateUtils.DATE_FORMAT_CALENDER);
        logDebug("startDateString" + startDateString);
        logDebug("completionDateString" + completionDateString);
        logDebug("startDate" + startDate);
        logDebug("completionDate" + completionDate);

        Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startDate, 0);
        Date dayEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, completionDate, 0);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("calenderDateStart", dayStartDate);
        dataMap.put("calenderDateEnd", dayEndDate);
        logDebug("dayStartDate : " + dayStartDate);

        UserRole userRole = getFamstackApplicationConfiguration().getCurrentUser().getUserRole();

        String queryKey = "userActivityItemsFromDatetoDate";
        if ((userRole == UserRole.ADMIN || userRole == UserRole.SUPERADMIN || userRole == UserRole.TEAMLEAD)
            && isFullLoad) {
            queryKey = "allUserActivityItemsFromDatetoDate";
        } else {
            dataMap.put("userId", currentUserId);
        }

        List<?> userActivityItems =
            getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString(queryKey), dataMap);
        return userActivityItems;
    }

    public List<TaskActivityDetails> getAllTaskActivities()
    {
        Date dayStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), -30);
        List<TaskActivityDetails> taskActivitiesList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("calenderDateStart", dayStartDate);
        logDebug("dayStartDate : " + dayStartDate);

        List<?> userActivityItems =
            getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString("allUserActivityItemsFromToday"),
                dataMap);

        getAllTaskActivitiesFromUserActivity(taskActivitiesList, userActivityItems);
        return taskActivitiesList;
    }

    private void getAllTaskActivitiesFromUserActivity(List<TaskActivityDetails> taskActivitiesList,
        List<?> userActivityItems)
    {
        logDebug("userActivityItems :" + userActivityItems);

        if (!userActivityItems.isEmpty()) {
            for (Object userActivityItemObj : userActivityItems) {

                UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;
                Set<UserTaskActivityItem> userTaskActivityItems = userActivityItem.getUserTaskActivities();
                logDebug("userActivityItem.getId() :" + userActivityItem.getId());
                if (!userActivityItems.isEmpty()) {
                    for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                        TaskActivityDetails taskActivityDetails = mapUserTaskActivityItem(userTaskActivityItem);
                        taskActivitiesList.add(taskActivityDetails);
                        taskActivityDetails.setDateId(DateUtils.format(userActivityItem.getCalenderDate(),
                            DateUtils.DATE_FORMAT));
                        if (userActivityItem.getUserItem() != null) {
                            taskActivityDetails.setUserId(userActivityItem.getUserItem().getId());
                        }
                    }
                }

            }
        }
    }

    public TaskActivityDetails mapUserTaskActivityItem(UserTaskActivityItem userTaskActivityItem)
    {
        TaskActivityDetails taskActivityDetails = new TaskActivityDetails();
        taskActivityDetails.setTaskActivityId(userTaskActivityItem.getId());
        taskActivityDetails.setTaskName(userTaskActivityItem.getTaskName());
        taskActivityDetails.setTaskId(userTaskActivityItem.getTaskId());
        taskActivityDetails.setStartTime(userTaskActivityItem.getStartTime());
        taskActivityDetails.setActualEndTime(userTaskActivityItem.getActualEndTime());
        taskActivityDetails.setDurationInMinutes(userTaskActivityItem.getDurationInMinutes());
        taskActivityDetails.setUserTaskType(userTaskActivityItem.getType());
        taskActivityDetails.setStartHour(userTaskActivityItem.getStartHour());
        taskActivityDetails.setInprogressComment((userTaskActivityItem.getInprogressComment()));
        taskActivityDetails.setCompletionComment(userTaskActivityItem.getCompletionComment());
        taskActivityDetails.setActualStartTime(userTaskActivityItem.getActualStartTime());
        taskActivityDetails.setRecordedStartTime(userTaskActivityItem.getRecordedStartTime());
        taskActivityDetails.setRecordedEndTime(userTaskActivityItem.getRecordedEndTime());
        if (userTaskActivityItem.getUserActivityItem() != null
            && userTaskActivityItem.getUserActivityItem().getUserItem() != null) {
            taskActivityDetails.setUserId(userTaskActivityItem.getUserActivityItem().getUserItem().getId());
        }
        return taskActivityDetails;

    }

    public UserTaskActivityItem getUserTaskActivityItem(int taskActivityItemId)
    {
        return (UserTaskActivityItem) getFamstackDataAccessObjectManager().getItemById(taskActivityItemId,
            UserTaskActivityItem.class);
    }

    public List<?> getUserTaskActivityItemByTaskId(int taskId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskId", taskId);

        List<?> userTaskActivityItems =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(
                HQLStrings.getString("userTaskActivityItemByTaskId"), dataMap);

        return userTaskActivityItems;
    }

    public List<TaskActivityDetails> getUserTaskActivityDetailsByTaskId(int taskId)
    {

        List<TaskActivityDetails> taskActivityDetails = new ArrayList<>();
        List<UserTaskActivityItem> userTaskActivityItems =
            (List<UserTaskActivityItem>) getUserTaskActivityItemByTaskId(taskId);

        if (!userTaskActivityItems.isEmpty()) {
            for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                taskActivityDetails.add(mapUserTaskActivityItem(userTaskActivityItem));
            }
            return taskActivityDetails;
        }
        return null;
    }

    public Map<String, Object> setProjectTaskActivityActualTime(int taskId, Date date, String comment,
        TaskStatus taskStatus, Date adjustStartTime, Date adjustCompletionTimeDate)
    {
        Integer actualDuration = 0;

        List<?> userTaskActivityItems = getUserTaskActivityItemByTaskId(taskId);
        List<Integer> contributersList = new ArrayList<>();

        if (adjustStartTime == null) {
            adjustStartTime = date;
        }
        if (adjustCompletionTimeDate == null) {
            adjustCompletionTimeDate = date;
        }
        for (Object userTaskActivityItemObj : userTaskActivityItems) {
            UserTaskActivityItem userTaskActivityItem = (UserTaskActivityItem) userTaskActivityItemObj;
            UserActivityItem userActivityItem = userTaskActivityItem.getUserActivityItem();
            if (userActivityItem != null && userActivityItem.getUserItem() != null) {
                contributersList.add(userActivityItem.getUserItem().getId());
            }

            if (userTaskActivityItem.getRecordedEndTime() == null) {
                if (taskStatus == TaskStatus.INPROGRESS) {
                    userTaskActivityItem.setInprogressComment(comment);
                    userTaskActivityItem.setActualStartTime(new Timestamp(adjustStartTime.getTime()));
                    userTaskActivityItem.setRecordedStartTime(new Timestamp(date.getTime()));
                } else if (taskStatus == TaskStatus.COMPLETED) {
                    userTaskActivityItem.setCompletionComment(comment);
                    userTaskActivityItem.setActualStartTime(new Timestamp(adjustStartTime.getTime()));
                    userTaskActivityItem.setActualEndTime(new Timestamp(adjustCompletionTimeDate.getTime()));
                    userTaskActivityItem.setRecordedEndTime(new Timestamp(date.getTime()));

                    Date completionTime = userTaskActivityItem.getActualEndTime();
                    Date startTime = userTaskActivityItem.getActualStartTime();

                    if (completionTime != null && startTime != null) {
                        userTaskActivityItem.setDurationInMinutes(DateUtils.getTimeDifference(TimeInType.MINS,
                            completionTime.getTime(), startTime.getTime()));
                    }

                    actualDuration +=
                        DateUtils.getTimeDifference(TimeInType.MINS, userTaskActivityItem.getActualEndTime().getTime(),
                            userTaskActivityItem.getActualStartTime().getTime());

                }

            } else {
                actualDuration += userTaskActivityItem.getDurationInMinutes();
            }

            getFamstackDataAccessObjectManager().updateItem(userTaskActivityItem);
        }
        Map<String, Object> taskActivities = new HashMap<>();

        taskActivities.put(TASK_ACTUAL_DURATION, actualDuration);
        taskActivities.put(TASK_CONTRIBUTERS, contributersList);
        return taskActivities;
    }

    public UserTaskActivityItem completeTaskActivityAndStartNewTaskActivity(int taskActivityId, TaskItem taskItem)
    {
        UserTaskActivityItem currentUserTaskActivityItem = getUserTaskActivityItem(taskActivityId);
        int previousTaskDuration = currentUserTaskActivityItem.getDurationInMinutes();
        long previousTaskStartTime = currentUserTaskActivityItem.getActualStartTime().getTime();
        long previousTaskPausedTime = taskItem.getTaskPausedTime().getTime();
        int prevoisTaskActualDuration =
            DateUtils.getTimeDifference(TimeInType.MINS, previousTaskPausedTime, previousTaskStartTime);

        int newTaskDuration = previousTaskDuration - prevoisTaskActualDuration;
        Date startDate = new Date();

        currentUserTaskActivityItem.setActualEndTime(taskItem.getTaskPausedTime());
        currentUserTaskActivityItem.setRecordedEndTime(taskItem.getTaskPausedTime());
        currentUserTaskActivityItem.setCompletionComment("Paused task Completed");
        currentUserTaskActivityItem.setDurationInMinutes(prevoisTaskActualDuration);
        famstackDataAccessObjectManager.saveOrUpdateItem(currentUserTaskActivityItem);

        UserTaskActivityItem userTaskActivityItem =
            createUserActivityItem(taskItem.getAssignee(), startDate, taskItem.getTaskId(), taskItem.getName(),
                newTaskDuration, currentUserTaskActivityItem.getType(), taskItem.getProjectItem().getType(), null);
        taskItem.setTaskPausedTime(null);
        famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);

        userTaskActivityItem.setActualStartTime(new Timestamp(new Date().getTime()));
        userTaskActivityItem.setRecordedStartTime(new Timestamp(new Date().getTime()));
        famstackDataAccessObjectManager.saveOrUpdateItem(userTaskActivityItem);
        return userTaskActivityItem;
    }

    public Map<Object, UserWorkDetails> getUserBillableProductiveHours(Date startTime, Date endTime)
    {

        Map<Object, UserWorkDetails> userWorkDetailsMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("calenderDateStart", startTime);
        dataMap.put("calenderDateEnd", endTime);

        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(
                HQLStrings.getString("userBillableProductiveHoursSQL"), dataMap);

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

    public Map<String, Map<Integer, UserWorkDetails>> getUserUtilizationHours(Date startDate, Date endDate)
    {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("calenderDateStart", startDate);
        dataMap.put("calenderDateEnd", endDate);
        List<UserActivityItem> userActivityItems =
            (List<UserActivityItem>) getFamstackDataAccessObjectManager().executeQuery(
                HQLStrings.getString("allUserActivityItemsFromDatetoDate"), dataMap);

        Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationMap = new HashMap<>();

        for (UserActivityItem userActivityItem : userActivityItems) {
            String dateStringKey = DateUtils.format(userActivityItem.getCalenderDate(), DateUtils.DATE_FORMAT_DP);
            Map<Integer, UserWorkDetails> userWorkList = employeeUtilizationMap.get(dateStringKey);

            if (userWorkList == null) {
                userWorkList = new HashMap<>();
                employeeUtilizationMap.put(dateStringKey, userWorkList);
            }

            UserWorkDetails userWorkDetails = new UserWorkDetails();
            userWorkDetails.setBillableMins(userActivityItem.getBillableMins());
            userWorkDetails.setNonBillableMins(userActivityItem.getNonBillableMins());
            userWorkDetails.setUserId(userActivityItem.getUserItem().getId());
            userWorkDetails.setLeaveMins(userActivityItem.getLeaveMins());
            userWorkDetails.setCalenderDate(userActivityItem.getCalenderDate());
            userWorkList.put(userWorkDetails.getUserId(), userWorkDetails);
        }
        return employeeUtilizationMap;
    }

    public UserTaskActivityItem deleteTaskActivity(int activityId)
    {
        UserTaskActivityItem userTaskActivityItem =
            (UserTaskActivityItem) getFamstackDataAccessObjectManager().getItemById(activityId,
                UserTaskActivityItem.class);
        if (userTaskActivityItem != null) {
            int durationInMinutes = userTaskActivityItem.getDurationInMinutes();
            UserActivityItem userActivityItem = userTaskActivityItem.getUserActivityItem();
            if (userTaskActivityItem.getType() == UserTaskType.LEAVE) {
                int leaveMins = userActivityItem.getLeaveMins();
                userActivityItem.setBillableMins(leaveMins - durationInMinutes);
            } else {
                if (userTaskActivityItem.getProjectType() == ProjectType.BILLABLE) {
                    int billableMins = userActivityItem.getBillableMins();
                    userActivityItem.setBillableMins(billableMins - durationInMinutes);
                } else {
                    int nonBillableMins = userActivityItem.getNonBillableMins();
                    userActivityItem.setNonBillableMins(nonBillableMins - durationInMinutes);
                }
            }

            famstackDataAccessObjectManager.updateItem(userActivityItem);

            getFamstackDataAccessObjectManager().deleteItem(userTaskActivityItem);
        }
        return userTaskActivityItem;
    }

    public Date getAssigneeSlot(int assigneeId, String startDateString, String completionDateString)
    {

        Date startDate = DateUtils.tryParse(startDateString, DateUtils.DATE_TIME_FORMAT);
        Date endDate = DateUtils.tryParse(completionDateString, DateUtils.DATE_TIME_FORMAT);

        List<UserActivityItem> userActivityItems =
            (List<UserActivityItem>) getAllTaskActivityItems(
                DateUtils.format(startDate, DateUtils.DATE_FORMAT_CALENDER),
                DateUtils.format(endDate, DateUtils.DATE_FORMAT_CALENDER), assigneeId, false);

        Timestamp userAvailableTime = null;

        if (userActivityItems != null && !userActivityItems.isEmpty()) {
            for (UserActivityItem userActivityItem : userActivityItems) {
                userAvailableTime = getUserAvailableTime(userActivityItem.getUserTaskActivities());

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
            return DateUtils.tryParse(startDateString, DateUtils.DATE_TIME_FORMAT);
        }

        return null;
    }

    public UserTaskActivityItem adjustTaskActivityTime(int taskActivityItemId, int newDurationInMins, String startTime,
        String endTime)
    {
        UserTaskActivityItem userTaskActivityItem = getUserTaskActivityItem(taskActivityItemId);

        if (userTaskActivityItem != null) {
            userTaskActivityItem.setDurationInMinutes(newDurationInMins);
            famstackDataAccessObjectManager.saveOrUpdateItem(userTaskActivityItem);
        }

        return userTaskActivityItem;
    }
}
