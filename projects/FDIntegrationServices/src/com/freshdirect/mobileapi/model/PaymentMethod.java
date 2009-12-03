package com.freshdirect.mobileapi.model;

import java.util.Date;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.payment.fraud.PaymentFraudManager;

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
        return ((ErpPaymentMethodModel) paymentMethod).getPK().getId();
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

    public String getCardType() {
        // TODO Auto-generated method stub
        return paymentMethod.getCardType().getDisplayName();
    }

    public Date getExpirationDate() {
        return paymentMethod.getExpirationDate();
    }

}
