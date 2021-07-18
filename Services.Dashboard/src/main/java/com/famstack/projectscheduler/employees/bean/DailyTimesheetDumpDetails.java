package com.famstack.projectscheduler.employees.bean;

import java.util.Date;

import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.util.DateUtils;

public class DailyTimesheetDumpDetails {
	private String fullName;
	private String reportingManagerName;
	private String employeeeId;
	private String deliveryLead;
	private String clientName;
	private String projectCode;
	private String projectId;
	private ProjectType projectType;
	private String projectNumber;
	private String orderRefNumber;
	private String proposalNumber;
	private String projectName;
	private String projectStatus;
	private String projectCategory;
	private String newProjectCategory;
	private String taskName;
	private String accountName;
	private String teamName;
	private String subTeamName;
	private Date taskStartTime;
	private String durationInHours;
	
	private String actDurationInHours;
	private Integer actDurationInMins;
	private String taskCompletionComments;
	private Date taskRecordedActivityStartTime;
	private Date taskActivityStartTime;
	private Date lastModifiedTime;
	
	public String getLastModifiedTimeFormated() {
		return lastModifiedTime != null ? DateUtils.format(lastModifiedTime, DateUtils.DATE_TIME_FORMAT) : "";
	}
	
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public String getFullName() {
		return fullName != null ? fullName : "";
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getDeliveryLead() {
		return deliveryLead != null ? deliveryLead : "";
	}
	public void setDeliveryLead(String deliveryLead) {
		this.deliveryLead = deliveryLead;
	}
	public String getClientName() {
		return clientName != null ? clientName : "";
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getProjectCode() {
		return projectCode != null ? projectCode : "";
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectId() {
		return projectId != null ? projectId : "";
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectNumber() {
		return projectNumber != null ? projectNumber : "";
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getOrderRefNumber() {
		return orderRefNumber != null ? orderRefNumber : "";
	}
	public void setOrderRefNumber(String orderRefNumber) {
		this.orderRefNumber = orderRefNumber;
	}
	public String getProposalNumber() {
		return proposalNumber != null ? proposalNumber : "";
	}
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	public String getProjectName() {
		return projectName != null ? projectName : "";
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectStatus() {
		return projectStatus != null ? projectStatus : "";
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getProjectCategory() {
		return projectCategory != null ? projectCategory : "";
	}
	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	public String getNewProjectCategory() {
		return newProjectCategory != null ? newProjectCategory : "";
	}
	public void setNewProjectCategory(String newProjectCategory) {
		this.newProjectCategory = newProjectCategory;
	}
	public String getTaskName() {
		return taskName != null ? taskName : "";
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getAccountName() {
		return accountName != null ? accountName : "";
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getTeamName() {
		return teamName != null ? teamName : "";
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getSubTeamName() {
		return subTeamName != null ? subTeamName : "";
	}
	public void setSubTeamName(String subTeamName) {
		this.subTeamName = subTeamName;
	}
	public String getTaskStartTime() {
		return taskStartTime != null ? DateUtils.format(taskStartTime, DateUtils.DATE_TIME_FORMAT) : "";
	}
	public void setTaskStartTime(Date taskStartTime) {
		this.taskStartTime = taskStartTime;
	}
	public String getDurationInHours() {
		return durationInHours;
	}
	public void setDurationInHours(String durationInHours) {
		this.durationInHours = durationInHours;
	}
	public String getActDurationInHours() {
		return actDurationInHours;
	}
	public void setActDurationInHours(String actDurationInHours) {
		this.actDurationInHours = actDurationInHours;
	}
	public String getTaskCompletionComments() {
		return taskCompletionComments != null ? taskCompletionComments : "";
	}
	public void setTaskCompletionComments(String taskCompletionComments) {
		this.taskCompletionComments = taskCompletionComments;
	}
	public String getTaskActivityStartTimeFormated() {
		return taskActivityStartTime != null ? DateUtils.format(taskActivityStartTime, DateUtils.DATE_TIME_FORMAT) : "";
	}
	public Date getTaskActivityStartTime() {
		return taskActivityStartTime;
	}
	public String getTaskActivityDate() {
		return taskActivityStartTime != null ? DateUtils.format(taskActivityStartTime, DateUtils.DATE_FORMAT) : "";
	}
	public void setTaskActivityStartTime(Date taskActivityStartTime) {
		this.taskActivityStartTime = taskActivityStartTime;
	}
	public String getTaskRecordedActivityStartTime() {
		return taskRecordedActivityStartTime != null ? DateUtils.format(taskRecordedActivityStartTime, DateUtils.DATE_TIME_FORMAT) : "";
	}
	public void setTaskRecordedActivityStartTime(Date taskRecordedActivityStartTime) {
		this.taskRecordedActivityStartTime = taskRecordedActivityStartTime;
	}
	public String getEmployeeeId() {
		return employeeeId != null ? employeeeId : "";
	}
	public void setEmployeeeId(String employeeeId) {
		this.employeeeId = employeeeId;
	}
	public Integer getActDurationInMins() {
		return actDurationInMins != null ? actDurationInMins : 0;
	}
	public void setActDurationInMins(Integer actDurationInMins) {
		this.actDurationInMins = actDurationInMins;
	}

	public String getReportingManagerName() {
		return reportingManagerName == null ? "" : reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
}
