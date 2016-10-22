package com.famstack.projectscheduler.employees.bean;

import java.util.Date;

import com.famstack.projectscheduler.contants.UserTaskType;

public class TaskActivityDetails {

	private int taskActivityId;

	private int taskId;

	private int startHour;

	private int duration;

	private String dateId;

	private String taskName;

	private int userId;

	private Date startTime;

	private Date actualStartTime;

	private Date actualEndTime;

	private UserTaskType userTaskType;

	private String inprogressComment;

	private String completionComment;

	private long timeTakenToCompleteHour;
	private long timeTakenToCompleteMinute;
	private long timeTakenToCompleteSecond;
	private String timeTakenToComplete;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDateId() {
		return dateId;
	}

	public void setDateId(String dateId) {
		this.dateId = dateId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public UserTaskType getUserTaskType() {
		return userTaskType;
	}

	public void setUserTaskType(UserTaskType userTaskType) {
		this.userTaskType = userTaskType;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getTaskActivityId() {
		return taskActivityId;
	}

	public void setTaskActivityId(int taskActivityId) {
		this.taskActivityId = taskActivityId;
	}

	public Date getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Date actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Date getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public long getTimeTakenToCompleteHour() {
		long diffHours = duration;
		if (actualStartTime != null) {
			long diff = new Date().getTime() - actualStartTime.getTime();
			diff = (duration * (60 * 60 * 1000)) - diff;
			diffHours = diff / (60 * 60 * 1000);
		}

		return timeTakenToCompleteHour = diffHours;
	}

	public long getTimeTakenToCompleteMinute() {
		long diffMinutes = 0;
		if (actualStartTime != null) {
			long diff = new Date().getTime() - actualStartTime.getTime();
			diff = (duration * (60 * 60 * 1000)) - diff;
			diffMinutes = diff / (60 * 1000) % 60;
		}
		return timeTakenToCompleteMinute = diffMinutes;
	}

	public long getTimeTakenToCompleteSecond() {

		long diffSeconds = 0;
		if (actualStartTime != null) {
			long diff = new Date().getTime() - actualStartTime.getTime();
			diff = (duration * (60 * 60 * 1000)) - diff;
			diffSeconds = diff / 1000 % 60;
		}
		return timeTakenToCompleteSecond = diffSeconds;
	}

	public String getTimeTakenToComplete() {
		long diffHours = duration;
		long diffMinutes = 0;
		long diffSeconds = 0;
		if (actualStartTime != null && actualEndTime != null) {
			long diff = actualEndTime.getTime() - actualStartTime.getTime();
			diffHours = diff / (60 * 60 * 1000);
			diffMinutes = diff / (60 * 1000) % 60;
			diffSeconds = diff / 1000 % 60;
		}

		return timeTakenToComplete = (diffHours + ":" + diffMinutes + ":" + diffSeconds);
	}

	public void setTimeTakenToCompleteHour(long timeTakenToCompleteHour) {
		this.timeTakenToCompleteHour = timeTakenToCompleteHour;
	}

	public void setTimeTakenToCompleteMinute(long timeTakenToCompleteMinute) {
		this.timeTakenToCompleteMinute = timeTakenToCompleteMinute;
	}

	public void setTimeTakenToCompleteSecond(long timeTakenToCompleteSecond) {
		this.timeTakenToCompleteSecond = timeTakenToCompleteSecond;
	}

	public void setTimeTakenToComplete(String timeTakenToComplete) {
		this.timeTakenToComplete = timeTakenToComplete;
	}

	public String getInprogressComment() {
		return inprogressComment;
	}

	public void setInprogressComment(String inprogressComment) {
		this.inprogressComment = inprogressComment;
	}

	public String getCompletionComment() {
		return completionComment;
	}

	public void setCompletionComment(String completionComment) {
		this.completionComment = completionComment;
	}

}
