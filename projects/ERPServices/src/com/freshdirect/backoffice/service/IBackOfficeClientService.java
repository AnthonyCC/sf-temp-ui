package com.freshdirect.backoffice.service;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.sms.SmsResponse;

/**
 * 
 * @author smerugu
 *
 */
public interface IBackOfficeClientService {
	public boolean sendSmsResponseForCaseCreate(SmsResponse smsResponse) throws FDResourceException ;

}
