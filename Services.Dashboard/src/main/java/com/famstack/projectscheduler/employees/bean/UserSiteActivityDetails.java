package com.famstack.projectscheduler.employees.bean;

import java.util.ArrayList;
import java.util.List;

public class UserSiteActivityDetails {

	String employeeName;
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
}
