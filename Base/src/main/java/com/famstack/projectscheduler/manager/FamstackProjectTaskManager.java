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

    @Resource
    FamstackProjectActivityManager famstackProjectActivityManager;

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    public void createTaskItem(TaskDetails taskDetails, ProjectItem projectItem)
    {
        TaskItem taskItem = new TaskItem();

        taskItem.setStatus(TaskStatus.NEW);
        taskItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());

        saveTask(taskDetails, projectItem, taskItem);
        famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.TASK_ADDED,
            taskItem.getName());
    }

    public void createExtraTaskItem(TaskDetails taskDetails, ProjectItem projectItem)
    {
        Set<TaskItem> taskItems = projectItem.getTaskItems();
        UserTaskActivityItem userTaskActivityItemOld = null;
        int durationNew = taskDetails.getDuration();

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
            taskItemNew.setPriority(ProjectPriority.HIGHT);
            taskItemNew.setProjectItem(projectItem);
            taskItemNew.setReviewTask(false);
            taskItemNew.setExtraTimeTask(true);
            taskItemNew.setDuration(taskDetails.getDuration());
            taskItemNew.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
            taskItemNew.setAssignee(taskDetails.getAssignee());
        } else {
            String helpers = "" + taskDetails.getAssignee();
            if (taskItemNew.getHelpers() != null) {
                helpers += "," + taskDetails.getAssignee();
            }
            taskItemNew.setHelpers(helpers);

            int duration = taskItemNew.getDuration() + taskDetails.getDuration();
            List<UserTaskActivityItem> userTaskActivityItems =
                (List<UserTaskActivityItem>) famstackUserActivityManager.getUserTaskActivityItemByTaskId(taskItemNew
                    .getTaskId());
            if (userTaskActivityItems != null) {
                for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                    if (userTaskActivityItem.getUserActivityItem().getUserItem().getId() == taskDetails.getAssignee()) {
                        userTaskActivityItemOld = userTaskActivityItem;
                        duration -= (userTaskActivityItemOld.getDurationInMinutes() / 60);
                        break;
                    }
                }
            }

            taskItemNew.setDuration(duration);
        }

        famstackDataAccessObjectManager.saveOrUpdateItem(taskItemNew);

        if (userTaskActivityItemOld == null) {
            famstackUserActivityManager.createCompletedUserActivityItem(taskDetails.getAssignee(),
                projectItem.getStartTime(), taskItemNew.getTaskId(), taskItemNew.getName(), durationNew,
                UserTaskType.EXTRATIME, projectItem.getType(), taskDetails.getDescription());
        } else {
            userTaskActivityItemOld.setDurationInMinutes(durationNew * 60);
            userTaskActivityItemOld.setTaskName(taskDetails.getName());
            userTaskActivityItemOld.setTaskName(taskDetails.getName());
            userTaskActivityItemOld.setCompletionComment(taskDetails.getDescription());
            famstackDataAccessObjectManager.saveOrUpdateItem(userTaskActivityItemOld);
        }

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
        taskItem.setReviewTask(taskDetails.getReviewTask());
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
        updateUserActivity(taskDetails, startDate, projectItem.getType());
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
            durationInMinutes =
                (int) ((new Date().getTime() - currentUserTaskActivityItem.getActualStartTime().getTime()) / (1000 * 60));
        }

        famstackUserActivityManager.createUserActivityItem(newUserId, startDate, taskDetails.getTaskId(), taskDetails
            .getName(), durationInMinutes, currentUserTaskActivityItem.getType(), taskItem.getProjectItem().getType());

        taskItem.setStatus(TaskStatus.ASSIGNED);
        taskItem.setAssignee(newUserId);
        famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);
    }

    private void updateUserActivity(TaskDetails taskDetails, Date startDate, ProjectType projectType)
    {

        logDebug("task assignee :" + taskDetails.getAssignee());

        famstackUserActivityManager.deleteAllUserTaskActivities(taskDetails.getTaskId());

        if (taskDetails.getAssignee() > 0) {
            famstackUserActivityManager.createUserActivityItem(taskDetails.getAssignee(), startDate,
                taskDetails.getTaskId(), taskDetails.getName(), taskDetails.getDuration() * 60,
                taskDetails.getReviewTask() ? UserTaskType.PROJECT_REVIEW : UserTaskType.PROJECT, projectType);
        }

        logDebug("helpers :" + taskDetails.getHelper());
        if (taskDetails.getHelper() != null && taskDetails.getHelper().length > 0) {
            for (int helperId : taskDetails.getHelper()) {
                famstackUserActivityManager.createUserActivityItem(helperId, startDate, taskDetails.getTaskId(),
                    taskDetails.getName(), taskDetails.getDuration() * 60, taskDetails.getReviewTask()
                        ? UserTaskType.PROJECT_HELPER_REVIEW : UserTaskType.PROJECT_HELPER, projectType);
            }
        }
    }

    public void deleteTaskItem(int taskId)
    {
        TaskItem taskItem = getTaskItemById(taskId);
        if (taskItem != null) {
            deleteAllTaskActivitiesItem(taskItem.getTaskId());
            famstackDataAccessObjectManager.deleteItem(taskItem);
        }
    }

    public void deleteAllTaskActivitiesItem(int taskId)
    {
        resetProjectProductiveAndBillableTime(taskId);
        famstackUserActivityManager.deleteAllUserTaskActivities(taskId);
    }

    public void resetProjectProductiveAndBillableTime(int taskId)
    {
        List<?> userTaskActivityItems = famstackUserActivityManager.getUserTaskActivityItemByTaskId(taskId);

        for (Object userTaskActivityItemObj : userTaskActivityItems) {
            UserTaskActivityItem userTaskActivityItem = (UserTaskActivityItem) userTaskActivityItemObj;

            UserActivityItem userActivityItem = userTaskActivityItem.getUserActivityItem();

            int durationInMinutes = userTaskActivityItem.getDurationInMinutes();
            if (userTaskActivityItem.getProjectType() == ProjectType.BILLABLE) {
                int billableHours = userActivityItem.getBillableHours();
                userActivityItem.setBillableHours(billableHours - durationInMinutes / 60);
            }
            int productiveHours = userActivityItem.getProductiveHousrs();
            userActivityItem.setProductiveHousrs(productiveHours - durationInMinutes / 60);

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
            taskDetails.setTaskId(taskItem.getTaskId());
            taskDetails.setName(taskItem.getName());
            taskDetails.setDuration(taskItem.getDuration());
            String startDateString = DateUtils.format(taskItem.getStartTime(), DateUtils.DATE_TIME_FORMAT);
            String completionDateString = DateUtils.format(taskItem.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
            taskDetails.setStartTime(startDateString);
            taskDetails.setCompletionTime(completionDateString);
            taskDetails.setStatus(taskItem.getStatus());
            taskDetails.setTaskActivityDetails(famstackUserActivityManager.getUserTaskActivityDetailsByTaskId(taskItem
                .getTaskId()));
            if (isFullLoad) {
                taskDetails.setDescription(taskItem.getDescription());
                taskDetails.setReviewTask(taskItem.getReviewTask());
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
        if (taskItem.getReviewTask()) {
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
            famstackUserActivityManager.setProjectTaskActivityActualTime(taskId, new Date(), comments, taskStatus,
                adjustStartTime, adjustCompletionTimeDate);
        }

        getFamstackDataAccessObjectManager().updateItem(taskItem);
        return taskItem;
    }

    public List<TaskDetails> getAllTaskStartWithin(Date startDate, Date startDate2)
    {

        Map<String, Object> dataMap = new HashMap<>();
        List<TaskDetails> taskDetailsList = new ArrayList<>();
        dataMap.put("startTime", startDate);
        dataMap.put("startTime2", startDate2);

        List<?> projectTaskList =
            getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString("getAllProjectTaskStartWithIn"),
                dataMap);

        for (Object taskItemObj : projectTaskList) {
            TaskItem taskItem = (TaskItem) taskItemObj;
            TaskDetails taskDetails = mapTask(taskItem);
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
            getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString("getAllProjectTaskEndWithIn"),
                dataMap);

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
            getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString("getAllProjectTaskMissedDeadLine"),
                dataMap);

        for (Object taskItemObj : projectTaskList) {
            TaskItem taskItem = (TaskItem) taskItemObj;
            TaskDetails taskDetails = mapTask(taskItem);
            taskDetailsList.add(taskDetails);
        }
        return taskDetailsList;

    }

    public static void main(String[] args)
    {
        System.out.println(UserTaskType.PROJECT_HELPER_REVIEW.toString());
    }
}
