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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.famstack.projectscheduler.contants.LeaveType;

@Entity
@Table(name = "user_activity_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_act_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class UserActivityItem implements FamstackBaseItem
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 5145320086419804408L;

    @Id
    @Column(name = "user_act_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int id;

    @Column(name = "user_grp_id")
    private String userGroupId;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userActivityItem", cascade = CascadeType.ALL)
    private Set<UserTaskActivityItem> userTaskActivities;

    @Column(name = "calender_date")
    private Timestamp calenderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "leaType")
    private LeaveType leave;

    @Column(name = "billable_mins")
    private Integer billableMins;

    @Column(name = "non_billable_mins")
    private Integer nonBillableMins;

    @Column(name = "leave_mins")
    private Integer leaveMins;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private UserItem userItem;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public Set<UserTaskActivityItem> getUserTaskActivities()
    {
        return userTaskActivities;
    }

    public void setUserTaskActivities(Set<UserTaskActivityItem> userTaskActivities)
    {
        this.userTaskActivities = userTaskActivities;
    }

    public Timestamp getCalenderDate()
    {
        return calenderDate;
    }

    public void setCalenderDate(Timestamp calenderDate)
    {
        this.calenderDate = calenderDate;
    }

    public UserItem getUserItem()
    {
        return userItem;
    }

    public void setUserItem(UserItem userItem)
    {
        this.userItem = userItem;
    }

    public LeaveType getLeave()
    {
        return leave;
    }

    public void setLeave(LeaveType leave)
    {
        this.leave = leave;
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

    public Integer getBillableMins()
    {
        return billableMins == null ? 0 : billableMins;
    }

    public void setBillableMins(Integer billableMins)
    {
        this.billableMins = billableMins;
    }

    public Integer getNonBillableMins()
    {
        return nonBillableMins == null ? 0 : nonBillableMins;
    }

    public void setNonBillableMins(Integer nonBillableMins)
    {
        this.nonBillableMins = nonBillableMins;
    }

    public Integer getLeaveMins()
    {
        return leaveMins == null ? 0 : leaveMins;
    }

    public void setLeaveMins(Integer leaveMins)
    {
        this.leaveMins = leaveMins;
    }

}
