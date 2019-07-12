package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Date;

import com.famstack.projectscheduler.contants.LeaveType;
import com.famstack.projectscheduler.security.user.UserRole;

public class EmployeeDetails
{

    private static final int AWAY = 1;

    private static final int ONLINE = 5;

    private static final int OFFLINE = 0;

    private int id;

    private String userGroupId;

    private String firstName;

    private String lastName;

    private String email;

    private String empCode;

    private Boolean temporaryEmployee;
    
    private Boolean fundedEmployee;

    private Boolean needPasswordReset;

    private String password;

    private String confirmPassword;

    private String team;

    private String mobileNumber;

    private String filePhoto;

    private String gender;

    private String qualification;

    private String designation;

    private UserRole role;

    private String dateOfBirth;

    private int reportingManger;

    private Timestamp lastPing;

    private Date userAvailableTime;

    private LeaveType leave;

    private Integer userAccessCode;
    
    private boolean deleted;

    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getConfirmPassword()
    {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFilePhoto()
    {
        return filePhoto;
    }

    public void setFilePhoto(String filePhoto)
    {
        this.filePhoto = filePhoto;
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
     * @return the designation
     */
    public String getDesignation()
    {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(String designation)
    {
        this.designation = designation;
    }

    /**
     * @return the role
     */
    public UserRole getRole()
    {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(UserRole role)
    {
        this.role = role;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    public String getTeam()
    {
        return team;
    }

    public void setTeam(String team)
    {
        this.team = team;
    }

    public int getReportingManger()
    {
        return reportingManger;
    }

    public void setReportingManger(int reportingManger)
    {
        this.reportingManger = reportingManger;
    }

    public Timestamp getLastPing()
    {
        return lastPing;
    }

    public void setLastPing(Timestamp lastPing)
    {
        this.lastPing = lastPing;
    }

    public int getCheckUserStatus()
    {
        if (lastPing == null) {
            return OFFLINE;
        }
        long timeDiff = System.currentTimeMillis() - lastPing.getTime();

        long diffInMinutes = timeDiff / 1000 / 60;

        if (diffInMinutes < 2) {
            return ONLINE;
        } else if (diffInMinutes < 4) {
            return AWAY;
        }
        return OFFLINE;
    }

    public Date getUserAvailableTime()
    {
        return userAvailableTime;
    }

    public void setUserAvailableTime(Date userAvailableTime)
    {
        this.userAvailableTime = userAvailableTime;
    }

    public LeaveType isLeave()
    {
        return leave;
    }

    public void setLeave(LeaveType leave)
    {
        this.leave = leave;
    }

    public String getUserGroupId()
    {
        return userGroupId;
    }

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

    public String getEmpCode()
    {
        return empCode;
    }

    public void setEmpCode(String empCode)
    {
        this.empCode = empCode;
    }

    public Boolean getTemporaryEmployee()
    {
        return temporaryEmployee == null ? false : temporaryEmployee;
    }

    public void setTemporaryEmployee(Boolean temporaryEmployee)
    {
        this.temporaryEmployee = temporaryEmployee;
    }

    public Boolean getNeedPasswordReset()
    {
        return needPasswordReset == null ? false : needPasswordReset;
    }

    public void setNeedPasswordReset(Boolean needPasswordReset)
    {
        this.needPasswordReset = needPasswordReset;
    }

	public Boolean getFundedEmployee() {
		return fundedEmployee;
	}

	public void setFundedEmployee(Boolean fundedEmployee) {
		this.fundedEmployee = fundedEmployee;
	}

}
