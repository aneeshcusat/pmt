package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Set;


/**
 * The Class GroupDetails.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */

public class GroupDetails {

	private Integer groupId;
	
	private String name;
	
	private String description;
	
	private Timestamp createdDate;

	private Timestamp lastModifiedDate;
	
	private String subscriberIds;
	
	private EmployeeDetails createdBy;
	
	private Set<EmployeeDetails> subscribers;
	
	private Set<GroupMessageDetails> messages;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<EmployeeDetails> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<EmployeeDetails> subscribers) {
		this.subscribers = subscribers;
	}

	public Set<GroupMessageDetails> getMessages() {
		return messages;
	}

	public void setMessages(Set<GroupMessageDetails> messages) {
		this.messages = messages;
	}

	public String getSubscriberIds() {
		return subscriberIds;
	}

	public void setSubscriberIds(String subscriberIds) {
		this.subscriberIds = subscriberIds;
	}

	public EmployeeDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(EmployeeDetails createdBy) {
		this.createdBy = createdBy;
	}

}
