package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.datatransferobject.AppConfItem;
import com.famstack.projectscheduler.datatransferobject.AppConfValueItem;
import com.famstack.projectscheduler.datatransferobject.UserGroupItem;
import com.famstack.projectscheduler.employees.bean.AppConfDetails;
import com.famstack.projectscheduler.employees.bean.AppConfValueDetails;
import com.famstack.projectscheduler.employees.bean.UserGroupDetails;

@Component
public class FamstackApplicationConfManager extends BaseFamstackManager
{

    public void createUserGroup(String name, String companyName, String companyId, String userGroupId,
        Integer userAccessCode)
    {
        UserGroupItem userGroupItem = new UserGroupItem();
        userGroupItem.setCompanyId(companyId);
        userGroupItem.setCompanyName(companyName);
        userGroupItem.setName(companyName);
        userGroupItem.setUserGroupId(userGroupId);
        famstackDataAccessObjectManager.saveOrUpdateItem(userGroupItem);

    }

    public void updateUserGroup(int userGroupId, String name, String companyName, String companyId,
        Integer userAccessCode)
    {
        UserGroupItem userGroupItem =
            (UserGroupItem) famstackDataAccessObjectManager.getItemById(userGroupId, UserGroupItem.class);
        if (userGroupItem != null) {
            userGroupItem.setCompanyId(companyId);
            userGroupItem.setCompanyName(companyName);
            userGroupItem.setName(companyName);
            userGroupItem.setUserAccessCode(userAccessCode);
            famstackDataAccessObjectManager.saveOrUpdateItem(userGroupItem);
        }

    }

    public AppConfItem createAppConfigType(String typeName)
    {
        AppConfItem appConfItem = new AppConfItem();
        appConfItem.setType(typeName);
        return (AppConfItem) famstackDataAccessObjectManager.saveOrUpdateItem(appConfItem);

    }

    public void createAppConfigValue(String name, String value, int appConfigTypeId)
    {
        AppConfItem appConfItem =
            (AppConfItem) famstackDataAccessObjectManager.getItemById(appConfigTypeId, AppConfItem.class);

        AppConfValueItem appConfValueItem = new AppConfValueItem();
        appConfValueItem.setName(name);
        appConfValueItem.setValue(value);
        appConfValueItem.setAppConfItem(appConfItem);

        famstackDataAccessObjectManager.saveOrUpdateItem(appConfValueItem);

    }

    public void createAppConfigValue(String name, String value, String type)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("type", type);

        List<AppConfItem> appConfItems =
            (List<AppConfItem>) famstackDataAccessObjectManager.executeQuery(
                HQLStrings.getString("getAppConfigByType"), dataMap);

        AppConfItem appConfItem = null;
        if (appConfItems == null || appConfItems.isEmpty()) {
            appConfItem = createAppConfigType(type);
        } else {
            appConfItem = appConfItems.get(0);
        }
        AppConfValueItem appConfValueItem = new AppConfValueItem();
        appConfValueItem.setName(name);
        appConfValueItem.setValue(value);
        appConfValueItem.setAppConfItem(appConfItem);

        famstackDataAccessObjectManager.saveOrUpdateItem(appConfValueItem);

        getFamstackApplicationConfiguration().reInitializeAppConfigMap(getCurrentAppConfigList());

    }

    public void updateAppConfigValue(String name, String value, String type)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("type", type);

        List<AppConfItem> appConfItems =
            (List<AppConfItem>) famstackDataAccessObjectManager.executeQuery(
                HQLStrings.getString("getAppConfigByType"), dataMap);

        AppConfItem appConfItem = null;
        AppConfValueItem appConfValueItem = null;
        if (appConfItems == null || appConfItems.isEmpty()) {
            appConfItem = createAppConfigType(type);

        } else {
            appConfItem = appConfItems.get(0);
            appConfValueItem =
                appConfItem.getAppConfValueItem() != null && appConfItem.getAppConfValueItem().size() > 0
                    ? new ArrayList<>(appConfItem.getAppConfValueItem()).get(0) : null;
        }

        if (appConfValueItem == null) {
            appConfValueItem = new AppConfValueItem();
        }

        appConfValueItem.setName(name);
        appConfValueItem.setValue(value);
        appConfValueItem.setAppConfItem(appConfItem);

        famstackDataAccessObjectManager.saveOrUpdateItem(appConfValueItem);

        getFamstackApplicationConfiguration().reInitializeAppConfigMap(getCurrentAppConfigList());

    }

    public void deleteAppConfigType(int appConfigTypeId)
    {
        AppConfItem appConfItem =
            (AppConfItem) famstackDataAccessObjectManager.getItemById(appConfigTypeId, AppConfItem.class);
        if (appConfItem != null) {
            famstackDataAccessObjectManager.deleteItem(appConfItem);
        }
    }

    public void deleteAppConfigValue(int appConfigValueId)
    {
        AppConfValueItem appConfValueItem =
            (AppConfValueItem) famstackDataAccessObjectManager.getItemById(appConfigValueId, AppConfValueItem.class);
        if (appConfValueItem != null) {
            famstackDataAccessObjectManager.deleteItem(appConfValueItem);
            getFamstackApplicationConfiguration().reInitializeAppConfigMap(getCurrentAppConfigList());
        }
    }

    public void deleteUserGroup(int userGroupId)
    {
        UserGroupItem userGroupItem =
            (UserGroupItem) famstackDataAccessObjectManager.getItemById(userGroupId, UserGroupItem.class);
        if (userGroupItem != null) {
            famstackDataAccessObjectManager.deleteItem(userGroupItem);
        }
    }

    @SuppressWarnings("unchecked")
    public List<UserGroupDetails> getUserGroupList()
    {
        return mapUserGroupDetails((List<UserGroupItem>) getFamstackDataAccessObjectManager().getAllGroupItems(
            "UserGroupItem"));
    }

    @SuppressWarnings("unchecked")
    public List<AppConfDetails> getAppConfigList()
    {
        return mapAppConfigDetails((List<AppConfItem>) getFamstackDataAccessObjectManager().getAllGroupItems(
            "AppConfItem"));
    }

    @SuppressWarnings("unchecked")
    public List<AppConfDetails> getCurrentAppConfigList()
    {
        return mapAppConfigDetails((List<AppConfItem>) getFamstackDataAccessObjectManager().getAllItems("AppConfItem"));
    }

    public List<AppConfDetails> mapAppConfigDetails(List<AppConfItem> appConfigItems)
    {
        List<AppConfDetails> appConfDetailsList = new ArrayList<>();

        if (appConfigItems != null) {
            for (AppConfItem appConfigItem : appConfigItems) {
                AppConfDetails appConfDetails = new AppConfDetails();

                if (appConfigItem.getAppConfValueItem() != null) {
                    appConfDetails.setAppConfValueDetails(new HashSet<AppConfValueDetails>());
                    logDebug("appConfDetails.getApplicationId() -->" + appConfDetails.getApplicationId());
                    for (AppConfValueItem appConfValueItem : appConfigItem.getAppConfValueItem()) {
                        AppConfValueDetails appConfValueDetails = new AppConfValueDetails();
                        appConfValueDetails.setAppConfValueId(appConfValueItem.getAppConfValueId());
                        appConfValueDetails.setName(appConfValueItem.getName());
                        appConfValueDetails.setValue(appConfValueItem.getValue());
                        appConfDetails.getAppConfValueDetails().add(appConfValueDetails);
                        logDebug("appConfValueDetails.getValue() -->" + appConfValueDetails.getValue());
                    }
                }
                appConfDetails.setApplicationId(appConfigItem.getApplicationId());
                appConfDetails.setType(appConfigItem.getType() + appConfigItem.getUserGroupId());
                appConfDetails.setUserGroupId(appConfigItem.getUserGroupId());
                appConfDetailsList.add(appConfDetails);
            }
        }
        return appConfDetailsList;
    }

    public List<UserGroupDetails> mapUserGroupDetails(List<UserGroupItem> allGroupItems)
    {
        List<UserGroupDetails> userGroupDetailsList = new ArrayList<>();
        if (allGroupItems != null) {
            for (UserGroupItem userGroupItem : allGroupItems) {
                UserGroupDetails userGroupDetails = new UserGroupDetails();
                userGroupDetails.setCompanyId(userGroupItem.getCompanyId());
                userGroupDetails.setCompanyName(userGroupItem.getCompanyName());
                userGroupDetails.setId(userGroupItem.getId());
                userGroupDetails.setName(userGroupItem.getName());
                userGroupDetails.setUserGroupId(userGroupItem.getUserGroupId());
                userGroupDetails.setUserAccessCode(userGroupItem.getUserAccessCode());
                userGroupDetailsList.add(userGroupDetails);
            }
        }
        return userGroupDetailsList;
    }

}
