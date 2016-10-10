package com.famstack.projectscheduler.contants;

public enum TaskStatus {

	NEW("NEW"), ASSIGNED("ASSIGNED"), INPROGRESS("INPROGRESS"), COMPLETED("COMPLETED"), CLOSED("CLOSED");

	private String value;

	TaskStatus(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
