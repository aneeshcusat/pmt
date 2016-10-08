package com.famstack.projectscheduler.dashboard.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.employees.bean.GroupMessageDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.manager.FamstackGroupMessageManager;
import com.famstack.projectscheduler.manager.FamstackProjectCommentManager;
import com.famstack.projectscheduler.manager.FamstackProjectFileManager;
import com.famstack.projectscheduler.manager.FamstackProjectManager;
import com.famstack.projectscheduler.manager.FamstackProjectTaskManager;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;
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
	FamstackProjectTaskManager famstackProjectTaskManager;

	@Resource
	FamstackProjectCommentManager projectCommentManager;

	@Resource
	FamstackGroupMessageManager groupMessageManager;

	@Resource
	FamstackProjectFileManager famstackProjectFileManager;

	public Map<String, Object> getUserData() {
		return null;
	}

	public Map<String, String> createUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<String, String>();
		userProfileManager.createUserItem(employeeDetails);
		getFamstackNotificationServiceManager().notifyAll(NotificationType.USER_REGISTRAION, employeeDetails);
		return errorMap;
	}

	public Map<String, String> updateUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<String, String>();
		userProfileManager.updateUserItem(employeeDetails);
		getFamstackNotificationServiceManager().notifyAll(NotificationType.USER_UPDATE, employeeDetails);
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

	public List<GroupDetails> getAllGroups(int userId) {
		List<GroupDetails> groupDetailsList = groupMessageManager.getGroupsForUser(userId);
		return groupDetailsList;
	}

	public void createTask(TaskDetails taskDetails) {
		ProjectItem projectItem = projectManager.getProjectItemById(taskDetails.getProjectId());
		famstackProjectTaskManager.createTaskItem(taskDetails, projectItem);

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
		ProjectItem projectItem = projectManager.getProjectItemById(taskDetails.getProjectId());
		famstackProjectTaskManager.updateTask(taskDetails, projectItem);

	}

	public void deleteProjectTask(int taskId) {
		famstackProjectTaskManager.deleteTaskItem(taskId);
	}

	public void createGroup(GroupDetails groupDetails) {
		groupMessageManager.createGroupItem(groupDetails);

	}

	public void sendMessage(int groupId, String message) {
		GroupMessageDetails groupMessageDetails = new GroupMessageDetails();
		groupMessageDetails.setGroup(groupId);
		groupMessageDetails.setDescription(message);
		groupMessageManager.createGroupMessageItem(groupMessageDetails);

	}

	public String getUserTaskActivityJson() {
		return famstackProjectTaskManager.getUserTaskActivityJson();
	}

	public boolean isValidKeyForUserReset(String key, int userId) {
		return userProfileManager.isValidUserResetKey(key, userId);
	}

	public boolean changePassword(String userName, String oldPassword, String password) {
		boolean status = userProfileManager.changePassword(userName, oldPassword, password);
		getFamstackNotificationServiceManager().notifyAll(NotificationType.USER_UPDATE, userName);
		return status;
	}
}
