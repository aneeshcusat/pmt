package com.famstack.projectscheduler.employees.bean;

public class ClientDetails
{

    private int clientId;

    private String name;

    private String email;

    private String userGoupId;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getClientId()
    {
        return clientId;
    }

    public void setClientId(int clientId)
    {
        this.clientId = clientId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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
