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
/**
 *@deprecated Please use the GiftCardController and GiftCardManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public interface GiftCardManagerSB extends EJBObject {
	@Deprecated
	public void registerGiftCard(String saleId, double amount) throws FDResourceException, ErpTransactionException, RemoteException;
	@Deprecated
	public List loadRecipentsForOrder(String saleId) throws RemoteException;	
	@Deprecated
	public void resendGiftCard(String saleId,List recepientList,EnumTransactionSource source) throws RemoteException, FDResourceException;
	@Deprecated
	public List getGiftCardRecepientsForCustomer(String customerId) throws RemoteException,FDResourceException;
	@Deprecated
	public ErpGiftCardModel validate(String givexNum) throws InvalidCardException, CardInUseException, RemoteException, CardOnHoldException;
	@Deprecated
	public List verifyStatusAndBalance(List giftcards, boolean reloadBalance ) throws RemoteException;
	@Deprecated
	public ErpGiftCardModel verifyStatusAndBalance(ErpGiftCardModel giftcard, boolean reloadBalance) throws  RemoteException;
	@Deprecated
	public void initiatePreAuthorization(String saleId) throws ErpTransactionException, RemoteException;
	@Deprecated
	public void initiateCancelAuthorizations(String saleId) throws ErpTransactionException, RemoteException;
	@Deprecated
	public List preAuthorizeSales(String saleId) throws RemoteException;
	@Deprecated
	public List reversePreAuthForCancelOrders(String saleId) throws RemoteException;
	@Deprecated
	public void postAuthorizeSales(String saleId) throws RemoteException;
	@Deprecated
	public List getGiftCardModel(GenericSearchCriteria resvCriteria) throws  RemoteException;
	@Deprecated
	public List getGiftCardOrdersForCustomer(String erpCustomerPK) throws RemoteException, FDResourceException;
	@Deprecated
	public Object getGiftCardRedeemedOrders(String erpCustomerPK, String certNum)  throws RemoteException, FDResourceException;	
	@Deprecated
	public Object getGiftCardRedeemedOrders(String certNum)  throws RemoteException, FDResourceException;	
	@Deprecated
	public List getAllDeletedGiftCard(String erpCustomerPK) throws RemoteException, FDResourceException;
	@Deprecated
	public List getGiftCardForOrder(String saleId) throws RemoteException, FDResourceException;
	@Deprecated	
	public List getGiftCardRecepientsForOrder(String saleId) throws RemoteException,FDResourceException;
	@Deprecated
	public ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum) throws RemoteException, InvalidCardException;
	@Deprecated
	public void transferGiftCardBalance(String fromGivexNum,String toGivexNum,double amount) throws RemoteException, InvalidCardException;
	@Deprecated
	public ErpGCDlvInformationHolder loadGiftCardRecipentByGivexNum(String fromGivexNum)throws RemoteException;
	@Deprecated
	public Map getGiftCardRecepientsForOrders(List saleIds) throws RemoteException,FDResourceException;
	@Deprecated
	public ErpGCDlvInformationHolder loadGiftCardRecipentByCertNum(String certNum)throws RemoteException;
}
