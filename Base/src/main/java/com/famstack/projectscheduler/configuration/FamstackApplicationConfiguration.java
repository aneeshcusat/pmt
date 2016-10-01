package com.famstack.projectscheduler.configuration;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
@Component
public class FamstackApplicationConfiguration {

    private boolean initialized = false;
    
    private boolean userMapInitialized = false;
    
    @Resource
    FamstackDashboardManager famstackDashboardManager;

    private String hostName;

    private int portNumber;

    private String protocol;
    
	public Map<String, String> getConfigSettings() {
		return null;
	}
	
	private List<EmployeeDetails> userList;

	public void initUserMap() {
		userList = famstackDashboardManager.getEmployeeDataList();
	}
	
	public List<EmployeeDetails> getUserList() {
		return userList;
	}

	public void setUserList(List<EmployeeDetails> userList) {
		this.userList = userList;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isUserMapInitialized() {
		return userMapInitialized;
	}

	public void setUserMapInitialized(boolean userMapInitialized) {
		this.userMapInitialized = userMapInitialized;
	}
}
