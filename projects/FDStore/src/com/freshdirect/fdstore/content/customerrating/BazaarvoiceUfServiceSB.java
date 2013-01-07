package com.freshdirect.fdstore.content.customerrating;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDTO;

public interface BazaarvoiceUfServiceSB extends EJBObject{
	
	public BazaarvoiceFeedProcessResult processFile() throws RemoteException;
	public BazaarvoiceFeedProcessResult processRatings() throws RemoteException;
	public long getLastRefresh() throws FDResourceException, RemoteException;
	public List<CustomerRatingsDTO> getCustomerRatings() throws FDResourceException, RemoteException;
}
