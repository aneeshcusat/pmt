package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.famstack.projectscheduler.contants.ProjectComplexity;
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.datatransferobject.UserItem;

public class ProjectDetails {

	private Integer id;

	private String name;

	private String code;

	private String description;

	private Timestamp createdDate;

	private Timestamp lastModifiedDate;

	private ProjectType type;

	private ProjectComplexity complexity;

	private Integer accountId;

	private Integer teamId;

	private UserItem reporter;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public Set<ProjectCommentDetails> getProjectComments() {
		return projectComments;
	}

	public void setProjectComments(Set<ProjectCommentDetails> projectComments) {
		this.projectComments = projectComments;
	}

	public Set<ProjectActivityDetails> getProjectActivityDetails() {
		return projectActivityDetails;
	}

	public void setProjectActivityItem(Set<ProjectActivityDetails> projectActivityDetails) {
		this.projectActivityDetails = projectActivityDetails;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getWatchers() {
		return watchers;
	}

	public void setWatchers(String watchers) {
		this.watchers = watchers;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public UserItem getReporter() {
		return reporter;
	}

	public void setReporter(UserItem reporter) {
		this.reporter = reporter;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	public ProjectPriority getPriority() {
		return priority;
	}

	public void setPriority(ProjectPriority priority) {
		this.priority = priority;
	}

	public void setProjectActivityDetails(Set<ProjectActivityDetails> projectActivityDetails) {
		this.projectActivityDetails = projectActivityDetails;
	}

	public ProjectComplexity getComplexity() {
		return complexity;
	}

	public void setComplexity(ProjectComplexity complexity) {
		this.complexity = complexity;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Set<TaskDetails> getProjectTaskDeatils() {
		return projectTaskDeatils;
	}

	public void setProjectTaskDeatils(Set<TaskDetails> projectTaskDeatils) {
		this.projectTaskDeatils = projectTaskDeatils;
	}

	public List<String> getFilesNames() {
		return filesNames;
	}

	public void setFilesNames(List<String> filesNames) {
		this.filesNames = filesNames;
	}

}
