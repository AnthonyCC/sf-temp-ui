package com.freshdirect.fdstore.coremetrics.service;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface CoremetricsCdfServiceSB extends EJBObject {
	
	public CdfProcessResult processCdf() throws RemoteException;
	
}
