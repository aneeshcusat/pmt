package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.famstack.projectscheduler.contants.ProjectComplexity;
import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;

@Entity
@Table(name = "project_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class ProjectItem implements FamstackBaseItem
{

    private static final long serialVersionUID = -5628656638213113049L;

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int projectId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProjectType type;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(name = "reporter")
    private UserItem reporter;

    @Column(name = "category")
    private String category;

    @Column(name = "tags")
    private String tags;

    @Column(name = "watchers")
    private String watchers;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ProjectPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexity")
    private ProjectComplexity complexity;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "completion_time")
    private Timestamp completionTime;

    @Column(name = "duration")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "po_number")
    private String PONumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projectItem", cascade = CascadeType.ALL)
    private Set<ProjectCommentItem> projectComments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projectItem", cascade = CascadeType.ALL)
    private Set<ProjectActivityItem> projectActivityItem;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projectItem", cascade = CascadeType.ALL)
    private Set<TaskItem> taskItems;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public UserItem getReporter()
    {
        return reporter;
    }

    public void setReporter(UserItem reporter)
    {
        this.reporter = reporter;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getWatchers()
    {
        return watchers;
    }

    public void setWatchers(String watchers)
    {
        this.watchers = watchers;
    }

    public Timestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Timestamp startTime)
    {
        this.startTime = startTime;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public ProjectStatus getStatus()
    {
        return status;
    }

    public void setStatus(ProjectStatus status)
    {
        this.status = status;
    }

    public Set<ProjectCommentItem> getProjectComments()
    {
        return projectComments;
    }

    public void setProjectComments(Set<ProjectCommentItem> projectComments)
    {
        this.projectComments = projectComments;
    }

    public Set<ProjectActivityItem> getProjectActivityItem()
    {
        return projectActivityItem;
    }

    public void setProjectActivityItem(Set<ProjectActivityItem> projectActivityItem)
    {
        this.projectActivityItem = projectActivityItem;
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

    public Timestamp getCompletionTime()
    {
        return completionTime;
    }

    public void setCompletionTime(Timestamp completionTime)
    {
        this.completionTime = completionTime;
    }

    public int getProjectId()
    {
        return projectId;
    }

    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }

    public ProjectType getType()
    {
        return type;
    }

    public void setType(ProjectType type)
    {
        this.type = type;
    }

    public ProjectPriority getPriority()
    {
        return priority;
    }

    public void setPriority(ProjectPriority priority)
    {
        this.priority = priority;
    }

    public ProjectComplexity getComplexity()
    {
        return complexity;
    }

    public void setComplexity(ProjectComplexity complexity)
    {
        this.complexity = complexity;
    }

    public Integer getClientId()
    {
        return clientId;
    }

    public void setClientId(Integer clientId)
    {
        this.clientId = clientId;
    }

    public Integer getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Integer accountId)
    {
        this.accountId = accountId;
    }

    public Integer getTeamId()
    {
        return teamId;
    }

    public void setTeamId(Integer teamId)
    {
        this.teamId = teamId;
    }

    public Set<TaskItem> getTaskItems()
    {
        return taskItems;
    }

    public void setTaskItems(Set<TaskItem> taskItems)
    {
        this.taskItems = taskItems;
    }

    public String getPONumber()
    {
        return PONumber;
    }

    public void setPONumber(String pONumber)
    {
        PONumber = pONumber;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

}
