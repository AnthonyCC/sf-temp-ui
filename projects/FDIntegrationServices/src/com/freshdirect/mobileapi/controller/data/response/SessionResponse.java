
package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author 
 *
 */
public class SessionResponse extends Message {

	private boolean loggedIn;   
	private boolean sessionIsNew; 
	private boolean sessionExpired; 
		
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public boolean isSessionIsNew() {
		return sessionIsNew;
	}
	public void setSessionIsNew(boolean sessionIsNew) {
		this.sessionIsNew = sessionIsNew;
	}
	public boolean isSessionExpired() {
		return sessionExpired;
	}
	public void setSessionExpired(boolean sessionExpired) {
		this.sessionExpired = sessionExpired;
	}
	
}
