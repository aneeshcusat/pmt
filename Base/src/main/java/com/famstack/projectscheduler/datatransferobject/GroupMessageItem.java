package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "group_message_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "message_id" }) })
public class GroupMessageItem implements FamstackBaseItem {

	private static final long serialVersionUID = -5628656638213113049L;
	
	@Id
	@Column(name = "message_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int messageId;

	@Column(name = "description")
	@Lob
	private String description;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;
	
	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = false)
	@JoinColumn(name = "user_id")
	private UserItem user;
	
	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = false)
	@JoinColumn(name = "group_id")
	private GroupItem groupItem;



	public GroupItem getGroupItem() {
		return groupItem;
	}

	public void setGroupItem(GroupItem groupItem) {
		this.groupItem = groupItem;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
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

	public UserItem getUser() {
		return user;
	}

	public void setUser(UserItem user) {
		this.user = user;
	}

		
}