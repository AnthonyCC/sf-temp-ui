package com.freshdirect.cmsadmin.config.security;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Spring security context wrapper.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SecurityContextWrapper {

    /**
     * Return authenticated user name from spring security context.
     *
     * @return username
     */
    public String getAuthenticatedUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Return is valid user authenticated, anonymousUser is not valid.
     *
     * @return true/false
     */
    public boolean isUserAuthenticated() {
        String userName = getAuthenticatedUserName();
        return userName != null && !"anonymousUser".equalsIgnoreCase(userName);
    }
}
