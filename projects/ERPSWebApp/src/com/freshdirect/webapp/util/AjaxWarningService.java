package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.FDAjaxWarning;

public class AjaxWarningService {

    private static final AjaxWarningService INSTANCE = new AjaxWarningService();

    private AjaxWarningService() {
    }

    public static AjaxWarningService defaultService() {
        return INSTANCE;
    }

    public FDAjaxWarning generateAjaxWarning(FDResourceException exception, FDUserI user) {
        String errorMessage = AjaxErrorHandlingService.defaultService().populateErrorMessage(exception.getMessage(),
                null != user ? user.getCustomerServiceContact() : "1-866-283-7374");
        String primaryMessage = AjaxErrorHandlingService.defaultService().getPrimaryErrorMessage(errorMessage);
        String secondaryMessage = AjaxErrorHandlingService.defaultService().getSecondaryErrorMessage(errorMessage);
        return new FDAjaxWarning(primaryMessage, secondaryMessage);
    }
}
