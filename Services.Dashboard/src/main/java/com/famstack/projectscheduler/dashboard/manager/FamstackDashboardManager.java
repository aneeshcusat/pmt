package com.famstack.projectscheduler.dashboard.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.contants.LeaveType;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.dashboard.bean.ClientProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectCategoryDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectStatusDetails;
import com.famstack.projectscheduler.dashboard.bean.TeamUtilizatioDetails;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ConfigurationSettingsItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.employees.bean.GroupMessageDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.employees.bean.UserStatus;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.manager.FamstackGroupMessageManager;
import com.famstack.projectscheduler.manager.FamstackProjectCommentManager;
import com.famstack.projectscheduler.manager.FamstackProjectFileManager;
import com.famstack.projectscheduler.manager.FamstackProjectManager;
import com.famstack.projectscheduler.manager.FamstackUserActivityManager;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;
import com.famstack.projectscheduler.notification.FamstackNotificationServiceManager;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.notification.services.FamstackDesktopNotificationService;
import com.famstack.projectscheduler.security.user.UserRole;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.LimitedQueue;
import com.famstack.projectscheduler.util.StringUtils;
import com.famstack.projectscheduler.utils.FamstackUtils;

@Component
public class FamstackDashboardManager extends BaseFamstackService
{

    @Resource
    FamstackDataAccessObjectManager famstackDataAccessObjectManager;

    @Resource
    FamstackUserProfileManager userProfileManager;

    @Resource
    FamstackProjectManager projectManager;

    @Resource
    FamstackAccountManager famstackAccountManager;

    @Resource
    FamstackProjectCommentManager projectCommentManager;

    @Resource
    FamstackGroupMessageManager groupMessageManager;

    @Resource
    FamstackProjectFileManager famstackProjectFileManager;

    @Resource
    FamstackNotificationServiceManager famstackNotificationServiceManager;

    @Resource
    FamstackDesktopNotificationService famstackDesktopNotificationService;

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    public Map<String, Object> getUserData()
    {
        return null;
    }

    public Map<String, String> createUser(EmployeeDetails employeeDetails)
    {
        Map<String, String> errorMap = new HashMap<String, String>();
        userProfileManager.createUserItem(employeeDetails);
        notifyAll(NotificationType.USER_REGISTRAION, employeeDetails);
        return errorMap;
    }

    public Map<String, String> updateUser(EmployeeDetails employeeDetails)
    {
        Map<String, String> errorMap = new HashMap<String, String>();
        userProfileManager.updateUserItem(employeeDetails);
        notifyAll(NotificationType.USER_UPDATE, employeeDetails);
        return errorMap;
    }

    private Map<String, String> valiateUser(EmployeeDetails employeeDetails)
    {
        Map<String, String> errorMap = new HashMap<>();
        if (!StringUtils.isNotBlank(employeeDetails.getFirstName())
            || !StringUtils.isNotBlank(employeeDetails.getConfirmPassword())
            || !StringUtils.isNotBlank(employeeDetails.getEmail())) {
            errorMap.put("invalidInput", "required inputs are missing");
        }
        return errorMap;
    }

    public UserItem getUser(int userId)
    {
        return userProfileManager.getUserItemById(userId);
    }

    public String getEmployeeDetails(int userId)
    {
        EmployeeDetails employeeDetails = userProfileManager.getEmployee(userId);
        return FamstackUtils.getJsonFromObject(employeeDetails);
    }

    public void deleteUser(int userId)
    {
        userProfileManager.deleteUserItem(userId);

    }

    public void createProject(ProjectDetails projectDetails)
    {
        projectManager.createProjectItem(projectDetails);
        setCurrentUser(projectDetails);
        notifyAll(NotificationType.PROJECT_CREATE, projectDetails);
    }

    public List<ProjectDetails> getProjectsDataList()
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();
        List<ProjectDetails> projectDetailsList = projectManager.getAllProjectDetailsList(startTime, true);
        return projectDetailsList;
    }

    public List<ProjectDetails> getProjects(boolean isFullLoad)
    {
        Date startTime =
            DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), getFamstackUserSessionConfiguration()
                .getProjectViewLimit());
        List<ProjectDetails> projectDetailsList = projectManager.getAllProjectDetailsList(startTime, isFullLoad);

        Collections.sort(projectDetailsList, new Comparator<ProjectDetails>()
        {
            @Override
            public int compare(ProjectDetails projectDetails1, ProjectDetails projectDetails2)
            {
                Date date1 = DateUtils.tryParse(projectDetails1.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
                Date date2 = DateUtils.tryParse(projectDetails2.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);

                if (date1.before(date2)) {
                    return -1;
                } else if (date1.after(date2)) {
                    return 1;
                }
                return 0;
            }
        });
        return projectDetailsList;
    }

    public void deleteProject(int projectId)
    {
        ProjectDetails projectDetails = projectManager.getProjectDetails(projectId);
        projectManager.deleteProjectItem(projectId);
        setCurrentUser(projectDetails);
        notifyAll(NotificationType.PROJECT_DELETE, projectDetails);
    }

    public void createComment(String projectComments, int projectId)
    {
        projectCommentManager.createProjectCommentItem(projectComments, projectId);

        ProjectDetails projectDetails = projectManager.getProjectDetails(projectId);
        setCurrentUser(projectDetails);
        notifyAll(NotificationType.PROJECT_COMMENT_ADDED, projectDetails);
    }

    public ProjectDetails getProjectDetails(int projectId, HttpServletRequest request)
    {
        ProjectDetails projectDetails = projectManager.getProjectDetails(projectId);
        List<String> filesNames = famstackProjectFileManager.getProjectFiles("" + projectDetails.getId(), request);
        List<String> completedFilesNames =
            famstackProjectFileManager.getProjectFiles(projectDetails.getId() + "-completed", request);
        projectDetails.setFilesNames(filesNames);
        projectDetails.setCompletedFilesNames(completedFilesNames);
        return projectDetails;
    }

    public String getProjectDetailsJson(int projectId)
    {
        ProjectDetails projectDetails = projectManager.getProjectDetails(projectId);
        return FamstackUtils.getJsonFromObject(projectDetails);
    }

    public List<GroupDetails> getAllGroups(int userId)
    {
        List<GroupDetails> groupDetailsList = groupMessageManager.getGroupsForUser(userId);
        Collections.sort(groupDetailsList, new Comparator<GroupDetails>()
        {
            @Override
            public int compare(GroupDetails groupDetailsOne, GroupDetails groupDetailsTwo)
            {
                Date date1 = groupDetailsOne.getCreatedDate();
                Date date2 = groupDetailsTwo.getCreatedDate();

                if (date1.before(date2)) {
                    return -1;
                } else if (date1.after(date2)) {
                    return 1;
                }
                return 0;
            }
        });
        return groupDetailsList;
    }

    public void createTask(TaskDetails taskDetails)
    {
        projectManager.createProjectTask(taskDetails);

        if (taskDetails.getAssignee() != 0) {
            triggerTaskNotification(NotificationType.TASK_CREATED_ASSIGNED, taskDetails);
        } else {
            triggerTaskNotification(NotificationType.TASK_CREATED, taskDetails);
        }

    }

    private void triggerTaskNotification(NotificationType type, TaskDetails taskDetails)
    {
        ProjectDetails projectDetails = projectManager.getProjectDetails(taskDetails.getProjectId());
        Map<String, Object> projectTaskMap = new HashMap<>();
        projectTaskMap.put("taskDetails", taskDetails);
        projectTaskMap.put("projectDetails", projectDetails);
        notifyAll(type, projectTaskMap);

    }

    private void setCurrentUser(ProjectDetails projectDetails)
    {
        EmployeeDetails employeeDetails =
            userProfileManager.mapEmployeeDetails(getFamstackApplicationConfiguration().getCurrentUser());
        projectDetails.setEmployeeDetails(employeeDetails);
    }

    public void uploadProjectFile(MultipartFile file, String projectId, HttpServletRequest request)
    {
        famstackProjectFileManager.uploadFile(file, projectId, request);
    }

    public void deleteProjectFile(String fileName, String projectCode, HttpServletRequest request)
    {
        famstackProjectFileManager.deleteFile(fileName, projectCode, request);
    }

    public File getProjectFile(String fileName, String projectCode, HttpServletRequest request)
    {
        return famstackProjectFileManager.getFile(fileName, projectCode, request);

    }

    public void updateTask(TaskDetails taskDetails)
    {
        projectManager.updateProjectTask(taskDetails);
        if (taskDetails.getAssignee() != 0) {
            triggerTaskNotification(NotificationType.TASK_ASSIGNED, taskDetails);
        } else {
            triggerTaskNotification(NotificationType.TASK_UPDATED, taskDetails);
        }
    }

    public void deleteProjectTask(int taskId, int projectId)
    {
        TaskDetails taskDetails = projectManager.getProjectTaskById(taskId);
        projectManager.deleteProjectTask(taskId, projectId);
        taskDetails.setProjectId(projectId);
        triggerTaskNotification(NotificationType.TASK_DELETED, taskDetails);
    }

    public String getUserTaskActivityJson()
    {
        return projectManager.getUserTaskActivityJson();
    }

    public void createGroup(GroupDetails groupDetails)
    {
        groupMessageManager.createGroupItem(groupDetails);

    }

    public void updateGroup(GroupDetails groupDetails)
    {
        groupMessageManager.updateGroupItem(groupDetails);

    }

    public String getGroup(int groupId)
    {
        GroupDetails groupDetails = groupMessageManager.getGroupDetails(groupId);
        return FamstackUtils.getJsonFromObject(groupDetails);
    }

    public void sendMessage(int groupId, String message)
    {
        GroupMessageDetails groupMessageDetails = new GroupMessageDetails();
        groupMessageDetails.setGroup(groupId);
        groupMessageDetails.setDescription(message);
        groupMessageManager.createGroupMessageItem(groupMessageDetails);
    }

    public boolean isValidKeyForUserReset(String key, int userId)
    {
        return userProfileManager.isValidUserResetKey(key, userId);
    }

    public boolean changePassword(String userName, String oldPassword, String password)
    {
        boolean status = userProfileManager.changePassword(userName, oldPassword, password);
        Integer userId = FamstackApplicationConfiguration.getUserIdMap().get(userName);
        EmployeeDetails employeeDetails = null;

        if (userId != null) {
            employeeDetails = getFamstackApplicationConfiguration().getUserMap().get(userId);
        }

        notifyAll(NotificationType.RESET_PASSWORD, employeeDetails);
        return status;
    }

    public String getMessageAfter(int groupId, int messageId)
    {
        List<GroupMessageDetails> groupMessageDetails = groupMessageManager.getGroupMessages(groupId, messageId);
        return FamstackUtils.getJsonFromObject(groupMessageDetails);
    }

    public Map<String, List<TaskDetails>> getProjectTasksDataList(Integer userId)
    {
        Map<String, List<TaskDetails>> taskDetailsMap = projectManager.getProjectTasksDataList(userId);
        sortTaskDeailsList(taskDetailsMap);
        return taskDetailsMap;
    }

    private void sortTaskDeailsList(Map<String, List<TaskDetails>> taskDetailsMap)
    {
        for (String key : taskDetailsMap.keySet()) {

            List<TaskDetails> taskDeatilsList = taskDetailsMap.get(key);
            if (taskDeatilsList != null) {
                Collections.sort(taskDeatilsList, new Comparator<TaskDetails>()
                {
                    @Override
                    public int compare(TaskDetails taskDetails1, TaskDetails taskDetails2)
                    {

                        Date date1 = DateUtils.tryParse(taskDetails1.getStartTime(), DateUtils.DATE_TIME_FORMAT);
                        Date date2 = DateUtils.tryParse(taskDetails2.getStartTime(), DateUtils.DATE_TIME_FORMAT);

                        if (date1.before(date2)) {
                            return -1;
                        } else if (date1.after(date2)) {
                            return 1;
                        }
                        return 0;
                    }

                });
            }
        }
    }

    public String getProjectTasksDataListJson(int userId)
    {
        Map<String, List<TaskDetails>> taskMap = projectManager.getProjectTasksDataList(userId);
        return FamstackUtils.getJsonFromObject(taskMap);
    }

    public void reAssignTask(int taskId, int newUserId, int taskActivityId, TaskStatus taskStatus)
    {
        TaskDetails taskDetails = projectManager.getProjectTaskById(taskId);
        projectManager.reAssignTask(taskDetails, newUserId, taskActivityId, taskStatus);
        triggerTaskNotification(NotificationType.TASK_RE_ASSIGNED, taskDetails);

    }

    public void updateTaskStatus(int taskId, TaskStatus taskStatus, String comments, String adjustStartTime,
        String adjustCompletionTime)
    {
        Date adjustStartTimeDate = null;
        if (StringUtils.isNotBlank(adjustStartTime)) {
            adjustStartTimeDate = DateUtils.tryParse(adjustStartTime, DateUtils.DATE_TIME_FORMAT);
        }

        Date adjustCompletionTimeDate = null;
        if (StringUtils.isNotBlank(adjustCompletionTime)) {
            adjustCompletionTimeDate = DateUtils.tryParse(adjustCompletionTime, DateUtils.DATE_TIME_FORMAT);
        }

        projectManager.updateTaskStatus(taskId, taskStatus, comments, adjustStartTimeDate, adjustCompletionTimeDate);
        TaskDetails taskDetails = projectManager.getProjectTaskById(taskId);
        switch (taskStatus) {
            case ASSIGNED:
                triggerTaskNotification(NotificationType.TASK_ASSIGNED, taskDetails);
                break;
            case CLOSED:
                triggerTaskNotification(NotificationType.TASK_CLOSED, taskDetails);
                break;
            case COMPLETED:
                triggerTaskNotification(NotificationType.TASK_COMPLETED, taskDetails);
                break;
            case INPROGRESS:
                triggerTaskNotification(NotificationType.TASK_INPROGRESS, taskDetails);
                break;
            case NEW:
                triggerTaskNotification(NotificationType.TASK_CREATED, taskDetails);
                break;
            default:
                break;
        }

    }

    public List<ProjectDetails> getProjectDetails(ProjectStatus projectStatus)
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();

        if (projectStatus == ProjectStatus.MISSED) {
            return projectManager.getAllMissedTimeLineProjectDetails(startTime);
        }
        return projectManager.getAllProjectDetailsList(projectStatus, startTime);
    }

    public Map<String, Long> getProjectsCounts()
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();
        Map<String, Long> projectCounts = new HashMap<>();
        projectCounts.put(ProjectStatus.ASSIGNED.value(),
            projectManager.getAllProjectDetailsCount(ProjectStatus.ASSIGNED, startTime));
        projectCounts.put(ProjectStatus.UNASSIGNED.value(),
            projectManager.getAllProjectDetailsCount(ProjectStatus.UNASSIGNED, startTime));
        projectCounts.put(ProjectStatus.INPROGRESS.value(),
            projectManager.getAllProjectDetailsCount(ProjectStatus.INPROGRESS, startTime));
        projectCounts.put(ProjectStatus.COMPLETED.value(),
            projectManager.getAllProjectDetailsCount(ProjectStatus.COMPLETED, startTime));
        projectCounts.put(ProjectStatus.NEW.value(),
            projectManager.getAllProjectDetailsCount(ProjectStatus.NEW, startTime));
        projectCounts.put("MISSED", projectManager.getAllMissedTimeLineProjectDetailsCount(startTime));

        return projectCounts;
    }

    public String getAjaxFullcalendar(String startDate, String endDate)
    {
        return projectManager.getUserTaskActivityForCalenderJson(startDate, endDate);
    }

    public List<AccountDetails> getAccountDataList()
    {
        return famstackAccountManager.getAllAccountDetails();
    }

    public String getProjectNameJson(String query)
    {
        return projectManager.getProjectNameJson(query);
    }

    public List<ProjectDetails> getProjectsReporingDataList(Date startDate, Date endDate)
    {
        List<ProjectDetails> projectDetailsList = projectManager.getAllProjectDetailsReportingList(startDate, endDate);
        return projectDetailsList;
    }

    public void updateProject(ProjectDetails projectDetails)
    {
        projectManager.updateProjectItem(projectDetails);
        setCurrentUser(projectDetails);
        notifyAll(NotificationType.PROJECT_UPDATE, projectDetails);
    }

    private void notifyAll(NotificationType notificationType, Object detailsObject)
    {
        UserItem currentUser = null;
        if (notificationType != NotificationType.RESET_PASSWORD && notificationType != NotificationType.FORGOT_PASSWORD
            && notificationType != NotificationType.USER_REGISTRAION
            && notificationType != NotificationType.USER_UPDATE) {
            currentUser = getFamstackApplicationConfiguration().getCurrentUser();
        }
        famstackNotificationServiceManager.notifyAll(notificationType, detailsObject, currentUser);
    }

    public String getNotifications(int userId)
    {

        LimitedQueue<NotificationItem> notificationItemList =
            famstackDesktopNotificationService.getNotificatioItems(userId);
        if (notificationItemList != null) {
            return FamstackUtils.getJsonFromObject(notificationItemList);
        }

        return "{}";
    }

    public void forgotPassword(String userName)
    {
        EmployeeDetails employeeDetails = userProfileManager.updateUserPasswordForReset(userName);

        if (employeeDetails != null) {
            notifyAll(NotificationType.FORGOT_PASSWORD, employeeDetails);
        }

    }

    public String getUserBillableProductiveJson()
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();
        Date endTime = getFamstackUserSessionConfiguration().getDashboardViewEndDate();
        Map<Object, UserWorkDetails> userWorkDetailsMap =
            famstackUserActivityManager.getUserBillableProductiveHours(startTime, endTime);

        Map<Integer, EmployeeDetails> empMap = FamstackApplicationConfiguration.userMap;

        for (int userId : empMap.keySet()) {
            UserWorkDetails userDetails = userWorkDetailsMap.get(userId);
            if (userWorkDetailsMap.get(userId) == null && empMap.get(userId).getRole() != UserRole.SUPERADMIN) {
                userDetails = new UserWorkDetails();
                userDetails.setBillableHours(0);
                userDetails.setCount(0);
                userDetails.setProductiveHours(0);
                userWorkDetailsMap.put(userId, userDetails);
            }

            if (userDetails != null) {
                userDetails.setUserId(empMap.get(userId).getFirstName());
            }
        }

        return FamstackUtils.getJsonFromObject(userWorkDetailsMap.values()).trim();
    }

    public String getProjectTypeJson()
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();
        Date endTime = getFamstackUserSessionConfiguration().getDashboardViewEndDate();

        List<ProjectStatusDetails> projectTypeCountList = projectManager.getProjectItemByTypeCount(startTime, endTime);

        return FamstackUtils.getJsonFromObject(projectTypeCountList).trim();
    }

    public String getTeamUtilizationJson()
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();
        Date endTime = getFamstackUserSessionConfiguration().getDashboardViewEndDate();

        List<TeamUtilizatioDetails> teamUtilizationDetailsList =
            projectManager.getTeamUtilizationJson(startTime, endTime);

        return FamstackUtils.getJsonFromObject(teamUtilizationDetailsList).trim();
    }

    public String getProjectCategoryJson()
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();
        Date endTime = getFamstackUserSessionConfiguration().getDashboardViewEndDate();

        List<ProjectCategoryDetails> projectCategoryDetailsList =
            projectManager.getProjectCategoryJson(startTime, endTime);

        return FamstackUtils.getJsonFromObject(projectCategoryDetailsList).trim();
    }

    public List<ClientProjectDetails> getClientProject()
    {
        Date startTime = getFamstackUserSessionConfiguration().getDashboardViewStartDate();
        Date endTime = getFamstackUserSessionConfiguration().getDashboardViewEndDate();

        List<ClientProjectDetails> clientProjectDetailsList = projectManager.getClientProject(startTime, endTime);

        return clientProjectDetailsList;
    }

    public String getUserStatusJson()
    {

        List<UserStatus> userStatusList = new ArrayList<UserStatus>();

        Map<Integer, EmployeeDetails> empMap = getFamstackApplicationConfiguration().getUserMap();
        for (Integer userId : empMap.keySet()) {
            String availableMessage = "Busy till";
            EmployeeDetails employeeDetails = empMap.get(userId);
            UserStatus userStatus = new UserStatus();
            userStatus.setStatus(employeeDetails.getCheckUserStatus());
            userStatus.setUserId(userId);
            Date userAvailableTime = employeeDetails.getUserAvailableTime();
            if (userAvailableTime != null) {
                availableMessage +=
                    " "
                        + (userAvailableTime.getHours() < 10 ? "0" + userAvailableTime.getHours() : userAvailableTime
                            .getHours())
                        + ":"
                        + (userAvailableTime.getMinutes() < 10 ? "0" + userAvailableTime.getMinutes()
                            : userAvailableTime.getMinutes());
            }
            if (employeeDetails.isLeave() != null && employeeDetails.isLeave() == LeaveType.FIRST) {
                availableMessage = "First Half of the Day Leave";
            } else if (employeeDetails.isLeave() != null && employeeDetails.isLeave() == LeaveType.SECOND) {
                availableMessage = "Second Half of the Day Leave";
            } else if (employeeDetails.isLeave() != null && employeeDetails.isLeave() == LeaveType.FULL) {
                availableMessage = "Full Day Leave";
            }

            userStatus.setUserAvailableMsg(availableMessage);
            userStatusList.add(userStatus);
        }

        return FamstackUtils.getJsonFromObject(userStatusList);
    }

    public void deleteGroup(int groupId)
    {
        groupMessageManager.deleteGroup(groupId);
    }

    public String getMessageNotifications(int userId)
    {
        List<GroupDetails> groupDetailsList = groupMessageManager.getGroupsForUser(userId);
        Map<Integer, ArrayList<GroupMessageDetails>> groupUserMessageMap = new HashMap<>();

        for (GroupDetails groupDetails : groupDetailsList) {
            LimitedQueue<GroupMessageDetails> groupMessageList =
                famstackDesktopNotificationService.getMessageNotificatioItems(userId, groupDetails.getGroupId());
            if (groupMessageList != null) {
                groupUserMessageMap.put(groupDetails.getGroupId(), new ArrayList<GroupMessageDetails>());
                for (GroupMessageDetails groupMessageDetails : groupMessageList) {
                    if (!groupMessageDetails.getRead()) {
                        groupUserMessageMap.get(groupDetails.getGroupId()).add(groupMessageDetails);
                    }
                }
            }
        }

        return FamstackUtils.getJsonFromObject(groupUserMessageMap);
    }

    public String createAccountConfig(String input1, String input2, String type, String action, int parentId, int id)
    {
        if ("ACCOUNT".equals(action)) {
            famstackAccountManager.createAccount(input1, input2, type, id);
        } else if ("TEAM".equals(action)) {
            famstackAccountManager.createTeam(input1, input2, parentId, id);
        } else if ("SUBTEAM".equals(action)) {
            famstackAccountManager.createSubTeam(input1, input2, parentId, id);
        } else if ("CLIENT".equals(action)) {
            famstackAccountManager.createClient(input1, input2, parentId, id);
        }
        return "success";
    }

    public void delteAccountConfig(String action, int id)
    {
        if ("ACCOUNT".equals(action)) {
            famstackAccountManager.deleteAccount(id);
        } else if ("TEAM".equals(action)) {
            famstackAccountManager.deleteTeam(id);
        } else if ("SUBTEAM".equals(action)) {
            famstackAccountManager.deleteSubTeam(id);
        } else if ("CLIENT".equals(action)) {
            famstackAccountManager.deleteClient(id);
        }

    }

    public void setConfiguration(String propertyName, String propertyValue)
    {
        ConfigurationSettingsItem configurationSettingsItem = new ConfigurationSettingsItem();
        configurationSettingsItem.setPropertyValue(propertyValue);
        configurationSettingsItem.setPropertyName(propertyName);
        famstackDataAccessObjectManager.saveOrUpdateItem(configurationSettingsItem);

        getFamstackApplicationConfiguration().initializeConfigurations();

    }

    public Set<Integer> getTaskOwners(Map<String, List<TaskDetails>> taskDetailsMap)
    {
        Set<Integer> taskOwners = new HashSet<>();

        if (taskDetailsMap != null) {
            for (String key : taskDetailsMap.keySet()) {
                List<TaskDetails> taskDetailsList = taskDetailsMap.get(key);

                if (taskDetailsList != null) {
                    for (TaskDetails taskDetails : taskDetailsList) {
                        TaskActivityDetails activityDetails = taskDetails.getTaskActivityDetails();

                        if (activityDetails != null) {
                            taskOwners.add(activityDetails.getUserId());
                        }
                    }
                }
            }
        }
        return taskOwners;
    }

    public void createNonBillableTask(int userId, String type, String startDateString, String endDateString,
        String comments)
    {
        Date startTime = null;
        int duration = 0;
        String taskName = "";
        if ("LEAVE".equalsIgnoreCase(type)) {
            startTime = DateUtils.tryParse(startDateString + " 08:00", DateUtils.DATE_TIME_FORMAT);
            switch (endDateString) {
                case "FIRST":
                    duration = 5;
                    break;
                case "SECOND":
                    startTime = DateUtils.getNextPreviousDate(DateTimePeriod.HOUR, startTime, 6);
                    duration = 7;
                    break;
                case "FULL":
                    duration = 12;
                    break;
            }

            taskName = endDateString + " half day ";

        } else if ("MEETING".equalsIgnoreCase(type)) {
            startTime = DateUtils.tryParse(startDateString, DateUtils.DATE_TIME_FORMAT);
            Date endTime = DateUtils.tryParse(endDateString, DateUtils.DATE_TIME_FORMAT);
            duration = (int) ((endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60));

            taskName = duration + " hours ";
        }

        taskName += type;

        famstackUserActivityManager.createNonBillableUserActivityItem(userId, startTime, 0, taskName, duration,
            UserTaskType.valueOf(type), ProjectType.NON_BILLABLE, comments);
    }
}
