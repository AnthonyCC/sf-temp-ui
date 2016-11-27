package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ActivityLogHome extends EJBHome {

	public ActivityLogSB create() throws CreateException, RemoteException;

}