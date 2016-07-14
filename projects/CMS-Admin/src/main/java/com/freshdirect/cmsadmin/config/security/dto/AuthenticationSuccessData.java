package com.freshdirect.cmsadmin.config.security.dto;

/**
 * POJO for authentication success data.
 */
public class AuthenticationSuccessData {

    private UserData user;
    private String defaultPath;

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

}
