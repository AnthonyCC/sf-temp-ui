package com.freshdirect.cmsadmin.config.security.inmemory;

public class InMemoryUser {

    private String password;
    private String userName;

    public InMemoryUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
