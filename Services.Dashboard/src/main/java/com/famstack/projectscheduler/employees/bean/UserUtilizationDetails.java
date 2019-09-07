package com.famstack.projectscheduler.employees.bean;



public class UserUtilizationDetails {

	String employeeName;
	int billableHours;
	int nonBillableHours;
	int leaveOrHoliday;
	int noOfWorkingDays;

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public double getUtilization() {
		if (noOfWorkingDays > 0) {
			double totalWorkingDayDurationInMins = (noOfWorkingDays * 8 * 60) - (leaveOrHoliday);
			double totalUtilizedInMins = billableHours + nonBillableHours;
			if (totalWorkingDayDurationInMins > 0) {
			 return (totalUtilizedInMins/totalWorkingDayDurationInMins) * 100;		
			}
		}
		return 0;
	}
	
	 private String getTimeInHrs(int timeInMinutes)
	    {
	        int hours = timeInMinutes / 60; // since both are ints, you get an int
	        int minutes = timeInMinutes % 60;
	        return String.format("%d:%02d", hours, minutes);
	    }
	
	public String getTotalHours() {
		return getTimeInHrs(billableHours + nonBillableHours);
	}

	public String getBillableHours() {
		return getTimeInHrs(billableHours);
	}

	public void setBillableHours(int billableHours) {
		this.billableHours = billableHours;
	}

	public String getNonBillableHours() {
		return getTimeInHrs(nonBillableHours);
	}

	public void setNonBillableHours(int nonBillableHours) {
		this.nonBillableHours = nonBillableHours;
	}

	public String getLeaveOrHoliday() {
		return getTimeInHrs(leaveOrHoliday);
	}

	public void setLeaveOrHoliday(int leaveOrHoliday) {
		this.leaveOrHoliday = leaveOrHoliday;
	}

	public int getNoOfWorkingDays() {
		return noOfWorkingDays;
	}

	public void setNoOfWorkingDays(int noOfWorkingDays) {
		this.noOfWorkingDays = noOfWorkingDays;
	}

	public boolean isUnderOrOverUtilized() {
		double utilization = getUtilization();
		if (utilization > 120 || utilization < 80) {
			return true;
		}
		return false;
	}
}
