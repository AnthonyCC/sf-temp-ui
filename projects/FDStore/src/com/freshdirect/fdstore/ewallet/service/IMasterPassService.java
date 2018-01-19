package com.freshdirect.fdstore.ewallet.service;

import com.freshdirect.fdstore.ewallet.PaymentData;
import com.freshdirect.fdstore.ewallet.impl.MasterpassData;
import com.mastercard.mcwallet.sdk.MasterPassServiceRuntimeException;
import com.mastercard.mcwallet.sdk.xml.allservices.MerchantTransactions;

public interface IMasterPassService {
	
	MasterpassData getPreCheckoutData(MasterpassData data,PaymentData paymentData) throws Exception;

	MasterpassData getExpressCheckoutData(MasterpassData data) throws Exception;

	MasterpassData getPairingToken(MasterpassData data) throws Exception;

	MasterpassData getAccessToken(MasterpassData data) throws Exception;

	MasterpassData getLongAccessToken(MasterpassData data) throws Exception;

	MasterpassData getCheckoutData(MasterpassData command) throws Exception;

	MasterpassData getRequestTokenAndRedirectUrl(MasterpassData data)throws Exception;

	MasterpassData postShoppingCart(MasterpassData data,String shoppingCartRequestasXML) throws Exception;

	MasterpassData postMerchantInit(MasterpassData data) throws Exception;

	MerchantTransactions postCheckoutTransaction(MasterpassData mpData,	MerchantTransactions reqTrxns)throws MasterPassServiceRuntimeException;

}
