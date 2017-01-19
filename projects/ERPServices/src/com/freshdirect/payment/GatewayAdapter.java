package com.freshdirect.payment;

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.braintreegateway.Result;
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
import com.freshdirect.customer.ErpPaymentModel;
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
import com.freshdirect.payment.gateway.impl.BillingInfoFactory;
import com.freshdirect.payment.gateway.impl.PaymentMethodFactory;
import com.freshdirect.payment.gateway.impl.RequestFactory;

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
}
