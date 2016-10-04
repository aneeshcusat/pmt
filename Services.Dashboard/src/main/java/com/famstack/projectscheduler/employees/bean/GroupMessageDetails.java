package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;


/**
 * The Class GroupMessageDetails.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */

public class GroupMessageDetails {

	private Integer messageId;

	private String description;

	private Timestamp createdDate;

	private Timestamp lastModifiedDate;

	private EmployeeDetails user;

	private Integer group;

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public EmployeeDetails getUser() {
		return user;
	}

	public void setUser(EmployeeDetails user) {
		this.user = user;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}
	

}
