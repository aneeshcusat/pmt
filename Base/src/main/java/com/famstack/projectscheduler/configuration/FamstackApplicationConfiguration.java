package com.famstack.projectscheduler.configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.ConfigurationSettingsItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;

public class FamstackApplicationConfiguration extends BaseFamstackService
{

    @Resource
    FamstackUserProfileManager famstackUserProfileManager;

    @Resource
    FamstackDataAccessObjectManager famstackDataAccessObjectManager;

    private String hostName;

    private int portNumber;

    private String protocol;

    public static Map<Integer, EmployeeDetails> userMap = new HashMap<>();

    private static Map<String, Integer> userIdMap = new HashMap<>();

    private final Map<String, String> configSettings = new HashMap<>();

    public void initialize()
    {

        logDebug("Initializing FamstackApplicationConfiguration...");
        initializeUserMap(famstackUserProfileManager.getEmployeeDataList());
        initializeConfigurations();
    }

    public void initializeUserMap(List<EmployeeDetails> employeeDetailsList)
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
            (List<ConfigurationSettingsItem>) famstackDataAccessObjectManager.getAllItems("ConfigurationSettingsItem");
        if (configurationSettingsItemsList != null) {
            for (ConfigurationSettingsItem configurationSettingsItem : configurationSettingsItemsList) {
                configSettings.put(configurationSettingsItem.getPropertyName(),
                    configurationSettingsItem.getPropertyValue());
            }
        }
    }

    public List<EmployeeDetails> getUserList()
    {
        return new ArrayList<EmployeeDetails>(userMap.values());
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

    public Map<Integer, EmployeeDetails> getUserMap()
    {
        return userMap;
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

}
