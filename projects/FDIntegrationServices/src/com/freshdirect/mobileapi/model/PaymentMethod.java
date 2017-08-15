package com.freshdirect.mobileapi.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class PaymentMethod {

    private ErpPaymentMethodI paymentMethod;
    
   

    private PaymentMethod() {

    }
    
    public static PaymentMethod wrap(ErpPaymentMethodI paymentMethod) {
        PaymentMethod newInstance = new PaymentMethod();
        newInstance.paymentMethod = paymentMethod;
        return newInstance;
    }

    public boolean isBadAccount() throws FDException {
        //CrmGetIsBadAccountTagWrapper wrapper = new CrmGetIsBadAccountTagWrapper(new CrmGetIsBadAccountTag());
        //wrapper.isBadAccount(paymentMethod);
        return PaymentFraudManager.checkBadAccount(paymentMethod, false);
    }

    public String getId() {
    	if(paymentMethod != null && ((ErpPaymentMethodModel) paymentMethod).getPK() != null){
    		return ((ErpPaymentMethodModel) paymentMethod).getPK().getId();
    	}else{
    		return "";
    	}
        
    }

    
    public String getDescription() {
        return paymentMethod.getBankAccountType().getDescription();
    }

    public String getMaskedAccountNumber() {
        return paymentMethod.getMaskedAccountNumber();
    }

    public String getBankName() {
        return paymentMethod.getBankName();
    }

    public String getAbaRouteNumber() {
        return paymentMethod.getAbaRouteNumber();
    }

    public String getName() {
        return paymentMethod.getName();
    }

    public String getAddress1() {
        return paymentMethod.getAddress1();
    }

    public String getAddress2() {
        return paymentMethod.getAddress2();
    }
    
    public String getCountry() {
        return paymentMethod.getCountry();
    }

    public String getApartment() {
        return paymentMethod.getApartment();
    }

    public String getState() {
        return paymentMethod.getState();
    }

    public String getCity() {
    	
    		return paymentMethod.getCity();
    		
    }

    public String getZipCode() {
    	
    		return paymentMethod.getZipCode();
    		
    }
    
    public boolean isDebitCard(){
    	return paymentMethod.isDebitCard();
    }

    public String getCardType() {
        // TODO Auto-generated method stub
        return paymentMethod.getCardType().getDisplayName();
    }

    public Date getExpirationDate() {
        return paymentMethod.getExpirationDate();
    }

    public String geteWalletID() {
    	if(paymentMethod.geteWalletID() != null){
    		int ewalletId = Integer.parseInt(paymentMethod.geteWalletID());
    		return EnumEwalletType.getEnum(ewalletId).getName();
    	}else{
    		return "";
    	}
    }
    
    public String getEmailId() {
        return paymentMethod.getEmailID();
    }
    
    public String getProfileId(){
    	if(paymentMethod.geteWalletID() != null && Integer.parseInt(paymentMethod.geteWalletID()) == EnumEwalletType.PP.getValue()){
    		return paymentMethod.getProfileID();
    	}else{
    		return "";
    	}
    }
    
    public String getTokenType(){
    	if(paymentMethod.geteWalletID() != null && Integer.parseInt(paymentMethod.geteWalletID()) == EnumEwalletType.PP.getValue()){
    		if(paymentMethod.getPK() == null || (paymentMethod.getCustomerId() == null || StringUtil.isEmpty(paymentMethod.getCustomerId()))){
    			return "Client";
    		}else{
    			return "Vault";
    		}
    	}else{
    		return "";
    	}
    }
}
