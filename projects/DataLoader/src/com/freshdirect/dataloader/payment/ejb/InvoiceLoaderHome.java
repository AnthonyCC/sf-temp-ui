/*
 * InvoiceLoaderHome.java
 *
 * Created on December 27, 2001, 12:06 PM
 */

package com.freshdirect.dataloader.payment.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;

public interface InvoiceLoaderHome extends EJBHome {
	
	public InvoiceLoaderSB create() throws CreateException, EJBException, RemoteException;
}

