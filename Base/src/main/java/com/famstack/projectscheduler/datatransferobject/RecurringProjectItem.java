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

import com.famstack.projectscheduler.contants.RecurringType;

@Entity
@Table(name = "recurring_prj_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class RecurringProjectItem implements FamstackBaseItem
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

    @Column(name = "project_id")
    private Integer projectId;
    
    @Column(name = "task_id")
    private Integer taskId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private RecurringType type;
    
    @Column(name = "project_code")
    private String projectCode;

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
    
    @Column(name = "recurre_original")
    private Boolean recurreOriginal;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    public String getProjectCode()
    {
        return projectCode;
    }

    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }

    public Integer getRequestedBy()
    {
        return requestedBy;
    }

    public void setRequestedBy(Integer requestedBy)
    {
        this.requestedBy = requestedBy;
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

    public Timestamp getNextRun()
    {
        return nextRun;
    }

    public void setNextRun(Timestamp nextRun)
    {
        this.nextRun = nextRun;
    }

    public Timestamp getLastRun()
    {
        return lastRun;
    }

    public void setLastRun(Timestamp lastRun)
    {
        this.lastRun = lastRun;
    }

    public Integer getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }

    public Timestamp getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Timestamp endDate)
    {
        this.endDate = endDate;
    }

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public RecurringType getType() {
		return type == null ? RecurringType.PROJECT : type;
	}

	public void setType(RecurringType type) {
		this.type = type;
	}

	public Boolean getRecurreOriginal() {
		return recurreOriginal;
	}

	public void setRecurreOriginal(Boolean recurreOriginal) {
		this.recurreOriginal = recurreOriginal;
	}

}
