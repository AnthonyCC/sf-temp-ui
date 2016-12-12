package com.freshdirect.fdstore.standingorders.service;


import java.rmi.RemoteException;

import javax.ejb.EJBObject;


public interface StandingOrderClientSB extends EJBObject{

	public  boolean runManualJob( String orders, boolean sendReportEmail, boolean sendReminderNotificationEmail) throws RemoteException ;	

}
