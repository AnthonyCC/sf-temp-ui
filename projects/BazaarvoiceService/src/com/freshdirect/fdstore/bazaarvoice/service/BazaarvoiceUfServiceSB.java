package com.freshdirect.fdstore.bazaarvoice.service;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface BazaarvoiceUfServiceSB extends EJBObject{
	
	public BazaarvoiceFeedProcessResult processFile() throws RemoteException;
	public BazaarvoiceFeedProcessResult processRatings() throws RemoteException;
}
