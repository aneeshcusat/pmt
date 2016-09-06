package com.famstack.projectscheduler.security.user;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.security.FamstackAuthenticationToken;
import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;
import com.famstack.projectscheduler.security.login.LoginResult;
import com.famstack.projectscheduler.security.login.LoginResult.Status;

/**
 * The Class UserProfileManager.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
public class FamstackUserProfileManager extends BaseFamstackService {

    /** The delivery interface data access object manager. */
    @Resource
    FamstackDataAccessObjectManager famstackDataAccessObjectManager;

    /**
     * Login.
     *
     * @param name the name
     * @param password the password
     * @return the login result
     */
    public LoginResult login(String name, String password) {
        LoginResult loginResult = new LoginResult();
        UserItem userItem = famstackDataAccessObjectManager.getUser(name);
        if (userItem != null) {
            String encryptPassword = FamstackSecurityTokenManager.encryptString(password, userItem.getHashkey());
            if (userItem.getPassword().equals(encryptPassword)) {
                loginResult.setStatus(Status.SUCCESS);
                loginResult.setUserRole(userItem.getUserRole());
                loginResult.setUserName(name);
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
    public Authentication getUserToken() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Gets the login result.
     *
     * @return the login result
     */
    public LoginResult getLoginResult() {
        Authentication authentication = getUserToken();
        if (authentication instanceof FamstackAuthenticationToken) {
            return ((FamstackAuthenticationToken) authentication).getLoginResult();
        }
        return null;
    }
}
