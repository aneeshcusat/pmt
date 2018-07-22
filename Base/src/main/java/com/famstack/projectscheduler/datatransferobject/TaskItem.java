package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.famstack.projectscheduler.contants.ProjectPriority;
import com.famstack.projectscheduler.contants.ProjectTaskType;
import com.famstack.projectscheduler.contants.TaskStatus;

@Entity
@Table(name = "task_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"task_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class TaskItem implements FamstackBaseItem
{

    private static final long serialVersionUID = -5628656638213113049L;

    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int taskId;

    @Column(name = "name")
    private String name;

    @Column(name = "user_grp_id")
    private String userGroupId;

    @Column(name = "assignee")
    private int assignee;

    @Column(name = "helpers")
    private String helpers;

    @Column(name = "contributers")
    private String contributers;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @Column(name = "task_paused_time")
    private Timestamp taskPausedTime;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(name = "reporter")
    private UserItem reporter;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ProjectPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "prj_task_type")
    private ProjectTaskType projectTaskType;

    @Column(name = "can_recure")
    private Boolean canRecure;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "completion_time")
    private Timestamp completionTime;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "actual_time_taken")
    private Integer actualTimeTaken;

    @Column(name = "task_remaining_time")
    private Integer taskRemainingTime;

    @Column(name = "extra_time_task")
    private Boolean extraTimeTask;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectItem projectItem;

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public Timestamp getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Timestamp lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public UserItem getReporter()
    {
        return reporter;
    }

    public void setReporter(UserItem reporter)
    {
        this.reporter = reporter;
    }

    public ProjectPriority getPriority()
    {
        return priority;
    }

    public void setPriority(ProjectPriority priority)
    {
        this.priority = priority;
    }

    public Timestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Timestamp startTime)
    {
        this.startTime = startTime;
    }

    public Timestamp getCompletionTime()
    {
        return completionTime;
    }

    public void setCompletionTime(Timestamp completionTime)
    {
        this.completionTime = completionTime;
    }

    public Integer getDuration()
    {
        return duration != null && duration < 0 ? 0 : duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public TaskStatus getStatus()
    {
        return status;
    }

    public void setStatus(TaskStatus status)
    {
        this.status = status;
    }

    public ProjectItem getProjectItem()
    {
        return projectItem;
    }

    public void setProjectItem(ProjectItem projectItem)
    {
        this.projectItem = projectItem;
    }

    public int getAssignee()
    {
        return assignee;
    }

    public void setAssignee(int assignee)
    {
        this.assignee = assignee;
    }

    public String getHelpers()
    {
        return helpers;
    }

    public void setHelpers(String helpers)
    {
        this.helpers = helpers;
    }

    public Boolean getExtraTimeTask()
    {
        return extraTimeTask == null ? false : extraTimeTask;
    }

    public void setExtraTimeTask(Boolean extraTimeTask)
    {
        this.extraTimeTask = extraTimeTask;
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

    public Integer getActualTimeTaken()
    {
        return actualTimeTaken == null || actualTimeTaken < 0 ? 0 : actualTimeTaken;
    }

    public void setActualTimeTaken(Integer actualTimeTaken)
    {
        this.actualTimeTaken = actualTimeTaken;
    }

    public Integer getTaskRemainingTime()
    {
        return taskRemainingTime == null || taskRemainingTime < 0 ? 0 : taskRemainingTime;
    }

    public void setTaskRemainingTime(Integer taskRemainingTime)
    {
        this.taskRemainingTime = taskRemainingTime;
    }

    public Timestamp getTaskPausedTime()
    {
        return taskPausedTime;
    }

    public void setTaskPausedTime(Timestamp taskPausedTime)
    {
        this.taskPausedTime = taskPausedTime;
    }

    public ProjectTaskType getProjectTaskType()
    {
        return projectTaskType == null ? ProjectTaskType.PRODUCTIVE : projectTaskType;
    }

    public void setProjectTaskType(ProjectTaskType projectTaskType)
    {
        this.projectTaskType = projectTaskType;
    }

    public String getContributers()
    {
        return contributers;
    }

    public void setContributers(String contributers)
    {
        this.contributers = contributers;
    }

    public Boolean getCanRecure()
    {
        return canRecure == null ? true : canRecure;
    }

    public void setCanRecure(Boolean canRecure)
    {
        this.canRecure = canRecure;
    }

}
