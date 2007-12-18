package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpTransactionException;

public interface PaymentManagerSB extends EJBObject {
	
	public List authorizeSaleRealtime(String saleId) throws ErpAuthorizationException, RemoteException;
	
	public EnumPaymentResponse authorizeSale(String saleId) throws RemoteException;
	
	public void voidCapturesNoTrans(String saleId) throws ErpTransactionException, RemoteException;
	
	public void captureAuthorizations(String saleId, List auths) throws ErpTransactionException, RemoteException;

}
