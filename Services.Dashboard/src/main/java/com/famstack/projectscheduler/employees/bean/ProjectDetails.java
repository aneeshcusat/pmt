package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.datatransferobject.UserItem;

public class ProjectDetails {

	private int id;

	private String name;

	private String code;

	private String description;

	private Timestamp createdDate;

	private Timestamp lastModifiedDate;

	private String type;

	private String team;

	private UserItem reporter;

	private String category;

	private String tags;

	private String watchers;

	private String priority;

	private Timestamp startTime;

	private Timestamp completionTime;

	private int duration;

	private ProjectStatus status;

	private String clientId;

	private Set<ProjectCommentDetails> projectComments;

	private Set<ProjectActivityDetails> projectActivityDetails;

	private String reporterName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getStartTime() {
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserItem getReporter() {
		return reporter;
	}

	public void setReporter(UserItem reporter) {
		this.reporter = reporter;
	}

	public Timestamp getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Timestamp completionTime) {
		this.completionTime = completionTime;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

}
