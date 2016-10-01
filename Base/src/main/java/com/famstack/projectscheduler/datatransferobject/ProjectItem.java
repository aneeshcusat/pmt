package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.famstack.projectscheduler.contants.ProjectStatus;

@Entity
@Table(name = "project_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class ProjectItem {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

	@Column(name = "created_by")
	private int createdBy;
	
	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "modified_by")
	private int modifiedBy;
	
	@Column(name = "modified_date")
	private Timestamp modifiedDate;
	
	@Column(name = "type")
	private String type;
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="reporter")
	private UserItem reporter;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assignee")
	private UserItem assignee;
	
	@Column(name = "review")
	private String review;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reviewer")
	private UserItem reviewer;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "tags")
	private String tags;
	
	@Column(name = "watchers")
	private String watchers;
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "start_time")
	private Timestamp startTime;
	
	@Column(name = "duration")
	private int duration;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ProjectStatus status;

	@Column(name = "client")
	private String clientId;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "projectItem", cascade = CascadeType.ALL)
	private Set<ProjectCommentItem> projectComments;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "projectItem", cascade = CascadeType.ALL)
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

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
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

	public UserItem getReporter() {
		return reporter;
	}

	public void setReporter(UserItem reporter) {
		this.reporter = reporter;
	}

	public UserItem getAssignee() {
		return assignee;
	}

	public void setAssignee(UserItem assignee) {
		this.assignee = assignee;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public UserItem getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserItem reviewer) {
		this.reviewer = reviewer;
	}

	public String getWatchers() {
		return watchers;
	}

	public void setWatchers(String watchers) {
		this.watchers = watchers;
	}

	
}
