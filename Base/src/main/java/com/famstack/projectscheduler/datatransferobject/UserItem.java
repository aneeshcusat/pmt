package com.famstack.projectscheduler.datatransferobject;

import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.famstack.projectscheduler.security.user.UserRole;

@Entity
@Table(name = "user_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
public class UserItem implements FamstackBaseItem
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -4647776200318098517L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private int id;

    @Column(name = "user_grp_id")
    private String userGroupId;

    /** The user id. */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /** The password. */
    @Column(name = "password")
    private String password;

    /** The hashkey. */
    @Column(name = "hashkey")
    private String hashkey;

    /** The user role. */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    UserRole userRole;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "user_team")
    private String team;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "designation")
    private String designation;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "profile_photo", columnDefinition = "LONGBLOB")
    private byte[] profilePhoto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userItem", cascade = CascadeType.ALL)
    private Set<UserActivityItem> userActivities;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @Column(name = "modified_by")
    private int modifiedBy;

    @Column(name = "need_password_reset")
    private Boolean needPasswordReset;

    @Column(name = "is_deleted", columnDefinition = "boolean default false", nullable = false)
    private Boolean deleted;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(name = "reporterting_manager")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "famstackEntityCache")
    private UserItem reportertingManager;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_subscribers", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "group_id", nullable = false, updatable = false)})
    private Set<GroupItem> groups;

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(int createdBy)
    {
        this.createdBy = createdBy;
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

    public int getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }

    public String getDesignation()
    {
        return designation;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setDesignation(String designation)
    {
        this.designation = designation;
    }

    /**
     * Gets the user id.
     * 
     * @return the user id
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Sets the user id.
     * 
     * @param userId the new user id
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Gets the hashkey.
     * 
     * @return the hashkey
     */
    public String getHashkey()
    {
        return hashkey;
    }

    /**
     * Sets the hashkey.
     * 
     * @param hashkey the new hashkey
     */
    public void setHashkey(String hashkey)
    {
        this.hashkey = hashkey;
    }

    /**
     * Gets the user role.
     * 
     * @return the user role
     */
    public UserRole getUserRole()
    {
        return userRole;
    }

    /**
     * Sets the user role.
     * 
     * @param userRole the new user role
     */
    public void setUserRole(UserRole userRole)
    {
        this.userRole = userRole;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }

    public byte[] getProfilePhoto()
    {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto)
    {
        this.profilePhoto = profilePhoto;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * @return the gender
     */
    public String getGender()
    {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    /**
     * @return the qualification
     */
    public String getQualification()
    {
        return qualification;
    }

    /**
     * @param qualification the qualification to set
     */
    public void setQualification(String qualification)
    {
        this.qualification = qualification;
    }

    /**
     * @return the dob
     */
    public Date getDob()
    {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob)
    {
        this.dob = dob;
    }

    public Set<UserActivityItem> getUserActivities()
    {
        return userActivities;
    }

    public void setUserActivities(Set<UserActivityItem> userActivities)
    {
        this.userActivities = userActivities;
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

    public String getTeam()
    {
        return team;
    }

    public void setTeam(String team)
    {
        this.team = team;
    }

    public UserItem getReportertingManager()
    {
        return reportertingManager;
    }

    public void setReportertingManager(UserItem reportertingManager)
    {
        this.reportertingManager = reportertingManager;
    }

    public Set<GroupItem> getGroups()
    {
        return groups;
    }

    public void setGroups(Set<GroupItem> groups)
    {
        this.groups = groups;
    }

    public Boolean getNeedPasswordReset()
    {

        return needPasswordReset == null ? false : needPasswordReset;
    }

    public void setNeedPasswordReset(Boolean needPasswordReset)
    {
        this.needPasswordReset = needPasswordReset;
    }

    public Boolean isDeleted()
    {

        return deleted == null ? false : deleted;
    }

    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
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

}
