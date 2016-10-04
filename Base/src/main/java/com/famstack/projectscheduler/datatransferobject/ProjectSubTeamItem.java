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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "project_subteam_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "sub_team_id" }) })
public class ProjectSubTeamItem implements FamstackBaseItem {

	private static final long serialVersionUID = -5628656638213113049L;

	@Id
	@Column(name = "sub_team_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int subTeamId;

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "po_id")
	private String poId;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "projectSubTeamItem", cascade = CascadeType.ALL)
	private Set<ClientItem> clientItem;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private ProjectTeamItem projectTeamItem;

	public int getSubTeamId() {
		return subTeamId;
	}

	public void setSubTeamId(int subTeamId) {
		this.subTeamId = subTeamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
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

	public Set<ClientItem> getClientItem() {
		return clientItem;
	}

	public void setClientItem(Set<ClientItem> clientItem) {
		this.clientItem = clientItem;
	}

	public ProjectTeamItem getProjectTeamItem() {
		return projectTeamItem;
	}

	public void setProjectTeamItem(ProjectTeamItem projectTeamItem) {
		this.projectTeamItem = projectTeamItem;
	}

}
