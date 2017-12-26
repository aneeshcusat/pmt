package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.datatransferobject.AppConfItem;
import com.famstack.projectscheduler.datatransferobject.AppConfValueItem;
import com.famstack.projectscheduler.datatransferobject.UserGroupItem;
import com.famstack.projectscheduler.employees.bean.AppConfDetails;
import com.famstack.projectscheduler.employees.bean.AppConfValueDetails;
import com.famstack.projectscheduler.employees.bean.UserGroupDetails;

@Component
public class FamstackApplicationConfManager extends BaseFamstackManager
{

    public void createAppConfigType(String typeName)
    {
        AppConfItem appConfItem = new AppConfItem();
        appConfItem.setType(typeName);
        famstackDataAccessObjectManager.saveOrUpdateItem(appConfItem);

    }

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

    public List<AppConfDetails> mapAppConfigDetails(List<AppConfItem> appConfigItems)
    {
        List<AppConfDetails> appConfDetailsList = new ArrayList<>();

        if (appConfigItems != null) {
            for (AppConfItem appConfigItem : appConfigItems) {
                AppConfDetails appConfDetails = new AppConfDetails();

                if (appConfigItem.getAppConfValueItem() != null) {
                    appConfDetails.setAppConfValueDetails(new HashSet<AppConfValueDetails>());
                    for (AppConfValueItem appConfValueItem : appConfigItem.getAppConfValueItem()) {
                        AppConfValueDetails appConfValueDetails = new AppConfValueDetails();
                        appConfValueDetails.setAppConfValueId(appConfValueItem.getAppConfValueId());
                        appConfValueDetails.setName(appConfValueItem.getName());
                        appConfValueDetails.setValue(appConfValueItem.getValue());
                    }
                }
                appConfDetails.setApplicationId(appConfigItem.getApplicationId());
                appConfDetails.setType(appConfigItem.getType());
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
