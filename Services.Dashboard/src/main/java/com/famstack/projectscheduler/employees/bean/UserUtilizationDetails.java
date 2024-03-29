package com.famstack.projectscheduler.employees.bean;

import java.util.List;
import java.util.Set;



public class UserUtilizationDetails  extends UserUtilization{

	String employeeName;
	String userGroupName;
	String reportingManager;
	String emailId;
	String empId;
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
		return isUnderOrOverUtilized() && getLeaveOrHolidayMins() == 0;
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

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}
}
