package com.famstack.projectscheduler.dashboard.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;


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
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Map estimatedHours;

	List<ProjectEstimate> projectEstimate;
	
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
	public Map getEstimatedHours() {
		return estimatedHours;
	}
	public void setEstimatedHours(Map estimatedHours) {
		this.estimatedHours = estimatedHours;
	}
	 
	public static class ProjectEstimate {
		String month;
		String activity;
		int estimate;
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}
		public String getActivity() {
			return activity;
		}
		public void setActivity(String activity) {
			this.activity = activity;
		}
		public int getEstimate() {
			return estimate;
		}
		public void setEstimate(int estimate) {
			this.estimate = estimate;
		}
	}

	public List<ProjectEstimate> getProjectEstimate() {
		return projectEstimate == null ? new ArrayList<>() : projectEstimate;
	}
	public void setProjectEstimate(List<ProjectEstimate> projectEstimate) {
		this.projectEstimate = projectEstimate;
	}
}
