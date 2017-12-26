package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;

public class ApplicationDetails
{
    private int applicationId;

    private String name;

    private String value;

    private String userGroupId;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
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

}
