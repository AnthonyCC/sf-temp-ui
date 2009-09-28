package com.freshdirect.giftcard.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;

public interface GiftCardManagerSB extends EJBObject {

	public void registerGiftCard(String saleId, double amount) throws FDResourceException, ErpTransactionException, RemoteException;
	
	public List loadRecipentsForOrder(String saleId) throws RemoteException;	
	
	public void resendGiftCard(String saleId,List recepientList,EnumTransactionSource source) throws RemoteException;
	
	public List getGiftCardRecepientsForCustomer(String customerId) throws RemoteException,FDResourceException;
	
	public ErpGiftCardModel validate(String givexNum) throws InvalidCardException, CardInUseException, RemoteException, CardOnHoldException;
	
	public List verifyStatusAndBalance(List giftcards, boolean reloadBalance ) throws RemoteException;
	
	public ErpGiftCardModel verifyStatusAndBalance(ErpGiftCardModel giftcard, boolean reloadBalance) throws  RemoteException;

	public void initiatePreAuthorization(String saleId) throws ErpTransactionException, RemoteException;
	
	public void initiateCancelAuthorizations(String saleId) throws ErpTransactionException, RemoteException;
	
	public List preAuthorizeSales(String saleId) throws RemoteException;
	
	public void postAuthorizeSales(String saleId) throws RemoteException;
	
	public List getGiftCardModel(GenericSearchCriteria resvCriteria) throws  RemoteException;

	public List getGiftCardOrdersForCustomer(String erpCustomerPK) throws RemoteException;

	public Object getGiftCardRedeemedOrders(String erpCustomerPK, String certNum)  throws RemoteException;	
	
	public Object getGiftCardRedeemedOrders(String certNum)  throws RemoteException;	
		
	public List getAllDeletedGiftCard(String erpCustomerPK) throws RemoteException, FDResourceException;

	public List getGiftCardForOrder(String saleId) throws RemoteException, FDResourceException;
			
	public List getGiftCardRecepientsForOrder(String saleId) throws RemoteException,FDResourceException;
		
	public ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum) throws RemoteException, InvalidCardException;
	 
	public void transferGiftCardBalance(String fromGivexNum,String toGivexNum,double amount) throws RemoteException;
		
	public ErpGCDlvInformationHolder loadGiftCardRecipentByGivexNum(String fromGivexNum)throws RemoteException;
	
	public Map getGiftCardRecepientsForOrders(List saleIds) throws RemoteException,FDResourceException;
	
	public ErpGCDlvInformationHolder loadGiftCardRecipentByCertNum(String certNum)throws RemoteException;
}
