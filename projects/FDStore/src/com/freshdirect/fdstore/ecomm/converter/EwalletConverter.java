package com.freshdirect.fdstore.ecomm.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.ecomm.converter.CrmManagerConverter;
import com.freshdirect.ecommerce.data.fdstore.EwalletData;
import com.freshdirect.ecommerce.data.fdstore.EwalletPostBackData;
import com.freshdirect.ecommerce.data.fdstore.ValidationErrorData;
import com.freshdirect.ecommerce.data.fdstore.ValidationResultData;
import com.freshdirect.ecommerce.data.list.FDActionInfoData;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.ewallet.EwalletPostBackModel;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.PaymentData;
import com.freshdirect.fdstore.ewallet.ValidationError;
import com.freshdirect.fdstore.ewallet.ValidationResult;
import com.freshdirect.payment.service.ModelConverter;

public class EwalletConverter {

	public static EwalletData buildEwalletData(EwalletRequestData data) {
		if(data != null){
		EwalletData requestData = new EwalletData();
		requestData.setAppBaseUrl(data.getAppBaseUrl());
		requestData.setContextPath(data.getContextPath());
		requestData.setCustomerId(data.getCustomerId());
		requestData.setDebitCardSwitch(data.isDebitCardSwitch());
		requestData.setEmailId(data.getEmailId());
		if(data.geteWalletType() != null)
		requestData.seteWalletType(data.geteWalletType());
		requestData.seteWalletAction(data.geteWalletAction());
		requestData.seteWalletResponseStatus(data.geteWalletResponseStatus());
		requestData.seteWalletType(data.geteWalletType());
		if(data.getFdActionInfo() != null)
		requestData.setFdActionInfo(buildFdActionInfo(data.getFdActionInfo()));
		requestData.setMobileCallbackDomain(data.getMobileCallbackDomain());
		requestData.setMpPairedPaymentMethod(data.getMpPairedPaymentMethod());
		requestData.setPairingToken(data.getPairingToken());
		requestData.setPairingVerifier(data.getPairingVerifier());
		if(data.getPaymentData() != null)
		requestData.setPaymentData(buildPaymentData(data.getPaymentData()));
		requestData.setPaymentechEnabled(data.isPaymentechEnabled());
		requestData.setPrecheckoutCardId(data.getPrecheckoutCardId());
		requestData.setPrecheckoutTransactionId(data.getPrecheckoutTransactionId());
		requestData.setReqParams(data.getReqParams());
		requestData.setShoppingCartItems(data.getShoppingCartItems());
		if(data.getTrxns() != null)
		requestData.setTrxns(buildErpPostBackData(data.getTrxns()));
		return requestData;
		}
		return null;
		
		
	}
	
	private static List<EwalletPostBackData> buildErpPostBackData(List<EwalletPostBackModel> trxns) {
		List<EwalletPostBackData> walletPostBackModel = new ArrayList<EwalletPostBackData>();
		for (EwalletPostBackModel ewalletPostBackData : trxns) {
			EwalletPostBackData model = new EwalletPostBackData();
			model.setApprovalCode(ewalletPostBackData.getApprovalCode());
			model.setConsumerKey(ewalletPostBackData.getConsumerKey());
			model.setCurrency(ewalletPostBackData.getCurrency());
			model.setCustomerId(ewalletPostBackData.getCurrency());
			model.setError(ewalletPostBackData.isError());
			model.setErrorStr(ewalletPostBackData.getErrorStr());
			model.setExpressCheckoutIndicator(ewalletPostBackData.isExpressCheckoutIndicator());
			model.setExtension(ewalletPostBackData.getExtension());
			model.setgAL(ewalletPostBackData.isgAL());
			model.setgALId(ewalletPostBackData.getgALId());
			model.setKey(ewalletPostBackData.getKey());
			model.setOrderAmount(ewalletPostBackData.getOrderAmount());
			model.setOrderId(ewalletPostBackData.getOrderId());
			model.setPostBackSuccess(ewalletPostBackData.isPostBackSuccess());
			model.setPreCheckoutTransactionId(ewalletPostBackData.getPreCheckoutTransactionId());
			if(ewalletPostBackData.getPurchaseDate() != null)
			model.setPurchaseDate(ewalletPostBackData.getPurchaseDate().getTime());
			model.setRecoverable(ewalletPostBackData.getRecoverable());
			model.setSalesActionId(ewalletPostBackData.getSalesActionId());
			model.setTransactionId(ewalletPostBackData.getTransactionId());
			String[] status = {ewalletPostBackData.getTransactionStatus()};
			model.setTransactionStatus(status);
		}
		return walletPostBackModel;
	}

	
	private static FDActionInfoData buildFdActionInfo(FDActionInfo fdActionInfo)  {
		FDActionInfoData actionInfodata = new FDActionInfoData();
		if( fdActionInfo.getType() != null)
		actionInfodata.setAccountActivityType(fdActionInfo.getType().getCode());
		if(fdActionInfo.getAgent() != null)
		actionInfodata.setAgent(CrmManagerConverter.buildCrmAgentModelData(fdActionInfo.getAgent()));
		actionInfodata.setDebitCardSwitch(fdActionInfo.isDebitCardSwitch());
		if(fdActionInfo.getIdentity() != null)
		actionInfodata.setErpCustomerId(fdActionInfo.getIdentity().getErpCustomerPK());
		actionInfodata.seteStore(fdActionInfo.geteStore().getContentId());
		if(fdActionInfo.getIdentity() != null)
		actionInfodata.setFdCustomerId(fdActionInfo.getIdentity().getFDCustomerPK());
		actionInfodata.setFdUserId(fdActionInfo.getFdUserId());
		actionInfodata.setInitiator(fdActionInfo.getInitiator());
		actionInfodata.setMasqueradeAgent(fdActionInfo.getMasqueradeAgentTL());
		actionInfodata.setNote(fdActionInfo.getNote());
		if(fdActionInfo.getPaymentDefaultType() != null)
		actionInfodata.setPaymentMethodDefaultType(fdActionInfo.getPaymentDefaultType().getName());
		actionInfodata.setPR1(fdActionInfo.isPR1());
		if(fdActionInfo.getSource() != null)
		actionInfodata.setSource(fdActionInfo.getSource().getCode());
		if(fdActionInfo.getTaxationType() != null)
		actionInfodata.setTaxationType(fdActionInfo.getTaxationType().getCode());
		return actionInfodata;
		
	}
	
	

	private static com.freshdirect.ecommerce.data.fdstore.PaymentData buildPaymentData(PaymentData data) {
		com.freshdirect.ecommerce.data.fdstore.PaymentData  paymentData = new com.freshdirect.ecommerce.data.fdstore.PaymentData ();
		paymentData.setAbaRouteNumber(data.getAbaRouteNumber());
		paymentData.setAccountNumber(data.getAccountNumber());
		paymentData.setAddress1(data.getAddress1());
		paymentData.setAddress2(data.getAddress2());
		paymentData.setApartment(data.getApartment());
		paymentData.setBankAccountType(data.getBankAccountType());
		paymentData.setBankName(data.getBankName());
		paymentData.setBestNumber(data.getBestNumber());
		paymentData.setCity(data.getCity());
		paymentData.setCountry(data.getCountry());
		paymentData.seteWalletID(data.geteWalletID());
		paymentData.setExpiration(data.getExpiration());
		paymentData.setId(data.getId());
		paymentData.setNameOnCard(data.getNameOnCard());
		paymentData.setSelected(data.isSelected());
		paymentData.setState(data.getState());
		paymentData.setTitle(data.getTitle());
		paymentData.setType(data.getType());
		paymentData.setVendorEWalletID(data.getVendorEWalletID());
		paymentData.setZip(data.getZip());
		return paymentData;
	}

	public static EwalletResponseData buildEwalletResponseDate(com.freshdirect.ecommerce.data.fdstore.EwalletResponseData data) {
		if(data != null){
		EwalletResponseData response = new EwalletResponseData();
		response.setAllowedPaymentMethodTypes(data.getAllowedPaymentMethodTypes());
		response.setCallbackUrl(data.getCallbackUrl());
		response.seteWalletExpressCheckout(data.geteWalletExpressCheckout());
		response.seteWalletIdentifier(data.geteWalletIdentifier());
		response.setLoyaltyEnabled(data.getLoyaltyEnabled());
		response.setPairingRequest(data.getPairingRequest());
		response.setPairingToken(data.getPairingToken());
		if(data.getPaymentMethod() != null)
		response.setPaymentMethod(ModelConverter.buildErpPaymentMethodModel(data.getPaymentMethod()));
		response.setPreCheckoutTnxId(data.getPreCheckoutTnxId());
		response.setPreferredMPCard(data.getPreferredMPCard());
		response.setRedirectUrl(data.getRedirectUrl());
		response.setReqDatatype(data.getReqDatatype());
		response.setRequestBasicCkt(data.getRequestBasicCkt());
		response.setResParam(data.getResParam());
		response.setSuppressShippingEnable(data.getSuppressShippingEnable());
		response.setToken(data.getToken());
		response.setTransactionId(data.getTransactionId());
		if(data.getTrxns() != null)
		response.setTrxns(buildErpPostBackModel(data.getTrxns()));
		if( data.getValidationResult() != null)
		response.setValidationResult(buildValidationResult(data.getValidationResult()));
		response.setVersion(data.getVersion());
		return response;
		}
		return null;
	}

	private static List<EwalletPostBackModel> buildErpPostBackModel(List<EwalletPostBackData> trxns) {
		List<EwalletPostBackModel> walletPostBackModel = new ArrayList<EwalletPostBackModel>();
		for (EwalletPostBackData ewalletPostBackData : trxns) {
			EwalletPostBackModel model = new EwalletPostBackModel();
			model.setApprovalCode(ewalletPostBackData.getApprovalCode());
			model.setConsumerKey(ewalletPostBackData.getConsumerKey());
			model.setCurrency(ewalletPostBackData.getCurrency());
			model.setCustomerId(ewalletPostBackData.getCurrency());
			model.setError(ewalletPostBackData.isError());
			model.setErrorStr(ewalletPostBackData.getErrorStr());
			model.setExpressCheckoutIndicator(ewalletPostBackData.getExpressCheckoutIndicator());
			model.setExtension(ewalletPostBackData.getExtension());
			model.setgAL(ewalletPostBackData.isgAL());
			model.setgALId(ewalletPostBackData.getgALId());
			model.setKey(ewalletPostBackData.getKey());
			model.setOrderAmount(ewalletPostBackData.getOrderAmount());
			model.setOrderId(ewalletPostBackData.getOrderId());
			model.setPostBackSuccess(ewalletPostBackData.isPostBackSuccess());
			model.setPreCheckoutTransactionId(ewalletPostBackData.getPreCheckoutTransactionId());
			if(ewalletPostBackData.getPurchaseDate() != null)
			model.setPurchaseDate(new Date(ewalletPostBackData.getPurchaseDate()));
			model.setRecoverable(ewalletPostBackData.getRecoverable());
			model.setSalesActionId(ewalletPostBackData.getSalesActionId());
			model.setTransactionId(ewalletPostBackData.getTransactionId());
			model.setTransactionStatus(ewalletPostBackData.getStatus());
		}
		return walletPostBackModel;
	}
	
	private static ValidationResult buildValidationResult(ValidationResultData validationResult) {
		ValidationResult validationResultData = new ValidationResult();
		validationResultData.setErrors(buildValidationError(validationResult.getErrors()));
		validationResultData.setFdform(validationResult.getFdform());
		return validationResultData;
	}

	private static List<ValidationError> buildValidationError(List<ValidationErrorData> errors) {
		List<ValidationError> validationErrorList = new ArrayList<ValidationError>();
		 for (ValidationErrorData validationError : errors) {
			 ValidationError errorData = new ValidationError();
			errorData.setError(validationError.getError());
			errorData.setName(validationError.getName());
			validationErrorList.add(errorData);
		}
		return validationErrorList;
		 
	}

	

}
