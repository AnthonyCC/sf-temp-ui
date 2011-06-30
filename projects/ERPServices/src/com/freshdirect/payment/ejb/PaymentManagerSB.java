package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;

public interface PaymentManagerSB extends EJBObject {
	
	public List<ErpAuthorizationModel> authorizeSaleRealtime(String saleId) throws ErpAuthorizationException, ErpAddressVerificationException, RemoteException;
	
	public EnumPaymentResponse authorizeSale(String saleId) throws RemoteException;
	
	public void voidCapturesNoTrans(String saleId) throws ErpTransactionException, RemoteException;
	
	public void captureAuthorizations(String saleId, List<ErpAuthorizationModel> auths) throws ErpTransactionException, RemoteException;
	
	public List<ErpAuthorizationModel> authorizeSaleRealtime(String saleId,EnumSaleType saleType) throws ErpAuthorizationException, ErpAddressVerificationException, RemoteException;
	
	public ErpAuthorizationModel verify(ErpPaymentMethodI paymentMethod)throws ErpTransactionException, RemoteException;

}