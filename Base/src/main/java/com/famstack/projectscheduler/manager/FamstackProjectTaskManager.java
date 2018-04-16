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
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectTaskType;
import com.famstack.projectscheduler.contants.ProjectType;
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
public class FamstackProjectTaskManager extends BaseFamstackManager
{

    private static final String TASK_CONTRIBUTERS = "TASK_CONTRIBUTERS";

    private static final String TASK_ACTUAL_DURATION = "TASK_ACTUAL_DURATION";

    @Resource
    FamstackProjectActivityManager famstackProjectActivityManager;

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    public void createTaskItem(TaskDetails taskDetails, ProjectItem projectItem)
    {
        createTaskItem(taskDetails, projectItem, false);
    }

    public void createTaskItem(TaskDetails taskDetails, ProjectItem projectItem, Boolean isScheduler)
    {
        TaskItem taskItem = new TaskItem();

        taskItem.setStatus(TaskStatus.NEW);
        if (!isScheduler) {
            taskItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
        }
        saveTask(taskDetails, projectItem, taskItem);
        famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.TASK_ADDED,
            taskItem.getName());

    }

    public void createExtraTaskItem(TaskDetails taskDetails, ProjectItem projectItem)
    {
        Set<TaskItem> taskItems = projectItem.getTaskItems();
        UserTaskActivityItem userTaskActivityItemOld = null;
        int durationNewMinutes = taskDetails.getDuration();

        TaskItem taskItemNew = null;
        if (taskItems != null) {
            for (TaskItem taskItem : taskItems) {
                if (taskItem.getExtraTimeTask()) {
                    taskItemNew = taskItem;
                    break;
                }
            }
        }

        if (taskItemNew == null) {
            taskItemNew = new TaskItem();
            taskItemNew.setName("Project extra time");
            taskItemNew.setStatus(TaskStatus.COMPLETED);
            taskItemNew.setDescription(taskDetails.getDescription());
            taskItemNew.setCompletionTime(projectItem.getCompletionTime());
            taskItemNew.setStartTime(projectItem.getStartTime());
            taskItemNew.setPriority(ProjectPriority.HIGH);
            taskItemNew.setProjectItem(projectItem);
            taskItemNew.setProjectTaskType(ProjectTaskType.PRODUCTIVE);
            taskItemNew.setExtraTimeTask(true);
            taskItemNew.setDuration(durationNewMinutes / 60);
            taskItemNew.setActualTimeTaken(durationNewMinutes);
            taskItemNew.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
            taskItemNew.setAssignee(taskDetails.getAssignee());
        } else {
            String helpers = "" + taskDetails.getAssignee();
            if (taskItemNew.getHelpers() != null) {
                helpers += "," + taskDetails.getAssignee();
            }
            taskItemNew.setHelpers(helpers);

            int durationInMins = taskItemNew.getDuration() * 60 + durationNewMinutes;
            List<UserTaskActivityItem> userTaskActivityItems =
                (List<UserTaskActivityItem>) famstackUserActivityManager.getUserTaskActivityItemByTaskId(taskItemNew
                    .getTaskId());
            if (userTaskActivityItems != null) {
                for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                    if (userTaskActivityItem.getUserActivityItem().getUserItem().getId() == taskDetails.getAssignee()) {
                        userTaskActivityItemOld = userTaskActivityItem;
                        durationInMins -= (userTaskActivityItemOld.getDurationInMinutes());
                        break;
                    }
                }
            }

            taskItemNew.setDuration(durationInMins / 60);
        }

        taskItemNew = (TaskItem) famstackDataAccessObjectManager.saveOrUpdateItem(taskItemNew);

        if (userTaskActivityItemOld == null) {
            famstackUserActivityManager.createCompletedUserActivityItem(taskDetails.getAssignee(),
                projectItem.getStartTime(), taskItemNew.getTaskId(), taskItemNew.getName(), durationNewMinutes,
                UserTaskType.EXTRATIME, projectItem.getType(), taskDetails.getDescription());
        } else {
            userTaskActivityItemOld.setDurationInMinutes(durationNewMinutes);
            userTaskActivityItemOld.setTaskName(taskDetails.getName());
            userTaskActivityItemOld.setTaskName(taskDetails.getName());
            userTaskActivityItemOld.setCompletionComment(taskDetails.getDescription());
            famstackDataAccessObjectManager.saveOrUpdateItem(userTaskActivityItemOld);
        }

        updateTaskActualDurationFromActivities(taskItemNew);
        calculateBillableAndNonBillableTime(taskItemNew.getTaskId());

    }

    private void updateTaskActualDurationFromActivities(TaskItem taskItemNew)
    {
        Integer actualDuration = 0;

        List<UserTaskActivityItem> userTaskActivityItems =
            (List<UserTaskActivityItem>) famstackUserActivityManager.getUserTaskActivityItemByTaskId(taskItemNew
                .getTaskId());

        List<Integer> contributersList = new ArrayList<>();

        for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
            UserActivityItem userActivityItem = userTaskActivityItem.getUserActivityItem();
            if (userActivityItem != null && userActivityItem.getUserItem() != null) {
                contributersList.add(userActivityItem.getUserItem().getId());
            }
            actualDuration += userTaskActivityItem.getDurationInMinutes();
        }

        taskItemNew.setActualTimeTaken(actualDuration);
        taskItemNew.setContributers(contributersList.toString().replace("[", "").replaceAll("]", "")
            .replace("null", ""));

        famstackDataAccessObjectManager.saveOrUpdateItem(taskItemNew);
    }

    public void updateTask(TaskDetails taskDetails, ProjectItem projectItem)
    {
        TaskItem taskItem =
            (TaskItem) famstackDataAccessObjectManager.getItemById(taskDetails.getTaskId(), TaskItem.class);
        if (taskItem != null) {
            saveTask(taskDetails, projectItem, taskItem);
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.TASK_UPDTED,
                taskItem.getName());
        }
    }

    private void saveTask(TaskDetails taskDetails, ProjectItem projectItem, TaskItem taskItem)
    {
        taskItem.setDescription(taskDetails.getDescription());
        taskItem.setName(taskDetails.getName());
        taskItem.setPriority(taskDetails.getPriority());
        taskItem.setProjectItem(projectItem);
        taskItem.setUserGroupId(projectItem.getUserGroupId());
        taskItem.setActualTimeTaken(taskDetails.getActualTimeTaken());
        taskItem.setTaskRemainingTime(taskDetails.getTaskRemainingTime());
        taskItem.setProjectTaskType(taskDetails.getProjectTaskType());
        Date startDate = DateUtils.tryParse(taskDetails.getStartTime(), DateUtils.DATE_TIME_FORMAT);
        Date completionDate = DateUtils.getNextPreviousDate(DateTimePeriod.HOUR, startDate, taskDetails.getDuration());
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
        updateUserActivity(taskDetails, startDate, projectItem.getType(), projectItem.getUserGroupId());
    }

    public void reAssignTask(TaskDetails taskDetails, int newUserId, int userTaskActivityId, TaskStatus taskStatus)
    {
        TaskItem taskItem =
            (TaskItem) famstackDataAccessObjectManager.getItemById(taskDetails.getTaskId(), TaskItem.class);
        UserTaskActivityItem currentUserTaskActivityItem =
            famstackUserActivityManager.getUserTaskActivityItem(userTaskActivityId);
        Date startDate = DateUtils.tryParse(taskDetails.getStartTime(), DateUtils.DATE_TIME_FORMAT);
        int durationInMinutes = currentUserTaskActivityItem.getDurationInMinutes();
        if (taskStatus == TaskStatus.ASSIGNED) {
            famstackUserActivityManager.deleteAllUserTaskActivities(taskDetails.getTaskId());
        } else if (taskStatus == TaskStatus.INPROGRESS) {
            famstackUserActivityManager.setProjectTaskActivityActualTime(taskDetails.getTaskId(), new Date(),
                "re assigned", TaskStatus.COMPLETED, currentUserTaskActivityItem.getActualStartTime(), null);
            startDate = new Date();
            durationInMinutes -=
                (int) ((new Date().getTime() - currentUserTaskActivityItem.getActualStartTime().getTime()) / (1000 * 60));
        }

        famstackUserActivityManager.createUserActivityItem(newUserId, startDate, taskDetails.getTaskId(), taskDetails
            .getName(), durationInMinutes, currentUserTaskActivityItem.getType(), taskItem.getProjectItem().getType(),
            null);

        taskItem.setStatus(TaskStatus.ASSIGNED);
        taskItem.setAssignee(newUserId);
        famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);
    }

    public TaskActivityDetails playTask(int taskId, int taskActivityId)
    {
        TaskItem taskItem = (TaskItem) famstackDataAccessObjectManager.getItemById(taskId, TaskItem.class);
        UserTaskActivityItem userTaskActivityItem =
            famstackUserActivityManager.completeTaskActivityAndStartNewTaskActivity(taskActivityId, taskItem);

        return famstackUserActivityManager.mapUserTaskActivityItem(userTaskActivityItem);
    }

    public void pauseTask(TaskDetails taskDetails)
    {
        TaskItem taskItem =
            (TaskItem) famstackDataAccessObjectManager.getItemById(taskDetails.getTaskId(), TaskItem.class);
        taskItem.setTaskPausedTime(new Timestamp(new Date().getTime()));

        famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);
    }

    private void updateUserActivity(TaskDetails taskDetails, Date startDate, ProjectType projectType, String userGroupId)
    {

        logDebug("task assignee :" + taskDetails.getAssignee());

        deleteAllTaskActivitiesItem(taskDetails.getTaskId());

        if (taskDetails.getAssignee() > 0) {
            famstackUserActivityManager.createUserActivityItem(taskDetails.getAssignee(), startDate, taskDetails
                .getTaskId(), taskDetails.getName(), taskDetails.getDuration() * 60,
                taskDetails.getProjectTaskType() == ProjectTaskType.REVIEW ? UserTaskType.PROJECT_REVIEW
                    : UserTaskType.PROJECT, projectType, userGroupId);
        }

        logDebug("helpers :" + taskDetails.getHelper());
        if (taskDetails.getHelper() != null && taskDetails.getHelper().length > 0) {
            for (int helperId : taskDetails.getHelper()) {
                famstackUserActivityManager.createUserActivityItem(helperId, startDate, taskDetails.getTaskId(),
                    taskDetails.getName(), taskDetails.getDuration() * 60,
                    taskDetails.getProjectTaskType() == ProjectTaskType.REVIEW ? UserTaskType.PROJECT_HELPER_REVIEW
                        : UserTaskType.PROJECT_HELPER, projectType, userGroupId);
            }
        }
    }

    public void deleteTaskItem(int taskId)
    {
        TaskItem taskItem = getTaskItemById(taskId);
        if (taskItem != null) {
            deleteAllTaskActivitiesItem(taskId);
            famstackDataAccessObjectManager.deleteItem(taskItem);
        }
    }

    public void deleteAllTaskActivitiesItem(int taskId)
    {
        TaskItem taskItem = getTaskItemById(taskId);
        if (taskItem != null) {
            resetProjectProductiveAndBillableTime(taskItem);
            famstackUserActivityManager.deleteAllUserTaskActivities(taskItem.getTaskId());
        }
    }

    public void resetProjectProductiveAndBillableTime(TaskItem taskItem)
    {
        if (taskItem.getStatus() != TaskStatus.COMPLETED) {
            return;
        }
        List<?> userTaskActivityItems =
            famstackUserActivityManager.getUserTaskActivityItemByTaskId(taskItem.getTaskId());

        for (Object userTaskActivityItemObj : userTaskActivityItems) {
            UserTaskActivityItem userTaskActivityItem = (UserTaskActivityItem) userTaskActivityItemObj;

            UserActivityItem userActivityItem = userTaskActivityItem.getUserActivityItem();

            int durationInMinutes = userTaskActivityItem.getDurationInMinutes();
            if (userTaskActivityItem.getProjectType() == ProjectType.BILLABLE) {
                int billableMins = userActivityItem.getBillableMins();
                userActivityItem.setBillableMins(billableMins - durationInMinutes);
            } else {
                int nonBillableMins = userActivityItem.getNonBillableMins();
                userActivityItem.setNonBillableMins(nonBillableMins - durationInMinutes);
            }

            famstackDataAccessObjectManager.updateItem(userActivityItem);
        }

    }

    public TaskItem getTaskItemById(int taskId)
    {
        return (TaskItem) famstackDataAccessObjectManager.getItemById(taskId, TaskItem.class);
    }

    public TaskDetails getTaskDetailsById(int taskId)
    {
        return mapTask(getTaskItemById(taskId));
    }

    public Set<TaskDetails> mapProjectTaskDetails(Set<TaskItem> taskItems)
    {
        return mapProjectTaskDetails(taskItems, true);
    }

    public Set<TaskDetails> mapProjectTaskDetails(Set<TaskItem> taskItems, boolean isFullLoad)
    {
        Set<TaskDetails> taskDetails = new HashSet<TaskDetails>();
        if (taskItems != null) {
            for (TaskItem taskItem : taskItems) {
                TaskDetails taskDetail = mapTask(taskItem, isFullLoad);
                taskDetails.add(taskDetail);
            }
        }

        return taskDetails;
    }

    public TaskDetails mapTask(TaskItem taskItem)
    {
        return mapTask(taskItem, true);
    }

    public TaskDetails mapTask(TaskItem taskItem, boolean isFullLoad)
    {
        if (taskItem != null) {
            TaskDetails taskDetails = new TaskDetails();
            taskDetails.setAssignee(taskItem.getAssignee());
            taskDetails.setActualTimeTaken(taskItem.getActualTimeTaken());
            taskDetails.setTaskRemainingTime(taskItem.getTaskRemainingTime());
            taskDetails.setTaskId(taskItem.getTaskId());
            taskDetails.setName(taskItem.getName());
            taskDetails.setDuration(taskItem.getDuration());
            taskDetails.setTaskPausedTime(taskItem.getTaskPausedTime());
            String startDateString = DateUtils.format(taskItem.getStartTime(), DateUtils.DATE_TIME_FORMAT);
            String completionDateString = DateUtils.format(taskItem.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
            taskDetails.setStartTime(startDateString);
            taskDetails.setCompletionTime(completionDateString);
            taskDetails.setStatus(taskItem.getStatus());
            taskDetails.setProjectTaskType(taskItem.getProjectTaskType());
            if (isFullLoad) {
                taskDetails.setTaskActivityDetails(famstackUserActivityManager
                    .getUserTaskActivityDetailsByTaskId(taskItem.getTaskId()));
                taskDetails.setDescription(taskItem.getDescription());
                taskDetails.setExtraTimeTask(taskItem.getExtraTimeTask());
                taskDetails.setPriority(taskItem.getPriority());

                if (taskItem.getReporter() != null) {
                    taskDetails.setReporterName(taskItem.getReporter().getFirstName() + " "
                        + taskItem.getReporter().getLastName());
                }
                taskDetails.setCreatedDate(taskItem.getCreatedDate());
                taskDetails.setLastModifiedDate(taskItem.getLastModifiedDate());
                taskDetails.setProjectId(taskItem.getProjectItem().getProjectId());
                taskDetails.setHelpersList(taskItem.getHelpers());
                taskDetails.setContributers(taskItem.getContributers());
            }
            return taskDetails;

        }
        return null;
    }

    public String getUserTaskActivityJson()
    {
        return FamstackUtils.getJsonFromObject(famstackUserActivityManager.getAllTaskActivities());
    }

    public String getUserTaskActivityJson(String startDate, String endDate)
    {
        JSONArray jsonArray = new JSONArray();
        for (TaskActivityDetails taskActivityDetails : famstackUserActivityManager.getAllTaskActivities(startDate,
            endDate)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", taskActivityDetails.getTaskName());
            jsonObject.put("start",
                DateUtils.format(taskActivityDetails.getStartTime(), DateUtils.DATE_TIME_FORMAT_CALENDER));

            Date completionDate =
                DateUtils.getNextPreviousDate(DateTimePeriod.HOUR, taskActivityDetails.getStartTime(),
                    taskActivityDetails.getDurationInMinutes() / 60);

            jsonObject.put("end", DateUtils.format(completionDate, DateUtils.DATE_TIME_FORMAT_CALENDER));
            jsonObject.put("tip", taskActivityDetails.getUserTaskType());
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

    public Map<String, List<TaskDetails>> getAllProjectTask(Integer userId)
    {
        Map<Integer, TaskItem> taskItemMap = new HashMap<>();
        Map<String, List<TaskDetails>> userTaskActivityItemMap = new HashMap<>();

        userTaskActivityItemMap.put(TaskStatus.ASSIGNED.value(), new ArrayList<TaskDetails>());
        userTaskActivityItemMap.put(TaskStatus.INPROGRESS.value(), new ArrayList<TaskDetails>());
        userTaskActivityItemMap.put(TaskStatus.COMPLETED.value(), new ArrayList<TaskDetails>());

        Map<String, Object> dataMap = new HashMap<>();

        String queryTobeExecuted = "getAllUserActivities";
        if (userId != null) {
            dataMap.put("userId", userId);
            queryTobeExecuted = "getUserActivities";
        }
        dataMap.put("startTime", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), 0));

        List<?> userActivityItemlist =
            getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString(queryTobeExecuted), dataMap);

        logDebug("userActivityItemlist : " + userActivityItemlist);

        if (!userActivityItemlist.isEmpty()) {
            for (Object userActivityItemObj : userActivityItemlist) {
                UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;
                logDebug("userProjectTasklist : " + userActivityItem.getUserTaskActivities());

                if (!userActivityItem.getUserTaskActivities().isEmpty()) {
                    for (UserTaskActivityItem userTaskActivityItem : userActivityItem.getUserTaskActivities()) {

                        int taskId = userTaskActivityItem.getTaskId();
                        if (taskId == 0) {
                            continue;
                        }
                        TaskItem taskItem = taskItemMap.get(taskId);
                        if (taskItem == null) {
                            taskItem = getTaskItemById(taskId);
                            taskItemMap.put(taskId, taskItem);
                        }

                        if (userTaskActivityItem.getActualEndTime() != null
                            && taskItem.getStatus() != TaskStatus.COMPLETED) {
                            continue;
                        }
                        TaskActivityDetails taskActivityDetails =
                            famstackUserActivityManager.mapUserTaskActivityItem(userTaskActivityItem);
                        TaskDetails taskDetails = mapTask(taskItem);
                        List<TaskActivityDetails> taskActivityDetailList = new ArrayList<>();
                        taskActivityDetailList.add(taskActivityDetails);

                        taskDetails.setTaskActivityDetails(taskActivityDetailList);
                        if (taskItem.getStatus() == TaskStatus.ASSIGNED
                            || taskItem.getStatus() == TaskStatus.INPROGRESS
                            || taskItem.getStatus() == TaskStatus.COMPLETED) {
                            userTaskActivityItemMap.get(taskItem.getStatus().value()).add(taskDetails);
                        }
                        if (taskItem.getStatus() == TaskStatus.ASSIGNED) {
                            checkTaskIsDisabled(taskItem, taskDetails);
                        }

                    }
                }
            }
        }

        return userTaskActivityItemMap;
    }

    public List<TaskDetails> getBaklogProjectTasks(Integer userId)
    {
        Map<Integer, TaskItem> taskItemMap = new HashMap<>();
        List<TaskDetails> userTaskActivityList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        String queryTobeExecuted = "getAllBackLogUserActivities";
        if (userId != null) {
            dataMap.put("userId", userId);
            queryTobeExecuted = "getBackLogUserActivities";
        }
        dataMap.put("startTime", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), -15));
        dataMap.put("endTime", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, new Date(), -1));

        List<?> userActivityItemlist =
            getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString(queryTobeExecuted), dataMap);

        logDebug("userActivityItemlist : " + userActivityItemlist);

        if (!userActivityItemlist.isEmpty()) {
            for (Object userActivityItemObj : userActivityItemlist) {
                UserActivityItem userActivityItem = (UserActivityItem) userActivityItemObj;

                logDebug("userProjectTasklist : " + userActivityItem.getUserTaskActivities());

                if (userActivityItem.getUserTaskActivities() != null) {
                    for (UserTaskActivityItem userTaskActivityItem : userActivityItem.getUserTaskActivities()) {

                        if (userTaskActivityItem.getActualEndTime() == null) {
                            int taskId = userTaskActivityItem.getTaskId();
                            if (taskId == 0) {
                                continue;
                            }
                            TaskItem taskItem = taskItemMap.get(taskId);
                            if (taskItem == null) {
                                taskItem = getTaskItemById(taskId);
                                taskItemMap.put(taskId, taskItem);
                            }

                            if (taskItem.getStatus() != TaskStatus.CLOSED
                                && taskItem.getStatus() != TaskStatus.COMPLETED) {
                                TaskActivityDetails taskActivityDetails =
                                    famstackUserActivityManager.mapUserTaskActivityItem(userTaskActivityItem);
                                TaskDetails taskDetails = mapTask(taskItem);

                                List<TaskActivityDetails> taskActivityDetailList = new ArrayList<>();
                                taskActivityDetailList.add(taskActivityDetails);
                                taskDetails.setTaskActivityDetails(taskActivityDetailList);
                                userTaskActivityList.add(taskDetails);
                            }
                        }

                    }
                }
            }
        }
        return userTaskActivityList;
    }

    private void checkTaskIsDisabled(TaskItem taskItem, TaskDetails taskDetails)
    {
        if (taskItem.getProjectTaskType() == ProjectTaskType.REVIEW) {
            ProjectItem projectItem = taskItem.getProjectItem();
            Set<TaskItem> taskItems = projectItem.getTaskItems();

            for (TaskItem tItem : taskItems) {
                if (taskItem.getTaskId() != tItem.getTaskId() && tItem.getStatus() != TaskStatus.COMPLETED
                    && projectItem.getStatus() != ProjectStatus.UNASSIGNED) {
                    taskDetails.setDisableTask(true);
                }
            }
        }
    }

    public TaskItem updateTaskStatus(int taskId, TaskStatus taskStatus, String comments, Date adjustStartTime,
        Date adjustCompletionTimeDate)
    {
        TaskItem taskItem = getTaskItemById(taskId);

        if (taskItem != null) {
            taskItem.setStatus(taskStatus);
            // TODO: change user activity date on start.
            Map<String, Object> taskActivities =
                famstackUserActivityManager.setProjectTaskActivityActualTime(taskId, new Date(), comments, taskStatus,
                    adjustStartTime, adjustCompletionTimeDate);

            Integer actualTimeTaken = (Integer) taskActivities.get(TASK_ACTUAL_DURATION);
            List<Integer> contributersList = (List<Integer>) taskActivities.get(TASK_CONTRIBUTERS);

            taskItem.setActualTimeTaken(actualTimeTaken);
            taskItem.setContributers(contributersList.toString().replace("[", "").replaceAll("]", "")
                .replace("null", ""));

            if (taskStatus == TaskStatus.COMPLETED) {
                calculateBillableAndNonBillableTime(taskId);
            }
        }

        getFamstackDataAccessObjectManager().updateItem(taskItem);
        return taskItem;
    }

    private void calculateBillableAndNonBillableTime(int taskId)
    {
        List<UserTaskActivityItem> userTaskActivityItems =
            (List<UserTaskActivityItem>) famstackUserActivityManager.getUserTaskActivityItemByTaskId(taskId);
        if (userTaskActivityItems != null) {
            for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                UserActivityItem userActivityItem = userTaskActivityItem.getUserActivityItem();

                if (userTaskActivityItem.getProjectType() == ProjectType.BILLABLE) {
                    userActivityItem.setBillableMins(userActivityItem.getBillableMins()
                        + userTaskActivityItem.getDurationInMinutes());
                } else {
                    userActivityItem.setNonBillableMins(userActivityItem.getNonBillableMins()
                        + userTaskActivityItem.getDurationInMinutes());
                }

                famstackDataAccessObjectManager.saveOrUpdateItem(userActivityItem);
            }
        }

    }

    public List<TaskDetails> getAllTaskStartWithin(Date startDate, Date startDate2)
    {

        Map<String, Object> dataMap = new HashMap<>();
        List<TaskDetails> taskDetailsList = new ArrayList<>();
        dataMap.put("startTime", startDate);
        dataMap.put("startTime2", startDate2);

        List<?> projectTaskList =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(
                HQLStrings.getString("getAllProjectTaskStartWithIn"), dataMap);

        for (Object taskItemObj : projectTaskList) {
            TaskItem taskItem = (TaskItem) taskItemObj;
            TaskDetails taskDetails = mapTask(taskItem);
            taskDetailsList.add(taskDetails);
        }
        return taskDetailsList;

    }

    public List<TaskDetails> getProjectDetailsTaskDetailsByProjectId(int projectId)
    {

        Map<String, Object> dataMap = new HashMap<>();
        List<TaskDetails> taskDetailsList = new ArrayList<>();
        dataMap.put("projectId", projectId);

        List<?> projectTaskList =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(
                HQLStrings.getString("getAllProjectTaskByProjectId"), dataMap);

        for (Object taskItemObj : projectTaskList) {
            TaskItem taskItem = (TaskItem) taskItemObj;
            TaskDetails taskDetails = mapTask(taskItem, false);
            taskDetailsList.add(taskDetails);
        }
        return taskDetailsList;

    }

    public List<TaskDetails> getAllTaskEndWithin(Date endDate, Date endDate2)
    {

        Map<String, Object> dataMap = new HashMap<>();
        List<TaskDetails> taskDetailsList = new ArrayList<>();
        dataMap.put("endDate", endDate);
        dataMap.put("endDate2", endDate2);

        List<?> projectTaskList =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(
                HQLStrings.getString("getAllProjectTaskEndWithIn"), dataMap);

        for (Object taskItemObj : projectTaskList) {
            TaskItem taskItem = (TaskItem) taskItemObj;
            TaskDetails taskDetails = mapTask(taskItem);
            taskDetailsList.add(taskDetails);
        }
        return taskDetailsList;

    }

    public List<TaskDetails> getAllProjectTaskMissedDeadLine(Date startTime)
    {

        Map<String, Object> dataMap = new HashMap<>();
        List<TaskDetails> taskDetailsList = new ArrayList<>();
        dataMap.put("currentTime", new Date());
        dataMap.put("startTime", startTime);

        List<?> projectTaskList =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(
                HQLStrings.getString("getAllProjectTaskMissedDeadLine"), dataMap);

        for (Object taskItemObj : projectTaskList) {
            TaskItem taskItem = (TaskItem) taskItemObj;
            TaskDetails taskDetails = mapTask(taskItem);
            taskDetailsList.add(taskDetails);
        }
        return taskDetailsList;

    }

    public void updateTaskRemaingTime()
    {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskStatus", TaskStatus.INPROGRESS);

        List<?> projectTaskList =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(
                HQLStrings.getString("getAllProjectInprogressTask"), dataMap);

        if (projectTaskList != null) {
            for (Object taskItemObj : projectTaskList) {
                try {
                    TaskItem taskItem = (TaskItem) taskItemObj;
                    int diffInMinute = 0;
                    List<UserTaskActivityItem> userTaskActivityItems =
                        (List<UserTaskActivityItem>) famstackUserActivityManager
                            .getUserTaskActivityItemByTaskId(taskItem.getTaskId());

                    if (userTaskActivityItems != null) {
                        Date actualStartTime = null;

                        for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                            if (userTaskActivityItem.getActualEndTime() == null) {
                                actualStartTime = userTaskActivityItem.getActualStartTime();
                                diffInMinute = userTaskActivityItem.getDurationInMinutes();
                                break;
                            }
                        }
                        if (actualStartTime != null) {
                            double diff = new Date().getTime() - actualStartTime.getTime();
                            diff = (diffInMinute * 60 * 1000) - diff;
                            diffInMinute = (int) (diff / (60 * 1000));
                        }
                    }

                    taskItem.setTaskRemainingTime(diffInMinute);
                    famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);

                } catch (Exception e) {

                }
            }
        }

    }

    public int adjustTaskActivityTime(int taskActivityItemId, int taskId, int newDuration, String startTime,
        String endTime)
    {
        int actualTaskDuration = 0;
        famstackUserActivityManager.adjustTaskActivityTime(taskActivityItemId, newDuration, startTime, endTime);
        List<TaskActivityDetails> userTaskActivities =
            famstackUserActivityManager.getUserTaskActivityDetailsByTaskId(taskId);

        if (userTaskActivities != null) {
            actualTaskDuration = recalculateTaskActualDuration(userTaskActivities);
            adjustTaskTime(taskId, actualTaskDuration);
        }

        return actualTaskDuration;

    }

    private int recalculateTaskActualDuration(List<TaskActivityDetails> userTaskActivities)
    {
        int actualTaskDuration = 0;
        for (TaskActivityDetails taskActivityDetail : userTaskActivities) {
            actualTaskDuration += taskActivityDetail.getDurationInMinutes();
        }
        return actualTaskDuration;
    }

    public void adjustTaskTime(int taskId, int newDuration)
    {
        TaskItem taskItem = getTaskItemById(taskId);

        if (taskItem != null) {
            taskItem.setActualTimeTaken(newDuration);
            famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);
        }

    }

}
