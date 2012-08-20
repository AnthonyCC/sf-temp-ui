package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

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
	
	public List<Date> queryCutoffReportDeliveryDates() throws RemoteException;
	
	public void postAuthEBTSales(long timeout) throws RemoteException;
	
	public void captureAndSettleEBTSales(long timeout) throws RemoteException;
	
	public void settleEBTSales(List<String> saleIds)throws RemoteException;
}