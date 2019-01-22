package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecommerce.data.dlv.FDReservationData;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.payment.EnumPaymentMethodType;

public interface OrderResourceApiClientI {

	public void chargeOrder(FDIdentity identity, String saleId,
			ErpPaymentMethodI paymentMethod, boolean sendEmail,
			CustomerRatingI cra, CrmAgentModel agent, double additionalCharge);
	
	public List<ErpSaleModel> getOrders(List<String> ids) throws RemoteException;
	
	public ErpSaleModel getLastNonCOSOrder(String customerID, EnumSaleType saleType, 
			EnumSaleStatus saleStatus, EnumPaymentMethodType paymentType) throws ErpSaleNotFoundException, RemoteException;
	
	public ErpSaleModel getLastNonCOSOrder(String customerID,	
			EnumSaleType saleType, EnumSaleStatus saleStatus, List<EnumPaymentMethodType> pymtMethodTypes) throws ErpSaleNotFoundException, RemoteException;
	
	public void cutOffSale(String saleId) throws ErpSaleNotFoundException, RemoteException, FDResourceException;
	
	public double getOutStandingBalance(ErpAbstractOrderModel order) throws RemoteException;
	
	public void updateWaveInfo(String saleId, ErpShippingInfo shippingInfo) throws RemoteException, FinderException;
	
	public void updateCartonInfo(String saleId, List<ErpCartonInfo> cartonList) throws RemoteException, FinderException, FDResourceException;

	public String placeGiftCardOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isBulkOrder) throws RemoteException, ServiceUnavailableException, FDResourceException, ErpFraudException, ErpAuthorizationException, ErpAddressVerificationException;


	public String placeSubscriptionOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isRealTimeAuthNeeded) throws RemoteException, FDResourceException, ErpFraudException, DeliveryPassException, FDPaymentInadequateException, InvalidCardException, ErpTransactionException;


	public String placeDonationOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status, boolean isOptIn) throws RemoteException;

	public String placeOrder(FDActionInfo info, ErpCreateOrderModel createOrder, Set<String> appliedPromos, String id,
			boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isFriendReferred, int fdcOrderCount) throws FDResourceException, ErpFraudException,
			ErpAuthorizationException, ErpAddressVerificationException, ReservationException, DeliveryPassException,
			FDPaymentInadequateException, ErpTransactionException, InvalidCardException, RemoteException;

	public void modifyOrder(FDActionInfo info, String saleId, ErpModifyOrderModel order, Set<String> appliedPromos,
			String originalReservationId, boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole,
			EnumDlvPassStatus status, boolean hasCouponDiscounts, int fdcOrderCount) throws FDResourceException,
			ErpFraudException, ErpAuthorizationException, ErpTransactionException, DeliveryPassException,
			FDPaymentInadequateException, ErpAddressVerificationException, InvalidCardException, RemoteException;


	public void modifyAutoRenewOrder(FDActionInfo info, String saleId,
			ErpModifyOrderModel order, Set<String> appliedPromos,
			String originalReservationId, boolean sendEmail,
			CustomerRatingI cra, CrmAgentRole crmAgentRole,
			EnumDlvPassStatus status);


	public FDReservation cancelOrder(FDActionInfo info, String saleId, boolean sendEmail, int currentDPExtendDays, boolean restoreReservation) throws FDResourceException, ErpTransactionException, DeliveryPassException, RemoteException;
	 

	public Map<String, FDAvailabilityI> checkAvailability(FDIdentity identity,	ErpCreateOrderModel createOrder, long timeout, String isFromLogin) throws FDResourceException;


	public void resubmitOrder(String saleId, CustomerRatingI cra,EnumSaleType saleType, String deliveryRegionId)throws ErpTransactionException, RemoteException;

	
			
}
