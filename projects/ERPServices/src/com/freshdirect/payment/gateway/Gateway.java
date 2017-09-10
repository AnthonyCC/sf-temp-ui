package com.freshdirect.payment.gateway;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpVoidCaptureModel;

public interface Gateway  extends java.io.Serializable{
	
	public GatewayType getType(); 
	public ErpAuthorizationModel verify(String merchantId,ErpPaymentMethodI paymentMethod) throws ErpTransactionException;
	public ErpAuthorizationModel authorize(ErpPaymentMethodI paymentMethod, String orderNumber,
			double authorizationAmount, double tax, String merchantId) throws ErpTransactionException;
	public ErpCaptureModel capture(ErpAuthorizationModel authorization, ErpPaymentMethodI paymentMethod, 
			double amount, double tax, String saleId) throws ErpTransactionException;
	public ErpVoidCaptureModel voidCapture(ErpPaymentMethodI paymentMethod, ErpCaptureModel request) throws ErpTransactionException;
	public Response reverseAuthorize(Request request) throws ErpTransactionException;
	public ErpCashbackModel issueCashback(String orderNumber, ErpPaymentMethodI paymentMethod, 
			double amount, double tax, ErpAffiliate affiliate) throws ErpTransactionException;
	public Response addProfile(Request request) throws ErpTransactionException;
	public Response updateProfile(Request request) throws ErpTransactionException;
	public Response deleteProfile(Request request) throws ErpTransactionException;
	public Response getProfile(Request request) throws ErpTransactionException;
	
	public boolean isValidToken(String token, String customerId);
	public Response voidCapture(Request _request)throws ErpTransactionException;
	
	
}
