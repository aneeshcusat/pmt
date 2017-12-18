package com.famstack.projectscheduler.employees.bean;

import java.util.HashSet;
import java.util.Set;

public class ProjectTeamDetails
{

    private int teamId;

    private String name;

    private String code;

    private String poc;

    private String userGoupId;

    private Set<ProjectSubTeamDetails> projectSubTeams;

    public int getTeamId()
    {
        return teamId;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
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

    public String getPoc()
    {
        return poc;
    }

    public void setPoc(String poc)
    {
        this.poc = poc;
    }

    public Set<ProjectSubTeamDetails> getProjectSubTeams()
    {
        if (projectSubTeams == null) {
            projectSubTeams = new HashSet<>();
        }
        return projectSubTeams;
    }

    public void setProjectSubTeams(Set<ProjectSubTeamDetails> projectSubTeams)
    {
        this.projectSubTeams = projectSubTeams;
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
