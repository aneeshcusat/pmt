package com.famstack.email.contants;

public enum EmailTemplates {
	DEFAULT("default"),

	USER_REGISTRAION("userRegistraion");

	private String value;

	EmailTemplates(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
