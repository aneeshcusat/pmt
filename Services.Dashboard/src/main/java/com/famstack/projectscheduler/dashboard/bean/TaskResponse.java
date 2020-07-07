package com.famstack.projectscheduler.dashboard.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {
	String taskDate;
	EmployeeResponse employee;
	String taskName;
	String billableHours;
	String nonBillableHours;
	String leaveHolidayHours;
	String totalTaskHours;
	String totalHours;
	String utilizationPercentage;
	String taskCategory;
	public EmployeeResponse getEmployee() {
		return employee;
	}
	public void setEmployee(EmployeeResponse employee) {
		this.employee = employee;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getBillableHours() {
		return billableHours;
	}
	public void setBillableHours(String billableHours) {
		this.billableHours = billableHours;
	}
	public String getNonBillableHours() {
		return nonBillableHours;
	}
	public void setNonBillableHours(String nonBillableHours) {
		this.nonBillableHours = nonBillableHours;
	}
	public String getLeaveHolidayHours() {
		return leaveHolidayHours;
	}
	public void setLeaveHolidayHours(String leaveHolidayHours) {
		this.leaveHolidayHours = leaveHolidayHours;
	}
	public String getTotalTaskHours() {
		return totalTaskHours;
	}
	public void setTotalTaskHours(String totalTaskHours) {
		this.totalTaskHours = totalTaskHours;
	}
	public String getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}
	public String getUtilizationPercentage() {
		return utilizationPercentage;
	}
	public void setUtilizationPercentage(String utilizationPercentage) {
		this.utilizationPercentage = utilizationPercentage;
	}
	public String getTaskCategory() {
		return taskCategory;
	}
	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}
}
