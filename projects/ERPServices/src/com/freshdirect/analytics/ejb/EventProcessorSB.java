package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.analytics.SessionEvent;
import com.freshdirect.analytics.TimeslotEventModel;

public interface EventProcessorSB extends EJBObject{ 

	public void getEvents(Date startTime, Date endTime) throws RemoteException;
	
	public void logEvent(SessionEvent event) throws RemoteException;

}
