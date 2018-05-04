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

/**
 * 
 * @deprecated
 *
 */
public interface PaymentManagerSB extends EJBObject {
	
	@Deprecated
	public List<ErpAuthorizationModel> authorizeSaleRealtime(String saleId) throws ErpAuthorizationException, ErpAddressVerificationException, RemoteException;
	@Deprecated
	public EnumPaymentResponse authorizeSale(String saleId, boolean force) throws RemoteException;
	@Deprecated
	public void voidCapturesNoTrans(String saleId) throws ErpTransactionException, RemoteException;
	@Deprecated
	public void captureAuthorizations(String saleId, List<ErpAuthorizationModel> auths) throws ErpTransactionException, RemoteException;
	@Deprecated
	public List<ErpAuthorizationModel> authorizeSaleRealtime(String saleId,EnumSaleType saleType) throws ErpAuthorizationException, ErpAddressVerificationException, RemoteException;
	@Deprecated
	public boolean isValidVaultToken(String token, String customerId)throws RemoteException;

}