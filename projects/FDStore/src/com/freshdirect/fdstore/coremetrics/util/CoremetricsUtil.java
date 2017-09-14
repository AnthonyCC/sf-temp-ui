package com.freshdirect.fdstore.coremetrics.util;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CoremetricsUtil {

    private static final Logger LOGGER = LoggerFactory.getInstance(CoremetricsUtil.class);

    private static final CoremetricsUtil INSTANCE = new CoremetricsUtil();

    private CoremetricsUtil() {
    }

    public static CoremetricsUtil defaultService() {
        return INSTANCE;
    }

    public String getCustomerTypeByOrderCount(FDUserI user) {
        return (user.isCorporateUser()) ? getCorporateCustomerTypeByOrderCount(user) : getResidentalCustomerTypeByOrderCount(user);
    }

    public String getResidentalCustomerTypeByOrderCount(FDUserI user) {
        return (isUserAlreadyOrdered(user)) ? FDStoreProperties.getHomepageRedesignCurrentUserContainerContentKey()
                : FDStoreProperties.getHomepageRedesignNewUserContainerContentKey();
    }

    public String getCorporateCustomerTypeByOrderCount(FDUserI user) {
        return (isUserAlreadyOrdered(user)) ? FDStoreProperties.getPropHomepageRedesignCurrentCosUserContainerContentKey()
                : FDStoreProperties.getPropHomepageRedesignNewCosUserContainerContentKey();
    }

    public boolean isUserAlreadyOrdered(FDUserI user) {
        boolean currentUser = false;
        try {
            currentUser = user.getAdjustedValidOrderCount() > 0;
        } catch (FDResourceException e) {
            LOGGER.error("User[" + user.getUserId() + "] order count evaluation failed", e);
            currentUser = false;
        }
        return currentUser;
    }

}
