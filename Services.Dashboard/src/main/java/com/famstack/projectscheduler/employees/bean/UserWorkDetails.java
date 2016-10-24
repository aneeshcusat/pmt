package com.famstack.projectscheduler.employees.bean;

public class UserWorkDetails {

	private Object userId;
	private Object count;
	private Object billableHours;
	private Object productiveHours;

	public Object getUserId() {
		return userId;
	}

	public void setUserId(Object userId) {
		this.userId = userId;
	}

	public Object getCount() {
		return count;
	}

	public void setCount(Object count) {
		this.count = count;
	}

	public Object getBillableHours() {
		return billableHours;
	}

	public void setBillableHours(Object billableHours) {
		this.billableHours = billableHours;
	}

	public Object getProductiveHours() {
		return productiveHours;
	}

	public void setProductiveHours(Object productiveHours) {
		this.productiveHours = productiveHours;
	}

}
