package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;

public interface GiftCardManagerServiceI {
	
	public void registerGiftCard(String saleId, double amount) throws FDResourceException, ErpTransactionException, RemoteException;
	
	public List loadRecipentsForOrder(String saleId) throws RemoteException;	
		
	public List getGiftCardRecepientsForCustomer(String customerId) throws RemoteException,FDResourceException;
		
	public List verifyStatusAndBalance(List giftcards, boolean reloadBalance ) throws RemoteException;
	
	public ErpGiftCardModel verifyStatusAndBalance(ErpGiftCardModel giftcard, boolean reloadBalance) throws  RemoteException;
	
	public void initiatePreAuthorization(String saleId) throws ErpTransactionException, RemoteException;
	
	public void initiateCancelAuthorizations(String saleId) throws ErpTransactionException, RemoteException;
	
	public List preAuthorizeSales(String saleId) throws RemoteException;
	
	public List reversePreAuthForCancelOrders(String saleId) throws RemoteException;
	
	public void postAuthorizeSales(String saleId) throws RemoteException;
	
	public List getGiftCardModel(GenericSearchCriteria resvCriteria) throws  RemoteException;
	
	public List getGiftCardOrdersForCustomer(String erpCustomerPK) throws RemoteException, FDResourceException;
	
	public Object getGiftCardRedeemedOrders(String erpCustomerPK, String certNum)  throws RemoteException, FDResourceException;	
	
	public Object getGiftCardRedeemedOrders(String certNum)  throws RemoteException, FDResourceException;	
	
	public List getAllDeletedGiftCard(String erpCustomerPK) throws RemoteException, FDResourceException;
	
	public List getGiftCardForOrder(String saleId) throws RemoteException, FDResourceException;
		
	public List getGiftCardRecepientsForOrder(String saleId) throws RemoteException,FDResourceException;
	
	public ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum) throws RemoteException;
	

	public ErpGCDlvInformationHolder loadGiftCardRecipentByGivexNum(String fromGivexNum)throws RemoteException;
	
	public Map getGiftCardRecepientsForOrders(List saleIds) throws RemoteException,FDResourceException;
	
	public ErpGCDlvInformationHolder loadGiftCardRecipentByCertNum(String certNum)throws RemoteException;

	public void transferGiftCardBalance(String customerid, String fromGivexNum,String toGivexNum, double amount) throws RemoteException;
}
