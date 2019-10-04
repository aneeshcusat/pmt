package com.famstack.projectscheduler.employees.bean;

import java.util.ArrayList;
import java.util.List;

public class UserSiteActivityDetails {

	private String employeeName;
	private String reportingManager;
	private boolean includeInactive;
	private String emailId;
	
	List<UserSiteActivityStatus> statusList;

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<UserSiteActivityStatus> getStatusList() {
		if (statusList == null) {
			statusList = new ArrayList<>();
		}
		return statusList;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public boolean isIncludeInactive() {
		return includeInactive;
	}

	public void setIncludeInactive(boolean includeInactive) {
		this.includeInactive = includeInactive;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
