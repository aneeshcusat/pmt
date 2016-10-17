package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Date;

import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.TaskStatus;

public class TaskDetails {

	private int taskId;

	private int projectId;

	private String name;

	private String description;

	private Timestamp createdDate;

	private Timestamp lastModifiedDate;

	private String reporterName;

	private ProjectPriority priority;

	private String startTime;

	private String completionTime;

	private Integer duration;

	private int assignee;

	private int[] helper;

	private String helpersList;

	private TaskStatus status;

	private TaskActivityDetails taskActivityDetails;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public ProjectPriority getPriority() {
		return priority;
	}

	public void setPriority(ProjectPriority priority) {
		this.priority = priority;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getAssignee() {
		return assignee;
	}

	public void setAssignee(int assignee) {
		this.assignee = assignee;
	}

	public int[] getHelper() {
		return helper;
	}

	public void setHelper(int[] helper) {
		this.helper = helper;
	}

	public TaskActivityDetails getTaskActivityDetails() {
		return taskActivityDetails;
	}

	public void setTaskActivityDetails(TaskActivityDetails taskActivityDetails) {
		this.taskActivityDetails = taskActivityDetails;
	}

	public String getHelpersList() {
		return helpersList;
	}

	public void setHelpersList(String helpersList) {
		this.helpersList = helpersList;
	}

	public long getPercentageOfTaskCompleted() {
		if (status == TaskStatus.COMPLETED) {
			return 100;
		} else if ((status == TaskStatus.INPROGRESS)) {
			long durationInMinute = duration * 60 * 1000;
			long taskRemainingTime = getTaskRemainingTime();
			long percentageOfWorkCompleted = 100 - (taskRemainingTime / durationInMinute) * 100;
			return percentageOfWorkCompleted;
		}

		return 0;

	}

	public long getTaskRemainingTime() {
		long durationInMinute = duration * 60 * 1000;
		if (taskActivityDetails == null) {
			return durationInMinute;
		}
		Date actualStartTime = taskActivityDetails.getActualStartTime();
		long diffInMinute = durationInMinute;
		if (actualStartTime != null) {

			long diff = new Date().getTime() - actualStartTime.getTime();
			diff = (durationInMinute * 60) - diff;
			diffInMinute = diff / (60 * 1000) % 60;
		}
		return diffInMinute;
	}

}
