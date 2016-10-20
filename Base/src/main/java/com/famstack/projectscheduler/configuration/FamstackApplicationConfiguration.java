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

	private static Map<Integer, EmployeeDetails> userMap = new HashMap<>();

	private static Map<String, Integer> userIdMap = new HashMap<>();

	public void initialize() {
		userMap.clear();
		userIdMap.clear();
		logDebug("Initializing FamstackApplicationConfiguration...");
		initializeUserMap(famstackUserProfileManager.getEmployeeDataList());
	}

	private void initializeUserMap(List<EmployeeDetails> employeeDetailsList) {
		for (EmployeeDetails employeeDetails : employeeDetailsList) {
			userMap.put(employeeDetails.getId(), employeeDetails);
			userIdMap.put(employeeDetails.getEmail(), employeeDetails.getId());
		}
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

	public void updateLastPing(int userId) {
		if (!userMap.isEmpty() && userMap.get(userId) != null) {
			userMap.get(userId).setLastPing(new Timestamp(new Date().getTime()));
		}
	}

	public String getUrl() {
		return protocol + "://" + hostName + ":" + portNumber;
	}

}
