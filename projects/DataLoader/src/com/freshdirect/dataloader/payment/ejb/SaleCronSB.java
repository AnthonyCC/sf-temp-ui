package com.freshdirect.dataloader.payment.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;

public interface SaleCronSB extends EJBObject{ 

	public void authorizeSales(long timeout) throws RemoteException;

	public void authorizeSubscriptions(long timeout) throws RemoteException;

	public int cutoffSales() throws RemoteException;

	public void captureSales(long timeout) throws RemoteException;

	public void cancelAuthorizationFailed() throws RemoteException;

	public void registerGiftCards(long timeout) throws RemoteException;
	
	public void preAuthorizeSales(long timeout) throws RemoteException;
	
	public void postAuthSales(long timeout) throws RemoteException;
}
