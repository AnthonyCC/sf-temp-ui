package com.freshdirect.payment.service;

import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.PayPalAccount;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.payment.PayPalResponse;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;

public interface IPayPalService {
	public GatewayType getType(); 
	public PayPalResponse authorize(ErpPaymentMethodI paymentMethod, String orderNumber,
			double authorizationAmount, double tax, String merchantId, String merchentMailId) throws FDPayPalServiceException ;
	public PayPalResponse capture(ErpAuthorizationModel authorization, ErpPaymentMethodI paymentMethod, 
			double amount, double tax, String saleId)  ;
	public PayPalResponse voidCapture(String txId) ;
	public PayPalResponse issueCashback(String orderNumber,ErpPaymentMethodI paymentMethod, double amount, double tax) ;
	public Response addProfile(Request request) ;
	public Response updateProfile(Request request) ;
	public Response deleteProfile(Request request) ;
	public Response getProfile(Request request) ;
	
	public PayPalResponse findToken(String token, String customerId)throws FDPayPalServiceException;
	public PayPalResponse createCustomer(String customerId, String fName, String lName) throws FDPayPalServiceException;
	public String generateToken() throws FDPayPalServiceException;
	public PayPalResponse createPaymentMethod(String customerId,String paymentMethodNonce,String deviceId) throws FDPayPalServiceException;
	public PayPalResponse reverseAuthorise(String taxId);

}
