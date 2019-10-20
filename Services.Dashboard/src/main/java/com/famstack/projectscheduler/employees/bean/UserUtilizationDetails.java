package com.famstack.projectscheduler.employees.bean;



public class UserUtilizationDetails  extends UserUtilization{

	String employeeName;
	String reportingManager;
	String emailId;
	
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
		return isUnderOrOverUtilized() && leaveOrHoliday == 0;
	}
}
