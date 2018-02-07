package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.sap.ejb.SapException;
/**
 *@deprecated Please use the FDXOrderPickEligibleController and FDXOrderPickEligibleServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface FDXOrderPickEligibleCronSB extends EJBObject{ 

	public void queryForSalesPickEligible() throws RemoteException;
	
	public void sendOrdersToSAP(List<SapOrderPickEligibleInfo> list) throws FinderException, RemoteException, ErpTransactionException, SapException;

}