package com.famstack.email.contants;

/**
 * The Enum EmailTemplates.
 */
public enum Templates
{

    /** The default. */
    DEFAULT("default", "default"),

    /** The user registraion. */
    USER_REGISTRAION("userRegistraion", "userRegistraion"),

    /** The reset password. */
    RESET_PASSWORD("resetPassword", "resetPassword"),

    /** The user update. */
    USER_UPDATE("userStatus", "userDetailsUpdate"),

    /** The project create. */
    PROJECT_CREATE("projectStatus", "projectCreated"),

    /** The project update. */
    PROJECT_UPDATE("projectStatus", "projectDetailsUpdate"),

    /** The project delete. */
    PROJECT_DELETE("projectStatus", "projectDelete"),

    /** The task created. */
    TASK_CREATED("projectTaskStatus", "projectTaskCreated"),

    /** The task updated. */
    TASK_UPDATED("projectTaskStatus", "projectTaskDetailsUpdate"),

    /** The task deleted. */
    TASK_DELETED("projectTaskStatus", "projectTaskDelete"),

    /** The task created assigned. */
    TASK_CREATED_ASSIGNED("projectTaskStatus", "projectTaskCreateAssigned"),

    /** The task assigned. */
    TASK_ASSIGNED("projectTaskStatus", "projectTaskAssigned"),

    /** The task closed. */
    TASK_CLOSED("projectTaskStatus", "projectTaskClosed"),

    /** The task inprogress. */
    TASK_INPROGRESS("projectTaskStatus", "projectTaskInProgress"),

    /** The task completed. */
    TASK_COMPLETED("projectTaskStatus", "projectTaskCompleted"),

    /** The forgot password. */
    FORGOT_PASSWORD("userRegistraion", "forgotPassword"),

    /** The project comment added. */
    PROJECT_COMMENT_ADDED("projectStatus", "projectCommentAdded"),

    /** The project end reminder. */
    PROJECT_END_REMINDER("projectStatus", "projectEndReminder"),

    /** The project start reminder. */
    PROJECT_START_REMINDER("projectStatus", "projectStartReminder"),

    /** The project deadline missed. */
    PROJECT_DEADLINE_MISSED("projectStatus", "projectDeadLineMissed"),

    /** The task deadline missed. */
    TASK_DEADLINE_MISSED("projectTaskStatus", "projectTaskDeadLineMissed"),

    /** The task end reminder. */
    TASK_END_REMINDER("projectTaskStatus", "projectTaskEndReminder"),

    /** The task start reminder. */
    TASK_START_REMINDER("projectTaskStatus", "projectTaskStartReminder"),

    TASK_RE_ASSIGNED("projectTaskStatus", "projectTaskAssigned"),

    TASK_AUTO_PAUSED("projectTaskStatus", "projectTaskPaused");

    /** The subjectkey. */
    private String subjectkey;

    /** The template. */
    private String template;

    /**
     * Instantiates a new email templates.
     * 
     * @param template the template
     * @param subjectKey the subject key
     */
    Templates(String template, String subjectKey)
    {
        this.template = template;
        this.subjectkey = subjectKey;
    }

    /**
     * Gets the subjectkey.
     * 
     * @return the subjectkey
     */
    public String getSubjectkey()
    {
        return subjectkey;
    }

    /**
     * Gets the template.
     * 
     * @return the template
     */
    public String getTemplate()
    {
        return template;
    }

}
