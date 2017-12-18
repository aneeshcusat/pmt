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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.famstack.projectscheduler.contants.ProjectActivityType;
import com.famstack.projectscheduler.contants.ProjectStatus;

@Entity
@Table(name = "project_activity_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"activity_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class ProjectActivityItem implements FamstackBaseItem
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -2318977744597290616L;

    @Id
    @Column(name = "activity_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int activityId;

    @Column(name = "user_grp_id")
    private String userGroupId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProjectActivityType type;

    @Column(name = "project_status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectItem projectItem;

    public ProjectItem getProjectItem()
    {
        return projectItem;
    }

    public void setProjectItem(ProjectItem projectItem)
    {
        this.projectItem = projectItem;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getActivityId()
    {
        return activityId;
    }

    public void setActivityId(int activityId)
    {
        this.activityId = activityId;
    }

    public ProjectActivityType getType()
    {
        return type;
    }

    public void setType(ProjectActivityType type)
    {
        this.type = type;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public ProjectStatus getProjectStatus()
    {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus)
    {
        this.projectStatus = projectStatus;
    }

    public String getUserGroupId()
    {
        return userGroupId;
    }

    @Override
    public void setUserGroupId(String userGroupId)
    {
        this.userGroupId = userGroupId;
    }

}
