package com.famstack.projectscheduler.dashboard.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.famstack.email.FamstackEmailSender;
import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.contants.FamstackConstants;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.ReportType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.dashboard.bean.ClientProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.DashBoardProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.DashboardUtilizationDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectCategoryDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectStatusDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.bean.TeamUtilizatioDetails;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ConfigurationSettingsItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ApplicationDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeBWDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.employees.bean.GroupMessageDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.RecurringProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.employees.bean.UserStatus;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.manager.FamstackGroupMessageManager;
import com.famstack.projectscheduler.manager.FamstackProjectCommentManager;
import com.famstack.projectscheduler.manager.FamstackProjectFileManager;
import com.famstack.projectscheduler.manager.FamstackProjectManager;
import com.famstack.projectscheduler.manager.FamstackProjectTaskManager;
import com.famstack.projectscheduler.manager.FamstackUserActivityManager;
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

	@Resource
	FamstackUserActivityManager famstackUserActivityManager;

	@Resource
	FamstackProjectTaskManager famstackProjectTaskManager;

	@Resource
	FamstackEmailSender famstackEmailSender;

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
				|| !StringUtils
						.isNotBlank(employeeDetails.getConfirmPassword())
				|| !StringUtils.isNotBlank(employeeDetails.getEmail())) {
			errorMap.put("invalidInput", "required inputs are missing");
		}
		return errorMap;
	}

	public UserItem getUser(int userId) {
		return userProfileManager.getUserItemById(userId);
	}

	public String getEmployeeDetails(int userId) {
		EmployeeDetails employeeDetails = userProfileManager
				.getEmployee(userId);
		return FamstackUtils.getJsonFromObject(employeeDetails);
	}

	public void deleteUser(int userId) {
		userProfileManager.deleteUserItem(userId);

	}

	public int createProject(ProjectDetails projectDetails) {
		int projectId = projectManager.createProjectItem(projectDetails);
		setCurrentUser(projectDetails);
		notifyAll(NotificationType.PROJECT_CREATE, projectDetails);
		return projectId;
	}

	public List<ProjectDetails> getProjectsDataList() {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();
		List<ProjectDetails> projectDetailsList = projectManager
				.getAllProjectDetailsList(startTime, true);
		return projectDetailsList;
	}

	public List<ProjectDetails> getProjects(boolean isFullLoad) {
		Date startTime = DateUtils.getNextPreviousDate(
				DateTimePeriod.DAY_START, new Date(),
				getFamstackUserSessionConfiguration().getProjectViewLimit());
		List<ProjectDetails> projectDetailsList = projectManager
				.getAllProjectDetailsList(startTime, isFullLoad);

		sortProjectData(projectDetailsList);
		return projectDetailsList;
	}

	public List<ProjectDetails> getLatestProjects(Date startDate, Date endDate,
			Boolean includeArchive) {
		List<ProjectDetails> projectDetailsList = projectManager
				.getPrimaryProjectsDetailList(startDate, endDate,
						includeArchive);

		// sortProjectData(projectDetailsList);
		return projectDetailsList;
	}

	public List<ProjectDetails> searchProjectDetails(String searchString,
			Boolean includeArchive) {
		List<ProjectDetails> projectDetailsList = projectManager
				.searchProjectDetails(searchString, includeArchive);

		return projectDetailsList;
	}

	public List<ProjectDetails> loadDuplicateProjectsJon(int projectId,
			String projectCode, Boolean includeArchive) {
		List<ProjectDetails> projectDetailsList = projectManager
				.loadDuplicateProjects(projectId, projectCode, includeArchive);
		return projectDetailsList;

	}

	private void sortProjectData(List<ProjectDetails> projectDetailsList) {
		Collections.sort(projectDetailsList, new Comparator<ProjectDetails>() {
			@Override
			public int compare(ProjectDetails projectDetails2,
					ProjectDetails projectDetails1) {
				Date date1 = DateUtils.tryParse(
						projectDetails1.getCompletionTime(),
						DateUtils.DATE_TIME_FORMAT);
				Date date2 = DateUtils.tryParse(
						projectDetails2.getCompletionTime(),
						DateUtils.DATE_TIME_FORMAT);

				if (date1.before(date2)) {
					return -1;
				} else if (date1.after(date2)) {
					return 1;
				}
				return 0;
			}
		});
	}

	public void deleteProject(int projectId) {
		ProjectDetails projectDetails = projectManager
				.getProjectDetails(projectId);
		projectManager.deleteProjectItem(projectId);
		setCurrentUser(projectDetails);
		notifyAll(NotificationType.PROJECT_DELETE, projectDetails);
	}

	public void archiveProject(int projectId) {
		projectManager.archiveProjectItem(projectId);

	}

	public void createComment(String projectComments, int projectId) {
		projectCommentManager.createProjectCommentItem(projectComments,
				projectId);

		ProjectDetails projectDetails = projectManager
				.getProjectDetails(projectId);
		setCurrentUser(projectDetails);
		notifyAll(NotificationType.PROJECT_COMMENT_ADDED, projectDetails);
	}

	public ProjectDetails getProjectDetails(int projectId,
			HttpServletRequest request) {
		ProjectDetails projectDetails = projectManager
				.getProjectDetails(projectId);
		if (projectDetails != null) {
			List<String> filesNames = famstackProjectFileManager
					.getProjectFiles("" + projectDetails.getId(), request);
			List<String> completedFilesNames = famstackProjectFileManager
					.getProjectFiles(projectDetails.getId() + "-completed",
							request);
			projectDetails.setFilesNames(filesNames);
			projectDetails.setCompletedFilesNames(completedFilesNames);
		}
		return projectDetails;
	}

	public String getProjectDetailsJson(int projectId) {
		ProjectDetails projectDetails = projectManager
				.getProjectDetails(projectId);
		return FamstackUtils.getJsonFromObject(projectDetails);
	}

	public ProjectDetails getProjectDetais(int projectId) {
		return projectManager.getProjectDetails(projectId);
	}

	public List<GroupDetails> getAllGroups(int userId) {
		List<GroupDetails> groupDetailsList = groupMessageManager
				.getGroupsForUser(userId);
		Collections.sort(groupDetailsList, new Comparator<GroupDetails>() {
			@Override
			public int compare(GroupDetails groupDetailsOne,
					GroupDetails groupDetailsTwo) {
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

	public void createTask(TaskDetails taskDetails) {
		projectManager.createProjectTask(taskDetails);

		if (taskDetails.getAssignee() != 0) {
			triggerTaskNotification(NotificationType.TASK_CREATED_ASSIGNED,
					taskDetails);
		} else {
			triggerTaskNotification(NotificationType.TASK_CREATED, taskDetails);
		}

	}

	public void createExtraTimeTask(TaskDetails taskDetails) {
		projectManager.createProjectExtraTimeTask(taskDetails);

	}

	private void triggerTaskNotification(NotificationType type,
			TaskDetails taskDetails) {
		ProjectDetails projectDetails = projectManager
				.getProjectDetails(taskDetails.getProjectId());
		Map<String, Object> projectTaskMap = new HashMap<>();
		projectTaskMap.put("taskDetails", taskDetails);
		projectTaskMap.put("projectDetails", projectDetails);
		notifyAll(type, projectTaskMap);

	}

	private void setCurrentUser(ProjectDetails projectDetails) {
		EmployeeDetails employeeDetails = userProfileManager
				.mapEmployeeDetails(getFamstackApplicationConfiguration()
						.getCurrentUser());
		projectDetails.setEmployeeDetails(employeeDetails);
	}

	public void uploadProjectFile(MultipartFile file, String projectId,
			HttpServletRequest request) {
		famstackProjectFileManager.uploadFile(file, projectId, request);
		projectManager.updateProjectActivityItem(Integer.parseInt(projectId),
				ProjectActivityType.FILE_UPLOADED, file.getName());
	}

	public void deleteProjectFile(String fileName, String projectCode,
			HttpServletRequest request) {
		famstackProjectFileManager.deleteFile(fileName, projectCode, request);
	}

	public File getProjectFile(String fileName, String projectCode,
			HttpServletRequest request) {
		return famstackProjectFileManager.getFile(fileName, projectCode,
				request);

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
		if (taskDetails != null) {
			taskDetails.setProjectId(projectId);
			triggerTaskNotification(NotificationType.TASK_DELETED, taskDetails);
		}
	}

	public String getUserTaskActivityJson(Integer userId, int dayfilter) {
		return projectManager.getUserTaskActivityJson(userId, dayfilter);
	}

	@Deprecated
	public Map getUserTaskActivity(Integer userId, String monthFilter) {
		List<TaskActivityDetails> taskActivities = projectManager
				.getUserTaskActivity(userId, monthFilter);
		Map<String, List<TaskActivityDetails>> taskActivitiesMap = new HashMap<>();

		if (taskActivities != null) {
			taskActivitiesMap
					.put("TODAY", new ArrayList<TaskActivityDetails>());
			taskActivitiesMap.put("UPCOMING",
					new ArrayList<TaskActivityDetails>());
			taskActivitiesMap.put("PAST", new ArrayList<TaskActivityDetails>());

			String todaysDate = DateUtils.format(new Date(),
					DateUtils.DATE_FORMAT);

			for (TaskActivityDetails taskActivityDetails : taskActivities) {

				String taskTimeString = "";
				if (taskActivityDetails.getActualEndTime() != null) {

					Integer taskActActivityDuration = taskActivityDetails
							.getDurationInMinutes();
					taskActActivityDuration = taskActActivityDuration == null ? 0
							: taskActActivityDuration;
					taskTimeString = " - "
							+ String.format("%02d:%02d",
									taskActActivityDuration / 60,
									taskActActivityDuration % 60) + " Hrs";
				}

				String titlePrefix = "[B" + taskTimeString + "] ";
				if (taskActivityDetails.getProjectType() == ProjectType.NON_BILLABLE) {
					titlePrefix = "[NB" + taskTimeString + "] ";
				}

				taskActivityDetails.setTaskName(titlePrefix
						+ taskActivityDetails.getTaskName());

				if (todaysDate.compareTo(taskActivityDetails.getDateId()) < 0) {
					taskActivitiesMap.get("UPCOMING").add(taskActivityDetails);
				} else if (todaysDate
						.compareTo(taskActivityDetails.getDateId()) > 0) {
					taskActivitiesMap.get("PAST").add(taskActivityDetails);
				} else {
					taskActivitiesMap.get("TODAY").add(taskActivityDetails);
				}
			}
		}
		sortTaskActivityDeailsList(taskActivitiesMap);
		return taskActivitiesMap;
	}

	public void createGroup(GroupDetails groupDetails) {
		groupMessageManager.createGroupItem(groupDetails);

	}

	public void updateGroup(GroupDetails groupDetails) {
		groupMessageManager.updateGroupItem(groupDetails);

	}

	public String getGroup(int groupId) {
		GroupDetails groupDetails = groupMessageManager
				.getGroupDetails(groupId);
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

	public boolean changePassword(String userName, String oldPassword,
			String password) {
		boolean status = userProfileManager.changePassword(userName,
				oldPassword, password);
		Integer userId = FamstackApplicationConfiguration.getUserIdMap().get(
				userName);
		EmployeeDetails employeeDetails = null;

		if (userId != null) {
			employeeDetails = getFamstackApplicationConfiguration()
					.getUserMap().get(userId);
		}

		notifyAll(NotificationType.RESET_PASSWORD, employeeDetails);
		return status;
	}

	public String getMessageAfter(int groupId, int messageId) {
		List<GroupMessageDetails> groupMessageDetails = groupMessageManager
				.getGroupMessages(groupId, messageId);
		return FamstackUtils.getJsonFromObject(groupMessageDetails);
	}

	public Map<String, List<TaskDetails>> getProjectTasksDataList(
			Integer userId, int dayfilter) {
		Map<String, List<TaskDetails>> taskDetailsMap = projectManager
				.getProjectTasksDataList(userId, dayfilter);
		sortTaskDeailsList(taskDetailsMap);
		return taskDetailsMap;
	}

	private void sortTaskDeailsList(
			Map<String, List<TaskDetails>> taskDetailsMap) {
		for (String key : taskDetailsMap.keySet()) {

			List<TaskDetails> taskDeatilsList = taskDetailsMap.get(key);
			if (taskDeatilsList != null) {
				Collections.sort(taskDeatilsList,
						new Comparator<TaskDetails>() {
							@Override
							public int compare(TaskDetails taskDetails1,
									TaskDetails taskDetails2) {

								Date date1 = DateUtils.tryParse(
										taskDetails1.getStartTime(),
										DateUtils.DATE_TIME_FORMAT);
								Date date2 = DateUtils.tryParse(
										taskDetails2.getStartTime(),
										DateUtils.DATE_TIME_FORMAT);

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

	private void sortTaskActivityDeailsList(
			Map<String, List<TaskActivityDetails>> taskActivityDetailsMap) {
		for (String key : taskActivityDetailsMap.keySet()) {

			List<TaskActivityDetails> taskActivityDetails = taskActivityDetailsMap
					.get(key);
			if (taskActivityDetails != null) {
				Collections.sort(taskActivityDetails,
						new Comparator<TaskActivityDetails>() {
							@Override
							public int compare(
									TaskActivityDetails taskActiryDetails1,
									TaskActivityDetails taskActiryDetails2) {

								Date date1 = taskActiryDetails1.getStartTime();
								Date date2 = taskActiryDetails2.getStartTime();

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

	public String getProjectTasksDataListJson(int userId) {
		Map<String, List<TaskDetails>> taskMap = projectManager
				.getProjectTasksDataList(userId, 15);
		return FamstackUtils.getJsonFromObject(taskMap);
	}

	public void reAssignTask(int taskId, int newUserId, int taskActivityId,
			TaskStatus taskStatus) {
		TaskDetails taskDetails = projectManager.getProjectTaskById(taskId);
		projectManager.reAssignTask(taskDetails, newUserId, taskActivityId,
				taskStatus);
		triggerTaskNotification(NotificationType.TASK_RE_ASSIGNED, taskDetails);

	}

	public String playTask(int taskId, int taskActivityId) {
		TaskActivityDetails taskActivityDetails = projectManager.playTask(
				taskId, taskActivityId);
		JSONObject jsonTaskActivity = new JSONObject();
		if (taskActivityDetails != null) {
			jsonTaskActivity.put("taskActivityId",
					taskActivityDetails.getTaskActivityId());
			jsonTaskActivity.put("startHour",
					taskActivityDetails.getTimeTakenToCompleteHour());
			jsonTaskActivity.put("startMins",
					taskActivityDetails.getTimeTakenToCompleteMinute());
			jsonTaskActivity.put("startSecs",
					taskActivityDetails.getTimeTakenToCompleteSecond());
			
			jsonTaskActivity.put("actualStartTime",
					taskActivityDetails.getActualStartTime());
		} else {
			jsonTaskActivity.put("ERROR", "Unable to pause task");
		}
		return jsonTaskActivity.toString();

	}

	public void pauseTask(int taskId) {
		TaskDetails taskDetails = projectManager.getProjectTaskById(taskId);
		projectManager.pauseTask(taskDetails);
	}

	public void updateTaskStatus(int taskId, TaskStatus taskStatus,
			String comments, String adjustStartTime, String adjustCompletionTime) {
		Date adjustStartTimeDate = null;
		if (StringUtils.isNotBlank(adjustStartTime)) {
			adjustStartTimeDate = DateUtils.tryParse(adjustStartTime,
					DateUtils.DATE_TIME_FORMAT);
		}

		Date adjustCompletionTimeDate = null;
		if (StringUtils.isNotBlank(adjustCompletionTime)) {
			adjustCompletionTimeDate = DateUtils.tryParse(adjustCompletionTime,
					DateUtils.DATE_TIME_FORMAT);
		}

		projectManager.updateTaskStatus(taskId, taskStatus, comments,
				adjustStartTimeDate, adjustCompletionTimeDate);
		TaskDetails taskDetails = projectManager.getProjectTaskById(taskId);
		switch (taskStatus) {
		case ASSIGNED:
			triggerTaskNotification(NotificationType.TASK_ASSIGNED, taskDetails);
			break;
		case CLOSED:
			triggerTaskNotification(NotificationType.TASK_CLOSED, taskDetails);
			break;
		case COMPLETED:
			triggerTaskNotification(NotificationType.TASK_COMPLETED,
					taskDetails);
			break;
		case INPROGRESS:
			triggerTaskNotification(NotificationType.TASK_INPROGRESS,
					taskDetails);
			break;
		case NEW:
			triggerTaskNotification(NotificationType.TASK_CREATED, taskDetails);
			break;
		default:
			break;
		}

	}

	public List<ProjectDetails> getAllUnAssignedProjects() {
		Date startDate = DateUtils.getNextPreviousDate(DateTimePeriod.YEAR,
				new Date(), -1);
		return projectManager.getAllProjectDetailsList(
				ProjectStatus.UNASSIGNED, startDate);
	}

	public List<ProjectDetails> getProjectDetails(ProjectStatus projectStatus) {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();

		if (projectStatus == ProjectStatus.MISSED) {
			return projectManager.getMissedTimeLineProjectDetails(startTime);
		}
		return projectManager
				.getAllProjectDetailsList(projectStatus, startTime);
	}

	public Map<String, Long> getProjectsCounts() {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();
		Map<String, Long> projectCounts = new HashMap<>();
		projectCounts.put(ProjectStatus.ASSIGNED.value(), projectManager
				.getAllProjectDetailsCount(ProjectStatus.ASSIGNED, startTime));
		projectCounts
				.put(ProjectStatus.UNASSIGNED.value(), projectManager
						.getAllProjectDetailsCount(ProjectStatus.UNASSIGNED,
								startTime));
		projectCounts
				.put(ProjectStatus.INPROGRESS.value(), projectManager
						.getAllProjectDetailsCount(ProjectStatus.INPROGRESS,
								startTime));
		projectCounts.put(ProjectStatus.COMPLETED.value(), projectManager
				.getAllProjectDetailsCount(ProjectStatus.COMPLETED, startTime));
		projectCounts.put(ProjectStatus.NEW.value(), projectManager
				.getAllProjectDetailsCount(ProjectStatus.NEW, startTime));
		projectCounts.put("MISSED", projectManager
				.getAllMissedTimeLineProjectDetailsCount(startTime));

		return projectCounts;
	}

	public String getAjaxFullcalendar(String startDate, String endDate,
			int userId) {
		return projectManager.getUserTaskActivityForCalenderJson(startDate,
				endDate, userId);
	}

	public String getEmpUtlAjaxFullcalendar(String startDate, String endDate,
			String userGroupId) {
		return projectManager.getUserTaskActivityForEmpUtlCalenderJson(
				startDate, endDate, userGroupId);
	}

	public String getEmpBWAjaxFullcalendar(String startDateString,
			String endDateString, String userGroupId) {
		Date startDate = DateUtils.tryParse(startDateString,
				DateUtils.DATE_FORMAT_CALENDER);
		Date endDate = DateUtils.tryParse(endDateString,
				DateUtils.DATE_FORMAT_CALENDER);

		List<EmployeeBWDetails> employeeBWCalenderList = userProfileManager
				.getEmployeesOnLeaveAndFreeDateToDate(userGroupId, startDate,
						endDate);
		JSONArray jsonArray = new JSONArray();

		if (employeeBWCalenderList != null) {

			for (EmployeeBWDetails employeeBWDetails : employeeBWCalenderList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("title", employeeBWDetails.getFirstName());
				jsonObject.put("color", "#EF8175");
				jsonObject.put("start", employeeBWDetails.getDateString());
				jsonObject.put("textColor", "#fff");
				jsonArray.put(jsonObject);
			}
		}
		return jsonArray.toString();
	}

	public List<AccountDetails> getAccountDataList() {
		return famstackAccountManager.getAllAccountDetails();
	}

	public List<ApplicationDetails> getApplicationDetails() {
		// return famstackAccountManager.getAllApplicationManager();
		return null;
	}

	public String getProjectNameJson(String query) {
		return projectManager.getProjectNameJson(query);
	}

	public String searchForProjectNamesCodePoIdJson(String query) {
		return projectManager.searchForProjectNamesCodePoIdJson(query);
	}

	public List<ProjectDetails> getAllProjectDetailsList(Date startDate,
			Date endDate) {
		List<ProjectDetails> projectDetailsList = projectManager
				.getAllProjectDetailsList(startDate, endDate);
		return projectDetailsList;
	}

	public List<DashBoardProjectDetails> getDashboardProjectData(
			Date startDate, Date endDate, String groupId) {
		List<DashBoardProjectDetails> dashBoardProjectDetailsList = projectManager
				.getDashboardProjectData(startDate, endDate, groupId);
		return dashBoardProjectDetailsList;
	}

	public String dashboardOverAllUtilizationPercentage(Date startDate,
			Date endDate, String userGroupId, String accountId, String teamId,
			String subTeamId, String userId) {
		Double billableMins = 0.0;
		Double nonBillableMins = 0.0;
		List<DashboardUtilizationDetails> dashboardOverAllutilizationList = dashboarAllUtilizationList(
				startDate, endDate, userGroupId, accountId, teamId, subTeamId,
				userId, false, false);
		for (DashboardUtilizationDetails dashboardOverAllutilization : dashboardOverAllutilizationList) {
			billableMins += dashboardOverAllutilization.getBillableMins();
			nonBillableMins += dashboardOverAllutilization.getNonBillableMins();
		}

		int billablePercentage = 0;
		int nonBillablePercentage = 0;
		if (billableMins > 0) {
			billablePercentage = (int) ((billableMins / (billableMins + nonBillableMins)) * 100);
		}
		if (nonBillableMins > 0) {
			nonBillablePercentage = (int) ((nonBillableMins / (billableMins + nonBillableMins)) * 100);
		}
		return "{\"billable\":" + billablePercentage + ",\"nonBillable\":"
				+ nonBillablePercentage + "}";

	}

	public String dashboardOverAllUtilization(Date startDate, Date endDate,
			String userGroupId, String accountId, String teamId,
			String subTeamId, String userId) {
		Double billableMins = 0.0;
		Double nonBillableMins = 0.0;
		List<DashboardUtilizationDetails> dashboardOverAllutilizationList = dashboarAllUtilizationList(
				startDate, endDate, userGroupId, "", "", "", "", false, false);
		for (DashboardUtilizationDetails dashboardOverAllutilization : dashboardOverAllutilizationList) {
			billableMins += dashboardOverAllutilization.getBillableMins();
			nonBillableMins += dashboardOverAllutilization.getNonBillableMins();
		}

		/*
		 * int billablePercentage = 0; int nonBillablePercentage = 0; if
		 * (billableMins >0) { billablePercentage = (int)
		 * ((billableMins/(billableMins+nonBillableMins))*100); } if
		 * (nonBillableMins > 0) { nonBillablePercentage = (int)
		 * ((nonBillableMins/(billableMins+nonBillableMins))*100); }
		 */
		int billableHrs = (int) (billableMins / 60);
		int nonBillableHrs = (int) (nonBillableMins / 60);
		int totalBillableHours = (int) (billableMins + nonBillableMins);
		int numberOfWorkingDays = DateUtils.getWorkingDaysBetweenTwoDates(
				startDate, endDate);
		int numberOfWorkingHrs = (numberOfWorkingDays * 8 * getFamstackApplicationConfiguration()
				.getAllUserList().size());
		int totalHrsPercentage = 0;
		if (totalBillableHours > 0) {
			totalHrsPercentage = (int) ((totalBillableHours / numberOfWorkingHrs) * 100);
		}

		return "{\"billableHrs\":" + billableHrs + ",\"nonBillableHrs\":"
				+ nonBillableHrs + ",\"totalHrsPercentage\":"
				+ totalHrsPercentage + "}";

	}

	public List<DashboardUtilizationDetails> dashboarAllUtilizationList(
			Date startDate, Date endDate, String userGroupId, String accountId,
			String teamId, String subTeamId, String userId,
			boolean isResouceUtilization, boolean isTotalUtilization) {

		List<DashboardUtilizationDetails> dashboardFilterdutilizationList = new ArrayList<>();

		List<DashboardUtilizationDetails> dashboardAllutilizationList = projectManager
				.dashboardAllUtilization(startDate, endDate, userGroupId,
						accountId, teamId, subTeamId, userId,
						isResouceUtilization, isTotalUtilization);
		System.out.println(dashboardAllutilizationList);
		for (DashboardUtilizationDetails dashboardOverAllutilization : dashboardAllutilizationList) {
			if (StringUtils.isNotBlank(teamId)
					&& !StringUtils.isNotBlank(subTeamId)) {
				List<String> teamIdsList = getArrayToList(teamId);
				dashboardOverAllutilization = filterDashboardUtilizationDetails(
						null, teamIdsList, null, dashboardOverAllutilization);
			}

			if (dashboardOverAllutilization != null) {
				dashboardFilterdutilizationList
						.add(dashboardOverAllutilization);
			}
		}
		System.out.println("Filtered DAUList : " + dashboardAllutilizationList);
		return dashboardFilterdutilizationList;
	}

	private List<String> getArrayToList(String ids) {
		List<String> idList = null;
		if (StringUtils.isNotBlank(ids)) {
			idList = new ArrayList(Arrays.asList(ids.split("#")));
		}
		return idList;
	}

	public List<DashboardUtilizationDetails> dashboarResourceUtilizationList(
			Date startDate, Date endDate, String userGroupId, String accountId,
			String teamId, String subTeamId, String userId) {
		return dashboarAllUtilizationList(startDate, endDate, userGroupId,
				accountId, teamId, subTeamId, userId, true, false);
	}

	public Double getTotalDashboardFilterdutilizationList(
			List<DashboardUtilizationDetails> dashboardOverAllutilizationList) {
		Double totalTime = 0.0;
		for (DashboardUtilizationDetails dashboardOverAllutilization : dashboardOverAllutilizationList) {
			totalTime += dashboardOverAllutilization.getBillableMins();
			totalTime += dashboardOverAllutilization.getNonBillableMins();
		}
		return totalTime;
	}

	public Double getTotalDashboardFilterdutilizationList(
			Map<String, Map<String, DashboardUtilizationDetails>> dashboardOverAllutilizationMap) {
		Double totalTime = 0.0;

		return totalTime;
	}

	private DashboardUtilizationDetails filterDashboardUtilizationDetails(
			List accountIdList, List teamIdList, List subTeamIdList,
			DashboardUtilizationDetails dashboardOverAllutilization) {
		if (accountIdList != null
				&& !accountIdList.contains(""
						+ dashboardOverAllutilization.getAccountId())) {
			dashboardOverAllutilization = null;
		} else if (teamIdList != null
				&& !teamIdList.contains(""
						+ dashboardOverAllutilization.getTeamId())) {
			dashboardOverAllutilization = null;
		} else if (subTeamIdList != null
				&& !subTeamIdList.contains(""
						+ dashboardOverAllutilization.getSubTeamId())) {
			dashboardOverAllutilization = null;
		}
		return dashboardOverAllutilization;
	}

	public Map<String, Map<String, DashboardUtilizationDetails>> dashboarAllUtilizationListCompare(
			Date startDate, Date endDate, String userGroupId,
			String accountIds, String teamIds, String subTeamIds,
			String userIds, String type) {
		List<DashboardUtilizationDetails> dashboardOverAllutilizationList = dashboarAllUtilizationList(
				startDate, endDate, userGroupId, accountIds, teamIds,
				subTeamIds, userIds, false, true);
		Map<String, Map<String, DashboardUtilizationDetails>> itemCacheMap = new HashMap<>();

		List<String> accountIdsList = getArrayToList(accountIds);
		List<String> teamIdsList = getArrayToList(teamIds);
		List<String> subTeamIdsList = getArrayToList(subTeamIds);
		List<String> userIdsLIst = getArrayToList(userIds);

		for (DashboardUtilizationDetails dashboardUtilizationDetails : dashboardOverAllutilizationList) {
			Date date = dashboardUtilizationDetails.getActualTaskStartTime();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String key = "" + calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1);

			Map<String, DashboardUtilizationDetails> newDashboardUtilizationDetailsMap = itemCacheMap
					.get(key);
			if (newDashboardUtilizationDetailsMap == null) {
				Map<String, DashboardUtilizationDetails> dbUlDataListMap = new HashMap<>();
				List<String> tempList = null;
				if (userIdsLIst != null) {
					tempList = userIdsLIst;
				} else if (subTeamIdsList != null) {
					tempList = subTeamIdsList;
				} else if (teamIdsList != null) {
					tempList = teamIdsList;
				} else if (accountIdsList != null) {
					tempList = accountIdsList;
				} else {
					return null;
				}

				for (String id : tempList) {
					DashboardUtilizationDetails tempDashboardUtilizationDetails = new DashboardUtilizationDetails();
					if (userIdsLIst != null) {
						tempDashboardUtilizationDetails.setUserId(Integer
								.parseInt(id));
						tempDashboardUtilizationDetails.setType("user");
					} else if (subTeamIdsList != null) {
						tempDashboardUtilizationDetails.setSubTeamId(Integer
								.parseInt(id));
						tempDashboardUtilizationDetails.setType("Sub Teams");
					} else if (teamIdsList != null) {
						tempDashboardUtilizationDetails.setTeamId(Integer
								.parseInt(id));
						tempDashboardUtilizationDetails.setType("Teams");
					} else if (accountIdsList != null) {
						tempDashboardUtilizationDetails.setAccountId(Integer
								.parseInt(id));
						tempDashboardUtilizationDetails.setType("Accounts");
					}
					tempDashboardUtilizationDetails.setBillableMins(0d);
					tempDashboardUtilizationDetails.setNonBillableMins(0d);
					dbUlDataListMap.put(id, tempDashboardUtilizationDetails);
				}

				itemCacheMap.put(key, dbUlDataListMap);
			}
			DashboardUtilizationDetails newpDashboardUtilizationDetails = null;
			if (userIdsLIst != null) {
				newpDashboardUtilizationDetails = itemCacheMap.get(key).get(
						"" + dashboardUtilizationDetails.getUserId());
			} else if (subTeamIdsList != null) {
				newpDashboardUtilizationDetails = itemCacheMap.get(key).get(
						"" + dashboardUtilizationDetails.getSubTeamId());
			} else if (teamIdsList != null) {
				newpDashboardUtilizationDetails = itemCacheMap.get(key).get(
						"" + dashboardUtilizationDetails.getTeamId());
			} else if (accountIdsList != null) {
				newpDashboardUtilizationDetails = itemCacheMap.get(key).get(
						"" + dashboardUtilizationDetails.getAccountId());
			}

			if (newpDashboardUtilizationDetails != null) {
				Double billableMins = dashboardUtilizationDetails
						.getBillableMins()
						+ newpDashboardUtilizationDetails.getBillableMins();
				Double nonBillableMins = dashboardUtilizationDetails
						.getNonBillableMins()
						+ newpDashboardUtilizationDetails.getNonBillableMins();
				newpDashboardUtilizationDetails.setBillableMins(billableMins);
				newpDashboardUtilizationDetails
						.setNonBillableMins(nonBillableMins);
				newpDashboardUtilizationDetails
						.setUserId(dashboardUtilizationDetails.getUserId());
				newpDashboardUtilizationDetails
						.setAccountId(dashboardUtilizationDetails
								.getAccountId());
				newpDashboardUtilizationDetails
						.setTeamId(dashboardUtilizationDetails.getTeamId());
				newpDashboardUtilizationDetails
						.setSubTeamId(dashboardUtilizationDetails
								.getSubTeamId());
			}
		}
		return itemCacheMap;
	}

	public List<DashboardUtilizationDetails> dashboardTotalUtilizationChart(
			Date startDate, Date endDate, String userGroupId, String accountId,
			String teamId, String subTeamId, String userId, String viewType) {
		List<DashboardUtilizationDetails> dashboardOverAllutilizationList = dashboarAllUtilizationList(
				startDate, endDate, userGroupId, accountId, teamId, subTeamId,
				userId, false, true);
		List<DashboardUtilizationDetails> dashboardOverAllutilizationFilterdList = new ArrayList<>();
		Map<String, DashboardUtilizationDetails> itemCacheMap = new HashMap<>();

		for (DashboardUtilizationDetails dashboardUtilizationDetails : dashboardOverAllutilizationList) {
			Date date = dashboardUtilizationDetails.getActualTaskStartTime();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String key = "";
			if ("month".equalsIgnoreCase(viewType)) {
				key = "" + calendar.get(Calendar.YEAR) + " - "
						+ (calendar.get(Calendar.MONTH) + 1);
			} else if ("week".equalsIgnoreCase(viewType)) {
				key = "" + calendar.get(Calendar.YEAR) + "-W"
						+ calendar.get(Calendar.WEEK_OF_MONTH);
				;
			} else if ("day".equalsIgnoreCase(viewType)) {
				key = ""
						+ calendar.get(Calendar.YEAR)
						+ " - "
						+ (calendar.get(Calendar.MONTH) + 1 < 10 ? "0"
								+ (calendar.get(Calendar.MONTH) + 1)
								: (calendar.get(Calendar.MONTH) + 1))
						+ "-"
						+ (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
								+ calendar.get(Calendar.DAY_OF_MONTH)
								: calendar.get(Calendar.DAY_OF_MONTH));
			}

			DashboardUtilizationDetails newDashboardUtilizationDetails = itemCacheMap
					.get(key);
			if (newDashboardUtilizationDetails == null) {
				newDashboardUtilizationDetails = new DashboardUtilizationDetails();
				newDashboardUtilizationDetails
						.setBillableMins(dashboardUtilizationDetails
								.getBillableMins());
				newDashboardUtilizationDetails
						.setNonBillableMins(dashboardUtilizationDetails
								.getNonBillableMins());
				newDashboardUtilizationDetails.setType(key);
				itemCacheMap.put(key, newDashboardUtilizationDetails);
			} else {
				Double billableMins = dashboardUtilizationDetails
						.getBillableMins()
						+ newDashboardUtilizationDetails.getBillableMins();
				Double nonBillableMins = dashboardUtilizationDetails
						.getNonBillableMins()
						+ newDashboardUtilizationDetails.getNonBillableMins();
				newDashboardUtilizationDetails.setBillableMins(billableMins);
				newDashboardUtilizationDetails
						.setNonBillableMins(nonBillableMins);
			}
		}

		List sortedKeys = new ArrayList(itemCacheMap.keySet());
		Collections.sort(sortedKeys);
		for (Object key : sortedKeys) {
			dashboardOverAllutilizationFilterdList.add(itemCacheMap.get(key));
		}

		return dashboardOverAllutilizationFilterdList;
	}

	public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(
			Date startDate, Date endDate) {
		return getAllProjectTaskAssigneeData(startDate, endDate, false);
	}

	public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(
			Date startDate, Date endDate, boolean uniqueTaskUser) {
		return getAllProjectTaskAssigneeData(startDate, endDate,
				uniqueTaskUser, null);
	}

	public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(
			Date startDate, Date endDate, boolean uniqueTaskUser, Integer userId) {
		return getAllProjectTaskAssigneeData(startDate, endDate,
				uniqueTaskUser, true, userId);
	}

	public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(
			Date startDate, Date endDate, boolean uniqueTaskUser,
			boolean addSameTaskActTime, Integer userId) {
		List<ProjectTaskActivityDetails> projectDetailsList = new ArrayList<>();
		if (startDate != null && endDate != null) {
			projectDetailsList = projectManager.getAllProjectTaskAssigneeData(
					startDate, endDate, uniqueTaskUser, addSameTaskActTime,
					userId);
		}
		return projectDetailsList;
	}

	public Map<String, Map<Integer, UserWorkDetails>> getAllEmployeeUtilizationList(
			Date startDate, Date endDate) {
		return famstackUserActivityManager.getUserUtilizationHours(startDate,
				endDate);
	}

	public Map<String, Map<Integer, Integer>> getAllNonBillableTaskActivityList(
			Date startDate, Date endDate) {
		return famstackUserActivityManager.getAllNonBillableTaskActivityList(
				startDate, endDate);
	}

	public Map<Integer, Map<String, UserTaskActivityItem>> getAllNonBillabileActivities(
			Date startDate, Date endDate) {
		return famstackUserActivityManager.getAllNonBillabileActivities(
				startDate, endDate);
	}

	public List<ProjectTaskActivityDetails> getAllNonBillableTaskActivities(
			Date startDate, Date endDate, boolean uniqueList,
			Integer currentUserId) {
		return famstackUserActivityManager.getAllNonBillableTaskActivities(
				startDate, endDate, uniqueList,
				true, currentUserId);
	}


	public void updateProject(ProjectDetails projectDetails) {
		projectManager.updateProjectItem(projectDetails);
		setCurrentUser(projectDetails);
		notifyAll(NotificationType.PROJECT_UPDATE, projectDetails);
	}

	private void notifyAll(NotificationType notificationType,
			Object detailsObject) {
		UserItem currentUser = null;
		if (notificationType != NotificationType.RESET_PASSWORD
				&& notificationType != NotificationType.FORGOT_PASSWORD
				&& notificationType != NotificationType.USER_REGISTRAION
				&& notificationType != NotificationType.USER_UPDATE) {
			currentUser = getFamstackApplicationConfiguration()
					.getCurrentUser();
		}
		famstackNotificationServiceManager.notifyAll(notificationType,
				detailsObject, currentUser);
	}

	public String getNotifications(int userId) {

		try {
			LimitedQueue<NotificationItem> notificationItemList = famstackDesktopNotificationService
					.getNotificatioItems(userId);
			if (notificationItemList != null) {
				return FamstackUtils.getJsonFromObject(notificationItemList);
			}
		} catch (Exception exception) {
			logError("Unable to notify user : " + userId, exception);
			famstackDesktopNotificationService.clearNotification(userId);
		}
		return "{}";
	}

	public boolean forgotPassword(String userName) {
		EmployeeDetails employeeDetails = userProfileManager
				.updateUserPasswordForReset(userName);

		if (employeeDetails != null) {
			notifyAll(NotificationType.FORGOT_PASSWORD, employeeDetails);
			return true;
		} else {
			return false;
		}

	}

	public String getUserBillableProductiveJson() {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();
		Date endTime = getFamstackUserSessionConfiguration()
				.getDashboardViewEndDate();
		Map<Object, UserWorkDetails> userWorkDetailsMap = famstackUserActivityManager
				.getUserBillableProductiveHours(startTime, endTime);

		Map<Integer, EmployeeDetails> empMap = getFamstackApplicationConfiguration()
				.getFilterdUserMap("");

		for (int userId : empMap.keySet()) {
			UserWorkDetails userDetails = userWorkDetailsMap.get(userId);
			if (userWorkDetailsMap.get(userId) == null) {
				userDetails = new UserWorkDetails();
				userDetails.setBillableMins(0);
				userDetails.setCount(0);
				userDetails.setNonBillableMins(0);
				userWorkDetailsMap.put(userId, userDetails);
			}

			if (userDetails != null) {
				userDetails.setUserFirstName(empMap.get(userId).getFirstName());
			}
		}

		return FamstackUtils.getJsonFromObject(userWorkDetailsMap.values())
				.trim();
	}

	public String getProjectTypeJson() {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();
		Date endTime = getFamstackUserSessionConfiguration()
				.getDashboardViewEndDate();

		List<ProjectStatusDetails> projectTypeCountList = projectManager
				.getProjectItemByTypeCount(startTime, endTime);

		return FamstackUtils.getJsonFromObject(projectTypeCountList).trim();
	}

	public String getTeamUtilizationJson() {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();
		Date endTime = getFamstackUserSessionConfiguration()
				.getDashboardViewEndDate();

		List<TeamUtilizatioDetails> teamUtilizationDetailsList = projectManager
				.getTeamUtilizationJson(startTime, endTime);

		return FamstackUtils.getJsonFromObject(teamUtilizationDetailsList)
				.trim();
	}

	public String getProjectCategoryJson() {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();
		Date endTime = getFamstackUserSessionConfiguration()
				.getDashboardViewEndDate();

		List<ProjectCategoryDetails> projectCategoryDetailsList = projectManager
				.getProjectCategoryJson(startTime, endTime);

		return FamstackUtils.getJsonFromObject(projectCategoryDetailsList)
				.trim();
	}

	public List<ClientProjectDetails> getClientProject() {
		Date startTime = getFamstackUserSessionConfiguration()
				.getDashboardViewStartDate();
		Date endTime = getFamstackUserSessionConfiguration()
				.getDashboardViewEndDate();

		List<ClientProjectDetails> clientProjectDetailsList = projectManager
				.getClientProject(startTime, endTime);

		return clientProjectDetailsList;
	}

	public String getUserStatusJson(String groupId) {

		List<UserStatus> userStatusList = new ArrayList<UserStatus>();

		Map<Integer, EmployeeDetails> empMap = getFamstackApplicationConfiguration()
				.getFilterdUserMap(groupId);
		for (Integer userId : empMap.keySet()) {
			String availableMessage = "Busy till";
			EmployeeDetails employeeDetails = empMap.get(userId);
			UserStatus userStatus = new UserStatus();
			userStatus.setStatus(employeeDetails.getCheckUserStatus());
			userStatus.setUserId(userId);
			Date userAvailableTime = employeeDetails.getUserAvailableTime();
			if (employeeDetails.isLeave() != null) {
				availableMessage = "Leave till";
			}
			if (userAvailableTime != null) {
				availableMessage += " "
						+ (userAvailableTime.getHours() < 10 ? "0"
								+ userAvailableTime.getHours()
								: userAvailableTime.getHours())
						+ ":"
						+ (userAvailableTime.getMinutes() < 10 ? "0"
								+ userAvailableTime.getMinutes()
								: userAvailableTime.getMinutes());
			} else {
				availableMessage = "Available";
			}
			/*
			 * if (employeeDetails.isLeave() != null &&
			 * employeeDetails.isLeave() == LeaveType.FIRST) { availableMessage
			 * = "First Half of the Day Leave"; } else if
			 * (employeeDetails.isLeave() != null && employeeDetails.isLeave()
			 * == LeaveType.SECOND) { availableMessage =
			 * "Second Half of the Day Leave"; } else if
			 * (employeeDetails.isLeave() != null && employeeDetails.isLeave()
			 * == LeaveType.FULL) { availableMessage = "Full Day Leave"; }
			 */
			userStatus.setUserAvailableMsg(availableMessage);
			userStatusList.add(userStatus);
		}

		return FamstackUtils.getJsonFromObject(userStatusList);
	}

	public void deleteGroup(int groupId) {
		groupMessageManager.deleteGroup(groupId);
	}

	public String getMessageNotifications(int userId) {
		List<GroupDetails> groupDetailsList = groupMessageManager
				.getGroupsForUser(userId);
		Map<Integer, ArrayList<GroupMessageDetails>> groupUserMessageMap = new HashMap<>();

		for (GroupDetails groupDetails : groupDetailsList) {
			LimitedQueue<GroupMessageDetails> groupMessageList = famstackDesktopNotificationService
					.getMessageNotificatioItems(userId,
							groupDetails.getGroupId());
			if (groupMessageList != null) {
				groupUserMessageMap.put(groupDetails.getGroupId(),
						new ArrayList<GroupMessageDetails>());
				for (GroupMessageDetails groupMessageDetails : groupMessageList) {
					if (!groupMessageDetails.getRead()) {
						groupUserMessageMap.get(groupDetails.getGroupId()).add(
								groupMessageDetails);
					}
				}
			}
		}

		return FamstackUtils.getJsonFromObject(groupUserMessageMap);
	}

	public String createAccountConfig(String input1, String input2,
			String type, String action, int parentId, int id) {
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

	public void delteAccountConfig(String action, int id) {
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

	public void setConfiguration(String propertyName, String propertyValue) {
		ConfigurationSettingsItem configurationSettingsItem = new ConfigurationSettingsItem();
		configurationSettingsItem.setPropertyValue(propertyValue);
		configurationSettingsItem.setPropertyName(propertyName);
		famstackDataAccessObjectManager
				.saveOrUpdateItem(configurationSettingsItem);

		getFamstackApplicationConfiguration().updatConfiguraionIteme(
				configurationSettingsItem);

	}

	public Set<Integer> getTaskOwners(
			Map<String, List<TaskDetails>> taskDetailsMap) {
		Set<Integer> taskOwners = new HashSet<>();

		if (taskDetailsMap != null) {
			for (String key : taskDetailsMap.keySet()) {
				List<TaskDetails> taskDetailsList = taskDetailsMap.get(key);

				if (taskDetailsList != null) {
					for (TaskDetails taskDetails : taskDetailsList) {
						List<TaskActivityDetails> activityDetails = taskDetails
								.getTaskActivityDetails();

						if (activityDetails != null) {
							for (TaskActivityDetails activityDetail : activityDetails) {
								taskOwners.add(activityDetail.getUserId());
							}
						}
					}
				}
			}
		}
		return taskOwners;
	}

	public void updateNonBillableTask(int taskActId, int userId, String type,
			String taskActCategory, String startDate, String endDate,
			String comments, Boolean skipWeekEnd) {
		UserTaskActivityItem userTaskActivityItem = famstackUserActivityManager
				.deleteTaskActivity(taskActId);
		createNonBillableTask(userId, type, taskActCategory, startDate,
				endDate, comments, skipWeekEnd);
	}

	public void createNonBillableTask(int userId, String type,
			String taskActCategory, String startDateString,
			String endDateString, String comments, Boolean skipWeekEnd) {
		Date startTime = DateUtils.tryParse(startDateString,
				DateUtils.DATE_TIME_FORMAT);
		Date endTime = DateUtils.tryParse(endDateString,
				DateUtils.DATE_TIME_FORMAT);

		int numberOfDays = DateUtils.getNumberOfDaysBetweenTwoDates(startTime,
				endTime);
		int durationInMinutes = 0;

		int index = 0;

		boolean isWeekEnd = false;

		do {
			Calendar startTimeCal = Calendar.getInstance();
			startTimeCal.setTime(startTime);

			if ((startTimeCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startTimeCal
					.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					&& skipWeekEnd) {
				isWeekEnd = true;
			} else {
				isWeekEnd = false;
			}

			if (!isWeekEnd) {
				if (numberOfDays == 0) {
					durationInMinutes = (int) ((endTime.getTime() - startTime
							.getTime()) / (1000 * 60));
				} else if (index == 0) {

					Calendar cal = Calendar.getInstance();
					cal.setTime(startTime);
					cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
							cal.get(Calendar.DAY_OF_MONTH), 17, 0);
					Date sameDayEndDate = cal.getTime();

					durationInMinutes = (int) ((sameDayEndDate.getTime() - startTime
							.getTime()) / (1000 * 60));
				} else if (numberOfDays == index) {
					durationInMinutes = (int) ((endTime.getTime() - startTime
							.getTime()) / (1000 * 60));
				} else {
					durationInMinutes = 480;
				}

				/*
				 * if (durationInMinutes > 480) { durationInMinutes = 480; }
				 */

				String taskName = taskActCategory;

				famstackUserActivityManager.createCompletedUserActivityItem(
						userId, startTime, 0, taskName, durationInMinutes,
						UserTaskType.valueOf(type), taskActCategory,
						ProjectType.NON_BILLABLE, comments);
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH), 9, 0);
			startTime = cal.getTime();
			index++;

		} while (index <= numberOfDays);
	}

	public void deleteTaskActivity(int activityId) {
		UserTaskActivityItem userTaskActivityItem = famstackUserActivityManager
				.deleteTaskActivity(activityId);
		if (userTaskActivityItem != null) {
			int taskId = userTaskActivityItem.getTaskId();
			TaskItem taskItem = famstackProjectTaskManager
					.getTaskItemById(taskId);
			if (taskItem != null) {
				// taskItem.setDuration(taskItem.getDuration() -
				// (userTaskActivityItem.getDurationInMinutes() / 60));
				taskItem.setActualTimeTaken(taskItem.getActualTimeTaken()
						- userTaskActivityItem.getDurationInMinutes());
				famstackDataAccessObjectManager.saveOrUpdateItem(taskItem);
			}
		}

	}

	private String getRandomColor() {
		Random random = new Random();
		int nextInt = random.nextInt(0xffffff + 1);
		return String.format("#%06x", nextInt);
	}

	public String covertdashboardOverAllutilizationMapJson(
			Map<String, Map<String, DashboardUtilizationDetails>> dashboardOverAllutilizationMap,
			String type) {
		JSONObject dbCompareJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray colorsJSonArray = new JSONArray();
		;
		JSONArray labelsJSonArray = new JSONArray();
		JSONArray ykeysJSonArray = null;
		if (dashboardOverAllutilizationMap != null) {
			for (String key : dashboardOverAllutilizationMap.keySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("month", key + "-31");
				Map<String, DashboardUtilizationDetails> dashboardUtilizationDetailsMao = dashboardOverAllutilizationMap
						.get(key);

				if (ykeysJSonArray == null) {
					ykeysJSonArray = new JSONArray();
					for (String itemKey : dashboardUtilizationDetailsMao
							.keySet()) {
						ykeysJSonArray.put(itemKey);
						labelsJSonArray.put(dashboardUtilizationDetailsMao.get(
								itemKey).getLabel());
						colorsJSonArray.put(getRandomColor());
					}
				}

				for (String itemKey : dashboardUtilizationDetailsMao.keySet()) {
					if ("All".equalsIgnoreCase(type)) {
						jsonObject.put(itemKey, dashboardUtilizationDetailsMao
								.get(itemKey).getGrandTotalPercentage());
					} else if ("BILLABLE".equalsIgnoreCase(type)) {
						jsonObject.put(itemKey, dashboardUtilizationDetailsMao
								.get(itemKey).getBillablePercentage());
					} else {
						jsonObject.put(itemKey, dashboardUtilizationDetailsMao
								.get(itemKey).getNonBillablePercentage());
					}

				}
				jsonArray.put(jsonObject);
			}
		}
		dbCompareJson.put("data", jsonArray);
		dbCompareJson.put("ykeys", ykeysJSonArray);
		dbCompareJson.put("labels", labelsJSonArray);
		dbCompareJson.put("lineColors", colorsJSonArray);

		return dbCompareJson.toString();
	}

	public List<TaskDetails> loadProjectTaskDetails(int projectId) {
		return projectManager.getProjectTaskDetails(projectId);
	}

	public Date getAssigneeSlot(int assigneeId, String startDateTime,
			String endDateTime) {
		return famstackUserActivityManager.getAssigneeSlot(assigneeId,
				startDateTime, endDateTime);

	}

	public void quickDuplicateProject(int projectId, String projectName,
			Integer projectDuration, String projectStartTime,
			String projectEndTime, String taskDetails) {

		projectManager.quickDuplicateProject(projectId, projectName,
				projectDuration, projectStartTime, projectEndTime, taskDetails);

	}

	public void weeklyTimeLog(String projectDetails, String weekStartDate) {
		projectManager.weeklyTimeLog(projectDetails, weekStartDate);
	}

	public int adjustTaskActivityTime(int activityId, int taskId,
			int newDuration, String startTime, String endTime) {

		return famstackProjectTaskManager.adjustTaskActivityTime(activityId,
				taskId, newDuration, startTime, endTime);

	}

	public void adjustTaskTime(int taskId, int newDuration) {
		famstackProjectTaskManager.adjustTaskTime(taskId, newDuration);
	}

	public String getRecurringProjectDetails(String projectCode, int projectId) {
		RecurringProjectDetails recurringProjectDetails = projectManager
				.getRecurringProjectDetails(projectCode, projectId);
		return recurringProjectDetails != null ? FamstackUtils
				.getJsonFromObject(recurringProjectDetails) : "";
	}

	public String createRecurringProject(String projectCode, int projectId,
			String cronExp, String recurringEndDate, boolean recurreOriginal) {
		RecurringProjectDetails recurringProjectDetails = projectManager
				.createRecurringProject(projectCode, projectId, cronExp,
						recurringEndDate, recurreOriginal);
		return recurringProjectDetails != null ? FamstackUtils
				.getJsonFromObject(recurringProjectDetails) : "";
	}

	public void deleteRecuringProjectDetails(int recurringProjectId) {
		projectManager.deleteRecuringProjectDetails(recurringProjectId);
	}

	public String getAllRecuringProjectCodes() {
		return FamstackUtils.getJsonFromObject(projectManager
				.getAllRecuringProjectCodes());
	}

	public void deleteProjects(List<Integer> projectIds, String type) {
		projectManager.deleteProjects(projectIds, type);

	}

	public void sendMail(String subject, String messageBody) {
		famstackEmailSender.sendTextMessage("ALERT: ERROR - " + subject,
				messageBody);
	}

	public void trackUserSiteActivity(Integer userId, Date activityDate,
			boolean status) {
		famstackUserActivityManager.createUserSiteActivities(userId,
				activityDate, status);
	}

	public Map<Integer, Map<String, String>> getAllUserSiteActivities(
			Date startDate, Date endDate) {
		
		String userGroupId = getFamstackApplicationConfiguration()
				.getCurrentUserGroupId();
		if ("1012".equalsIgnoreCase(getFamstackApplicationConfiguration().getCurrentUserGroupId())) {
			userGroupId = null;
		}
		return famstackUserActivityManager.getAllUserSiteActivities(startDate,
				endDate, userGroupId);
	}

	public UserItem getUserItem(String emailId) {
		return userProfileManager.getUserItem(emailId);
	}

	public UserItem unblockUser(int userId) {
		return userProfileManager.unblockUser(userId);
	}

	public List<EmployeeBWDetails> getEmployeesBandWithTodayAndYesterDay(
			String groupId, Date date) {
		return userProfileManager.getEmployeesBandWithTodayAndYesterDay(
				groupId, date);
	}

	public List<EmployeeBWDetails> getEmployeesOnLeaveToday(String groupId,
			Date date) {
		return userProfileManager.getEmployeesOnLeaveToday(groupId, date);
	}

	public void initializeAccounts() {
		famstackAccountManager.forceInitialize();
	}

	public String getUserSiteActivityJson(Date startDate, Date endDate,
			int userId) {
		JSONArray jsonArray = new JSONArray();
		Map<Integer, Map<String, String>> userSiteActivityMap = getAllUserSiteActivities(
				startDate, endDate);
		Map<Integer, Map<String, UserTaskActivityItem>> nonBillativityMap = getAllNonBillabileActivities(
				startDate, endDate);

		Map<String, String> userSiteActivities = userSiteActivityMap
				.get(userId);
		Map<String, UserTaskActivityItem> nonBillativities = nonBillativityMap
				.get(userId);

		JSONObject jsonObject = null;
		if (userSiteActivities != null) {
			for (String dateString : userSiteActivities.keySet()) {
				jsonObject = new JSONObject();
				jsonObject.put("start", dateString.replace("/", "-"));
				jsonObject.put("dateString", dateString.replace("/", "-"));
				jsonObject.put("type", "activity");
				jsonArray.put(jsonObject);
			}
		}

		if (nonBillativities != null) {
			for (String dateString : nonBillativities.keySet()) {

				String taskActCategory = nonBillativities.get(dateString)
						.getTaskActCategory();
				if (FamstackConstants.LEAVE.equalsIgnoreCase(taskActCategory)
						|| FamstackConstants.HOLIDAY
								.equalsIgnoreCase(taskActCategory)
						|| FamstackConstants.LEAVE_OR_HOLIDAY
								.equalsIgnoreCase(taskActCategory)
						|| FamstackConstants.MEETING
								.equalsIgnoreCase(taskActCategory)) {
					jsonObject = new JSONObject();
					jsonObject.put("start", dateString.replace("/", "-"));
					jsonObject.put("dateString", dateString.replace("/", "-"));

					jsonObject.put("type", taskActCategory);
					jsonArray.put(jsonObject);
				}
			}
		}
		return jsonArray.toString();
	}

	public String mapProjectDataToTimeline(List<ProjectDetails> projectData) {
		JSONObject dataObject = new JSONObject();
		JSONArray dataArray = new JSONArray();
		JSONArray resourceArray = new JSONArray();
		Set<Integer> ownerSet = new HashSet<>();

		for (ProjectDetails projectDetail : projectData) {
			JSONObject projectDataObject = new JSONObject();
			projectDataObject.put("id", projectDetail.getId());
			projectDataObject.put("text", projectDetail.getName());
			projectDataObject.put("start_date", DateUtils.format(DateUtils
					.tryParse(projectDetail.getStartTime(),
							DateUtils.DATE_TIME_FORMAT),
					DateUtils.DATE_MONTH_YEAR_TIME));
			projectDataObject.put("duration",
					projectDetail.getProjectDurationInDays());
			projectDataObject.put("progress", 0);
			projectDataObject.put("parent", 0);
			projectDataObject.put("type", "task");
			dataArray.put(projectDataObject);
			if (projectDetail.getProjectTaskDeatils() != null) {
				for (TaskDetails taskDetails : projectDetail
						.getProjectTaskDeatils()) {
					JSONObject taskDataObject = new JSONObject();
					taskDataObject.put("id", taskDetails.getTaskId());
					taskDataObject.put("text", taskDetails.getName());
					taskDataObject.put("start_date", DateUtils.format(DateUtils
							.tryParse(projectDetail.getStartTime(),
									DateUtils.DATE_TIME_FORMAT),
							DateUtils.DATE_MONTH_YEAR_TIME));
					taskDataObject.put("duration",
							(taskDetails.getDuration() / 24));
					taskDataObject.put("progress",
							taskDetails.getPercentageOfTaskCompleted());
					taskDataObject.put("parent", projectDetail.getId());
					taskDataObject.put("owner_id", taskDetails.getAssignee());
					taskDataObject.put("type", "task");
					ownerSet.add(taskDetails.getAssignee());
					dataArray.put(taskDataObject);
				}
			}
		}

		for (Integer user : ownerSet) {
			if (user != 0) {
				JSONObject resourceDataObject = new JSONObject();
				resourceDataObject.put("id", user);
				EmployeeDetails employeeDetails = getFamstackApplicationConfiguration()
						.getAllUsersMap().get(user);
				if (employeeDetails != null) {
					resourceDataObject.put("text",
							employeeDetails.getFirstName() + " "
									+ employeeDetails.getLastName());
				} else {
					resourceDataObject.put("text", "Unassigned");
				}
				resourceArray.put(resourceDataObject);
			}
		}

		dataObject.put("data", dataArray);
		dataObject.put("resource", resourceArray);

		return dataObject.toString();
	}

	public String getWeeklyLogggedTime(String weekStartDate,
			Integer currentUserId) {
		Date startDate = DateUtils.tryParse(weekStartDate,
				DateUtils.DAY_MONTH_YEAR);
		Date endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY,
				startDate, 6);
		if (currentUserId == 0) {
			currentUserId = null;
		}

		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = getBillableAndNonBillaleSortedListByAssignee(
				startDate, endDate, true, currentUserId);
		return getJsonPrjTskWeeklyTaskList(projectTaskAssigneeDataList);
	}

	public List<ProjectTaskActivityDetails> getBillableAndNonBillaleSortedListByStartDate(
			Date startDate, Date endDate, boolean isUnique,
			Integer currentUserId) {
		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
		projectTaskAssigneeDataList.addAll(getAllProjectTaskAssigneeData(
				startDate, endDate, isUnique, currentUserId));
		projectTaskAssigneeDataList.addAll(getAllNonBillableTaskActivities(
				startDate, endDate, isUnique, currentUserId));
		Collections.sort(projectTaskAssigneeDataList,
				new Comparator<ProjectTaskActivityDetails>() {
					@Override
					public int compare(
							ProjectTaskActivityDetails projectDetails2,
							ProjectTaskActivityDetails projectDetails1) {
						Date date1 = projectDetails1.getTaskActivityStartTime();
						Date date2 = projectDetails2.getTaskActivityStartTime();

						if (date1.before(date2)) {
							return 1;
						} else if (date1.after(date2)) {
							return -1;
						}
						return 0;
					}
				});
		return projectTaskAssigneeDataList;

	}

	public List<ProjectTaskActivityDetails> getBillableAndNonBillaleSortedListByAssignee(
			Date startDate, Date endDate, boolean isUnique,
			Integer currentUserId) {
		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
		projectTaskAssigneeDataList.addAll(getAllProjectTaskAssigneeData(
				startDate, endDate, isUnique, currentUserId));
		projectTaskAssigneeDataList.addAll(getAllNonBillableTaskActivities(
				startDate, endDate, isUnique, currentUserId));
		Collections.sort(projectTaskAssigneeDataList,
				new Comparator<ProjectTaskActivityDetails>() {
					@Override
					public int compare(
							ProjectTaskActivityDetails projectDetails2,
							ProjectTaskActivityDetails projectDetails1) {
						int useId1 = projectDetails1.getUserId();
						int useId2 = projectDetails2.getUserId();
						EmployeeDetails emp1 = getFamstackApplicationConfiguration()
								.getAllUsersMap().get(useId1);
						EmployeeDetails emp2 = getFamstackApplicationConfiguration()
								.getAllUsersMap().get(useId2);
						if (emp1 != null && emp2 != null) {
							return emp2.getFirstName().compareTo(
									emp1.getFirstName());
						}
						return 0;
					}
				});
		return projectTaskAssigneeDataList;

	}

	private String getJsonPrjTskWeeklyTaskList(
			List<ProjectTaskActivityDetails> projectTaskAssigneeDataList) {
		JSONArray jsonArray = new JSONArray();
		for (ProjectTaskActivityDetails projectTaskActivityDetails : projectTaskAssigneeDataList) {
			int weekTotal = 0;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userId", projectTaskActivityDetails.getUserId());
			jsonObject.put("projectId",
					projectTaskActivityDetails.getProjectId());
			;
			jsonObject.put("taskId", projectTaskActivityDetails.getTaskId());
			jsonObject.put("taskActId",
					projectTaskActivityDetails.getTaskActivityId());
			;

			if (getFamstackApplicationConfiguration().getAllUsersMap().get(
					projectTaskActivityDetails.getUserId()) != null) {
				jsonObject.put("userName",
						getFamstackApplicationConfiguration().getAllUsersMap()
								.get(projectTaskActivityDetails.getUserId())
								.getFirstName());
			} else {
				jsonObject.put("userName",
						projectTaskActivityDetails.getUserId());
			}

			jsonObject.put("type", projectTaskActivityDetails.getProjectType());
			jsonObject.put("project",
					projectTaskActivityDetails.getProjectName());
			jsonObject.put("task", projectTaskActivityDetails.getTaskName());
			jsonObject.put("comments",
					projectTaskActivityDetails.getTaskCompletionComments());
			JSONObject timeData = new JSONObject();
			String dateString = DateUtils.format(
					projectTaskActivityDetails.getTaskActivityStartTime(),
					DateUtils.DAY_MONTH_YEAR);
			timeData.put(dateString,
					projectTaskActivityDetails.getDurationInHours());
			weekTotal += projectTaskActivityDetails.getTaskActivityDuration();
			for (ProjectTaskActivityDetails subItem : projectTaskActivityDetails
					.getSubItems()) {
				dateString = DateUtils.format(
						subItem.getTaskActivityStartTime(),
						DateUtils.DAY_MONTH_YEAR);
				timeData.put(dateString, subItem.getDurationInHours());
				weekTotal += subItem.getTaskActivityDuration();
			}

			int hours = weekTotal / 60;
			int minutes = weekTotal % 60;
			timeData.put("weekdayTotal",
					String.format("%d:%02d", hours, minutes));
			jsonObject.put("time", timeData);
			jsonArray.put(jsonObject);
		}
		return jsonArray.toString();
	}

	public String getMonthlyLogggedTime(String monthFilter, Integer userId) {
		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
		Date startDate = DateUtils.tryParse("01-" + monthFilter,
				DateUtils.DAY_MONTH_YEAR);
		Date endDate = DateUtils.getLastDayOfThisMonth(startDate);

		projectTaskAssigneeDataList.addAll(getAllProjectTaskAssigneeData(
				startDate, endDate, false, false, userId));
		projectTaskAssigneeDataList.addAll(famstackUserActivityManager
				.getAllNonBillableTaskActivities(
				startDate, endDate, false, false, userId));

		Collections.sort(projectTaskAssigneeDataList,
				new Comparator<ProjectTaskActivityDetails>() {
					@Override
					public int compare(
							ProjectTaskActivityDetails projectDetails2,
							ProjectTaskActivityDetails projectDetails1) {
						Date date1 = projectDetails1.getTaskActivityStartTime();
						Date date2 = projectDetails2.getTaskActivityStartTime();

						if (date1.before(date2)) {
							return -1;
						} else if (date1.after(date2)) {
							return 1;
						}
						return 0;
					}
				});
		return mapProjectTaskAssigneeDataToJson(projectTaskAssigneeDataList);
	}

	private String mapProjectTaskAssigneeDataToJson(
			List<ProjectTaskActivityDetails> projectTaskAssigneeDataList) {
		JSONArray jsonArray = new JSONArray();
		for (ProjectTaskActivityDetails projectTaskActivityDetails : projectTaskAssigneeDataList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userId", projectTaskActivityDetails.getUserId());
			jsonObject.put("projectId",
					projectTaskActivityDetails.getProjectId());
			;
			jsonObject.put("taskId", projectTaskActivityDetails.getTaskId());
			jsonObject.put("taskActivityId",
					projectTaskActivityDetails.getTaskActivityId());
			;
			if (getFamstackApplicationConfiguration().getAllUsersMap().get(
					projectTaskActivityDetails.getUserId()) != null) {
				jsonObject.put("userName",
						getFamstackApplicationConfiguration().getAllUsersMap()
								.get(projectTaskActivityDetails.getUserId())
								.getFirstName());
			} else {
				jsonObject.put("userName", "Unknown");
			}
			jsonObject.put("projectType",
					projectTaskActivityDetails.getProjectType());
			jsonObject.put("projectName",
					projectTaskActivityDetails.getProjectName());
			jsonObject
					.put("taskName", projectTaskActivityDetails.getTaskName());
			jsonObject.put("projectCategory",
					projectTaskActivityDetails.getProjectCategory());
			jsonObject.put("taskActProjType",
					projectTaskActivityDetails.getTaskActType());
			jsonObject.put("taskActCategory",
					projectTaskActivityDetails.getTaskActCategory());
			jsonObject.put("completionComments",
					projectTaskActivityDetails.getTaskCompletionComments());
			jsonObject.put("durationInHours",
					projectTaskActivityDetails.getDurationInHours());
			jsonObject.put("dateString", DateUtils.format(
					projectTaskActivityDetails.getTaskActivityStartTime(),
					DateUtils.DAY_MONTH_YEAR));

			jsonArray.put(jsonObject);
		}
		return jsonArray.toString();
	}

	public String getRecurringTaskDetails(int taskId) {
		RecurringProjectDetails recurringTaskDetails = projectManager
				.getRecurringTaskDetails(taskId);
		return recurringTaskDetails != null ? FamstackUtils
				.getJsonFromObject(recurringTaskDetails) : "";
	}

	public String createRecurringTask(int projectId, int taskId,
			String cronExp, String recurringEndDate, boolean recurreOriginal) {
		RecurringProjectDetails recurringTaskDetails = projectManager
				.createRecurringTask(projectId, taskId, cronExp,
						recurringEndDate, recurreOriginal);
		return recurringTaskDetails != null ? FamstackUtils
				.getJsonFromObject(recurringTaskDetails) : "";
	}

	public void deleteRecuringTaskDetails(int recurringProjectId) {
		projectManager.deleteRecuringProjectDetails(recurringProjectId);
	}

	public String getAllRecuringTaskByProjectId(int projectId) {
		return FamstackUtils.getJsonFromObject(projectManager
				.getAllRecuringTaskByProjectId(projectId));
	}

	public void sendAutoReportEmail(String userGroupId, ReportType reportType, int lastHowManyDays, int startDays) {
		projectManager.sendAutoReportEmail(null, null, userGroupId, reportType, lastHowManyDays,startDays, reportType + " Report" );
	}

}