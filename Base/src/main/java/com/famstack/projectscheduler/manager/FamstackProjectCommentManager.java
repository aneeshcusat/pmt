package com.famstack.projectscheduler.manager;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

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

		projectCommentItem.setDescription(projectComments);
		projectCommentItem.setTitle("");

		UserItem commentUser = getFamstackUserSessionConfiguration().getLoginResult().getUserItem();
		projectCommentItem.setUser(commentUser);
		ProjectItem projectItem = (ProjectItem) famstackDataAccessObjectManager.getItemById(projectId,
				ProjectItem.class);
		projectCommentItem.setProjectItem(projectItem);
		famstackDataAccessObjectManager.saveItem(projectCommentItem);

	}

	public Set<ProjectCommentDetails> mapProjectCommentDetails(Set<ProjectCommentItem> projectComments) {
		Set<ProjectCommentDetails> projectCommentDetailsSet = new HashSet<ProjectCommentDetails>();
		if (projectComments != null) {

			for (ProjectCommentItem projectCommentItem : projectComments) {
				ProjectCommentDetails projectCommentDetails = new ProjectCommentDetails();

				projectCommentDetails.setCreatedDate(projectCommentItem.getCreatedDate());
				projectCommentDetails.setDescription(projectCommentItem.getDescription());
				projectCommentDetails.setId(projectCommentItem.getCommentId());
				projectCommentDetails.setModifiedDate(projectCommentItem.getLastModifiedDate());
				projectCommentDetails.setProjectId(projectCommentItem.getProjectItem().getProjectId());
				projectCommentDetails.setTitle(projectCommentItem.getTitle());
				if (projectCommentItem.getUser() != null) {
					projectCommentDetails.setUser(
							famstackUserProfileManager.getEmployeeDetailsFromUserItem(projectCommentItem.getUser()));
				}
				projectCommentDetailsSet.add(projectCommentDetails);
			}
		}

		return projectCommentDetailsSet;
	}
}
