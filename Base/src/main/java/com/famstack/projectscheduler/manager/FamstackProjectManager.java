package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ProjectCommentItem;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.employees.bean.ProjectCommentDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;

/**
 * The Class FamstackProjectManager.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */
@Component
public class FamstackProjectManager extends BaseFamstackService {

	/** The delivery interface data access object manager. */
	@Resource
	FamstackDataAccessObjectManager famstackDataAccessObjectManager;
	
	@Resource
	FamstackProjectCommentManager famstackProjectCommentManager;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public void createProjectItem(ProjectDetails projectDetails) {
		ProjectItem projectItem = null;
		
		if (projectDetails.getId() == 0) {
			 projectItem = new ProjectItem();
			 projectItem.setCreatedDate(new Timestamp((new java.util.Date()).getTime()));
			 projectItem.setCreatedBy(projectDetails.getCreatedBy());
		} else {
			projectItem = getProjectItemById(projectDetails.getId());
			if (projectItem == null) {
				projectItem = new ProjectItem();
				projectItem.setCreatedDate(new Timestamp((new java.util.Date()).getTime()));
				projectItem.setCreatedBy(projectDetails.getCreatedBy());
			} else {
				projectItem.setModifiedDate(new Timestamp((new java.util.Date()).getTime()));
				projectItem.setModifiedBy(projectDetails.getModifiedBy());
				
			}
		}
		projectItem.setCategory(projectDetails.getCategory());
		projectItem.setClientId(projectDetails.getClientId());
		projectItem.setCode(projectDetails.getCode());
		projectItem.setDescription(projectDetails.getDescription());
		projectItem.setDuration(projectDetails.getDuration());
		projectItem.setName(projectDetails.getName());
		projectItem.setPriority(projectDetails.getPriority());
		if (projectDetails.getStartTime() != null) {
			projectItem.setStartTime(new Timestamp(projectDetails.getStartTime().getTime()));
		}
		projectItem.setStatus(projectDetails.getStatus());
		projectItem.setTags(projectDetails.getTags());
		projectItem.setType(projectDetails.getType());
		projectItem.setReporter(famstackDataAccessObjectManager.getUserById(projectDetails.getReporter()));
		projectItem.setAssignee(famstackDataAccessObjectManager.getUserById(projectDetails.getAssignee()));
		projectItem.setReviewer(famstackDataAccessObjectManager.getUserById(projectDetails.getReviewer()));
		projectItem.setReview(projectDetails.getReview());
		projectItem.setWatchers(projectDetails.getWatchers());
		famstackDataAccessObjectManager.saveOrUpdateItem(projectItem);
		 
	}

	public ProjectItem getProjectItemById(int id) {
		return famstackDataAccessObjectManager.getProjectById(id);
	}
	

	public void deleteProjectItem(int projectId) {
		ProjectItem projectItem = getProjectItemById(projectId);
		if (projectItem != null) {
			famstackDataAccessObjectManager.deleteItem(projectItem);
		} 
	}
	
	public List<?> getAllProjectItems() {
		return famstackDataAccessObjectManager.getAllItems("ProjectItem");
	}

	public ProjectDetails getProjectDetailsFromProjectItem(
			ProjectItem projectItem) {
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
				projectDetails.setReporter(projectItem.getReporter().getId());
			}
			if (projectItem.getAssignee() != null) {
				projectDetails.setAssignee(projectItem.getAssignee().getId());
			}
			if (projectItem.getReviewer() != null) {
				projectDetails.setReviewer(projectItem.getReviewer().getId());
			}
			projectDetails.setReview(projectItem.getReview());
			projectDetails.setWatchers(projectItem.getWatchers());
			projectDetails.setCreatedDate(projectItem.getCreatedDate());
			projectDetails.setCreatedBy(projectItem.getCreatedBy());
			projectDetails.setModifiedDate(projectItem.getModifiedDate());
			projectDetails.setModifiedBy(projectItem.getModifiedBy());
			projectDetails.setId(projectItem.getId());
			if (projectItem.getReporter() != null) {
				projectDetails.setReporterName(projectItem.getReporter().getFirstName() + " " + projectItem.getReporter().getLastName());
			}
			if (projectItem.getAssignee() != null) {
				projectDetails.setAssigneeName(projectItem.getAssignee().getFirstName() + " " + projectItem.getAssignee().getLastName());
			}
			if (projectItem.getReviewer() != null) {
				projectDetails.setReviewerName(projectItem.getReviewer().getFirstName() + " " + projectItem.getReviewer().getLastName());
			}
			projectDetails.setProjectComments(getProjectCommentDetailsSet(projectItem.getProjectComments()));
			return projectDetails;
			
		}
		return null;
	}
	
	
	public Set<ProjectCommentDetails> getProjectCommentDetailsSet(Set<ProjectCommentItem> projectCommentItemSet) {
		if (projectCommentItemSet != null) {
			Set<ProjectCommentDetails> projectCommentDetailsSet = new HashSet<ProjectCommentDetails>();
			Iterator<ProjectCommentItem> iter = projectCommentItemSet.iterator();
			while (iter.hasNext()) {
				ProjectCommentItem commentItem = iter.next();
				ProjectCommentDetails commentDetails = famstackProjectCommentManager
						.getProjectCommentDetailsFromProjectCommentItem(commentItem);
				projectCommentDetailsSet.add(commentDetails);
			}
			return projectCommentDetailsSet;
		}
		
		return null;
	}
}
