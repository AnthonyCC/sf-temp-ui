package com.freshdirect.mail.service;

import com.freshdirect.framework.mail.TEmailI;

public interface TranMailServiceI {

	//bad idea to put constants in an interface. now we are stuck with this forever.
	public static final String ERROR_EXTERNAL="EXTERNAL";
	public static final String ERROR_INTERNAL="INTERNAL";
	public static final String INFO_WARNING="INFORMATION";
	
	public String sendTranEmail(TEmailI emailInfo) throws TranEmailServiceException; 
	
}
