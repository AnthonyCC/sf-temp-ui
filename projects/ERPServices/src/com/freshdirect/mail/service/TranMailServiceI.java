package com.freshdirect.mail.service;

import com.freshdirect.framework.mail.TEmailI;

public interface TranMailServiceI {

	
	public static final String ERROR_EXTERNAL="EXTERNAL";
	public static final String ERROR_INTERNAL="INTERNAL";
	
	public String sendTranEmail(TEmailI emailInfo) throws TranEmailServiceException; 
	
}
