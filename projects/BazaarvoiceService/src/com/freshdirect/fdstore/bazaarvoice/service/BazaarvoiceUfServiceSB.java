package com.freshdirect.fdstore.bazaarvoice.service;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface BazaarvoiceUfServiceSB extends EJBObject{
	
	public UploadFeedProcessResult processFile() throws RemoteException;

}
