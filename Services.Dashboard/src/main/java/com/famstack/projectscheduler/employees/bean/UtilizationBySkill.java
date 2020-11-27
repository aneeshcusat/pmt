package com.famstack.projectscheduler.employees.bean;

public class UtilizationBySkill {
	int billableMins;
	int nonBillableMins;
	int leaveMins = 0;
	int holidayMins = 0;
	String monthYear = null;
	String skill = null;
	
	 public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

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
}
