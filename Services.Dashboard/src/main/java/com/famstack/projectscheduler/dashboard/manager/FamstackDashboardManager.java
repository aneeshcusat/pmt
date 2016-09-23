package com.famstack.projectscheduler.dashboard.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.security.user.FamstackUserProfileManager;
import com.famstack.projectscheduler.util.StringUtils;

@Component
public class FamstackDashboardManager extends BaseFamstackService {

	@Resource
	FamstackDataAccessObjectManager famstackDataAccessObjectManager;

	@Resource
	FamstackUserProfileManager userProfileManager;

	public Map<String, Object> getUserData() {
		return null;
	}

	public void deleteUser(String userId) {

	}

	public Map<String, String> createUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<String, String>();
		/*Map<String, String> errorMap = valiateUser(employeeDetails);
		if (!errorMap.isEmpty()) {
			return errorMap;
		}
		UserItem userItem = userProfileManager.getUserItem(employeeDetails.getEmail());

		if (userItem != null) {
			errorMap.put("userExists", "user already exist in the system");
		}*/

		userProfileManager.createUserItem(employeeDetails);

		return errorMap;
	}

	private Map<String, String> valiateUser(EmployeeDetails employeeDetails) {
		Map<String, String> errorMap = new HashMap<>();
		if (!StringUtils.isNotBlank(employeeDetails.getFirstName())
				|| !StringUtils.isNotBlank(employeeDetails.getConfirmPassword())
				|| !StringUtils.isNotBlank(employeeDetails.getEmail())) {
			errorMap.put("invalidInput", "required inputs are missing");
		}

		String password = employeeDetails.getPassword();
		String confirmPassword = employeeDetails.getConfirmPassword();

		if (!password.equals(confirmPassword)) {
			errorMap.put("passwordMissmatch", "password missmatch");
		}

		return errorMap;
	}

	public List<UserItem> getUsersData() {
		return userProfileManager.getAllUserItems();
	}

	public UserItem getUser(int userId) {
		return userProfileManager.getUserItemById(userId);
	}

	public String getEmployeeDetails(int userId) {
		
		EmployeeDetails employeeDetails = userProfileManager.getEmployee(userId);
		String jsonString = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(employeeDetails);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public List<EmployeeDetails> getEmployeeDataList() {
		List<EmployeeDetails> employeeDetailsList = new ArrayList<EmployeeDetails>();
		List<UserItem> userItemList = getUsersData();
		if (userItemList != null) {
			Iterator<UserItem> iter = userItemList.iterator();
			while (iter.hasNext()) {
				UserItem userItem = iter.next();
				EmployeeDetails employeeDetails = userProfileManager.getEmployeeDetailsFromUserItem(userItem);
				if (employeeDetails != null) {
					employeeDetailsList.add(employeeDetails);
				}
			}
		}
		
		return employeeDetailsList;
	}

	public void deleteUser(int userId) {
		userProfileManager.deleteUserItem(userId);
		
	}
	
}
