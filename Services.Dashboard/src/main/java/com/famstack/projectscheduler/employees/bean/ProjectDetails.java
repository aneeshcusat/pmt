package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.datatransferobject.ProjectActivityItem;
import com.famstack.projectscheduler.datatransferobject.ProjectCommentItem;

public class ProjectDetails {

	private int id;

	private String name;

	private String code;

	private String description;

	private int createdBy;
	
	private String createdByName;
	
	private Date createdDate;

	private int modifiedBy;
	
	private String modifiedByName;
	
	private Date modifiedDate;
	
	private String type;
	
	private String category;
	
	private String tags;
	
	private String priority;
	
	private Date startTime;
	
	private int duration;
	
	private int reporter;
	
	private String reporterName;
	
	private int assignee;
	
	private String assigneeName;
	
	private String review;
	
	private int reviewer;
	
	private String reviewerName;
	
	private String watchers;

	private ProjectStatus status;

	private String clientId;
	
	private String clientName;

	private Set<ProjectCommentItem> projectComments;

	private Set<ProjectActivityItem> projectActivityItem;

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

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public Set<ProjectCommentItem> getProjectComments() {
		return projectComments;
	}

	public void setProjectComments(Set<ProjectCommentItem> projectComments) {
		this.projectComments = projectComments;
	}

	public Set<ProjectActivityItem> getProjectActivityItem() {
		return projectActivityItem;
	}

	public void setProjectActivityItem(Set<ProjectActivityItem> projectActivityItem) {
		this.projectActivityItem = projectActivityItem;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getReporter() {
		return reporter;
	}

	public void setReporter(int reporter) {
		this.reporter = reporter;
	}

	public int getAssignee() {
		return assignee;
	}

	public void setAssignee(int assignee) {
		this.assignee = assignee;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getReviewer() {
		return reviewer;
	}

	public void setReviewer(int reviewer) {
		this.reviewer = reviewer;
	}

	public String getWatchers() {
		return watchers;
	}

	public void setWatchers(String watchers) {
		this.watchers = watchers;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
