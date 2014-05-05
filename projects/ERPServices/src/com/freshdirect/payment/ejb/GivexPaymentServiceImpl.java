package com.freshdirect.payment.ejb;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;

import lcc.japi.trans;

import com.freshdirect.client.TransPortType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.payment.GivexResponseModel;
import com.givex.gapi.x1.x0.types_common.Identification;
import com.givex.gapi.x1.x0.types_trans.Balance;
import com.givex.gapi.x1.x0.types_trans.BalanceTransfer;
import com.givex.gapi.x1.x0.types_trans.BalanceTransferResponse;
import com.givex.gapi.x1.x0.types_trans.GetBalance;
import com.givex.gapi.x1.x0.types_trans.PostAuth;
import com.givex.gapi.x1.x0.types_trans.PostAuthResponse;
import com.givex.gapi.x1.x0.types_trans.PreAuth;
import com.givex.gapi.x1.x0.types_trans.PreAuthResponse;
import com.givex.gapi.x1.x0.types_trans.Register;
import com.givex.gapi.x1.x0.types_trans.RegisterResponse;

public final class GivexPaymentServiceImpl implements GivexPaymentService {
	
	private TransPortType transportType=null;
	


	private static GivexPaymentService givexPaymentService=null;
		
	private GivexPaymentServiceImpl(TransPortType transportType){
	   this.transportType=transportType;	
	}
	
	
	
	
	public  synchronized static GivexPaymentService getInstance(TransPortType transportType){
		if(givexPaymentService==null){ 
			if(transportType==null){
				throw new IllegalArgumentException("need to set TransPortType before using Givex Fervice");
			}
			givexPaymentService=new GivexPaymentServiceImpl(transportType);	
		}
		return givexPaymentService;
	}

	
	public Balance getBalance(Identification identification,
			ErpPaymentMethodI paymentMethod) throws RemoteException{
		// TODO Auto-generated method stub
		
		GetBalance balance=new GetBalance();
		balance.setGivexNumber(paymentMethod.getAccountNumber());
		balance.setCurrency("USD");
		balance.setId(identification);
	
		Balance res=transportType.getBalance(balance);
		
		return res;
	}

	
	public PostAuthResponse postAuthGiftCard(Identification identification,
			ErpPaymentMethodI paymentMethod, double amount, long authCode,String reference)
	throws RemoteException {
		// TODO Auto-generated method stub
		PostAuth pAuth=new PostAuth();
		//pAuth.setAmount(new BigDecimal(10.00));
		pAuth.setAmount(new BigDecimal(amount));
		pAuth.setId(identification);		
		pAuth.setReference(reference);
		pAuth.setGivexNumber(paymentMethod.getAccountNumber());
		pAuth.setAuthCode(authCode);
		PostAuthResponse authPostRes=transportType.postAuth(pAuth);			
		return authPostRes;
	}

	
	public PreAuthResponse preAuthGiftCard(Identification identification,
			ErpPaymentMethodI paymentMethod, double amount,String reference) throws RemoteException {
		// TODO Auto-generated method stub
		PreAuth auth=new PreAuth();
		auth.setAmount(new BigDecimal(amount));
		auth.setId(identification);
		//auth.setSecurityCode(securityCode);
		auth.setReference(reference);
		auth.setGivexNumber(paymentMethod.getAccountNumber());			
		PreAuthResponse authRes=transportType.preAuth(auth);
		return authRes;
	}

	
	public RegisterResponse registerCard(Identification identification,
			 double amount,String reference) throws RemoteException {
		// TODO Auto-generated method stub
		Register r=new Register();
		r.setId(identification);
		r.setReference(reference);
		r.setAmount(new BigDecimal(amount));
		RegisterResponse res=transportType.register(r);
		return res;
	}
	
	public TransPortType getTransportType() {
		return transportType;
	}


	public void setTransportType(TransPortType transportType) {
		this.transportType = transportType;
	}







	@Override
	public BalanceTransferResponse transferBalance(
			Identification identification, ErpPaymentMethodI paymentMethodFrom,
			ErpPaymentMethodI paymentMethodTo, double amount, String reference)
			throws RemoteException {
		// TODO Auto-generated method stub
		BalanceTransfer transfer=new BalanceTransfer();
		transfer.setGivexNumberFrom(paymentMethodFrom.getAccountNumber());
		transfer.setGivexNumberTo(paymentMethodTo.getAccountNumber());		
		transfer.setId(identification);
		transfer.setReference(reference);
		return transportType.balanceTransfer(transfer);
	}


}
