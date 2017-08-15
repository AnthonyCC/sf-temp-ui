package com.freshdirect.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;


import com.braintreegateway.Transaction;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumCVVResponse;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpReversalModel;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.gateway.BankAccountType;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.ECheck;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.impl.*;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;


public class GatewayAdapter {
	private static final Category LOGGER = LoggerFactory.getInstance( GatewayAdapter.class );
	private static final String PP_DECRIPTION_AUTH="PayPal authorization";
	
	public static Request getAddProfileRequest(ErpPaymentMethodI paymentMethod) {
		return getProfileRequest(TransactionType.ADD_PROFILE,paymentMethod);
	}
	public static Request getUpdateProfileRequest(ErpPaymentMethodI paymentMethod) {
		return getProfileRequest(TransactionType.UPDATE_PROFILE,paymentMethod);
	}
	public static Request getDeleteProfileRequest(ErpPaymentMethodI paymentMethod) {
		return getProfileRequest(TransactionType.DELETE_PROFILE,paymentMethod);
	}
	public static Request getVerifyRequest(final String merchant,ErpPaymentMethodI paymentMethod) {
		Random randomGenerator = new Random();
		String saleId="";
		if(null==paymentMethod.getCustomerId()) {
			saleId=new StringBuilder("0000").append(randomGenerator.nextInt(10000)).append("X").append(randomGenerator.nextInt(10000)).toString();	
		} else {
			saleId=new StringBuilder(paymentMethod.getCustomerId()).append("X").append(randomGenerator.nextInt(10000)).toString();
		}
		Request request=getRequest(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())?TransactionType.ACH_VERIFY:TransactionType.CC_VERIFY,paymentMethod, 0,0, saleId,
				StringUtils.isEmpty(merchant)?ErpAffiliate.getPrimaryAffiliate().getMerchant(paymentMethod.getCardType()):merchant);
		return request;
	}
	
	
	public static Request getCashbackRequest(ErpPaymentMethodI paymentMethod, double amount, double tax, String saleId, String merchantID) {
		Request request=getRequest(TransactionType.CASHBACK,paymentMethod, amount,tax, saleId, merchantID);
		return request;
	}
	public static ErpCashbackModel getCashbackResponse(Response response, ErpPaymentMethodI paymentMethod) {
		ErpCashbackModel cashback = new ErpCashbackModel();
		cashback.setAuthCode(response.getAuthCode());
		cashback.setResponseCode(response.getResponseCode());
		cashback.setDescription(response.getResponseCode()+"-"+response.getStatusMessage());
		cashback.setSequenceNumber(response.getBillingInfo().getTransactionRef());
		cashback.setPaymentMethodType(paymentMethod.getPaymentMethodType());
		cashback.setCardType(paymentMethod.getCardType());
		String accountNumber = paymentMethod.getAccountNumber();
		cashback.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
		cashback.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
		cashback.setBankAccountType(paymentMethod.getBankAccountType());
		cashback.setTransactionSource(EnumTransactionSource.CUSTOMER_REP);
		return cashback;
	}
	
	public static Request getReverseAuthRequest(ErpPaymentMethodI paymentMethod,ErpAuthorizationModel authorization) {
		if(authorization==null) return null;
		Request request=getRequest(TransactionType.REVERSE_AUTHORIZE,paymentMethod, authorization.getAmount(),authorization.getTax(), authorization.getGatewayOrderID(), authorization.getMerchantId());
		if(request!=null && request.getBillingInfo()!=null) {
			request.getBillingInfo().setTransactionRef(authorization.getSequenceNumber());
			request.getBillingInfo().setTransactionRefIndex(authorization.getTrasactionRefIndex());
		}
		return request;
	}
	
	public static ErpReversalModel getReverseAuthResponse(Response response,ErpAuthorizationModel authModel) throws PaylinxResourceException {
		if(authModel==null) return null;
		ErpReversalModel model =null;
		/*if(TransactionType.REVERSE_AUTHORIZE.equals(response.getTransactionType())) {
			if(!response.isRequestProcessed())
				throw new PaylinxResourceException(response.getStatusMessage());
			model = new ErpReversalModel();
			model.setReturnMessage(response.getResponseCode()+"-"+response.getStatusMessage());
			model.setApprovalCode(response.getAuthCode());
			model.setResponseCode(response.getResponseCode());
			model.setResponseMessage(response.getResponseCode()+"-"+response.getStatusMessage());
			
			//model.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID)); // Fix this
			model.setPaymentMethodType(capture.getPaymentMethodType());
			model.setCardType(capture.getCardType());
			model.setCcNumLast4(capture.getCcNumLast4());
			model.setAbaRouteNumber(capture.getAbaRouteNumber());
			model.setBankAccountType(capture.getBankAccountType());
			model.setIsChargePayment(capture.getIsChargePayment());
			BillingInfo billingInfo=response.getBillingInfo();
			if(billingInfo!=null) {
				model.setGatewayOrderID(billingInfo.getTransactionID());
				model.setAmount(billingInfo.getAmount());
				model.setTax(billingInfo.getTax());
				model.setSequenceNumber(billingInfo.getTransactionRef());
				model.setTrasactionRefIndex(billingInfo.getTransactionRefIndex());
				PaymentMethod pm =billingInfo.getPaymentMethod();
				if(pm!=null) {
					model.setProfileID(pm.getBillingProfileID());
					model.setCustomerId(pm.getCustomerID());
				}
			}
		}*/
		return model;
	}
	
	public static Request getVoidCaptureRequest(ErpPaymentMethodI paymentMethod,ErpCaptureModel capture) {
		if(capture==null) return null;
		
		Request request=getRequest(TransactionType.VOID_CAPTURE,paymentMethod, capture.getAmount(),capture.getTax(), capture.getGatewayOrderID(), capture.getMerchantId());
		if(request!=null && request.getBillingInfo()!=null) {
			request.getBillingInfo().setTransactionRef(capture.getSequenceNumber());
			request.getBillingInfo().setTransactionRefIndex(capture.getTrasactionRefIndex());
		}
		return request;
	}
	public static ErpVoidCaptureModel getVoidCaptureResponse(Response response, ErpCaptureModel capture) throws PaylinxResourceException {
		if(capture==null) return null;
		ErpVoidCaptureModel model =null;
		if(TransactionType.VOID_CAPTURE.equals(response.getTransactionType())) {
			if(!response.isRequestProcessed())
				throw new PaylinxResourceException(response.getStatusMessage());
			model = new ErpVoidCaptureModel();
			model.setAmount(capture.getAmount());
			model.setTax(capture.getTax());
			model.setTransactionDate(new Date());
			model.setAuthCode(response.getAuthCode());
			//model.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID)); // Fix this
			model.setPaymentMethodType(capture.getPaymentMethodType());
			model.setCardType(capture.getCardType());
			model.setCcNumLast4(capture.getCcNumLast4());
			model.setAbaRouteNumber(capture.getAbaRouteNumber());
			model.setBankAccountType(capture.getBankAccountType());
			model.setIsChargePayment(capture.getIsChargePayment());
			BillingInfo billingInfo=response.getBillingInfo();
			if(billingInfo!=null) {
				model.setGatewayOrderID(billingInfo.getTransactionID());
				model.setAmount(billingInfo.getAmount());
				model.setTax(billingInfo.getTax());
				model.setSequenceNumber(billingInfo.getTransactionRef());
				model.setTrasactionRefIndex(billingInfo.getTransactionRefIndex());
				PaymentMethod pm =billingInfo.getPaymentMethod();
				if(pm!=null) {
					model.setProfileID(pm.getBillingProfileID());
					model.setCustomerId(pm.getCustomerID());
				}
			}
		}
		return model;
		
	}
	public static Request getCaptureRequest(ErpPaymentMethodI paymentMethod,ErpAuthorizationModel authorization, double actualAmount, double actualTax) {
		if(authorization==null) return null;
		
		Request request=getRequest(TransactionType.CAPTURE,paymentMethod, actualAmount,actualTax, authorization.getGatewayOrderID(), authorization.getMerchantId());
		if(request!=null && request.getBillingInfo()!=null) {
			request.getBillingInfo().setTransactionRef(authorization.getSequenceNumber());
			request.getBillingInfo().setTransactionRefIndex(authorization.getTrasactionRefIndex());
		}
		return request;
	}
	public static ErpCaptureModel getCaptureResponse(Response response,ErpAuthorizationModel authorization)throws PaylinxResourceException {
		if(response==null) return null;
		ErpCaptureModel model =null;
		if(TransactionType.CAPTURE.equals(response.getTransactionType())) {
			if(!response.isRequestProcessed())
				throw new PaylinxResourceException(response.getStatusMessage());
			model = new ErpCaptureModel();
			model.setTransactionSource(EnumTransactionSource.SYSTEM);
			/** Copy from the authorization model passed in -START*/
			model.setPaymentMethodType(authorization.getPaymentMethodType());
			model.setCcNumLast4(authorization.getCcNumLast4());
			model.setAbaRouteNumber(authorization.getAbaRouteNumber());
			model.setBankAccountType(authorization.getBankAccountType());
			model.setIsChargePayment(authorization.getIsChargePayment());
			model.setAffiliate(authorization.getAffiliate());
			/** Copy from the authorization model passed in -END*/
			
			model.setAuthCode(response.getAuthCode());
			model.setResponseCode(response.getResponseCode());
			model.setDescription(response.getResponseCode()+"-"+response.getStatusMessage());
			model.setSequenceNumber(response.getBillingInfo().getTransactionRef());
			//model.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID)); Fix 
			model.setMerchantId(response.getRequest().getBillingInfo().getMerchant().name());
			BillingInfo billingInfo=response.getBillingInfo();
			if(billingInfo!=null) {
				model.setGatewayOrderID(billingInfo.getTransactionID());
				model.setAmount(billingInfo.getAmount());
				model.setTax(billingInfo.getTax());
				model.setSequenceNumber(billingInfo.getTransactionRef());
				model.setTrasactionRefIndex(billingInfo.getTransactionRefIndex());
				PaymentMethod pm =billingInfo.getPaymentMethod();
				if(pm!=null) {
					model.setProfileID(pm.getBillingProfileID());
					model.setCustomerId(pm.getCustomerID());
					if(PaymentMethodType.CREDIT_CARD.equals(pm.getType())) {
						model.setCardType(translate(((CreditCard)pm).getCreditCardType()));	
					}
					
				}
			}
		}
		
		return model;
	}
	public static Request getRequest(TransactionType transType, ErpPaymentMethodI paymentMethod) {
		Random randomGenerator = new Random();
		String saleId="";
		if(null==paymentMethod.getCustomerId()) {
			saleId=new StringBuilder("0000").append(randomGenerator.nextInt(10000)).append("X").append(randomGenerator.nextInt(10000)).toString();	
		} else {
			saleId=new StringBuilder(paymentMethod.getCustomerId()).append("X").append(randomGenerator.nextInt(10000)).toString();
		}
		return getRequest(transType,paymentMethod,0,0,saleId,ErpAffiliate.getPrimaryAffiliate().getMerchant(paymentMethod.getCardType()));
	}
	
	private static Request getProfileRequest(TransactionType transType, ErpPaymentMethodI paymentMethod) {
		return getRequest(transType,paymentMethod,0,0,"",ErpAffiliate.getPrimaryAffiliate().getMerchant(paymentMethod.getCardType()));
	}
	public static Request getRequest(TransactionType transType, ErpPaymentMethodI paymentMethod, double amount, double tax, String saleId, String merchantID) {
		if(paymentMethod==null) return null;
		
		Request request=RequestFactory.getRequest(transType);
		BillingInfo billingInfo=null;
		Merchant merchant=getMerchant(merchantID);
		if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType()) || 
				EnumPaymentMethodType.PAYPAL.equals(paymentMethod.getPaymentMethodType())) {
			billingInfo=BillingInfoFactory.getBillingInfo(merchant, getCreditCardModel(paymentMethod));
		}
		else if(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			billingInfo=BillingInfoFactory.getBillingInfo(merchant, getECheckModel(paymentMethod));
		}
		else 
			return null;
		if(billingInfo!=null ) {
			if(!isProfileRequest(transType)) {
				billingInfo.setTransactionID(saleId);
				billingInfo.setAmount(amount);
				billingInfo.setTax(tax);
				billingInfo.setEwalletId(paymentMethod.geteWalletID());
				billingInfo.setEwalletTxId(paymentMethod.geteWalletTrxnId());
				billingInfo.setVendorEwalletId(paymentMethod.getVendorEWalletID());
			} else {
				billingInfo.setEwalletId(paymentMethod.geteWalletID());
				billingInfo.setEwalletTxId(paymentMethod.geteWalletTrxnId());
				billingInfo.setVendorEwalletId(paymentMethod.getVendorEWalletID());
				billingInfo.setEmailID(paymentMethod.getEmailID());
			}
			request.setBillingInfo(billingInfo);
		} else return null;
		return request;
	}
	private static boolean isProfileRequest(TransactionType transType) {
		if( TransactionType.ADD_PROFILE.equals(transType)||
			TransactionType.GET_PROFILE.equals(transType)||
			TransactionType.UPDATE_PROFILE.equals(transType)||
			TransactionType.DELETE_PROFILE.equals(transType)
			) {
			return true;
		}
		return false;
	}
	public static ErpAuthorizationModel getVerifyResponse(Response response,boolean bypassAVS) throws PaylinxResourceException {
		return getAuthResponse(response,bypassAVS);
	}
	
	public static ErpAuthorizationModel getAuthResponse(Response response) throws PaylinxResourceException {
		return getAuthResponse(response,false);
	}
	private static ErpAuthorizationModel getAuthResponse(Response response, boolean bypassAVS) throws PaylinxResourceException {
		if(response==null) return null;
		if(TransactionType.AUTHORIZE.equals(response.getTransactionType()) ||
		   TransactionType.CC_VERIFY.equals(response.getTransactionType())||
		   TransactionType.ACH_VERIFY.equals(response.getTransactionType())) {
			if(!response.isRequestProcessed())
				throw new PaylinxResourceException(response.getStatusMessage());
			ErpAuthorizationModel model = new ErpAuthorizationModel();
			model.setTransactionSource(EnumTransactionSource.SYSTEM);
			
			setPaymentResponse(response,model);
			if(response.isApproved() && bypassAVS) {
				if(StringUtil.isEmpty(response.getCVVResponse()))//CVV is turned off
						model.setResponseCode(EnumPaymentResponse.APPROVED); //hack for AVS bypass	
				else if(response.isCVVMatch())//CVV is turned on and matches.
					model.setResponseCode(EnumPaymentResponse.APPROVED); 
			}
			BillingInfo billingInfo=response.getBillingInfo();
			
			if(billingInfo!=null) {
				model.setAuthCode(response.getAuthCode());
				model.setSequenceNumber(billingInfo.getTransactionRef());
				model.setMerchantId(billingInfo.getMerchant().name());//Correct this
				model.setAvs((response.isAVSMatch()||bypassAVS)?"Y":"N");
				model.setZipMatchResponse((response.isAVSMatch()||bypassAVS)?"Y":"N");
				PaymentMethod pm =billingInfo.getPaymentMethod();
				if(TransactionType.CC_VERIFY.equals(response.getTransactionType())) {
					model.setCvvResponse(EnumCVVResponse.getEnum(response.getCVVResponse()));
					
				}
				if(pm!=null) {
					model.setAmount(billingInfo.getAmount());
					model.setTax(billingInfo.getTax());
					setPaymentInfo(pm,model);
					if(EnumCardType.ECP.equals(model.getCardType()))
						model.setAvs("Y"); //always set AVS to Y for Echeck?
					model.setCustomerId(pm.getCustomerID());
					model.setReturnCode(0);//??? Is this OK??. Should be. PaylinxException thrown otherwise anyway.
					/*New Fields */
					model.setGatewayOrderID(billingInfo.getTransactionID());
					model.setProfileID(pm.getBillingProfileID());
					model.setTrasactionRefIndex(billingInfo.getTransactionRefIndex());//Verify, if this is set.
				}
			}
			
			return model;
			
		} else return null;
	}
	
	private static void setPaymentInfo(PaymentMethod pm,ErpAuthorizationModel model) {
		
		PaymentMethodType pmType=pm.getType();
		model.setPaymentMethodType(translate(pmType));
		if(PaymentMethodType.CREDIT_CARD.equals(pmType)) {
			CreditCard cc=(CreditCard)pm;
			model.setCardType(translate(cc.getCreditCardType()));
		} else if(PaymentMethodType.ECHECK.equals(pmType)) {
			model.setCardType(EnumCardType.ECP);
			ECheck ec=(ECheck)pm;
			model.setAbaRouteNumber(ec.getRoutingNumber());
			model.setBankAccountType(translate(ec.getBankAccountType()));
		
		}
		String accountNumber = pm.getAccountNumber();
		if(accountNumber!=null && accountNumber.length()>3)
			model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
	}
	private static void setPaymentResponse(Response response,ErpAuthorizationModel model) {
		String procResponseCode=response.getResponseCode();
		EnumPaymentResponse pymtResponse =null;
		if(response.isSuccess())
			pymtResponse=EnumPaymentResponse.APPROVED;
		else if(response.isDeclined())
			pymtResponse=EnumPaymentResponse.DECLINED;
		else if(response.isError())
			pymtResponse=EnumPaymentResponse.ERROR;
		else if(response.isApproved() && !response.isSuccess())
			pymtResponse=EnumPaymentResponse.DECLINED;
		if(pymtResponse==null) {
			pymtResponse=EnumPaymentResponse.getEnum(procResponseCode);
			if(pymtResponse==null) {
				procResponseCode=response.getResponseCodeAlt();
				pymtResponse = EnumPaymentResponse.getEnum(procResponseCode);
			}
		}
		
		if(pymtResponse==null) {
			LOGGER.warn("Unknown proc response code " + procResponseCode + ", setting to CALL");
			pymtResponse = EnumPaymentResponse.CALL;
		}
		model.setResponseCode(pymtResponse);
		model.setProcResponseCode(procResponseCode);
		model.setDescription(response.getResponseCodeAlt()+"-"+response.getStatusMessage());
	}
	private static EnumCardType translate(CreditCardType ccType) {
		if(CreditCardType.AMEX.equals(ccType))
			return EnumCardType.AMEX;
		else if(CreditCardType.DISCOVER.equals(ccType))
			return EnumCardType.DISC;
		else if(CreditCardType.MASTERCARD.equals(ccType))
			return EnumCardType.MC;
		else if(CreditCardType.VISA.equals(ccType))
			return EnumCardType.VISA;
		return null;
	}
	private static CreditCardType translate(EnumCardType ccType) {
		if(EnumCardType.AMEX.equals(ccType))
			return CreditCardType.AMEX;
		else if(EnumCardType.DISC.equals(ccType))
			return CreditCardType.DISCOVER;
		else if(EnumCardType.MC.equals(ccType))
			return CreditCardType.MASTERCARD;
		else if(EnumCardType.VISA.equals(ccType))
			return CreditCardType.VISA;
		else if(EnumCardType.PAYPAL.equals(ccType))
			return CreditCardType.PYPL;
		return null;
	}
	private static EnumPaymentMethodType translate(PaymentMethodType pmType) {
		if(PaymentMethodType.CREDIT_CARD.equals(pmType))
			return EnumPaymentMethodType.CREDITCARD;
		else if(PaymentMethodType.ECHECK.equals(pmType))
			return EnumPaymentMethodType.ECHECK;
		return null;
	}
    private static BankAccountType translate(EnumBankAccountType baType) {
	
		if(EnumBankAccountType.PERSONAL_CHECKING.equals(baType))
			return BankAccountType.CHECKING;
		else if(EnumBankAccountType.PERSONAL_SAVINGS.equals(baType))
			return BankAccountType.SAVINGS;
		return null;
	}
    private static EnumBankAccountType translate(BankAccountType baType) {
    	
		if(BankAccountType.CHECKING.equals(baType))
			return EnumBankAccountType.PERSONAL_CHECKING;
		else if(BankAccountType.SAVINGS.equals(baType))
			return EnumBankAccountType.PERSONAL_SAVINGS;
		return null;
	}
	private static Merchant getMerchant(String merchantID) {
		
		if(StringUtils.isEmpty(merchantID))
			return Merchant.FRESHDIRECT;
		else if (Merchant.FRESHDIRECT.name().equalsIgnoreCase(merchantID))
			return Merchant.FRESHDIRECT;
		else if (Merchant.FDW.name().equalsIgnoreCase(merchantID))
			return Merchant.FDW;
		else if (Merchant.FDX.name().equalsIgnoreCase(merchantID))
			return Merchant.FDX;
		else 
			return Merchant.USQ;
	}
	
	public static CreditCard getCreditCardModel(ErpPaymentMethodI paymentMethod) {
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			EnumCardType cardType=paymentMethod.getCardType();
			if(cardType!=null) {
				cc.setCreditCardType(translate(cardType));
			}
			cc.setAccountNumber(paymentMethod.getAccountNumber());
			cc.setExpirationDate(paymentMethod.getExpirationDate());
			cc.setCustomerName(paymentMethod.getName());
			cc.setAddressLine1(paymentMethod.getAddress1());
			cc.setAddressLine2(paymentMethod.getAddress2());
			cc.setCity(paymentMethod.getCity());
			cc.setState(paymentMethod.getState());
			cc.setCountry(paymentMethod.getCountry());
			cc.setZipCode(paymentMethod.getZipCode());
			cc.setCVV(paymentMethod.getCVV());
			cc.setBillingProfileID(paymentMethod.getProfileID());
			cc.setCustomerID(paymentMethod.getCustomerId());
			// Get EWallet ID
			cc.setEwalletId(paymentMethod.geteWalletID());
			cc.setEwalletTxId(paymentMethod.geteWalletTrxnId());
			cc.setVendorEWalletID(paymentMethod.getVendorEWalletID());
			cc.setEmailID(paymentMethod.getEmailID());
			cc.setDeviceId(paymentMethod.getDeviceId());
			return cc;
		
	}
	public static ECheck getECheckModel(ErpPaymentMethodI paymentMethod) {
		
		
			ECheck ec=PaymentMethodFactory.getECheck();
			EnumBankAccountType baType=paymentMethod.getBankAccountType();
			if(baType!=null) {
				ec.setBankAccountType(translate(baType));
			}
			ec.setAccountNumber(paymentMethod.getAccountNumber());
			ec.setRoutingNumber(paymentMethod.getAbaRouteNumber());
			ec.setCustomerName(paymentMethod.getName());
			ec.setAddressLine1(paymentMethod.getAddress1());
			ec.setAddressLine2(paymentMethod.getAddress2());
			ec.setCity(paymentMethod.getCity());
			ec.setState(paymentMethod.getState());
			ec.setCountry(paymentMethod.getCountry());
			ec.setZipCode(paymentMethod.getZipCode());
			ec.setBillingProfileID(paymentMethod.getProfileID());
			ec.setCustomerID(paymentMethod.getCustomerId());
			return ec;
	}
	
	/**
	 * @param saleResult
	 * @param paymentMethod
	 * @return
	 */
	public static ErpAuthorizationModel getPPAuthResponse(
			PayPalResponse payPalResponse, ErpPaymentMethodI paymentMethod) {
		TransactionModel saleResult = payPalResponse.getTransactionModel();
		if(saleResult==null) 
			return null;
					
		ErpAuthorizationModel model = new ErpAuthorizationModel();
		model.setTransactionSource(EnumTransactionSource.SYSTEM);
		
		if(saleResult.getStatus().equals(Transaction.Status.AUTHORIZED.name())) {
			model.setResponseCode(EnumPaymentResponse.APPROVED); //hack for AVS bypass	
			model.setAvs("Y");
			model.setSequenceNumber(saleResult.getAuthorizationId());	
			model.setEwalletTxId(saleResult.getId());
			
			model.setAmount(saleResult.getAmount().doubleValue());
			model.setCustomerId(paymentMethod.getCustomerId());
			
			model.setGatewayOrderID(saleResult.getOrderId());
			model.setProfileID(saleResult.getToken());
			model.setMerchantId(saleResult.getMerchantAccountId());
			model.setDescription(PP_DECRIPTION_AUTH);
			// Payment Method 
			model.setCardType(EnumCardType.PAYPAL);
			model.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
			String accountNumber = paymentMethod.getAccountNumber();
			if(accountNumber!=null && accountNumber.length()>3){
				model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
			}
		}else{	// PayPal AUthorization Denied
			model.setResponseCode(EnumPaymentResponse.DECLINED);
//			return null;
		}
		return model;
	}
	
	
	 public static Request getRequest(PaymentGatewayRequest pgRequest) {

         

         if(pgRequest==null) return null;

         

         Request _request=RequestFactory.getRequest(TransactionType.valueOf(pgRequest.getTransactionType()));

         Merchant merchant=getMerchant(pgRequest.getMerchant());

         PaymentMethod paymentMethod=getPaymentMethod(pgRequest);

         BillingInfo billingInfo=getBillingInfo(pgRequest, merchant, paymentMethod);

         _request.setBillingInfo(billingInfo);

         return _request;

         

  }

  

  public static PaymentGatewayResponse getPaymentGatewayResponse(ErpAuthorizationModel  authModel) {

         

         if(authModel==null) return null;

         PaymentGatewayResponse gatewayResponse=new PaymentGatewayResponse();

         

         return gatewayResponse;

  }

public static PaymentGatewayResponse getPaymentGatewayResponse(Response response) {

     

     if(response==null) return null;

     

     PaymentGatewayResponse gatewayResponse=new PaymentGatewayResponse();

     gatewayResponse.setAvsResponse(response.getAVSResponse());

     gatewayResponse.setCvvResponse(response.getCVVResponse());

     gatewayResponse.setAuthCode(response.getAuthCode());

     gatewayResponse.setRawRequest(response.getRawRequest());

     gatewayResponse.setRawResponse(response.getRawResponse());

     //gatewayResponse.setRawRequest(rawRequest);//TODO

     gatewayResponse.setResponseTime(response.getResponseTime());

     gatewayResponse.setStatusCode(response.getStatusCode());

     gatewayResponse.setStatusMessage(response.getStatusMessage());

     

     gatewayResponse.setAVSMatch(response.isAVSMatch());

     gatewayResponse.setCVVMatch(response.isCVVMatch());

     gatewayResponse.setApproved(response.isApproved());

     gatewayResponse.setDeclined(response.isDeclined());

     gatewayResponse.setError(response.isError());

     gatewayResponse.setRequestProcessed(response.isRequestProcessed());

     gatewayResponse.setResponseCode(response.getResponseCode());

     gatewayResponse.setResponseCodeAlt(response.getResponseCodeAlt());

     

     BillingInfo billingInfo=response.getBillingInfo();

     if(billingInfo!=null) {

            

            gatewayResponse.setAmount(new BigDecimal(billingInfo.getAmount()));

            gatewayResponse.setTaxAmount(new BigDecimal(billingInfo.getTax()));

            gatewayResponse.setTransactionId(billingInfo.getTransactionID());

            gatewayResponse.setTransactionIndex(billingInfo.getTransactionRef());

            gatewayResponse.setTransactionRef(billingInfo.getTransactionRef());

            

            gatewayResponse.setMerchant(billingInfo.getMerchant().name());

            PaymentMethod pm=billingInfo.getPaymentMethod();

            if(pm!=null) {

            	PaymentMethodData pmData = new PaymentMethodData();

            	pmData.setPaymentMethodType(pm.getType().name());

            	pmData.setCustomerId(pm.getCustomerID());

            	pmData.setCustomerName(pm.getCustomerName());

            	pmData.setAddressLine1(pm.getAddressLine1());

            	pmData.setAddressLine2(pm.getAddressLine2());

            	pmData.setCity(pm.getCity());

            	pmData.setState(pm.getState());

            	pmData.setCountry(pm.getCountry());

            	pmData.setZipCode(pm.getZipCode());

            	pmData.setProfileId(pm.getBillingProfileID());

            	pmData.setAccountNumber(pm.getAccountNumber());

                   if(PaymentMethodType.ECHECK.equals(pm.getType())) {

                	   pmData.setRoutingNumber(((ECheck)pm).getRoutingNumber());

                	   pmData.setBankAccountType(((ECheck)pm).getBankAccountType().name());

                   } else {//Credit Card
                	   pmData.setExpirationDate(((CreditCard)pm).getExpirationDate());
                   }
                   gatewayResponse.setPaymentMethod(pmData);

            }

     }

     

     return gatewayResponse;

}

  

  public static PaymentMethod getPaymentMethod(PaymentGatewayRequest pgRequest) {

         PaymentMethod paymentMethod=null;
         PaymentMethodData  pmData = null;
         if(pgRequest.getPaymentMethod()!=null)
        	 pmData = pgRequest.getPaymentMethod();
         else return  paymentMethod;

  if(PaymentMethodType.CREDIT_CARD.equals(PaymentMethodType.valueOf(pmData.getPaymentMethodType()))) {

                CreditCard cc=new CreditCardImpl();

                cc.setAccountNumber(pmData.getAccountNumber());

                cc.setExpirationDate(pmData.getExpirationDate());

                paymentMethod=cc;

         } else  {

                ECheck ec=new ECheckImpl();

                ec.setAccountNumber(pmData.getAccountNumber());

                ec.setRoutingNumber(pmData.getRoutingNumber());

                String val=pmData.getBankAccountType();

                if(PaymentechConstants.BankAccountType.CONSUMER_CHECKING.getCode().equals(val)) 

                      ec.setBankAccountType(BankAccountType.CHECKING );

                else if(PaymentechConstants.BankAccountType.CONSUMER_SAVINGS.getCode().equals(val)) 

                      ec.setBankAccountType(BankAccountType.SAVINGS);

                paymentMethod=ec;

         }

         paymentMethod.setCustomerID(pmData.getCustomerId());

         paymentMethod.setCustomerName(pmData.getCustomerName());

         paymentMethod.setAddressLine1(pmData.getAddressLine1());

         paymentMethod.setAddressLine2(pmData.getAddressLine2());

         paymentMethod.setCity(pmData.getCity());

         paymentMethod.setState(pmData.getState());

         paymentMethod.setZipCode(pmData.getZipCode());

         paymentMethod.setState(pmData.getState());

         paymentMethod.setBillingProfileID(pmData.getProfileId());

         return paymentMethod;

         

  }

  
  private static BillingInfo getBillingInfo(PaymentGatewayRequest pgRequest,Merchant merchant, PaymentMethod paymentMethod) {

         

         BillingInfo billingInfo=BillingInfoFactory.getBillingInfo(merchant, paymentMethod);

         billingInfo.setTransactionID(pgRequest.getTransactionId());

         billingInfo.setTransactionRef(pgRequest.getTransactionRef());

         billingInfo.setTransactionRefIndex(pgRequest.getTransactionIndex());

         billingInfo.setAmount(pgRequest.getAmount().doubleValue());

         billingInfo.setTax(pgRequest.getTaxAmount().doubleValue());

         

         return billingInfo;

  }
  private static BillingInfo getBillingInfo(PaymentGatewayResponse pgResponse) {

	  Merchant merchant = Merchant.valueOf(pgResponse.getMerchant());
	  PaymentMethod paymentMethod = getPaymentMethod(pgResponse);
	  BillingInfo billingInfo=BillingInfoFactory.getBillingInfo(merchant, paymentMethod);
	  billingInfo.setTransactionRef(pgResponse.getTransactionRef());
	  billingInfo.setTransactionID(pgResponse.getTransactionId());
	  billingInfo.setTransactionRefIndex(pgResponse.getTransactionIndex());
	  billingInfo.setAmount(pgResponse.getAmount().doubleValue());
	  billingInfo.setTax(pgResponse.getTaxAmount().doubleValue());
	  
      return billingInfo;

}

  
 private static PaymentMethod getPaymentMethod(PaymentGatewayResponse pgResponse) {

     PaymentMethod paymentMethod=null;
     PaymentMethodData pmData=null;
     if(pgResponse.getPaymentMethod()!=null)
    	 pmData = pgResponse.getPaymentMethod();
     else return paymentMethod;

if(PaymentMethodType.CREDIT_CARD.equals(PaymentMethodType.valueOf(pmData.getPaymentMethodType()))) {

            CreditCard cc=new CreditCardImpl();

            cc.setExpirationDate(pmData.getExpirationDate());
            if(pmData.getCardType()!=null)
            cc.setCreditCardType(CreditCardType.valueOf(pmData.getCardType()));
            paymentMethod=cc;

     } else  {

            ECheck ec=new ECheckImpl();

            ec.setRoutingNumber(pmData.getRoutingNumber());

            String val=pmData.getBankAccountType();

            if(PaymentechConstants.BankAccountType.CONSUMER_CHECKING.getCode().equals(val)) 

                  ec.setBankAccountType(BankAccountType.CHECKING );

            else if(PaymentechConstants.BankAccountType.CONSUMER_SAVINGS.getCode().equals(val)) 

                  ec.setBankAccountType(BankAccountType.SAVINGS);

            paymentMethod=ec;

     }

	 paymentMethod.setAccountNumber(pmData.getAccountNumber());
     paymentMethod.setCustomerID(pmData.getCustomerId());
     paymentMethod.setCustomerName(pmData.getCustomerName());
     paymentMethod.setAddressLine1(pmData.getAddressLine1());
     paymentMethod.setAddressLine2(pmData.getAddressLine2());
     paymentMethod.setCity(pmData.getCity());
     paymentMethod.setState(pmData.getState());
     paymentMethod.setZipCode(pmData.getZipCode());
     paymentMethod.setCountry(pmData.getCountry());
     paymentMethod.setBillingProfileID(pmData.getProfileId());

     return paymentMethod;

}
public static PaymentGatewayRequest getPaymentGatewayRequest(Request request) {
	
	
	 
	 if(request==null) return null;
	 
	 PaymentGatewayRequest pgRequest=new PaymentGatewayRequest();
	 pgRequest.setTransactionType(request.getTransactionType().name());
	 
	 BillingInfo billingInfo=request.getBillingInfo();
	 
	 if(billingInfo!=null) {
		 
		 pgRequest.setAmount(new BigDecimal(billingInfo.getAmount()));
		 pgRequest.setTaxAmount(new BigDecimal(billingInfo.getTax()));
		 pgRequest.setCurrency(billingInfo.getCurrency().name());
		 pgRequest.setMerchant(billingInfo.getMerchant().name());
		 
		 pgRequest.setTransactionId(billingInfo.getTransactionID());
		 pgRequest.setTransactionIndex(billingInfo.getTransactionRef());
		 pgRequest.setTransactionRef(billingInfo.getTransactionRef());
		 
		 if(billingInfo.getEStoreId()!=null)
		 pgRequest.setEStore(billingInfo.getEStoreId().getContentId());
		 
		 PaymentMethod pm=billingInfo.getPaymentMethod();
		 
		 if(pm!=null) {
			 PaymentMethodData pmData = new PaymentMethodData();
			 pmData.setPaymentMethodType(pm.getType().name());
			 
			 if(PaymentMethodType.CREDIT_CARD.equals(pm.getType())) {
				 
				 CreditCard cc=(CreditCard)pm;
				 if(cc.getCreditCardType()!=null) {
					 pmData.setCardType(PaymentechConstants.CardType.get( cc.getCreditCardType()).getType().name());
				 }
				 pmData.setExpirationDate(cc.getExpirationDate());
			 } else {
				 
				 ECheck ec=(ECheck)pm;
				 pmData.setRoutingNumber(ec.getRoutingNumber());
				 if(ec.getBankAccountType()!=null) {
					 pmData.setBankAccountType(PaymentechConstants.BankAccountType.get(ec.getBankAccountType()).getCode());
				 }
			 }
			 pmData.setAccountNumber(pm.getAccountNumber());
			 pmData.setCustomerName(pm.getCustomerName());
			 pmData.setAddressLine1(pm.getAddressLine1());
			 pmData.setAddressLine2(pm.getAddressLine2());
			 pmData.setCity(pm.getCity());
			 pmData.setState(pm.getState());
			 pmData.setCountry(pm.getCountry());
			 pmData.setZipCode(pm.getZipCode());
			 if(StringUtil.isEmpty(pgRequest.getCurrency())) {
				 pgRequest.setCurrency(pm.getCurrency().name());
			 }
			 pmData.setCustomerId(pm.getCustomerID());
			 pmData.setProfileId(pm.getBillingProfileID());
			 pgRequest.setPaymentMethod(pmData);
		 }
		 
	 }
	 
	 return pgRequest;
 }
public static Response getResponse(PaymentGatewayResponse serviceResponse, Request request) {
	if(serviceResponse==null)
		return null;
	ResponseImpl response = new ResponseImpl(request);
	response.setStatusCode(serviceResponse.getStatusCode());
	response.setStatusMessage(serviceResponse.getStatusMessage());
	response.setRequestProcessed(serviceResponse.isRequestProcessed());
	response.setApproved(serviceResponse.isApproved());
	response.setAuthCode(serviceResponse.getAuthCode());
	response.setDeclined(serviceResponse.isDeclined());
	response.setError(serviceResponse.isError());
	response.setResponseCode(serviceResponse.getResponseCode());
	response.setResponseCodeAlt(serviceResponse.getResponseCodeAlt());
	response.setAVSMatch(serviceResponse.isAVSMatch());
	response.setAVSResponse(serviceResponse.getAvsResponse());
	response.setCVVMatch(serviceResponse.isCVVMatch());
	response.setCVVResponse(serviceResponse.getCvvResponse());
	response.setBillingInfo(getBillingInfo(serviceResponse));
	response.setResponseTime(serviceResponse.getResponseTime());
	
	return response;
}


}
