package com.famstack.projectscheduler.employees.bean;

import java.util.Date;

public class UserUtilization {
	int billableMins;
	int nonBillableMins;
	int leaveOrHolidayMins = 0; 
	int leaveMins = 0;
	int holidayMins = 0;
	int noOfWorkingDays;
	Date startDate;
	boolean futureDateTask;
	String userJoinDate;
	String userExitDate;
	boolean enabledUserActiveUtilization;
	
	public double getUtilizationDouble() {
		if (noOfWorkingDays > 0) {
			double totalWorkingDayDurationInMins = (noOfWorkingDays * 8 * 60) - (getLeaveOrHolidayMins());
			double totalUtilizedInMins = billableMins + (enabledUserActiveUtilization ? 0 : nonBillableMins);
			if (totalWorkingDayDurationInMins > 0) {
			 return (totalUtilizedInMins/totalWorkingDayDurationInMins) * 100;		
			}
		}
		return 0;
	}
	
	public String getUtilization() {
		return String.format("%.2f", getUtilizationDouble());
	}
	
	 protected String getTimeInHrs(int timeInMinutes)
	    {
	        int hours = timeInMinutes / 60; // since both are ints, you get an int
	        int minutes = timeInMinutes % 60;
	        return String.format("%d:%02d", hours, minutes);
	    }
	
	public String getTotalHours() {
		return getTimeInHrs(billableMins + (enabledUserActiveUtilization ? 0 : nonBillableMins));
	}
	
	public Integer getTotalMins() {
		return billableMins + (enabledUserActiveUtilization ? 0 : nonBillableMins);
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

	public String getLeaveOrHolidayHours() {
		return getTimeInHrs(getLeaveOrHolidayMins());
	}

	public void setLeaveOrHolidayMins(int leaveOrHolidayMins) {
		this.leaveOrHolidayMins = leaveOrHolidayMins;
	}

	
	public int getLeaveOrHolidayMins() {
		return leaveOrHolidayMins + leaveMins + holidayMins;
	}

	public int getNoOfWorkingDays() {
		return noOfWorkingDays;
	}

	public void setNoOfWorkingDays(int noOfWorkingDays) {
		this.noOfWorkingDays = noOfWorkingDays;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getTotal() {
		return billableMins + nonBillableMins;
	}
	
	public int getTotalWithLeaveMins() {
		return billableMins + nonBillableMins + getLeaveOrHolidayMins();
	}
	
	public String getTotalWithLeaveHrs() {
		return getTimeInHrs(getTotalWithLeaveMins());
	}

	public boolean isNotifyUsers() {
		int totalMins = getTotalWithLeaveMins(); 
		if (totalMins < getNoOfWorkingDays() * 8 * 60) {
			return true;
		}
		return false;
	}

	public boolean isFutureDateTask() {
		return futureDateTask;
	}

	public void setFutureDateTask(boolean futureDateTask) {
		this.futureDateTask = futureDateTask;
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
	
	public String getEstimatedHours() {
		return getTimeInHrs((noOfWorkingDays * 8 * 60) - holidayMins);
	}
	
	public String getActualHours() {
		return getTimeInHrs(getTotal() + getLeaveMins());
	}

	public String getUserJoinDate() {
		return userJoinDate;
	}

	public void setUserJoinDate(String userJoinDate) {
		this.userJoinDate = userJoinDate;
	}

	public String getUserExitDate() {
		return userExitDate;
	}

	public void setUserExitDate(String userExitDate) {
		this.userExitDate = userExitDate;
	}

	public boolean isEnabledUserActiveUtilization() {
		return enabledUserActiveUtilization;
	}

	public void setEnabledUserActiveUtilization(boolean enabledUserActiveUtilization) {
		this.enabledUserActiveUtilization = enabledUserActiveUtilization;
	}
}
