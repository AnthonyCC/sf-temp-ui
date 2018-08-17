package com.freshdirect.webapp.ajax.signup;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.util.ValidationUtils;

public class HomeSignUpService implements SignUpService {

    private static final HomeSignUpService INSTANCE = new HomeSignUpService();

    public static HomeSignUpService getInstance() {
        return INSTANCE;
    }

    private HomeSignUpService() {
    }

    @Override
    public ActionResult validate(SignUpRequest request) {
        ActionResult result = new ActionResult();

        ValidationUtils.validateEmailAddress(result, EnumUserInfoName.EMAIL.getCode(), request.getEmail());
        ValidationUtils.validatePassword(result, EnumUserInfoName.PASSWORD.getCode(), request.getPassword());
        ValidationUtils.validateZipCode(result, "zipCode", request.getZipCode());
        ValidationUtils.validateEmailPreferencesLevel(result, "emailPreferenceLevel", request.getEmailPreferenceLevel());

        ValidationUtils.validateFirstName(result, "firstName", request.getEmail().substring(0, request.getEmail().indexOf("@")));
        return result;
    }

    @Override
    public FDCustomerModel createFdCustomer(FDUserI user, SignUpRequest request) {
        return SignUpFactory.getInstance().getFDCustomer(null, null, null);
    }

    @Override
    public ErpCustomerModel createErpCustomer(FDUserI user, SignUpRequest request) {
        String lastName = null;
        String firstName = request.getEmail().substring(0, request.getEmail().indexOf("@"));
        EnumServiceType serviceType = EnumServiceType.getEnum(request.getServiceType());

        ErpAddressModel erpAddressModel = SignUpFactory.getInstance().createDefaultErpAddressModel(serviceType, firstName, lastName);
        ErpCustomerInfoModel customerInfo = SignUpFactory.getInstance().getCustomerInfo(request.getEmail(), request.getEmailPreferenceLevel(), request.isPlainTextEmail(),
                request.isReceiveNews(), request.isTcAgree(), firstName, lastName, user.getLastRefTrackingCode(), user.getLastRefProgId(), user.getLastRefProgInvtId());
        return SignUpFactory.getInstance().getErpCustomerModel(request.getEmail(), request.getPassword(), request.isSocialLoginOnly(), customerInfo, erpAddressModel, true);
    }

}
