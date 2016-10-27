package com.famstack.email.contants;

/**
 * The Enum EmailTemplates.
 */
public enum EmailTemplates {

	/** The default. */
	DEFAULT("default", "default"),

	/** The user registraion. */
	USER_REGISTRAION("userRegistraion",
			"userRegistraion"), /** The reset password. */
	RESET_PASSWORD("resetPassword", "resetPassword"), /** The user update. */
	USER_UPDATE("userStatus", "userDetailsUpdate"), /** The project create. */
	PROJECT_CREATE("projectStatus",
			"projectCreated"), /** The project update. */
	PROJECT_UPDATE("projectStatus",
			"projectDetailsUpdate"), /** The project delete. */
	PROJECT_DELETE("projectStatus", "projectDelete"), /** The task created. */
	TASK_CREATED("projectTaskStatus",
			"projectTaskCreated"), /** The task updated. */
	TASK_UPDATED("projectTaskStatus",
			"projectTaskDetailsUpdate"), /** The task deleted. */
	TASK_DELETED("projectTaskStatus",
			"projectTaskDelete"), /** The task created assigned. */
	TASK_CREATED_ASSIGNED("projectTaskStatus",
			"projectTaskCreateAssigned"), /** The task assigned. */
	TASK_ASSIGNED("projectTaskStatus", "projectTaskAssigned"), TASK_CLOSED("projectTaskStatus",
			"projectTaskClosed"), TASK_INPROGRESS("projectTaskStatus", "projectTaskInProgress"), TASK_COMPLETED(
					"projectTaskStatus", "projectTaskCompleted"), FORGOT_PASSWORD("userRegistraion",
							"forgotPassword"), PROJECT_COMMENT_ADDED("projectStatus", "projectCommentAdded");

	/** The value. */
	private String subjectkey;

	private String template;

	/**
	 * Instantiates a new email templates.
	 *
	 * @param value
	 *            the value
	 */
	EmailTemplates(String template, String subjectKey) {
		this.template = template;
		this.subjectkey = subjectKey;
	}

	public String getSubjectkey() {
		return subjectkey;
	}

	public String getTemplate() {
		return template;
	}

}
