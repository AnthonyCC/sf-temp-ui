package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.analytics.TimeslotEventModel;

public interface EventProcessorSB extends EJBObject{ 

	public List getEvents() throws RemoteException;
	
	public void logEvent(String custId , Date loginTime) throws RemoteException;

}
