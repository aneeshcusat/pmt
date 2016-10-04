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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "account_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "account_id" }) })
public class AccountItem implements FamstackBaseItem {

	private static final long serialVersionUID = -5628656638213113049L;

	@Id
	@Column(name = "account_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int accountId;

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "holder")
	private String holder;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "accountItem", cascade = CascadeType.ALL)
	private Set<ProjectTeamItem> projectTeam;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
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

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
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

	public Set<ProjectTeamItem> getProjectTeam() {
		return projectTeam;
	}

	public void setProjectTeam(Set<ProjectTeamItem> projectTeam) {
		this.projectTeam = projectTeam;
	}

}
