package com.famstack.projectscheduler.configuration;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ConfigurationSettingsItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AppConfDetails;
import com.famstack.projectscheduler.employees.bean.AppConfValueDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.UserGroupDetails;
import com.famstack.projectscheduler.manager.FamstackApplicationConfManager;
import com.famstack.projectscheduler.manager.FamstackRemoteServiceRefreshManager;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

public class FamstackApplicationConfiguration extends BaseFamstackService
{

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    @Resource
    FamstackApplicationConfManager famstackApplicationConfManager;

    @Resource
    FamstackDataAccessObjectManager famstackDataAccessObjectManager;
    
    @Resource
    FamstackRemoteServiceRefreshManager famstackRemoteServiceRefreshManager;

    private String hostName;

    private int portNumber;

    private String protocol;
    
    private String staticFilesLocation;
    
    private String instanceName;
    
    private boolean tracopusConfigEnabled;
    
    private String fsVersionNumber;

    private boolean staticReportEnabled;

    public static Map<Integer, EmployeeDetails> userMap = new HashMap<>();
    
    public static Map<Integer, EmployeeDetails> allUsersMap = new HashMap<>();

    private static Map<String, UserGroupDetails> userGroupMap = new HashMap<>();

    private static Map<String, AppConfDetails> appConfigMap = new HashMap<>();

    private static Map<String, Integer> userIdMap = new HashMap<>();

    private final Map<String, String> configSettings = new HashMap<>();

    public void forceInitialize()
    {
        appConfigMap.clear();
        userGroupMap.clear();
        configSettings.clear();

        forceInitializeUserMap(famstackUserProfileManager.getAllEmployeeDataList());
        initializeUserGroupMap(famstackApplicationConfManager.getUserGroupList());
        forceInitializeAppConfigMap(famstackApplicationConfManager.getAppConfigList());
        initializeConfigurations();
    }
    
    public void forceInitializeAppConfigMap(List<AppConfDetails> appConfigList)
    {
    	reInitializeAppConfigMap(appConfigList);
    	famstackRemoteServiceRefreshManager.createOrUpdateRemoteRefreshItem(getFamstackApplicationConfiguration().getInstanceName(), "application", false);
    }

    public void reInitializeAppConfigMap(List<AppConfDetails> appConfigList)
    {
        logInfo("Re Initializing app config value" + appConfigList);
        for (AppConfDetails appConfDetails : appConfigList) {
            appConfigMap.put(appConfDetails.getType(), appConfDetails);
        }
        famstackRemoteServiceRefreshManager.createOrUpdateRemoteRefreshItem(getFamstackApplicationConfiguration().getInstanceName(), "application", true);
    }

    public void initializeUserGroupMap(List<UserGroupDetails> userGroupList)
    {
        if (userGroupMap.isEmpty()) {
            forceInitializeUserGroup(userGroupList);
        }
    }

    public void forceInitializeUserGroup(List<UserGroupDetails> userGroupList)
    {
        for (UserGroupDetails userGroupDetail : userGroupList) {
            userGroupMap.put(userGroupDetail.getUserGroupId(), userGroupDetail);
        }
    }

    public synchronized void forceInitializeUserMap(List<EmployeeDetails> employeeDetailsList)
    {
    	initializeUserMap(employeeDetailsList);
    	famstackRemoteServiceRefreshManager.createOrUpdateRemoteRefreshItem(getFamstackApplicationConfiguration().getInstanceName(), "user", false);
    }
    public synchronized void initializeUserMap(List<EmployeeDetails> employeeDetailsList)
    {
        Map<Integer, EmployeeDetails> userMapTemp = new HashMap<>();
        Map<Integer, EmployeeDetails> allUserMapTemp = new HashMap<>();
        Map<String, Integer> userIdMapTemp = new HashMap<>();

        for (EmployeeDetails employeeDetails : employeeDetailsList) {
        	allUserMapTemp.put(employeeDetails.getId(), employeeDetails);
        	if (!employeeDetails.isDeleted()) {
        		userMapTemp.put(employeeDetails.getId(), employeeDetails);
        	}
            userIdMapTemp.put(employeeDetails.getEmail(), employeeDetails.getId());
        }
        allUsersMap.clear();
        userMap.clear();
        userIdMap.clear();
        userMap.putAll(userMapTemp);
        userIdMap.putAll(userIdMapTemp);
        allUsersMap.putAll(allUserMapTemp);
        
        famstackRemoteServiceRefreshManager.createOrUpdateRemoteRefreshItem(getFamstackApplicationConfiguration().getInstanceName(), "user", true);
    }
    
    /**
     * Initialize configurations.
     */
    public void initializeConfigurations()
    {
        List<ConfigurationSettingsItem> configurationSettingsItemsList =
            (List<ConfigurationSettingsItem>) famstackDataAccessObjectManager
                .getAllGroupItems("ConfigurationSettingsItem");
        if (configurationSettingsItemsList != null) {
            for (ConfigurationSettingsItem configurationSettingsItem : configurationSettingsItemsList) {
                updatConfiguraionIteme(configurationSettingsItem);
            }
        }
    }

    public void updatConfiguraionIteme(ConfigurationSettingsItem configurationSettingsItem)
    {
        logDebug("intizlized  configurationSettingsItem : " + configurationSettingsItem.getPropertyName() + " ,value :"
            + configurationSettingsItem.getPropertyValue());
        configSettings.put(configurationSettingsItem.getPropertyName(), configurationSettingsItem.getPropertyValue());
    }

    public List<EmployeeDetails> getAllUserList()
    {
        List<EmployeeDetails> userList = new ArrayList<>();
        if (userMap != null) {
            for (Integer userId : userMap.keySet()) {
                userList.add(userMap.get(userId));
            }
        }
        sortUserList(userList);
        return userList;
    }

    public List<EmployeeDetails> getUserList()
    {
        List<EmployeeDetails> userList = new ArrayList<>();
        if (userMap != null) {
            for (Integer userId : userMap.keySet()) {

                if (getFamstackUserSessionConfiguration().getUserGroupId().equalsIgnoreCase(
                    userMap.get(userId).getUserGroupId())) {
                    userList.add(userMap.get(userId));
                }
            }
        }
        sortUserList(userList);
        return userList;
    }
    
    public List<EmployeeDetails> getUserList(String groupId)
    {
        List<EmployeeDetails> userList = new ArrayList<>();
        if (userMap != null) {
            for (Integer userId : userMap.keySet()) {

                if (groupId.equalsIgnoreCase(
                    userMap.get(userId).getUserGroupId())) {
                    userList.add(userMap.get(userId));
                }
            }
        }
        sortUserList(userList);
        return userList;
    }

    private void sortUserList(List<EmployeeDetails> userList)
    {
        Collections.sort(userList, new Comparator<EmployeeDetails>()
        {
            @Override
            public int compare(EmployeeDetails employeeDetails1, EmployeeDetails employeeDetails2)
            {
                return employeeDetails1.getFirstName().toUpperCase()
                    .compareTo(employeeDetails2.getFirstName().toUpperCase());
            }
        });
    }

    public Map<Integer, EmployeeDetails> getFilterdUserMap(String groupId)
    {
    	if (!StringUtils.isNotBlank(groupId)){
    		groupId = getFamstackUserSessionConfiguration().getUserGroupId();
    	}
    	
        Map<Integer, EmployeeDetails> userFiltedMap = new HashMap<>();
        if (userMap != null) {
            for (Integer userId : userMap.keySet()) {
                if (groupId.equalsIgnoreCase(
                    userMap.get(userId).getUserGroupId())) {
                    userFiltedMap.put(userId, userMap.get(userId));
                }
            }
        }
        return userFiltedMap;
    }

    public Map<String, AppConfDetails> getFilterdApplicationConfigMap()
    {
        Map<String, AppConfDetails> appConfigFilterdMap = new HashMap<>();
        if (appConfigMap != null) {
            for (String appConfigType : appConfigMap.keySet()) {
                if (getFamstackUserSessionConfiguration().getUserGroupId().equalsIgnoreCase(
                    appConfigMap.get(appConfigType).getUserGroupId())) {
                    appConfigFilterdMap.put(appConfigType, appConfigMap.get(appConfigType));
                }
            }
        }
        return appConfigFilterdMap;
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public int getPortNumber()
    {
        return portNumber;
    }

    public void setPortNumber(int portNumber)
    {
        this.portNumber = portNumber;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public FamstackUserProfileManager getFamstackUserProfileManager()
    {
        return famstackUserProfileManager;
    }

    public UserItem getCurrentUser()
    {
        return getFamstackUserSessionConfiguration().getCurrentUser();
    }

    public Boolean getUserAccessedSystemToday()
    {
        Date userLastActivityDate = getFamstackUserSessionConfiguration().getUserLastActivityDate();
        return userLastActivityDate == null || !DateUtils.isTodayDate(userLastActivityDate);
    }

    public String getCurrentUserGroupId()
    {
        return getFamstackUserSessionConfiguration().getUserGroupId();
    }

    public Map<Integer, EmployeeDetails> getUserMap()
    {
        return userMap;
    }

    public Map<Integer, EmployeeDetails> getAllUsersMap()
    {
        return allUsersMap;
    }
    
    public List<EmployeeDetails> getAllSortedUsers()
    {
    	List<EmployeeDetails> employeeDetails = new ArrayList<>(allUsersMap.values());
        Collections.sort(employeeDetails);
        return employeeDetails;
    }

    
    public Map<Integer, EmployeeDetails> getCurrentGroupUserMap()
    {
        Map<Integer, EmployeeDetails> currentUserGroup = new HashMap<>();
        if (userMap != null) {
            for (Integer userId : userMap.keySet()) {
                if (getFamstackUserSessionConfiguration().getUserGroupId().equalsIgnoreCase(
                    userMap.get(userId).getUserGroupId())) {
                    currentUserGroup.put(userId, userMap.get(userId));
                }
            }
        }
        return currentUserGroup;
    }

    public Map<String, UserGroupDetails> getUserGroupMap()
    {
        return userGroupMap;
    }

    public void updateLastPing()
    {
        int userId = getCurrentUserId();
        logDebug("updating user ping check" + userId);
        if (!userMap.isEmpty() && userMap.get(userId) != null) {
            userMap.get(userId).setLastPing(new Timestamp(new Date().getTime()));
        }
    }

    public String getUrl()
    {
        return protocol + "://" + hostName + ":" + portNumber + "/" + "bops/dashboard";
    }

    public int getCurrentUserId()
    {
        int userId = 0;
        if (getCurrentUser() != null) {
            userId = getCurrentUser().getId();
        }
        return userId;
    }

    public static Map<String, Integer> getUserIdMap()
    {
        return userIdMap;
    }

    /**
     * Checks if is log debug.
     * 
     * @return true, if is log debug
     */
    public boolean isLogDebug()
    {
        if (configSettings.get("logDebug") == null) {
            return true;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get("logDebug")) ? true : false;
    }

    /**
     * Checks if is log debug.
     * 
     * @return true, if is log debug
     */
    public boolean isAutoRefresh()
    {
        if (configSettings.get("autoRefresh") == null) {
            return true;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get("autoRefresh")) ? true : false;
    }

    /**
     * Checks if is log debug.
     * 
     * @return true, if is log debug
     */
    public boolean isEmailDisabled(String userId)
    {
        String isEmailDisabledId = userId + "_disableEmail";

        if (configSettings.get(isEmailDisabledId) == null) {
            return false;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get(isEmailDisabledId)) ? true : false;
    }

    public boolean isEmailUserDisabled()
    {
        return isEmailDisabled(getCurrentUser().getUserId());
    }

    public boolean isEmailAllEnabled()
    {
        String isEmailEnabledId = "enableEmail";

        if (configSettings.get(isEmailEnabledId) == null) {
            return true;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get(isEmailEnabledId)) ? true : false;
    }

    public String getConfiguraionItem(String scheduleCron)
    {
        if (configSettings.get("scheduleCron") == null) {
            return "";
        }

        return configSettings.get("scheduleCron");
    }

    public boolean isDesktopNotificationEnabled()
    {
        if (configSettings.get("desktopNotification") == null) {
            return true;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get("desktopNotification")) ? true : false;
    }

    public boolean isExpandedPage()
    {
        String toggelId = getCurrentUserId() + "_dashboardToggle";
        if (configSettings.get(toggelId) == null) {
            return false;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get(toggelId)) ? true : false;
    }

    public String getDefaultDate()
    {
        String defaultDateId = getCurrentUserId() + "_filterDate";
        String defaultDateLabel = configSettings.get(defaultDateId);
        logDebug("deraultFilterDate : " + defaultDateLabel);
        return defaultDateLabel == null ? "This Week" : defaultDateLabel;
    }

    public Map<String, AppConfDetails> getAppConfigMap()
    {
        return appConfigMap;
    }

    public List<AppConfValueDetails> getProjectCategories()
    {
        return getCategories("projectCategory");
    }
    
    public List<AppConfValueDetails> getNewProjectCategories()
    {
        return getCategories("newProjectCategory");
    }

    public List<AppConfValueDetails> getNonBillableCategories()
    {
        return getCategories("nonBillableCategory");
    }

    public List<AppConfValueDetails> getCategories(String type)
    {
        List<AppConfValueDetails> appConfValueDetails = new ArrayList<>();
        String projectCategoryId = type + getCurrentUserGroupId();
        if (appConfigMap != null) {
            AppConfDetails appConfigDetails = appConfigMap.get(projectCategoryId);

            if (appConfigDetails != null && appConfigDetails.getAppConfValueDetails() != null) {
                appConfValueDetails.addAll(appConfigDetails.getAppConfValueDetails());

                Collections.sort(appConfValueDetails, new Comparator<AppConfValueDetails>()
                {
                    @Override
                    public int compare(AppConfValueDetails appConfValueDetails1,
                        AppConfValueDetails appConfValueDetails2)
                    {
                        return appConfValueDetails1.getName().toUpperCase()
                            .compareTo(appConfValueDetails2.getName().toUpperCase());
                    }
                });
            }
        }
        return appConfValueDetails;
    }
    
    public String getInstanceName(){
    	
    	if (instanceName == null) {
			try {
		        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
		        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
		                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		        String host = InetAddress.getLocalHost().getHostAddress();
		        String port = objectNames.iterator().next().getKeyProperty("port");
		        instanceName = host + ":" + port;
			} catch (Exception e) {
				return "unknown";
			}
    	}
    	return instanceName;
    }

    public String getAppConfig(String type, String userGroupId)
    {
        List<AppConfValueDetails> appConfValueDetails = new ArrayList<>();
        String appConfigType = type + userGroupId;
        if (appConfigMap != null) {
            AppConfDetails appConfigDetails = appConfigMap.get(appConfigType);

            if (appConfigDetails != null && appConfigDetails.getAppConfValueDetails() != null) {
                appConfValueDetails.addAll(appConfigDetails.getAppConfValueDetails());
               return appConfValueDetails.get(0).getValue();
            }
        }
        return null;
    }

	public boolean isRecurringByCode(String userGroupId) {
		String value = getAppConfig("projectRecurringByCode", userGroupId);
		if( value != null && "enabled".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}
	
    
	public String getStaticFilesLocation() {
		return staticFilesLocation;
	}

	public void setStaticFilesLocation(String staticFilesLocation) {
		this.staticFilesLocation = staticFilesLocation;
	}

	public boolean isTracopusConfigEnabled() {
		return tracopusConfigEnabled;
	}

	public void setTracopusConfigEnabled(boolean tracopusConfigEnabled) {
		this.tracopusConfigEnabled = tracopusConfigEnabled;
	}

	public String getFsVersionNumber() {
		return fsVersionNumber;
	}

	public void setFsVersionNumber(String fsVersionNumber) {
		this.fsVersionNumber = fsVersionNumber;
	}

	public boolean isStaticReportEnabled() {
		return staticReportEnabled;
	}

	public void setStaticReportEnabled(boolean staticReportEnabled) {
		this.staticReportEnabled = staticReportEnabled;
	}

}
