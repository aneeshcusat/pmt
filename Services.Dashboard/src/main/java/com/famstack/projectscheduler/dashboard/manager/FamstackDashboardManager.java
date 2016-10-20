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

	public Map<String, Object> getUserData() {
		return null;
	}

	public Map<String, String> createUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<String, String>();
		userProfileManager.createUserItem(employeeDetails);
		famstackNotificationServiceManager.notifyAll(NotificationType.USER_REGISTRAION, 0, employeeDetails);
		return errorMap;
	}

	public Map<String, String> updateUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<String, String>();
		userProfileManager.updateUserItem(employeeDetails);
		famstackNotificationServiceManager.notifyAll(NotificationType.USER_UPDATE, 0, employeeDetails);
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

	}

	public List<ProjectDetails> getProjectsDataList() {
		List<ProjectDetails> projectDetailsList = projectManager.getAllProjectDetailsList();
		return projectDetailsList;
	}

	public void deleteProject(int projectId) {
		projectManager.deleteProjectItem(projectId);
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

	}

	public void deleteProjectTask(int taskId, int projectId) {
		projectManager.deleteProjectTask(taskId, projectId);
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
		famstackNotificationServiceManager.notifyAll(NotificationType.USER_UPDATE, 0, userName);
		return status;
	}

	public String getMessageAfter(int groupId, int messageId) {
		List<GroupMessageDetails> groupMessageDetails = groupMessageManager.getGroupMessages(groupId, messageId);
		return FamstackUtils.getJsonFromObject(groupMessageDetails);
	}

	public Map<String, ArrayList<TaskDetails>> getProjectTasksDataList(int userId) {
		return projectManager.getProjectTasksDataList(userId);
	}

	public void updateTaskStatus(int taskId, int taskActivityId, TaskStatus taskStatus, String comments) {
		projectManager.updateTaskStatus(taskId, taskActivityId, taskStatus, comments);

	}

	public List<ProjectDetails> getUnassignedProjects(ProjectStatus unassigned) {
		return projectManager.getAllProjectDetailsList(unassigned);
	}

	public Map<String, Long> getProjectsCounts() {
		Map<String, Long> projectCounts = new HashMap<>();
		projectCounts.put(ProjectStatus.ASSIGNED.value(),
				projectManager.getAllProjectDetailsCount(ProjectStatus.ASSIGNED));
		projectCounts.put(ProjectStatus.UNASSIGNED.value(),
				projectManager.getAllProjectDetailsCount(ProjectStatus.UNASSIGNED));
		projectCounts.put(ProjectStatus.INPROGRESS.value(),
				projectManager.getAllProjectDetailsCount(ProjectStatus.INPROGRESS));
		projectCounts.put(ProjectStatus.COMPLETED.value(),
				projectManager.getAllProjectDetailsCount(ProjectStatus.COMPLETED));
		projectCounts.put(ProjectStatus.NEW.value(), projectManager.getAllProjectDetailsCount(ProjectStatus.NEW));
		projectCounts.put("MISSED", projectManager.getAllMissedTimeLineProjectDetailsCount());

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
	}
}
