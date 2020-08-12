package com.famstack.projectscheduler.dashboard.bean;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POEstimateResponse {
	String year;
	String month;
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
	String startDate;
	String endDate;
	String projectStatus;
	String skillSet;
	String location;
	Map estimatedHours;
	HoursResponse utilizedHours;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getSkillSet() {
		return skillSet;
	}
	public void setSkillSet(String skillSet) {
		this.skillSet = skillSet;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Map getEstimatedHours() {
		return estimatedHours;
	}
	public void setEstimatedHours(Map estimatedHours) {
		this.estimatedHours = estimatedHours;
	}
	public HoursResponse getUtilizedHours() {
		return utilizedHours;
	}
	public void setUtilizedHours(HoursResponse utilizedHours) {
		this.utilizedHours = utilizedHours;
	}
}
