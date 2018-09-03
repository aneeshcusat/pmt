package com.famstack.projectscheduler.employees.bean;

public class EmployeeBWDetails {
	
	private int userId;
	private String firstName;
	private Integer todayUtilization;
	private Integer yesterdayUtilization;
	
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
		this.firstName = firstName;
	}
	public String getTodayUtilization() {
		return todayUtilization== null ? "--" :""+ todayUtilization;
	}
	public void setTodayUtilization(Integer todayUtilization) {
		this.todayUtilization = todayUtilization;
	}
	public String getYesterdayUtilization() {
		return yesterdayUtilization == null ? "--" : ""+yesterdayUtilization;
	}
	public void setYesterdayUtilization(Integer yesterdayUtilization) {
		this.yesterdayUtilization = yesterdayUtilization;
	}

}
