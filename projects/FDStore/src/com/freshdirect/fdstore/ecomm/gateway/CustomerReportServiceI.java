package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.fdstore.FDResourceException;

public interface CustomerReportServiceI {
	
	public void generateLateDeliveryCreditReport() throws FDResourceException, RemoteException;
	
	
}
