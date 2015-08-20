package com.freshdirect.webapp.taglib.fdstore;


import java.util.HashMap;

public interface SocialProvider {
	public HashMap<String,String> getSocialUserProfile(String connectionToken);
	public HashMap<String,String> getSocialUserProfileByUserToken(String userToken,String providerName);
}
