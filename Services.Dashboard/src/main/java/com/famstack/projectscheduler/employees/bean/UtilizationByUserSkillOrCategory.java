package com.famstack.projectscheduler.employees.bean;

import java.util.Set;

public class UtilizationByUserSkillOrCategory {
	int billableMins;
	int nonBillableMins;
	int leaveMins = 0;
	int holidayMins = 0;
	String monthYear = null;
	String skillOrCategory = null;
	String employeeName = null;
	String userGroupName = null;
	String employeeCode = null;
	String designation = null;
	Integer employeeId = null;
	Set<String> projectAccounts;
	
	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	protected String getTimeInHrs(int timeInMinutes)
	    {
	        int hours = timeInMinutes / 60; // since both are ints, you get an int
	        int minutes = timeInMinutes % 60;
	        return String.format("%d:%02d", hours, minutes);
	    }
	
	public String getBillableHours() {
		return getTimeInHrs(billableMins);
	}

	public void setBillableMins(int billableHours) {
		this.billableMins = billableHours;
	}

	public String getNonBillableHours() {
		return getTimeInHrs(nonBillableMins);
	}

	public void setNonBillableMins(int nonBillableHours) {
		this.nonBillableMins = nonBillableHours;
	}

	public int getTotal() {
		return billableMins + nonBillableMins;
	}
	
	public String getTotalHrs() {
		return getTimeInHrs(getTotal());
	}

	public int getBillableMins() {
		return billableMins;
	}

	public int getNonBillableMins() {
		return nonBillableMins;
	}

	public int getLeaveMins() {
		return leaveMins;
	}

	public void setLeaveMins(int leaveMins) {
		this.leaveMins = leaveMins;
	}

	public int getHolidayMins() {
		return holidayMins;
	}

	public void setHolidayMins(int holidayMins) {
		this.holidayMins = holidayMins;
	}

	public String getSkillOrCategory() {
		return skillOrCategory;
	}

	public void setSkillOrCategory(String skillOrCategory) {
		this.skillOrCategory = skillOrCategory;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public void setProjectAccounts(Set<String> projectAccounts) {
		this.projectAccounts = projectAccounts;
	}
	
	public Set<String> getProjectAccounts() {
		return projectAccounts;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}
}
