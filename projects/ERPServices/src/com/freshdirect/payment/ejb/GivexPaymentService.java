package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;

import com.freshdirect.client.TransPortType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.givex.gapi.x1.x0.types_common.Identification;
import com.givex.gapi.x1.x0.types_trans.Balance;
import com.givex.gapi.x1.x0.types_trans.BalanceTransferResponse;
import com.givex.gapi.x1.x0.types_trans.PostAuthResponse;
import com.givex.gapi.x1.x0.types_trans.PreAuthResponse;
import com.givex.gapi.x1.x0.types_trans.RegisterResponse;



public interface GivexPaymentService {
	
	public  RegisterResponse registerCard(Identification identification, double amount,String reference) throws RemoteException;
	
	public  Balance getBalance(Identification identification, ErpPaymentMethodI paymentMethod) throws RemoteException;
		
	public PreAuthResponse preAuthGiftCard(Identification identification, ErpPaymentMethodI paymentMethod,double amount,String reference) throws RemoteException;
	
	public PostAuthResponse postAuthGiftCard(Identification identification, ErpPaymentMethodI paymentMethod,double amount,long authCode,String reference) throws RemoteException;
	
	public BalanceTransferResponse transferBalance(Identification identification, ErpPaymentMethodI paymentMethodFrom,ErpPaymentMethodI paymentMethodTo,double amount,String reference) throws RemoteException;
			
	public void setTransportType(TransPortType transportType);
}
