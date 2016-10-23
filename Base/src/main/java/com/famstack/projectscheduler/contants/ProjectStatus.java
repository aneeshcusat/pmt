package com.famstack.projectscheduler.contants;

public enum ProjectStatus {

	NEW("NEW"), UNASSIGNED("UNASSIGNED"), ASSIGNED("ASSIGNED"), INPROGRESS("INPROGRESS"), REVIEW("REVIEW"), COMPLETED(
			"COMPLETED"), CLOSED("CLOSED"), MISSED("MISSED");

	private String value;

	ProjectStatus(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
