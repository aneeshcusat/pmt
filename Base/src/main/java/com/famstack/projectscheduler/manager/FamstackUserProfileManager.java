package com.famstack.projectscheduler.manager;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
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
public class FamstackUserProfileManager extends BaseFamstackManager {

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
				loginResult.setUserItem(userItem);
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
		String password = passwordTokenGenerator.generate(8);
		UserItem userItem = new UserItem();
		String encryptedPassword = FamstackSecurityTokenManager.encryptString(password, hashKey);
		userItem.setHashkey(hashKey);
		userItem.setPassword(encryptedPassword);
		saveUserItem(employeeDetails, userItem);
	}

	public void updateUserItem(EmployeeDetails employeeDetails) {
		UserItem userItem = getUserItem(employeeDetails.getEmail());
		if (userItem != null) {
			saveUserItem(employeeDetails, userItem);
		}
	}

	private void saveUserItem(EmployeeDetails employeeDetails, UserItem userItem) {
		userItem.setUserId(employeeDetails.getEmail());

		byte[] imageBytes = getImageBytes(employeeDetails.getFilePhoto());
		if (imageBytes.length > 0) {
			userItem.setProfilePhoto(imageBytes);
		}

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
		getFamstackDataAccessObjectManager().saveOrUpdateItem(userItem);
	}

	private byte[] getImageBytes(String filePhoto) {
		if (filePhoto == null) {
			return null;
		}
		return filePhoto.getBytes();
	}

	public List<?> getAllUserItems() {
		return getFamstackDataAccessObjectManager().getAllItems("UserItem");
	}

	public UserItem getUserItemById(int id) {
		return (UserItem) getFamstackDataAccessObjectManager().getItemById(id, UserItem.class);
	}

	public UserItem getUserItem(String userId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("id", userId);
		List<?> userItemList = getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString("FamstackQueryStringsusersByUserId"), dataMap);
		if (!userItemList.isEmpty()) {
			return (UserItem) userItemList.get(0);
		}
		return null;
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
			getFamstackDataAccessObjectManager().deleteItem(userItem);
		}
	}
}
