package com.famstack.projectscheduler.employees.bean;

import java.math.BigDecimal;

public class UserWorkDetails
{

    private Object userId;

    private Object count;

    private Object billableHours;

    private Object nonBillableHours;

    public Object getUserId()
    {
        return userId;
    }

    public void setUserId(Object userId)
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
        int billableHours = 0;
        if (billableMins != null) {
            if (billableMins instanceof BigDecimal) {
                billableHours = ((BigDecimal) billableMins).intValue() / 60;
            } else if (billableMins instanceof Integer) {
                billableHours = ((Integer) billableMins).intValue() / 60;
            }
        }
        this.billableHours = billableHours;
    }

    public Object getNonBillableHours()
    {
        return nonBillableHours;
    }

    public void setNonBillableHours(Object nonBillableMin)
    {
        int nonBillableHours = 0;

        if (nonBillableMin != null) {
            if (nonBillableMin instanceof BigDecimal) {
                nonBillableHours = ((BigDecimal) nonBillableMin).intValue() / 60;
            } else if (nonBillableMin instanceof Integer) {
                nonBillableHours = ((Integer) nonBillableMin).intValue() / 60;
            }
        }
        this.nonBillableHours = nonBillableHours;
    }
}
