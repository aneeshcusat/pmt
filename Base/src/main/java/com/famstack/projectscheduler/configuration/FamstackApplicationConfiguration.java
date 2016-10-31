package com.famstack.projectscheduler.configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;

public class FamstackApplicationConfiguration extends BaseFamstackService {

	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	private String hostName;

	private int portNumber;

	private String protocol;

	private final boolean emailEnabled = true;

	public static Map<Integer, EmployeeDetails> userMap = new HashMap<>();

	private static Map<String, Integer> userIdMap = new HashMap<>();

	public void initialize() {

		logDebug("Initializing FamstackApplicationConfiguration...");
		initializeUserMap(famstackUserProfileManager.getEmployeeDataList());
	}

	private void initializeUserMap(List<EmployeeDetails> employeeDetailsList) {
		Map<Integer, EmployeeDetails> userMapTemp = new HashMap<>();
		Map<String, Integer> userIdMapTemp = new HashMap<>();

		for (EmployeeDetails employeeDetails : employeeDetailsList) {
			userMapTemp.put(employeeDetails.getId(), employeeDetails);
			userIdMapTemp.put(employeeDetails.getEmail(), employeeDetails.getId());
		}
		userMap.clear();
		userIdMap.clear();
		userMap.putAll(userMapTemp);
		userIdMap.putAll(userIdMapTemp);
	}

	public List<EmployeeDetails> getUserList() {
		return new ArrayList<EmployeeDetails>(userMap.values());
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public FamstackUserProfileManager getFamstackUserProfileManager() {
		return famstackUserProfileManager;
	}

	public boolean isEmailEnabled() {
		return emailEnabled;
	}

	public UserItem getCurrentUser() {
		return getFamstackUserSessionConfiguration().getCurrentUser();
	}

	public Map<Integer, EmployeeDetails> getUserMap() {
		return userMap;
	}

	public void updateLastPing() {
		int userId = getCurrentUserId();
		logDebug("updating user ping check" + userId);
		if (!userMap.isEmpty() && userMap.get(userId) != null) {
			userMap.get(userId).setLastPing(new Timestamp(new Date().getTime()));
			logDebug("updated user ping check" + userId);
		}
	}

	public String getUrl() {
		return protocol + "://" + hostName + ":" + portNumber + "/" + "bops/dashboard";
	}

	public int getCurrentUserId() {
		int userId = 0;
		if (getCurrentUser() != null) {
			userId = getCurrentUser().getId();
		}
		return userId;
	}

	public static Map<String, Integer> getUserIdMap() {
		return userIdMap;
	}

	public String getConfiguraionItem(String userTaskStatusRefresh) {
		// TODO Auto-generated method stub
		return null;
	}

}
