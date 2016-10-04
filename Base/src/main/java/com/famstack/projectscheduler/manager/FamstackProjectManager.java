package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
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
		projectItem.setCategory(projectDetails.getCategory());
		projectItem.setCode(projectDetails.getCode());
		projectItem.setDescription(projectDetails.getDescription());
		projectItem.setName(projectDetails.getName());
		projectItem.setPriority(projectDetails.getPriority());
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
		projectItem.setStatus(ProjectStatus.NEW);
		projectItem.setTags(projectDetails.getTags());
		projectItem.setType(projectDetails.getType());
		projectItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
		projectItem.setWatchers(projectDetails.getWatchers());
		famstackProjectActivityManager.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED, null);
		famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
	}

	public void deleteProjectItem(int projectId) {
		ProjectItem projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId,
				ProjectItem.class);
		if (projectItem != null) {
			famstackDataAccessObjectManager.deleteItem(projectItem);
		}
	}

	public ProjectItem getProjectItemById(int projectId) {
		return (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class);
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
			String startDateString = DateUtils.format(projectItem.getStartTime(), DateUtils.DATE_TIME_FORMAT);
			String completionDateString = DateUtils.format(projectItem.getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
			projectDetails.setStartTime(startDateString);
			projectDetails.setCompletionTime(completionDateString);

			projectDetails.setStatus(projectItem.getStatus());
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
			return projectDetails;

		}
		return null;
	}

	public List<ProjectDetails> getAllProjectDetailsList() {
		List<ProjectDetails> projectDetailsList = new ArrayList<>();
		List<?> projectItemList = famstackDataAccessObjectManager.getAllItems("ProjectItem");
		if (projectItemList != null) {
			for (Object projectItemObj : projectItemList) {
				ProjectItem projectItem = (ProjectItem) projectItemObj;
				ProjectDetails projectDetails = mapProjectItemToProjectDetails(projectItem);
				if (projectDetails != null) {
					projectDetailsList.add(projectDetails);
				}
			}
		}
		return projectDetailsList;
	}

	public ProjectDetails getProjectDetails(int projectId) {
		return mapProjectItemToProjectDetails(
				(ProjectItem) famstackDataAccessObjectManager.getItemById(projectId, ProjectItem.class));
	}
}
