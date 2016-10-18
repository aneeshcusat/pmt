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
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.TaskItem;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.util.DateUtils;

/**
 * The Class FamstackProjectManager.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */
@Component
public class FamstackProjectManager extends BaseFamstackManager {

	@Resource
	FamstackProjectCommentManager famstackProjectCommentManager;

	@Resource
	FamstackProjectActivityManager famstackProjectActivityManager;

	@Resource
	FamstackProjectTaskManager famstackProjectTaskManager;

	public void createProjectItem(ProjectDetails projectDetails) {
		ProjectItem projectItem = new ProjectItem();
		projectItem.setStatus(ProjectStatus.NEW);
		saveProjectItem(projectDetails, projectItem);
		famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED, null);
	}

	private void saveProjectItem(ProjectDetails projectDetails, ProjectItem projectItem) {
		projectItem.setCategory(projectDetails.getCategory());
		projectItem.setCode(projectDetails.getCode());
		projectItem.setDescription(projectDetails.getDescription());
		projectItem.setName(projectDetails.getName());
		projectItem.setPriority(projectDetails.getPriority());
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

	public void deleteProjectItem(int projectId) {
		ProjectItem projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId,
				ProjectItem.class);
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

	public ProjectItem getProjectItemById(int projectId) {
		return (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
	}

	public void createProjectTask(TaskDetails taskDetails) {
		ProjectItem projectItem = getProjectItemById(taskDetails.getProjectId());
		famstackProjectTaskManager.createTaskItem(taskDetails, projectItem);
		updateProjectStatusBasedOnTaskStatus(taskDetails.getProjectId());
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

		List<TaskStatus> taskStatsList = new ArrayList<>();
		int projectDuration = projectItem.getDuration();
		if (projectItem != null) {
			for (TaskItem taskItem : projectItem.getTaskItems()) {
				TaskStatus taskStatus = taskItem.getStatus();
				taskStatsList.add(taskStatus);
				projectDuration -= taskItem.getDuration();
			}
		}
		if (projectDuration != 0) {
			return ProjectStatus.UNASSIGNED;
		} else if (taskStatsList.contains(TaskStatus.NEW) && (taskStatsList.contains(TaskStatus.ASSIGNED)
				|| (taskStatsList.contains(TaskStatus.INPROGRESS) || taskStatsList.contains(TaskStatus.COMPLETED)))) {
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

	public String getUserTaskActivityJson() {
		return famstackProjectTaskManager.getUserTaskActivityJson();
	}

	public ProjectDetails mapProjectItemToProjectDetails(ProjectItem projectItem) {

		if (projectItem != null) {
			ProjectDetails projectDetails = new ProjectDetails();

			projectDetails.setCategory(projectItem.getCategory());
			projectDetails.setClientId(projectItem.getClientId());
			projectDetails.setAccountId(projectItem.getAccountId());
			projectDetails.setTeamId(projectItem.getTeamId());

			projectDetails.setCode(projectItem.getCode());
			projectDetails.setDescription(projectItem.getDescription());
			projectDetails.setDuration(projectItem.getDuration());
			projectDetails.setName(projectItem.getName());
			projectDetails.setPriority(projectItem.getPriority());
			projectDetails.setComplexity(projectItem.getComplexity());
			projectDetails.setPONumber(projectItem.getPONumber());
			String startDateString = DateUtils.format(projectItem.getStartTime(), DateUtils.DATE_TIME_FORMAT);
			String completionDateString = DateUtils.format(projectItem.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
			projectDetails.setStartTime(startDateString);
			projectDetails.setCompletionTime(completionDateString);

			projectDetails.setTags(projectItem.getTags());
			projectDetails.setType(projectItem.getType());
			if (projectItem.getReporter() != null) {
				projectDetails.setReporterName(
						projectItem.getReporter().getFirstName() + " " + projectItem.getReporter().getLastName());
			}
			projectDetails.setWatchers(projectItem.getWatchers());
			projectDetails.setCreatedDate(projectItem.getCreatedDate());
			projectDetails.setLastModifiedDate(projectItem.getLastModifiedDate());
			projectDetails.setId(projectItem.getProjectId());
			projectDetails.setProjectComments(
					famstackProjectCommentManager.mapProjectCommentDetails(projectItem.getProjectComments()));
			projectDetails.setProjectActivityItem(
					famstackProjectActivityManager.mapProjectActivityDetails(projectItem.getProjectActivityItem()));
			projectDetails.setProjectTaskDeatils(
					famstackProjectTaskManager.mapProjectTaskDetails(projectItem.getTaskItems()));

			int unAssignedDuration = getUnAssignedDuration(projectDetails);
			projectDetails.setUnAssignedDuration(unAssignedDuration);
			projectDetails.setStatus(projectItem.getStatus());

			return projectDetails;

		}
		return null;
	}

	private int getUnAssignedDuration(ProjectDetails projectDetails) {
		int unassignedCount = projectDetails.getDuration();
		if (projectDetails.getProjectTaskDeatils() != null) {
			for (TaskDetails taskDetails : projectDetails.getProjectTaskDeatils()) {
				unassignedCount -= taskDetails.getDuration();
			}
		}
		return unassignedCount;
	}

	public List<ProjectDetails> getAllProjectDetailsList() {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();
		List<?> projectItemList = famstackDataAccessObjectManager.getAllItems("ProjectItem");
		getProjectsList(projectDetailsList, projectItemList);
		return projectDetailsList;
	}

	private void getProjectsList(List<ProjectDetails> projectDetailsList, List<?> projectItemList) {
		if (projectItemList != null) {
			for (Object projectItemObj : projectItemList) {
				ProjectItem projectItem = (ProjectItem) projectItemObj;
				ProjectDetails projectDetails = mapProjectItemToProjectDetails(projectItem);
				if (projectDetails != null) {
					projectDetailsList.add(projectDetails);
				}
			}
		}
	}

	public List<ProjectDetails> getAllProjectDetailsList(ProjectStatus projectStatus) {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("status", projectStatus);

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("getProjectItemsByStatus"), dataMap);
		getProjectsList(projectDetailsList, projectItemList);
		return projectDetailsList;
	}

	public List<ProjectDetails> getAllProjectDetailsList(String projectCode, int projectId) {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("code", projectCode);
		dataMap.put("projectId", projectId);

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("getProjectItemsByCode"), dataMap);
		getProjectsList(projectDetailsList, projectItemList);
		return projectDetailsList;
	}

	public long getAllProjectDetailsCount(ProjectStatus projectStatus) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("status", projectStatus);

		long count = famstackDataAccessObjectManager.getCount(HQLStrings.getString("getProjectItemCountByStatus"),
				dataMap);

		return count;
	}

	public long getAllMissedTimeLineProjectDetailsCount() {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("status", ProjectStatus.COMPLETED);
		dataMap.put("completionDate", new Date());

		long count = famstackDataAccessObjectManager
				.getCount(HQLStrings.getString("getMissedTimeLineProjectItemCountByStatus"), dataMap);

		return count;
	}

	public ProjectDetails getProjectDetails(int projectId) {
		ProjectDetails projectDetails = mapProjectItemToProjectDetails(
				(ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class));
		projectDetails.setDuplicateProjects(getAllProjectDetailsList(projectDetails.getCode(), projectDetails.getId()));
		return projectDetails;
	}

	public Map<String, ArrayList<TaskDetails>> getProjectTasksDataList(int userId) {
		return famstackProjectTaskManager.getAllProjectTask(userId);
	}

	public void updateTaskStatus(int taskId, int taskActivityId, TaskStatus taskStatus, String comments) {
		TaskItem taskItem = famstackProjectTaskManager.updateTaskStatus(taskId, taskActivityId, taskStatus, comments);
		updateProjectStatusBasedOnTaskStatus(taskItem.getProjectItem().getProjectId());

	}

	public String getUserTaskActivityForCalenderJson(String startDate, String endDate) {
		return famstackProjectTaskManager.getUserTaskActivityJson(startDate, endDate);
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

	public List<ProjectDetails> getAllProjectDetailsList(Date startDate, Date endDate) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("startDate", startDate);
		dataMap.put("endDate", endDate);

		List<ProjectDetails> projectDetailsList = new ArrayList<>();

		List<?> projectItemList = famstackDataAccessObjectManager
				.executeQuery(HQLStrings.getString("searchForProjectsForRepoting"), dataMap);
		logDebug("projectItemList" + projectItemList);
		logDebug("startDate" + startDate);
		logDebug("endDate" + endDate);
		getProjectsList(projectDetailsList, projectItemList);
		return projectDetailsList;
	}

	public void updateProjectItem(ProjectDetails projectDetails) {
		ProjectItem projectItem = getProjectItemById(projectDetails.getId());
		if (projectItem != null) {
			saveProjectItem(projectDetails, projectItem);
			famstackProjectActivityManager.createProjectActivityItemItem(projectItem,
					ProjectActivityType.PROJECT_DETAILS_UPDATED, null);
		}
	}
}
