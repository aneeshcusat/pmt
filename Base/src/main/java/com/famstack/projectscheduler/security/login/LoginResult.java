package com.famstack.projectscheduler.security.login;

import java.io.Serializable;

import com.famstack.projectscheduler.security.user.UserRole;

/**
 * The Enum LoginResult.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class LoginResult implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2913433474292241747L;

    /**
     * The Enum status.
     */
    public enum Status {
        /** The success. */
        SUCCESS,

        /** The failed. */
        FAILED,

        /** The new password required. */
        NEW_PASSWORD_REQUIRED,

        /** The temporary password expired. */
        TEMPORARY_PASSWORD_EXPIRED,

        /** The user account locked. */
        USER_ACCOUNT_LOCKED
    }

    /** The user role. */
    private UserRole userRole;

    /** The status. */
    private Status status = Status.FAILED;

    /** The user name. */
    private String userName;

    /** The club id. */
    private String clubId;

    /**
     * Gets the user role.
     *
     * @return the user role
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     * Sets the user role.
     *
     * @param userRole the new user role
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

}
