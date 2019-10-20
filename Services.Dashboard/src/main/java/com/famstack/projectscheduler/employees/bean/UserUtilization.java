package com.famstack.projectscheduler.employees.bean;

import java.util.Date;

public class UserUtilization {
	int billableMins;
	int nonBillableMins;
	int leaveOrHoliday = 0;
	int noOfWorkingDays;
	Date startDate;
	boolean futureDateTask;

	public double getUtilizationDouble() {
		if (noOfWorkingDays > 0) {
			double totalWorkingDayDurationInMins = (noOfWorkingDays * 8 * 60) - (leaveOrHoliday);
			double totalUtilizedInMins = billableMins + nonBillableMins;
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
		return getTimeInHrs(billableMins + nonBillableMins);
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

	public String getLeaveOrHoliday() {
		return getTimeInHrs(leaveOrHoliday);
	}

	public void setLeaveOrHoliday(int leaveOrHoliday) {
		this.leaveOrHoliday = leaveOrHoliday;
	}

	
	public int getLeaveHours() {
		return leaveOrHoliday;
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
	
	public int getTotalWithLeave() {
		return billableMins + nonBillableMins + leaveOrHoliday;
	}
	
	public String getTotalWithLeaveHrs() {
		return getTimeInHrs(getTotalWithLeave());
	}

	public boolean isNotifyUsers() {
		int totalHours = getTotalWithLeave(); 
		if (totalHours < getNoOfWorkingDays() * 8 * 60) {
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
}
