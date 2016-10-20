package com.famstack.projectscheduler.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.famstack.projectscheduler.security.login.LoginResult;

/**
 * The Class famstackAuthenticationToken.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class FamstackAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * Instantiates a new delivery interface authentication token.
     *
     * @param principal the principal
     * @param credentials the credentials
     */
    public FamstackAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials, null);
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1130961461756135458L;

    /** The login result. */
    private LoginResult loginResult;

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        Object credentials = getCredentials();
        if (credentials != null) {
            return credentials.toString();
        }
        return null;
    }

    /**
     * Gets the login result.
     *
     * @return the login result
     */
    public LoginResult getLoginResult() {
        return loginResult;
    }

    /**
     * Sets the login result.
     *
     * @param loginResult the new login result
     */
    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }
}
