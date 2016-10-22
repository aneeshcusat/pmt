package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.famstack.projectscheduler.contants.UserTaskType;

@Entity
@Table(name = "user_task_activity_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_tsk_act_id" }) })
public class UserTaskActivityItem implements FamstackBaseItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5388442861793363726L;

	@Id
	@Column(name = "user_tsk_act_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private UserTaskType type;

	@Column(name = "task_id")
	private int taskId;

	@Column(name = "task_name")
	private String taskName;

	@Column(name = "duration")
	private int duration;

	@Column(name = "start_hour")
	private int startHour;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "start_time")
	private Timestamp startTime;

	@Column(name = "actual_start_time")
	private Timestamp actualStartTime;

	@Column(name = "actual_end_time")
	private Timestamp actualEndTime;

	@Column(name = "inprogress_comment")
	private String inprogressComment;

	@Column(name = "completion_comment")
	private String completionComment;

	@ManyToOne
	@JoinColumn(name = "user_act_id")
	private UserActivityItem userActivityItem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserTaskType getType() {
		return type;
	}

	public void setType(UserTaskType type) {
		this.type = type;
	}

	@Override
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Timestamp getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Timestamp actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Timestamp getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(Timestamp actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

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

	public UserActivityItem getUserActivityItem() {
		return userActivityItem;
	}

	public void setUserActivityItem(UserActivityItem userActivityItem) {
		this.userActivityItem = userActivityItem;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
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
