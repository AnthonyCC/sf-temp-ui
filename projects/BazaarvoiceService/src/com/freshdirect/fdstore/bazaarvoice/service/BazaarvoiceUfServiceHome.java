package com.freshdirect.fdstore.bazaarvoice.service;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface BazaarvoiceUfServiceHome extends EJBHome{
	
	public final static String JNDI_HOME = "freshdirect.fdstore.BazaarvoiceUfService";

	public BazaarvoiceUfServiceSB create() throws CreateException, RemoteException;

}
