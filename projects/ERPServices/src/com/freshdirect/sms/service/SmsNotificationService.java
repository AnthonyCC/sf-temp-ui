package com.freshdirect.sms.service;

import com.freshdirect.sms.model.st.STSmsResponse;

public interface SmsNotificationService {
	
	void close();
	
	STSmsResponse sendSMSRequest(String mobileNumber, String message, String eStoreId) throws SmsServiceException;
	
}
