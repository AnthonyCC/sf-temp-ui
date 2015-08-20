package com.freshdirect.mobileapi.controller.data.request;

public class SocialRegisterRequest extends RegisterMessageFdxRequest  {
	private String userToken;
	private String provider;
	
	
	public SocialRegisterRequest()
	{
		super();
	}
	
	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	
}
