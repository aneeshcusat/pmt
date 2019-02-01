package com.famstack.projectscheduler.employees.bean;

import java.util.Date;

/**
 * The Class ProjectCommentDetails.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */

public class ProjectCommentDetails {

	private int id;

	private int projectId;

	private String userName;

	private EmployeeDetails user;

	private String description;
	
	private String title;
	
	private Date createdDate;

	private Date modifiedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public EmployeeDetails getUser() {
		return user;
	}

	public void setUser(EmployeeDetails userId) {
		this.user = userId;
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
	
}
