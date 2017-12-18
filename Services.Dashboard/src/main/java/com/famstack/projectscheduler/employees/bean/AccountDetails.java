package com.famstack.projectscheduler.employees.bean;

import java.util.HashSet;
import java.util.Set;

public class AccountDetails
{

    private int accountId;

    private String name;

    private String code;

    private String holder;

    private String type;

    private String userGoupId;

    private Set<ProjectTeamDetails> projectTeams;

    public int getAccountId()
    {
        return accountId;
    }

    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getHolder()
    {
        return holder;
    }

    public void setHolder(String holder)
    {
        this.holder = holder;
    }

    public Set<ProjectTeamDetails> getProjectTeams()
    {
        if (projectTeams == null) {
            projectTeams = new HashSet<>();
        }
        return projectTeams;
    }

    public void setProjectTeams(Set<ProjectTeamDetails> projectTeams)
    {
        this.projectTeams = projectTeams;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getUserGoupId()
    {
        return userGoupId;
    }

    public void setUserGoupId(String userGoupId)
    {
        this.userGoupId = userGoupId;
    }

}
