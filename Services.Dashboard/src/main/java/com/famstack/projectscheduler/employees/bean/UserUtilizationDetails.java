package com.famstack.projectscheduler.employees.bean;

import java.util.List;
import java.util.Set;



public class UserUtilizationDetails  extends UserUtilization{

	String employeeName;
	String reportingManager;
	String emailId;
	List<UtilizationProjectDetails> utilizationProjectDetailsList;
	private Set<String> teamMembers;
	
	private Boolean funded;
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isUnderOrOverUtilized() {
		double utilization = getUtilizationDouble();
		if (utilization > 120 || utilization < 80) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isNotifyUsers(){
		return isUnderOrOverUtilized() && leaveOrHolidayMins == 0;
	}

	public List<UtilizationProjectDetails> getUtilizationProjectDetailsList() {
		return utilizationProjectDetailsList;
	}

	public void setUtilizationProjectDetailsList(
			List<UtilizationProjectDetails> utilizationProjectDetailsList) {
		this.utilizationProjectDetailsList = utilizationProjectDetailsList;
	}

	public Boolean getFunded() {
		return funded;
	}

	public void setFunded(Boolean funded) {
		this.funded = funded;
	}

	public Set<String> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(Set<String> teamMembers) {
		this.teamMembers = teamMembers;
	}
}
