package com.freshdirect.mobileapi.api.service;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.stereotype.Component;

import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;

@Component
public class PasswordService {

    private static Category LOGGER = LoggerFactory.getInstance(PasswordService.class);

    public boolean isTokenExpired(String email, String token) throws FDResourceException {
        return FDCustomerManager.isPasswordRequestExpired(email, token);
    }

    public void changeForgotPassword(HttpSession session, String email, String password) throws FDResourceException, ErpInvalidPasswordException {
        FDCustomerManager.changePassword(AccountActivityUtil.getActionInfo(session), email, password);
        LOGGER.debug("Password has been changed for email: " + email);
    }

}
