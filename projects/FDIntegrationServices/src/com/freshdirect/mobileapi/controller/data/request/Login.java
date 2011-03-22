package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * 
 * @author Rob
 *
 */
public class Login extends Message {

    private String username;

    //be defensive until proven otherwise. marking it transient so that it won't be serialized
    private transient String password;
    
    private String source;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
        
}
