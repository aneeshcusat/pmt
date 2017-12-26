package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;

public class AppConfValueDetails
{

    private int appConfValueId;

    private String name;

    private String value;

    private String userGroupId;

    private AppConfDetails appConfItem;

    private Timestamp createdDate;

    private Timestamp lastModifiedDate;

    public int getAppConfValueId()
    {
        return appConfValueId;
    }

    public void setAppConfValueId(int appConfValueId)
    {
        this.appConfValueId = appConfValueId;
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

    public AppConfDetails getAppConfItem()
    {
        return appConfItem;
    }

    public void setAppConfItem(AppConfDetails appConfItem)
    {
        this.appConfItem = appConfItem;
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
