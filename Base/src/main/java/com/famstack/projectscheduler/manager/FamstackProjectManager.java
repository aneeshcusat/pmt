package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectTaskType;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.dashboard.bean.ClientProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectCategoryDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectStatusDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.bean.TeamUtilizatioDetails;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.RecurringProjectItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.RecurringProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.notification.FamstackNotificationServiceManager;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;
import com.famstack.projectscheduler.util.TimeInType;
import com.famstack.projectscheduler.utils.FamstackUtils;

/**
 * The Class FamstackProjectManager.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */
@Component
public class FamstackProjectManager extends BaseFamstackManager
{

    @Resource
    FamstackProjectCommentManager famstackProjectCommentManager;

    @Resource
    FamstackProjectActivityManager famstackProjectActivityManager;

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    @Resource
    FamstackProjectTaskManager famstackProjectTaskManager;

    @Resource
    FamstackNotificationServiceManager famstackNotificationServiceManager;

    public int createProjectItem(ProjectDetails projectDetails)
    {
        ProjectItem projectItem = new ProjectItem();
        projectItem.setStatus(ProjectStatus.NEW);
        int projectId = saveProjectItem(projectDetails, projectItem);
        famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED, null);
        return projectId;
    }

    public void quickDuplicateProject(int projectId, String projectName, Integer projectDuration,
        String projectStartTime, String projectEndTime, String taskDetailsList)
    {
        ProjectItem projectDetails = getProjectItemById(projectId);
        List<TaskDetails> allTaskDetailsList = new ArrayList<>();

        Date startDate = DateUtils.tryParse(projectStartTime, DateUtils.DATE_TIME_FORMAT);
        Date completionDate = DateUtils.tryParse(projectEndTime, DateUtils.DATE_TIME_FORMAT);
        Timestamp startTimeStamp = null;
        if (startDate != null) {
            startTimeStamp = new Timestamp(startDate.getTime());
        }

        Timestamp completionTimeStamp = null;
        if (completionDate != null) {
            completionTimeStamp = new Timestamp(completionDate.getTime());
        }

        if (taskDetailsList != null && !taskDetailsList.isEmpty()) {
            String[] taskList = taskDetailsList.split("#TD#");

            if (taskList.length > 0) {
                for (String taskData : taskList) {
                    String[] task = taskData.split("#TDD#");
                    if (task.length > 0) {
                        TaskDetails taskDetails = new TaskDetails();
                        taskDetails.setAssignee(Integer.parseInt(task[0]));
                        taskDetails.setName(task[1]);
                        taskDetails.setCanRecure(true);
                        taskDetails.setStartTime(task[2]);
                        taskDetails.setDuration(Integer.parseInt(task[3]));
                        taskDetails.setProjectTaskType(ProjectTaskType.valueOf(task[4]));
                        allTaskDetailsList.add(taskDetails);
                    }
                }
            }
        }

        quickDuplicateProject(projectDetails, projectName, projectDuration, startTimeStamp, completionTimeStamp,
            allTaskDetailsList);
    }

    public void quickDuplicateProject(ProjectItem projectDetails, String projectName, Integer projectDuration,
        Timestamp startTimeStamp, Timestamp completionTimeStamp, List<TaskDetails> taskItemList)
    {
        ProjectItem projectItem = new ProjectItem();

        if (projectDetails != null) {
            projectItem.setStatus(ProjectStatus.NEW);
            projectItem.setCategory(projectDetails.getCategory());
            projectItem.setCode(projectDetails.getCode());
            projectItem.setDescription(projectDetails.getDescription());
            projectItem.setName(projectName);
            projectItem.setQuantity(projectDetails.getQuantity());
            projectItem.setPriority(projectDetails.getPriority());
            projectItem.setProjectSubType(projectDetails.getProjectSubType());
            projectItem.setProjectLead(projectDetails.getProjectLead());

            projectItem.setPONumber(projectDetails.getPONumber());
            projectItem.setComplexity(projectDetails.getComplexity());

            projectItem.setStartTime(startTimeStamp);
            projectItem.setCompletionTime(completionTimeStamp);
            projectItem.setDurationHrs(projectDuration);

            projectItem.setAccountId(projectDetails.getAccountId());
            projectItem.setTeamId(projectDetails.getTeamId());
            projectItem.setClientId(projectDetails.getClientId());
            projectItem.setTags(projectDetails.getTags());
            projectItem.setType(projectDetails.getType());

            projectItem.setUserGroupId(projectDetails.getUserGroupId());

            projectItem.setReporter(projectDetails.getReporter());

            projectItem.setWatchers(projectDetails.getWatchers());
            famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED,
                "Duplicated from " + projectDetails.getProjectId());

            for (TaskDetails taskDetails : taskItemList) {
                famstackProjectTaskManager.createTaskItem(taskDetails, projectItem, true);
            }

            updateProjectStatusBasedOnTaskStatus(projectItem.getProjectId());
            notifyProjectTaskAssignment(mapProjectItemToProjectDetails(projectItem), taskItemList);

        }

    }

    private void notifyProjectTaskAssignment(ProjectDetails projectDetails, List<TaskDetails> allTaskDetailsList)
    {
        if (projectDetails != null) {
            for (TaskDetails taskDetails : allTaskDetailsList) {

                Map<String, Object> projectTaskMap = new HashMap<>();
                projectTaskMap.put("taskDetails", taskDetails);
                projectTaskMap.put("projectDetails", projectDetails);
                famstackNotificationServiceManager.notifyAll(NotificationType.TASK_ASSIGNED, projectTaskMap, null);
            }
        }
    }

    private int saveProjectItem(ProjectDetails projectDetails, ProjectItem projectItem)
    {
        projectItem.setCategory(projectDetails.getCategory());
        projectItem.setCode(projectDetails.getCode());
        projectItem.setDescription(projectDetails.getDescription());
        projectItem.setName(projectDetails.getName());
        projectItem.setQuantity(projectDetails.getQuantity());
        projectItem.setPriority(projectDetails.getPriority());
        projectItem.setProjectSubType(projectDetails.getProjectSubType());
        projectItem.setProjectLead(projectDetails.getProjectLead());

        projectItem.setPONumber(projectDetails.getPONumber());
        projectItem.setComplexity(projectDetails.getComplexity());
        Date startDate = DateUtils.tryParse(projectDetails.getStartTime(), DateUtils.DATE_TIME_FORMAT);
        Date completionDate = DateUtils.tryParse(projectDetails.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
        Timestamp startTimeStamp = null;
        if (startDate != null) {
            startTimeStamp = new Timestamp(startDate.getTime());
        }

        Timestamp completionTimeStamp = null;
        if (completionDate != null) {
            completionTimeStamp = new Timestamp(completionDate.getTime());
        }

        projectItem.setStartTime(startTimeStamp);
        projectItem.setCompletionTime(completionTimeStamp);
        projectItem.setDurationHrs(projectDetails.getDurationHrs());

        projectItem.setAccountId(projectDetails.getAccountId());
        projectItem.setTeamId(projectDetails.getTeamId());
        projectItem.setClientId(projectDetails.getClientId());
        projectItem.setTags(projectDetails.getTags());
        projectItem.setType(projectDetails.getType());
        projectItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
        projectItem.setWatchers(projectDetails.getWatchers());
        projectItem = (ProjectItem) famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
        return projectItem.getProjectId();
    }

    public void deleteProjectItem(int projectId)
    {
        ProjectItem projectItem =
            (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
        if (projectItem != null) {
            Set<TaskItem> taskItems = projectItem.getTaskItems();
            if (!taskItems.isEmpty()) {
                for (TaskItem taskItem : taskItems) {
                    famstackProjectTaskManager.deleteAllTaskActivitiesItem(taskItem.getTaskId());
                }
            }
            famstackDataAccessObjectManager.deleteItem(projectItem);
        }
    }

    public void archiveProjectItem(int projectId)
    {
        ProjectItem projectItem =
            (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
        if (projectItem != null) {
            projectItem.setDeleted(true);
            famstackDataAccessObjectManager.updateItem(projectItem);
        }

    }

    public ProjectItem getProjectItemById(int projectId)
    {
        return (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
    }

    public ProjectItem getLatestProjectByCode(String projectCode)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectCode", projectCode);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("getProjectTobeRecurring"),
                dataMap);

        if (projectItemList.size() > 0) {
            return (ProjectItem) projectItemList.get(0);
        }
        return null;
    }

    public void createProjectTask(TaskDetails taskDetails)
    {
        ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
        famstackProjectTaskManager.createTaskItem(taskDetails, projectItem);
        updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
    }

    public void createProjectExtraTimeTask(TaskDetails taskDetails)
    {
        ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
        famstackProjectTaskManager.createExtraTaskItem(taskDetails, projectItem);
    }

    public void reAssignTask(TaskDetails taskDetails, int newUserId, int taskActivityId, TaskStatus taskStatus)
    {
        famstackProjectTaskManager.reAssignTask(taskDetails, newUserId, taskActivityId, taskStatus);
    }

    public TaskActivityDetails playTask(int taskId, int taskActivityId)
    {
        return famstackProjectTaskManager.playTask(taskId, taskActivityId);

    }

    public void pauseTask(TaskDetails taskDetails)
    {
        famstackProjectTaskManager.pauseTask(taskDetails);
    }

    private void updateProjectStatusBasedOnTaskStatus(int projectId)
    {
        ProjectItem projectItem;
        projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
        ProjectStatus projectStatus = getProjectStatus(projectItem);
        updateProjectStatus(projectStatus, projectItem);
    }

    public void updateProjectTask(TaskDetails taskDetails)
    {
        ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
        famstackProjectTaskManager.updateTask(taskDetails, projectItem);
        updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
    }

    public void updateProjectStatus(ProjectStatus projectStatus, ProjectItem projectItem)
    {
        projectItem.setStatus(projectStatus);
        getFamstackDataAccessObjectManager().updateItem(projectItem);

    }

    public ProjectStatus getProjectStatus(ProjectItem projectItem)
    {

        List<TaskStatus> taskStatsList = new ArrayList<>();
        if (projectItem != null) {
            for (TaskItem taskItem : projectItem.getTaskItems()) {
                TaskStatus taskStatus = taskItem.getStatus();
                taskStatsList.add(taskStatus);
            }
        }
        if (taskStatsList.isEmpty()) {
            return ProjectStatus.NEW;
        } else if (taskStatsList.contains(TaskStatus.NEW)) {
            return ProjectStatus.UNASSIGNED;
        } else if (taskStatsList.contains(TaskStatus.ASSIGNED)
            && (taskStatsList.contains(TaskStatus.INPROGRESS) || taskStatsList.contains(TaskStatus.COMPLETED))) {
            return ProjectStatus.INPROGRESS;
        } else if (taskStatsList.contains(TaskStatus.ASSIGNED)) {
            return ProjectStatus.ASSIGNED;
        } else if (taskStatsList.contains(TaskStatus.INPROGRESS)) {
            return ProjectStatus.INPROGRESS;
        } else if (taskStatsList.contains(TaskStatus.COMPLETED)) {
            return ProjectStatus.COMPLETED;
        }
        return ProjectStatus.NEW;
    }

    public void deleteProjectTask(int taskId, int projectId)
    {
        famstackProjectTaskManager.deleteTaskItem(taskId);
        updateProjectStatusBasedOnTaskStatus(projectId);
    }

    public String getUserTaskActivityJson(Integer userId, int dayfilter)
    {
        return famstackProjectTaskManager.getUserTaskActivityJson(userId, dayfilter);
    }

    public List<TaskActivityDetails> getUserTaskActivity(Integer userId, int dayfilter)
    {
        return famstackProjectTaskManager.getUserTaskActivity(userId, dayfilter);
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem)
    {
        return mapProjectItemToProjectDetails(projectItem, true);
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem, boolean isFullLoad)
    {
        return mapProjectItemToProjectDetails(projectItem, isFullLoad, false);
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem, boolean isFullLoad,
        boolean includeDeleted)
    {
        if (projectItem != null) {
            if (!includeDeleted && projectItem.getDeleted() == true) {
                return null;
            }
            ProjectDetails projectDetails = new ProjectDetails();
            projectDetails.setCode(projectItem.getCode());
            projectDetails.setId(projectItem.getProjectId());
            projectDetails.setName(projectItem.getName());
            projectDetails.setDeleted(projectItem.getDeleted());
            projectDetails.setProjectTaskDeatils(famstackProjectTaskManager.mapProjectTaskDetails(
                projectItem.getTaskItems(), isFullLoad));
            projectDetails.setDurationHrs(projectItem.getDurationHrs());
            int unAssignedDuration = getUnAssignedDuration(projectDetails);
            projectDetails.setUnAssignedDuration(unAssignedDuration);
            String startDateString = DateUtils.format(projectItem.getStartTime(), DateUtils.DATE_TIME_FORMAT);
            String completionDateString = DateUtils.format(projectItem.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
            projectDetails.setStartTime(startDateString);
            projectDetails.setCompletionTime(completionDateString);
            projectDetails.setStatus(projectItem.getStatus());
            projectDetails.setCreatedDate(projectItem.getCreatedDate());
            projectDetails.setProjectSubType(projectItem.getProjectSubType());
            projectDetails.setProjectLead(projectItem.getProjectLead());
            projectDetails.setClientId(projectItem.getClientId());
            projectDetails.setAccountId(projectItem.getAccountId());
            projectDetails.setTeamId(projectItem.getTeamId());
            projectDetails.setDescription(projectItem.getDescription());
            projectDetails.setCategory(projectItem.getCategory());
            if (isFullLoad) {
                projectDetails.setCategory(projectItem.getCategory());
                projectDetails.setQuantity(projectItem.getQuantity());
                projectDetails.setPriority(projectItem.getPriority());
                projectDetails.setComplexity(projectItem.getComplexity());
                projectDetails.setPONumber(projectItem.getPONumber());

                projectDetails.setTags(projectItem.getTags());
                projectDetails.setType(projectItem.getType());
                if (projectItem.getReporter() != null) {
                    projectDetails.setReporterName(projectItem.getReporter().getFirstName() + " "
                        + projectItem.getReporter().getLastName());
                }
                projectDetails.setEmployeeDetails(famstackUserProfileManager.mapEmployeeDetails(projectItem
                    .getReporter()));
                projectDetails.setWatchers(projectItem.getWatchers());
                projectDetails.setLastModifiedDate(projectItem.getLastModifiedDate());

                projectDetails.setProjectComments(famstackProjectCommentManager.mapProjectCommentDetails(projectItem
                    .getProjectComments()));
                projectDetails.setProjectActivityItem(famstackProjectActivityManager
                    .mapProjectActivityDetails(projectItem.getProjectActivityItem()));
            }
            return projectDetails;

        }
        return null;
    }

    private int getUnAssignedDuration(ProjectDetails projectDetails)
    {
        Integer unassignedDuration = projectDetails.getDurationHrs();
        if (projectDetails.getProjectTaskDeatils() != null && unassignedDuration != 0) {
            for (TaskDetails taskDetails : projectDetails.getProjectTaskDeatils()) {
                if (!taskDetails.getExtraTimeTask()) {
                    unassignedDuration -= taskDetails.getDuration();
                }
            }
        }

        return unassignedDuration;
    }

    public List<ProjectDetails> getAllProjectDetailsList(Date startTime, boolean isFullLoad)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getAllProjectItems"), dataMap);

        getProjectsList(projectDetailsList, projectItemList, isFullLoad);
        return projectDetailsList;
    }

    public List<ProjectDetails> getPrimaryProjectsDetailList(Date startDate, Date endDate, Boolean includeArchive)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQueryOrderedBy(HQLStrings.getString("getPrimaryProjectsItems"),
                dataMap, HQLStrings.getString("getPrimaryProjectsItems-OrderBy"));

        getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
        return projectDetailsList;

        /*
         * List<Object[]> projectItemResultList =
         * famstackDataAccessObjectManager.executeSQLQuery(HQLStrings.getString("getPrimaryProjectsItems"), dataMap);
         * for (int i = 0; i < projectItemResultList.size(); i++) { ProjectDetails projectDetails = new
         * ProjectDetails(); Object[] data = projectItemResultList.get(i); projectDetails.setId((Integer) data[0]);
         * projectDetails.setName((String) data[1]); projectDetails.setCreatedDate((Timestamp) data[2]); String
         * completionDateString = DateUtils.format((Date) data[3], DateUtils.DATE_TIME_FORMAT);
         * projectDetails.setCompletionTime(completionDateString); projectDetails.setCode((String) data[4]);
         * projectDetails.setStatus(ProjectStatus.valueOf((String) data[5])); projectDetailsList.add(projectDetails); }
         */

    }

    public List<ProjectDetails> searchProjectDetails(String searchString, Boolean includeArchive)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("searchString", "%" + searchString + "%");

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQueryOrderedBy(
                HQLStrings.getString("getPrimaryProjectsSearchItems"), dataMap,
                HQLStrings.getString("getPrimaryProjectsItems-OrderBy"));

        getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
        return projectDetailsList;
    }

    private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList, boolean isFullLoad)
    {
        getProjectsList(projectDetailsList, projectItemList, isFullLoad, false);
    }

    private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList, boolean isFullLoad,
        Boolean includeArchive)
    {
        if (projectItemList != null) {
            for (Object projectItemObj : projectItemList) {
                ProjectItem projectItem = (ProjectItem) projectItemObj;
                ProjectDetails projectDetails = mapProjectItemToProjectDetails(projectItem, isFullLoad, includeArchive);
                if (projectDetails != null) {
                    projectDetailsList.add(projectDetails);
                }
            }
        }
    }

    public List<ProjectDetails> getAllProjectDetailsList(ProjectStatus projectStatus, Date startTime)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", projectStatus);
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getProjectItemsByStatus"), dataMap);
        getProjectsList(projectDetailsList, projectItemList, false);
        return projectDetailsList;
    }

    public List<ProjectDetails> getMissedTimeLineProjectDetails(Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        dataMap.put("status", ProjectStatus.COMPLETED);
        dataMap.put("completionDate", new Date());
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager
                .executeQuery(HQLStrings.getString("getMissedTimeLineProjectItems"), dataMap);
        getProjectsList(projectDetailsList, projectItemList, false);

        return projectDetailsList;
    }

    public List<ProjectDetails> getAllMissedTimeLineProjectDetails(Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        dataMap.put("status", ProjectStatus.COMPLETED);
        dataMap.put("completionDate", new Date());
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("getMissedTimeLineProjectItems"),
                dataMap);
        getProjectsList(projectDetailsList, projectItemList, false);

        return projectDetailsList;
    }

    public List<ProjectDetails> getAllProjectDetailsList(String projectCode, int projectId, Boolean includeArchive)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("code", projectCode);
        dataMap.put("projectId", projectId);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQueryOrderedBy(HQLStrings.getString("getProjectItemsByCode"),
                dataMap, HQLStrings.getString("getProjectItemsByCode-OrderBy"));

        getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
        return projectDetailsList;
    }

    public long getAllProjectDetailsCount(ProjectStatus projectStatus, Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("startTime", startTime);
        dataMap.put("status", projectStatus);

        long count =
            famstackDataAccessObjectManager.getCount(HQLStrings.getString("getProjectItemCountByStatus"), dataMap);

        return count;
    }

    public long getAllMissedTimeLineProjectDetailsCount(Date startTime)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", ProjectStatus.COMPLETED);
        dataMap.put("completionDate", new Date());
        dataMap.put("startTime", startTime);

        long count =
            famstackDataAccessObjectManager.getCount(HQLStrings.getString("getMissedTimeLineProjectItemCountByStatus"),
                dataMap);

        return count;
    }

    public ProjectDetails getProjectDetails(int projectId)
    {
        ProjectDetails projectDetails =
            mapProjectItemToProjectDetails(
                (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class), true);
        if (projectDetails != null) {
            projectDetails.setDuplicateProjects(getAllProjectDetailsList(projectDetails.getCode(),
                projectDetails.getId(), false));
        }
        return projectDetails;

    }

    public List<ProjectDetails> loadDuplicateProjects(int projectId, String projectCode, Boolean includeArchive)
    {
        return getAllProjectDetailsList(projectCode, projectId, includeArchive);
    }

    public Map<String, List<TaskDetails>> getProjectTasksDataList(Integer userId, int dayfilter)
    {
        Map<String, List<TaskDetails>> projectTaskDetailsMap = famstackProjectTaskManager.getAllProjectTask(userId);
        List<TaskDetails> backLogTasks = famstackProjectTaskManager.getBaklogProjectTasks(userId, dayfilter);
        moveInProgressBackLogTasksToInProgress(projectTaskDetailsMap, backLogTasks);
        projectTaskDetailsMap.put("BACKLOG", backLogTasks);

        sorTaksDetails(projectTaskDetailsMap);
        return projectTaskDetailsMap;
    }

    private void sorTaksDetails(Map<String, List<TaskDetails>> projectTaskDetailsMap)
    {
        List<TaskDetails> assignedTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.ASSIGNED);
        List<TaskDetails> inProgressTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.INPROGRESS);
        List<TaskDetails> completedTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.COMPLETED);
        List<TaskDetails> backLogTaskDetailsList = projectTaskDetailsMap.get("BACKLOG");

        sortProjectData(assignedTaskDetailsList);
        sortProjectData(inProgressTaskDetailsList);
        sortProjectData(completedTaskDetailsList);
        sortProjectData(backLogTaskDetailsList);
    }

    private void sortProjectData(List<TaskDetails> taskDetailsList)
    {
        if (taskDetailsList != null) {
            Collections.sort(taskDetailsList, new Comparator<TaskDetails>()
            {
                @Override
                public int compare(TaskDetails taskDetails1, TaskDetails taskDetails2)
                {
                    if (taskDetails1.getPriorityInt() > taskDetails2.getPriorityInt()) {
                        return -1;
                    } else if (taskDetails1.getPriorityInt() < taskDetails2.getPriorityInt()) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
    }

    private void moveInProgressBackLogTasksToInProgress(Map<String, List<TaskDetails>> projectTaskDetailsMap,
        List<TaskDetails> backLogTasks)
    {
        List<TaskDetails> inProgressTaskList = new ArrayList<TaskDetails>();

        if (backLogTasks != null) {
            for (TaskDetails taskDetails : backLogTasks) {
                if (taskDetails.getStatus() == TaskStatus.INPROGRESS) {
                    inProgressTaskList.add(taskDetails);
                }
            }

            projectTaskDetailsMap.get(TaskStatus.INPROGRESS.value()).addAll(inProgressTaskList);
            backLogTasks.removeAll(inProgressTaskList);
        }
    }

    public void updateTaskStatus(int taskId, TaskStatus taskStatus, String comments, Date adjustStartTime,
        Date adjustCompletionTimeDate)
    {
        TaskItem taskItem =
            famstackProjectTaskManager.updateTaskStatus(taskId, taskStatus, comments, adjustStartTime,
                adjustCompletionTimeDate);
        updateProjectStatusBasedOnTaskStatus(taskItem.getProjectItem().getProjectId());

    }

    public String getUserTaskActivityForCalenderJson(String startDate, String endDate, int userId)
    {
        return famstackProjectTaskManager.getUserTaskActivityJson(startDate, endDate, userId);
    }

    public String getProjectNameJson(String query)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", query + "%");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonProductListObject = new JSONObject();
        List<?> projectItems =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("searchForProjectNames"), dataMap);

        for (Object projectItemObj : projectItems) {
            ProjectItem projectItem = (ProjectItem) projectItemObj;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", projectItem.getName());
            jsonObject.put("data", projectItem.getProjectId());
            jsonArray.put(jsonObject);
        }
        jsonProductListObject.put("suggestions", jsonArray);
        return jsonProductListObject.toString();
    }

    public List<ProjectDetails> getAllProjectDetailsList(Date startDate, Date endDate)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("searchForProjectsForRepoting"), dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("startDate" + startDate);
        logDebug("endDate" + endDate);
        getProjectsList(projectDetailsList, projectItemList, true);
        return projectDetailsList;
    }

    public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(Date startDate, Date endDate)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

        List<ProjectTaskActivityDetails> projectDetailsList = new ArrayList<>();

        String sqlQuery = HQLStrings.getString("projectTeamAssigneeReportSQL");
        String userGroupId = getFamstackUserSessionConfiguration().getUserGroupId();
        sqlQuery += " and utai.user_grp_id = " + userGroupId;
        sqlQuery += " " + HQLStrings.getString("projectTeamAssigneeReportSQL-OrderBy");

        List<Object[]> projectItemList = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery, dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("startDate" + startDate);
        logDebug("endDate" + endDate);
        mapProjectsList(projectDetailsList, projectItemList);
        return projectDetailsList;
    }

    private void mapProjectsList(List<ProjectTaskActivityDetails> projectDetailsList, List<Object[]> projectItemList)
    {

        /*
         * projectStartTime projectCompletionTime projectCode projectId projectNumber projectName projectStatus
         * projectType projectCategory projectTeamId projectClientId taskName taskActivityStartTime taskActivityDuration
         * userId
         */

        Map<String, ProjectTaskActivityDetails> projectCacheMap = new HashMap<>();
        ProjectTaskActivityDetails projectTaskActivityDetailsTmp;

        for (int i = 0; i < projectItemList.size(); i++) {
            ProjectTaskActivityDetails projectTaskActivityDetails = new ProjectTaskActivityDetails();
            Object[] data = projectItemList.get(i);
            projectTaskActivityDetails.setProjectStartTime((Date) data[0]);
            projectTaskActivityDetails.setProjectCompletionTime((Date) data[1]);
            projectTaskActivityDetails.setProjectCode((String) data[2]);
            projectTaskActivityDetails.setProjectId((Integer) data[3]);
            projectTaskActivityDetails.setProjectNumber((String) data[4]);
            projectTaskActivityDetails.setProjectName((String) data[5]);
            projectTaskActivityDetails.setProjectStatus(ProjectStatus.valueOf((String) data[6]));
            projectTaskActivityDetails.setProjectType(ProjectType.valueOf((String) data[7]));
            projectTaskActivityDetails.setProjectCategory((String) data[8]);
            projectTaskActivityDetails.setProjectTeamId((Integer) data[9]);
            projectTaskActivityDetails.setProjectClientId((Integer) data[10]);

            projectTaskActivityDetails.setTaskName((String) data[11]);
            projectTaskActivityDetails.setTaskActivityStartTime((Date) data[12]);
            projectTaskActivityDetails.setTaskActivityDuration((Integer) data[13]);
            projectTaskActivityDetails.setTaskActActivityDuration((Integer) data[13]);

            projectTaskActivityDetails.setUserId((Integer) data[14]);

            projectTaskActivityDetails.setTaskId((Integer) data[15]);
            projectTaskActivityDetails.setTaskActivityId((Integer) data[16]);
            projectTaskActivityDetails.setTaskActivityEndTime((Date) data[17]);
            String key = "D" + DateUtils.format((Date) data[0], DateUtils.DATE_FORMAT);
            key += "T" + data[15];
            key += "U" + data[14];

            projectTaskActivityDetailsTmp = projectCacheMap.get(key);
            if (projectTaskActivityDetailsTmp != null) {
                projectTaskActivityDetailsTmp.addToChildProjectActivityDetailsMap(projectTaskActivityDetails);
                projectTaskActivityDetailsTmp.setTaskActivityDuration(projectTaskActivityDetailsTmp
                    .getTaskActivityDuration() + projectTaskActivityDetails.getTaskActivityDuration());
            } else {
                projectCacheMap.put(key, projectTaskActivityDetails);
                projectDetailsList.add(projectTaskActivityDetails);
            }
        }
    }

    public void updateProjectItem(ProjectDetails projectDetails)
    {
        ProjectItem projectItem = getProjectItemById(projectDetails.getId());
        if (projectItem != null) {
            saveProjectItem(projectDetails, projectItem);
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem,
                ProjectActivityType.PROJECT_DETAILS_UPDATED, null);
        }
    }

    public void updateProjectActivityItem(int projectId, ProjectActivityType projectActivityType, String description)
    {
        ProjectItem projectItem = getProjectItemById(projectId);
        if (projectItem != null) {
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem,
                ProjectActivityType.PROJECT_DETAILS_UPDATED, description);
        }
    }

    public TaskDetails getProjectTaskById(int taskId)
    {
        return famstackProjectTaskManager.getTaskDetailsById(taskId);
    }

    public List<ProjectStatusDetails> getProjectItemByTypeCount(Date startTime, Date endTime)
    {
        List<ProjectStatusDetails> projectStatusDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("projectItemByTypeCountSQL"),
                dataMap);

        for (int i = 0; i < result.size(); i++) {
            ProjectStatusDetails projectStatusDetails = new ProjectStatusDetails();
            Object[] data = result.get(i);
            logDebug("project count " + data[1]);
            logDebug("Project type " + data[0]);
            projectStatusDetails.setLabel((String) data[0]);
            projectStatusDetails.setValue(data[1]);
            projectStatusDetailsList.add(projectStatusDetails);
        }

        return projectStatusDetailsList;
    }

    public List<TeamUtilizatioDetails> getTeamUtilizationJson(Date startTime, Date endTime)
    {
        List<TeamUtilizatioDetails> teamUtilizatioDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("teamUtilizationSQL"), dataMap);

        for (int i = 0; i < result.size(); i++) {
            TeamUtilizatioDetails teamUtilizatioDetails = new TeamUtilizatioDetails();
            Object[] data = result.get(i);
            logDebug("team Name " + data[0]);
            logDebug("billable " + data[2]);
            logDebug("non billable " + data[3]);
            ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(data[0]);
            if (projectSubTeamDetails != null) {
                teamUtilizatioDetails.setName(projectSubTeamDetails.getName());
            } else {
                teamUtilizatioDetails.setName(data[0]);
            }

            teamUtilizatioDetails.setBillable(data[2]);
            teamUtilizatioDetails.setNonBillable(data[3]);

            teamUtilizatioDetailsList.add(teamUtilizatioDetails);
        }

        return teamUtilizatioDetailsList;
    }

    public List<ProjectCategoryDetails> getProjectCategoryJson(Date startTime, Date endTime)
    {
        List<ProjectCategoryDetails> projectCategoryDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("projectCategorySQL"), dataMap);

        for (int i = 0; i < result.size(); i++) {
            ProjectCategoryDetails projectCategoryDetails = new ProjectCategoryDetails();
            Object[] data = result.get(i);
            logDebug("category Name " + data[0]);
            logDebug("coutn " + data[1]);

            projectCategoryDetails.setCategoryName(data[0]);
            projectCategoryDetails.setCount(data[1]);
            projectCategoryDetailsList.add(projectCategoryDetails);
        }

        return projectCategoryDetailsList;
    }

    public List<ClientProjectDetails> getClientProject(Date startTime, Date endTime)
    {
        List<ClientProjectDetails> clientProjectDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);
        List<Object[]> result =
            getFamstackDataAccessObjectManager().executeSQLQuery(HQLStrings.getString("clientProjectStatusSQL"),
                dataMap);

        for (int i = 0; i < result.size(); i++) {
            ClientProjectDetails clientProjectDetails = new ClientProjectDetails();
            Object[] data = result.get(i);
            logDebug("account Name " + data[0]);
            logDebug("completed " + data[2]);
            logDebug("not ocmpleted " + data[1]);

            AccountDetails accountDetails = FamstackAccountManager.getAccountmap().get(data[0]);
            if (accountDetails != null) {
                clientProjectDetails.setClientName(accountDetails.getName());
            } else {
                clientProjectDetails.setClientName(data[0]);
            }
            clientProjectDetails.setCompletedCount(data[2]);
            clientProjectDetails.setNoCompletedCount(data[1]);
            clientProjectDetailsList.add(clientProjectDetails);
        }

        return clientProjectDetailsList;
    }

    public List<ProjectDetails> getAllProjectDetailsWithIn(Date startDate, Date startDate2)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startDate", startDate);
        dataMap.put("startDate2", startDate2);

        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("searchForProjectsWithIn"),
                dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("startDate" + startDate);
        logDebug("startDate 2" + startDate2);
        getProjectsList(projectDetailsList, projectItemList, true);
        return projectDetailsList;
    }

    public List<ProjectDetails> getAllProjectDetailsEndTimeWithIn(Date endDate, Date endDate2)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));
        dataMap.put("endDate2", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate2, 0));

        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("searchForProjectsEndWithIn"),
                dataMap);
        logDebug("projectItemList" + projectItemList);
        logDebug("endDate" + endDate);
        logDebug("endDate 2" + endDate2);
        getProjectsList(projectDetailsList, projectItemList, true);
        return projectDetailsList;
    }

    public List<TaskDetails> getProjectTaskDetails(int projectId)
    {
        return famstackProjectTaskManager.getProjectDetailsTaskDetailsByProjectId(projectId);
    }

    public RecurringProjectDetails mapRecurringProjectItem(RecurringProjectItem recurringProjectItem)
    {
        RecurringProjectDetails recurringProjectDetails = null;
        if (recurringProjectItem != null) {
            recurringProjectDetails = new RecurringProjectDetails();
            recurringProjectDetails.setId(recurringProjectItem.getId());

            String cronExpression = recurringProjectItem.getCronExpression();

            if (cronExpression.contains("|")) {
                cronExpression = cronExpression.substring(0, cronExpression.indexOf("|"));
            }

            recurringProjectDetails.setCronExpression(cronExpression);
            recurringProjectDetails.setLastRun(recurringProjectItem.getLastRun());
            recurringProjectDetails.setName(recurringProjectItem.getName());
            recurringProjectDetails.setNextRun(recurringProjectItem.getNextRun());
            recurringProjectDetails.setProjectId(recurringProjectItem.getProjectId());
            recurringProjectDetails.setProjectCode(recurringProjectItem.getProjectCode());
            recurringProjectDetails.setRequestedBy(recurringProjectItem.getRequestedBy());
            recurringProjectDetails.setUserGroupId(recurringProjectItem.getUserGroupId());
            Date endDate = recurringProjectItem.getEndDate();
            if (endDate != null) {
                recurringProjectDetails.setEndDateString(DateUtils.format(endDate, DateUtils.DATE_FORMAT));
            }
        }
        return recurringProjectDetails;
    }

    public RecurringProjectItem mapRecurringProjectDetails(RecurringProjectDetails recurringProjectDetails)
    {
        RecurringProjectItem recurringProjectItem = null;
        if (recurringProjectDetails != null) {
            recurringProjectItem = new RecurringProjectItem();
            recurringProjectItem.setId(recurringProjectDetails.getId());
            recurringProjectItem.setCronExpression(recurringProjectDetails.getCronExpression());
            recurringProjectItem.setName(recurringProjectDetails.getName());
            recurringProjectItem.setProjectId(recurringProjectDetails.getProjectId());
            recurringProjectItem.setProjectCode(recurringProjectDetails.getProjectCode());
            recurringProjectItem.setRequestedBy(recurringProjectDetails.getRequestedBy());
            recurringProjectItem.setUserGroupId(recurringProjectDetails.getUserGroupId());
        }
        return recurringProjectItem;
    }

    public List<RecurringProjectDetails> getAllRecurringProjects()
    {
        List<RecurringProjectItem> allRecurringProjectItems =
            (List<RecurringProjectItem>) famstackDataAccessObjectManager.getAllItems("RecurringProjectItem");
        List<RecurringProjectDetails> allRecurringProjectDetails = new ArrayList<>();
        if (allRecurringProjectDetails != null) {
            for (RecurringProjectItem recurringProjectItem : allRecurringProjectItems) {
                allRecurringProjectDetails.add(mapRecurringProjectItem(recurringProjectItem));
            }
        }

        return allRecurringProjectDetails;
    }

    public RecurringProjectDetails getRecurringProjectDetails(String projectCode, int projectId)
    {
        return mapRecurringProjectItem(getRecurringProjectItem(projectCode, projectId));
    }

    public RecurringProjectItem getRecurringProjectItem(String projectCode, int projectId)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectCode", projectCode);
        List<?> RecurringProjectItems =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getRecurringProject"), dataMap);

        if (RecurringProjectItems != null && RecurringProjectItems.size() > 0) {
            return (RecurringProjectItem) RecurringProjectItems.get(0);
        }

        return null;
    }

    public RecurringProjectDetails createRecurringProject(String projectCode, int projectId, String cronExpression,
        String recurringEndDate)
    {
        return mapRecurringProjectItem(saveOrUpdateRecurringProject(projectCode, projectId, cronExpression,
            recurringEndDate));
    }

    private RecurringProjectItem saveOrUpdateRecurringProject(String projectCode, int projectId, String cronExpression,
        String recurringEndDate)
    {
        RecurringProjectItem recurringProjectItem = getRecurringProjectItem(projectCode, projectId);
        if (recurringProjectItem == null) {
            recurringProjectItem = new RecurringProjectItem();
        }

        Date nextRun = FamstackUtils.getNextRunFromCron(cronExpression, new Date());

        String newCronExpression = cronExpression;
        if (cronExpression.contains("#")) {
            String secondCronExpression =
                cronExpression.contains("#1") ? cronExpression.replace("#1", "#3") : cronExpression.contains("#2")
                    ? cronExpression.replace("#2", "#4") : "";
            Date nextRun2 = FamstackUtils.getNextRunFromCron(secondCronExpression, new Date());

            if (nextRun.after(nextRun2)) {
                nextRun = nextRun2;
            }

            newCronExpression = cronExpression + "|" + secondCronExpression;
        }
        if (StringUtils.isNotBlank(recurringEndDate)) {
            Date recurringEndTime = DateUtils.tryParse(recurringEndDate, DateUtils.DATE_FORMAT);
            recurringEndTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, recurringEndTime, 0);
            recurringProjectItem.setEndDate(new Timestamp(recurringEndTime.getTime()));
        } else {
            recurringProjectItem.setEndDate(null);
        }
        recurringProjectItem.setCronExpression(newCronExpression);
        recurringProjectItem.setNextRun(nextRun == null ? null : new Timestamp(nextRun.getTime()));
        recurringProjectItem.setName(projectCode);
        recurringProjectItem.setProjectId(projectId);
        recurringProjectItem.setProjectCode(projectCode);

        famstackDataAccessObjectManager.saveOrUpdateItem(recurringProjectItem);
        return recurringProjectItem;
    }

    public void deleteRecuringProjectDetails(int recurringProjectId)
    {
        RecurringProjectItem recurringProjectItem =
            (RecurringProjectItem) famstackDataAccessObjectManager.getItemById(recurringProjectId,
                RecurringProjectItem.class);
        if (recurringProjectItem != null) {
            famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
        }
    }

    public List<Object> getAllRecuringProjectCodes()
    {
        List<Object[]> result =
            getFamstackDataAccessObjectManager()
                .executeSQLQuery(HQLStrings.getString("recurringProjectCodesSQL"), null);
        List<Object> projectCodes = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            projectCodes.add(result.get(i));
        }
        return projectCodes;
    }

    public void createRecurringProjects()
    {
        List<RecurringProjectItem> recurringProjectItems = getAllrecurringProjectsForCreation();
        logDebug("recurringProjectItems :" + recurringProjectItems);
        if (recurringProjectItems != null) {
            for (RecurringProjectItem recurringProjectItem : recurringProjectItems) {
                String projectCode = recurringProjectItem.getProjectCode();
                if (recurringProjectItem.getEndDate() != null
                    && new Date().after(new Date(recurringProjectItem.getEndDate().getTime()))) {
                    famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
                    logInfo("Deleting recurring project after expiry : " + projectCode);
                    continue;
                }
                ProjectItem projectItem = getLatestProjectByCode(projectCode);
                logDebug("recurring Project code :" + projectCode);
                if (projectItem != null) {
                    Timestamp projectStartTime = getNewTimeForDuplicate(projectItem.getStartTime());

                    int projectDuration =
                        DateUtils.getTimeDifference(TimeInType.MINS, projectItem.getCompletionTime().getTime(),
                            projectItem.getStartTime().getTime());

                    if (projectDuration < 0) {
                        projectDuration = 60 * 24 * 15;
                    }

                    Timestamp projectEndTime =
                        new Timestamp(DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, projectStartTime,
                            projectDuration).getTime());

                    List<TaskDetails> taskItemList = getUpdatedRecureTaskDetailsList(projectItem);

                    quickDuplicateProject(projectItem, projectItem.getName(), projectItem.getDurationHrs(),
                        projectStartTime, projectEndTime, taskItemList);
                }

                recurringProjectItem.setLastRun(recurringProjectItem.getNextRun());
                String cronExpression = recurringProjectItem.getCronExpression();
                Date nextRun = null;
                if (cronExpression.contains("|")) {
                    String cronExpression1 = cronExpression.split("[|]")[0];
                    String cronExpression2 = cronExpression.split("[|]")[1];

                    nextRun = FamstackUtils.getNextRunFromCron(cronExpression1, recurringProjectItem.getNextRun());
                    Date nextRun2 =
                        FamstackUtils.getNextRunFromCron(cronExpression2, recurringProjectItem.getNextRun());

                    if (nextRun.after(nextRun2)) {
                        nextRun = nextRun2;
                    }

                } else {
                    nextRun = FamstackUtils.getNextRunFromCron(cronExpression, recurringProjectItem.getNextRun());
                }

                recurringProjectItem.setNextRun(nextRun == null ? null : new Timestamp(nextRun.getTime()));

                famstackDataAccessObjectManager.saveOrUpdateItem(recurringProjectItem);
            }
        }

    }

    private List<TaskDetails> getUpdatedRecureTaskDetailsList(ProjectItem projectItem)
    {
        List<TaskDetails> taskDetailsList = new ArrayList<>();

        for (TaskItem taskItem : projectItem.getTaskItems()) {
            Date taskStartTime = getNewTimeForDuplicate(taskItem.getStartTime());
            if (taskItem.getCanRecure()) {
                TaskDetails taskDetails = new TaskDetails();
                taskDetails.setAssignee(taskItem.getAssignee());
                taskDetails.setName(taskItem.getName());
                taskDetails.setCanRecure(taskItem.getCanRecure());
                taskDetails.setDescription(taskItem.getDescription());
                taskDetails.setStartTime(DateUtils.format(taskStartTime, DateUtils.DATE_TIME_FORMAT));
                taskDetails.setDuration(taskItem.getDuration());
                taskDetails.setProjectTaskType(taskItem.getProjectTaskType());
                taskDetailsList.add(taskDetails);
            }

        }
        return taskDetailsList;
    }

    private Timestamp getNewTimeForDuplicate(Timestamp startTime)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);

        Calendar todaysCalender = Calendar.getInstance();
        todaysCalender.setTime(new Date());

        todaysCalender.set(todaysCalender.get(Calendar.YEAR), todaysCalender.get(Calendar.MONTH),
            todaysCalender.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

        logDebug("todaysCalender :" + todaysCalender);
        return new Timestamp(todaysCalender.getTimeInMillis());
    }

    private List<RecurringProjectItem> getAllrecurringProjectsForCreation()
    {
        Map<String, Object> dataMap = new HashMap<>();
        Date startTime = new Date();
        dataMap.put("startTime", startTime);

        List<?> recurrinProjectItems =
            famstackDataAccessObjectManager.executeAllGroupQuery(HQLStrings.getString("recurringProjectsWithIn"),
                dataMap);

        logDebug("recurrinProjectItems" + recurrinProjectItems);
        logDebug("startTime" + startTime);
        return (List<RecurringProjectItem>) recurrinProjectItems;

    }

    public void deleteProjects(List<Integer> projectIds, String type)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectIds", projectIds);
        if ("soft".equalsIgnoreCase(type)) {
            famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("projectSoftDeleteSQL"), dataMap);

        } else if ("hard".equalsIgnoreCase(type)) {

            for (Integer projectId : projectIds) {
                try {
                    deleteProjectItem(projectId);
                } catch (Exception e) {
                    logError("Unable to delete project : " + projectId, e);
                }
            }
        } else if ("undoarchived".equalsIgnoreCase(type)) {
            famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("projectUndoSoftDeleteSQL"), dataMap);
        }

    }

    public void softDeleteProjectOlderThan(int days)
    {

        Date dateTill = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1 * days);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dateTill", dateTill);
        famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("projectSoftDeleteOlderSQL"), dataMap);
    }

}
