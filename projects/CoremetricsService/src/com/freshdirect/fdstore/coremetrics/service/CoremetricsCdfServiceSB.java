package com.freshdirect.fdstore.coremetrics.service;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.coremetrics.CmContext;

public interface CoremetricsCdfServiceSB extends EJBObject {
	@Deprecated
	public CdfProcessResult processCdf() throws RemoteException;
	public CdfProcessResult processCdf(CmContext ctx) throws RemoteException;
	
}
