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

    @Column(name = "emp_code")
    private String empCode;

    @Column(name = "is_funded")
    private Boolean fundedEmployee;

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

    @Column(name = "is_deleted")
    private Boolean deleted;
    
    @Column(name = "division")
    private String division;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "sub_department")
    private String subDepartment;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "band")
    private String band;
    
    @Column(name = "grade")
    private String grade;

    @Column(name = "date_of_join")
    private Date dateOfJoin;
    
    @Column(name = "exit_date")
    private Date exitDate;
    
    @Column(name = "emp_type")
    private String empType;
    
    @Column(name = "rept_mgr_email_id")
    private String reportertingManagerEmailId;
    
    @Column(name = "dept_lead_email_id")
    private String deptLeadEmailId;
    
    @Column(name = "lob_head_email_id")
    private String lobHeadEmailId;
    
    @Column(name = "skills", columnDefinition="LONGTEXT")
    private String skills;
    
    @Column(name = "additional_skills", columnDefinition="LONGTEXT")
    private String additionalSkills;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_subscribers", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "group_id", nullable = false, updatable = false)})
    private Set<GroupItem> groups;

    @Column(name = "user_access_code")
    private Integer userAccessCode;
    
    @Column(name = "login_attempt_count")
    private Integer loginAttemptCount;

    @Column(name = "account_locked")
    private Boolean accountLocked = false;

    
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

    public Integer getUserAccessCode()
    {
        return userAccessCode;
    }

    public void setUserAccessCode(Integer userAccessCode)
    {
        this.userAccessCode = userAccessCode;
    }

    public Boolean getDeleted()
    {
        return deleted ==  null ? false : deleted;
    }

    public String getEmpCode()
    {
        return empCode;
    }

    public void setEmpCode(String empCode)
    {
        this.empCode = empCode;
    }

	public Boolean getFundedEmployee() {
		return fundedEmployee;
	}

	public void setFundedEmployee(Boolean fundedEmployee) {
		this.fundedEmployee = fundedEmployee;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDateOfJoin() {
		return dateOfJoin;
	}

	public void setDateOfJoin(Date dateOfJoin) {
		this.dateOfJoin = dateOfJoin;
	}

	public String getReportertingManagerEmailId() {
		return reportertingManagerEmailId;
	}

	public void setReportertingManagerEmailId(String reportertingManagerEmailId) {
		this.reportertingManagerEmailId = reportertingManagerEmailId;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(String subDepartment) {
		this.subDepartment = subDepartment;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getDeptLeadEmailId() {
		return deptLeadEmailId;
	}

	public void setDeptLeadEmailId(String deptLeadEmailId) {
		this.deptLeadEmailId = deptLeadEmailId;
	}

	public String getLobHeadEmailId() {
		return lobHeadEmailId;
	}

	public void setLobHeadEmailId(String lobHeadEmailId) {
		this.lobHeadEmailId = lobHeadEmailId;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public Integer getLoginAttemptCount() {
		return loginAttemptCount == null ? 0 : loginAttemptCount;
	}

	public void setLoginAttemptCount(Integer loginAttemptCount) {
		this.loginAttemptCount = loginAttemptCount;
	}

	public Boolean isAccountLocked() {
		return accountLocked == null ? false : accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public String getAdditionalSkills() {
		return additionalSkills;
	}

	public void setAdditionalSkills(String additionalSkills) {
		this.additionalSkills = additionalSkills;
	}

}
