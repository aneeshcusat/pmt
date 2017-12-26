package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Set;

public class AppConfDetails
{

    private int applicationId;

    private String userGroupId;

    private Set<AppConfValueDetails> appConfValueDetails;

    private String type;

    private Timestamp createdDate;

    private Timestamp lastModifiedDate;

    public int getApplicationId()
    {
        return applicationId;
    }

    public void setApplicationId(int applicationId)
    {
        this.applicationId = applicationId;
    }

    public String getUserGroupId()
    {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId)
    {
        this.userGroupId = userGroupId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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

    public Set<AppConfValueDetails> getAppConfValueDetails()
    {
        return appConfValueDetails;
    }

    public void setAppConfValueDetails(Set<AppConfValueDetails> appConfValueDetails)
    {
        this.appConfValueDetails = appConfValueDetails;
    }

}
