package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

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
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

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
	
	@Column(name = "exclude_mail", columnDefinition = "LONGTEXT")
	private String excludeMails;

	@Column(name = "subject", columnDefinition = "LONGTEXT")
	private String subject;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "notify_defaulter")
	private Boolean notifyDefaulters;
	
	@Column(name = "start_days")
	private Integer startDays;
	
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
		return enabled == null ? false : enabled;
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

	public Integer getStartDays() {
		return startDays == null ? 0 : startDays;
	}

	public void setStartDays(Integer startDays) {
		this.startDays = startDays;
	}

	public String getReportingDate(){
		if (getStartDays() == getLastHowManyDays()) {
			return getReportingDateString(getStartDays());
		} else {
			return getReportingDateString(getStartDays()) +" - "+getReportingDateString(getLastHowManyDays());
		}
	}
	
	private String getReportingDateString(int value){
		if (value == 0){
			return "Current Day";
		} else if (value == -1){
			return "Previous Day";
		} else if (value == -8){
			return "Current Month";
		} else if (value == -9){
			return "Previous week";
		} else {
			return (value *-1) + "Days before";
		}
	}
	
	public String getScheduleTime(){
		String[] cronString = cronExpression.split(" ");
		if (cronString.length > 5) {
			String time = " At " +  cronString[2];
			String dayString =  getDayString(cronString[5]);
			return "Every " + dayString + time;
		}
		return "cronExpression";
	}
	
	public String getCronDay(){
		String[] cronString = cronExpression.split(" ");
		if (cronString.length > 5) {
			return cronString[5];
		}
		return "";
	}
	
	public String getCronTime(){
		String[] cronString = cronExpression.split(" ");
		return cronString[2];
	}

	private String getDayString(String day) {
		if ("MON,TUE,WED,THU,FRI".equalsIgnoreCase(day)) {
			return "Week Day";
		} else 	if ("TUE,WED,THU,FRI,SAT".equalsIgnoreCase(day)) {
			return "Tuesday-Saturday";
		} else if ("MON,TUE,WED,THU,FRI,SAT,SUN".equalsIgnoreCase(day)) {
			return "Day";
		} else if ("SAT,SUN".equalsIgnoreCase(day)) {
			return "Weekends";
		} else if ("MON".equalsIgnoreCase(day)) {
			return "Monday";
		} else if ("TUE".equalsIgnoreCase(day)) {
			return "Tuesday";
		} else if ("WED".equalsIgnoreCase(day)) {
			return "Wednesday";
		} else if ("THU".equalsIgnoreCase(day)) {
			return "Thursday";
		} else if ("FRI".equalsIgnoreCase(day)) {
			return "Friday";
		}else if ("SAT".equalsIgnoreCase(day)) {
			return "Saturday";
		}else if ("SUN".equalsIgnoreCase(day)) {
			return "Sunday";
		}
		return "Day";
	}
	
	public List<String> getEmailToList(){
		if (StringUtils.isNotBlank(toList)) {
			return Arrays.asList(toList.split(","));
		}
		return null;
	}
	
	public List<String> getEmailCCList(){
		if (StringUtils.isNotBlank(ccList)) {
			return Arrays.asList(ccList.split(","));
		}
		return null;
	}
	
	public String getEndDateString(){
		if (endDate != null) {
			return DateUtils.format(endDate, DateUtils.DATE_FORMAT);
		}
		return "";
	}

	public Boolean getNotifyDefaulters() {
		return notifyDefaulters == null ? false : notifyDefaulters;
	}

	public void setNotifyDefaulters(Boolean notifyDefaulters) {
		this.notifyDefaulters = notifyDefaulters;
	}

	public String getExcludeMails() {
		return excludeMails;
	}

	public void setExcludeMails(String excludeMails) {
		this.excludeMails = excludeMails;
	}

	public List<String> getExcludeMailList(){
		if (StringUtils.isNotBlank(excludeMails)) {
			return Arrays.asList(excludeMails.split(","));
		}
		return null;
	}
}
