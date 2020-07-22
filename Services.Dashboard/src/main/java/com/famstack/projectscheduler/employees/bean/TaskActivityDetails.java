package com.famstack.projectscheduler.employees.bean;

import java.util.Date;

import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.UserTaskType;

public class TaskActivityDetails
{

    private int taskActivityId;

    private int taskId;

    private int startHour;

    private int durationInMinutes;

    private String dateId;

    private String taskName;
    
    private String clientName;

    private String teamName;
    
    private String clientPartner;

    private int userId;

    private Date startTime;

    private Date actualStartTime;

    private Date actualEndTime;

    private Date recordedStartTime;

    private Date recordedEndTime;

    private UserTaskType userTaskType;

    private ProjectType projectType;

    private String taskActCategory;

    private String inprogressComment;

    private String completionComment;

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public String getDateId()
    {
        return dateId;
    }

    public void setDateId(String dateId)
    {
        this.dateId = dateId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public UserTaskType getUserTaskType()
    {
        return userTaskType;
    }

    public void setUserTaskType(UserTaskType userTaskType)
    {
        this.userTaskType = userTaskType;
    }

    public int getStartHour()
    {
        Date startTimeDate = actualStartTime == null ? startTime : actualStartTime;
        return startTimeDate != null ? startTimeDate.getHours() : 0;
    }

    public void setStartHour(int startHour)
    {
        this.startHour = startHour;
    }

    public int getStartMins()
    {
        Date startTimeDate = actualStartTime == null ? startTime : actualStartTime;
        return startTimeDate != null ? startTimeDate.getMinutes() : 0;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public int getTaskActivityId()
    {
        return taskActivityId;
    }

    public void setTaskActivityId(int taskActivityId)
    {
        this.taskActivityId = taskActivityId;
    }

    public Date getActualStartTime()
    {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime)
    {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime()
    {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime)
    {
        this.actualEndTime = actualEndTime;
    }

    public int getTimeTakenToCompleteHour()
    {
        long diffHours = durationInMinutes / 60;
        if (actualStartTime != null) {
            long diff = new Date().getTime() - actualStartTime.getTime();
            diff = (durationInMinutes * (60 * 1000)) - diff;
            diffHours = diff / (60 * 60 * 1000);
        }

        return (int) diffHours;
    }

    public int getTimeTakenToCompleteMinute()
    {
        long diffMinutes = durationInMinutes % 60;
        if (actualStartTime != null) {
            long diff = new Date().getTime() - actualStartTime.getTime();
            diff = (durationInMinutes * (60 * 1000)) - diff;
            diffMinutes = diff / (60 * 1000) % 60;
        }
        return (int) diffMinutes;
    }

    public int getTimeTakenToCompleteSecond()
    {

        long diffSeconds = 0;
        if (actualStartTime != null) {
            long diff = new Date().getTime() - actualStartTime.getTime();
            diff = (durationInMinutes * (60 * 1000)) - diff;
            diffSeconds = diff / 1000 % 60;
        }
        return (int) diffSeconds;
    }

    public String getTimeTakenToComplete()
    {
        long diffHours = durationInMinutes / 60;
        long diffMinutes = durationInMinutes % 60;
        long diffSeconds = 0;
        if (actualStartTime != null && actualEndTime != null) {
            long diff = actualEndTime.getTime() - actualStartTime.getTime();
            diffHours = diff / (60 * 60 * 1000);
            diffMinutes = diff / (60 * 1000) % 60;
            diffSeconds = diff / 1000 % 60;
        }

        return (diffHours + ":" + diffMinutes + ":" + diffSeconds);
    }

    public String getActualTimeTakenInHrs()
    {
        int actualDurationInMins = getDurationInMinutes();
        String actualDurationString = "00";
        if (actualDurationInMins > 0) {
            int actualTimeInHrs = actualDurationInMins / 60;
            int actualTimeInMin = actualDurationInMins % 60;
            if (actualTimeInHrs < 10) {
                actualDurationString = "0" + actualTimeInHrs;
            } else {
                actualDurationString = "" + actualTimeInHrs;
            }

            if (actualTimeInMin > 59) {
                actualTimeInMin = actualTimeInMin / 10;
            }

            if (actualTimeInMin < 10) {
                actualDurationString += ":0" + actualTimeInMin;
            } else {
                actualDurationString += ":" + actualTimeInMin;
            }

        } else {
            actualDurationString += ":00";
        }

        return actualDurationString;
    }

    public String getInprogressComment()
    {
        return inprogressComment;
    }

    public void setInprogressComment(String inprogressComment)
    {
        this.inprogressComment = inprogressComment;
    }

    public String getCompletionComment()
    {
        return completionComment;
    }

    public void setCompletionComment(String completionComment)
    {
        this.completionComment = completionComment;
    }

    public Date getRecordedStartTime()
    {
        return recordedStartTime;
    }

    public void setRecordedStartTime(Date recordedStartTime)
    {
        this.recordedStartTime = recordedStartTime;
    }

    public Date getRecordedEndTime()
    {
        return recordedEndTime;
    }

    public void setRecordedEndTime(Date recordedEndTime)
    {
        this.recordedEndTime = recordedEndTime;
    }

    public int getDurationInMinutes()
    {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes)
    {
        this.durationInMinutes = durationInMinutes;
    }

    public String getTaskActCategory()
    {
        return taskActCategory;
    }

    public void setTaskActCategory(String taskActCategory)
    {
        this.taskActCategory = taskActCategory;
    }

    public ProjectType getProjectType()
    {
        return projectType;
    }

    public void setProjectType(ProjectType projectType)
    {
        this.projectType = projectType;
    }

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getClientPartner() {
		return clientPartner;
	}

	public void setClientPartner(String clientPartner) {
		this.clientPartner = clientPartner;
	}
}
