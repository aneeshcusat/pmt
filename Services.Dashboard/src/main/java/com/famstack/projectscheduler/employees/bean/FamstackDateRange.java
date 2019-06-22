package com.famstack.projectscheduler.employees.bean;

import java.util.Date;

public class FamstackDateRange {

	private Date startDate;
	private Date endDate;
	
	public FamstackDateRange(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
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
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		if (startDate != null && endDate != null) {
			return "start Date " + startDate.toString() + ", end Date " + endDate.toString(); 
		}
		return "Empty data";
	}
}
