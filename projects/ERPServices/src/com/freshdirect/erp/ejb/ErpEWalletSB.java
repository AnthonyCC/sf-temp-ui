package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpEWalletTxNotifyModel;


/**
 * @author Aniwesh Vatsal
 *
 */
public interface ErpEWalletSB extends EJBObject{
	
	
	List<ErpEWalletModel> getAllEWallets() throws RemoteException;
	
	ErpEWalletModel findEWalletById(String eWalletId) throws RemoteException;
	
	ErpEWalletModel findEWalletByType(String eWalletType)  throws RemoteException;
	
	int insertCustomerLongAccessToken(ErpCustEWalletModel custEWallet) throws RemoteException;

	int insertEWalletTxnNotify(ErpEWalletTxNotifyModel eWallettxtNotify) throws RemoteException;
	
	ErpCustEWalletModel getLongAccessTokenByCustID(String custID, String eWalletType) throws RemoteException;
	
	int updateLongAccessToken(String custId, String longAccessToken,String eWalletType) throws RemoteException;
	
	int deleteLongAccessToken(String custId, String eWalletID) throws RemoteException;
	
	int updateEWalletTxnNotify(ErpEWalletTxNotifyModel eWallettxtNotify) throws RemoteException;
	
	void updateEWalletTxnNotifyStatus(String orderId, String status) throws RemoteException;
}
