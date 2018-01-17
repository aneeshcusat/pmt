package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.dashboard.bean.ClientProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectCategoryDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectStatusDetails;
import com.famstack.projectscheduler.dashboard.bean.TeamUtilizatioDetails;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;

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

    public void createProjectItem(ProjectDetails projectDetails)
    {
        ProjectItem projectItem = new ProjectItem();
        projectItem.setStatus(ProjectStatus.NEW);
        saveProjectItem(projectDetails, projectItem);
        famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED, null);
    }

    public void quickDuplicateProject(int projectId, String projectName, int projectDuration, String projectStartTime,
        String projectEndTime, String taskDetailsList)
    {
        ProjectItem projectDetails = getProjectItemById(projectId);
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

            projectItem.setStartTime(startTimeStamp);
            projectItem.setCompletionTime(completionTimeStamp);
            projectItem.setDuration(projectDuration);

            projectItem.setAccountId(projectDetails.getAccountId());
            projectItem.setTeamId(projectDetails.getTeamId());
            projectItem.setClientId(projectDetails.getClientId());
            projectItem.setTags(projectDetails.getTags());
            projectItem.setType(projectDetails.getType());
            projectItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
            projectItem.setWatchers(projectDetails.getWatchers());
            famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
            famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED,
                "Duplicated from " + projectDetails.getProjectId());

            if (taskDetailsList != null && !taskDetailsList.isEmpty()) {
                String[] taskList = taskDetailsList.split("#TD#");

                if (taskList.length > 0) {
                    for (String taskData : taskList) {
                        String[] task = taskData.split("#TDD#");
                        if (task.length > 0) {
                            TaskDetails taskDetails = new TaskDetails();
                            taskDetails.setAssignee(Integer.parseInt(task[0]));
                            taskDetails.setName(task[1]);
                            taskDetails.setStartTime(task[2]);
                            taskDetails.setDuration(Integer.parseInt(task[3]));
                            taskDetails.setReviewTask("0".equalsIgnoreCase(task[4]) ? false : true);
                            taskDetails.setProjectId(projectId);

                            famstackProjectTaskManager.createTaskItem(taskDetails, projectItem);
                        }
                    }
                }
            }

            updateProjectStatusBasedOnTaskStatus(projectId);

        }

    }

    private void saveProjectItem(ProjectDetails projectDetails, ProjectItem projectItem)
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
        projectItem.setDuration(projectDetails.getDuration());

        projectItem.setAccountId(projectDetails.getAccountId());
        projectItem.setTeamId(projectDetails.getTeamId());
        projectItem.setClientId(projectDetails.getClientId());
        projectItem.setTags(projectDetails.getTags());
        projectItem.setType(projectDetails.getType());
        projectItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
        projectItem.setWatchers(projectDetails.getWatchers());
        famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
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

    public ProjectItem getProjectItemById(int projectId)
    {
        return (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
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
        int projectDuration = projectItem.getDuration();
        if (projectItem != null) {
            for (TaskItem taskItem : projectItem.getTaskItems()) {
                TaskStatus taskStatus = taskItem.getStatus();
                taskStatsList.add(taskStatus);
                projectDuration -= taskItem.getDuration();
            }
        }
        if (taskStatsList.isEmpty()) {
            return ProjectStatus.NEW;
        } else if (projectDuration != 0) {
            return ProjectStatus.UNASSIGNED;
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

    public String getUserTaskActivityJson()
    {
        return famstackProjectTaskManager.getUserTaskActivityJson();
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem)
    {
        return mapProjectItemToProjectDetails(projectItem, true);
    }

    public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem, boolean isFullLoad)
    {
        if (projectItem != null) {
            ProjectDetails projectDetails = new ProjectDetails();
            projectDetails.setCode(projectItem.getCode());
            projectDetails.setId(projectItem.getProjectId());
            projectDetails.setName(projectItem.getName());
            projectDetails.setProjectTaskDeatils(famstackProjectTaskManager.mapProjectTaskDetails(
                projectItem.getTaskItems(), isFullLoad));
            projectDetails.setDuration(projectItem.getDuration());
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
        int unassignedCount = projectDetails.getDuration();
        if (projectDetails.getProjectTaskDeatils() != null) {
            for (TaskDetails taskDetails : projectDetails.getProjectTaskDeatils()) {
                if (!taskDetails.getExtraTimeTask()) {
                    unassignedCount -= taskDetails.getDuration();
                }
            }
        }
        return unassignedCount;
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

    public List<ProjectDetails> getPrimaryProjectsDetailList(Date startTime, boolean isFullLoad)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("startTime", startTime);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getPrimaryProjectsItems"), dataMap);

        getProjectsList(projectDetailsList, projectItemList, isFullLoad);
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

    private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList, boolean isFullLoad)
    {
        if (projectItemList != null) {
            for (Object projectItemObj : projectItemList) {
                ProjectItem projectItem = (ProjectItem) projectItemObj;
                ProjectDetails projectDetails = mapProjectItemToProjectDetails(projectItem, isFullLoad);
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

    public List<ProjectDetails> getAllProjectDetailsList(String projectCode, int projectId)
    {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("code", projectCode);
        dataMap.put("projectId", projectId);

        List<?> projectItemList =
            famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("getProjectItemsByCode"), dataMap);
        getProjectsList(projectDetailsList, projectItemList, false);
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
            mapProjectItemToProjectDetails((ProjectItem) famstackDataAccessObjectManager.getItemById(projectId,
                ProjectItem.class));
        projectDetails.setDuplicateProjects(getAllProjectDetailsList(projectDetails.getCode(), projectDetails.getId()));
        return projectDetails;
    }

    public List<ProjectDetails> loadDuplicateProjects(int projectId, String projectCode)
    {
        return getAllProjectDetailsList(projectCode, projectId);
    }

    public Map<String, List<TaskDetails>> getProjectTasksDataList(Integer userId)
    {
        Map<String, List<TaskDetails>> projectTaskDetailsMap = famstackProjectTaskManager.getAllProjectTask(userId);
        List<TaskDetails> backLogTasks = famstackProjectTaskManager.getBaklogProjectTasks(userId);
        projectTaskDetailsMap.put("BACKLOG", backLogTasks);
        return projectTaskDetailsMap;
    }

    public void updateTaskStatus(int taskId, TaskStatus taskStatus, String comments, Date adjustStartTime,
        Date adjustCompletionTimeDate)
    {
        TaskItem taskItem =
            famstackProjectTaskManager.updateTaskStatus(taskId, taskStatus, comments, adjustStartTime,
                adjustCompletionTimeDate);
        updateProjectStatusBasedOnTaskStatus(taskItem.getProjectItem().getProjectId());

    }

    public String getUserTaskActivityForCalenderJson(String startDate, String endDate)
    {
        return famstackProjectTaskManager.getUserTaskActivityJson(startDate, endDate);
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

}
