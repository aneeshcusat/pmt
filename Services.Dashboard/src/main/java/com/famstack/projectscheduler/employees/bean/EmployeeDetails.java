package com.famstack.projectscheduler.employees.bean;

import java.sql.Timestamp;
import java.util.Date;

import com.famstack.projectscheduler.contants.LeaveType;
import com.famstack.projectscheduler.security.user.UserRole;

public class EmployeeDetails implements Comparable<EmployeeDetails> {

    private static final int AWAY = 1;

    private static final int ONLINE = 5;

    private static final int OFFLINE = 0;

    private int id;

    private String userGroupId;

    private String firstName;

    private String lastName;

    private String email;

    private String empCode;

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

    private Timestamp lastPing;

    private Date userAvailableTime;

    private LeaveType leave;

    private Integer userAccessCode;
    
    private boolean deleted;
    
    private String division;
    
    private String country;
    
    private String department;
    
    private String subDepartment;
    
    private String location;
    
    private String band;
    
    private String grade;

    private String dateOfJoin;
    
    private String exitDate;
    
    private String empType;
    
    private String reportertingManagerEmailId;
    
    private String deptLeadEmailId;
    
    private String lobHeadEmailId;

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

	@Override
	public int compareTo(EmployeeDetails employeeDetails) {
		if (firstName != null) {
			return firstName.compareTo(employeeDetails.firstName);
		} 
		return 0;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getDateOfJoin() {
		return dateOfJoin;
	}

	public String getExitDate() {
		return exitDate;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getReportertingManagerEmailId() {
		return reportertingManagerEmailId;
	}

	public void setReportertingManagerEmailId(String reportertingManagerEmailId) {
		this.reportertingManagerEmailId = reportertingManagerEmailId;
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

	public void setDateOfJoin(String dateOfJoin) {
		this.dateOfJoin = dateOfJoin;
	}

	public void setExitDate(String exitDate) {
		this.exitDate = exitDate;
	}

}
