package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ProfileModel;

public interface CustomersApiClientI {
	
 enum EndPoints {
		
		PAYMENT_METHODS("customers/{customerId}/payment-methods"),
		STATUS("customers/{customerId}/status"),
		CUSTOMER_ID_FOR_USER("customers?userId="),
		DEFAULT_PAYMENT_METHOD_ID_FOR_FDCUSTOMER("fdCustomers/{fdCustomerId}/defaultPaymentMethodId"),
		PAYMENT_METHOD_DEFAULT_TYPE_FOR_FDCUSTOMER("fdCustomers/{fdCustomerId}/paymentMethodDefaultType"),
		DEFAULT_SHIPPING_ADDRESS_ID_FOR_FDCUSTOMER("fdCustomers/{fdCustomerId}/defaultShippingAddressId"),
		PROFILES("fdCustomers/{fdCustomerId}/profiles"),
		PROMOTION_HISTORY("customers/{customerId}/promotion-history"),
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
	public String isActive(String customerId) throws FDResourceException;
	
	public String getCustomerIdForUserId(String userId) throws FDResourceException;
	
	//public Collection<ErpPaymentMethodI> getPaymentMethods(FDIdentity identity) throws FDResourceException, RemoteException;
	
	public String setActive(String customerId) throws FDResourceException;
	
	public String setInActive(String customerId) throws FDResourceException;
	
	public String getDefaultPaymentMethodId(String fdCustomerId) throws FDResourceException;
	
	public String getPaymentMethodDefaultType(String fdCustomerId) throws FDResourceException;
	
	public String getDefaultShippingAddressId(String fdCustomerId) throws FDResourceException;

	public ProfileModel getProfile(String fdCustomerId) throws FDResourceException;
	
	public ErpPromotionHistory getPromotionHistory(String customerId) throws FDResourceException;
	
	

}
