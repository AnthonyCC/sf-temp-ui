package com.freshdirect.webapp.ajax.signup;

import java.util.Date;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.framework.util.MD5Hasher;

public class SignUpFactory {

    private static final SignUpFactory INSTANCE = new SignUpFactory();

    public static SignUpFactory getInstance() {
        return INSTANCE;
    }

    private SignUpFactory() {
    }

    public FDCustomerModel getFDCustomer(String depotCode, String clickId, String promoCode) {
        FDCustomerModel fdCustomer = new FDCustomerModel();
        fdCustomer.setDepotCode(depotCode);
        fdCustomer.getCustomerEStoreModel().setRafClickId(clickId);
        fdCustomer.getCustomerEStoreModel().setRafPromoCode(promoCode);
        return fdCustomer;
    }

    public ErpCustomerModel getErpCustomerModel(String emailAddress, String password, boolean socialLoginOnly, ErpCustomerInfoModel customerInfo, ErpAddressModel erpAddressModel,
            boolean isActive) {
        ErpCustomerModel erpCustomer = new ErpCustomerModel();
        erpCustomer.setUserId(emailAddress);
        erpCustomer.setPasswordHash(MD5Hasher.hash(password));
        erpCustomer.setActive(isActive);
        erpCustomer.setSocialLoginOnly(socialLoginOnly);
        erpCustomer.setCustomerInfo(customerInfo);
        erpCustomer.setSapBillToAddress(erpAddressModel);
        return erpCustomer;
    }

    public ErpCustomerInfoModel getCustomerInfo(String emailAddress, String emailPreferenceLimit, boolean isPlainTextEmail, boolean isRecieveNews, boolean isTcAgree,
            String firstName, String lastName, String regRefTrackingCode, String referralProgId, String referralProgInvtId) {
        ErpCustomerInfoModel customerInfo = new ErpCustomerInfoModel();
        customerInfo.setEmail(emailAddress);
        customerInfo.setUnsubscribeDate(isRecieveNews ? null : new Date());
        customerInfo.setReceiveNewsletter(isRecieveNews); // FD Email Preference
        customerInfo.setEmailPreferenceLevel(emailPreferenceLimit); // FDX Email Preference
        customerInfo.setEmailPlaintext(isPlainTextEmail);
        customerInfo.setFdTcAgree(isTcAgree ? "X" : null);
        customerInfo.setRegRefTrackingCode(regRefTrackingCode);
        customerInfo.setReferralProgId(referralProgId);
        customerInfo.setReferralProgInvtId(referralProgInvtId);
        customerInfo.setFirstName(firstName);
        customerInfo.setLastName(lastName);
        return customerInfo;
    }

    public ErpAddressModel createDefaultErpAddressModel(EnumServiceType serviceType, String firstName, String lastName) {
        ErpAddressModel erpAddress = new ErpAddressModel();
        erpAddress.setAddress1(FDStoreProperties.getFdDefaultBillingStreet());
        erpAddress.setCity(FDStoreProperties.getFdDefaultBillingTown());
        erpAddress.setState(FDStoreProperties.getFdDefaultBillingState());
        erpAddress.setCountry(FDStoreProperties.getFdDefaultBillingCountry());
        erpAddress.setZipCode(FDStoreProperties.getFdDefaultBillingPostalcode());
        erpAddress.setServiceType(serviceType);
        erpAddress.setFirstName(firstName);
        erpAddress.setLastName(lastName);
        return erpAddress;
    }

}
