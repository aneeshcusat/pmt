package com.famstack.projectscheduler.dashboard.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.employees.bean.GroupMessageDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.manager.FamstackGroupMessageManager;
import com.famstack.projectscheduler.manager.FamstackProjectCommentManager;
import com.famstack.projectscheduler.manager.FamstackProjectFileManager;
import com.famstack.projectscheduler.manager.FamstackProjectManager;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;
import com.famstack.projectscheduler.notification.FamstackNotificationServiceManager;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.notification.services.FamstackDesktopNotificationService;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.LimitedQueue;
import com.famstack.projectscheduler.util.StringUtils;
import com.famstack.projectscheduler.utils.FamstackUtils;

@Component
public class FamstackDashboardManager extends BaseFamstackService {

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

	public Map<String, Object> getUserData() {
		return null;
	}

	public Map<String, String> createUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<String, String>();
		userProfileManager.createUserItem(employeeDetails);
		notifyAll(NotificationType.USER_REGISTRAION, employeeDetails);
		return errorMap;
	}

	public Map<String, String> updateUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<String, String>();
		userProfileManager.updateUserItem(employeeDetails);
		notifyAll(NotificationType.USER_UPDATE, employeeDetails);
		return errorMap;
	}

	private Map<String, String> valiateUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<>();
		if (!StringUtils.isNotBlank(employeeDetails.getFirstName())
				|| !StringUtils.isNotBlank(employeeDetails.getConfirmPassword())
				|| !StringUtils.isNotBlank(employeeDetails.getEmail())) {
			errorMap.put("invalidInput", "required inputs are missing");
		}
		return errorMap;
	}

	public UserItem getUser(int userId) {
		return userProfileManager.getUserItemById(userId);
	}

	public String getEmployeeDetails(int userId) {
		EmployeeDetails employeeDetails = userProfileManager.getEmployee(userId);
		return FamstackUtils.getJsonFromObject(employeeDetails);
	}

	public void deleteUser(int userId) {
		userProfileManager.deleteUserItem(userId);

	}

	public void createProject(ProjectDetails projectDetails) {
		projectManager.createProjectItem(projectDetails);
		setCurrentUser(projectDetails);
		notifyAll(NotificationType.PROJECT_CREATE, projectDetails);
	}

	public List<ProjectDetails> getProjectsDataList() {
		Date startTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(),
				getFamstackUserSessionConfiguration().getDashboardViewDays());
		List<ProjectDetails> projectDetailsList = projectManager.getAllProjectDetailsList(startTime);
		return projectDetailsList;
	}

	public List<ProjectDetails> getProjects() {
		Date startTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(),
				getFamstackUserSessionConfiguration().getProjectViewLimit());
		List<ProjectDetails> projectDetailsList = projectManager.getAllProjectDetailsList(startTime);
		return projectDetailsList;
	}

	public void deleteProject(int projectId) {
		ProjectDetails projectDetails = projectManager.getProjectDetails(projectId);
		projectManager.deleteProjectItem(projectId);
		setCurrentUser(projectDetails);
		notifyAll(NotificationType.PROJECT_DELETE, projectDetails);
	}

	public void createComment(String projectComments, int projectId) {
		projectCommentManager.createProjectCommentItem(projectComments, projectId);
	}

	public ProjectDetails getProjectDetails(int projectId, HttpServletRequest request) {
		ProjectDetails projectDetails = projectManager.getProjectDetails(projectId);
		List<String> filesNames = famstackProjectFileManager.getProjectFiles(projectDetails.getCode(), request);
		projectDetails.setFilesNames(filesNames);
		return projectDetails;
	}

	public String getProjectDetailsJson(int projectId) {
		ProjectDetails projectDetails = projectManager.getProjectDetails(projectId);
		return FamstackUtils.getJsonFromObject(projectDetails);
	}

	public List<GroupDetails> getAllGroups(int userId) {
		List<GroupDetails> groupDetailsList = groupMessageManager.getGroupsForUser(userId);
		Collections.sort(groupDetailsList, new Comparator<GroupDetails>() {
			@Override
			public int compare(GroupDetails groupDetailsOne, GroupDetails groupDetailsTwo) {
				return groupDetailsOne.getCreatedDate().getTime() > groupDetailsTwo.getCreatedDate().getTime() ? 1 : -1;
			}
		});
		return groupDetailsList;
	}

	public void createTask(TaskDetails taskDetails) {
		projectManager.createProjectTask(taskDetails);
		triggerTaskNotification(NotificationType.TASK_CREATED, taskDetails);
	}

	private void triggerTaskNotification(NotificationType type, TaskDetails taskDetails) {
		ProjectDetails projectDetails = projectManager.getProjectDetails(taskDetails.getProjectId());
		Map<String, Object> projectTaskMap = new HashMap<>();
		projectTaskMap.put("taskDetails", taskDetails);
		projectTaskMap.put("projectDetails", projectDetails);
		notifyAll(type, projectTaskMap);

	}

	private void setCurrentUser(ProjectDetails projectDetails) {
		EmployeeDetails employeeDetails = userProfileManager
				.mapEmployeeDetails(getFamstackApplicationConfiguration().getCurrentUser());
		projectDetails.setEmployeeDetails(employeeDetails);
	}

	public void uploadProjectFile(MultipartFile file, String projectCode, HttpServletRequest request) {
		famstackProjectFileManager.uploadFile(file, projectCode, request);
	}

	public void deleteProjectFile(String fileName, String projectCode, HttpServletRequest request) {
		famstackProjectFileManager.deleteFile(fileName, projectCode, request);
	}

	public File getProjectFile(String fileName, String projectCode, HttpServletRequest request) {
		return famstackProjectFileManager.getFile(fileName, projectCode, request);

	}

	public void updateTask(TaskDetails taskDetails) {
		projectManager.updateProjectTask(taskDetails);
		if (taskDetails.getAssignee() != 0) {
			triggerTaskNotification(NotificationType.TASK_ASSIGNED, taskDetails);
		} else {
			triggerTaskNotification(NotificationType.TASK_UPDATED, taskDetails);
		}
	}

	public void deleteProjectTask(int taskId, int projectId) {
		TaskDetails taskDetails = projectManager.getProjectTaskById(taskId);
		projectManager.deleteProjectTask(taskId, projectId);
		taskDetails.setProjectId(projectId);
		triggerTaskNotification(NotificationType.TASK_DELETED, taskDetails);
	}

	public String getUserTaskActivityJson() {
		return projectManager.getUserTaskActivityJson();
	}

	public void createGroup(GroupDetails groupDetails) {
		groupMessageManager.createGroupItem(groupDetails);

	}

	public void updateGroup(GroupDetails groupDetails) {
		groupMessageManager.updateGroupItem(groupDetails);

	}

	public String getGroup(int groupId) {
		GroupDetails groupDetails = groupMessageManager.getGroupDetails(groupId);
		return FamstackUtils.getJsonFromObject(groupDetails);
	}

	public void sendMessage(int groupId, String message) {
		GroupMessageDetails groupMessageDetails = new GroupMessageDetails();
		groupMessageDetails.setGroup(groupId);
		groupMessageDetails.setDescription(message);
		groupMessageManager.createGroupMessageItem(groupMessageDetails);
	}

	public boolean isValidKeyForUserReset(String key, int userId) {
		return userProfileManager.isValidUserResetKey(key, userId);
	}

	public boolean changePassword(String userName, String oldPassword, String password) {
		boolean status = userProfileManager.changePassword(userName, oldPassword, password);
		Integer userId = getFamstackApplicationConfiguration().getUserIdMap().get(userName);
		EmployeeDetails employeeDetails = null;

		if (userId != null) {
			employeeDetails = getFamstackApplicationConfiguration().getUserMap().get(userId);
		}

		notifyAll(NotificationType.RESET_PASSWORD, employeeDetails);
		return status;
	}

	public String getMessageAfter(int groupId, int messageId) {
		List<GroupMessageDetails> groupMessageDetails = groupMessageManager.getGroupMessages(groupId, messageId);
		return FamstackUtils.getJsonFromObject(groupMessageDetails);
	}

	public Map<String, ArrayList<TaskDetails>> getProjectTasksDataList(int userId) {
		return projectManager.getProjectTasksDataList(userId);
	}

	public String getProjectTasksDataListJson(int userId) {
		Map<String, ArrayList<TaskDetails>> taskMap = projectManager.getProjectTasksDataList(userId);
		return FamstackUtils.getJsonFromObject(taskMap);
	}

	public void updateTaskStatus(int taskId, int taskActivityId, TaskStatus taskStatus, String comments) {
		projectManager.updateTaskStatus(taskId, taskActivityId, taskStatus, comments);
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

	public List<ProjectDetails> getProjectDetails(ProjectStatus projectStatus) {
		Date startTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(),
				getFamstackUserSessionConfiguration().getDashboardViewDays());
		if (projectStatus == ProjectStatus.MISSED) {
			return projectManager.getAllMissedTimeLineProjectDetails(startTime);
		}
		return projectManager.getAllProjectDetailsList(projectStatus, startTime);
	}

	public Map<String, Long> getProjectsCounts() {
		Date startTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(),
				getFamstackUserSessionConfiguration().getDashboardViewDays());
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

	public String getAjaxFullcalendar(String startDate, String endDate) {
		return projectManager.getUserTaskActivityForCalenderJson(startDate, endDate);
	}

	public List<AccountDetails> getAccountDataList() {
		return famstackAccountManager.getAllAccountDetails();
	}

	public String getProjectNameJson(String query) {
		return projectManager.getProjectNameJson(query);
	}

	public List<ProjectDetails> getProjectsReporingDataList(Date startDate, Date endDate) {
		List<ProjectDetails> projectDetailsList = projectManager.getAllProjectDetailsList(startDate, endDate);
		return projectDetailsList;
	}

	public void updateProject(ProjectDetails projectDetails) {
		projectManager.updateProjectItem(projectDetails);
		setCurrentUser(projectDetails);
		notifyAll(NotificationType.PROJECT_UPDATE, projectDetails);
	}

	private void notifyAll(NotificationType notificationType, Object detailsObject) {
		UserItem currentUser = getFamstackApplicationConfiguration().getCurrentUser();
		famstackNotificationServiceManager.notifyAll(notificationType, detailsObject, currentUser);
	}

	public String getNotifications(int userId) {

		LimitedQueue<NotificationItem> notificationItemList = famstackDesktopNotificationService
				.getNotificatioItems(userId);
		if (notificationItemList != null) {
			return FamstackUtils.getJsonFromObject(notificationItemList);
		}

		return "{}";
	}

	public void forgotPassword(String userName) {
		EmployeeDetails employeeDetails = userProfileManager.updateUserPasswordForReset(userName);

		if (employeeDetails != null) {
			notifyAll(NotificationType.FORGOT_PASSWORD, employeeDetails);
		}

	}
}
