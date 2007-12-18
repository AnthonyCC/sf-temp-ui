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
import javax.ejb.*;
import java.rmi.RemoteException;

public interface InvoiceLoaderHome extends EJBHome {
	
	public InvoiceLoaderSB create() throws CreateException, EJBException, RemoteException;
}

