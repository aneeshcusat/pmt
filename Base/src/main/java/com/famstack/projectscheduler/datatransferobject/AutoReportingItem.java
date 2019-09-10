package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.famstack.projectscheduler.contants.ReportType;

@Entity
@Table(name = "auto_report_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class AutoReportingItem implements FamstackBaseItem
{

    private static final long serialVersionUID = -5628656638213113049L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "next_run")
    private Timestamp nextRun;

    @Column(name = "last_run")
    private Timestamp lastRun;

    @Column(name = "cron_exp")
    private String cronExpression;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
	private ReportType type;
    
	@Column(name = "cc_list", columnDefinition = "LONGTEXT")
	private String ccList;

	@Column(name = "to_lit", columnDefinition = "LONGTEXT")
	private String toList;

	@Column(name = "subject")
	private String subject;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "last_hm_days")
	private Integer lastHowManyDays;

	@Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "requested_by")
    private Integer requestedBy;

    @Column(name = "user_grp_id")
    private String userGroupId;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getNextRun() {
		return nextRun;
	}

	public void setNextRun(Timestamp nextRun) {
		this.nextRun = nextRun;
	}

	public Timestamp getLastRun() {
		return lastRun;
	}

	public void setLastRun(Timestamp lastRun) {
		this.lastRun = lastRun;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	public String getToList() {
		return toList;
	}

	public void setToList(String toList) {
		this.toList = toList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Integer getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(Integer requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
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

	public Boolean getEnabled() {
		return enabled == null ? true : enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getLastHowManyDays() {
		return lastHowManyDays == null ? 0 : lastHowManyDays;
	}

	public void setLastHowManyDays(Integer lastHowManyDays) {
		this.lastHowManyDays = lastHowManyDays;
	}

}
