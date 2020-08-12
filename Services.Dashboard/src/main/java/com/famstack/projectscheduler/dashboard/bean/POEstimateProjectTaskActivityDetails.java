package com.famstack.projectscheduler.dashboard.bean;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ClientDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.util.DateUtils;

public class POEstimateProjectTaskActivityDetails
{
    private Date projectStartTime;

    private Date projectCompletionTime;

    private String projectCode;

    private Integer projectId;

    private String projectNumber;
    
    private Integer projectDurationHrs;
    
    private String sowLineItem;
    
     private String projectName;

    private ProjectStatus projectStatus;

    private ProjectType projectType;

    private String projectCategory;
    
    private String newProjectCategory;
    
    private Integer projectAccountId;
    
    private Integer projectTeamId;

    private Integer projectClientId;

    private Integer taskActivityDuration;

    private Integer projectLead;
    
    private String accountName;
    
    private String teamName;
    
    private String subTeamName;
    
    private String clientName;
    
    private String orderBookId;
	private String proposalNumber;
	private String location;
	private String clientPartner;
	private Map estHoursByMonthSkills;
	private Integer deliveryLead;
	
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

    public Integer getTaskActivityDuration()
    {
        return taskActivityDuration;
    }

    public void setTaskActivityDuration(Integer taskActivityDuration)
    {
        this.taskActivityDuration = taskActivityDuration;
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

    private String getTimeInHrs(int timeInMinutes)
    {
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;
        return String.format("%d:%02d", hours, minutes);
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
	public Integer getProjectDurationHrs() {
		return projectDurationHrs;
	}

	public void setProjectDurationHrs(Integer projectDurationHrs) {
		this.projectDurationHrs = projectDurationHrs;
	}
	
	public Integer getTaskActivityDurationHrs() {
		if (taskActivityDuration != null) {
			return taskActivityDuration/60;
		}
		return 0;
	}
	
	public int getUtilizationPercentage(){
		if (projectDurationHrs != null && getTaskActivityDurationHrs() != null && projectDurationHrs > 0) {
			long percentage = getTaskActivityDurationHrs()/projectDurationHrs;
			percentage = percentage * 100;
			return (int) percentage;
		}
		return 0;
	}
	
	
	public double getUtilizationPercentageDouble() {
		if (projectDurationHrs != null && getTaskActivityDurationHrs() != null && projectDurationHrs > 0) {
			double totalUtilizedInMins = getTaskActivityDurationHrs().doubleValue()/projectDurationHrs.doubleValue();
			return totalUtilizedInMins * 100;
		}
		return 0;
	}
	
	public String getUtilizationString() {
		return String.format("%.2f", getUtilizationPercentageDouble());
	}

    public String getProjectStartTimeFormated()
    {
        return DateUtils.format(projectStartTime, DateUtils.DATE_FORMAT);
    }

    public String getProjectCompletionTimeFormated()
    {
        return DateUtils.format(projectCompletionTime, DateUtils.DATE_FORMAT);
    }
    
    public String getProjectEstimateStatus(){
    	if (projectStatus == ProjectStatus.COMPLETED || projectStatus == ProjectStatus.CLOSED) {
    		return "Closed";	
    	} 
    	return "Live";
    }
    
    public static void main(String[] args) {
		System.out.println(10/3 * 100);
	}

	public String getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(String orderBookId) {
		this.orderBookId = orderBookId;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getClientPartner() {
		return clientPartner;
	}

	public void setClientPartner(String clientPartner) {
		this.clientPartner = clientPartner;
	}

	public Map getEstHoursByMonthSkills() {
		return estHoursByMonthSkills;
	}

	public void setEstHoursByMonthSkills(Map estHoursByMonthSkills) {
		this.estHoursByMonthSkills = estHoursByMonthSkills;
	}

	public Integer getDeliveryLead() {
		return deliveryLead;
	}

	public void setDeliveryLead(Integer deliveryLead) {
		this.deliveryLead = deliveryLead;
	}
}
