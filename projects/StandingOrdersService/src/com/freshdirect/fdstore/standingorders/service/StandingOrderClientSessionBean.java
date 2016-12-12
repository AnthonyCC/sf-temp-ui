package com.freshdirect.fdstore.standingorders.service;

import com.freshdirect.framework.core.SessionBeanSupport;

public class StandingOrderClientSessionBean extends SessionBeanSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3776881974103072790L;

	
	public  boolean runManualJob( String orders, boolean sendReportEmail, boolean sendReminderNotificationEmail)  {
		
		
		StandingOrdersServiceCmd.runManualJob(orders, sendReportEmail, sendReminderNotificationEmail,null);
		
		return true;
	}

	
}
