package com.freshdirect.webapp.taglib.fdstore;


public class SocialGateway {
	

	public static SocialProvider getSocialProvider(String providerName){
		
		if(providerName.equalsIgnoreCase("ONE_ALL"))
		{
			return new SocialProviderOneAll();
		}
		else if(providerName.equalsIgnoreCase("JAN_RAIN"))
		{
			return new SocialProviderJanRain();
		}
		
		return null;
	}

	
}
