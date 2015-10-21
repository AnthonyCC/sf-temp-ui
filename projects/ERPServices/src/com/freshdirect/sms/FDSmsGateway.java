package com.freshdirect.sms;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sms.model.st.STSmsResponse;
import com.freshdirect.sms.service.SmsNotificationService;
import com.freshdirect.sms.service.SmsProviderFactory;
import com.freshdirect.sms.service.SmsServiceCreationException;
import com.freshdirect.sms.service.SmsServiceException;

public class FDSmsGateway {
	
	private final static Logger LOG = LoggerFactory.getInstance(FDSmsGateway.class);
		
	private final static Object sync = new Object();

	private static SmsNotificationService theOnlyService;

	public static SmsNotificationService getService() throws SmsServiceCreationException {
		synchronized (sync) {
			if (theOnlyService == null) {
				theOnlyService = SmsProviderFactory.getSmsProvider().newService();
			}
			return theOnlyService;
		}
	}


	public static void resetService() {
		synchronized (sync) {
			try {
				getService().close();
				theOnlyService = null;
				LOG.info("SmsService was reset");
			} catch (SmsServiceCreationException e) {
			}
		}
	}
	
	public static STSmsResponse sendSMS(String mobileNumber, String message, String eStoreId) throws SmsServiceException {
		STSmsResponse response = null;
		try {
			
			response = getService().sendSMSRequest(mobileNumber, message, eStoreId);
			
		} catch (SmsServiceCreationException e) {
			LOG.error("Exception while sending SMS request: "+e.getMessage());
		}
		return response;
	}

}
