package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ProjectCommentItem;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.ProjectCommentDetails;
import com.famstack.projectscheduler.security.user.FamstackUserProfileManager;

/**
 * The Class FamstackProjectCommentManager.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */
@Component
public class FamstackProjectCommentManager extends BaseFamstackService {

	/** The delivery interface data access object manager. */
	@Resource
	FamstackDataAccessObjectManager famstackDataAccessObjectManager;
	
	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public void createProjectCommentItem(ProjectCommentDetails projectCommentDetails) {
		ProjectCommentItem projectCommentItem = null;
		
		if (projectCommentDetails.getId() == 0) {
			 projectCommentItem = new ProjectCommentItem();
			 projectCommentItem.setCreatedDate(new Timestamp((new java.util.Date()).getTime()));
		} else {
			projectCommentItem = getProjectCommentItemById(projectCommentDetails.getId());
			if (projectCommentItem == null) {
				projectCommentItem = new ProjectCommentItem();
				projectCommentItem.setCreatedDate(new Timestamp((new java.util.Date()).getTime()));
			} else {
				projectCommentItem.setModifiedDate(new Timestamp((new java.util.Date()).getTime()));
			}
		}
		projectCommentItem.setDescription(projectCommentDetails.getDescription());
		projectCommentItem.setTitle(projectCommentDetails.getTitle());
		if (projectCommentDetails.getUser() != null) {
			UserItem commentUser = famstackDataAccessObjectManager.getUserById(projectCommentDetails.getUser().getId());
			commentUser.setId(projectCommentDetails.getUser().getId());
			projectCommentItem.setUser(commentUser);
		}
		if (projectCommentDetails.getProjectId() != 0) {
			ProjectItem projectItem = famstackDataAccessObjectManager.getProjectById(projectCommentDetails.getProjectId());
			projectCommentItem.setProjectItem(projectItem);
		}
		famstackDataAccessObjectManager.saveOrUpdateItem(projectCommentItem);
		 
	}

	public ProjectCommentItem getProjectCommentItemById(int id) {
		return famstackDataAccessObjectManager.getCommentById(id);
	}
	

	public void deleteProjectCommentItem(int projectId) {
		ProjectCommentItem projectCommentItem = getProjectCommentItemById(projectId);
		if (projectCommentItem != null) {
			famstackDataAccessObjectManager.deleteItem(projectCommentItem);
		} 
	}
	
	public List<ProjectCommentItem> getAllProjectCommentItems(int projectId) {
		return famstackDataAccessObjectManager.getProjectComments(projectId);
	}

	public ProjectCommentDetails getProjectCommentDetailsFromProjectCommentItem(
			ProjectCommentItem projectCommentItem) {
		if (projectCommentItem != null) {
			ProjectCommentDetails projectCommentDetails = new ProjectCommentDetails();
			
			projectCommentDetails.setCreatedDate(projectCommentItem.getCreatedDate());
			projectCommentDetails.setDescription(projectCommentItem.getDescription());
			projectCommentDetails.setId(projectCommentItem.getId());
			projectCommentDetails.setModifiedDate(projectCommentItem.getModifiedDate());
			projectCommentDetails.setProjectId(projectCommentItem.getProjectItem().getId());
			projectCommentDetails.setTitle(projectCommentItem.getTitle());
			if (projectCommentItem.getUser() != null) {
				projectCommentDetails.setUser(famstackUserProfileManager.getEmployeeDetailsFromUserItem(projectCommentItem.getUser()));
			}
			
			return projectCommentDetails;
			
		}
		return null;
	}
	
	public List<ProjectCommentDetails> getAllProjectCommentDetails(int projectId) {
		List<ProjectCommentItem> commentItemDetails = famstackDataAccessObjectManager.getProjectComments(projectId);
		if (commentItemDetails != null) {
			List<ProjectCommentDetails> projectCommentItemList = new ArrayList<ProjectCommentDetails>();
			Iterator<ProjectCommentItem> iter = commentItemDetails.iterator();
			while (iter.hasNext()) {
				ProjectCommentItem commentItem = iter.next();
				ProjectCommentDetails projectCommentDetails = getProjectCommentDetailsFromProjectCommentItem(commentItem);
				projectCommentItemList.add(projectCommentDetails);
			}
			return projectCommentItemList;
		}
		return null;
	}
}
