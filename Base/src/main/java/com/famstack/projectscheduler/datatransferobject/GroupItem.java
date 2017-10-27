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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "group_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"group_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class GroupItem implements FamstackBaseItem
{

    private static final long serialVersionUID = -5628656638213113049L;

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int groupId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserItem createdBy;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_subscribers", joinColumns = {@JoinColumn(name = "group_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)})
    private Set<UserItem> subscribers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groupItem", cascade = CascadeType.ALL)
    private Set<GroupMessageItem> messages;

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
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

    public Set<UserItem> getSubscribers()
    {
        return subscribers;
    }

    public void setSubscribers(Set<UserItem> subscribers)
    {
        this.subscribers = subscribers;
    }

    public Set<GroupMessageItem> getMessages()
    {
        return messages;
    }

    public void setMessages(Set<GroupMessageItem> messages)
    {
        this.messages = messages;
    }

    public UserItem getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(UserItem createdBy)
    {
        this.createdBy = createdBy;
    }

}
