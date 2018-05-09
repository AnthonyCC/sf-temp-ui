package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.logistics.fdx.controller.data.request.CreateOrderRequest;
import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.sap.ejb.SapException;

/**
 * 
 * @deprecated
 *
 */
public interface SaleCronSB extends EJBObject{ 

	@Deprecated
	public void authorizeSales(long timeout) throws RemoteException;
	@Deprecated
	public void authorizeSubscriptions(long timeout) throws RemoteException;
	@Deprecated
	public int cutoffSales() throws RemoteException;
	@Deprecated
	public void captureSales(long timeout) throws RemoteException;
	@Deprecated
	public void cancelAuthorizationFailed() throws RemoteException;
	@Deprecated
	public void registerGiftCards(long timeout) throws RemoteException;
	@Deprecated
	public void preAuthorizeSales(long timeout) throws RemoteException;
	@Deprecated
	public void reverseAuthorizeSales(long timeout)throws RemoteException;
	@Deprecated
	public void postAuthSales(long timeout) throws RemoteException;
	@Deprecated
	public List<Date> queryCutoffReportDeliveryDates() throws SQLException, RemoteException;
	@Deprecated
	public void postAuthEBTSales(long timeout) throws RemoteException;
	@Deprecated
	public void captureEBTSales(long timeout) throws RemoteException;
	@Deprecated
	public void settleEBTSales() throws RemoteException;
	@Deprecated
	public void settleEBTSales(List<String> saleIds)throws FinderException, RemoteException, ErpTransactionException, SapException, RemoteException;

	
	
}