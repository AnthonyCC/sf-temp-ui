package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import javax.ejb.FinderException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.payment.EnumPaymentMethodType;

public interface OrderResourceApiClientI {

	void chargeOrder(FDIdentity identity, String saleId,
			ErpPaymentMethodI paymentMethod, boolean sendEmail,
			CustomerRatingI cra, CrmAgentModel agent, double additionalCharge);


	public ErpSaleModel getOrder(String id) throws RemoteException;
	
	public List<ErpSaleModel> getOrders(List<String> ids) throws RemoteException;
	
	public ErpSaleModel getLastNonCOSOrder(String customerID, EnumSaleType saleType, 
			EnumSaleStatus saleStatus, EnumPaymentMethodType paymentType) throws ErpSaleNotFoundException, RemoteException;
	
	public ErpSaleModel getLastNonCOSOrder(String customerID,	
			EnumSaleType saleType, EnumSaleStatus saleStatus, List<EnumPaymentMethodType> pymtMethodTypes) throws ErpSaleNotFoundException, RemoteException;
	
	public void cutOffSale(String saleId) throws ErpSaleNotFoundException, RemoteException, FDResourceException;
	
	public double getOutStandingBalance(ErpAbstractOrderModel order) throws RemoteException;
	
	public double getPerishableBufferAmount(ErpAbstractOrderModel order) throws RemoteException;
	
	public void updateWaveInfo(String saleId, ErpShippingInfo shippingInfo) throws RemoteException, FinderException;
	
	public void updateCartonInfo(String saleId, List<ErpCartonInfo> cartonList) throws RemoteException, FinderException, FDResourceException;
	 
	public ErpDeliveryInfoModel getDeliveryInfo(String saleId) throws ErpSaleNotFoundException, RemoteException;


	String placeGiftCardOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isBulkOrder) throws RemoteException;


	String placeSubscriptionOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isRealTimeAuthNeeded) throws RemoteException;


	String placeDonationOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status, boolean isOptIn) throws RemoteException;


	String placeOrder(FDActionInfo info, ErpCreateOrderModel createOrder,
			Set<String> appliedPromos, String id, boolean sendEmail,
			CustomerRatingI cra, CrmAgentRole crmAgentRole,
			EnumDlvPassStatus status, boolean isFriendReferred,
			int fdcOrderCount) throws RemoteException;
	
	//public List<ErpSaleInfo> getNSMOrdersForGC()  throws RemoteException;
			
}
