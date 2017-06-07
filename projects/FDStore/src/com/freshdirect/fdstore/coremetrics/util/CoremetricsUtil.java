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
        String result = FDStoreProperties.getHomepageRedesignNewUserContainerContentKey();
        try {
            if (user.getAdjustedValidOrderCount() > 0) {
                result = FDStoreProperties.getHomepageRedesignCurrentUserContainerContentKey();
            }
        } catch (FDResourceException e) {
            LOGGER.error("User[" + user.getUserId() + "] order count evaluation failed", e);
        }
        return result;
    }
}
