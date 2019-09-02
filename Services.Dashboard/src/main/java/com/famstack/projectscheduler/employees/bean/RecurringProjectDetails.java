package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;

import com.famstack.projectscheduler.contants.RecurringType;
import com.famstack.projectscheduler.util.DateUtils;

public class RecurringProjectDetails
{
    private int id;

    private String name;

    private Timestamp nextRun;

    private Timestamp lastRun;

    private String cronExpression;

    private int projectId;
    
    private Integer taskId;
    
    private RecurringType type;

    private Boolean recurreOriginal;
    
    private String projectCode;

    private String endDateString;

    private Integer requestedBy;

    private String userGroupId;

    private Timestamp createdDate;

    private Timestamp lastModifiedDate;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    public String getProjectCode()
    {
        return projectCode;
    }

    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }

    public Integer getRequestedBy()
    {
        return requestedBy;
    }

    public void setRequestedBy(Integer requestedBy)
    {
        this.requestedBy = requestedBy;
    }

    public String getNextRun()
    {
        return nextRun == null ? null : DateUtils.getDisplayDate(nextRun);
    }

    public void setNextRun(Timestamp nextRun)
    {
        this.nextRun = nextRun;
    }

    public String getLastRun()
    {
        return lastRun == null ? null : DateUtils.getDisplayDate(lastRun);
    }

    public void setLastRun(Timestamp lastRun)
    {
        this.lastRun = lastRun;
    }

    public String getUserGroupId()
    {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId)
    {
        this.userGroupId = userGroupId;
    }

    public Timestamp getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate)
    {
        this.createdDate = createdDate;
    }

    public Timestamp getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }

    public String getEndDateString()
    {
        return endDateString == null ? "" : endDateString;
    }

    public void setEndDateString(String endDateString)
    {
        this.endDateString = endDateString;
    }

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public RecurringType getType() {
		return type;
	}

	public void setType(RecurringType type) {
		this.type = type;
	}


	public Boolean getRecurreOriginal() {
		return recurreOriginal;
	}

	public void setRecurreOriginal(Boolean recurreOriginal) {
		this.recurreOriginal = recurreOriginal;
	}

}
