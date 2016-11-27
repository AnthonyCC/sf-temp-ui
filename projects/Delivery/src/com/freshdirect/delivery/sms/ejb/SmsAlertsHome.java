package com.freshdirect.delivery.sms.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface SmsAlertsHome extends EJBHome{

	public SmsAlertsSB create() throws CreateException, RemoteException;
}
