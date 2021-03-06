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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.UserTaskType;

@Entity
@Table(name = "user_task_activity_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_tsk_act_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class UserTaskActivityItem implements FamstackBaseItem
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 5388442861793363726L;

    @Id
    @Column(name = "user_tsk_act_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int id;

    @Column(name = "user_grp_id")
    private String userGroupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserTaskType type;

    @Column(name = "task_act_category")
    private String taskActCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type")
    private ProjectType projectType;

    @Column(name = "task_id")
    private int taskId;
    
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "team_name")
    private String teamName;
    
    @Column(name = "client_partner")
    private String clientPartner;
    
    @Column(name = "division")
    private String division;
    
    @Column(name = "account")
    private String account;
    
    @Column(name = "order_book_number")
    private String orderBookNumber;
    
    @Column(name = "reference_no")
    private String referenceNo;
    
    @Column(name = "act_prj_name")
    private String actProjectName;
    
    @Column(name = "duration")
    private int durationInMinutes;

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

    @Column(name = "recorded_start_time")
    private Timestamp recordedStartTime;

    @Column(name = "recorded_end_time")
    private Timestamp recordedEndTime;

    @Column(name = "inprogress_comment", columnDefinition="LONGTEXT")
    private String inprogressComment;

    @Column(name = "completion_comment", columnDefinition="LONGTEXT")
    private String completionComment;
    
    @Column(name = "modified_by")
    private Integer modifiedBy;

    @ManyToOne
    @JoinColumn(name = "user_act_id")
    private UserActivityItem userActivityItem;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public UserTaskType getType()
    {
        return type;
    }

    public void setType(UserTaskType type)
    {
        this.type = type;
    }

    @Override
    public Timestamp getCreatedDate()
    {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Timestamp createdDate)
    {
        this.createdDate = createdDate;
    }

    public Timestamp getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Timestamp lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Timestamp getActualStartTime()
    {
        return actualStartTime;
    }

    public void setActualStartTime(Timestamp actualStartTime)
    {
        this.actualStartTime = actualStartTime;
    }

    public Timestamp getActualEndTime()
    {
        return actualEndTime;
    }

    public void setActualEndTime(Timestamp actualEndTime)
    {
        this.actualEndTime = actualEndTime;
    }

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public UserActivityItem getUserActivityItem()
    {
        return userActivityItem;
    }

    public void setUserActivityItem(UserActivityItem userActivityItem)
    {
        this.userActivityItem = userActivityItem;
    }

    public int getStartHour()
    {
        return startHour;
    }

    public void setStartHour(int startHour)
    {
        this.startHour = startHour;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public Timestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Timestamp startTime)
    {
        this.startTime = startTime;
    }

    public String getInprogressComment()
    {
        return inprogressComment;
    }

    public void setInprogressComment(String inprogressComment)
    {
        this.inprogressComment = inprogressComment;
    }

    public String getCompletionComment()
    {
        return completionComment;
    }

    public void setCompletionComment(String completionComment)
    {
        this.completionComment = completionComment;
    }

    public Timestamp getRecordedStartTime()
    {
        return recordedStartTime;
    }

    public void setRecordedStartTime(Timestamp recordedStartTime)
    {
        this.recordedStartTime = recordedStartTime;
    }

    public Timestamp getRecordedEndTime()
    {
        return recordedEndTime;
    }

    public void setRecordedEndTime(Timestamp recordedEndTime)
    {
        this.recordedEndTime = recordedEndTime;
    }

    public int getDurationInMinutes()
    {
        return  durationInMinutes < 0 ? 0 : durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes)
    {
        this.durationInMinutes = durationInMinutes;
    }

    public ProjectType getProjectType()
    {
        return projectType;
    }

    public void setProjectType(ProjectType projectType)
    {
        this.projectType = projectType;
    }

    @Override
    public String getUserGroupId()
    {
        return userGroupId;
    }

    @Override
    public void setUserGroupId(String userGroupId)
    {
        this.userGroupId = userGroupId;
    }

    public String getTaskActCategory()
    {
        return taskActCategory;
    }

    public void setTaskActCategory(String taskActCategory)
    {
        this.taskActCategory = taskActCategory;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getClientPartner() {
		return clientPartner;
	}

	public void setClientPartner(String clientPartner) {
		this.clientPartner = clientPartner;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOrderBookNumber() {
		return orderBookNumber;
	}

	public void setOrderBookNumber(String orderBookNumber) {
		this.orderBookNumber = orderBookNumber;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getActProjectName() {
		return actProjectName;
	}

	public void setActProjectName(String actProjectName) {
		this.actProjectName = actProjectName;
	}

}
