package com.famstack.projectscheduler.dashboard.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;

@Component
public class FamstackDashboardManager extends BaseFamstackService {
	
	@Resource
	FamstackDataAccessObjectManager famstackDataAccessObjectManager;
   
    public Map<String, Object> getUserData() {
        return null;
    }

    public void deleteUser(String userId) {

    }

    public void createUser(String userId, String oldUserName, String userRole, String password, String clubId) {
     
    }
}
