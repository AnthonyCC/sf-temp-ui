package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpEWalletModel;


/**
 * @author Aniwesh Vatsal
 *
 */

/**
 *@deprecated Please use the ErpEWalletController and ErpEWalletServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public interface ErpEWalletSB extends EJBObject{
	
	@Deprecated
	List<ErpEWalletModel> getAllEWallets() throws RemoteException;
	@Deprecated
	ErpEWalletModel findEWalletById(String eWalletId) throws RemoteException;
	@Deprecated
	ErpEWalletModel findEWalletByType(String eWalletType)  throws RemoteException;
	@Deprecated
	int insertCustomerLongAccessToken(ErpCustEWalletModel custEWallet) throws RemoteException;
	@Deprecated
	ErpCustEWalletModel getLongAccessTokenByCustID(String custID, String eWalletType) throws RemoteException;
	@Deprecated
	int updateLongAccessToken(String custId, String longAccessToken,String eWalletType) throws RemoteException;
	@Deprecated
	int deleteLongAccessToken(String custId, String eWalletID) throws RemoteException;
	
}
