package com.famstack.projectscheduler.datatransferobject;

import java.sql.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "project_code")
	private String projectCode;

	@Column(name = "project_des")
	private String projectDescription;

	@Column(name = "created_by")
	private String createdBy;

	@Enumerated(EnumType.STRING)
	@Column(name = "project_status")
	private ProjectStatus projectStaus;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "modified_date")
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "projectItem", cascade = CascadeType.ALL)
	private Set<ProjectCommentItem> projectComments;

	@ManyToOne
	private ProjectItem projectItem;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "projectItem", cascade = CascadeType.ALL)
	private Set<ProjectItem> projectParents;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "projectItem", cascade = CascadeType.ALL)
	private Set<ProjectActivityItem> projectActivityItem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ProjectStatus getProjectStaus() {
		return projectStaus;
	}

	public void setProjectStaus(ProjectStatus projectStaus) {
		this.projectStaus = projectStaus;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Set<ProjectCommentItem> getProjectComments() {
		return projectComments;
	}

	public void setProjectComments(Set<ProjectCommentItem> projectComments) {
		this.projectComments = projectComments;
	}

	public Set<ProjectItem> getProjectParents() {
		return projectParents;
	}

	public void setProjectParents(Set<ProjectItem> projectParents) {
		this.projectParents = projectParents;
	}

	public Set<ProjectActivityItem> getProjectActivityItem() {
		return projectActivityItem;
	}

	public void setProjectActivityItem(Set<ProjectActivityItem> projectActivityItem) {
		this.projectActivityItem = projectActivityItem;
	}

	public ProjectItem getProjectItem() {
		return projectItem;
	}

	public void setProjectItem(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

}
