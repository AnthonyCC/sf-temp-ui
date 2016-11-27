package com.freshdirect.mobileapi.controller.data.request;

public class ExternalAccountRegisterRequest extends RegisterMessageEx  {
	private String userToken;
	private String provider;
	private String source;
	
	public ExternalAccountRegisterRequest()
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
}
