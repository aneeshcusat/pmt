package com.famstack.projectscheduler.dashboard.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.contants.UserTaskType;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ClientDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.util.DateUtils;

public class ProjectTaskActivityDetails
{
    private Date projectStartTime;

    private Date projectCompletionTime;

    private String projectCode;

    private Integer projectId;

    private String projectNumber;
    
    private String sowLineItem;
    
     private String projectName;

    private ProjectStatus projectStatus;

    private ProjectType projectType;

    private String projectCategory;
    
    private String newProjectCategory;
    
    private Integer projectAccountId;
    
    private Integer projectTeamId;

    private Integer projectClientId;

    private String taskName;

    private Date taskActivityStartTime;

    private Integer taskActivityDuration;

    private Integer taskActActivityDuration;
    
    private Double taskActivityTimeXls;

    private Integer userId;

    private Integer taskActivityId;

    private Integer taskId;

    private Date taskActivityEndTime;

    private TaskStatus taskStatus;

    private Date taskPausedTime;

    private UserTaskType taskActType;

    private ProjectType taskActProjType;

    private String taskActCategory;

    private Date taskStartTime;

    private Date taskCompletionTime;

    private int taskDuration;
    
    private Integer projectLead;
    
    private String accountName;
    
    private String teamName;
    
    private String subTeamName;
    
    private String clientName;
    
    private String taskCompletionComments;
    
    private List<ProjectTaskActivityDetails> subItems;

    private List<ProjectTaskActivityDetails> childs;

    public String getProjectCode()
    {
        return projectCode;
    }

    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }

    public Integer getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }

    public String getProjectNumber()
    {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber)
    {
        this.projectNumber = projectNumber;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public ProjectStatus getProjectStatus()
    {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus)
    {
        this.projectStatus = projectStatus;
    }

    public ProjectType getProjectType()
    {
        return projectType;
    }

    public void setProjectType(ProjectType projectType)
    {
        this.projectType = projectType;
    }

    public String getProjectCategory()
    {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory)
    {
        this.projectCategory = projectCategory;
    }

    public Integer getProjectTeamId()
    {
        return projectTeamId;
    }

    public void setProjectTeamId(Integer projectTeamId)
    {
        this.projectTeamId = projectTeamId;
    }

    public Integer getProjectClientId()
    {
        return projectClientId;
    }

    public void setProjectClientId(Integer projectClientId)
    {
        this.projectClientId = projectClientId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public Date getTaskActivityStartTime()
    {
        return taskActivityStartTime;
    }

    public void setTaskActivityStartTime(Date taskActivityStartTime)
    {
        this.taskActivityStartTime = taskActivityStartTime;
    }

    public Integer getTaskActivityDuration()
    {
        return taskActivityDuration;
    }

    public void setTaskActivityDuration(Integer taskActivityDuration)
    {
        this.taskActivityDuration = taskActivityDuration;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Date getProjectStartTime()
    {
        return projectStartTime;
    }

    public void setProjectStartTime(Date projectStartTime)
    {
        this.projectStartTime = projectStartTime;
    }

    public Date getProjectCompletionTime()
    {
        return projectCompletionTime;
    }

    public void setProjectCompletionTime(Date projectCompletionTime)
    {
        this.projectCompletionTime = projectCompletionTime;
    }

    public String getClientName()
    {
	    if (clientName == null) {
	        ClientDetails clientDetails = FamstackAccountManager.getClientmap().get(projectClientId);
	        return clientDetails != null ? clientDetails.getName() : "";
	    } else {
			return clientName;
		}
    }
    
    public String getAccountName()
    {
    	if (accountName == null) {
    		AccountDetails accountDetails = FamstackAccountManager.getAccountmap().get(projectAccountId);
    		return accountDetails != null ? accountDetails.getName() : "";
    	} else {
    		return accountName;
    	}
    }
    
    public Integer getTeamId()
    {
        ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(projectTeamId);
        if (projectSubTeamDetails != null) {
            return projectSubTeamDetails.getTeamId();
        }
        return -1;
    }
    
    public Integer getSubTeamId(){
    	return projectTeamId;
    }

    public String getTeamName()
    {
	  if (teamName == null) {
	    ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(projectTeamId);
	    String teamNameString = "";
	    if (projectSubTeamDetails != null) {
	        teamNameString = FamstackAccountManager.getTeammap().get(projectSubTeamDetails.getTeamId()).getName();
	    }
	    return teamNameString;
	  } else {
		  return teamName;
	  }
    }

    public String getSubTeamName()
    {
	    if (subTeamName == null) {
	    	 ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(projectTeamId);
	         return projectSubTeamDetails != null ? projectSubTeamDetails.getName() : "";
	    } else {
			return subTeamName;
		}
    }

    public String getDurationInHours()
    {
        if (taskActivityDuration == null) {
            return "";
        }
        return getTimeInHrs(taskActivityDuration);
    }

    public String getActDurationInHours()
    {
        if (taskActActivityDuration == null) {
            return "";
        }
        return getTimeInHrs(taskActActivityDuration);
    }

    private String getTimeInHrs(int timeInMinutes)
    {
        int hours = timeInMinutes / 60; // since both are ints, you get an int
        int minutes = timeInMinutes % 60;
        return String.format("%d:%02d", hours, minutes);
    }

    public Integer getTaskActivityId()
    {
        return taskActivityId;
    }

    public void setTaskActivityId(Integer taskActivityId)
    {
        this.taskActivityId = taskActivityId;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Date getTaskActivityEndTime()
    {
        return taskActivityEndTime;
    }

    public void setTaskActivityEndTime(Date taskActivityEndTime)
    {
        this.taskActivityEndTime = taskActivityEndTime;
    }

    public void addToChildProjectActivityDetailsMap(ProjectTaskActivityDetails projectTaskActivityDetails)
    {
        if (childs == null) {
            childs = new ArrayList<>();
        }
        childs.add(projectTaskActivityDetails);
    }
    
    public Set<Integer> getAllTaskActivityIds(){
    	 Set<Integer> taskActivityIds = new HashSet<>();
    	 taskActivityIds.add(getTaskActivityId());
    	if (childs != null) {
           for(ProjectTaskActivityDetails projectTaskActivityDetails : childs){
        	   taskActivityIds.add(projectTaskActivityDetails.getTaskActivityId());   
           }
        }
    	return taskActivityIds;
    }

    public List<ProjectTaskActivityDetails> getChilds()
    {
        return childs;
    }

    public Integer getTaskActActivityDuration()
    {
        return taskActActivityDuration;
    }

    public void setTaskActActivityDuration(Integer taskActActivityDuration)
    {
        this.taskActActivityDuration = taskActActivityDuration;
    }

    public TaskStatus getTaskStatus()
    {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public UserTaskType getTaskActType()
    {
        return taskActType;
    }

    public void setTaskActType(UserTaskType taskActType)
    {
        this.taskActType = taskActType;
    }

    public ProjectType getTaskActProjType()
    {
        return taskActProjType;
    }

    public void setTaskActProjType(ProjectType taskActProjType)
    {
        this.taskActProjType = taskActProjType;
    }

    public String getTaskActCategory()
    {
        return taskActCategory;
    }

    public void setTaskActCategory(String taskActCategory)
    {
        this.taskActCategory = taskActCategory;
    }

    public Date getTaskStartTime()
    {
        return taskStartTime;
    }

    public void setTaskStartTime(Date taskStartTime)
    {
        this.taskStartTime = taskStartTime;
    }

    public Date getTaskCompletionTime()
    {
        return taskCompletionTime;
    }

    public void setTaskCompletionTime(Date taskCompletionTime)
    {
        this.taskCompletionTime = taskCompletionTime;
    }

    public int getTaskDuration()
    {
        return taskDuration;
    }

    public void setTaskDuration(int taskDuration)
    {
        this.taskDuration = taskDuration;
    }

    public Date getTaskPausedTime()
    {
        return taskPausedTime;
    }

    public void setTaskPausedTime(Date taskPausedTime)
    {
        this.taskPausedTime = taskPausedTime;
    }
    
    public String getTaksUserDateKey(){
		 String key = "D" + DateUtils.format(taskActivityStartTime, DateUtils.DATE_FORMAT_CALENDER);
		 key += "T" + taskId;
		 key += "U" + userId;
		 return key;
    }

	public Integer getProjectAccountId() {
		return projectAccountId;
	}

	public void setProjectAccountId(Integer projectAccountId) {
		this.projectAccountId = projectAccountId;
	}

	public Integer getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(Integer projectLead) {
		this.projectLead = projectLead;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public void setSubTeamName(String subTeamName) {
		this.subTeamName = subTeamName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Double getTaskActivityTimeXls() {
		return taskActivityTimeXls;
	}

	public void setTaskActivityTimeXls(Double taskActivityTimeXls) {
		this.taskActivityTimeXls = taskActivityTimeXls;
	}
	
	public ProjectTaskActivityDetails getClone(){
		ProjectTaskActivityDetails projectDetails = new ProjectTaskActivityDetails();
		projectDetails.setTaskActivityStartTime(getTaskActivityStartTime());
		projectDetails.setProjectCode(getProjectCode());
		projectDetails.setProjectId(getProjectId());
		projectDetails.setProjectName(getProjectName());
		projectDetails.setProjectNumber(getProjectNumber());
		projectDetails.setProjectStatus(getProjectStatus());
		projectDetails.setProjectType(getProjectType());
		projectDetails.setProjectCategory(getProjectCategory());
		projectDetails.setAccountName(getAccountName());
		projectDetails.setTeamName(getTeamName());
		projectDetails.setSubTeamName(getSubTeamName());
		projectDetails.setClientName(getClientName());
		projectDetails.setTaskName(getTaskName());
		return projectDetails;
	}

	public String getSowLineItem() {
		return sowLineItem;
	}

	public void setSowLineItem(String sowLineItem) {
		this.sowLineItem = sowLineItem;
	}

	public String getNewProjectCategory() {
		return newProjectCategory;
	}

	public void setNewProjectCategory(String newProjectCategory) {
		this.newProjectCategory = newProjectCategory;
	}

	public List<ProjectTaskActivityDetails> getSubItems() {
		if (subItems == null) {
			subItems = new ArrayList<>();
		}
		return subItems;
	}

	public String getTaskCompletionComments() {
		return taskCompletionComments;
	}

	public void setTaskCompletionComments(String taskCompletionComments) {
		this.taskCompletionComments = taskCompletionComments;
	}
}
