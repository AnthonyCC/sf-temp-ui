package com.freshdirect.webapp.ajax.signup;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class RegisterService {

    private static final Category LOGGER = LoggerFactory.getInstance(RegisterService.class);

    private static final RegisterService INSTANCE = new RegisterService();

    public static RegisterService getInstance() {
        return INSTANCE;
    }

    private RegisterService() {
    }

    public void register(FDSessionUser user, FDActionInfo actionInfo, ErpCustomerModel erpCustomer, FDCustomerModel fdCustomer, EnumServiceType serviceType, boolean tcAgree)
            throws FDResourceException, ErpDuplicateUserIdException, ErpFraudException {
        LOGGER.debug("RegisterService: register user");
        RegistrationResult regResult = FDCustomerManager.register(actionInfo, erpCustomer, fdCustomer, user.getCookie(), user.isPickupOnly(), user.isEligibleForSignupPromotion(),
                null, serviceType);
        FDIdentity identity = regResult.getIdentity();
        LOGGER.debug("RegisterService: registered user id: " + identity);

        user.setIdentity(identity);
        user.invalidateCache();
        user.isLoggedIn(true);
        // Set the Default Delivery pass status.
        FDUserDlvPassInfo dlvpassInfo = new FDUserDlvPassInfo(EnumDlvPassStatus.NONE, null, null, null, 0.0, null, 0, 0, 0, false, 0, null, 0, null, null);
        user.getUser().setDlvPassInfo(dlvpassInfo);
        user.getUser().setAssignedCustomerParams(FDCustomerManager.getAssignedCustomerParams(user));
        user.setTcAcknowledge(tcAgree);
    }
}
