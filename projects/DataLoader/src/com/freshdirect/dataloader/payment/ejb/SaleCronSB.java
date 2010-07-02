package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface SaleCronSB extends EJBObject{ 

	public void authorizeSales(long timeout) throws RemoteException;

	public void authorizeSubscriptions(long timeout) throws RemoteException;

	public int cutoffSales() throws RemoteException;

	public void captureSales(long timeout) throws RemoteException;

	public void cancelAuthorizationFailed() throws RemoteException;

	public void registerGiftCards(long timeout) throws RemoteException;
	
	public void preAuthorizeSales(long timeout) throws RemoteException;

	public void reverseAuthorizeSales(long timeout)throws RemoteException;
	
	public void postAuthSales(long timeout) throws RemoteException;
}
