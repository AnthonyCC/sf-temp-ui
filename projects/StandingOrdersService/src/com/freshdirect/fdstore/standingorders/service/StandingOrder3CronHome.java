package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface StandingOrder3CronHome extends EJBHome{
	
	public StandingOrder3CronSB create() throws CreateException, RemoteException;
	

}
