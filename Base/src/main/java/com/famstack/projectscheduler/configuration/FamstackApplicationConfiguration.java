package com.famstack.projectscheduler.configuration;

import java.util.List;

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

	private List<EmployeeDetails> userList;

	public void initialize() {
		logDebug("Initializing FamstackApplicationConfiguration...");
		userList = famstackUserProfileManager.getEmployeeDataList();
	}

	public List<EmployeeDetails> getUserList() {
		return userList;
	}

	public void setUserList(List<EmployeeDetails> userList) {
		this.userList = userList;
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

}
