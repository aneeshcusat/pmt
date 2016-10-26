package com.famstack.projectscheduler.employees.bean;

public class UserStatus {
	private int userId;
	private int status;
	private String userAvailableMsg;
	private boolean leave;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isLeave() {
		return leave;
	}

	public void setLeave(boolean leave) {
		this.leave = leave;
	}

	public String getUserAvailableMsg() {
		return userAvailableMsg;
	}

	public void setUserAvailableMsg(String userAvailableMsg) {
		this.userAvailableMsg = userAvailableMsg;
	}

}
