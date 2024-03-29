package com.famstack.projectscheduler.manager;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.famstack.email.FamstackEmailSender;
import com.famstack.projectscheduler.contants.FamstackConstants;
import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.contants.NotificationType;
import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectTaskType;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.RecurringType;
import com.famstack.projectscheduler.contants.ReportType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.dashboard.bean.ClientProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.DashBoardProjectDetails;
import com.famstack.projectscheduler.dashboard.bean.DashboardUtilizationDetails;
import com.famstack.projectscheduler.dashboard.bean.POEstimateProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectCategoryDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectStatusDetails;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.bean.TeamUtilizatioDetails;
import com.famstack.projectscheduler.datatransferobject.AutoReportingItem;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.RecurringProjectItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.DailyTimesheetDumpDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.RecurringProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.employees.bean.UserGroupDetails;
import com.famstack.projectscheduler.employees.bean.UserSiteActivityDetails;
import com.famstack.projectscheduler.employees.bean.UserUtilization;
import com.famstack.projectscheduler.employees.bean.UserUtilizationDetails;
import com.famstack.projectscheduler.employees.bean.UserUtilizationWeekWiseDetails;
import com.famstack.projectscheduler.employees.bean.UtilizationByUserSkillOrCategory;
import com.famstack.projectscheduler.employees.bean.UtilizationProjectDetails;
import com.famstack.projectscheduler.notification.FamstackNotificationServiceManager;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;
import com.famstack.projectscheduler.util.TimeInType;
import com.famstack.projectscheduler.utils.FamstackUtils;

/**
 * The Class FamstackProjectManager.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
public class FamstackProjectManager extends BaseFamstackManager {

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

	@Resource
	FamstackEmailSender famstackEmailSender;

	@Resource
	FamstackUserActivityManager famstackUserActivityManager;

	public int createProjectItem(ProjectDetails projectDetails) {
		ProjectItem projectItem = new ProjectItem();
		projectItem.setStatus(ProjectStatus.NEW);
		int projectId = saveProjectItem(projectDetails, projectItem);
		famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED, null);
		return projectId;
	}

	public void quickDuplicateProject(int projectId, String projectName, Integer projectDuration,
			String projectStartTime, String projectEndTime, String taskDetailsList) {
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
			Timestamp startTimeStamp, Timestamp completionTimeStamp, List<TaskDetails> taskItemList) {
		ProjectItem projectItem = new ProjectItem();

		if (projectDetails != null) {
			projectItem.setStatus(ProjectStatus.NEW);
			projectItem.setCategory(projectDetails.getCategory());
			projectItem.setCode(projectDetails.getCode());
			projectItem.setDescription(projectDetails.getDescription());
			projectItem.setName(projectName);
			projectItem.setPpi(projectDetails.getPpi());
			projectItem.setQuantity(projectDetails.getQuantity());
			projectItem.setPriority(projectDetails.getPriority());
			projectItem.setProjectSubType(projectDetails.getProjectSubType());
			projectItem.setProjectLead(projectDetails.getProjectLead());

			projectItem.setPONumber(projectDetails.getPONumber());

			projectItem.setSowLineItem(projectDetails.getSowLineItem());
			projectItem.setNewCategory(projectDetails.getNewCategory());

			projectItem.setOrderBookRefNo(projectDetails.getOrderBookRefNo());
			projectItem.setProposalNo(projectDetails.getProposalNo());
			projectItem.setProjectLocation(projectDetails.getProjectLocation());

			projectItem.setClientPartner(projectDetails.getClientPartner());
			projectItem.setHoursUserSkillMonthlySplitJson(projectDetails.getHoursUserSkillMonthlySplitJson());

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

	public void weeklyTimeLog(String projectDetails, String weekStartDate) {
		String[] projectRowDetailsArray = projectDetails.split("#PD#");
		// Billable ->projectIds->tasks->usersIds->daylist
		Map<String, Map<String, Map<String, Map<String, List<Integer>>>>> projectDetailsMap = new HashMap<>();
		Map<String, String> taskCommentsMap = new HashMap<>();
		for (String projectRowDetail : projectRowDetailsArray) {
			String[] projectLineItemDetailsArray = projectRowDetail.split("#PID#");

			String userId = projectLineItemDetailsArray[0];
			String projectType = projectLineItemDetailsArray[1];
			String projectId = projectLineItemDetailsArray[2];

			String taskId = projectLineItemDetailsArray[3];
			String nonBillableTask = projectLineItemDetailsArray[4];
			String taskComments = projectLineItemDetailsArray[12];

			if (projectType.equalsIgnoreCase("NON_BILLABLE")) {
				projectId = "projectId";
				taskId = nonBillableTask;
			}

			if (taskCommentsMap.get(taskId) != null) {
				String previousComments = taskCommentsMap.get(taskId);
				taskComments = previousComments + ", " + taskComments;

			}

			taskCommentsMap.put(taskId, taskComments);

			Map<String, Map<String, Map<String, List<Integer>>>> projectTypeMap = projectDetailsMap.get(projectType);
			if (projectTypeMap == null) {
				projectTypeMap = new HashMap();
				projectDetailsMap.put(projectType, projectTypeMap);
			}

			Map<String, Map<String, List<Integer>>> projectIdsMap = projectTypeMap.get(projectId);
			if (projectIdsMap == null) {
				projectIdsMap = new HashMap<>();
				projectTypeMap.put(projectId, projectIdsMap);
			}

			Map<String, List<Integer>> tasksMap = projectIdsMap.get(taskId);
			if (tasksMap == null) {
				tasksMap = new HashMap<>();
				projectIdsMap.put(taskId, tasksMap);
			}

			List<Integer> weekDayTaskTimeList = tasksMap.get(userId);
			if (weekDayTaskTimeList == null) {
				weekDayTaskTimeList = new ArrayList<>();
				tasksMap.put(userId, weekDayTaskTimeList);
			}

			for (int i = 1; i <= 7; i++) {
				String taskTime = projectLineItemDetailsArray[i + 4];
				int taskTimeInMins = 0;
				if (weekDayTaskTimeList.size() >= i) {
					taskTimeInMins = weekDayTaskTimeList.get(i - 1);
				}
				if (StringUtils.isNotBlank(taskTime)) {
					String[] taskTimeSplit = taskTime.split("[.]");
					int mins = 0;
					if (taskTimeSplit.length > 1) {
						if (StringUtils.isNotBlank(taskTimeSplit[1])) {
							mins = Integer.parseInt(taskTimeSplit[1]);
						}
						if (mins < 10) {
							mins *= 10;
						}
					}
					int hours = 0;
					if (StringUtils.isNotBlank(taskTimeSplit[0])) {
						hours = Integer.parseInt(taskTimeSplit[0]);
					}
					taskTimeInMins += ((hours * 60) + mins);
				}
				if (weekDayTaskTimeList.size() >= i) {
					weekDayTaskTimeList.set(i - 1, taskTimeInMins);
				} else {
					weekDayTaskTimeList.add(taskTimeInMins);
				}

			}

		}

		createBillableWeeklyTasks(projectDetailsMap, DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE,
				DateUtils.tryParse(weekStartDate, DateUtils.DAY_MONTH_YEAR), 5), taskCommentsMap);

	}

	private void createBillableWeeklyTasks(
			Map<String, Map<String, Map<String, Map<String, List<Integer>>>>> projectDetailsMap,
			Date weekStartTimeInitial, Map<String, String> taskCommentsMap) {
		for (String projectType : projectDetailsMap.keySet()) {
			Map<String, Map<String, Map<String, List<Integer>>>> projectIds = projectDetailsMap.get(projectType);

			for (String projectId : projectIds.keySet()) {
				ProjectItem projectItem = null;
				if (projectType.equalsIgnoreCase("BILLABLE")) {
					projectItem = getProjectItemById(Integer.parseInt(projectId));
				}
				Map<String, Map<String, List<Integer>>> taskIds = projectIds.get(projectId);
				int taskId = 0;
				for (String taskNameOrId : taskIds.keySet()) {
					TaskDetails taskDetails = new TaskDetails();
					TaskItem taskItem = null;
					if (projectType.equalsIgnoreCase("BILLABLE")) {
						if (org.apache.commons.lang.StringUtils.isNumeric(taskNameOrId)) {
							taskItem = famstackProjectTaskManager.getTaskItemById(Integer.parseInt(taskNameOrId));
						}
						if (taskItem == null) {
							taskItem = getTaskItemFromProject(projectItem, taskNameOrId);
						}
						if (taskItem == null) {
							taskDetails.setName(taskNameOrId);
							// taskDetails.setDescription("Weekly time log task
							// for " + taskNameOrId);
							taskDetails
									.setStartTime(DateUtils.format(weekStartTimeInitial, DateUtils.DATE_TIME_FORMAT));
							taskDetails.setDuration(0);
							taskDetails.setProjectTaskType(ProjectTaskType.PRODUCTIVE);
							taskDetails.setPriority(ProjectPriority.HIGH);
							famstackProjectTaskManager.createTaskItem(taskDetails, projectItem, true);
							taskId = taskDetails.getTaskId();
							taskItem = famstackProjectTaskManager.getTaskItemById(taskId);
						}
						String taskComments = taskCommentsMap.get(taskNameOrId);
						if (StringUtils.isNotBlank(taskComments)) {
							taskDetails.setDescription(taskComments);
						} else {
							taskDetails.setDescription(taskItem.getDescription());
						}
					}
					Map<String, List<Integer>> userIds = taskIds.get(taskNameOrId);
					for (String userId : userIds.keySet()) {
						taskDetails.setAssignee(Integer.parseInt(userId));
						List<Integer> weekTaskTimeList = userIds.get(userId);
						Date weekStartTime = new Date(weekStartTimeInitial.getTime());
						taskDetails.setStartTime(DateUtils.format(weekStartTimeInitial, DateUtils.DATE_TIME_FORMAT));
						for (int dayTaskTime : weekTaskTimeList) {
							if (dayTaskTime != 0) {
								if (projectType.equalsIgnoreCase("BILLABLE")) {
									famstackProjectTaskManager.createCompletedTaskItem(taskDetails, projectItem,
											taskItem, dayTaskTime, UserTaskType.PROJECT);
								} else {
									String userTaskType = "OTHER";
									String taskActCategory = taskNameOrId;
									if ("LEAVE".equalsIgnoreCase(taskNameOrId)) {
										taskActCategory = "Leave";
									}
									if ("MEETING".equalsIgnoreCase(taskNameOrId)) {
										taskActCategory = "Meeting";
									}
									if ("LEAVE".equalsIgnoreCase(taskNameOrId)
											|| "MEETING".equalsIgnoreCase(taskNameOrId)) {
										userTaskType = taskNameOrId;
									}
									String taskName = taskActCategory;
									famstackUserActivityManager.createCompletedUserActivityItem(
											taskDetails.getAssignee(), weekStartTime, 0, taskName, dayTaskTime,
											UserTaskType.valueOf(userTaskType), taskActCategory,
											ProjectType.NON_BILLABLE, taskCommentsMap.get(taskNameOrId), null, null,
											null, null, null, null, null, null);
								}
							}
							weekStartTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, weekStartTime, 1);
							taskDetails.setStartTime(DateUtils.format(weekStartTime, DateUtils.DATE_TIME_FORMAT));
						}
					}

					if (taskItem != null) {
						taskItem.setStatus(TaskStatus.COMPLETED);
						famstackDataAccessObjectManager.updateItem(taskItem);
					}
				}

			}
		}

	}

	private TaskItem getTaskItemFromProject(ProjectItem projectItem, String taskNameOrId) {
		try {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("taskName", taskNameOrId);
			dataMap.put("projectId", projectItem.getProjectId());
			List<TaskItem> taskItems = (List<TaskItem>) getFamstackDataAccessObjectManager()
					.executeQuery(HQLStrings.getString("getTaskByNameOrId"), dataMap);

			if (taskItems != null && taskItems.size() > 0) {
				return taskItems.get(0);
			}
		} catch (Exception e) {
			logError("getTaskItemFromProject failed ", e);
		}
		return null;
	}

	private void notifyProjectTaskAssignment(ProjectDetails projectDetails, List<TaskDetails> allTaskDetailsList) {
		if (projectDetails != null) {
			for (TaskDetails taskDetails : allTaskDetailsList) {

				Map<String, Object> projectTaskMap = new HashMap<>();
				projectTaskMap.put("taskDetails", taskDetails);
				projectTaskMap.put("projectDetails", projectDetails);
				famstackNotificationServiceManager.notifyAll(NotificationType.TASK_ASSIGNED, projectTaskMap, null);
			}
		}
	}

	private int saveProjectItem(ProjectDetails projectDetails, ProjectItem projectItem) {
		projectItem.setCategory(projectDetails.getCategory());
		projectItem.setCode(projectDetails.getCode());
		projectItem.setDescription(projectDetails.getDescription());
		projectItem.setName(projectDetails.getName());
		projectItem.setQuantity(projectDetails.getQuantity());
		projectItem.setPriority(projectDetails.getPriority());
		projectItem.setProjectSubType(projectDetails.getProjectSubType());
		projectItem.setPpi(projectDetails.getPpi());

		projectItem.setProjectLead(projectDetails.getProjectLead());

		projectItem.setPONumber(projectDetails.getPONumber());
		projectItem.setSowLineItem(projectDetails.getSowLineItem());
		projectItem.setNewCategory(projectDetails.getNewCategory());

		projectItem.setOrderBookRefNo(projectDetails.getOrderBookRefNo());
		projectItem.setProposalNo(projectDetails.getProposalNo());
		projectItem.setProjectLocation(projectDetails.getProjectLocation());
		projectItem.setClientPartner(projectDetails.getClientPartner());
		projectItem.setHoursUserSkillMonthlySplitJson(projectDetails.getHoursUserSkillMonthlySplitJson());

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

	public void deleteProjectItem(int projectId) {
		ProjectItem projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId,
				ProjectItem.class);

		if (projectItem != null) {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("projectId", projectId);
			famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("deleteAllUserTaskItemsByTaskIds"),
					dataMap);
			famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("deleteTaskItemsByProjectId"),
					dataMap);
			famstackDataAccessObjectManager.deleteItem(projectItem);
		}
	}

	public void archiveProjectItem(int projectId) {
		ProjectItem projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId,
				ProjectItem.class);
		if (projectItem != null) {
			projectItem.setDeleted(true);
			famstackDataAccessObjectManager.updateItem(projectItem);
		}

	}

	public ProjectItem getProjectItemById(int projectId) {
		return (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
	}

	public ProjectItem getLatestProjectByCode(String projectCode) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("projectCode", projectCode);

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeAllGroupQuery(HQLStrings.getString("getProjectTobeRecurring"), dataMap);

		if (projectItemList.size() > 0) {
			return (ProjectItem) projectItemList.get(0);
		}
		return null;
	}

	public void createProjectTask(TaskDetails taskDetails) {
		ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
		famstackProjectTaskManager.createTaskItem(taskDetails, projectItem);
		updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
	}

	public void createProjectExtraTimeTask(TaskDetails taskDetails) {
		ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
		famstackProjectTaskManager.createExtraTaskItem(taskDetails, projectItem);
		updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
	}

	public void reAssignTask(TaskDetails taskDetails, int newUserId, int taskActivityId, TaskStatus taskStatus) {
		famstackProjectTaskManager.reAssignTask(taskDetails, newUserId, taskActivityId, taskStatus);
	}

	public TaskActivityDetails playTask(int taskId, int taskActivityId) {
		return famstackProjectTaskManager.playTask(taskId, taskActivityId);

	}

	public void pauseTask(TaskDetails taskDetails) {
		famstackProjectTaskManager.pauseTask(taskDetails);
	}

	private void updateProjectStatusBasedOnTaskStatus(int projectId) {
		ProjectItem projectItem;
		projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
		ProjectStatus projectStatus = getProjectStatus(projectItem);
		updateProjectStatus(projectStatus, projectItem);
	}

	public void updateProjectTask(TaskDetails taskDetails) {
		ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
		famstackProjectTaskManager.updateTask(taskDetails, projectItem);
		updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
	}

	public void updateProjectStatus(ProjectStatus projectStatus, ProjectItem projectItem) {
		projectItem.setStatus(projectStatus);
		getFamstackDataAccessObjectManager().updateItem(projectItem);

	}

	public ProjectStatus getProjectStatus(ProjectItem projectItem) {
		// get project status
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

	public void deleteProjectTask(int taskId, int projectId) {
		famstackProjectTaskManager.deleteTaskItem(taskId);
		updateProjectStatusBasedOnTaskStatus(projectId);
	}

	public String getUserTaskActivityJson(Integer userId, int dayfilter) {
		return famstackProjectTaskManager.getUserTaskActivityJson(userId, dayfilter);
	}

	public List<TaskActivityDetails> getUserTaskActivity(Integer userId, String monthFilter) {
		return famstackProjectTaskManager.getUserTaskActivity(userId, monthFilter);
	}

	public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem) {
		return mapProjectItemToProjectDetails(projectItem, true);
	}

	public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem, boolean isFullLoad) {
		return mapProjectItemToProjectDetails(projectItem, isFullLoad, false);
	}

	public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem, boolean isFullLoad,
			boolean includeDeleted) {
		if (projectItem != null) {
			if (!includeDeleted && projectItem.getDeleted() == true) {
				return null;
			}
			ProjectDetails projectDetails = new ProjectDetails();
			projectDetails.setCode(projectItem.getCode());
			projectDetails.setPpi(projectItem.getPpi());
			projectDetails.setUserGroupId(projectItem.getUserGroupId());
			projectDetails.setId(projectItem.getProjectId());
			projectDetails.setName(projectItem.getName());
			projectDetails.setDeleted(projectItem.getDeleted());
			// remove
			projectDetails.setProjectTaskDeatils(
					famstackProjectTaskManager.mapProjectTaskDetails(projectItem.getTaskItems(), isFullLoad));
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
			projectDetails.setPpi(projectItem.getPpi());
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

				projectDetails.setSowLineItem(projectItem.getSowLineItem());
				projectDetails.setNewCategory(projectItem.getNewCategory());

				projectDetails.setOrderBookRefNo(projectItem.getOrderBookRefNo());
				projectDetails.setProposalNo(projectItem.getProposalNo());
				projectDetails.setProjectLocation(projectItem.getProjectLocation());
				projectDetails.setClientPartner(projectItem.getClientPartner());
				projectDetails.setHoursUserSkillMonthlySplitJson(projectItem.getHoursUserSkillMonthlySplitJson());

				projectDetails.setTags(projectItem.getTags());
				projectDetails.setType(projectItem.getType());
				if (projectItem.getReporter() != null) {
					projectDetails.setReporterName(
							projectItem.getReporter().getFirstName() + " " + projectItem.getReporter().getLastName());
				}
				projectDetails
						.setEmployeeDetails(famstackUserProfileManager.mapEmployeeDetails(projectItem.getReporter()));
				projectDetails.setWatchers(projectItem.getWatchers());
				projectDetails.setLastModifiedDate(projectItem.getLastModifiedDate());

				projectDetails.setProjectComments(
						famstackProjectCommentManager.mapProjectCommentDetails(projectItem.getProjectComments()));
				projectDetails.setProjectActivityItem(
						famstackProjectActivityManager.mapProjectActivityDetails(projectItem.getProjectActivityItem()));
			}
			return projectDetails;

		}
		return null;
	}

	private int getUnAssignedDuration(ProjectDetails projectDetails) {
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

	public List<ProjectDetails> getAllProjectDetailsList(Date startTime, boolean isFullLoad) {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startTime", startTime);

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("getAllProjectItems"), dataMap);

		getProjectsList(projectDetailsList, projectItemList, isFullLoad);
		return projectDetailsList;
	}

	public List<ProjectDetails> getPrimaryProjectsDetailList(Date startDate, Date endDate, Boolean includeArchive) {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", startDate);
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

		List<?> projectItemList = famstackDataAccessObjectManager.executeQueryOrderedBy(
				HQLStrings.getString("getPrimaryProjectsItems"), dataMap,
				HQLStrings.getString("getPrimaryProjectsItems-OrderBy"));

		getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
		return projectDetailsList;

		/*
		 * List<Object[]> projectItemResultList =
		 * famstackDataAccessObjectManager.executeSQLQuery(HQLStrings.getString(
		 * "getPrimaryProjectsItems"), dataMap); for (int i = 0; i <
		 * projectItemResultList.size(); i++) { ProjectDetails projectDetails =
		 * new ProjectDetails(); Object[] data = projectItemResultList.get(i);
		 * projectDetails.setId((Integer) data[0]);
		 * projectDetails.setName((String) data[1]);
		 * projectDetails.setCreatedDate((Timestamp) data[2]); String
		 * completionDateString = DateUtils.format((Date) data[3],
		 * DateUtils.DATE_TIME_FORMAT);
		 * projectDetails.setCompletionTime(completionDateString);
		 * projectDetails.setCode((String) data[4]);
		 * projectDetails.setStatus(ProjectStatus.valueOf((String) data[5]));
		 * projectDetailsList.add(projectDetails); }
		 */

	}

	public List<ProjectDetails> searchProjectDetails(String searchString, Boolean includeArchive) {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("searchString", "%" + searchString + "%");

		List<?> projectItemList = famstackDataAccessObjectManager.executeQueryOrderedBy(
				HQLStrings.getString("getPrimaryProjectsSearchItems"), dataMap,
				HQLStrings.getString("getPrimaryProjectsItems-OrderBy"));

		getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
		return projectDetailsList;
	}

	private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList, boolean isFullLoad) {
		getProjectsList(projectDetailsList, projectItemList, isFullLoad, false);
	}

	private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList, boolean isFullLoad,
			Boolean includeArchive) {
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

	public List<ProjectDetails> getAllProjectDetailsList(ProjectStatus projectStatus, Date startTime) {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("status", projectStatus);
		dataMap.put("startTime", startTime);

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("getProjectItemsByStatus"), dataMap);
		getProjectsList(projectDetailsList, projectItemList, false);
		return projectDetailsList;
	}

	public List<ProjectDetails> getMissedTimeLineProjectDetails(Date startTime) {
		Map<String, Object> dataMap = new HashMap<>();
		List<ProjectDetails> projectDetailsList = new ArrayList<>();
		dataMap.put("status", ProjectStatus.COMPLETED);
		dataMap.put("completionDate", new Date());
		dataMap.put("startTime", startTime);

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("getMissedTimeLineProjectItems"), dataMap);
		getProjectsList(projectDetailsList, projectItemList, false);

		return projectDetailsList;
	}

	public List<ProjectDetails> getAllMissedTimeLineProjectDetails(Date startTime) {
		Map<String, Object> dataMap = new HashMap<>();
		List<ProjectDetails> projectDetailsList = new ArrayList<>();
		dataMap.put("status", ProjectStatus.COMPLETED);
		dataMap.put("completionDate", new Date());
		dataMap.put("startTime", startTime);

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeAllGroupQuery(HQLStrings.getString("getMissedTimeLineProjectItems"), dataMap);
		getProjectsList(projectDetailsList, projectItemList, false);

		return projectDetailsList;
	}

	public List<ProjectDetails> getAllProjectDetailsList(String projectCode, int projectId, Boolean includeArchive) {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("code", projectCode);
		dataMap.put("projectId", projectId);

		List<?> projectItemList = famstackDataAccessObjectManager.executeQueryOrderedBy(
				HQLStrings.getString("getProjectItemsByCode"), dataMap,
				HQLStrings.getString("getProjectItemsByCode-OrderBy"));

		getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
		return projectDetailsList;
	}

	public long getAllProjectDetailsCount(ProjectStatus projectStatus, Date startTime) {
		Map<String, Object> dataMap = new HashMap<>();

		dataMap.put("startTime", startTime);
		dataMap.put("status", projectStatus);

		long count = famstackDataAccessObjectManager.getCount(HQLStrings.getString("getProjectItemCountByStatus"),
				dataMap);

		return count;
	}

	public long getAllMissedTimeLineProjectDetailsCount(Date startTime) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("status", ProjectStatus.COMPLETED);
		dataMap.put("completionDate", new Date());
		dataMap.put("startTime", startTime);

		long count = famstackDataAccessObjectManager
				.getCount(HQLStrings.getString("getMissedTimeLineProjectItemCountByStatus"), dataMap);

		return count;
	}

	public ProjectDetails getProjectDetails(int projectId) {
		ProjectDetails projectDetails = mapProjectItemToProjectDetails(
				(ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class), true);
		if (projectDetails != null) {
			projectDetails.setDuplicateProjects(
					getAllProjectDetailsList(projectDetails.getCode(), projectDetails.getId(), false));
		}
		return projectDetails;

	}

	public List<ProjectDetails> loadDuplicateProjects(int projectId, String projectCode, Boolean includeArchive) {
		return getAllProjectDetailsList(projectCode, projectId, includeArchive);
	}

	public Map<String, List<TaskDetails>> getProjectTasksDataList(Integer userId, int dayfilter) {
		Map<String, List<TaskDetails>> projectTaskDetailsMap = famstackProjectTaskManager.getAllProjectTask(userId);
		List<TaskDetails> backLogTasks = famstackProjectTaskManager.getBaklogProjectTasks(userId, dayfilter);
		moveInProgressBackLogTasksToInProgress(projectTaskDetailsMap, backLogTasks);
		projectTaskDetailsMap.put("BACKLOG", backLogTasks);

		sorTaksDetails(projectTaskDetailsMap);
		return projectTaskDetailsMap;
	}

	private void sorTaksDetails(Map<String, List<TaskDetails>> projectTaskDetailsMap) {
		List<TaskDetails> assignedTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.ASSIGNED);
		List<TaskDetails> inProgressTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.INPROGRESS);
		List<TaskDetails> completedTaskDetailsList = projectTaskDetailsMap.get(TaskStatus.COMPLETED);
		List<TaskDetails> backLogTaskDetailsList = projectTaskDetailsMap.get("BACKLOG");

		sortProjectData(assignedTaskDetailsList);
		sortProjectData(inProgressTaskDetailsList);
		sortProjectData(completedTaskDetailsList);
		sortProjectData(backLogTaskDetailsList);
	}

	private void sortProjectData(List<TaskDetails> taskDetailsList) {
		if (taskDetailsList != null) {
			Collections.sort(taskDetailsList, new Comparator<TaskDetails>() {
				@Override
				public int compare(TaskDetails taskDetails1, TaskDetails taskDetails2) {
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
			List<TaskDetails> backLogTasks) {
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
			Date adjustCompletionTimeDate) {
		TaskItem taskItem = famstackProjectTaskManager.updateTaskStatus(taskId, taskStatus, comments, adjustStartTime,
				adjustCompletionTimeDate);
		if (taskItem != null) {
			updateProjectStatusBasedOnTaskStatus(taskItem.getProjectItem().getProjectId());
		}

	}

	public String getUserTaskActivityForCalenderJson(String startDate, String endDate, int userId) {
		String userGroupId = getFamstackUserSessionConfiguration().getUserGroupId();
		return famstackProjectTaskManager.getUserTaskActivityJson(startDate, endDate, userId, userGroupId);
	}

	public String getUserTaskActivityForEmpUtlCalenderJson(String startDate, String endDate, String userGroupId) {
		return famstackProjectTaskManager.getUserTaskActivityJson(startDate, endDate, -1, userGroupId);
	}

	public String getProjectNameJson(String query) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("name", query + "%");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonProductListObject = new JSONObject();
		List<?> projectItems = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("searchForProjectNames"), dataMap);

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

	public String getProjectByOrderRefNoJson(String orderRefNo) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("orderBookRefNo", orderRefNo + "%");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonProductListObject = new JSONObject();
		List<?> projectItems = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("searchForProjectOrderBookRefNo"), dataMap);

		for (Object projectItemObj : projectItems) {
			ProjectItem projectItem = (ProjectItem) projectItemObj;

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", projectItem.getOrderBookRefNo());
			jsonObject.put("data", projectItem.getProjectId());
			jsonObject.put("name", projectItem.getName());
			jsonArray.put(jsonObject);
		}
		jsonProductListObject.put("suggestions", jsonArray);
		return jsonProductListObject.toString();
	}

	// to do check task item
	public String searchForProjectNamesCodePoIdJson(String query) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("name", "%" + query + "%");
		dataMap.put("code", "%" + query + "%");
		dataMap.put("PONumber", "%" + query + "%");
		dataMap.put("date", new Date());
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonProductListObject = new JSONObject();
		List<?> projectItems = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("searchForProjectNamesCodePoId"), dataMap);

		for (Object projectItemObj : projectItems) {
			ProjectItem projectItem = (ProjectItem) projectItemObj;

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", projectItem.getName());
			jsonObject.put("name", projectItem.getName());
			jsonObject.put("data", projectItem.getProjectId());
			jsonObject.put("projectCode", projectItem.getCode());
			jsonObject.put("projectPOId", projectItem.getPONumber());
			jsonObject.put("projectCategory", projectItem.getCategory());
			jsonObject.put("projectDate", projectItem.getCompletionTime());

			jsonArray.put(jsonObject);
		}
		jsonProductListObject.put("suggestions", jsonArray);
		return jsonProductListObject.toString();
	}

	public String getAllTasksJson(int projectId, int howManyOldData) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("projectId", projectId);
		dataMap.put("startDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), howManyOldData));
		List<?> taskItems = famstackDataAccessObjectManager.executeQuery(HQLStrings.getString("searchTasksByProjectId"),
				dataMap);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		if (taskItems != null) {
			JSONArray jsonTaskArray = new JSONArray();
			for (Object taskItemObj : taskItems) {
				TaskItem taskItem = (TaskItem) taskItemObj;
				JSONObject jsonTaskObject = new JSONObject();
				jsonTaskObject.put("name", taskItem.getName());
				jsonTaskObject.put("id", taskItem.getTaskId());
				jsonTaskObject.put("status", taskItem.getStatus());
				jsonTaskObject.put("timeTaken", taskItem.getActualTimeTaken());
				jsonTaskObject.put("startTime", taskItem.getStartTime());
				jsonTaskObject.put("endTime", taskItem.getCompletionTime());
				jsonTaskArray.put(jsonTaskObject);
			}

			jsonObject.put("tasks", jsonTaskArray);
		}
		return jsonObject.toString();
	}

	public List<ProjectDetails> getAllProjectDetailsList(Date startDate, Date endDate) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", startDate);
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("searchForProjectsForRepoting"), dataMap);
		logDebug("projectItemList" + projectItemList);
		logDebug("startDate" + startDate);
		logDebug("endDate" + endDate);
		getProjectsList(projectDetailsList, projectItemList, true);
		return projectDetailsList;
	}

	public List<DashBoardProjectDetails> getDashboardProjectData(Date startDate, Date endDate, String userGroupId) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", startDate);
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

		List<DashBoardProjectDetails> dashBoardProjectDetailsList = new ArrayList<>();

		String sqlQuery = HQLStrings.getString("projectTeamAssigneeReportSQL");
		sqlQuery += " and utai.user_grp_id = " + userGroupId;

		List<Object[]> projectItemList = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery, dataMap);
		logDebug("projectItemList" + projectItemList);
		logDebug("startDate" + startDate);
		logDebug("endDate" + endDate);
		mapDashBoardProjectsList(dashBoardProjectDetailsList, projectItemList);
		return dashBoardProjectDetailsList;

	}

	private List<String> getArrayToList(String ids) {
		List<String> idList = null;
		if (StringUtils.isNotBlank(ids)) {
			idList = new ArrayList(java.util.Arrays.asList(ids.split("#")));
		}
		return idList;
	}

	public List<DashboardUtilizationDetails> dashboardAllUtilization(Date startDate, Date endDate, String userGroupId,
			String accountId, String teamId, String subTeamId, String userId, boolean isResouceUtilization,
			boolean isTotalUtilization) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", startDate);
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));
		String type;
		List<String> accountIdsList = getArrayToList(accountId);
		List<String> subTeamIdsList = getArrayToList(subTeamId);
		List<String> resourceIdsList = getArrayToList(userId);

		String sqlQuery = HQLStrings.getString("projectOverAllUtilizationSQL");
		String groupBy = " group by ";
		if (StringUtils.isNotBlank(userId) && !isResouceUtilization) {
			sqlQuery += " and " + getCompareQuery("uai.id", resourceIdsList);
			groupBy += "pi.account_id";
			type = "Accounts";
		} else if (StringUtils.isNotBlank(subTeamId)) {
			sqlQuery += " and " + getCompareQuery("pi.team_id", subTeamIdsList);
			groupBy += "pi.team_id";
			type = "Sub Teams";
		} else if (StringUtils.isNotBlank(teamId)) {
			sqlQuery += " and " + getCompareQuery("pi.account_id", accountIdsList);
			groupBy += "pi.team_id";
			type = "Sub Teams";
		} else if (StringUtils.isNotBlank(accountId)) {
			sqlQuery += " and " + getCompareQuery("pi.account_id", accountIdsList);
			groupBy += "pi.team_id";
			type = "Teams";
		} else {
			groupBy += "pi.account_id";
			type = "Accounts";
		}

		if (isResouceUtilization) {
			groupBy = " group by uai.id";
		}

		if (isTotalUtilization) {
			groupBy += ", DATE_FORMAT(utai.actual_start_time, '%Y-%m-%d')";
		}

		sqlQuery += " and utai.user_grp_id = " + userGroupId;
		logInfo("dashboardAllUtilization Date " + dataMap);
		logInfo("dashboardAllUtilization : " + sqlQuery + groupBy);

		List<Object[]> overAllUtilization = famstackDataAccessObjectManager
				.executeAllSQLQueryOrderedBy(sqlQuery + groupBy, dataMap);
		return mapDashboardUtilizationDetails(overAllUtilization, type);

	}

	private String getCompareQuery(String coloumn, List<String> IdsList) {
		String query = "";
		if (IdsList != null) {
			if (IdsList.size() == 1) {
				return coloumn + "=" + IdsList.get(0);
			} else {
				for (String id : IdsList) {
					if (query == "") {
						query += "(";
					} else {
						query += " or ";
					}
					query += coloumn + " = " + id;
				}
				query += ")";
			}
		}
		return query;
	}

	private List<DashboardUtilizationDetails> mapDashboardUtilizationDetails(List<Object[]> overAllUtilization,
			String type) {
		List<DashboardUtilizationDetails> dashboardUtilizationDetailsList = new ArrayList<>();
		for (int i = 0; i < overAllUtilization.size(); i++) {
			DashboardUtilizationDetails dashboardUtilizationDetails = new DashboardUtilizationDetails();
			Object[] data = overAllUtilization.get(i);
			dashboardUtilizationDetails.setActualTaskStartTime((Date) data[0]);
			if (data[1] != null) {
				dashboardUtilizationDetails.setBillableMins(Double.valueOf(((BigDecimal) data[1]).doubleValue()));
			}
			if (data[2] != null) {
				dashboardUtilizationDetails.setNonBillableMins(Double.valueOf(((BigDecimal) data[2]).doubleValue()));
			}
			dashboardUtilizationDetails.setAccountId((Integer) data[3]);
			dashboardUtilizationDetails.setSubTeamId((Integer) data[4]);
			dashboardUtilizationDetails.setUserId((Integer) data[5]);
			dashboardUtilizationDetails.setType(type);
			ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap()
					.get(dashboardUtilizationDetails.getSubTeamId());
			if (projectSubTeamDetails != null) {
				dashboardUtilizationDetails.setTeamId(projectSubTeamDetails.getTeamId());
			}
			dashboardUtilizationDetailsList.add(dashboardUtilizationDetails);
		}
		return dashboardUtilizationDetailsList;
	}

	@Deprecated
	private String getFilterQuery(String filters) {
		String query = null;
		if (StringUtils.isNotBlank(filters)) {
			String filter[] = filters.split("#");
			if (filter.length > 0) {
				for (String filterData : filter) {
					String filterItem[] = filterData.split("$");
					if (filterItem.length > 0) {
						query += " and pi.";
						query += filterItem[0] + "=" + filterItem[1];
					}
				}
			}
		}
		logDebug("Dashboard Filter query " + query);
		return query == null ? "" : query;
	}

	public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(Date startDate, Date endDate,
			boolean getUnique, boolean addSameTaskActTime, Integer userId) {
		return getAllProjectTaskAssigneeData(startDate, endDate, getUnique, addSameTaskActTime, userId, null);
	}

	public List<ProjectTaskActivityDetails> getAllProjectTaskAssigneeData(Date startDate, Date endDate,
			boolean getUnique, boolean addSameTaskActTime, Integer userId, List<String> userGroupIds) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startDate, 0));
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

		List<ProjectTaskActivityDetails> projectDetailsList = new ArrayList<>();
		List<ProjectTaskActivityDetails> projectDetailsUniqueTasksList = new ArrayList<>();
		List<ProjectTaskActivityDetails> allTaskActProjectDetailsList = new ArrayList<>();
		String sqlQuery = HQLStrings.getString("projectTeamAssigneeReportSQL");
		if (CollectionUtils.isEmpty(userGroupIds)) {
			userGroupIds = Collections.singletonList(getFamstackUserSessionConfiguration().getUserGroupId());
		}
		if (userId == null) {
				String userGroupIdsArray = org.apache.commons.lang3.StringUtils.join(userGroupIds, ",");
				sqlQuery += " and utai.user_grp_id in (" + userGroupIdsArray + ")";
		}
		if (userId != null) {
			sqlQuery += " and uai.id = " + userId;
		}

		sqlQuery += " " + HQLStrings.getString("projectTeamAssigneeReportSQL-OrderBy");

		List<Object[]> projectItemList = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery, dataMap);
		logDebug("projectItemList" + projectItemList);
		logDebug("startDate" + startDate);
		logDebug("endDate" + endDate);
		mapProjectsList(projectDetailsList, projectDetailsUniqueTasksList, allTaskActProjectDetailsList,
				projectItemList);

		if (!addSameTaskActTime) {
			return allTaskActProjectDetailsList;
		}

		if (getUnique) {
			return projectDetailsUniqueTasksList;
		}
		return projectDetailsList;
	}

	public List<POEstimateProjectTaskActivityDetails> getAllProjectPOEstimateDuration(Date startDate, Date endDate,
			List<String> userGroupIds) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startDate, 0));
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

		List<POEstimateProjectTaskActivityDetails> projectDetailsList = new ArrayList<>();
		String sqlQuery = HQLStrings.getString("poEstimateReportSQL");
		if (CollectionUtils.isEmpty(userGroupIds)) {
			userGroupIds = Collections.singletonList(getFamstackUserSessionConfiguration().getUserGroupId());
		}
		String userGroupIdArray = org.apache.commons.lang3.StringUtils.join(userGroupIds, ",");
		sqlQuery += " and utai.user_grp_id in ( " + userGroupIdArray + ")";

		sqlQuery += " " + HQLStrings.getString("poEstimateReportSQL-OrderBy");

		List<Object[]> projectItemList = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery, dataMap);
		logDebug("projectItemList" + projectItemList);
		logDebug("startDate" + startDate);
		logDebug("endDate" + endDate);
		mapPOEstimateProjectsList(projectDetailsList, projectItemList);

		return projectDetailsList;
	}

	private void mapPOEstimateProjectsList(List<POEstimateProjectTaskActivityDetails> projectDetailsList,
			List<Object[]> projectItemList) {
		for (int i = 0; i < projectItemList.size(); i++) {
			POEstimateProjectTaskActivityDetails projectTaskActivityDetails = new POEstimateProjectTaskActivityDetails();
			Object[] data = projectItemList.get(i);
			projectTaskActivityDetails.setProjectStartTime((Date) data[0]);
			projectTaskActivityDetails.setProjectCompletionTime((Date) data[1]);
			projectTaskActivityDetails.setProjectCode((String) data[2]);
			projectTaskActivityDetails.setProjectId((Integer) data[3]);
			projectTaskActivityDetails.setProjectNumber((String) data[4]);
			projectTaskActivityDetails.setProjectName((String) data[5]);
			projectTaskActivityDetails.setProjectStatus(ProjectStatus.valueOf((String) data[6]));
			if (data[7] != null) {
				projectTaskActivityDetails.setProjectType(ProjectType.valueOf((String) data[7]));
			}
			String projectCategory = (String) data[8];

			projectTaskActivityDetails.setProjectTeamId((Integer) data[9]);
			projectTaskActivityDetails.setProjectClientId((Integer) data[10]);

			projectTaskActivityDetails.setProjectAccountId((Integer) data[11]);
			projectTaskActivityDetails.setProjectLead((Integer) data[12]);
			projectTaskActivityDetails.setSowLineItem((String) data[13]);
			
			String newProjectCategory = (String) data[14];

			if (!StringUtils.isNotBlank(projectCategory)) {
				projectCategory = (String) data[17];
			}

			if (!StringUtils.isNotBlank(newProjectCategory)) {
				newProjectCategory = (String) data[18];
			}

			projectTaskActivityDetails.setProjectCategory(projectCategory);
			projectTaskActivityDetails.setNewProjectCategory(newProjectCategory);

			projectTaskActivityDetails.setProjectDurationHrs((Integer) data[15]);
			projectTaskActivityDetails.setTaskActivityDuration(((BigDecimal) data[16]).intValue());

			projectTaskActivityDetails.setOrderBookId((String) data[20]);
			projectTaskActivityDetails.setProposalNumber((String) data[21]);
			projectTaskActivityDetails.setClientPartner((String) data[22]);
			projectTaskActivityDetails.setDeliveryLead((Integer) data[23]);
			EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getAllUsersMap()
					.get(projectTaskActivityDetails.getDeliveryLead());
			projectTaskActivityDetails
					.setDeliveryLeadName(employeeDetails != null ? employeeDetails.getFirstName() : "");

			projectTaskActivityDetails.setLocation((String) data[24]);
			projectTaskActivityDetails.setEstHoursByMonthSkills(convertStringToJsonObject((String) data[25]));
			projectTaskActivityDetails.setUserGroupName(getUserGroupName((String) data[26]));
			projectDetailsList.add(projectTaskActivityDetails);
		}

	}

	private Map convertStringToJsonObject(String estJson) {
		if (StringUtils.isNotBlank(estJson)) {
			return FamstackUtils.getJsonObjectFromJson(estJson);
		}
		return null;
	}

	private void mapProjectsList(List<ProjectTaskActivityDetails> projectDetailsList,
			List<ProjectTaskActivityDetails> projectDetailsUniqueTasksList,
			List<ProjectTaskActivityDetails> allTaskActProjectDetailsList, List<Object[]> projectItemList) {

		/*
		 * projectStartTime projectCompletionTime projectCode projectId
		 * projectNumber projectName projectStatus projectType projectCategory
		 * projectTeamId projectClientId taskName taskActivityStartTime
		 * taskActivityDuration userId
		 */

		Map<String, ProjectTaskActivityDetails> projectCacheMap = new HashMap<>();
		Map<String, ProjectTaskActivityDetails> projectUniqueItemCacheMap = new HashMap<>();

		ProjectTaskActivityDetails projectTaskActivityDetailsTmp;
		ProjectTaskActivityDetails projectUniqueItemDetails;

		for (int i = 0; i < projectItemList.size(); i++) {
			Object[] data = projectItemList.get(i);
			if (Objects.isNull(data[3])) {
				continue;
			}
			ProjectTaskActivityDetails projectTaskActivityDetails = new ProjectTaskActivityDetails();
			
			projectTaskActivityDetails.setProjectStartTime((Date) data[0]);
			projectTaskActivityDetails.setProjectCompletionTime((Date) data[1]);
			projectTaskActivityDetails.setProjectCode((String) data[2]);
			projectTaskActivityDetails.setProjectId((Integer) data[3]);
			projectTaskActivityDetails.setProjectNumber((String) data[4]);
			projectTaskActivityDetails.setProjectName((String) data[5]);
			projectTaskActivityDetails.setProjectStatus(ProjectStatus.valueOf((String) data[6]));
			if (data[7] != null) {
				projectTaskActivityDetails.setProjectType(ProjectType.valueOf((String) data[7]));
			}

			projectTaskActivityDetails.setProjectTeamId((Integer) data[9]);
			projectTaskActivityDetails.setProjectClientId((Integer) data[10]);

			projectTaskActivityDetails.setTaskName((String) data[11]);
			projectTaskActivityDetails.setTaskActivityStartTime((Date) data[12]);
			projectTaskActivityDetails.setTaskActivityEndTime((Date) data[17]);

			projectTaskActivityDetails.setTaskPausedTime((Date) data[18]);
			projectTaskActivityDetails.setTaskStatus(TaskStatus.valueOf((String) data[19]));
			Integer taskActivityDuration = (Integer) data[13];

			if (projectTaskActivityDetails.getTaskStatus() != TaskStatus.COMPLETED
					&& projectTaskActivityDetails.getTaskActivityEndTime() == null
					&& projectTaskActivityDetails.getTaskPausedTime() != null) {

				taskActivityDuration = DateUtils.getTimeDifference(TimeInType.MINS,
						projectTaskActivityDetails.getTaskPausedTime().getTime(),
						projectTaskActivityDetails.getTaskActivityStartTime().getTime());

			} else if (projectTaskActivityDetails.getTaskStatus() == TaskStatus.INPROGRESS
					&& projectTaskActivityDetails.getTaskActivityEndTime() == null
					&& projectTaskActivityDetails.getTaskPausedTime() == null) {

				taskActivityDuration = DateUtils.getTimeDifference(TimeInType.MINS, new Date().getTime(),
						projectTaskActivityDetails.getTaskActivityStartTime().getTime());

			}

			projectTaskActivityDetails.setTaskActivityDuration(taskActivityDuration);
			projectTaskActivityDetails.setTaskActActivityDuration(taskActivityDuration);

			projectTaskActivityDetails.setUserId((Integer) data[14]);

			projectTaskActivityDetails.setTaskId((Integer) data[15]);
			projectTaskActivityDetails.setTaskActivityId((Integer) data[16]);
			projectTaskActivityDetails.setProjectAccountId((Integer) data[20]);

			String projectCategory = (String) data[8];
			String newProjectCategory = (String) data[23];

			if (!StringUtils.isNotBlank(projectCategory)) {
				projectCategory = (String) data[29];
			}

			if (!StringUtils.isNotBlank(newProjectCategory)) {
				newProjectCategory = (String) data[28];
			}

			projectTaskActivityDetails.setProjectCategory(projectCategory);
			projectTaskActivityDetails.setNewProjectCategory(newProjectCategory);

			projectTaskActivityDetails.setTaskCompletionComments((String) data[24]);

			projectTaskActivityDetails.setTaskActType(UserTaskType.valueOf((String) data[25]));
			projectTaskActivityDetails.setTaskActProjType(ProjectType.valueOf((String) data[26]));
			projectTaskActivityDetails.setTaskActCategory((String) data[27]);
			// projectTaskActivityDetails.setProjectDurationHrs((Integer)
			// data[31]);

			projectTaskActivityDetails.setOrderRefNumber((String) data[31]);
			projectTaskActivityDetails.setProposalNumber((String) data[32]);

			projectTaskActivityDetails.setClientPartner((String) data[33]);
			projectTaskActivityDetails.setDeliveryLead((Integer) data[34]);
			projectTaskActivityDetails.setLocation((String) data[35]);
			projectTaskActivityDetails.setEstHoursByMonthSkills(convertStringToJsonObject((String) data[36]));

			projectTaskActivityDetails.setClientName((String) data[37]);
			projectTaskActivityDetails.setRecordedTaskStartTime((Date) data[38]);
			projectTaskActivityDetails.setLastModifiedTime((Date) data[39]);
			projectTaskActivityDetails.setProjectSubType((String) data[40]);
			projectTaskActivityDetails.setUserGroupId((String) data[41]);

			String key = "D" + DateUtils.format((Date) data[12], DateUtils.DATE_FORMAT);
			key += "T" + data[15];
			key += "U" + data[14];

			String uniqueItemKey = "T" + data[15];
			uniqueItemKey += "U" + data[14];

			projectTaskActivityDetailsTmp = projectCacheMap.get(key);
			projectUniqueItemDetails = projectUniqueItemCacheMap.get(uniqueItemKey);
			allTaskActProjectDetailsList.add(projectTaskActivityDetails);
			if (projectTaskActivityDetailsTmp != null) {
				projectTaskActivityDetailsTmp.addToChildProjectActivityDetailsMap(projectTaskActivityDetails);
				projectTaskActivityDetailsTmp
						.setTaskActivityDuration(projectTaskActivityDetailsTmp.getTaskActivityDuration()
								+ projectTaskActivityDetails.getTaskActivityDuration());
			} else {
				projectCacheMap.put(key, projectTaskActivityDetails);
				projectDetailsList.add(projectTaskActivityDetails);

				if (projectUniqueItemDetails != null) {
					projectUniqueItemDetails.getSubItems().add(projectTaskActivityDetails);
				} else {
					projectUniqueItemCacheMap.put(uniqueItemKey, projectTaskActivityDetails);
					projectDetailsUniqueTasksList.add(projectTaskActivityDetails);
				}
			}

		}
	}

	private void mapDashBoardProjectsList(List<DashBoardProjectDetails> projectDetailsList,
			List<Object[]> projectItemList) {

		Map<String, DashBoardProjectDetails> projectCacheMap = new HashMap<>();
		DashBoardProjectDetails projectTaskActivityDetailsTmp;

		for (int i = 0; i < projectItemList.size(); i++) {
			DashBoardProjectDetails dashBoardProjectDetails = new DashBoardProjectDetails();
			Object[] data = projectItemList.get(i);
			dashBoardProjectDetails.setProjectStartTime((Date) data[0]);
			dashBoardProjectDetails.setProjectCompletionTime((Date) data[1]);
			dashBoardProjectDetails.setProjectCode((String) data[2]);
			dashBoardProjectDetails.setProjectId((Integer) data[3]);
			dashBoardProjectDetails.setProjectNumber((String) data[4]);
			dashBoardProjectDetails.setProjectName((String) data[5]);
			dashBoardProjectDetails.setProjectStatus(ProjectStatus.valueOf((String) data[6]));
			dashBoardProjectDetails.setProjectType(ProjectType.valueOf((String) data[7]));
			dashBoardProjectDetails.setProjectCategory((String) data[8]);
			dashBoardProjectDetails.setProjectTeamId((Integer) data[9]);
			dashBoardProjectDetails.setProjectClientId((Integer) data[10]);

			dashBoardProjectDetails.setTaskName((String) data[11]);
			dashBoardProjectDetails.setTaskActivityStartTime((Date) data[12]);
			dashBoardProjectDetails.setTaskActivityEndTime((Date) data[17]);

			dashBoardProjectDetails.setTaskPausedTime((Date) data[18]);
			dashBoardProjectDetails.setTaskStatus(TaskStatus.valueOf((String) data[19]));
			Integer taskActivityDuration = (Integer) data[13];

			if (dashBoardProjectDetails.getTaskStatus() != TaskStatus.COMPLETED
					&& dashBoardProjectDetails.getTaskActivityEndTime() == null
					&& dashBoardProjectDetails.getTaskPausedTime() != null) {

				taskActivityDuration = DateUtils.getTimeDifference(TimeInType.MINS,
						dashBoardProjectDetails.getTaskPausedTime().getTime(),
						dashBoardProjectDetails.getTaskActivityStartTime().getTime());

			} else if (dashBoardProjectDetails.getTaskStatus() == TaskStatus.INPROGRESS
					&& dashBoardProjectDetails.getTaskActivityEndTime() == null
					&& dashBoardProjectDetails.getTaskPausedTime() == null) {

				taskActivityDuration = DateUtils.getTimeDifference(TimeInType.MINS, new Date().getTime(),
						dashBoardProjectDetails.getTaskActivityStartTime().getTime());

			}

			dashBoardProjectDetails.setTaskActivityDuration(taskActivityDuration);
			dashBoardProjectDetails.setTaskActActivityDuration(taskActivityDuration);

			dashBoardProjectDetails.setUserId((Integer) data[14]);
			dashBoardProjectDetails.getAssigneeIdList().add(dashBoardProjectDetails.getUserId());
			dashBoardProjectDetails.setTaskId((Integer) data[15]);
			dashBoardProjectDetails.setTaskActivityId((Integer) data[16]);
			dashBoardProjectDetails.setProjectAccountId((Integer) data[20]);
			dashBoardProjectDetails.setProjectLead((Integer) data[21]);

			String key = "PRJ" + dashBoardProjectDetails.getProjectId();

			projectTaskActivityDetailsTmp = projectCacheMap.get(key);
			if (projectTaskActivityDetailsTmp != null) {
				projectTaskActivityDetailsTmp.getAssigneeIdList().add(dashBoardProjectDetails.getUserId());
				projectTaskActivityDetailsTmp.addToChildProjectActivityDetailsMap(dashBoardProjectDetails);
				projectTaskActivityDetailsTmp
						.setTaskActivityDuration(projectTaskActivityDetailsTmp.getTaskActivityDuration()
								+ dashBoardProjectDetails.getTaskActivityDuration());
			} else {
				projectCacheMap.put(key, dashBoardProjectDetails);
				projectDetailsList.add(dashBoardProjectDetails);
			}
		}
	}

	public void updateProjectItem(ProjectDetails projectDetails) {
		ProjectItem projectItem = getProjectItemById(projectDetails.getId());
		if (projectItem != null) {
			saveProjectItem(projectDetails, projectItem);
			famstackProjectActivityManager.createProjectActivityItemItem(projectItem,
					ProjectActivityType.PROJECT_DETAILS_UPDATED, null);
		}
	}

	public void updateProjectActivityItem(int projectId, ProjectActivityType projectActivityType, String description) {
		ProjectItem projectItem = getProjectItemById(projectId);
		if (projectItem != null) {
			famstackProjectActivityManager.createProjectActivityItemItem(projectItem,
					ProjectActivityType.PROJECT_DETAILS_UPDATED, description);
		}
	}

	public TaskDetails getProjectTaskById(int taskId) {
		return famstackProjectTaskManager.getTaskDetailsById(taskId);
	}

	public List<ProjectStatusDetails> getProjectItemByTypeCount(Date startTime, Date endTime) {
		List<ProjectStatusDetails> projectStatusDetailsList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startTime", startTime);
		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeSQLQuery(HQLStrings.getString("projectItemByTypeCountSQL"), dataMap);

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

	public List<TeamUtilizatioDetails> getTeamUtilizationJson(Date startTime, Date endTime) {
		List<TeamUtilizatioDetails> teamUtilizatioDetailsList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startTime", startTime);
		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeSQLQuery(HQLStrings.getString("teamUtilizationSQL"), dataMap);

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

	public List<ProjectCategoryDetails> getProjectCategoryJson(Date startTime, Date endTime) {
		List<ProjectCategoryDetails> projectCategoryDetailsList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startTime", startTime);
		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeSQLQuery(HQLStrings.getString("projectCategorySQL"), dataMap);

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

	public List<ClientProjectDetails> getClientProject(Date startTime, Date endTime) {
		List<ClientProjectDetails> clientProjectDetailsList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startTime", startTime);
		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeSQLQuery(HQLStrings.getString("clientProjectStatusSQL"), dataMap);

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

	public List<ProjectDetails> getAllProjectDetailsWithIn(Date startDate, Date startDate2) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", startDate);
		dataMap.put("startDate2", startDate2);

		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeAllGroupQuery(HQLStrings.getString("searchForProjectsWithIn"), dataMap);
		logDebug("projectItemList" + projectItemList);
		logDebug("startDate" + startDate);
		logDebug("startDate 2" + startDate2);
		getProjectsList(projectDetailsList, projectItemList, true);
		return projectDetailsList;
	}

	public List<ProjectDetails> getAllProjectDetailsEndTimeWithIn(Date endDate, Date endDate2) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));
		dataMap.put("endDate2", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate2, 0));

		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeAllGroupQuery(HQLStrings.getString("searchForProjectsEndWithIn"), dataMap);
		logDebug("projectItemList" + projectItemList);
		logDebug("endDate" + endDate);
		logDebug("endDate 2" + endDate2);
		getProjectsList(projectDetailsList, projectItemList, true);
		return projectDetailsList;
	}

	public List<TaskDetails> getProjectTaskDetails(int projectId) {
		return famstackProjectTaskManager.getProjectDetailsTaskDetailsByProjectId(projectId);
	}

	public RecurringProjectDetails mapRecurringProjectItem(RecurringProjectItem recurringProjectItem) {
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
			recurringProjectDetails.setTaskId(recurringProjectItem.getTaskId());
			recurringProjectDetails.setType(recurringProjectItem.getType());
			recurringProjectDetails.setRecurreOriginal(recurringProjectItem.getRecurreOriginal());
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

	public RecurringProjectItem mapRecurringProjectDetails(RecurringProjectDetails recurringProjectDetails) {
		RecurringProjectItem recurringProjectItem = null;
		if (recurringProjectDetails != null) {
			recurringProjectItem = new RecurringProjectItem();
			recurringProjectItem.setId(recurringProjectDetails.getId());
			recurringProjectItem.setCronExpression(recurringProjectDetails.getCronExpression());
			recurringProjectItem.setName(recurringProjectDetails.getName());
			recurringProjectItem.setProjectId(recurringProjectDetails.getProjectId());
			recurringProjectItem.setTaskId(recurringProjectDetails.getTaskId());
			recurringProjectItem.setType(recurringProjectDetails.getType());
			recurringProjectItem.setRecurreOriginal(recurringProjectDetails.getRecurreOriginal());
			recurringProjectItem.setProjectCode(recurringProjectDetails.getProjectCode());
			recurringProjectItem.setRequestedBy(recurringProjectDetails.getRequestedBy());
			recurringProjectItem.setUserGroupId(recurringProjectDetails.getUserGroupId());
		}
		return recurringProjectItem;
	}

	public List<RecurringProjectDetails> getAllRecurringProjects() {
		List<RecurringProjectItem> allRecurringProjectItems = (List<RecurringProjectItem>) famstackDataAccessObjectManager
				.getAllItems("RecurringProjectItem");
		List<RecurringProjectDetails> allRecurringProjectDetails = new ArrayList<>();
		if (allRecurringProjectDetails != null) {
			for (RecurringProjectItem recurringProjectItem : allRecurringProjectItems) {
				if (recurringProjectItem.getType() == RecurringType.PROJECT)
					allRecurringProjectDetails.add(mapRecurringProjectItem(recurringProjectItem));
			}
		}

		return allRecurringProjectDetails;
	}

	public RecurringProjectDetails getRecurringProjectDetails(String projectCode, int projectId) {
		return mapRecurringProjectItem(getRecurringProjectItem(projectCode, projectId));
	}

	public RecurringProjectItem getRecurringProjectItem(String projectCode, int projectId) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("projectCode", projectCode);
		List<?> RecurringProjectItems = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("getRecurringProject"), dataMap);

		if (RecurringProjectItems != null && RecurringProjectItems.size() > 0) {
			return (RecurringProjectItem) RecurringProjectItems.get(0);
		}

		return null;
	}

	public RecurringProjectDetails createRecurringProject(String projectCode, int projectId, String cronExpression,
			String recurringEndDate, boolean recurreOriginal) {
		return mapRecurringProjectItem(saveOrUpdateRecurringProject(projectCode, projectId, null, cronExpression,
				recurringEndDate, RecurringType.PROJECT, recurreOriginal));
	}

	private RecurringProjectItem saveOrUpdateRecurringProject(String projectCode, int projectId, Integer taskId,
			String cronExpression, String recurringEndDate, RecurringType type, boolean recurreOriginal) {
		RecurringProjectItem recurringProjectItem = null;
		if (type == RecurringType.PROJECT) {
			recurringProjectItem = getRecurringProjectItem(projectCode, projectId);
		} else {
			recurringProjectItem = getRecurringTaskItem(taskId);
		}

		if (recurringProjectItem == null) {
			recurringProjectItem = new RecurringProjectItem();
			recurringProjectItem.setName(projectCode);
			recurringProjectItem.setProjectId(projectId);
			recurringProjectItem.setProjectCode(projectCode);
			recurringProjectItem.setTaskId(taskId);
			recurringProjectItem.setType(type);
		}

		Date nextRun = FamstackUtils.getNextRunFromCron(cronExpression, new Date());

		String newCronExpression = cronExpression;
		if (cronExpression.contains("#")) {
			String secondCronExpression = cronExpression.contains("#1") ? cronExpression.replace("#1", "#3")
					: cronExpression.contains("#2") ? cronExpression.replace("#2", "#4") : "";
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
		recurringProjectItem.setRecurreOriginal(recurreOriginal);
		famstackDataAccessObjectManager.saveOrUpdateItem(recurringProjectItem);
		return recurringProjectItem;
	}

	public void deleteRecuringProjectDetails(int recurringProjectId) {
		RecurringProjectItem recurringProjectItem = (RecurringProjectItem) famstackDataAccessObjectManager
				.getItemById(recurringProjectId, RecurringProjectItem.class);
		if (recurringProjectItem != null) {
			famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
		}
	}

	public List<Object> getAllRecuringProjectCodes() {
		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeSQLQuery(HQLStrings.getString("recurringProjectCodesSQL"), null);
		List<Object> projectCodes = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			projectCodes.add(result.get(i));
		}
		return projectCodes;
	}

	public void createRecurringProjects() {
		List<RecurringProjectItem> recurringProjectItems = getAllrecurringProjectsForCreation();
		logDebug("recurringProjectItems :" + recurringProjectItems);
		if (recurringProjectItems != null) {
			for (RecurringProjectItem recurringProjectItem : recurringProjectItems) {
				int projectId = recurringProjectItem.getProjectId();
				if (recurringProjectItem.getEndDate() != null
						&& new Date().after(new Date(recurringProjectItem.getEndDate().getTime()))) {
					famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
					logInfo("Deleting recurring item after expiry, project id : " + projectId);
					continue;
				}
				if (recurringProjectItem.getType() == RecurringType.PROJECT) {
					String projectCode = recurringProjectItem.getProjectCode();
					ProjectItem projectItem = null;
					boolean recurreOriginal = !getFamstackApplicationConfiguration()
							.isRecurringByCode(recurringProjectItem.getUserGroupId());

					if ((recurringProjectItem.getRecurreOriginal() == null && !recurreOriginal)
							|| (recurringProjectItem.getRecurreOriginal() != null
									&& !recurringProjectItem.getRecurreOriginal())) {
						projectItem = getLatestProjectByCode(projectCode);
						logInfo("Recurring project by code : " + projectId + ", Recurring id "
								+ recurringProjectItem.getId());
					} else {
						projectItem = getProjectItemById(projectId);
						logInfo("Recurring project by id : " + projectId + ", Recurring id "
								+ recurringProjectItem.getId());
					}
					if (projectItem != null && !isExceedProjectTime(projectItem.getCompletionTime().getTime())) {
						Timestamp projectStartTime = getNewTimeForDuplicate(projectItem.getStartTime());

						int projectDuration = DateUtils.getTimeDifference(TimeInType.MINS,
								projectItem.getCompletionTime().getTime(), projectItem.getStartTime().getTime());

						if (projectDuration < 0) {
							projectDuration = 60 * 24 * 15;
						}

						Timestamp projectEndTime = new Timestamp(
								DateUtils.getNextPreviousDate(DateTimePeriod.MINUTE, projectStartTime, projectDuration)
										.getTime());

						List<TaskDetails> taskItemList = getUpdatedRecureTaskDetailsList(projectItem);

						quickDuplicateProject(projectItem, projectItem.getName(), projectItem.getDurationHrs(),
								projectStartTime, projectEndTime, taskItemList);
					} else {
						logError("Recurring project not found : " + projectId + ", Recurring id "
								+ recurringProjectItem.getId());
						try {
							famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
							famstackEmailSender
									.sendTextMessage(
											"ALERT ERROR - SERVER Recurring project not found, Project Id " + projectId
													+ ", Recurring id " + recurringProjectItem.getId(),
											"Reccuring failed");
						} catch (Exception e) {

						}
						continue;
					}

					if (recurringProjectItem.getRecurreOriginal() == null) {
						recurringProjectItem.setRecurreOriginal(recurreOriginal);
					}
				} else if (recurringProjectItem.getType() == RecurringType.TASK) {
					TaskItem taskItem = (TaskItem) famstackDataAccessObjectManager
							.getItemById(recurringProjectItem.getTaskId(), TaskItem.class);

					ProjectItem projectItem = taskItem != null ? taskItem.getProjectItem() : null;
					
					if (projectItem != null && isExceedProjectTime(projectItem.getCompletionTime().getTime())) {
						logInfo("Project time exceed for task so deleting recurring");
						taskItem = null;
					}
					
					if (taskItem == null) {
						famstackDataAccessObjectManager.deleteItem(recurringProjectItem);
						continue;
					}

					TaskDetails taskDetails = getTaskDetailsFromTaskItemForDuplicate(taskItem);
					TaskItem taskItemCreated = famstackProjectTaskManager.createTaskItem(taskDetails,
							taskItem.getProjectItem(), true);

					if (recurringProjectItem.getRecurreOriginal() != null
							&& !recurringProjectItem.getRecurreOriginal()) {
						recurringProjectItem.setTaskId(taskItemCreated.getTaskId());
					}
				}

				if (recurringProjectItem != null) {
					recurringProjectItem.setLastRun(recurringProjectItem.getNextRun());
					String cronExpression = recurringProjectItem.getCronExpression();
					Date nextRun = null;
					if (cronExpression.contains("|")) {
						String cronExpression1 = cronExpression.split("[|]")[0];
						String cronExpression2 = cronExpression.split("[|]")[1];

						nextRun = FamstackUtils.getNextRunFromCron(cronExpression1, recurringProjectItem.getNextRun());
						Date nextRun2 = FamstackUtils.getNextRunFromCron(cronExpression2,
								recurringProjectItem.getNextRun());

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

	}

	private boolean isExceedProjectTime(long completionTime) {
		Date completionDate = new Date(completionTime);
		return completionDate.before(new Date());
	}

	// to do remove
	private List<TaskDetails> getUpdatedRecureTaskDetailsList(ProjectItem projectItem) {
		List<TaskDetails> taskDetailsList = new ArrayList<>();

		for (TaskItem taskItem : projectItem.getTaskItems()) {

			if (taskItem.getCanRecure()) {
				TaskDetails taskDetails = getTaskDetailsFromTaskItemForDuplicate(taskItem);
				taskDetailsList.add(taskDetails);
			}

		}
		return taskDetailsList;
	}

	private TaskDetails getTaskDetailsFromTaskItemForDuplicate(TaskItem taskItem) {
		TaskDetails taskDetails = new TaskDetails();
		try {
			Map<Integer, EmployeeDetails> allEmployeeDetails = getFamstackApplicationConfiguration().getAllUsersMap();
			if (allEmployeeDetails != null && allEmployeeDetails.get(taskItem.getAssignee()) != null
					&& taskItem.getUserGroupId()
							.equalsIgnoreCase(allEmployeeDetails.get(taskItem.getAssignee()).getUserGroupId())) {
				taskDetails.setAssignee(taskItem.getAssignee());
			}
		} catch (Exception e) {
			logError("Recurring project task assignee get error", e);
			famstackEmailSender.sendTextMessage(
					"ALERT ERROR - SERVER Recurring project task assignee get error, task id " + taskItem.getTaskId(),
					"Reccuring task assignee failed");
		}
		Date taskStartTime = getNewTimeForDuplicate(taskItem.getStartTime());
		taskDetails.setName(taskItem.getName());
		taskDetails.setCanRecure(taskItem.getCanRecure());
		taskDetails.setDescription(taskItem.getDescription());
		taskDetails.setStartTime(DateUtils.format(taskStartTime, DateUtils.DATE_TIME_FORMAT));
		taskDetails.setDuration(taskItem.getDuration());
		taskDetails.setProjectTaskType(taskItem.getProjectTaskType());
		taskDetails.setProjectCategory(taskItem.getProjectCategory());
		taskDetails.setTaskCategory(taskItem.getTaskCategory());
		return taskDetails;
	}

	private Timestamp getNewTimeForDuplicate(Timestamp startTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);

		Calendar todaysCalender = Calendar.getInstance();
		todaysCalender.setTime(new Date());

		todaysCalender.set(todaysCalender.get(Calendar.YEAR), todaysCalender.get(Calendar.MONTH),
				todaysCalender.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

		logDebug("todaysCalender :" + todaysCalender);
		return new Timestamp(todaysCalender.getTimeInMillis());
	}

	private List<RecurringProjectItem> getAllrecurringProjectsForCreation() {
		Map<String, Object> dataMap = new HashMap<>();
		Date startTime = new Date();
		dataMap.put("startTime", startTime);

		List<?> recurrinProjectItems = famstackDataAccessObjectManager
				.executeAllGroupQuery(HQLStrings.getString("recurringProjectsWithIn"), dataMap);

		logDebug("recurrinProjectItems" + recurrinProjectItems);
		logDebug("startTime" + startTime);
		return (List<RecurringProjectItem>) recurrinProjectItems;

	}

	public void deleteProjects(List<Integer> projectIds, String type) {
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

	public void softDeleteProjectOlderThan(int days) {

		Date dateTill = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1 * days);
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("dateTill", dateTill);
		famstackDataAccessObjectManager.executeSQLUpdate(HQLStrings.getString("projectSoftDeleteOlderSQL"), dataMap);
	}

	public RecurringProjectItem getRecurringTaskItem(int taskId) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("taskId", taskId);
		List<?> RecurringProjectItems = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("getRecurringTask"), dataMap);

		if (RecurringProjectItems != null && RecurringProjectItems.size() > 0) {
			return (RecurringProjectItem) RecurringProjectItems.get(0);
		}

		return null;
	}

	public List<Object> getAllRecuringTaskIdsByProjectId(int projectId) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("projectId", projectId);

		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeSQLQuery(HQLStrings.getString("recurringTaskIdsByProjectIdSQL"), dataMap);
		List<Object> taskIds = new ArrayList<>();
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				taskIds.add(result.get(i));
			}
		}
		return taskIds;
	}

	public RecurringProjectDetails getRecurringTaskDetails(int taskId) {
		return mapRecurringProjectItem(getRecurringTaskItem(taskId));
	}

	public RecurringProjectDetails createRecurringTask(int projectId, int taskId, String cronExpression,
			String recurringEndDate, boolean recurreOriginal) {
		return mapRecurringProjectItem(saveOrUpdateRecurringProject(null, projectId, taskId, cronExpression,
				recurringEndDate, RecurringType.TASK, recurreOriginal));
	}

	public List<Object> getAllRecuringTaskByProjectId(int projectId) {
		return getAllRecuringTaskIdsByProjectId(projectId);
	}

	@SuppressWarnings("unchecked")
	public void sendAutoReportEmail() {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startTime", new Date());

		List<AutoReportingItem> autoReportingItems = (List<AutoReportingItem>) famstackDataAccessObjectManager
				.executeAllGroupQuery(HQLStrings.getString("autoReportWithIn"), dataMap);

		logDebug("autoReportingItems " + autoReportingItems);

		if (autoReportingItems != null) {

			for (AutoReportingItem autoReportingItem : autoReportingItems) {

				if (autoReportingItem.getEndDate() != null
						&& new Date().after(new Date(autoReportingItem.getEndDate().getTime()))) {
					logInfo("Deleting auto Reporting Item item after expiry, type : " + autoReportingItem.getType()
							+ ", Group id " + autoReportingItem.getUserGroupId());
					famstackDataAccessObjectManager.deleteItem(autoReportingItem);

					continue;
				}
				logDebug(autoReportingItem.getName());
				try {
					if (autoReportingItem.getEnabled()) {

						sendAutoReportingNotification(autoReportingItem, false, 8);
					}
					autoReportingItem.setLastRun(autoReportingItem.getNextRun());
					String cronExpression = autoReportingItem.getCronExpression();
					Date nextRun = null;
					if (cronExpression.contains("|")) {
						String cronExpression1 = cronExpression.split("[|]")[0];
						String cronExpression2 = cronExpression.split("[|]")[1];

						nextRun = FamstackUtils.getNextRunFromCron(cronExpression1, autoReportingItem.getNextRun());
						Date nextRun2 = FamstackUtils.getNextRunFromCron(cronExpression2,
								autoReportingItem.getNextRun());

						if (nextRun.after(nextRun2)) {
							nextRun = nextRun2;
						}

					} else {
						nextRun = FamstackUtils.getNextRunFromCron(cronExpression, autoReportingItem.getNextRun());
					}

					if (autoReportingItem.getType() == ReportType.WEEKWISE_USER_UTILIZATION_MONTHLY) {
						Calendar cal = DateUtils.getLastSundayOfMonthWeek();
						Date nextRunDate = FamstackUtils.getNextRunFromCron(cronExpression,
								autoReportingItem.getNextRun());
						if (cal != null) {
							Calendar nextRunCal = Calendar.getInstance();
							nextRunCal.setTime(nextRunDate);
							cal.set(Calendar.HOUR, nextRunCal.get(Calendar.HOUR));
							nextRun = cal.getTime();
						}
					}

					autoReportingItem.setNextRun(nextRun == null ? null : new Timestamp(nextRun.getTime()));

					famstackDataAccessObjectManager.saveOrUpdateItem(autoReportingItem);

				} catch (Exception e) {
					logError("sendUserActivityEmail failed!", e);
				}
			}

		}

	}

	public void sendAutoReportingNotification(AutoReportingItem autoReportingItem, boolean forceTrigger,
			int howManyPreviousDays) {
		List<String> toList = autoReportingItem.getToList() != null
				? Arrays.asList(autoReportingItem.getToList().split(",")) : new ArrayList<String>();

		List<String> ccList = autoReportingItem.getCcList() != null
				? Arrays.asList(autoReportingItem.getCcList().split(",")) : new ArrayList<String>();

		List<String> exludeMailList = autoReportingItem.getExcludeMails() != null
				? Arrays.asList(autoReportingItem.getExcludeMails().split(",")) : new ArrayList<String>();

		logDebug("toList " + toList);
		logDebug("ccList " + ccList);
		String userGroupId = autoReportingItem.getUserGroupId();
		ReportType reportType = autoReportingItem.getType();
		int lastHowManyDays = autoReportingItem.getLastHowManyDays();
		int startDays = autoReportingItem.getLastHowManyDays();

		sendAutoReportEmail(toList, ccList, exludeMailList, userGroupId, reportType, lastHowManyDays, startDays,
				autoReportingItem.getSubject(), autoReportingItem.getNotifyDefaulters(), forceTrigger,
				howManyPreviousDays);
	}

	public void sendAutoReportEmail(List<String> toList, List<String> ccList, List<String> excludeMailList,
			String userGroupId, ReportType reportType, int lastHowManyDays, int startDays, String subject,
			Boolean notifyDefaulters, boolean forceTrigger, int howManyPreviousDays) {
		Date startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), startDays);

		Date endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, new Date(), lastHowManyDays);

		List<String> dateList = getReportDateRangeList(startDate, endDate);
		String dateRange = getReportDateRangeString(dateList);

		UserGroupDetails userGroupDetails = getFamstackApplicationConfiguration().getUserGroupMap().get(userGroupId);
		List<String> userGroupIds = Collections.singletonList(userGroupId);
		List<EmployeeDetails> employeesList = getFamstackApplicationConfiguration().sortedUserList(userGroupId);

		Map<String, Object> notificationDataMap = new HashMap<>();
		notificationDataMap.put("TEAM_NAME", userGroupDetails.getName());
		notificationDataMap.put("REPORT_DATE", dateRange);
		notificationDataMap.put("TO_LIST", new HashSet<>(toList));
		notificationDataMap.put("CC_LIST", new HashSet<>(ccList));
		notificationDataMap.put("DATE_LIST", dateList);

		if (StringUtils.isNotBlank(subject)) {
			subject = subject.replace("{teamName}", userGroupDetails.getName());
			subject = subject.replace("{date}", dateRange);
		}

		notificationDataMap.put("subject", subject);

		if (reportType == ReportType.USER_SITE_ACTIVITY) {
			/******** user site activity start *******/

			Map<Integer, Map<String, UserTaskActivityItem>> nonBillativityMap = famstackUserActivityManager
					.getAllNonBillabileActivities(startDate, endDate);

			Map<Integer, Map<String, String>> userSiteActivityMap = famstackUserActivityManager
					.getAllUserSiteActivities(startDate, endDate, null);

			List<UserSiteActivityDetails> activityData = famstackUserActivityManager.getUserSiteActivityForReport(
					userSiteActivityMap, nonBillativityMap, employeesList, dateList, excludeMailList);
			if (activityData != null && !activityData.isEmpty()) {
				notificationDataMap.put("ACTIVITY_DATA", activityData);

				famstackNotificationServiceManager.notifyAll(NotificationType.USER_ACTIVITY_REPORT, notificationDataMap,
						null);

				if (notifyDefaulters) {
					List<String> emailList = !ccList.isEmpty() ? ccList : toList;
					notifyUserInactiveUser(activityData, new HashSet<>(emailList), dateRange,
							userGroupDetails.getName(), subject, dateList);
				}
			}
			/******** user site activity end *******/
		} else if (reportType == ReportType.USER_UTILIZATION) {
			
			List<UserUtilizationDetails> utilizationDetails = getAutoReportUtilizationDataForEmail(startDate, endDate,
					employeesList, userGroupIds, excludeMailList);

			logDebug(FamstackUtils.getJsonFromObject(utilizationDetails));
			if (utilizationDetails != null) {

				notificationDataMap.put("UTILIZATION_DATA", utilizationDetails);
				famstackNotificationServiceManager.notifyAll(NotificationType.USER_UTILIZATION_REPORT,
						notificationDataMap, null);

				if (notifyDefaulters) {
					List<String> emailList = !ccList.isEmpty() ? ccList : toList;
					notifyUserUnderOverUtilized(utilizationDetails, new HashSet<>(emailList), dateRange,
							userGroupDetails.getName(), subject, NotificationType.USER_UTILIZATION_REPORT_DEFAULTER);
				}
			}
		} else if (reportType == ReportType.WEEKWISE_USER_UTILIZATION_MONTHLY) {

			if (DateUtils.isLastSundayOfMonthWeek() || forceTrigger) {

				startDate = DateUtils.getFirstDayOfThisMonthWeek(
						DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1 * howManyPreviousDays));
				endDate = DateUtils
						.getLastSundayOfMonthWeek(
								DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1 * howManyPreviousDays))
						.getTime();
				System.out.println("Start date " + startDate);
				System.out.println("endDate date " + endDate);
				Map<String, String> weekRangeBtwTwoDates = DateUtils.getWeekRangeBetwwenTwoDates(startDate, endDate);

				notificationDataMap.put("DATE_LIST", weekRangeBtwTwoDates);

				List<String> yearMonthWeekNumberList = DateUtils.getYearMonthWeekNumberBetwwenTwoDates(startDate,
						endDate);

				notificationDataMap.put("WEEK_LIST", yearMonthWeekNumberList);

				List<UserUtilizationWeekWiseDetails> utilizationWeekWiseDetails = getAutoReportUtilizationWeekWiseDataForEmail(
						startDate, endDate, employeesList, userGroupIds, excludeMailList);

				if (utilizationWeekWiseDetails != null) {

					notificationDataMap.put("UTILIZATION_DATA", utilizationWeekWiseDetails);
					famstackNotificationServiceManager.notifyAll(NotificationType.WEEKWISE_USER_UTILIZATION_MONTHLY,
							notificationDataMap, null);
					if (notifyDefaulters) {
						List<String> emailList = !ccList.isEmpty() ? ccList : toList;
						notifyUserWeeklyWiseUnderUtilized(utilizationWeekWiseDetails, new HashSet<>(emailList),
								dateRange, userGroupDetails.getName(), subject, weekRangeBtwTwoDates,
								yearMonthWeekNumberList);
					}
				}
			}
		} else if (reportType == ReportType.WEEKLY_PO_ESTIMATION) {
			startDate = DateUtils
					.getFirstDayOfPrevioushWeek(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1 * 3));
			endDate = DateUtils
					.getLastDayOfPreviousWeek(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1 * 3));

			List<POEstimateProjectTaskActivityDetails> projectTaskActivityDetails = getAutoReportPOEstimationDataForEmail(
					startDate, endDate, employeesList, userGroupIds);
			if (projectTaskActivityDetails != null && !projectTaskActivityDetails.isEmpty()) {
				notificationDataMap.put("REPORT_DATE",
						getReportDateRangeString(getReportDateRangeList(startDate, endDate)));
				notificationDataMap.put("ESTIMATION_DATA", projectTaskActivityDetails);
				famstackNotificationServiceManager.notifyAll(NotificationType.WEEKLY_PO_ESTIMATION_REPORT,
						notificationDataMap, null);
			}
		}
	}

	public Map<String, Object> getReportData(List<String> userGroupIds, ReportType reportType, Date reportStartDate,
			Date reportEndDate) {
		Date startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, reportStartDate, 0);

		Date endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, reportEndDate, 0);

		List<String> dateList = getReportDateRangeList(startDate, endDate);
		String dateRange = getReportDateRangeString(dateList);

		//UserGroupDetails userGroupDetails = getFamstackApplicationConfiguration().getUserGroupMap().get(userGroupId);

		List<EmployeeDetails> employeesList = getFamstackApplicationConfiguration().sortedUserList(userGroupIds);

		Map<String, Object> reportDataMap = new HashMap<>();

		reportDataMap.put("REPORT_DATE", dateRange);
		reportDataMap.put("DATE_LIST", dateList);
		if (reportType == ReportType.TIME_SHEET_DUMP) {
			reportDataMap.put("DATA",
					getDailyTimesheetDump(startDate, endDate, employeesList, userGroupIds, reportType));
		} else if (reportType == ReportType.UTILIZATION_BY_SKILLS
				|| reportType == ReportType.UTILIZATION_BY_EMPLOYEE_BY_SKILLS
				|| reportType == ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY) {
			reportDataMap.put("DATA", getUtilizationByEmployeeProjecctCategoryOrSkill(startDate, endDate, employeesList,
					userGroupIds, reportType));
		} else if (reportType == ReportType.USER_SITE_ACTIVITY) {

			Map<Integer, Map<String, UserTaskActivityItem>> nonBillativityMap = famstackUserActivityManager
					.getAllNonBillabileActivities(startDate, endDate);

			Map<Integer, Map<String, String>> userSiteActivityMap = famstackUserActivityManager
					.getAllUserSiteActivities(startDate, endDate, null);

			List<UserSiteActivityDetails> activityData = famstackUserActivityManager.getUserSiteActivityForReport(
					userSiteActivityMap, nonBillativityMap, employeesList, dateList, null);
			reportDataMap.put("DATA", activityData);
		} else if (reportType == ReportType.USER_UTILIZATION || reportType == ReportType.WEEKLY_PROJECT_HOURS) {

			List<UserUtilizationDetails> utilizationDetails = getAutoReportUtilizationDataForEmail(startDate, endDate,
					employeesList, userGroupIds, null);
			reportDataMap.put("DATA", utilizationDetails);
		} else if (reportType == ReportType.WEEKWISE_USER_UTILIZATION_MONTHLY
				|| reportType == ReportType.WEEKWISE_USER_UTILIZATION_MONTHLY_ENE) {
			Map<String, String> weekRangeBtwTwoDates = null;
			List<String> yearMonthWeekNumberList = null;

			if (reportType == ReportType.WEEKWISE_USER_UTILIZATION_MONTHLY_ENE) {
				weekRangeBtwTwoDates = DateUtils.getWeekRangeBetwwenTwoDatesEndToEnd(startDate, endDate);
				yearMonthWeekNumberList = DateUtils.getYearMonthWeekNumberBetwwenTwoDatesENE(startDate, endDate);
			} else {
				weekRangeBtwTwoDates = DateUtils.getWeekRangeBetwwenTwoDates(startDate, endDate);
				yearMonthWeekNumberList = DateUtils.getYearMonthWeekNumberBetwwenTwoDates(startDate, endDate);
			}
			reportDataMap.put("DATE_LIST", weekRangeBtwTwoDates);
			reportDataMap.put("WEEK_LIST", yearMonthWeekNumberList);

			List<UserUtilizationWeekWiseDetails> utilizationWeekWiseDetails = getAutoReportUtilizationWeekWiseDataForEmail(
					startDate, endDate, employeesList, userGroupIds, null);
			reportDataMap.put("DATA", utilizationWeekWiseDetails);
			famstackNotificationServiceManager.notifyAll(NotificationType.WEEKWISE_USER_UTILIZATION_MONTHLY,
					reportDataMap, null);
		} else if (reportType == ReportType.WEEKLY_PO_ESTIMATION) {

			List<POEstimateProjectTaskActivityDetails> projectTaskActivityDetails = getAutoReportPOEstimationDataForEmail(
					startDate, endDate, employeesList, userGroupIds);
			reportDataMap.put("DATA", projectTaskActivityDetails);
		}

		return reportDataMap;
	}

	private List<DailyTimesheetDumpDetails> getDailyTimesheetDump(Date startDate, Date endDate,
			List<EmployeeDetails> employeesList, List<String> userGroupIds, ReportType reportType) {

		List<DailyTimesheetDumpDetails> dailyTimesheetDumpDetailsList = new ArrayList<>();
		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();

		if (startDate != null && endDate != null) {
			projectTaskAssigneeDataList
					.addAll(getAllProjectTaskAssigneeData(startDate, endDate, false, false, null, userGroupIds));
			projectTaskAssigneeDataList.addAll(famstackUserActivityManager.getAllNonBillableTaskActivities(startDate,
					endDate, false, false, null, userGroupIds));
			if (projectTaskAssigneeDataList != null && !projectTaskAssigneeDataList.isEmpty()) {
				for (ProjectTaskActivityDetails projectDetails : projectTaskAssigneeDataList) {

					DailyTimesheetDumpDetails dailyTimesheetDumpDetails = new DailyTimesheetDumpDetails();

					EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getAllUsersMap()
							.get(projectDetails.getUserId());
					if (employeeDetails != null) {
						dailyTimesheetDumpDetails.setFullName(employeeDetails.getFirstName());
						dailyTimesheetDumpDetails.setEmployeeeId(employeeDetails.getEmpCode());
					}

					employeeDetails = getFamstackApplicationConfiguration().getAllUsersMap()
							.get(projectDetails.getDeliveryLead());
					if (employeeDetails != null) {
						dailyTimesheetDumpDetails.setDeliveryLead(employeeDetails.getFirstName());
					}

					dailyTimesheetDumpDetails.setClientName(projectDetails.getClientName());
					dailyTimesheetDumpDetails.setProjectType(projectDetails.getProjectType());
					dailyTimesheetDumpDetails.setProjectCode(projectDetails.getProjectCode());
					dailyTimesheetDumpDetails.setProjectId(
							projectDetails.getProjectId() != null ? "" + projectDetails.getProjectId() : "");
					dailyTimesheetDumpDetails.setProjectNumber(projectDetails.getProjectNumber());
					dailyTimesheetDumpDetails.setOrderRefNumber(projectDetails.getOrderRefNumber());
					dailyTimesheetDumpDetails.setProposalNumber(projectDetails.getProposalNumber());
					dailyTimesheetDumpDetails.setProjectName(projectDetails.getProjectName());
					dailyTimesheetDumpDetails.setProjectStatus(
							projectDetails.getProjectStatus() != null ? "" + projectDetails.getProjectStatus() : "");
					dailyTimesheetDumpDetails.setProjectCategory(projectDetails.getProjectCategory());
					dailyTimesheetDumpDetails.setNewProjectCategory(projectDetails.getNewProjectCategory());
					dailyTimesheetDumpDetails.setTaskName(projectDetails.getTaskName());
					dailyTimesheetDumpDetails.setAccountName(projectDetails.getAccountName());
					dailyTimesheetDumpDetails.setTeamName(projectDetails.getTeamName());
					dailyTimesheetDumpDetails.setSubTeamName(projectDetails.getSubTeamName());
					dailyTimesheetDumpDetails.setTaskStartTime(projectDetails.getTaskStartTime());
					dailyTimesheetDumpDetails.setDurationInHours(projectDetails.getDurationInHours());
					dailyTimesheetDumpDetails.setActDurationInHours(projectDetails.getActDurationInHours());
					dailyTimesheetDumpDetails.setTaskCompletionComments(projectDetails.getTaskCompletionComments());
					dailyTimesheetDumpDetails.setTaskActivityStartTime(projectDetails.getTaskActivityStartTime());
					dailyTimesheetDumpDetails
							.setTaskRecordedActivityStartTime(projectDetails.getRecordedTaskStartTime());
					dailyTimesheetDumpDetails.setLastModifiedTime(projectDetails.getLastModifiedTime());
					dailyTimesheetDumpDetails.setActDurationInMins(projectDetails.getTaskActActivityDuration());
					String projectUserGropuId = projectDetails.getUserGroupId();
					if(projectUserGropuId != null) {
						UserGroupDetails userGroupDetails = getFamstackApplicationConfiguration().getUserGroupMap().get(projectUserGropuId);
						if(userGroupDetails !=null) {
							dailyTimesheetDumpDetails.setUserGroupName(userGroupDetails.getName());
						}
					}
					
					dailyTimesheetDumpDetailsList.add(dailyTimesheetDumpDetails);
				}
			}
		}

		Collections.sort(dailyTimesheetDumpDetailsList, new Comparator<DailyTimesheetDumpDetails>() {
			@Override
			public int compare(DailyTimesheetDumpDetails dailyTimesheetDumpDetails1,
					DailyTimesheetDumpDetails dailyTimesheetDumpDetails2) {
				return dailyTimesheetDumpDetails1.getFullName().compareTo(dailyTimesheetDumpDetails2.getFullName());
			}
		});
		return dailyTimesheetDumpDetailsList;
	}

	private Object getUtilizationByEmployeeProjecctCategoryOrSkill(Date startDate, Date endDate,
			List<EmployeeDetails> employeesList, List<String> userGroupIds, ReportType reportType) {

		List<UtilizationByUserSkillOrCategory> utilizationBySkillList = new ArrayList<>();
		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();

		if (startDate != null && endDate != null) {
			projectTaskAssigneeDataList
					.addAll(getAllProjectTaskAssigneeData(startDate, endDate, false, true, null, userGroupIds));
			projectTaskAssigneeDataList.addAll(famstackUserActivityManager.getAllNonBillableTaskActivities(startDate,
					endDate, false, true, null, userGroupIds));
			// month:userid:skill:type:hours
			Map<String, Map<Integer, Map<String, Map<String, Integer>>>> monthYearUserIdSkillHoursMap = new HashMap<>();
			Map<String, Map<Integer, Map<String, Set<String>>>> monthUserCategoryProjectAccountMap = new HashMap<>();

			if (projectTaskAssigneeDataList != null && !projectTaskAssigneeDataList.isEmpty()) {
				for (ProjectTaskActivityDetails projectDetails : projectTaskAssigneeDataList) {

					String skillOrCategory = projectDetails.getProjectCategory() == null ? "Other"
							: projectDetails.getProjectCategory();

					if (projectDetails.getProjectCategory() == null) {
						System.out.println("ProjectCategory NULL : " + projectDetails);
					}

					if (reportType == ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY
							&& "Holiday".equalsIgnoreCase(skillOrCategory)
							|| "Leave".equalsIgnoreCase(skillOrCategory)) {
						continue;
					}
					String monthYear = DateUtils.getMonthYear(projectDetails.getTaskActivityStartTime());
					
					int userId = projectDetails.getUserId();
					int userIdValue = 0;
					if (reportType != ReportType.UTILIZATION_BY_SKILLS) {
						userIdValue = userId;
					}
					Map<Integer, Map<String, Map<String, Integer>>> userIdSkillHoursMap = monthYearUserIdSkillHoursMap
							.get(monthYear);

					if (userIdSkillHoursMap == null) {
						userIdSkillHoursMap = new HashMap<>();
						monthYearUserIdSkillHoursMap.put(monthYear, userIdSkillHoursMap);
					}
					Map<Integer, Map<String, Set<String>>> userCategoryProjectAccountMap = monthUserCategoryProjectAccountMap
							.get(monthYear);

					if (userCategoryProjectAccountMap == null) {
						userCategoryProjectAccountMap = new HashMap<>();
						monthUserCategoryProjectAccountMap.put(monthYear, userCategoryProjectAccountMap);

					}
					Map<String, Map<String, Integer>> userSkillHoursMap = userIdSkillHoursMap.get(userIdValue);

					if (userSkillHoursMap == null) {
						userSkillHoursMap = new HashMap<>();
						userIdSkillHoursMap.put(userIdValue, userSkillHoursMap);
					}

					Map<String, Set<String>> categoryProjectAccountMap = userCategoryProjectAccountMap.get(userIdValue);
					if (categoryProjectAccountMap == null) {
						categoryProjectAccountMap = new HashMap<>();
						userCategoryProjectAccountMap.put(userIdValue, categoryProjectAccountMap);
					}

					if (reportType != ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY) {
						EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getAllUsersMap()
								.get(userId);
						skillOrCategory = employeeDetails != null && StringUtils.isNotBlank(employeeDetails.getSkills())
								? employeeDetails.getSkills() : "Other";
					}

					Map<String, Integer> hoursMap = userSkillHoursMap.get(skillOrCategory);

					if (hoursMap == null) {
						hoursMap = new HashMap<>();
						userSkillHoursMap.put(skillOrCategory, hoursMap);
					}

					Set<String> projectAccountMap = categoryProjectAccountMap.get(skillOrCategory);
					if (projectAccountMap == null) {
						projectAccountMap = new TreeSet<>();
						categoryProjectAccountMap.put(skillOrCategory, projectAccountMap);
					}
					if (projectDetails.getAccountName() != null) {
						projectAccountMap.add(projectDetails.getAccountName());
					}
					getUserUtilization(hoursMap, projectDetails);
				}
			}

			for (String monthYear : monthYearUserIdSkillHoursMap.keySet()) {

				for (Integer userId : monthYearUserIdSkillHoursMap.get(monthYear).keySet()) {
					List<UtilizationByUserSkillOrCategory> skillList = new ArrayList<>();
					for (String skillOrCategory : monthYearUserIdSkillHoursMap.get(monthYear).get(userId).keySet()) {
						UtilizationByUserSkillOrCategory utilizationByUserSkillOrCategory = new UtilizationByUserSkillOrCategory();
						utilizationByUserSkillOrCategory.setMonthYear(monthYear);
						utilizationByUserSkillOrCategory.setSkillOrCategory(skillOrCategory);
						utilizationByUserSkillOrCategory.setProjectAccounts(
								monthUserCategoryProjectAccountMap.get(monthYear).get(userId).get(skillOrCategory));
						if (reportType != ReportType.UTILIZATION_BY_SKILLS) {
							EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getAllUsersMap()
									.get(userId);
							if (employeeDetails != null) {
								utilizationByUserSkillOrCategory.setEmployeeCode(employeeDetails.getEmpCode());
								utilizationByUserSkillOrCategory.setEmployeeId(employeeDetails.getId());
								utilizationByUserSkillOrCategory.setDesignation(employeeDetails.getDesignation());
								utilizationByUserSkillOrCategory.setEmployeeName(employeeDetails.getFirstName());
								//TODO: change to get user group from project item
								utilizationByUserSkillOrCategory.setUserGroupName(getUserGroupName(employeeDetails.getUserGroupId()));
							}
						}
						Map<String, Integer> hoursMap = monthYearUserIdSkillHoursMap.get(monthYear).get(userId)
								.get(skillOrCategory);

						for (String type : hoursMap.keySet()) {
							if ("billable".equalsIgnoreCase(type)) {
								utilizationByUserSkillOrCategory.setBillableMins(hoursMap.get(type));
							} else if ("nonBillabile".equalsIgnoreCase(type)) {
								utilizationByUserSkillOrCategory.setNonBillableMins(hoursMap.get(type));
							} else if ("leave".equalsIgnoreCase(type)) {
								utilizationByUserSkillOrCategory.setLeaveMins(hoursMap.get(type));
							} else if ("holiday".equalsIgnoreCase(type)) {
								utilizationByUserSkillOrCategory.setHolidayMins(hoursMap.get(type));
							}
						}
						skillList.add(utilizationByUserSkillOrCategory);
					}
					if (reportType == ReportType.UTILIZATION_BY_SKILLS) {
						Collections.sort(skillList, new Comparator<UtilizationByUserSkillOrCategory>() {
							@Override
							public int compare(UtilizationByUserSkillOrCategory utilizationBySkill1,
									UtilizationByUserSkillOrCategory utilizationBySkill2) {
								return utilizationBySkill1.getSkillOrCategory()
										.compareTo(utilizationBySkill2.getSkillOrCategory());
							}
						});
					}
					utilizationBySkillList.addAll(skillList);
				}

			}
		}
		if (reportType != ReportType.UTILIZATION_BY_SKILLS) {
			Collections.sort(utilizationBySkillList, new Comparator<UtilizationByUserSkillOrCategory>() {
				@Override
				public int compare(UtilizationByUserSkillOrCategory utilizationBySkill1,
						UtilizationByUserSkillOrCategory utilizationBySkill2) {
					return utilizationBySkill1.getEmployeeName().compareTo(utilizationBySkill2.getEmployeeName());
				}
			});
		}
		return utilizationBySkillList;
	}

	private String getReportDateRangeString(List<String> dateList) {
		String dateRange = "Invalid";
		if (dateList != null && dateList.size() > 0) {
			dateRange = dateList.get(0);
			if (dateList.size() > 1) {
				dateRange += "-" + dateList.get(dateList.size() - 1);
			}
		}
		return dateRange;
	}

	private List<String> getReportDateRangeList(Date startDate, Date endDate) {
		Date tmpStartDate = new Date(startDate.getTime());
		List<String> dateList = new ArrayList<>();
		while (tmpStartDate.before(endDate)) {
			dateList.add(DateUtils.format(tmpStartDate, DateUtils.DATE_FORMAT));
			tmpStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, tmpStartDate, 1);
		}
		return dateList;
	}

	private List<POEstimateProjectTaskActivityDetails> getAutoReportPOEstimationDataForEmail(Date startDate,
			Date endDate, List<EmployeeDetails> employeesList, List<String> userGroupIds) {

		List<POEstimateProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
		if (startDate != null && endDate != null) {
			projectTaskAssigneeDataList.addAll(getAllProjectPOEstimateDuration(startDate, endDate, userGroupIds));
		}
		return projectTaskAssigneeDataList;
	}

	private void notifyUserWeeklyWiseUnderUtilized(List<UserUtilizationWeekWiseDetails> utilizationWeekWiseDetails,
			HashSet<String> ccEmailList, String dateRange, String name, String subject,
			Map<String, String> weekRangeBtwTwoDates, List<String> yearMonthWeekNumberList) {
		if (utilizationWeekWiseDetails != null) {
			Set<String> emailIds = new HashSet<>();
			List<UserUtilizationWeekWiseDetails> finalUtilizationData = new ArrayList<>();
			for (UserUtilizationWeekWiseDetails userUtilizationWeekWiseDetails : utilizationWeekWiseDetails) {
				if (userUtilizationWeekWiseDetails.isNotifyUsers()) {
					emailIds.add(userUtilizationWeekWiseDetails.getEmailId());
					finalUtilizationData.add(userUtilizationWeekWiseDetails);
				}
			}

			if (!finalUtilizationData.isEmpty()) {
				try {
					Map<String, Object> notificationDataMap = new HashMap<>();
					notificationDataMap.put("DATE_LIST", weekRangeBtwTwoDates);

					notificationDataMap.put("WEEK_LIST", yearMonthWeekNumberList);
					notificationDataMap.put("REPORT_DATE", dateRange);
					notificationDataMap.put("TO_LIST", emailIds);
					notificationDataMap.put("CC_LIST", ccEmailList);
					notificationDataMap.put("subject", subject);
					notificationDataMap.put("UTILIZATION_DATA", finalUtilizationData);

					famstackNotificationServiceManager.notifyAll(
							NotificationType.WEEKWISE_USER_UTILIZATION_MONTHLY_DEFAULTER, notificationDataMap, null);
				} catch (Exception e) {
					logError("Unable to notify in active user " + emailIds);
				}
			}

		}

	}

	@Async
	private void notifyUserInactiveUser(List<UserSiteActivityDetails> activityData, HashSet<String> ccEmailList,
			String dateRange, String teamName, String subject, List<String> dateList) {
		if (activityData != null) {
			Set<String> emailIds = new HashSet<>();
			List<UserSiteActivityDetails> finalActivityData = new ArrayList<>();
			for (UserSiteActivityDetails activityDetails : activityData) {
				if (activityDetails.isIncludeInactive()) {
					emailIds.add(activityDetails.getEmailId());
					finalActivityData.add(activityDetails);
				}
			}

			if (!finalActivityData.isEmpty()) {
				try {
					Map<String, Object> notificationDataMap = new HashMap<>();
					notificationDataMap.put("REPORT_DATE", dateRange);
					notificationDataMap.put("TO_LIST", emailIds);
					notificationDataMap.put("CC_LIST", ccEmailList);
					notificationDataMap.put("subject", subject);
					notificationDataMap.put("ACTIVITY_DATA", finalActivityData);

					famstackNotificationServiceManager.notifyAll(NotificationType.USER_ACTIVITY_REPORT_DEFAULTER,
							notificationDataMap, null);

				} catch (Exception e) {
					logError("Unable to notify in active user " + emailIds);
				}
			}
		}

	}

	@Async
	private void notifyUserUnderOverUtilized(List<UserUtilizationDetails> utilizationDetails,
			HashSet<String> ccEmailList, String dateRange, String teamName, String subject,
			NotificationType notificationType) {
		if (utilizationDetails != null) {
			Set<String> emailIds = new HashSet<>();
			List<UserUtilizationDetails> finalUtilizationData = new ArrayList<>();
			for (UserUtilizationDetails userUtilizationDetails : utilizationDetails) {
				if (userUtilizationDetails.isNotifyUsers()) {
					emailIds.add(userUtilizationDetails.getEmailId());
					finalUtilizationData.add(userUtilizationDetails);
				}
			}

			if (!finalUtilizationData.isEmpty()) {
				try {
					Map<String, Object> notificationDataMap = new HashMap<>();
					notificationDataMap.put("REPORT_DATE", dateRange);
					notificationDataMap.put("TO_LIST", emailIds);
					notificationDataMap.put("CC_LIST", ccEmailList);
					notificationDataMap.put("subject", subject);
					notificationDataMap.put("UTILIZATION_DATA", finalUtilizationData);

					famstackNotificationServiceManager.notifyAll(notificationType, notificationDataMap, null);
				} catch (Exception e) {
					logError("Unable to notify in active user " + emailIds);
				}
			}

		}

	}

	public List<UserUtilizationDetails> getAutoReportUtilizationDataForEmail(Date startDate, Date endDate,
			List<EmployeeDetails> employeesList, List<String> userGroupIds, List<String> excludeMailList) {

		int numberOfWorkingDays = DateUtils.getWorkingDaysBetweenTwoDates(startDate, endDate);
		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
		logDebug("startDate" + startDate);
		logDebug("endDate" + endDate);
		if (startDate != null && endDate != null) {
			projectTaskAssigneeDataList
					.addAll(getAllProjectTaskAssigneeData(startDate, endDate, true, true, null, userGroupIds));
			projectTaskAssigneeDataList.addAll(famstackUserActivityManager.getAllNonBillableTaskActivities(startDate,
					endDate, true, true, null, userGroupIds));
			FamstackUtils.sortProjectTaskAssigneeDataList(projectTaskAssigneeDataList,
					getFamstackApplicationConfiguration().getAllUsersMap());

			Map<Integer, Map<String, Integer>> userTotalHoursMap = new HashMap<>();
			Map<Integer, Map<Integer, UtilizationProjectDetails>> projectDetailsMap = new HashMap<>();
			Map<Integer, Set<Integer>> projectTeamMembers = new HashMap<>();

			List<UserUtilizationDetails> userUtilizationDetailsList = new ArrayList<>();
			Set<Integer> projectTaskUserSet = new HashSet<>();
			if (projectTaskAssigneeDataList != null && !projectTaskAssigneeDataList.isEmpty()) {
				for (ProjectTaskActivityDetails projectDetails : projectTaskAssigneeDataList) {
					logDebug("projectDetails.getTaskActivityDuration" + projectDetails.getTaskActivityDuration());
					logDebug("projectDetails.type" + projectDetails.getTaskActProjType());
					logDebug("projectDetails.tcat" + projectDetails.getTaskActCategory());

					logDebug("projectDetails.sub" + projectDetails.getSubItems());

					logDebug("projectDetails.child" + projectDetails.getChilds());
					int userId = projectDetails.getUserId();
					projectTaskUserSet.add(userId);

					Map<String, Integer> dateStringHoursMap = userTotalHoursMap.get(userId);
					if (dateStringHoursMap == null) {
						dateStringHoursMap = new HashMap<>();
						userTotalHoursMap.put(userId, dateStringHoursMap);
					}

					Set<Integer> teamMembers = projectTeamMembers.get(projectDetails.getProjectId());
					if (teamMembers == null) {
						teamMembers = new HashSet<>();
						projectTeamMembers.put(projectDetails.getProjectId(), teamMembers);
					}

					teamMembers.add(userId);

					Map<Integer, UtilizationProjectDetails> utilizationprojectDetailsMap = projectDetailsMap
							.get(userId);
					if (utilizationprojectDetailsMap == null) {
						utilizationprojectDetailsMap = new HashMap<>();
						projectDetailsMap.put(userId, utilizationprojectDetailsMap);
					}
					UtilizationProjectDetails userUtilizationProjectDetails = utilizationprojectDetailsMap
							.get(projectDetails.getProjectId());

					if (userUtilizationProjectDetails == null) {
						userUtilizationProjectDetails = new UtilizationProjectDetails();
						userUtilizationProjectDetails.setClientName(projectDetails.getClientName());
						userUtilizationProjectDetails.setEndDate(projectDetails.getProjectCompletionTime());

						userUtilizationProjectDetails.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
						userUtilizationProjectDetails.setWeekNumber(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
						userUtilizationProjectDetails.setYear(Calendar.getInstance().get(Calendar.YEAR));
							
						userUtilizationProjectDetails.setProjectName(projectDetails.getProjectName());
						userUtilizationProjectDetails.setProjectNumber(projectDetails.getProjectNumber());
						userUtilizationProjectDetails.setStartDate(projectDetails.getProjectStartTime());
						userUtilizationProjectDetails.setTeamName(projectDetails.getTeamName());
						userUtilizationProjectDetails.setAccountName(projectDetails.getAccountName());
						userUtilizationProjectDetails.setUserGroupName(getUserGroupName(projectDetails.getUserGroupId()));
						utilizationprojectDetailsMap.put(projectDetails.getProjectId(), userUtilizationProjectDetails);
					}

					getUserUtilization(dateStringHoursMap, projectDetails);
					if (projectDetails.getSubItems().size() > 0) {
						for (ProjectTaskActivityDetails subActivityDetails : projectDetails.getSubItems()) {
							logDebug("subActivityDetails.getTaskActProjType()"
									+ subActivityDetails.getTaskActProjType());
							logDebug("subActivityDetails.getTaskActivityDuration()"
									+ subActivityDetails.getTaskActivityDuration());
							getUserUtilization(dateStringHoursMap, subActivityDetails);
						}
					}
				}
			}
			List<UserUtilizationDetails> underOrOverUtilizedList = new ArrayList<>();
			List<UserUtilizationDetails> leaveOrHolidayUtilizedList = new ArrayList<>();

			List<EmployeeDetails> projectTaskEmployees = getProjectEmployeesList(projectTaskUserSet, employeesList);

			for (EmployeeDetails employeeDetails : projectTaskEmployees) {
				if (excludeMailList != null && excludeMailList.contains(employeeDetails.getEmail())) {
					continue;
				}

				UserUtilizationDetails userUtilizationDetails = new UserUtilizationDetails();
				
				if (getFamstackApplicationConfiguration().isEnableUserAcivtiveUtilization(userGroupIds.get(0))) {
					userUtilizationDetails.setUserExitDate(employeeDetails.getExitDate());
					userUtilizationDetails.setUserJoinDate(employeeDetails.getDateOfJoin());
					userUtilizationDetails.setNoOfWorkingDays(DateUtils.getUsersActualWorkingHours(numberOfWorkingDays,
							employeeDetails.getDateOfJoin(), employeeDetails.getExitDate(), startDate, endDate));
					userUtilizationDetails.setEnabledUserActiveUtilization(true);
				} else {
					userUtilizationDetails.setNoOfWorkingDays(numberOfWorkingDays);
					userUtilizationDetails.setEnabledUserActiveUtilization(false);
				}

				userUtilizationDetails.setReportingManager(getEmployeeReportingManager(employeeDetails));

				userUtilizationDetails.setEmployeeName(employeeDetails.getFirstName());
				if(!StringUtils.isNotBlank(userUtilizationDetails.getUserGroupName())) {
					userUtilizationDetails.setUserGroupName(getUserGroupName(employeeDetails.getUserGroupId()));
				}
				userUtilizationDetails.setEmpId(employeeDetails.getEmpCode());
				userUtilizationDetails.setFunded(employeeDetails.getFundedEmployee());
				userUtilizationDetails.setEmailId(employeeDetails.getEmail());

				Map<String, Integer> utilizationTypeMap = userTotalHoursMap.get(employeeDetails.getId());

				Map<Integer, UtilizationProjectDetails> utilizationProjectDetailsMap = projectDetailsMap
						.get(employeeDetails.getId());

				userUtilizationDetails
						.setTeamMembers(getProjectTeamMembersNames(utilizationProjectDetailsMap, projectTeamMembers));
				List<UtilizationProjectDetails> projectUtilizationDetails = new ArrayList<>();
				if (utilizationProjectDetailsMap != null) {
					projectUtilizationDetails.addAll(utilizationProjectDetailsMap.values());
				}
				userUtilizationDetails.setUtilizationProjectDetailsList(projectUtilizationDetails);
				setUtilizationNonBillableHours(userUtilizationDetails, utilizationTypeMap);

				if (userUtilizationDetails.getLeaveOrHolidayMins() > 0) {
					leaveOrHolidayUtilizedList.add(userUtilizationDetails);
				} else if (userUtilizationDetails.isUnderOrOverUtilized()) {
					underOrOverUtilizedList.add(userUtilizationDetails);
				} else {
					userUtilizationDetailsList.add(userUtilizationDetails);
				}
			}
			underOrOverUtilizedList.addAll(userUtilizationDetailsList);
			underOrOverUtilizedList.addAll(leaveOrHolidayUtilizedList);
			return underOrOverUtilizedList;
		}
		return null;
	}

	private List<EmployeeDetails> getProjectEmployeesList(Set<Integer> projectTaskUserSet,
			List<EmployeeDetails> employeeDetailsList) {
		List<EmployeeDetails> projectTaskEmployees = new ArrayList<>();
		projectTaskEmployees.addAll(employeeDetailsList);

		for (Integer projectTaskUserId : projectTaskUserSet) {
			EmployeeDetails employeeDetails = getFamstackApplicationConfiguration().getAllUsersMap()
					.get(projectTaskUserId);
			if (employeeDetails != null && !projectTaskEmployees.contains(employeeDetails)) {
				projectTaskEmployees.add(employeeDetails);
			}
		}
		return projectTaskEmployees;
	}

	private Set<String> getProjectTeamMembersNames(Map<Integer, UtilizationProjectDetails> utilizationProjectDetailsMap,
			Map<Integer, Set<Integer>> projectTeamMembers) {
		Set<String> teamMemberNames = new HashSet<>();
		if (utilizationProjectDetailsMap != null) {
			for (Integer projectId : utilizationProjectDetailsMap.keySet()) {
				UtilizationProjectDetails utilizationProjectDetails = utilizationProjectDetailsMap.get(projectId);
				Set<Integer> teamMembers = projectTeamMembers.get(projectId);

				if (teamMembers != null) {
					for (Integer userId : teamMembers) {
						EmployeeDetails empDetails = getFamstackApplicationConfiguration().getAllUsersMap().get(userId);
						if (empDetails != null) {
							teamMemberNames.add(empDetails.getFirstName());
						}
					}
				}
			}
		}
		return teamMemberNames;
	}

	private void setUtilizationNonBillableHours(UserUtilization userUtilization,
			Map<String, Integer> utilizationTypeMap) {
		if (utilizationTypeMap != null) {
			for (String type : utilizationTypeMap.keySet()) {
				if ("billable".equalsIgnoreCase(type)) {
					userUtilization.setBillableMins(utilizationTypeMap.get(type));
				} else if ("leaveOrHoliday".equalsIgnoreCase(type)) {
					userUtilization.setLeaveOrHolidayMins(utilizationTypeMap.get(type));
				} else if ("nonBillabile".equalsIgnoreCase(type)) {
					userUtilization.setNonBillableMins(utilizationTypeMap.get(type));
				} else if ("leave".equalsIgnoreCase(type)) {
					userUtilization.setLeaveMins(utilizationTypeMap.get(type));
				} else if ("holiday".equalsIgnoreCase(type)) {
					userUtilization.setHolidayMins(utilizationTypeMap.get(type));
				}
			}
		}
	}

	private List<UserUtilizationWeekWiseDetails> getAutoReportUtilizationWeekWiseDataForEmail(Date startDate,
			Date endDate, List<EmployeeDetails> employeesList, List<String> userGroupIds, List<String> excludeMailList) {
		List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
		List<UserUtilizationWeekWiseDetails> userUtilizationList = new ArrayList<>();
		List<UserUtilizationWeekWiseDetails> underOrOverUtilizedList = new ArrayList<>();

		if (startDate != null && endDate != null) {
			projectTaskAssigneeDataList
					.addAll(getAllProjectTaskAssigneeData(startDate, endDate, false, true, null, userGroupIds));
			projectTaskAssigneeDataList.addAll(famstackUserActivityManager.getAllNonBillableTaskActivities(startDate,
					endDate, false, true, null, userGroupIds));
			FamstackUtils.sortProjectTaskAssigneeDataList(projectTaskAssigneeDataList,
					getFamstackApplicationConfiguration().getAllUsersMap());

			Map<Integer, Map<String, Map<String, Integer>>> userDateWeekHoursMap = new HashMap<>();
			Set<Integer> projectTaskUserSet = new HashSet<>();
			// user -> weekId -> taskType -> hour
			if (projectTaskAssigneeDataList != null && !projectTaskAssigneeDataList.isEmpty()) {
				for (ProjectTaskActivityDetails projectDetails : projectTaskAssigneeDataList) {

					int userId = projectDetails.getUserId();
					projectTaskUserSet.add(userId);
					Map<String, Map<String, Integer>> weekTypeHoursMap = userDateWeekHoursMap.get(userId);
					if (weekTypeHoursMap == null) {
						weekTypeHoursMap = new HashMap<>();
						userDateWeekHoursMap.put(userId, weekTypeHoursMap);
					}

					String yearMonthWeekNumber = DateUtils
							.getYearMonthWeekNumber(projectDetails.getTaskActivityStartTime());

					Map<String, Integer> taskTypeHours = weekTypeHoursMap.get(yearMonthWeekNumber);

					if (taskTypeHours == null) {
						taskTypeHours = new HashMap<>();
						weekTypeHoursMap.put(yearMonthWeekNumber, taskTypeHours);
					}

					getUserUtilization(taskTypeHours, projectDetails);
				}
			}
			Map<String, Integer> numberOfDaysInAWeekMap = DateUtils.getWeekNumberWeekDayCountMap(startDate, endDate);

			List<EmployeeDetails> projectTaskEmployees = getProjectEmployeesList(projectTaskUserSet, employeesList);

			for (EmployeeDetails employeeDetails : projectTaskEmployees) {
				if (excludeMailList != null && excludeMailList.contains(employeeDetails.getEmail())) {
					continue;
				}
				List<String> yearMonthWeekNumberList = DateUtils.getYearMonthWeekNumberBetwwenTwoDates(startDate,
						endDate);

				UserUtilizationWeekWiseDetails userUtilizationWeekWiseDetails = new UserUtilizationWeekWiseDetails();
				userUtilizationWeekWiseDetails.createUtilizationMap(yearMonthWeekNumberList, numberOfDaysInAWeekMap);

				userUtilizationWeekWiseDetails.setReportingManager(getEmployeeReportingManager(employeeDetails));
				userUtilizationWeekWiseDetails.setEmployeeName(employeeDetails.getFirstName());
				userUtilizationWeekWiseDetails.setEmpId(employeeDetails.getEmpCode());
				userUtilizationWeekWiseDetails.setEmailId(employeeDetails.getEmail());
				// TODO: get user utilizaton from project item
				userUtilizationWeekWiseDetails.setUserGroupName(getUserGroupName(employeeDetails.getUserGroupId()));
				Map<String, Map<String, Integer>> utilizationWeekTypeMap = userDateWeekHoursMap
						.get(employeeDetails.getId());

				if (utilizationWeekTypeMap != null) {
					for (String weekDate : yearMonthWeekNumberList) {
						Map<String, Integer> utilizationTypeMap = utilizationWeekTypeMap.get(weekDate);
						if (utilizationTypeMap != null) {
							UserUtilization userUtilization = userUtilizationWeekWiseDetails.getUserUtilizationMap()
									.get(weekDate);

							setUtilizationNonBillableHours(userUtilization, utilizationTypeMap);
						}
					}
				}

				if (userUtilizationWeekWiseDetails.isUnderOrOverUtilized()) {
					underOrOverUtilizedList.add(userUtilizationWeekWiseDetails);
				} else {
					userUtilizationList.add(userUtilizationWeekWiseDetails);
				}
			}
			underOrOverUtilizedList.addAll(userUtilizationList);
			// underOrOverUtilizedList.addAll(leaveOrHolidayUtilizedList);
		}
		return underOrOverUtilizedList;
	}

	private String getEmployeeReportingManager(EmployeeDetails employeeDetails) {
		try {
			if (employeeDetails.getReportertingManagerEmailId() != null) {
				Integer reportingMangerId = getFamstackApplicationConfiguration().getUserIdMap()
						.get(employeeDetails.getReportertingManagerEmailId());
				if (reportingMangerId != null) {
					EmployeeDetails reportingEmployeeDetail = getFamstackApplicationConfiguration().getAllUsersMap()
							.get(reportingMangerId);
					if (reportingEmployeeDetail != null) {
						return reportingEmployeeDetail.getFirstName();
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	private void getUserUtilization(Map<String, Integer> dateStringHoursMap,
			ProjectTaskActivityDetails subActivityDetails) {
		String userUtilizationType = getTaskActivityType(subActivityDetails);

		if (!dateStringHoursMap.containsKey(userUtilizationType)) {
			dateStringHoursMap.put(userUtilizationType, subActivityDetails.getTaskActivityDuration());
		} else {
			dateStringHoursMap.put(userUtilizationType,
					dateStringHoursMap.get(userUtilizationType) + subActivityDetails.getTaskActivityDuration());
		}
	}

	private String getTaskActivityType(ProjectTaskActivityDetails subActivityDetails) {
		String userUtilizationType;
		if (subActivityDetails.getTaskActProjType() == ProjectType.BILLABLE) {
			userUtilizationType = "billable";
		} else {
			String taskActCategory = subActivityDetails.getTaskActCategory();
			if (FamstackConstants.HOLIDAY.equalsIgnoreCase(taskActCategory)) {
				userUtilizationType = "holiday";
			} else if (FamstackConstants.LEAVE.equalsIgnoreCase(taskActCategory)) {
				userUtilizationType = "leave";
			} else if (FamstackConstants.LEAVE_OR_HOLIDAY.equalsIgnoreCase(taskActCategory)) {
				userUtilizationType = "leaveOrHoliday";
			} else {
				userUtilizationType = "nonBillabile";
			}
		}
		return userUtilizationType;
	}

	public Map<String, Map<String, Object>> getTeamUtilizationChartData(List<String> groupIds, Date startDate,
			Date endDate) {
		Date chartStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startDate, 0);
		Date chartEndendDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0);
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", chartStartDate);
		dataMap.put("endDate", chartEndendDate);
		dataMap.put("groupIds", groupIds);

		List<Object[]> result = getFamstackDataAccessObjectManager()
				.executeAllSQLQueryOrderedBy(HQLStrings.getString("teamwiseUtilizationSQL"), dataMap);
		Map<String, Map<String, Object>> teamUtilizationData = new HashMap<>();
		for (int i = 0; i < result.size(); i++) {
			Object[] data = result.get(i);
			Map<String, Object> teamUtilizationMap = new HashMap<>();
			teamUtilizationMap.put("HOLIDAY", ((BigDecimal) data[1]).intValue());
			teamUtilizationMap.put("LEAVE", ((BigDecimal) data[2]).intValue());
			teamUtilizationMap.put("BILLABLE", ((BigDecimal) data[3]).intValue());
			teamUtilizationMap.put("NONBILLABLE", ((BigDecimal) data[4]).intValue());
			teamUtilizationMap.put("USERGROUPID", (String) data[0]);
			int noOfWorkingDays = DateUtils.getWorkingDaysBetweenTwoDates(chartStartDate, chartEndendDate);
			teamUtilizationMap.put("NUMBEROFWORKINGDAYS", noOfWorkingDays == 0 ? 1 : noOfWorkingDays);
			List<EmployeeDetails> listOfEmps = getFamstackApplicationConfiguration().getUsersByGroupMap().get(data[0]);
			Integer empCount = 0;
			if (listOfEmps != null) {
				empCount = listOfEmps.size();
			}
			teamUtilizationMap.put("NUMBEROFEMPS", empCount);
			teamUtilizationData.put((String) data[5], teamUtilizationMap);
		}

		return teamUtilizationData;
	}

	public Map<String, List<Map<String, Object>>> getTeamUtilizationComparisonChartData(String groupIds, String year,
			String displayWise) {

		String sqlQuery = HQLStrings.getString("teamwiseUtilizationComparisonSQL");
		sqlQuery += " and YEAR(actual_start_time) = " + year + " and utai.user_grp_id in (" + groupIds
				+ ")  GROUP BY DATE_FORMAT(utai.actual_start_time, "
				+ ("DateWise".equalsIgnoreCase(displayWise) ? "'%Y%m%d'" : "'%Y%m'") + ")";
		List<String> groupIdList = Arrays.asList(groupIds.split(","));
		if (groupIdList.size() > 1) {
			sqlQuery += " , utai.user_grp_id";
		}
		List<Object[]> result = getFamstackDataAccessObjectManager().executeAllSQLQueryOrderedBy(sqlQuery, null);
		Map<String, Integer> noOfWorkingDaysMap = DateUtils.getNumberOfworkingDaysMap(year);
		Map<String, List<Map<String, Object>>> teamUtilizationData = new HashMap<>();
		for (String groupId : groupIdList) {
			String groupName = getUserGroupName(groupId);
			teamUtilizationData.put(groupName, new ArrayList<Map<String, Object>>());
		}
		for (int i = 0; i < result.size(); i++) {
			Object[] data = result.get(i);
			String groupName = getUserGroupName((String) data[0]);

			Map<String, Object> teamUtilizationMap = new HashMap<>();
			teamUtilizationMap.put("USERGROUPID", (String) data[0]);
			teamUtilizationMap.put("YEAR", data[1]);
			teamUtilizationMap.put("MONTH", data[2]);
			teamUtilizationMap.put("DATE", data[3]);
			teamUtilizationMap.put("HOLIDAY", ((BigDecimal) data[4]).intValue());
			teamUtilizationMap.put("LEAVE", ((BigDecimal) data[5]).intValue());
			teamUtilizationMap.put("BILLABLE", ((BigDecimal) data[6]).intValue());
			teamUtilizationMap.put("NONBILLABLE", ((BigDecimal) data[7]).intValue());

			int noOfWorkingDays = "DateWise".equalsIgnoreCase(displayWise) ? 1
					: noOfWorkingDaysMap.get((String) data[1] + Integer.parseInt((String) data[2]));

			teamUtilizationMap.put("NUMBEROFWORKINGDAYS", noOfWorkingDays == 0 ? 1 : noOfWorkingDays);

			List<EmployeeDetails> listOfEmps = getFamstackApplicationConfiguration().getUsersByGroupMap().get(data[0]);
			Integer empCount = 0;
			if (listOfEmps != null) {
				empCount = listOfEmps.size();
			}
			teamUtilizationMap.put("NUMBEROFEMPS", empCount);

			teamUtilizationData.get(groupName).add(teamUtilizationMap);
		}

		return teamUtilizationData;
	}

	private String getUserGroupName(String groupId) {
		UserGroupDetails groupDetails = getFamstackApplicationConfiguration().getUserGroupMap().get(groupId);
		String groupName = "Other";
		if (groupDetails != null) {
			groupName = groupDetails.getName();
		}
		return groupName;
	}

	public List<ProjectDetails> searchProjectDetails(String orderBookRefNo, String proposalNo, Boolean includeArchive) {

		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		Map<String, Object> dataMap = new HashMap<>();
		String queryString = HQLStrings.getString("getPrimaryProjectsSearchByProposal");
		if (StringUtils.isNotBlank(orderBookRefNo) && StringUtils.isNotBlank(proposalNo)) {
			dataMap.put("orderBookRefNo", orderBookRefNo);
			dataMap.put("proposalNo", proposalNo);
			queryString = HQLStrings.getString("getPrimaryProjectsSearchByOrderBookOrProposal");
		} else if (StringUtils.isNotBlank(orderBookRefNo)) {
			queryString = HQLStrings.getString("getPrimaryProjectsSearchByOrderBook");
			dataMap.put("orderBookRefNo", orderBookRefNo);
		} else {
			dataMap.put("proposalNo", proposalNo);
		}

		List<?> projectItemList = famstackDataAccessObjectManager.executeQueryOrderedBy(queryString, dataMap,
				HQLStrings.getString("getPrimaryProjectsItems-OrderBy"));

		getProjectsList(projectDetailsList, projectItemList, false, includeArchive);
		return projectDetailsList; 
	}

	public List<ProjectTaskActivityDetails> getAllProjectWithoutTasksAssigneeData(Date startDate,Date endDate, List<String> userGroupIds) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, startDate, 0));
		dataMap.put("endDate", DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, endDate, 0));

		String sqlQuery = HQLStrings.getString("getProjectWithoutAssigneeData");
		String userGroupIdArray = org.apache.commons.lang3.StringUtils.join(userGroupIds, ",");
		sqlQuery += " and user_grp_id in (" + userGroupIdArray + ")";
	
		
		List<ProjectTaskActivityDetails> projectDetailsList = new ArrayList<>();

		List<Object[]> projectItemList = famstackDataAccessObjectManager.executeAllSQLQueryOrderedBy(sqlQuery, dataMap);
		logDebug("projectItemList" + projectItemList);

		
		for (int i = 0; i < projectItemList.size(); i++) {
			Object[] data = projectItemList.get(i);
			if (Objects.isNull(data[3])) {
				continue;
			}
			ProjectTaskActivityDetails projectTaskActivityDetails = new ProjectTaskActivityDetails();
			
			projectTaskActivityDetails.setProjectId((Integer) data[0]);
			projectTaskActivityDetails.setProjectName((String) data[1]);
			projectTaskActivityDetails.setUserGroupId((String) data[2]);
			projectTaskActivityDetails.setProjectCode((String) data[3]);

			if (data[4] != null) {
				projectTaskActivityDetails.setProjectType(ProjectType.valueOf((String) data[4]));
			}
			
			projectTaskActivityDetails.setProjectStartTime((Date) data[5]);
			//projectTaskActivityDetails.setTaskActivityStartTime((Date) data[5]);
			projectTaskActivityDetails.setProjectCompletionTime((Date) data[6]);

			projectTaskActivityDetails.setProjectStatus(ProjectStatus.valueOf((String) data[7]));

			projectTaskActivityDetails.setProjectAccountId((Integer) data[8]);
			projectTaskActivityDetails.setProjectTeamId((Integer) data[9]);
			projectTaskActivityDetails.setProjectClientId((Integer) data[10]);

			projectTaskActivityDetails.setProjectNumber((String) data[11]);
			projectTaskActivityDetails.setOrderRefNumber((String) data[12]);
			projectTaskActivityDetails.setProposalNumber((String) data[13]);
			projectTaskActivityDetails.setEstHoursByMonthSkills(convertStringToJsonObject((String) data[14]));
			projectTaskActivityDetails.setClientPartner((String) data[15]);
			projectTaskActivityDetails.setTaskActivityDuration(0);
			projectTaskActivityDetails.setProjectSubType((String) data[16]);
			projectDetailsList.add(projectTaskActivityDetails);
		}
		
		return projectDetailsList;
	}
}
