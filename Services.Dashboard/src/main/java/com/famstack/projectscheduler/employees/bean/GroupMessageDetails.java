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

	private Integer user;

	private String userFullName;

	private Integer group;

	private String createdDateDisplay;

	private boolean read = false;

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
		read = true;
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

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getCreatedDateDisplay() {
		return createdDateDisplay;
	}

	public void setCreatedDateDisplay(String createdDateDisplay) {
		this.createdDateDisplay = createdDateDisplay;
	}

	public boolean getRead() {
		return read;
	}

}
