package com.famstack.projectscheduler.security.user;

import java.io.FileInputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.security.FamstackAuthenticationToken;
import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;
import com.famstack.projectscheduler.security.hasher.generator.PasswordTokenGenerator;
import com.famstack.projectscheduler.security.login.LoginResult;
import com.famstack.projectscheduler.security.login.LoginResult.Status;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * The Class UserProfileManager.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
public class FamstackUserProfileManager extends BaseFamstackService {

	/** The delivery interface data access object manager. */
	@Resource
	FamstackDataAccessObjectManager famstackDataAccessObjectManager;

	@Resource
	PasswordTokenGenerator passwordTokenGenerator;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Login.
	 *
	 * @param name
	 *            the name
	 * @param password
	 *            the password
	 * @return the login result
	 */
	public LoginResult login(String name, String password) {
		LoginResult loginResult = new LoginResult();
		UserItem userItem = getUserItem(name);
		if (userItem != null) {
			String encryptPassword = FamstackSecurityTokenManager.encryptString(password, userItem.getHashkey());
			if (userItem.getPassword().equals(encryptPassword)) {
				loginResult.setStatus(Status.SUCCESS);
				loginResult.setUserRole(userItem.getUserRole());
				loginResult.setUserName(name);
				return loginResult;
			}
		}
		loginResult.setStatus(Status.FAILED);
		return loginResult;

	}

	/**
	 * Gets the user token.
	 *
	 * @return the user token
	 */
	public Authentication getUserToken() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * Gets the login result.
	 *
	 * @return the login result
	 */
	public LoginResult getLoginResult() {
		Authentication authentication = getUserToken();
		if (authentication instanceof FamstackAuthenticationToken) {
			return ((FamstackAuthenticationToken) authentication).getLoginResult();
		}
		return null;
	}

	public void createUserItem(EmployeeDetails employeeDetails) {
		String hashKey = passwordTokenGenerator.generate(32);
		String password = employeeDetails.getPassword();
		UserItem userItem = new UserItem();
		userItem.setUserId(employeeDetails.getEmail());
		userItem.setPassword(FamstackSecurityTokenManager.encryptString(password, hashKey));
		userItem.setHashkey(hashKey);
		byte[] imageBytes = getImageBytes(employeeDetails.getFilePhoto());
		userItem.setProfilePhoto(imageBytes);
		
		userItem.setLastName(employeeDetails.getLastName());
		userItem.setDesignation(employeeDetails.getDesignation());
		if (StringUtils.isNotBlank(employeeDetails.getDateOfBirth())) {
			try {
				userItem.setDob(new Date(sdf.parse(employeeDetails.getDateOfBirth()).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		userItem.setFirstName(employeeDetails.getFirstName());
		userItem.setMobileNumber(employeeDetails.getMobileNumber());
		userItem.setQualification(employeeDetails.getQualification());
		userItem.setUserRole(employeeDetails.getRole());
		userItem.setGender(employeeDetails.getGender());
		userItem.setGroup(employeeDetails.getGroup());
		
		if (employeeDetails.getId() == 0) {
			famstackDataAccessObjectManager.saveItem(userItem);
		} else {
			userItem.setId(employeeDetails.getId());
			famstackDataAccessObjectManager.updateItem(userItem);
		}
		
	}

	private byte[] getImageBytes(String filePhoto) {
		if (filePhoto == null) {
			return null;
		}
		return filePhoto.getBytes();
	}

	public List<UserItem> getAllUserItems() {
		return famstackDataAccessObjectManager.getAllUsers();
	}

	public UserItem getUserItemById(int id) {
		return famstackDataAccessObjectManager.getUserById(id);
	}

	public UserItem getUserItem(String userId) {
		return famstackDataAccessObjectManager.getUser(userId);
	}
	
	public EmployeeDetails getEmployee(int userId) {
		UserItem userItem = getUserItemById(userId);
		return getEmployeeDetailsFromUserItem(userItem);
	}
	
	public EmployeeDetails getEmployeeDetailsFromUserItem(UserItem userItem) {
		if (userItem != null) {
			EmployeeDetails employeeDetails = new EmployeeDetails();
			if (userItem.getDob() != null) {
				employeeDetails.setDateOfBirth(sdf.format(userItem.getDob()));
			}
			employeeDetails.setDesignation(userItem.getDesignation());
			employeeDetails.setEmail(userItem.getUserId());
			employeeDetails.setFirstName(userItem.getFirstName());
			employeeDetails.setGender(userItem.getGender());
			employeeDetails.setGroup(userItem.getGroup());
			employeeDetails.setLastName(userItem.getLastName());
			employeeDetails.setMobileNumber(userItem.getMobileNumber());
			employeeDetails.setQualification(userItem.getQualification());
			employeeDetails.setRole(userItem.getUserRole());
			employeeDetails.setId(userItem.getId());
			if (userItem.getProfilePhoto() != null) {
				employeeDetails.setFilePhoto(new String(userItem.getProfilePhoto())); 
			}
			return employeeDetails;
		}
		return null;
		
	}

	public void deleteUserItem(int userId) {
		UserItem userItem = getUserItemById(userId);
		if (userItem != null) {
			famstackDataAccessObjectManager.deleteItem(userItem);
		} 
	}
}
