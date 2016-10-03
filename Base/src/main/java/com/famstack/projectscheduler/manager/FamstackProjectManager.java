package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.datatransferobject.ProjectActivityItem;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;

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

	public void createProjectItem(ProjectDetails projectDetails) {
		ProjectItem projectItem = new ProjectItem();
		projectItem.setCategory(projectDetails.getCategory());
		projectItem.setClientId(projectDetails.getClientId());
		projectItem.setCode(projectDetails.getCode());
		projectItem.setDescription(projectDetails.getDescription());
		projectItem.setName(projectDetails.getName());
		projectItem.setPriority(projectDetails.getPriority());
		projectItem.setStatus(ProjectStatus.NEW);
		projectItem.setTags(projectDetails.getTags());
		projectItem.setType(projectDetails.getType());
		projectItem.setReporter(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
		projectItem.setWatchers(projectDetails.getWatchers());
		ProjectActivityItem projectActivityItem = famstackProjectActivityManager
				.createProjectActivityItemItem(projectItem, ProjectActivityType.CREATED, null);
		famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
		famstackDataAccessObjectManager.saveOrUpdateItem(projectActivityItem);
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
			projectDetails.setCode(projectItem.getCode());
			projectDetails.setDescription(projectItem.getDescription());
			projectDetails.setDuration(projectItem.getDuration());
			projectDetails.setName(projectItem.getName());
			projectDetails.setPriority(projectItem.getPriority());
			projectDetails.setStartTime(projectItem.getStartTime());
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
