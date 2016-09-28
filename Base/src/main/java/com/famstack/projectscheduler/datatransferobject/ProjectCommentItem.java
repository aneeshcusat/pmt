package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "project_comments")
public class ProjectCommentItem {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int id;

	@ManyToOne
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	private ProjectItem projectItem;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserItem user;
	
	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "modified_date")
	private Timestamp modifiedDate;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "title")
	private String title;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProjectItem getProjectItem() {
		return projectItem;
	}

	public void setProjectItem(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

	public UserItem getUser() {
		return user;
	}

	public void setUser(UserItem user) {
		this.user = user;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
