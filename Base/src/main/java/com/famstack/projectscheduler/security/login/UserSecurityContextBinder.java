package com.famstack.projectscheduler.security.login;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * The Class UserSecurityContextBinder.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
@Component
@Scope(SCOPE_SINGLETON)
public class UserSecurityContextBinder {

    /** The trust resolver. */
    @Autowired
    private AuthenticationTrustResolver trustResolver;

    /**
     * Bind user authentication.
     *
     * @param authentication the authentication
     */
    public void bindUserAuthentication(Authentication authentication) {
        getSecurityContext().setAuthentication(authentication);
    }

    /**
     * Unbind user authentication.
     */
    public void unbindUserAuthentication() {
        getSecurityContext().setAuthentication(null);
    }

    /**
     * Checks if is authenticated.
     *
     * @return true, if is authenticated
     */
    public boolean isAuthenticated() {
        Authentication authentication = getSecurityContext().getAuthentication();
        return !trustResolver.isAnonymous(authentication) && authentication != null;
    }

    /**
     * Gets the security context.
     *
     * @return the security context
     */
    SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    /**
     * Gets the trust resolver.
     *
     * @return the trust resolver
     */
    public AuthenticationTrustResolver getTrustResolver() {
        return trustResolver;
    }

    /**
     * Sets the trust resolver.
     *
     * @param trustResolver the new trust resolver
     */
    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        this.trustResolver = trustResolver;
    }
}
