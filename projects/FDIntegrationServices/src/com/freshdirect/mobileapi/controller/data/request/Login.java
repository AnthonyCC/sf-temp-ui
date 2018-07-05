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
    
    private String captchaToken;
    
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
	
	// For IBM silverpop integration ..
	private String channel;
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}   
	
	private String destination;
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	private String qualifier;
	
	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	
	public String rafclickid;
	
	public String getRafclickid() {
		return rafclickid;
	}

	public void setRafclickid(String rafclickid) {
		this.rafclickid = rafclickid;
	}

	public String rafpromocode;
	
	public String getRafpromocode() {
		return rafpromocode;
	}

	public void setRafpromocode(String rafpromocode) {
		this.rafpromocode = rafpromocode;
	}

	public String getCaptchaToken() {
		return captchaToken;
	}

	public void setCaptchaToken(String captchaToken) {
		this.captchaToken = captchaToken;
	}
}
