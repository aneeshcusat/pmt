package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "client_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "client_id" }) })
public class ClientItem implements FamstackBaseItem {

	private static final long serialVersionUID = -5628656638213113049L;

	@Id
	@Column(name = "client_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int clientID;

	@Column(name = "name")
	private String name;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@ManyToOne
	@JoinColumn(name = "sub_team_id")
	private ProjectSubTeamItem projectSubTeamItem;

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public ProjectSubTeamItem getProjectSubTeamItem() {
		return projectSubTeamItem;
	}

	public void setProjectSubTeamItem(ProjectSubTeamItem projectSubTeamItem) {
		this.projectSubTeamItem = projectSubTeamItem;
	}

}