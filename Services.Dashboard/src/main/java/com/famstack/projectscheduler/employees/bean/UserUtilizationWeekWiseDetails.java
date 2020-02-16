package com.famstack.projectscheduler.employees.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;



public class UserUtilizationWeekWiseDetails {

	String employeeName;
	String reportingManager;
	String emailId;
	Map<String, UserUtilization> userUtilizationMap;
	
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

	public Map<String, UserUtilization> getUserUtilizationMap() {
		return userUtilizationMap;
	}

	public void createUtilizationMap(List<String> yearMonthWeekNumberList, Map<String,Integer> numberOfDaysInAWeekMap) {
		userUtilizationMap= new HashMap<>();
		for(String yearMonthWeekNumber: yearMonthWeekNumberList) {
			String currentWeekNumber = DateUtils.getYearMonthWeekNumber(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1));
			UserUtilization userUtilization = new UserUtilization();
			userUtilization.setNoOfWorkingDays(numberOfDaysInAWeekMap.get(yearMonthWeekNumber));
			if (currentWeekNumber.compareTo(yearMonthWeekNumber) < 0) {
				userUtilization.setFutureDateTask(true);
			}
			
			userUtilizationMap.put(yearMonthWeekNumber, userUtilization);
		}
		
	}

	public boolean isUnderOrOverUtilized() {
		return isNotifyUsers();
	}

	public String getCurrentWeekNumber(){
		return DateUtils.getYearMonthWeekNumber(new Date());
	}
	
	public boolean isNotifyUsers() {
		if (userUtilizationMap != null) {
			
			for (String weekDate : userUtilizationMap.keySet()) {
				UserUtilization userUtilization = userUtilizationMap.get(weekDate);
				if (userUtilization != null && !userUtilization.isFutureDateTask()) {
					return userUtilization.isNotifyUsers();
				}
			}
		}
		return false;
	}
}
