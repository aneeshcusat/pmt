package com.famstack.projectscheduler.employees.bean;

import java.math.BigDecimal;
import java.util.Date;

public class UserWorkDetails
{

    private Integer userId;

    private Object count;

    private Date calenderDate;

    private Integer billableMins;

    private Integer nonBillableMins;

    private Integer leaveMins;

    private String userFirstName;

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Object getCount()
    {
        return count;
    }

    public void setCount(Object count)
    {
        this.count = count;
    }

    public Object getBillableHours()
    {
        return billableMins;// s == null ? 0 : billableMins / 60;
    }

    public Object getNonBillableHours()
    {
        return nonBillableMins;// == null ? 0 : nonBillableMins / 60;
    }

    public Object getLeaveHours()
    {
        return leaveMins;// == null ? 0 : leaveMins / 60;
    }

    private int convertToInt(Object timeInMins)
    {
        int timeInHours = 0;
        if (timeInMins != null) {
            if (timeInMins instanceof BigDecimal) {
                timeInHours = ((BigDecimal) timeInMins).intValue();
            } else if (timeInMins instanceof Integer) {
                timeInHours = ((Integer) timeInMins).intValue();
            }
        }
        return timeInHours;
    }

    public Date getCalenderDate()
    {
        return calenderDate;
    }

    public void setCalenderDate(Date calenderDate)
    {
        this.calenderDate = calenderDate;
    }

    public String getUserFirstName()
    {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName)
    {
        this.userFirstName = userFirstName;
    }

    public Integer getBillableMins()
    {
        return billableMins;
    }

    public void setBillableMins(Object billableMins)
    {
        this.billableMins = convertToInt(billableMins);
    }

    public Integer getNonBillableMins()
    {
        return nonBillableMins;
    }

    public void setNonBillableMins(Object nonBillableMins)
    {
        this.nonBillableMins = convertToInt(nonBillableMins);
    }

    public Integer getLeaveMins()
    {
        return leaveMins;
    }

    public void setLeaveMins(Object leaveMins)
    {
        this.leaveMins = convertToInt(leaveMins);
    }
}
