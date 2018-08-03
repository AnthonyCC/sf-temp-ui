package com.freshdirect.webapp.taglib.fdstore;


import java.util.HashMap;

public interface SocialProvider {
	public HashMap<String,String> getSocialUserProfile(String connectionToken);
	public HashMap<String,String> getSocialUserProfileByUserToken(String userToken,String providerName);
	public HashMap<String,String> getSocialUserProfileByAccessToken(String accessToken,String providerName);

    boolean deleteSocialIdentity(String identityToken);
}
