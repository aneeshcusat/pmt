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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "project_team_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "team_id" }) })
public class ProjectTeamItem implements FamstackBaseItem {

	private static final long serialVersionUID = -5628656638213113049L;

	@Id
	@Column(name = "team_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int teamId;

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "poc")
	private String poc;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "projectTeamItem", cascade = CascadeType.ALL)
	private Set<ProjectSubTeamItem> projectSubTeam;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_team_multi", joinColumns = {
			@JoinColumn(name = "team_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "account_id", nullable = false, updatable = false) })
	private Set<AccountItem> accountItems;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
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

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
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

	public Set<ProjectSubTeamItem> getProjectSubTeam() {
		return projectSubTeam;
	}

	public void setProjectSubTeam(Set<ProjectSubTeamItem> projectSubTeam) {
		this.projectSubTeam = projectSubTeam;
	}

	public Set<AccountItem> getAccountItems() {
		return accountItems;
	}

	public void setAccountItems(Set<AccountItem> accountItems) {
		this.accountItems = accountItems;
	}

}
