package com.famstack.projectscheduler.employees.bean;

public class EmployeeBWDetails {
	
	private int userId;
	private String firstName;
	private Integer todayUtilization;
	private Integer yesterdayUtilization;
	private String dateString;
	private String taskType;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		if (firstName != null){
			this.firstName = firstName.trim();
		}
	}
	public String getTodayUtilization() {
		return todayUtilization== null ? "--" :""+ todayUtilization;
	}
	public void setTodayUtilization(Integer todayUtilization) {
		this.todayUtilization = todayUtilization > 0 ? todayUtilization :0;
	}
	public String getYesterdayUtilization() {
		return yesterdayUtilization == null ? "--" : ""+yesterdayUtilization;
	}
	public void setYesterdayUtilization(Integer yesterdayUtilization) {
		this.yesterdayUtilization = yesterdayUtilization > 0 ? yesterdayUtilization :0;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	public String getOnlyFirstName() {
		if (firstName != null &&  firstName.indexOf(" ") > 0) {
			return  firstName.substring(0, firstName.indexOf(" "))+" " + firstName.substring(firstName.indexOf(" ") +1 , firstName.indexOf(" ")+2);
		}  
		return firstName;
    }

}
