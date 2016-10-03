package com.famstack.projectscheduler.configuration;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;

public class FamstackApplicationConfiguration extends BaseFamstackService {

	@Resource
	FamstackUserProfileManager famstackUserProfileManager;

	private String hostName;

	private int portNumber;

	private String protocol;

	public Map<String, String> getConfigSettings() {
		return null;
	}

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
}
