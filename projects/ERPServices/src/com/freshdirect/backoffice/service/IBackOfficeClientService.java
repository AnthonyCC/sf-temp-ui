package com.freshdirect.backoffice.service;

import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.fdstore.FDResourceException;

/**
 * 
 * @author smerugu
 *
 */
public interface IBackOfficeClientService {
	public boolean createCaseByRecievedSmsData(RecievedSmsData recievedSmsData) throws FDResourceException ;

}
