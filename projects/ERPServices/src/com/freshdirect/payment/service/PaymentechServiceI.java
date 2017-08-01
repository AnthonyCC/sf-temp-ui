package com.freshdirect.payment.service;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;

public interface PaymentechServiceI {
	public GatewayType getType(); 
	public Response verify(Request request) throws ErpTransactionException;
	public Response authorize(Request request) throws ErpTransactionException;
	public Response capture(Request request) throws ErpTransactionException;
	public Response voidCapture(Request request) throws ErpTransactionException;
	public Response reverseAuthorize(Request request) throws ErpTransactionException;
	public Response issueCashback(Request request) throws ErpTransactionException;
	public Response addProfile(Request request) throws ErpTransactionException;
	public Response updateProfile(Request request) throws ErpTransactionException;
	public Response deleteProfile(Request request) throws ErpTransactionException;
	public Response getProfile(Request request) throws ErpTransactionException;
	public boolean isValidToken(String token, String customerId);
	public Response processRequest(Request request) throws ErpTransactionException;
	public Response processProfileRequest(Request request)
			throws ErpTransactionException;
}
