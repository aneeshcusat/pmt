package com.famstack.projectscheduler.dashboard.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDetailsResponse {
	String creationDate;
	String projectId;
	String orderBookId;
	String poNo;
	String proposalNumber;
	String clientName;
	String projectName;
	String location;
	String projectCategory;
	String newProjectCategory;
	String teamName;
	String clientPartner;
	String projectType;
	String projectStatus;
	List<TaskResponse> tasks;
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getOrderBookId() {
		return orderBookId;
	}
	public void setOrderBookId(String orderBookId) {
		this.orderBookId = orderBookId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getProposalNumber() {
		return proposalNumber;
	}
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getClientPartner() {
		return clientPartner;
	}
	public void setClientPartner(String clientPartner) {
		this.clientPartner = clientPartner;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public List<TaskResponse> getTasks() {
		if (tasks == null) {
			tasks = new ArrayList<>();
		}
		return tasks;
	}
	public void setTasks(List<TaskResponse> tasks) {
		this.tasks = tasks;
	}
	
}
