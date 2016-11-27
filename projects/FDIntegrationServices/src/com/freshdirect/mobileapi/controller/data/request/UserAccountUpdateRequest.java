package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * This class is the request class for updating a user's email address and password.
 *
 */
public class UserAccountUpdateRequest extends Message {

	/*
	 * The user's current user id
	 */
	private String oldUserName;
	
	/*
	 * The new ID the user wishes to use
	 */
    private String newUserName;
    
    /*
     * The user's old password
     */
    private transient String oldPassword;
    
    /*
     * The user's new password
     */
    private transient String newPassword;
    
 
	public String getOldUserName() {
		return oldUserName;
	}
	
	public void setOldUserName(String oldUserName) {
		this.oldUserName = oldUserName;
	}
	
	public String getNewUserName() {
		return newUserName;
	}
	
	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
