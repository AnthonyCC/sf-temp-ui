package com.freshdirect.fdstore.standingorders.service;


import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface StandingOrderClientHome extends EJBHome{

	public StandingOrderClientSB create() throws CreateException, RemoteException;
}
