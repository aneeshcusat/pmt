package com.famstack.projectscheduler.datatransferobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.famstack.projectscheduler.security.user.UserRole;

@Entity
@Table(name = "user_info", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id" }) })
public class UserItem {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private int id;

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

	@Column(name = "designation")
	private String designation;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "profile_photo", columnDefinition = "blob")
	private byte[] profilePhoto;

	public String getDesignation() {
		return designation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the hashkey.
	 *
	 * @return the hashkey
	 */
	public String getHashkey() {
		return hashkey;
	}

	/**
	 * Sets the hashkey.
	 *
	 * @param hashkey
	 *            the new hashkey
	 */
	public void setHashkey(String hashkey) {
		this.hashkey = hashkey;
	}

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
	 * @param userRole
	 *            the new user role
	 */
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public byte[] getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(byte[] profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
