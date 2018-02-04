package com.famstack.projectscheduler.manager;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.datatransferobject.ProjectActivityItem;
import com.famstack.projectscheduler.datatransferobject.ProjectItem;
import com.famstack.projectscheduler.employees.bean.ProjectActivityDetails;

@Component
public class FamstackProjectActivityManager extends BaseFamstackManager
{

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    public ProjectActivityItem createProjectActivityItemItem(ProjectItem projectItem,
        ProjectActivityType projectActivityType, String description)
    {
        ProjectActivityItem projectActivityItem = new ProjectActivityItem();
        projectActivityItem.setProjectItem(projectItem);
        projectActivityItem.setType(projectActivityType);
        projectActivityItem.setProjectStatus(projectItem.getStatus());
        projectActivityItem.setUserGroupId(projectItem.getUserGroupId());
        projectActivityItem.setUserName(projectItem.getReporter().getFirstName());
        projectActivityItem.setDescription(description);
        famstackDataAccessObjectManager.saveOrUpdateItem(projectActivityItem);
        return projectActivityItem;
    }

    public Set<ProjectActivityDetails> mapProjectActivityDetails(Set<ProjectActivityItem> ProjectActivityItems)
    {
        Set<ProjectActivityDetails> projectActivityDetailsSet = new HashSet<ProjectActivityDetails>();
        if (ProjectActivityItems != null) {

            for (ProjectActivityItem projectActivityItem : ProjectActivityItems) {
                ProjectActivityDetails projectActivityDetails = new ProjectActivityDetails();

                projectActivityDetails.setCreatedDate(projectActivityItem.getCreatedDate());
                projectActivityDetails.setDescription(projectActivityItem.getDescription());
                projectActivityDetails.setProjectActivityType(projectActivityItem.getType());
                projectActivityDetails.setModifiedDate(projectActivityItem.getLastModifiedDate());
                projectActivityDetails.setUserName(projectActivityItem.getUserName());
                projectActivityDetails.setProjectStatus(projectActivityItem.getProjectStatus());
                projectActivityDetailsSet.add(projectActivityDetails);
            }
        }

        return projectActivityDetailsSet;
    }
}
