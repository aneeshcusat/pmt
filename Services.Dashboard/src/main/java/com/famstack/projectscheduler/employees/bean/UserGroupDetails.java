package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;

public class UserGroupDetails
{

    private int id;

    private String name;

    private String companyName;

    private String companyId;

    private String userGroupId;

    private Timestamp createdDate;

    private Timestamp lastModifiedDate;

    private Integer userAccessCode;

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

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(String companyId)
    {
        this.companyId = companyId;
    }

    public Integer getUserAccessCode()
    {
        return userAccessCode;
    }

    public void setUserAccessCode(Integer userAccessCode)
    {
        this.userAccessCode = userAccessCode;
    }

}
