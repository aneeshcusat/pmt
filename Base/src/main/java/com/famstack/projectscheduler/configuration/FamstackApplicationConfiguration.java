package com.famstack.projectscheduler.configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ConfigurationSettingsItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.AppConfDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.UserGroupDetails;
import com.famstack.projectscheduler.manager.FamstackApplicationConfManager;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;

public class FamstackApplicationConfiguration extends BaseFamstackService
{

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    @Resource
    FamstackApplicationConfManager famstackApplicationConfManager;

    @Resource
    FamstackDataAccessObjectManager famstackDataAccessObjectManager;

    private String hostName;

    private int portNumber;

    private String protocol;

    public static Map<Integer, EmployeeDetails> userMap = new HashMap<>();

    private static Map<String, UserGroupDetails> userGroupMap = new HashMap<>();

    private static Map<String, AppConfDetails> appConfigMap = new HashMap<>();

    private static Map<String, Integer> userIdMap = new HashMap<>();

    private final Map<String, String> configSettings = new HashMap<>();

    public void initialize()
    {

        logDebug("Initializing FamstackApplicationConfiguration...");
        initializeUserMap(famstackUserProfileManager.getEmployeeDataList());
        initializeUserGroupMap(famstackApplicationConfManager.getUserGroupList());
        initializeAppConfigMap(famstackApplicationConfManager.getAppConfigList());
        initializeConfigurations();
        logDebug("END : Initializing FamstackApplicationConfiguration...");

    }

    private void initializeAppConfigMap(List<AppConfDetails> appConfigList)
    {
        if (appConfigMap.isEmpty()) {
            for (AppConfDetails appConfDetails : appConfigList) {
                appConfigMap.put(appConfDetails.getType(), appConfDetails);
            }
        }

    }

    private void initializeUserGroupMap(List<UserGroupDetails> userGroupList)
    {
        if (userGroupMap.isEmpty()) {
            for (UserGroupDetails userGroupDetail : userGroupList) {
                userGroupMap.put(userGroupDetail.getUserGroupId(), userGroupDetail);
            }
        }
    }

    public synchronized void initializeUserMap(List<EmployeeDetails> employeeDetailsList)
    {
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
                configSettings.put(configurationSettingsItem.getPropertyName(),
                    configurationSettingsItem.getPropertyValue());
            }
        }
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
        Collections.sort(userList, new Comparator<EmployeeDetails>()
        {
            @Override
            public int compare(EmployeeDetails employeeDetails1, EmployeeDetails employeeDetails2)
            {
                return employeeDetails1.getFirstName().toUpperCase()
                    .compareTo(employeeDetails2.getFirstName().toUpperCase());
            }
        });
        return userList;
    }

    public Map<Integer, EmployeeDetails> getFilterdUserMap()
    {
        Map<Integer, EmployeeDetails> userFiltedMap = new HashMap<>();
        if (userMap != null) {
            for (Integer userId : userMap.keySet()) {
                if (getFamstackUserSessionConfiguration().getUserGroupId().equalsIgnoreCase(
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

    public String getCurrentUserGroupId()
    {
        return getFamstackUserSessionConfiguration().getUserGroupId();
    }

    public Map<Integer, EmployeeDetails> getUserMap()
    {
        return userMap;
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
            logDebug("updated user ping check" + userId);
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
    public boolean isEmailEnabled()
    {
        if (configSettings.get("enableEmail") == null) {
            return true;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get("enableEmail")) ? true : false;
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
            return true;
        }
        return "TRUE".equalsIgnoreCase(configSettings.get(toggelId)) ? true : false;
    }

}
