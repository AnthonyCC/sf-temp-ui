package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.UserSocialProfile;

import java.util.List;
import java.util.ArrayList;

public class SocialLoginResponse extends Message {
	private boolean userExists;
	private List<String> providerTypes = new ArrayList<String>();
	private UserSocialProfile userSocialProfile;
	private boolean loggedInSuccess;
	
	public boolean isUserExists() {
		return userExists;
	}
	public void setUserExists(boolean userExists) {
		this.userExists = userExists;
	}
	public List<String> getProviderTypes() {
		return providerTypes;
	}
	public void setProviderTypes(List<String> providerTypes) {
		this.providerTypes = providerTypes;
	}
	public UserSocialProfile getUserSocialProfile() {
		return userSocialProfile;
	}
	public void setUserSocialProfile(UserSocialProfile userSocialProfile) {
		this.userSocialProfile = userSocialProfile;
	}
	public boolean isLoggedInSuccess() {
		return loggedInSuccess;
	}
	public void setLoggedInSuccess(boolean loggedInSuccess) {
		this.loggedInSuccess = loggedInSuccess;
	}
	
	
}
