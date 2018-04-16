package com.famstack.projectscheduler.manager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.security.FamstackAuthenticationToken;
import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;
import com.famstack.projectscheduler.security.hasher.generator.PasswordTokenGenerator;
import com.famstack.projectscheduler.security.login.LoginResult;
import com.famstack.projectscheduler.security.login.LoginResult.Status;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * The Class UserProfileManager.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
public class FamstackUserProfileManager extends BaseFamstackManager
{

    @Resource
    PasswordTokenGenerator passwordTokenGenerator;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Login.
     * 
     * @param name the name
     * @param password the password
     * @return the login result
     */
    public LoginResult login(String name, String password)
    {
        LoginResult loginResult = new LoginResult();
        UserItem userItem = getUserItem(name);
        if (userItem != null) {
            String encryptPassword = FamstackSecurityTokenManager.encryptString(password, userItem.getHashkey());
            if (userItem.getPassword().equals(encryptPassword)) {
                if (userItem.getNeedPasswordReset()) {
                    loginResult.setStatus(Status.NEW_PASSWORD_REQUIRED);
                    loginResult.setHashKey(FamstackSecurityTokenManager.encryptStringWithDate(userItem.getHashkey(),
                        userItem.getHashkey()));
                } else {
                    loginResult.setStatus(Status.SUCCESS);
                    getFamstackApplicationConfiguration().getUserMap().get(userItem.getId())
                        .setLastPing(new Timestamp(new Date().getTime()));
                }
                loginResult.setUserItem(userItem);
                return loginResult;
            }
        }
        loginResult.setStatus(Status.FAILED);
        return loginResult;

    }

    /**
     * Gets the user token.
     * 
     * @return the user token
     */
    public Authentication getUserToken()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Gets the login result.
     * 
     * @return the login result
     */
    public LoginResult getLoginResult()
    {
        Authentication authentication = getUserToken();
        if (authentication instanceof FamstackAuthenticationToken) {
            return ((FamstackAuthenticationToken) authentication).getLoginResult();
        }
        return null;
    }

    public void createUserItem(EmployeeDetails employeeDetails)
    {
        String hashKey = passwordTokenGenerator.generate(32);
        String password = passwordTokenGenerator.generate(8);
        UserItem userItem = new UserItem();
        String encryptedPassword = FamstackSecurityTokenManager.encryptString(password, hashKey);
        userItem.setHashkey(hashKey);
        userItem.setPassword(encryptedPassword);
        userItem.setNeedPasswordReset(true);
        userItem.setDeleted(false);
        employeeDetails.setPassword(password);
        saveUserItem(employeeDetails, userItem);
    }

    public EmployeeDetails updateUserPasswordForReset(String userName)
    {
        UserItem userItem = getUserItem(userName);
        EmployeeDetails employeeDetails = new EmployeeDetails();
        if (userItem != null) {
            String hashKey = passwordTokenGenerator.generate(32);
            String password = passwordTokenGenerator.generate(8);
            String encryptedPassword = FamstackSecurityTokenManager.encryptString(password, hashKey);
            userItem.setHashkey(hashKey);
            userItem.setPassword(encryptedPassword);
            userItem.setNeedPasswordReset(true);
            getFamstackDataAccessObjectManager().saveOrUpdateItem(userItem);
            employeeDetails.setPassword(password);
            employeeDetails.setEmail(userName);
            employeeDetails.setFirstName(userItem.getFirstName());
            employeeDetails.setId(userItem.getId());
            employeeDetails.setMobileNumber(userItem.getMobileNumber());
            return employeeDetails;
        }
        return null;
    }

    public void updateUserItem(EmployeeDetails employeeDetails)
    {
        UserItem userItem = getUserItem(employeeDetails.getEmail());
        if (userItem != null) {
            saveUserItem(employeeDetails, userItem);
        }
    }

    private void saveUserItem(EmployeeDetails employeeDetails, UserItem userItem)
    {
        userItem.setUserId(employeeDetails.getEmail());

        byte[] imageBytes = getImageBytes(employeeDetails.getFilePhoto());
        if (imageBytes.length > 0) {
            userItem.setProfilePhoto(imageBytes);
        }

        userItem.setLastName(employeeDetails.getLastName());
        userItem.setDesignation(employeeDetails.getDesignation());
        if (StringUtils.isNotBlank(employeeDetails.getDateOfBirth())) {
            try {
                userItem.setDob(new Date(sdf.parse(employeeDetails.getDateOfBirth()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        userItem.setReportertingManager(getUserItemById(employeeDetails.getReportingManger()));
        userItem.setFirstName(employeeDetails.getFirstName());
        userItem.setMobileNumber(employeeDetails.getMobileNumber());
        userItem.setQualification(employeeDetails.getQualification());
        userItem.setUserRole(employeeDetails.getRole());
        userItem.setUserAccessCode(employeeDetails.getUserAccessCode());
        userItem.setGender(employeeDetails.getGender());
        userItem.setTeam(employeeDetails.getTeam());
        userItem.setEmpCode(employeeDetails.getEmpCode());
        userItem.setTemporaryEmployee(employeeDetails.getTemporaryEmployee());
        userItem.setUserGroupId(employeeDetails.getUserGroupId());
        getFamstackDataAccessObjectManager().saveOrUpdateItem(userItem);
    }

    private byte[] getImageBytes(String filePhoto)
    {
        if (filePhoto == null) {
            return null;
        }
        return filePhoto.getBytes();
    }

    public List<?> getAllUserItems()
    {
        return getFamstackDataAccessObjectManager().getAllGroupItems("UserItem");
    }

    public UserItem getUserItemById(int id)
    {
        return (UserItem) getFamstackDataAccessObjectManager().getItemById(id, UserItem.class);
    }

    public UserItem getUserItem(String userId)
    {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("id", userId);
        List<?> userItemList =
            getFamstackDataAccessObjectManager().executeAllGroupQuery(
                HQLStrings.getString("FamstackQueryStringsusersByUserId"), dataMap);
        if (!userItemList.isEmpty()) {
            UserItem userItem = (UserItem) userItemList.get(0);
            return userItem.isDeleted() ? null : userItem;
        }
        return null;
    }

    public EmployeeDetails getEmployee(int userId)
    {
        UserItem userItem = getUserItemById(userId);
        return userItem.isDeleted() ? null : mapEmployeeDetails(userItem);
    }

    public List<EmployeeDetails> getEmployeeDataList()
    {
        List<EmployeeDetails> employeeDetailsList = new ArrayList<EmployeeDetails>();
        List<?> userItemList = getAllUserItems();
        if (userItemList != null) {
            for (Object userItemObj : userItemList) {
                UserItem userItem = (UserItem) userItemObj;
                EmployeeDetails employeeDetails = mapEmployeeDetails(userItem);
                if (employeeDetails != null) {
                    employeeDetailsList.add(employeeDetails);
                }
            }
        }

        return employeeDetailsList;

    }

    public EmployeeDetails mapEmployeeDetails(UserItem userItem)
    {
        if (userItem != null && !userItem.isDeleted()) {
            EmployeeDetails employeeDetails = new EmployeeDetails();
            if (userItem.getDob() != null) {
                employeeDetails.setDateOfBirth(sdf.format(userItem.getDob()));
            }
            employeeDetails.setDesignation(userItem.getDesignation());
            employeeDetails.setEmail(userItem.getUserId());
            employeeDetails.setFirstName(userItem.getFirstName());
            employeeDetails.setGender(userItem.getGender());
            employeeDetails.setTeam(userItem.getTeam());
            employeeDetails.setLastName(userItem.getLastName());
            employeeDetails.setMobileNumber(userItem.getMobileNumber());
            employeeDetails.setQualification(userItem.getQualification());
            employeeDetails.setRole(userItem.getUserRole());
            employeeDetails.setUserGroupId(userItem.getUserGroupId());
            employeeDetails.setUserAccessCode(userItem.getUserAccessCode());

            employeeDetails.setEmpCode(userItem.getEmpCode());
            employeeDetails.setTemporaryEmployee(userItem.getTemporaryEmployee());

            if (userItem.getReportertingManager() != null) {
                employeeDetails.setReportingManger(userItem.getReportertingManager().getId());
            }
            employeeDetails.setId(userItem.getId());
            return employeeDetails;
        }
        return null;

    }

    public void deleteUserItem(int userId)
    {
        UserItem userItem = getUserItemById(userId);
        if (userItem != null) {
            // getFamstackDataAccessObjectManager().deleteItem(userItem);
            userItem.setDeleted(true);
            famstackDataAccessObjectManager.updateItem(userItem);
            getFamstackApplicationConfiguration().initializeUserMap(getEmployeeDataList());
        }
    }

    public boolean isValidUserResetKey(String key, int userId)
    {
        UserItem userItem = getUserItemById(userId);
        logDebug("validating reset key: " + key);
        if (userItem != null) {
            logDebug("user item found");
            logDebug("haskey :" + userItem.getHashkey());
            return FamstackSecurityTokenManager
                .validateSecurityToken(userItem.getHashkey(), key, userItem.getHashkey());
        }
        return false;
    }

    public boolean changePassword(String userName, String oldPassword, String password)
    {
        UserItem userItem = getUserItem(userName);
        if (userItem != null) {
            String encryptPassword = FamstackSecurityTokenManager.encryptString(oldPassword, userItem.getHashkey());
            if (userItem.getPassword().equals(encryptPassword)) {
                userItem.setPassword(FamstackSecurityTokenManager.encryptString(password, userItem.getHashkey()));
                userItem.setNeedPasswordReset(false);
                getFamstackDataAccessObjectManager().updateItem(userItem);
                return true;
            }
        }
        return false;
    }
}
