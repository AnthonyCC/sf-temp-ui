package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface StandingOrdersServiceHome extends EJBHome {
    
    public final static String JNDI_HOME = "freshdirect.fdstore.StandingOrdersService";

	public StandingOrdersServiceSB create() throws CreateException, RemoteException;

}
