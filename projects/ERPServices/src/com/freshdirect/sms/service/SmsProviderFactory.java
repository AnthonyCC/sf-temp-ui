package com.freshdirect.sms.service;

public class SmsProviderFactory implements SmsProvider {
	
	private static SmsProviderFactory theOnlyFactory;
	
	public SmsNotificationService newService() {
		return getSingleTouchProvider();
	}
	
	private SmsNotificationService getSingleTouchProvider() {
		STSmsProvider provider = new STSmsProvider();
		return provider;
	}
	
	private SmsProviderFactory() {}

    public static SmsProvider getSmsProvider() {
    	
      if (theOnlyFactory == null) {
    	  theOnlyFactory = new SmsProviderFactory();
      }
      return theOnlyFactory;
    }
}
