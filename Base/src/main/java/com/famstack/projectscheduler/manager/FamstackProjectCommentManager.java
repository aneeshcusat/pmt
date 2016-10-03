package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.datatransferobject.ProjectCommentItem;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.ProjectCommentDetails;

/**
 * The Class FamstackProjectCommentManager.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */
@Component
public class FamstackProjectCommentManager extends BaseFamstackManager {

	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void createProjectCommentItem(String projectComments, int projectId) {
		ProjectCommentItem projectCommentItem = new ProjectCommentItem();
		projectCommentItem.setCreatedDate(new Timestamp((new java.util.Date()).getTime()));
		projectCommentItem.setModifiedDate(new Timestamp((new java.util.Date()).getTime()));

		projectCommentItem.setDescription(projectComments);
		projectCommentItem.setTitle("");

		UserItem commentUser = getFamstackUserSessionConfiguration().getLoginResult().getUserItem();
		projectCommentItem.setUser(commentUser);
		ProjectItem projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId,
				ProjectItem.class);
		projectCommentItem.setProjectItem(projectItem);
		famstackDataAccessObjectManager.saveOrUpdateItem(projectCommentItem);

	}

	public List<ProjectCommentItem> getAllProjectCommentItems(int projectId) {
		return famstackDataAccessObjectManager.getProjectComments(projectId);
	}

	public ProjectCommentDetails getProjectCommentDetailsFromProjectCommentItem(ProjectCommentItem projectCommentItem) {
		if (projectCommentItem != null) {
			ProjectCommentDetails projectCommentDetails = new ProjectCommentDetails();

			projectCommentDetails.setCreatedDate(projectCommentItem.getCreatedDate());
			projectCommentDetails.setDescription(projectCommentItem.getDescription());
			projectCommentDetails.setId(projectCommentItem.getId());
			projectCommentDetails.setModifiedDate(projectCommentItem.getModifiedDate());
			projectCommentDetails.setProjectId(projectCommentItem.getProjectItem().getId());
			projectCommentDetails.setTitle(projectCommentItem.getTitle());
			if (projectCommentItem.getUser() != null) {
				projectCommentDetails.setUser(
						famstackUserProfileManager.getEmployeeDetailsFromUserItem(projectCommentItem.getUser()));
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
				ProjectCommentDetails projectCommentDetails = getProjectCommentDetailsFromProjectCommentItem(
						commentItem);
				projectCommentItemList.add(projectCommentDetails);
			}
			return projectCommentItemList;
		}
		return null;
	}
}
