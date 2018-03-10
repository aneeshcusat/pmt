package com.famstack.projectscheduler.employees.bean;

import java.math.BigDecimal;
import java.util.Date;

public class UserWorkDetails
{

    private Integer userId;

    private Object count;

    private Object billableHours;

    private Object nonBillableHours;

    private Object leaveHours;

    private Date calenderDate;

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
        return billableHours;
    }

    public void setBillableHours(Object billableMins)
    {
        int billableHours = convertToHours(billableMins);
        this.billableHours = billableHours;
    }

    private int convertToHours(Object timeInMins)
    {
        int timeInHours = 0;
        if (timeInMins != null) {
            if (timeInMins instanceof BigDecimal) {
                timeInHours = ((BigDecimal) timeInMins).intValue() / 60;
            } else if (timeInMins instanceof Integer) {
                timeInHours = ((Integer) timeInMins).intValue() / 60;
            }
        }
        return timeInHours;
    }

    public Object getNonBillableHours()
    {
        return nonBillableHours;
    }

    public void setNonBillableHours(Object nonBillableMin)
    {
        int nonBillableHours = convertToHours(nonBillableMin);
        this.nonBillableHours = nonBillableHours;
    }

    public Date getCalenderDate()
    {
        return calenderDate;
    }

    public void setCalenderDate(Date calenderDate)
    {
        this.calenderDate = calenderDate;
    }

    public Object getLeaveHours()
    {
        return leaveHours;
    }

    public void setLeaveHours(Object leaveHours)
    {
        this.leaveHours = convertToHours(leaveHours);
        ;
    }

    public String getUserFirstName()
    {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName)
    {
        this.userFirstName = userFirstName;
    }
}
