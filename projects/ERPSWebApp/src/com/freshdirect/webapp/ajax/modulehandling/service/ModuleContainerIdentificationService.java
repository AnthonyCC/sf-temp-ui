package com.freshdirect.webapp.ajax.modulehandling.service;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ModuleContainerIdentificationService {

    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleContainerIdentificationService.class);
    private static final ModuleContainerIdentificationService INSTANCE = new ModuleContainerIdentificationService();

    private ModuleContainerIdentificationService() {
    }

    public static ModuleContainerIdentificationService defaultService() {
        return INSTANCE;
    }

    public String getContainerIdByCustomerOrderCount(FDUserI user) {
        return (user.isCorporateUser()) ? getContainerIdByCorporateCustomerOrderCount(user) : getContainerIdByResidentalCustomerOrderCount(user);
    }

    public String getContainerIdByResidentalCustomerOrderCount(FDUserI user) {
        return (isUserAlreadyOrdered(user)) ? FDStoreProperties.getHomepageRedesignCurrentUserContainerContentKey()
                : FDStoreProperties.getHomepageRedesignNewUserContainerContentKey();
    }

    public String getContainerIdByCorporateCustomerOrderCount(FDUserI user) {
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
