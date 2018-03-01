package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.sap.ejb.SapException;

public interface SaleCronServiceI {

	public void authorizeSales(long timeout) throws RemoteException;

	public void authorizeSubscriptions(long timeout) throws RemoteException;

	public int cutoffSales() throws RemoteException;
	
	public void captureSales(long timeout) throws RemoteException;

	public void cancelAuthorizationFailed() throws RemoteException;

	public void registerGiftCards(long timeout) throws RemoteException;
	
	public void preAuthorizeSales(long timeout) throws RemoteException;

	public void reverseAuthorizeSales(long timeout)throws RemoteException;
	
	public void postAuthSales(long timeout) throws RemoteException;
	
	public List<Date> queryCutoffReportDeliveryDates() throws SQLException, RemoteException;
	
	public void postAuthEBTSales(long timeout) throws RemoteException;
	
	public void captureEBTSales(long timeout) throws RemoteException;
	
	public void settleEBTSales() throws RemoteException;
	
	public void settleEBTSales(List<String> saleIds)throws FinderException, RemoteException, ErpTransactionException, SapException, RemoteException;

}