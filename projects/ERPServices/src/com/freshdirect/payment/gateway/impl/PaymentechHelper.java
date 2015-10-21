package com.freshdirect.payment.gateway.impl;

import java.text.SimpleDateFormat;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.gateway.BankAccountType;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.ECheck;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;
import com.paymentech.orbital.sdk.interfaces.TransactionProcessorIF;
import com.paymentech.orbital.sdk.request.FieldNotFoundException;
import com.paymentech.orbital.sdk.request.RequestConstructionException;
import com.paymentech.orbital.sdk.transactionProcessor.TransactionException;
import com.paymentech.orbital.sdk.transactionProcessor.TransactionProcessor;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;


class PaymentechHelper {
	private static final SimpleDateFormat SF = new SimpleDateFormat("MMyy"); 
	
	 static ResponseIF processRequest(RequestIF request)throws InitializationException,TransactionException  {
			
			TransactionProcessorIF tp = new TransactionProcessor();
			ResponseIF processorResponse=tp.process(request);
			return processorResponse;
  	}
	 private static boolean isValid(Request request) {
		
		 if(request.getBillingInfo()==null)return false;
		 if(request.getBillingInfo().getPaymentMethod()==null)return false;
		 return true;
	 }
	    
		static RequestIF createProfileRequest(Request _request) throws FieldNotFoundException, InitializationException  {
            
			
			if(!isValid(_request)) return null;
			BillingInfo billingInfo=_request.getBillingInfo();
			PaymentMethod paymentMethod=billingInfo.getPaymentMethod();
			
			RequestIF request=new com.paymentech.orbital.sdk.request.Request(RequestIF.PROFILE_TRANSACTION);
			PaymentechRequestHelper.setMerchantID(RequestIF.PROFILE_TRANSACTION,request,billingInfo.getMerchant());
			PaymentechRequestHelper.setBIN(RequestIF.PROFILE_TRANSACTION,request);
			if(TransactionType.ADD_PROFILE.equals(_request.getTransactionType())||
			   TransactionType.UPDATE_PROFILE.equals(_request.getTransactionType())) {
				setPaymentMethodInfo(RequestIF.PROFILE_TRANSACTION,
						             paymentMethod,
						             request
						             );
			}
			PaymentechRequestHelper.setProfileTxInfo(_request.getTransactionType(),request,paymentMethod.getBillingProfileID());
			return request;
		}
		
		private static void setPaymentMethodInfo(String transaction,
				                                 PaymentMethod paymentMethod,
				                                 RequestIF request
				                                 )throws FieldNotFoundException, InitializationException   {
			if (PaymentMethodType.CREDIT_CARD.equals(paymentMethod.getType())) {
				
				CreditCard creditCard = (CreditCard) paymentMethod;
				PaymentechRequestHelper.setCCInfo(transaction,
						                          request,
						                          creditCard);
			} else if (PaymentMethodType.ECHECK.equals(paymentMethod.getType())) {
				
				ECheck echeck = (ECheck) paymentMethod;
				PaymentechRequestHelper.setECheckInfo(transaction,
                          request,
                          echeck);
			}
			PaymentechRequestHelper.setAddressInfo(transaction,request,paymentMethod);
		}
		
		 static Response setResponse(Request request,
				ResponseImpl response, RequestIF _request,
				ResponseIF _response) {
			 
			 TransactionType transType=request.getTransactionType();
			if(_response==null||response==null)
				return response;
			response.setRawRequest(getString(_request));
			response.setRawResponse(getString(_response));
			if(isProfileTx(transType)) {
				return setProfileResponse(response,_response);
			} else {
				response=(ResponseImpl)setNewOrderTxResponse(transType,response,_response);
			}
			return response;
			
		}
		 private static Response setNewOrderTxResponse(TransactionType transType,ResponseImpl response, ResponseIF _response) { 
			 
			    PaymentMethod pm=null;
				response.setBillingInfo(response.getRequest().getBillingInfo());
				if(_response==null)
					return response;
				
				String val=_response.getValue(PaymentechFields.NewOrderResponse.ProcStatus.name());
				response.setStatusMessage(_response.getValue(PaymentechFields.NewOrderResponse.StatusMsg.name()));
				response.setStatusCode(val);
				if(PaymentechConstants.SUCCESS.equals(val)) {
					response.setRequestProcessed(true);
					response.setAuthCode(_response.getAuthCode());
					response.getBillingInfo().setTransactionRef(_response.getTxRefNum()); 
					response.getBillingInfo().setTransactionRefIndex(_response.getValue(PaymentechFields.NewOrderResponse.TxRefIdx.name()));
					response.setApproved(_response.isApproved());
					response.setDeclined(_response.isDeclined());
					response.setError(_response.isError());
					response.setResponseCode(PaymentechResponseHelper.getResponseCode(_response));
					response.setResponseCodeAlt(PaymentechResponseHelper.getHostResponseCode(_response));
					val=_response.getAVSResponseCode();
					if(val!=null)val=val.trim();
					response.setAVSMatch(PaymentechResponseHelper.isZipMatch(val));
					response.setAVSResponse(val);
					pm=response.getPaymentMethod();
					if(pm!=null && PaymentMethodType.CREDIT_CARD.equals(pm.getType())) {
						CreditCard cc=(CreditCard)pm;
						CreditCardType ccType=PaymentechResponseHelper.getCardBrand(_response);
						if(ccType!=null)
							cc.setCreditCardType(ccType);
						val=_response.getValue(PaymentechFields.NewOrderResponse.AccountNum.name());
						if(val!=null && !val.trim().equals(""))
							cc.setAccountNumber(val);
						
						
						val=_response.getCVV2RespCode();
						response.setCVVMatch(PaymentechResponseHelper.isCVVMatch(val));
						response.setCVVResponse(val);
						
						// EWallet Id 
						response.setEwalletId(cc.getEwalletId());
						response.setEwalletTxId(cc.getEwalletTxId());
					} else if(pm!=null && PaymentMethodType.ECHECK.equals(pm.getType())) {
						ECheck ec=(ECheck)pm;
						
						val=_response.getValue(PaymentechFields.NewOrderResponse.CheckDDA.name());
						if(val!=null && !val.trim().equals(""))
							ec.setAccountNumber(val);
						
						
					}
				} else {
					response.setRequestProcessed(false);
				}
				response.setResponseTime(_response.getValue(PaymentechFields.NewOrderResponse.RespTime.name()));
				return response;
		 }
		
		
		private static boolean isProfileTx(TransactionType type) {
			return (TransactionType.ADD_PROFILE.equals(type)||
					TransactionType.GET_PROFILE.equals(type)||
					TransactionType.UPDATE_PROFILE.equals(type)||
					TransactionType.DELETE_PROFILE.equals(type));
		}
		
		
		private static Response setProfileResponse(ResponseImpl response, ResponseIF _response) {
		
			PaymentMethod pm=null;
			
			if(_response==null)
				return response;
			String val=_response.getValue(PaymentechFields.ProfileResponse.ProfileProcStatus.name());
			response.setStatusMessage(_response.getValue(PaymentechFields.ProfileResponse.CustomerProfileMessage.name()));
			response.setStatusCode(val);
			
			if(PaymentechConstants.SUCCESS.equals(val)) {
				response.setRequestProcessed(true);
				val=_response.getValue(PaymentechFields.ProfileResponse.CustomerAccountType.name());
				if(StringUtil.isEmpty(val)) {
					
					response.setBillingInfo(response.getRequest().getBillingInfo());
					pm=response.getBillingInfo().getPaymentMethod();
				}
				else if( PaymentechConstants.AccountType.CREDIT_CARD.getCode().equals(val)) {
					CreditCard cc=new CreditCardImpl();
					cc.setAccountNumber(_response.getValue(PaymentechFields.ProfileResponse.CCAccountNum.name()));
					//cc.setExpirationDate(null);//Set the expiration date.
					//How to set the credit card type??
					
					pm=cc;
					
				} else {
					ECheck ec=new ECheckImpl();
					ec.setAccountNumber(_response.getValue(PaymentechFields.ProfileResponse.ECPAccountDDA.name()));
					ec.setRoutingNumber(_response.getValue(PaymentechFields.ProfileResponse.ECPAccountRT.name()));
					val=_response.getValue(PaymentechFields.ProfileResponse.ECPAccountType.name());
					if(PaymentechConstants.BankAccountType.CONSUMER_CHECKING.getCode().equals(val)) 
						ec.setBankAccountType(com.freshdirect.payment.gateway.BankAccountType.CHECKING );
					else if(PaymentechConstants.BankAccountType.CONSUMER_SAVINGS.getCode().equals(val)) 
						ec.setBankAccountType(com.freshdirect.payment.gateway.BankAccountType.SAVINGS);
					pm=ec;
				}
				pm.setBillingProfileID(_response.getValue(PaymentechFields.ProfileResponse.CustomerRefNum.name()));
				pm.setCustomerID(response.getRequest().getBillingInfo().getPaymentMethod().getCustomerID());
				pm.setCustomerName(_response.getValue(PaymentechFields.ProfileResponse.CustomerName.name()));
				pm.setAddressLine1(_response.getValue(PaymentechFields.ProfileResponse.CustomerAddress1.name()));
				pm.setAddressLine2(_response.getValue(PaymentechFields.ProfileResponse.CustomerAddress2.name()));
				pm.setCity(_response.getValue(PaymentechFields.ProfileResponse.CustomerCity.name()));
				pm.setState(_response.getValue(PaymentechFields.ProfileResponse.CustomerState.name()));
				pm.setZipCode(_response.getValue(PaymentechFields.ProfileResponse.CustomerZIP.name()));
				response.setBillingInfo(new BillingInfoImpl(response.getMerchant(),pm));
				response.setEwalletId(response.getRequest().getBillingInfo().getPaymentMethod().getEwalletId());
				response.setEwalletTxId(response.getRequest().getBillingInfo().getPaymentMethod().getEwalletTxId());
				
			} else {
				response.setRequestProcessed(false);
			}
			response.setResponseTime(_response.getValue(PaymentechFields.ProfileResponse.RespTime.name()));
		    
			return  response;
		}

		static String getString(RequestIF _request) {
			String out="";
			if(_request!=null) {
				try {
					out=_request.getXML().replace("\t\t", "\n").toString();//change to getMaskedXML()
				} catch (InitializationException e) {
					
					e.printStackTrace();
				} catch (RequestConstructionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return out;
		}
		static String getString(ResponseIF _response) {
			String out="";
			if(_response!=null) {
				out=_response.toXmlString().replace("><", ">\n<");
			}
			return out;
		}

		

		
		static Response  processRequest(Request request) {
			ResponseImpl response=new ResponseImpl(request);
			RequestIF _request=getGatewayTxRequest(request,response);
			ResponseIF _response=null;
			try {
				_response=processRequest(_request);
			} catch (InitializationException e) {
				e.printStackTrace();
				setException(response,e.toString());
			} catch (TransactionException e) {
				e.printStackTrace();
				setException(response,e.toString());
			}
			response=(ResponseImpl)setResponse(request,response,_request,_response);
			
			return response;
		}
		

		private static RequestIF getGatewayTxRequest(Request request,ResponseImpl response) {
			
			RequestIF gatewayReq=null;
			try {
				if(TransactionType.CC_VERIFY.equals(request.getTransactionType())||
				   TransactionType.ACH_VERIFY.equals(request.getTransactionType())	)
					gatewayReq=createVerifyTxRequest(request);
				else if(TransactionType.AUTHORIZE.equals(request.getTransactionType()))
					gatewayReq=createAuthorizeTxRequest(request);
				else if(TransactionType.CAPTURE.equals(request.getTransactionType()))
					gatewayReq=createCaptureTxRequest(request);
				else if(TransactionType.VOID_CAPTURE.equals(request.getTransactionType()))
					gatewayReq=createVoidCaptureTxRequest(request);
				else if(TransactionType.REVERSE_AUTHORIZE.equals(request.getTransactionType()))
					gatewayReq=createReverseAuthTxRequest(request);
				else if(TransactionType.CASHBACK.equals(request.getTransactionType()))
					gatewayReq=createCashbackTxRequest(request);
			} catch (InitializationException e) {
				e.printStackTrace();
				setException(response,e.toString());
			} catch (FieldNotFoundException e) {
				setException(response,e.toString());
			} catch(Exception e) {
				e.printStackTrace();
				setException(response,e.toString());
			}		
			return gatewayReq;
		}
		private static RequestIF createAuthorizeTxRequest(Request request) throws 
			        InitializationException,
			        FieldNotFoundException {
			BillingInfo billingInfo=request.getBillingInfo();
			PaymentMethod paymentMethod=billingInfo.getPaymentMethod();
			RequestIF _request=new com.paymentech.orbital.sdk.request.Request(RequestIF.NEW_ORDER_TRANSACTION);
			
			_request.setFieldValue(PaymentechFields.NewOrderRequest.IndustryType.name(),
			PaymentechConstants.IndustryType.ECOMMERCE.getCode() );
			PaymentechRequestHelper.setMerchantID(RequestIF.NEW_ORDER_TRANSACTION,_request,billingInfo.getMerchant());
			PaymentechRequestHelper.setBIN(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setTerminalID(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setMessageType(request.getTransactionType(),_request);
			PaymentechRequestHelper.setOrderID(RequestIF.NEW_ORDER_TRANSACTION,billingInfo.getTransactionID(),_request);
			PaymentechRequestHelper.setCurrencyCode(RequestIF.NEW_ORDER_TRANSACTION,paymentMethod.getCurrency(),_request);
			PaymentechRequestHelper.setCurrencyExponent(_request);
			PaymentechRequestHelper.setAmount(request.getBillingInfo().getAmount(),_request);
			PaymentechRequestHelper.setProfileID(request.getTransactionType(), _request, paymentMethod.getBillingProfileID());
			return _request;
		}
		
		private static RequestIF createCashbackTxRequest(Request request) throws 
																	        InitializationException,
																	        FieldNotFoundException {
			BillingInfo billingInfo=request.getBillingInfo();
			PaymentMethod paymentMethod=billingInfo.getPaymentMethod();
			RequestIF _request=new com.paymentech.orbital.sdk.request.Request(RequestIF.NEW_ORDER_TRANSACTION);
			
			_request.setFieldValue(PaymentechFields.NewOrderRequest.IndustryType.name(),
								   PaymentechConstants.IndustryType.ECOMMERCE.getCode() );
			PaymentechRequestHelper.setMerchantID(RequestIF.NEW_ORDER_TRANSACTION,_request,billingInfo.getMerchant());
			PaymentechRequestHelper.setBIN(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setTerminalID(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setMessageType(request.getTransactionType(),_request);
			PaymentechRequestHelper.setOrderID(RequestIF.NEW_ORDER_TRANSACTION,billingInfo.getTransactionID(),_request);
			PaymentechRequestHelper.setCurrencyCode(RequestIF.NEW_ORDER_TRANSACTION,paymentMethod.getCurrency(),_request);
			PaymentechRequestHelper.setCurrencyExponent(_request);
			PaymentechRequestHelper.setAmount(request.getBillingInfo().getAmount(),_request);
			PaymentechRequestHelper.setProfileID(request.getTransactionType(), _request, paymentMethod.getBillingProfileID());
			return _request;
		}
		
		
		private static RequestIF createCaptureTxRequest(Request request) throws 
																        InitializationException,
			 
																        FieldNotFoundException {
			
			RequestIF _request=null;
			BillingInfo billingInfo=request.getBillingInfo();
			if(PaymentMethodType.ECHECK.equals(billingInfo.getPaymentMethod().getType())) {
				_request=createAuthorizeTxRequest(request);
				PaymentechRequestHelper.setMessageTypeForECheckCapture(_request);
				
			} else {
				 _request=new com.paymentech.orbital.sdk.request.Request(RequestIF.MARK_FOR_CAPTURE_TRANSACTION);
				PaymentechRequestHelper.setMerchantID(RequestIF.NEW_ORDER_TRANSACTION,_request,billingInfo.getMerchant());
				PaymentechRequestHelper.setBIN(RequestIF.NEW_ORDER_TRANSACTION,_request);
				PaymentechRequestHelper.setOrderID(RequestIF.NEW_ORDER_TRANSACTION,billingInfo.getTransactionID(),_request);
				PaymentechRequestHelper.setAmount(billingInfo.getAmount(),_request);
				PaymentechRequestHelper.setTxRefNum(billingInfo.getTransactionRef(), _request);
			}
			return _request;
		}
		private static RequestIF createVoidCaptureTxRequest(Request request) throws 
			        														InitializationException,
			        														FieldNotFoundException {
			BillingInfo billingInfo=request.getBillingInfo();
			RequestIF _request=new com.paymentech.orbital.sdk.request.Request(RequestIF.REVERSE_TRANSACTION);
			PaymentechRequestHelper.setMerchantID(RequestIF.NEW_ORDER_TRANSACTION,_request,billingInfo.getMerchant());
			PaymentechRequestHelper.setBIN(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setTerminalID(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setOrderID(RequestIF.NEW_ORDER_TRANSACTION,billingInfo.getTransactionID(),_request);
			PaymentechRequestHelper.setTxRefNum(billingInfo.getTransactionRef(), _request);
			PaymentechRequestHelper.setTxRefIdx(billingInfo.getTransactionRefIndex(), _request);
			
			PaymentechRequestHelper.setOnlineReverseIndicator(PaymentechConstants.VOID_INDICATOR, _request);
			return _request;
		}
		private static RequestIF createReverseAuthTxRequest(Request request) throws 
																				InitializationException,
																				FieldNotFoundException {
			BillingInfo billingInfo=request.getBillingInfo();
			RequestIF _request=new com.paymentech.orbital.sdk.request.Request(RequestIF.REVERSE_TRANSACTION);
			PaymentechRequestHelper.setMerchantID(RequestIF.NEW_ORDER_TRANSACTION,_request,billingInfo.getMerchant());
			PaymentechRequestHelper.setBIN(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setOrderID(RequestIF.NEW_ORDER_TRANSACTION,billingInfo.getTransactionID(),_request);
			//PaymentechRequestHelper.setAmount(billingInfo.getAmount(),_request);
			PaymentechRequestHelper.setTxRefNum(billingInfo.getTransactionRef(), _request);
			PaymentechRequestHelper.setTxRefIdx(billingInfo.getTransactionRefIndex(), _request);
			PaymentechRequestHelper.setOnlineReverseIndicator(PaymentechConstants.REVERSE_AUTH_INDICATOR, _request);
			return _request;
		}		
		private static RequestIF createVerifyTxRequest(Request request) throws 
															          InitializationException,
															          FieldNotFoundException {
			BillingInfo billingInfo=request.getBillingInfo();
			PaymentMethod paymentMethod=billingInfo.getPaymentMethod();
			RequestIF _request=new com.paymentech.orbital.sdk.request.Request(RequestIF.NEW_ORDER_TRANSACTION);
			
			_request.setFieldValue(PaymentechFields.NewOrderRequest.IndustryType.name(),
					               PaymentechConstants.IndustryType.ECOMMERCE.getCode() );
			PaymentechRequestHelper.setMerchantID(RequestIF.NEW_ORDER_TRANSACTION,_request,billingInfo.getMerchant());
			PaymentechRequestHelper.setBIN(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setTerminalID(RequestIF.NEW_ORDER_TRANSACTION,_request);
			PaymentechRequestHelper.setMessageType(request.getTransactionType(),_request);
			PaymentechRequestHelper.setOrderID(RequestIF.NEW_ORDER_TRANSACTION,billingInfo.getTransactionID(),_request);
			PaymentechRequestHelper.setCurrencyCode(RequestIF.NEW_ORDER_TRANSACTION,paymentMethod.getCurrency(),_request);
			PaymentechRequestHelper.setCurrencyExponent(_request);
			PaymentechRequestHelper.setProfileID(request.getTransactionType(), _request, paymentMethod.getBillingProfileID());
            double amount=0;
			if (PaymentMethodType.CREDIT_CARD.equals(paymentMethod.getType())) {
				
				CreditCard creditCard = (CreditCard) paymentMethod;
				PaymentechRequestHelper.setCCInfo(RequestIF.NEW_ORDER_TRANSACTION,
						                          _request,
						                          creditCard);
				if( CreditCardType.AMEX.equals(creditCard.getCreditCardType())||
				    CreditCardType.DISCOVER.equals(creditCard.getCreditCardType())||
				    CreditCardType.MASTERCARD.equals(creditCard.getCreditCardType())
				  ) {
					amount=0.01;
					request.getBillingInfo().setAmount(amount);
				}
				
			} else if (PaymentMethodType.ECHECK.equals(billingInfo.getPaymentMethod().getType())) {
				
				ECheck echeck = (ECheck) paymentMethod;
				PaymentechRequestHelper.setECheckInfo(RequestIF.NEW_ORDER_TRANSACTION,
                          							  _request,
                          							  echeck);
			}
			PaymentechRequestHelper.setAmount(amount,_request);
			PaymentechRequestHelper.setAddressInfo(RequestIF.NEW_ORDER_TRANSACTION,_request,paymentMethod);
   		return _request;
		}
		private static Response setException(ResponseImpl response, String exceptionMsg) {
			response.setRequestProcessed(false);
			response.setStatusCode("ERROR");
			response.setStatusMessage(exceptionMsg);
			if(null==response.getBillingInfo()) {
				response.setBillingInfo(response.getRequest().getBillingInfo());
			}
			
			return response;
		}
}
