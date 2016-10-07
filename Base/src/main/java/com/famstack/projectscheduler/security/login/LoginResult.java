package com.famstack.projectscheduler.security.login;

import java.io.Serializable;

import com.famstack.projectscheduler.datatransferobject.UserItem;

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

	/** The status. */
	private Status status = Status.FAILED;

	/** The club id. */
	private String clubId;

	private String hashKey;

	private UserItem userItem;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getClubId() {
		return clubId;
	}

	public void setClubId(String clubId) {
		this.clubId = clubId;
	}

	public UserItem getUserItem() {
		return userItem;
	}

	public void setUserItem(UserItem userItem) {
		this.userItem = userItem;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

}
