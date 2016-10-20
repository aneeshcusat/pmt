package com.famstack.projectscheduler.security;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.manager.FamstackUserProfileManager;
import com.famstack.projectscheduler.security.login.LoginResult;
import com.famstack.projectscheduler.security.login.LoginResult.Status;

/**
 * The Class famstackAuthenticationProvider.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class FamstackAuthenticationProvider extends BaseFamstackService implements AuthenticationProvider {

    @Resource
    FamstackUserProfileManager userProfileManager;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.authentication.AuthenticationProvider#
     * authenticate(org.springframework.security.core.Authentication)
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FamstackAuthenticationToken token = (FamstackAuthenticationToken) authentication;
        LoginResult loginResult;
        loginResult = userProfileManager.login(token.getName(), token.getPassword());
        if (loginResult.getStatus() != Status.SUCCESS) {
            token.setAuthenticated(false);
        }
        getFamstackUserSessionConfiguration().setLoginResult(loginResult);
        token.setLoginResult(loginResult);
        return token;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.authentication.AuthenticationProvider#supports
     * (java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(FamstackAuthenticationToken.class);
    }
}
