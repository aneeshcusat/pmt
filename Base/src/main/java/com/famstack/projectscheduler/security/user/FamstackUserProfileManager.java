package com.famstack.projectscheduler.security.user;

import java.io.ByteArrayInputStream;
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
		famstackDataAccessObjectManager.saveItem(userItem);
	}

	private byte[] getImageBytes(MultipartFile filePhoto) {
		byte[] photoFileBytes = new byte[(int) filePhoto.getSize()];

		try {
			ByteArrayInputStream fileInputStream = (ByteArrayInputStream) filePhoto.getInputStream();
			fileInputStream.read(photoFileBytes);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photoFileBytes;
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
}
