package com.freshdirect.fdstore.coremetrics.service;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface CoremetricsCdfServiceHome extends EJBHome {
    
    public final static String JNDI_HOME = "freshdirect.fdstore.CoremetricsCdfService";

	public CoremetricsCdfServiceSB create() throws CreateException, RemoteException;

}
