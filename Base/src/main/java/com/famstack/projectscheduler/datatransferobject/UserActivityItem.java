package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user_activity_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_act_id" }) })
public class UserActivityItem implements FamstackBaseItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5145320086419804408L;

	@Id
	@Column(name = "user_act_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int id;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "userActivityItem", cascade = CascadeType.ALL)
	private Set<UserTaskActivityItem> userTaskActivities;

	@Column(name = "calender_date")
	private Timestamp calenderDate;

	@Column(name = "leave")
	private boolean leave;

	@Column(name = "billable_hours")
	private boolean billableHours;

	@Column(name = "productive_housrs")
	private boolean productiveHousrs;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id")
	private UserItem userItem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Set<UserTaskActivityItem> getUserTaskActivities() {
		return userTaskActivities;
	}

	public void setUserTaskActivities(Set<UserTaskActivityItem> userTaskActivities) {
		this.userTaskActivities = userTaskActivities;
	}

	public Timestamp getCalenderDate() {
		return calenderDate;
	}

	public void setCalenderDate(Timestamp calenderDate) {
		this.calenderDate = calenderDate;
	}

	public boolean isLeave() {
		return leave;
	}

	public void setLeave(boolean leave) {
		this.leave = leave;
	}

	public boolean isBillableHours() {
		return billableHours;
	}

	public void setBillableHours(boolean billableHours) {
		this.billableHours = billableHours;
	}

	public boolean isProductiveHousrs() {
		return productiveHousrs;
	}

	public void setProductiveHousrs(boolean productiveHousrs) {
		this.productiveHousrs = productiveHousrs;
	}

	public UserItem getUserItem() {
		return userItem;
	}

	public void setUserItem(UserItem userItem) {
		this.userItem = userItem;
	}

}
