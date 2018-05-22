package com.famstack.projectscheduler.dashboard.bean;

import java.util.Date;

import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.employees.bean.ClientDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;

public class ProjectTaskActivityDetails
{
    private Date projectStartTime;

    private Date projectCompletionTime;

    private String projectCode;

    private Integer projectId;

    private String projectNumber;

    private String projectName;

    private ProjectStatus projectStatus;

    private ProjectType projectType;

    private String projectCategory;

    private Integer projectTeamId;

    private Integer projectClientId;

    private String taskName;

    private Date taskActivityStartTime;

    private Integer taskActivityDuration;

    private Integer userId;

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
        ClientDetails clientDetails = FamstackAccountManager.getClientmap().get(projectClientId);
        return clientDetails != null ? clientDetails.getName() : "";
    }

    public String getTeamName()
    {
        ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(projectTeamId);
        String teamName = "";
        if (projectSubTeamDetails != null) {
            teamName = FamstackAccountManager.getTeammap().get(projectSubTeamDetails.getTeamId()).getName();
        }
        return teamName;
    }

    public String getSubTeamName()
    {
        ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(projectTeamId);
        return projectSubTeamDetails != null ? projectSubTeamDetails.getName() : "";
    }

    public String getDurationInHours()
    {
        if (taskActivityDuration == null) {
            return "";
        }
        int hours = taskActivityDuration / 60; // since both are ints, you get an int
        int minutes = taskActivityDuration % 60;
        return String.format("%d:%02d", hours, minutes);
    }
}
