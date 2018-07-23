package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.sms.shortsubstitute.ShortSubstituteResponse;
import com.freshdirect.framework.core.PrimaryKey;

public interface CustomersApiClientI {
	
 enum EndPoints {
		
		PAYMENT_METHODS("customers/{customerId}/payment-methods"),
		STATUS("customers/{customerId}/status"),
		CUSTOMER_ID_FOR_USER("customers?userId="),
		DEFAULT_PAYMENT_METHOD_ID_FOR_FDCUSTOMER("fdCustomers/{fdCustomerId}/defaultPaymentMethodId"),
		DEFAULT_PAYMENT_METHOD_FOR_FDCUSTOMER("fdCustomers/{fdCustomerId}/defaultPaymentMethod"),
		PAYMENT_METHOD_DEFAULT_TYPE_FOR_FDCUSTOMER("fdCustomers/{fdCustomerId}/paymentMethodDefaultType"),
		DEFAULT_SHIPPING_ADDRESS_ID_FOR_FDCUSTOMER("fdCustomers/{fdCustomerId}/defaultShippingAddressId"),
		PROFILES("fdCustomers/{fdCustomerId}/profiles"),
		PROFILES_FOR_CUSTOMER("customers/{customerId}/profiles"),
		PROMOTION_HISTORY("customers/{customerId}/promotion-history"),
		GET_ORDERS_BY_SALESTATUS_STORE_API("orders/noncos/last/salestatus/store"),
		GET_SS_ORDERS("orders/ssOrders"),
		GET_ORDER_IS_READY_TO_PICK("orders/isReadyforPick/{saleId}"),
		GET_ORDER_RELEASE_MODIFICATION_LOCK("orders/releaseModificationLock/{saleId}"),
		GET_ORDERS_BY_SALETYPE_STORE_API("orders/noncos/last/salestype/store"),
		PASS_REQUEST_STATUS("customers/isPassRequestExpired?emailId={emailId}&passRequestId={passRequestId}");
		
		private String value;
		
		private EndPoints(final String value) {
			this.value=value;
		}
		
		public String getValue() {
			
			return value;
		}
	}
	
	//public Collection<ErpPaymentMethodI> getPaymentMethods(String customerId) throws FDResourceException;
	public boolean isActive(String customerId) throws FDResourceException;
	
	public String getCustomerIdForUserId(String userId) throws FDResourceException;
	
	//public Collection<ErpPaymentMethodI> getPaymentMethods(FDIdentity identity) throws FDResourceException, RemoteException;
	
	public String setActive(String customerId) throws FDResourceException;
	
	public String setInActive(String customerId) throws FDResourceException;
	
	public String getDefaultPaymentMethodId(String fdCustomerId) throws FDResourceException;
	
	public String getPaymentMethodDefaultType(String fdCustomerId) throws FDResourceException;
	
	public String getDefaultShippingAddressId(String fdCustomerId) throws FDResourceException;

	public ProfileModel getProfile(String fdCustomerId) throws FDResourceException;
	
	public ErpPromotionHistory getPromotionHistory(String customerId) throws FDResourceException;

	public ProfileModel getProfileForCustomerId(String customerId)	throws FDResourceException;
	
	public void setDefaultPaymentMethod(FDActionInfo info, PrimaryKey paymentMethodPK, EnumPaymentMethodDefaultType type, boolean isDebitCardSwitch) throws FDResourceException,RemoteException;

	public FDOrderI getLastNonCOSOrder(String customerID, EnumSaleType saleType,		EnumSaleStatus saleStatus, EnumEStoreId storeId)		throws ErpSaleNotFoundException, RemoteException;

	public FDOrderI getLastNonCOSOrder(String customerID,	EnumSaleType saleType, EnumEStoreId eStore)throws FDResourceException, ErpSaleNotFoundException, RemoteException;

	public ShortSubstituteResponse getShortSubstituteOrders(	List<String> orderList)throws FDResourceException, RemoteException;

	public boolean isReadyForPick(String orderNum)throws FDResourceException, RemoteException;

	public void releaseModificationLock(String orderId)throws FDResourceException, RemoteException;
	
	
	
	

}
