package com.famstack.projectscheduler.employees.bean;

import java.util.Date;
import java.util.Set;

import com.famstack.projectscheduler.util.DateUtils;

public class UtilizationProjectDetails {
	private String teamName;
	private String accountName;
	private Integer year;
	private Integer month;
	private Integer weekNumber;
	private String clientName;
	private String projectNumber;
	private String projectName;
	private Date startDate;
	private Date endDate;
	private String userGroupName;
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getWeekNumber() {
		return weekNumber;
	}
	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public String getStartDateString() {
		return DateUtils.format(startDate, DateUtils.DATE_FORMAT);
	}
	public String getEndDateString() {
		return DateUtils.format(endDate, DateUtils.DATE_FORMAT);
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getUserGroupName() {
		return userGroupName;
	}
	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

}
