package com.famstack.projectscheduler.dashboard.bean;

public class ClientProjectDetails
{

    private Object clientName;

    private Object completedCount;

    private Object noCompletedCount;

    public Object getClientName()
    {
        return clientName;
    }

    public void setClientName(Object clientName)
    {
        this.clientName = clientName;
    }

    public Object getCompletedCount()
    {
        return completedCount;
    }

    public void setCompletedCount(Object completedCount)
    {
        this.completedCount = completedCount;
    }

    public Object getNoCompletedCount()
    {
        return noCompletedCount;
    }

    public void setNoCompletedCount(Object noCompletedCount)
    {
        this.noCompletedCount = noCompletedCount;
    }

    public double getPercentageOfCompletion()
    {
        String completedCountString = "" + completedCount;
        String noCompletedCountString = "" + noCompletedCount;

        double completedCountInt = Double.parseDouble(completedCountString);
        double noCompletedCountInt = Double.parseDouble(noCompletedCountString);

        if (noCompletedCountInt > 0) {
            double percentageOfWorkCompleted = (completedCountInt / noCompletedCountInt) * 100;
            return Math.round(percentageOfWorkCompleted * 100.0) / 100.0;
        }

        return 100;

    }

}
