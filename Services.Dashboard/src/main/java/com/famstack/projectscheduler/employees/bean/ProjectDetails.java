package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.contants.ProjectComplexity;
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.util.DateUtils;

public class ProjectDetails
{

    private Integer id;

    private String name;

    private String code;

    private String description;

    private Timestamp createdDate;

    private Timestamp lastModifiedDate;

    private ProjectType type;

    private ProjectComplexity complexity;

    private Integer accountId;

    private String quantity;

    private Integer teamId;

    private EmployeeDetails employeeDetails;

    private String category;

    private String tags;

    private String watchers;

    private ProjectPriority priority;

    private String startTime;

    private String completionTime;

    private Integer duration;

    private ProjectStatus status;

    private Integer clientId;

    private Set<ProjectCommentDetails> projectComments;

    private Set<ProjectActivityDetails> projectActivityDetails;

    private Set<TaskDetails> projectTaskDeatils;

    private String reporterName;

    private List<String> filesNames;

    private List<String> completedFilesNames;

    private int unAssignedDuration;

    private String PONumber;

    private List<ProjectDetails> duplicateProjects;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public ProjectStatus getStatus()
    {
        return status;
    }

    public void setStatus(ProjectStatus status)
    {
        this.status = status;
    }

    public Set<ProjectCommentDetails> getProjectComments()
    {
        return projectComments;
    }

    public void setProjectComments(Set<ProjectCommentDetails> projectComments)
    {
        this.projectComments = projectComments;
    }

    public Set<ProjectActivityDetails> getProjectActivityDetails()
    {
        return projectActivityDetails;
    }

    public void setProjectActivityItem(Set<ProjectActivityDetails> projectActivityDetails)
    {
        this.projectActivityDetails = projectActivityDetails;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getWatchers()
    {
        return watchers;
    }

    public void setWatchers(String watchers)
    {
        this.watchers = watchers;
    }

    public String getReporterName()
    {
        return reporterName;
    }

    public void setReporterName(String reporterName)
    {
        this.reporterName = reporterName;
    }

    public Timestamp getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCompletionTime()
    {
        return completionTime;
    }

    public void setCompletionTime(String completionTime)
    {
        this.completionTime = completionTime;
    }

    public void setCreatedDate(Timestamp createdDate)
    {
        this.createdDate = createdDate;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public ProjectType getType()
    {
        return type;
    }

    public void setType(ProjectType type)
    {
        this.type = type;
    }

    public ProjectPriority getPriority()
    {
        return priority;
    }

    public void setPriority(ProjectPriority priority)
    {
        this.priority = priority;
    }

    public void setProjectActivityDetails(Set<ProjectActivityDetails> projectActivityDetails)
    {
        this.projectActivityDetails = projectActivityDetails;
    }

    public ProjectComplexity getComplexity()
    {
        return complexity;
    }

    public void setComplexity(ProjectComplexity complexity)
    {
        this.complexity = complexity;
    }

    public Integer getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Integer accountId)
    {
        this.accountId = accountId;
    }

    public Integer getTeamId()
    {
        return teamId;
    }

    public void setTeamId(Integer teamId)
    {
        this.teamId = teamId;
    }

    public Integer getClientId()
    {
        return clientId;
    }

    public void setClientId(Integer clientId)
    {
        this.clientId = clientId;
    }

    public Set<TaskDetails> getProjectTaskDeatils()
    {
        return projectTaskDeatils;
    }

    public int getCompletionInDays()
    {
        Date completionDate = DateUtils.tryParse(getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
        return (int) ((completionDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
    }

    public int getNoOfTasks()
    {
        Set<TaskDetails> taskDetailsSet = getProjectTaskDeatils();
        if (taskDetailsSet != null) {
            return taskDetailsSet.size();
        }
        return 0;
    }

    public int getNoOfOpenTasks()
    {
        int openTasks = 0;
        Set<TaskDetails> taskDetailsSet = getProjectTaskDeatils();
        if (taskDetailsSet != null) {
            for (TaskDetails taskDetails : taskDetailsSet) {
                if (taskDetails.getStatus() != TaskStatus.COMPLETED && taskDetails.getStatus() != TaskStatus.CLOSED) {
                    openTasks++;
                }
            }
        }

        return openTasks;
    }

    public void setProjectTaskDeatils(Set<TaskDetails> projectTaskDeatils)
    {
        this.projectTaskDeatils = projectTaskDeatils;
    }

    public List<String> getFilesNames()
    {
        return filesNames;
    }

    public void setFilesNames(List<String> filesNames)
    {
        this.filesNames = filesNames;
    }

    public int getUnAssignedDuration()
    {
        return unAssignedDuration;
    }

    public void setUnAssignedDuration(int unAssignedDuration)
    {
        this.unAssignedDuration = unAssignedDuration;
    }

    public String getPONumber()
    {
        return PONumber;
    }

    public void setPONumber(String pONumber)
    {
        PONumber = pONumber;
    }

    public long getProjectCompletionPercentage()
    {

        long projectCompletionPercentage = 0;
        if (getStatus() == ProjectStatus.COMPLETED || getStatus() == ProjectStatus.CLOSED) {
            return 100;
        } else {
            Set<TaskDetails> taskDetailsList = getProjectTaskDeatils();
            if (!taskDetailsList.isEmpty()) {
                for (TaskDetails taskDetails : taskDetailsList) {
                    projectCompletionPercentage += taskDetails.getPercentageOfTaskCompleted();
                }
                projectCompletionPercentage = projectCompletionPercentage / taskDetailsList.size();
            }
        }
        return projectCompletionPercentage;
    }

    public boolean getProjectMissedTimeLine()
    {
        if (getStatus() != ProjectStatus.COMPLETED) {
            Date compleltionDate = DateUtils.tryParse(completionTime, DateUtils.DATE_TIME_FORMAT);
            if (compleltionDate.getTime() <= new Date().getTime()) {
                return true;
            }
        }
        return false;
    }

    public String getAccountName()
    {
        AccountDetails accountDetails = FamstackAccountManager.getAccountmap().get(accountId);
        return accountDetails != null ? accountDetails.getName() : "";
    }

    public String getClientName()
    {
        ClientDetails clientDetails = FamstackAccountManager.getClientmap().get(clientId);
        return clientDetails != null ? clientDetails.getName() : "";
    }

    public String getTeamName()
    {
        ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(teamId);
        String teamName = "";
        if (projectSubTeamDetails != null) {
            teamName = FamstackAccountManager.getTeammap().get(projectSubTeamDetails.getTeamId()).getName();
        }
        return teamName;
    }

    public String getSubTeamName()
    {
        ProjectSubTeamDetails projectSubTeamDetails = FamstackAccountManager.getSubteammap().get(teamId);
        return projectSubTeamDetails != null ? projectSubTeamDetails.getName() : "";
    }

    public List<ProjectDetails> getDuplicateProjects()
    {
        return duplicateProjects;
    }

    public void setDuplicateProjects(List<ProjectDetails> duplicateProjects)
    {
        this.duplicateProjects = duplicateProjects;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public EmployeeDetails getEmployeeDetails()
    {
        return employeeDetails;
    }

    public void setEmployeeDetails(EmployeeDetails employeeDetails)
    {
        this.employeeDetails = employeeDetails;
    }

    public List<String> getCompletedFilesNames()
    {
        return completedFilesNames;
    }

    public void setCompletedFilesNames(List<String> completedFilesNames)
    {
        this.completedFilesNames = completedFilesNames;
    }

    public int getActualDuration()
    {
        double actualDuration = 0;
        if (getStatus() == ProjectStatus.COMPLETED || getStatus() == ProjectStatus.CLOSED) {
            Set<TaskDetails> taskDetailsList = getProjectTaskDeatils();
            if (taskDetailsList != null && !taskDetailsList.isEmpty()) {
                for (TaskDetails taskDetails : taskDetailsList) {

                    TaskActivityDetails taskActivityDetails = taskDetails.getTaskActivityDetails();
                    if (taskActivityDetails != null) {
                        Date completionTime = taskActivityDetails.getActualEndTime();
                        Date startTime = taskActivityDetails.getActualStartTime();

                        if (completionTime != null && startTime != null) {
                            actualDuration += completionTime.getTime() - startTime.getTime();
                        }
                    }
                }
            }
        }
        System.out.println("actual duration " + actualDuration);
        return (int) (actualDuration > 0 ? Math.round(actualDuration / 60 / 1000) : 0);
    }

    public String getActualDurationInHrs()
    {
        int actualDurationInMins = getActualDuration();
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

    public String getAssigneeNames()
    {
        String assigneeString = "";
        Set<TaskDetails> taskDetailsSet = getProjectTaskDeatils();
        if (taskDetailsSet != null) {

            for (TaskDetails taskDetails : taskDetailsSet) {
                EmployeeDetails employeeDetails =
                    FamstackApplicationConfiguration.userMap.get(taskDetails.getAssignee());
                assigneeString += employeeDetails.getFirstName();
                // assigneeString += taskDetails.getHelperNames();

            }
        }

        return assigneeString;
    }
}
