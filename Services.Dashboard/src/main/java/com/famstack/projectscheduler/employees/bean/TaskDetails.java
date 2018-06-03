package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.List;

import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectTaskType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.util.StringUtils;

public class TaskDetails
{

    private int taskId;

    private int projectId;

    private String name;

    private String description;

    private Timestamp createdDate;

    private Timestamp lastModifiedDate;

    private Timestamp taskPausedTime;

    private String reporterName;

    private ProjectPriority priority;

    private int priorityInt;

    private String startTime;

    private String completionTime;

    private Integer duration;

    private int assignee;

    private Integer[] helper;

    private Integer[] contributers;

    private ProjectTaskType projectTaskType;

    private Boolean canRecure;

    private String helpersList;

    private TaskStatus status;

    private Boolean extraTimeTask;

    private EmployeeDetails employeeDetails;

    private List<TaskActivityDetails> taskActivityDetails;

    private Boolean disableTask;

    private int actualTimeTaken;

    private int taskRemainingTime;

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public ProjectPriority getPriority()
    {
        return priority;
    }

    public void setPriority(ProjectPriority priority)
    {
        if (priority == null) {
            priorityInt = -1;
        } else if (priority == ProjectPriority.HIGH) {
            priorityInt = 3;
        } else if (priority == ProjectPriority.MEDIUM) {
            priorityInt = 2;
        } else if (priority == ProjectPriority.HIGH) {
            priorityInt = 1;
        }

        this.priority = priority;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getCompletionTime()
    {
        return completionTime;
    }

    public void setCompletionTime(String completionTime)
    {
        this.completionTime = completionTime;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public TaskStatus getStatus()
    {
        return status;
    }

    public void setStatus(TaskStatus status)
    {
        this.status = status;
    }

    public String getReporterName()
    {
        return reporterName;
    }

    public void setReporterName(String reporterName)
    {
        this.reporterName = reporterName;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }

    public int getAssignee()
    {
        return assignee;
    }

    public void setAssignee(int assignee)
    {
        this.assignee = assignee;
    }

    public Integer[] getHelper()
    {
        return helper;
    }

    public void setHelper(Integer[] helper)
    {
        this.helper = helper;
    }

    public String getHelpersList()
    {
        if (StringUtils.isNotBlank(helpersList)) {
            helpersList = helpersList.replace("[", "").replaceAll("]", "").replace("null", "");
        }
        return helpersList;
    }

    public void setHelpersList(String helpersList)
    {
        this.helpersList = helpersList;
    }

    public double getPercentageOfTaskCompleted()
    {
        double progressPercentage = 0;
        if (status == TaskStatus.COMPLETED) {
            return 100;
        } else if ((status == TaskStatus.INPROGRESS)) {
            double durationInMinute = duration * 60;
            double taskRemainingTime = getTaskRemainingTime();
            double percentageOfWorkCompleted = (100 - (taskRemainingTime / durationInMinute) * 100);
            progressPercentage = Math.round(percentageOfWorkCompleted * 100.0) / 100.0;
        }

        return (progressPercentage > 100 ? 100 : progressPercentage) < 0 ? 0 : progressPercentage;

    }

    public int getTaskRemainingTime()
    {
        // int diffInMinute = duration * 60;
        /*
         * if (status == TaskStatus.COMPLETED) { return 0; } else if (taskActivityDetails == null) { return
         * diffInMinute; } Date actualStartTime = null; if (taskActivityDetails != null) { for (TaskActivityDetails
         * taskActivityDetail : taskActivityDetails) { if (taskActivityDetail.getActualEndTime() == null) {
         * actualStartTime = taskActivityDetail.getActualStartTime(); diffInMinute =
         * taskActivityDetail.getDurationInMinutes(); break; } } } if (actualStartTime != null) { double diff = new
         * Date().getTime() - actualStartTime.getTime(); diff = (diffInMinute * 60 * 1000) - diff; diffInMinute = diff /
         * (60 * 1000); }
         */
        // return diffInMinute;
        return taskRemainingTime;
    }

    public EmployeeDetails getEmployeeDetails()
    {
        return employeeDetails;
    }

    public void setEmployeeDetails(EmployeeDetails employeeDetails)
    {
        this.employeeDetails = employeeDetails;
    }

    public String getHelperNames()
    {
        Integer helperId = 0;
        String hleperNames = "";
        if (StringUtils.isNotBlank(helpersList)) {
            String[] helpers = helpersList.split(",");
            for (String helper : helpers) {
                helper = helper.trim();
                helperId = Integer.parseInt(helper);
                EmployeeDetails employeeDetails = FamstackApplicationConfiguration.userMap.get(helperId);
                if (employeeDetails != null) {
                    hleperNames = hleperNames + " " + employeeDetails.getFirstName();
                }
            }
        }
        return hleperNames.trim();
    }

    public Boolean getDisableTask()
    {
        return disableTask;
    }

    public void setDisableTask(Boolean disableTask)
    {
        this.disableTask = disableTask;
    }

    public List<TaskActivityDetails> getTaskActivityDetails()
    {
        return taskActivityDetails;
    }

    public void setTaskActivityDetails(List<TaskActivityDetails> taskActivityDetails)
    {
        this.taskActivityDetails = taskActivityDetails;
    }

    public int getActualTimeTaken()
    {
        return actualTimeTaken;
    }

    public String getActualTimeTakenInHrs()
    {
        int actualDurationInMins = getActualTimeTaken();
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

    public Boolean getExtraTimeTask()
    {
        return extraTimeTask == null ? false : extraTimeTask;
    }

    public void setExtraTimeTask(Boolean extraTimeTask)
    {
        this.extraTimeTask = extraTimeTask;
    }

    public void setActualTimeTaken(int actualTimeTaken)
    {
        this.actualTimeTaken = actualTimeTaken;
    }

    public void setTaskRemainingTime(int taskRemainingTime)
    {
        this.taskRemainingTime = taskRemainingTime;
    }

    public String getTaskType()
    {
        if (projectTaskType != null) {
            switch (projectTaskType) {
                case ITERATION:
                    return "I";
                case PRODUCTIVE:
                    return "P";
                case REVIEW:
                    return "R";
            }

        }
        return "P";
    }

    public Timestamp getTaskPausedTime()
    {
        return taskPausedTime;
    }

    public void setTaskPausedTime(Timestamp taskPausedTime)
    {
        this.taskPausedTime = taskPausedTime;
    }

    public ProjectTaskType getProjectTaskType()
    {
        return projectTaskType == null ? ProjectTaskType.PRODUCTIVE : projectTaskType;
    }

    public void setProjectTaskType(ProjectTaskType projectTaskType)
    {
        this.projectTaskType = projectTaskType;
    }

    public Integer[] getContributers()
    {
        Integer[] defaultContributer = {assignee};

        return contributers == null ? defaultContributer : contributers;
    }

    public void setContributers(String contributers)
    {
        if (StringUtils.isNotBlank(contributers)) {
            String[] contribuersList = contributers.split(",");
            Integer[] contribuersIntList = new Integer[contribuersList.length];
            int index = 0;
            for (String contributer : contribuersList) {
                contribuersIntList[index++] = Integer.parseInt(contributer.trim());
            }
            this.contributers = contribuersIntList;
        }
    }

    public int getPriorityInt()
    {
        return priorityInt;
    }

    public void setPriorityInt(int priorityInt)
    {
        this.priorityInt = priorityInt;
    }

    public Boolean getCanRecure()
    {
        return canRecure == null ? true : canRecure;
    }

    public void setCanRecure(Boolean canRecure)
    {
        this.canRecure = canRecure;
    }
}
