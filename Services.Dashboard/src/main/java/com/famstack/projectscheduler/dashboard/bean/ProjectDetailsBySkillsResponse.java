package com.famstack.projectscheduler.dashboard.bean;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDetailsBySkillsResponse {
	String monthYear;
	String team;
	String account;
	String poNumber;
	String proposalNumber;
	String orderBookId;
	String projectId;
	String clientName;
	String clientPartner;
	String deliveryLeadName;
	String projectName;
	String projectCategory;
	String newProjectCategory;
	String projectSubType;
	String startDate;
	String endDate;
	String location;
	Map<String, SkillSetResponse> skills;

@JsonInclude(JsonInclude.Include.NON_NULL)	
public static class SkillSetResponse{
	
	private Integer totalHours = 0;
	private Integer estimatedHours = 0;
	private List<Resources> resources;
	
	public Integer getEstimatedHours() {
		return estimatedHours;
	}
	public void setEstimatedHours(Integer estimatedHours) {
		this.estimatedHours = estimatedHours;
	}
	public List<Resources> getResources() {
		return resources;
	}
	public void setResources(List<Resources> resources) {
		this.resources = resources;
	}
	public void addTotalHours(Integer taskActivityDuration) {
		this.totalHours+=taskActivityDuration;
	}
	public Double getTotalHours() {
	/*	if(totalHours > 0) {
			return convertMinsToHours(totalHours);
		}*/
		return Objects.nonNull(totalHours) ? Double.valueOf(totalHours) : 0 ;
	}
	public void setTotalHours(Integer totalHours) {
		this.totalHours = totalHours;
	}
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Resources{
	private String name;
	private String email;
	@JsonIgnore
	private int id;
	private String employeeCode;
	private int hoursSpent;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public Double getHoursSpent() {
		if(hoursSpent > 0) {
			return convertMinsToHours(hoursSpent);
		}
		return  (double) hoursSpent;
	}
	public void setHoursSpent(Integer hoursSpent) {
		this.hoursSpent = hoursSpent;
	}
	
	public void addMinsSpent(Integer addMinsSpent) {
		this.hoursSpent += addMinsSpent;
	}
	
}

public String getMonthYear() {
	return monthYear;
}

public void setMonthYear(String monthYear) {
	this.monthYear = monthYear;
}

public String getTeam() {
	return team;
}

public void setTeam(String team) {
	this.team = team;
}

public String getAccount() {
	return account;
}

public void setAccount(String account) {
	this.account = account;
}

public String getPoNumber() {
	return poNumber;
}

public void setPoNumber(String poNumber) {
	this.poNumber = poNumber;
}

public String getProposalNumber() {
	return proposalNumber;
}

public void setProposalNumber(String proposalNumber) {
	this.proposalNumber = proposalNumber;
}

public String getOrderBookId() {
	return orderBookId;
}

public void setOrderBookId(String orderBookId) {
	this.orderBookId = orderBookId;
}

public String getProjectId() {
	return projectId;
}

public void setProjectId(String projectId) {
	this.projectId = projectId;
}

public String getClientName() {
	return clientName;
}

public void setClientName(String clientName) {
	this.clientName = clientName;
}

public String getClientPartner() {
	return clientPartner;
}

public void setClientPartner(String clientPartner) {
	this.clientPartner = clientPartner;
}

public String getDeliveryLeadName() {
	return deliveryLeadName;
}

public void setDeliveryLeadName(String deliveryLeadName) {
	this.deliveryLeadName = deliveryLeadName;
}

public String getProjectName() {
	return projectName;
}

public void setProjectName(String projectName) {
	this.projectName = projectName;
}

public String getProjectCategory() {
	return projectCategory;
}

public void setProjectCategory(String projectCategory) {
	this.projectCategory = projectCategory;
}

public String getNewProjectCategory() {
	return newProjectCategory;
}

public void setNewProjectCategory(String newProjectCategory) {
	this.newProjectCategory = newProjectCategory;
}

public String getStartDate() {
	return startDate;
}

public void setStartDate(String startDate) {
	this.startDate = startDate;
}

public String getEndDate() {
	return endDate;
}

public void setEndDate(String endDate) {
	this.endDate = endDate;
}

public String getLocation() {
	return location;
}

public void setLocation(String location) {
	this.location = location;
}

public Map<String, SkillSetResponse> getSkills() {
	return skills;
}

public void setSkills(Map<String, SkillSetResponse> skills) {
	this.skills = skills;
}
public static Double convertMinsToHours(int mins) {
	
	int hours = mins / 60; //since both are ints, you get an int
	int minutes = mins % 60;
	return Double.valueOf(String.format("%d.%02d", hours, minutes));
}

public String getProjectSubType() {
	return projectSubType;
}

public void setProjectSubType(String projectSubType) {
	this.projectSubType = projectSubType;
}
}
