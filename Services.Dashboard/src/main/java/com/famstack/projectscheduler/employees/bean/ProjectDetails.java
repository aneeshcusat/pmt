package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.contants.ProjectComplexity;
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectSubType;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.TaskStatus;
import com.famstack.projectscheduler.manager.FamstackAccountManager;
import com.famstack.projectscheduler.util.DateUtils;

public class ProjectDetails
{

    private Integer id;
    
    private String userGroupId;

    private String name;
    
    private String csrToken;

    private String code;

    private String description;

    private Timestamp createdDate;

    private Timestamp lastModifiedDate;

    private ProjectType type;

    private ProjectComplexity complexity;

    private Integer accountId;

    private String quantity;

    private Integer teamId;

    private Integer projectLead;

    private ProjectSubType projectSubType;

    private EmployeeDetails employeeDetails;

    private String category;

    private String tags;

    private String watchers;

    private ProjectPriority priority;

    private String startTime;

    private String completionTime;

    private Integer durationHrs;

    private ProjectStatus status;

    private Integer clientId;

    private Boolean deleted;

    private Set<ProjectCommentDetails> projectComments;

    private Set<ProjectActivityDetails> projectActivityDetails;

    private Set<TaskDetails> projectTaskDeatils;

    private String reporterName;

    private List<String> filesNames;

    private List<String> completedFilesNames;

    private int unAssignedDuration;

    private String PONumber;

    private String newCategory;

    private String sowLineItem;

    private String orderBookRefNo;

    private String proposalNo;

    private String projectLocation;

    private List<ProjectDetails> duplicateProjects;
    
    private String hoursUserSkillMonthlySplitJson;
    
    private String clientPartner;
    
    private Boolean ppi;

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
    
    public List<TaskDetails> getSortProjectTaskDeatils()
    {
    	List<TaskDetails> sortedTaskDetailsList =  null;
    	if (getProjectTaskDeatils() != null) {
    	 sortedTaskDetailsList = new ArrayList<>(getProjectTaskDeatils());
    	 Collections.sort(sortedTaskDetailsList , new Comparator<TaskDetails>()
    	            {
    	                @Override
    	                public int compare(TaskDetails taskDetails1, TaskDetails taskDetails2)
    	                {
    	                    if (taskDetails1.getTaskId() > taskDetails2.getTaskId()) {
    	                        return -1;
    	                    } else if (taskDetails1.getTaskId() < taskDetails2.getTaskId()) {
    	                        return 1;
    	                    }
    	                    return 0;
    	                }
    	            });
    	}
        return sortedTaskDetailsList;
    }

    public Set<TaskDetails> getProjectActualTaskDeatils()
    {
        Set<TaskDetails> taskDetails = new HashSet<>();
        if (projectTaskDeatils != null) {
            for (TaskDetails taskDetail : projectTaskDeatils) {
                if (!taskDetail.getExtraTimeTask()) {
                    taskDetails.add(taskDetail);
                }
            }
        }
        return taskDetails;
    }

    public int getCompletionInDays()
    {
        Date completionDate = DateUtils.tryParse(getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
        return (int) ((completionDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
    }
    
    public int getProjectDurationInDays()
    {
        Date completionDate = DateUtils.tryParse(getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
        Date startDate = DateUtils.tryParse(getStartTime(), DateUtils.DATE_TIME_FORMAT);
        return (int) ((completionDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public int getNoOfTasks()
    {
        Set<TaskDetails> taskDetailsSet = getProjectActualTaskDeatils();
        if (taskDetailsSet != null) {
            return taskDetailsSet.size();
        }
        return 0;
    }
    
    public boolean isProjectDateExceed()
    {
    	if (getCompletionTime() != null) {
    		Date completionDate = DateUtils.tryParse(getCompletionTime(), DateUtils.DATE_TIME_FORMAT);
    		return completionDate.before(new Date());   
    	}
    	return false;
    }

    public Set<Integer> getContributers()
    {
        Set<Integer> contribuersSet = new HashSet<>();
        Set<TaskDetails> taskDetailsSet = getProjectTaskDeatils();
        if (taskDetailsSet != null) {
            for (TaskDetails taskDetails : taskDetailsSet) {
                if (taskDetails.getContributers().length > 0) {
                    contribuersSet.addAll(Arrays.asList(taskDetails.getContributers()));
                }
                /*
                 * if (StringUtils.isNotBlank(taskDetails.getHelpersList())) { String[] helperArray =
                 * taskDetails.getHelpersList().split(","); if (helperArray.length > 0) { for (String helper :
                 * helperArray) { contribuersSet.add(Integer.parseInt(helper)); } } }
                 */
            }
        }
        return contribuersSet;
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
        this.PONumber = pONumber;
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
        return (projectCompletionPercentage > 100 ? 100 : projectCompletionPercentage) < 0 ? 0
            : projectCompletionPercentage;
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
        int actualDuration = 0;
        if (getStatus() == ProjectStatus.COMPLETED || getStatus() == ProjectStatus.CLOSED
            || getStatus() == ProjectStatus.INPROGRESS) {
            Set<TaskDetails> taskDetailsList = getProjectTaskDeatils();
            if (taskDetailsList != null) {
                for (TaskDetails taskDetails : taskDetailsList) {
                    actualDuration += taskDetails.getActualTimeTaken();
                }
            }
        }
        return actualDuration;
    }

    public String getActualDurationInHrs()
    {
        int actualDurationInMins = getActualDuration();
        if (actualDurationInMins <= 0
            && !(getStatus() == ProjectStatus.COMPLETED || getStatus() == ProjectStatus.CLOSED)) {
            return "";
        }

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
                if (employeeDetails != null) {
                    assigneeString += employeeDetails.getFirstName();
                    // assigneeString += taskDetails.getHelperNames();
                }

            }
        }

        return assigneeString;
    }

    public Integer getProjectLead()
    {
        return projectLead;
    }

    public void setProjectLead(Integer projectLead)
    {
        this.projectLead = projectLead;
    }

    public ProjectSubType getProjectSubType()
    {
        return projectSubType;
    }

    public void setProjectSubType(ProjectSubType projectSubType)
    {
        this.projectSubType = projectSubType;
    }

    public Boolean getDeleted()
    {
        return deleted == null ? false : deleted;
    }

    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }

    public Integer getDurationHrs()
    {
        return durationHrs == null ? 0 : durationHrs;
    }

    public void setDurationHrs(Integer durationHrs)
    {
        this.durationHrs = durationHrs;
    }

    public String getCreatedTime()
    {
        return DateUtils.format(createdDate, DateUtils.DATE_FORMAT_CALENDER);
    }

	public String getNewCategory() {
		return newCategory;
	}

	public void setNewCategory(String newCategory) {
		this.newCategory = newCategory;
	}

	public String getSowLineItem() {
		return sowLineItem;
	}

	public void setSowLineItem(String sowLineItem) {
		this.sowLineItem = sowLineItem;
	}

	public String getOrderBookRefNo() {
		return orderBookRefNo;
	}

	public void setOrderBookRefNo(String orderBookRefNo) {
		this.orderBookRefNo = orderBookRefNo;
	}

	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}

	public String getProjectLocation() {
		return projectLocation;
	}

	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}

	public String getHoursUserSkillMonthlySplitJson() {
		return hoursUserSkillMonthlySplitJson;
	}

	public void setHoursUserSkillMonthlySplitJson(
			String hoursUserSkillMonthlySplitJson) {
		this.hoursUserSkillMonthlySplitJson = hoursUserSkillMonthlySplitJson;
	}

	public String getClientPartner() {
		return clientPartner;
	}

	public void setClientPartner(String clientPartner) {
		this.clientPartner = clientPartner;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getCsrToken() {
		return csrToken;
	}

	public void setCsrToken(String csrToken) {
		this.csrToken = csrToken;
	}

	public Boolean getPpi() {
		return ppi;
	}

	public void setPpi(Boolean ppi) {
		this.ppi = ppi;
	}
}
