package com.freshdirect.payment.ejb;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.client.GapiTrans_Impl;
import com.freshdirect.client.TransPortType;
import com.freshdirect.client.TransPortType_Stub;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.payment.EnumGivexErrorType;
import com.freshdirect.payment.GivexException;
import com.freshdirect.payment.GivexResponseModel;
import com.givex.gapi.x1.x0.types_common.Identification;
import com.givex.gapi.x1.x0.types_trans.Balance;
import com.givex.gapi.x1.x0.types_trans.BalanceTransferResponse;
import com.givex.gapi.x1.x0.types_trans.GetBalance;
import com.givex.gapi.x1.x0.types_trans.PostAuthResponse;
import com.givex.gapi.x1.x0.types_trans.PreAuthResponse;
import com.givex.gapi.x1.x0.types_trans.Register;
import com.givex.gapi.x1.x0.types_trans.RegisterResponse;
import com.givex.gapi.x1.x0.types_trans.Reversal;
import com.givex.gapi.x1.x0.types_trans.ReversalResponse;

public class GivexServerGateway {
	
	private static final Category LOGGER = LoggerFactory.getInstance( GivexServerGateway.class );
	private static final SimpleDateFormat SF = new SimpleDateFormat("MMyy");	
	private static final String GIVEX_FD_TOKEN = ErpServicesProperties.getFDGivexToken();
	private static final String GIVEX_FD_USER = ErpServicesProperties.getFDGivexUser(); // "Chase" for test purposes only
	private static final String GIVEX_FD_USER_PASSWD = ErpServicesProperties.getFDGivexUserPassword(); //to take into account the results of AVS CHECK
	private static final String GIVEX_SERVER_URL= ErpServicesProperties.getGivexServerURL();
	private static final String GIVEX_SERVER_BACKUP_URL= ErpServicesProperties.getGivexServerSecondaryURL();
	private static final int GIVEX_TRAN_TIMEOUT_SECS= ErpServicesProperties.getGivexTransactionTimeOut();
	
	private static TransPortType transport_type=null ;
	private static  Identification identification= null;
	private static GivexPaymentService paymentService=null;
	
	
	static{				
		try {
			transport_type=getTransportTypeProxy();			
			paymentService=GivexPaymentServiceImpl.getInstance(transport_type);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
	}
	
	
	public static synchronized Identification getIdentification(){
		
		if(identification==null){
			identification=new Identification();
			identification.setToken(GIVEX_FD_TOKEN);
			identification.setUser(GIVEX_FD_USER);
			identification.setUserPasswd(GIVEX_FD_USER_PASSWD);		
			identification.setLanguage("en");					
		}
		
		return identification;
	}
	
	
	public static GivexResponseModel convertGivexResponseModel(Object obj){
		
		GivexResponseModel model=new GivexResponseModel();
		if(obj instanceof RegisterResponse){
			RegisterResponse res=(RegisterResponse)obj;
			model.setAuthCode(res.getAuthCode());
			model.setCertBalance(res.getCertBalance().doubleValue());
			//model.setExpiryDate()
			model.setGivexNumber(res.getGivexNumber());
			model.setSecurityCode(res.getSecurityCode());
			if(res.getCertBalance()!=null)
			  model.setAmount(res.getCertBalance().doubleValue());
		}
		else if(obj instanceof Balance){
			Balance res=(Balance)obj;
			//model.setAuthCode(res.getAuthCode());
			model.setCertBalance(res.getCertBalance().doubleValue());
			//model.setExpiryDate()
			//model.setGivexNumber(res.getGivexNumber());
			model.setSecurityCode(res.getSecurityCode());			
		}
		else if(obj instanceof PreAuthResponse){
			PreAuthResponse res=(PreAuthResponse)obj;
			model.setAuthCode(res.getAuthCode());
			model.setCertBalance(res.getCertBalance().doubleValue());
			//model.setExpiryDate()
			model.setAmount(res.getAmount().doubleValue());				
		}
		else if(obj instanceof PostAuthResponse){
			PostAuthResponse res=(PostAuthResponse)obj;
			model.setAuthCode(res.getAuthCode());
			model.setCertBalance(res.getCertBalance().doubleValue());
			//model.setExpiryDate()
			model.setAmount(res.getAmount().doubleValue());		    
		}
		else if(obj instanceof BalanceTransferResponse){
			BalanceTransferResponse res=(BalanceTransferResponse)obj;
			//model.setAuthCode(res.getAuthCode());			
			model.setCertBalance(res.getCertBalance().doubleValue());
			//model.setExpiryDate()			
			model.setAuthCode(res.getCertAuthCodeFrom());			
			res.getCertAuthCodeTo();
			model.setCertBalance(res.getCertBalance().doubleValue());
			model.setAmount(res.getCertBalance().doubleValue());		    
		}
		

		return model;
	}
	public static GivexResponseModel registerCard(double amount,String reference) throws GivexException{
		
		Identification id=getIdentification();						
		Register r=new Register();
		r.setId(id);
		r.setReference(reference);
		r.setAmount(new BigDecimal(amount));				
		RegisterResponse res=null;
		try {
			res = paymentService.registerCard(id,amount,reference);
		} catch(RemoteException e){
			LOGGER.debug(" GivexServerver Gateway caught RemoteException while registerCard() :"+e.getMessage());
             EnumGivexErrorType errorType=EnumGivexErrorType.getErrorTypeFromMessage(e.getMessage());
             
				// dont mind add some more error desc please
                String extraDesc="";
                String message=e.getMessage();				
				int extraLength=message.indexOf(errorType.getDescription());
				if(extraLength==-1) extraLength=message.length();
				if(extraLength>90) extraLength=90; 				
				extraDesc=message.substring(0, extraLength);									             
             
             if(EnumGivexErrorType.ERROR_TIME_OUT==errorType){
            	 Reversal rev=new Reversal();
            	 rev.setAmount(new BigDecimal(amount));
            	 //r.setGivexNumber("60362879471161375433");
            	 rev.setId(id);
            	 rev.setReference(reference);
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);            	 
            	 port._setProperty("weblogic.webservice.rpc.timeoutsecs", ""+GIVEX_TRAN_TIMEOUT_SECS /* secs */);
            	 paymentService.setTransportType(port);
            	 ReversalResponse resp;
				try {
					resp = port.reversal(rev);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					// hey can't to much pal 
					EnumGivexErrorType errorType1=EnumGivexErrorType.getErrorTypeFromMessage(e1.getMessage()); 
					throw new GivexException(errorType1.getInfo()+extraDesc, errorType1.getErrorCode());
					
				}			            	 
				throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
             }
             if(EnumGivexErrorType.ERROR_FAIL_OVER==errorType){
            	 LOGGER.debug(" ERROR_FAIL_OVER transaction :");                  	             	             	 
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);
            	 String address=(String)port._getProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY);
            	 if(address!=null && GIVEX_SERVER_BACKUP_URL.equalsIgnoreCase(address)){
            		// second port failed so cant do much
            		 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
            	 }            		
            	 port._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, GIVEX_SERVER_BACKUP_URL);            	             	
            	 return registerCard(amount,reference);            	 
             }else{
            	 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
             }
		} 
		return convertGivexResponseModel(res);		
	}
	
	
	public static GivexResponseModel transferBalance(ErpPaymentMethodI fromMethod,ErpPaymentMethodI toMethod,double amount,String reference) throws IOException, GivexException{
		Identification id=getIdentification();
		BalanceTransferResponse res=null;
		try {			
			res = paymentService.transferBalance(id, fromMethod, toMethod, amount,reference);
		} catch(RemoteException e){
			LOGGER.debug(" GivexServerver Gateway caught RemoteException while transferBalance() : :"+e.getMessage());
             //e.printStackTrace();
             EnumGivexErrorType errorType=EnumGivexErrorType.getErrorTypeFromMessage(e.getMessage());
         	// dont mind add some more error desc please
                String extraDesc="";
                String message=e.getMessage();				
				int extraLength=message.indexOf(errorType.getDescription());
				if(extraLength==-1) extraLength=message.length();
				if(extraLength>90) extraLength=90; 
				extraDesc=message.substring(0, extraLength);	
             
                         
             if(EnumGivexErrorType.ERROR_FAIL_OVER==errorType){
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);
            	 String address=(String)port._getProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY);
            	 if(address!=null && GIVEX_SERVER_BACKUP_URL.equalsIgnoreCase(address)){
            		// second port failed so cant do much
            		 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
            	 }            		
            	 port._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, GIVEX_SERVER_BACKUP_URL);            	             	
            	 return transferBalance(fromMethod, toMethod, amount,reference);    	 
             }
             else{
            	 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
             }
		}   
             return convertGivexResponseModel(res);
	}
	
	
	
	public static GivexResponseModel getBalance(ErpPaymentMethodI paymentMethod) throws IOException, GivexException{
		Identification id=getIdentification();			
		Balance res=null;				
		try {
			res = paymentService.getBalance(id,paymentMethod);
		} catch(RemoteException e){
			LOGGER.debug(" GivexServerver Gateway caught RemoteException while getBalance() : :"+e.getMessage());
			System.out.println("Error message $$$$$$$$$$$$ "+e.getMessage());
             //e.printStackTrace();
             EnumGivexErrorType errorType=EnumGivexErrorType.getErrorTypeFromMessage(e.getMessage()); 
         	// dont mind add some more error desc please
             String extraDesc="";
             String message=e.getMessage();				
				int extraLength=message.indexOf(errorType.getDescription());
				if(extraLength==-1) extraLength=message.length();
				if(extraLength>90) extraLength=90;				
				extraDesc=message.substring(0, extraLength);	
				
             if(EnumGivexErrorType.ERROR_FAIL_OVER==errorType){
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);
            	 String address=(String)port._getProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY);
            	 if(address!=null && GIVEX_SERVER_BACKUP_URL.equalsIgnoreCase(address)){
            		// second port failed so cant do much
            		 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
            	 }            		
            	 port._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, GIVEX_SERVER_BACKUP_URL);            	             	
            	 return getBalance(paymentMethod);            	 
             }
             else{
            	 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
             }
		} catch(Exception ex) {
			System.out.println("Exception Caught getBalance $$$$$$$$$$$$$$$$ "+ex.getMessage());
			ex.printStackTrace();
		}  
             return convertGivexResponseModel(res);
	}
	
	
	public static GivexResponseModel preAuthGiftCard(ErpPaymentMethodI paymentMethod,double amount,String reference) throws GivexException{
		
		Identification id=getIdentification();						
		PreAuthResponse res=null;
		try {
			res = paymentService.preAuthGiftCard(id, paymentMethod, amount, reference);
		} catch(RemoteException e){
			LOGGER.debug(" GivexServerver Gateway caught RemoteException while preAuthGiftCard() : :"+e.getMessage());
            //e.printStackTrace();
            EnumGivexErrorType errorType=EnumGivexErrorType.getErrorTypeFromMessage(e.getMessage()); 
        	// dont mind add some more error desc please
            String extraDesc="";
            String message=e.getMessage();				
			int extraLength=message.indexOf(errorType.getDescription());
			if(extraLength==-1) extraLength=message.length();
			if(extraLength>90) extraLength=90; 
			extraDesc=message.substring(0, extraLength);
			
             if(EnumGivexErrorType.ERROR_TIME_OUT==errorType){
            	 LOGGER.debug(" reverse transaction :");
            	 Reversal rev=new Reversal();
            	 rev.setAmount(new BigDecimal(amount));
            	 rev.setGivexNumber(paymentMethod.getAccountNumber());
            	 rev.setId(id);
            	 rev.setReference(reference);
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);            	 
            	 port._setProperty("weblogic.webservice.rpc.timeoutsecs", ""+GIVEX_TRAN_TIMEOUT_SECS /* secs */);
            	 paymentService.setTransportType(port);
            	 ReversalResponse resp;
				try {
					resp = port.reversal(rev);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					// hey can't to much pal 
					EnumGivexErrorType errorType1=EnumGivexErrorType.getErrorTypeFromMessage(e1.getMessage()); 
					throw new GivexException(errorType1.getInfo()+extraDesc, errorType1.getErrorCode());
				}			            	 
				throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
             }
             if(EnumGivexErrorType.ERROR_FAIL_OVER==errorType){
            	 LOGGER.debug(" ERROR_FAIL_OVER transaction :");                  	             	             	 
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);
            	 String address=(String)port._getProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY);
            	 if(address!=null && GIVEX_SERVER_BACKUP_URL.equalsIgnoreCase(address)){
            		// second port failed so cant do much
            		 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
            	 }            		
            	 port._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, GIVEX_SERVER_BACKUP_URL);            	             	
            	 return preAuthGiftCard(paymentMethod, amount,reference);            	 
             }else{
            	 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
             }
		} 
		return convertGivexResponseModel(res);				
	}
	
	
	public static GivexResponseModel postAuthGiftCard(ErpPaymentMethodI paymentMethod,double amount,long authCode,String reference) throws GivexException{
		
		Identification id=getIdentification();								
		PostAuthResponse res=null;
		try {
			res = paymentService.postAuthGiftCard(id, paymentMethod, amount, authCode, reference);
		} catch(RemoteException e){
			LOGGER.debug(" GivexServerver Gateway caught RemoteException while postAuthGiftCard() : :"+e.getMessage());
			 System.out.println("Error message $$$$$$$$$$$$ "+e.getMessage());
             //e.printStackTrace();
             EnumGivexErrorType errorType=EnumGivexErrorType.getErrorTypeFromMessage(e.getMessage()); 
         	// dont mind add some more error desc please
             String extraDesc="";
             String message=e.getMessage();				
			 int extraLength=message.indexOf(errorType.getDescription());
			 if(extraLength==-1) extraLength=message.length();
			 if(extraLength>90) extraLength=90; 
			 extraDesc=message.substring(0, extraLength);	
				
             if(EnumGivexErrorType.ERROR_TIME_OUT==errorType){
            	 Reversal rev=new Reversal();
            	 rev.setAmount(new BigDecimal(amount));
            	 rev.setGivexNumber(paymentMethod.getAccountNumber());
            	 rev.setId(id);
            	 rev.setReference(reference);
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);            	 
            	 port._setProperty("weblogic.webservice.rpc.timeoutsecs", ""+GIVEX_TRAN_TIMEOUT_SECS /* secs */);
            	 paymentService.setTransportType(port);
            	 ReversalResponse resp;
				try {
					resp = port.reversal(rev);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					// hey can't to much pal 
					EnumGivexErrorType errorType1=EnumGivexErrorType.getErrorTypeFromMessage(e1.getMessage()); 
					throw new GivexException(errorType1.getInfo()+extraDesc, errorType1.getErrorCode());
				}			            	 
				throw new GivexException(errorType.getInfo(), errorType.getErrorCode());
             }
             if(EnumGivexErrorType.ERROR_FAIL_OVER==errorType){
            	 LOGGER.debug(" ERROR_FAIL_OVER transaction :");                  	             	             	 
            	 TransPortType_Stub port= (TransPortType_Stub)(transport_type);
            	 String address=(String)port._getProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY);
            	 if(address!=null && GIVEX_SERVER_BACKUP_URL.equalsIgnoreCase(address)){
            		// second port failed so cant do much
            		 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
            	 }            		
            	 port._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, GIVEX_SERVER_BACKUP_URL);            	             	
            	 return postAuthGiftCard(paymentMethod, amount, authCode,reference);            	 
             }else{
            	 throw new GivexException(errorType.getInfo()+extraDesc, errorType.getErrorCode());
             }
		} 
		return convertGivexResponseModel(res);
	}
	
	public static GivexResponseModel cancelPreAuthorization(ErpPaymentMethodI paymentMethod,long authCode,String reference) throws GivexException{
	
	    return postAuthGiftCard(paymentMethod, 0, authCode,reference);  									
	
	}
								
	
	public static synchronized TransPortType getTransportTypeProxy() throws IOException{
		if(transport_type==null)
			transport_type=new GapiTrans_Impl().gettransPortType();
		
		
//		GapiTrans_Impl impl=new GapiTrans_Impl();
		TransPortType_Stub port= (TransPortType_Stub)(transport_type);
		port._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, GIVEX_SERVER_URL);
		port._setProperty("weblogic.webservice.rpc.timeoutsecs", ""+GIVEX_TRAN_TIMEOUT_SECS /* secs */);
		
	    return port;	
	}
	
	 
	public static EnumGivexErrorType getGivexErrorType(String message){
		
		if(message!=null && message.trim().length()>0){
			
			if(message.indexOf(EnumGivexErrorType.ERROR_FAIL_OVER.getName())!=-1){
				return EnumGivexErrorType.ERROR_FAIL_OVER;
			}else if(message.indexOf(EnumGivexErrorType.ERROR_TIME_OUT.getName())!=-1)
			{
				return EnumGivexErrorType.ERROR_TIME_OUT;
			}
			return EnumGivexErrorType.ERROR_GENERIC;
		}	
		return null;
	}
	
	
	public static void main(String args[]) throws FDResourceException, IOException{
		
		try{
			//GivexResponseModel response = registerCard(5.0, "timeOutTest2");
			//System.out.println("New Card Number "+response.getGivexNumber());
			//System.out.println("Auth Code: "+response.getAuthCode());
			ErpGiftCardModel pm = new ErpGiftCardModel();
			pm.setAccountNumber("60362847331161464760");
			System.out.println("Balance on new Card "+getBalance(pm).getCertBalance());			
		}catch(GivexException ge) {
			System.out.println("Error code "+ge.getErrorCode());
			System.out.println("Error Message "+ge.getMessage());
		}catch(Exception ex) {
			System.out.println("Exception Caught $$$$$$$$$$$$$$$$ ");
			ex.printStackTrace();
		}
	}

}
