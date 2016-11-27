package com.freshdirect.mobileapi.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.tagwrapper.SiteAccessControllerTagWrapper;

/**
 * This is a wrapper on FDUser. FDUser and FDSessionUser are pretty much identical.
 * One exception is that FDSessionUser has logic for persisting user data.
 * Use SessionUser (wrapper for FDSessionUser) for all user related operation.
 * Use this object for login related operation
 * 
 * @author Rob
 *
 */
public class User {

    private FDUser user;

    /**
     * @param user
     * @return
     */
    public static User wrap(FDUser user) {
        User newInstance = new User();
        newInstance.user = user;
        return newInstance;
    }

    /**
     * @param username
     * @param password
     * @return
     * @throws FDAuthenticationException
     * @throws FDResourceException
     */
    public static User login(String username, String password) throws FDAuthenticationException, FDResourceException {
        FDIdentity identity = FDCustomerManager.login(username, password);
        FDUser loginUser = FDCustomerManager.recognize(identity);
        return wrap(loginUser);
    }

    public static boolean authenticate(String username, String password) throws FDResourceException {
        boolean authenticated = false;
        try {
            FDCustomerManager.login(username, password);
            authenticated = true;
        } catch (FDAuthenticationException e) {
            //Just pass as authentication error.
        }
        return authenticated;
    }

    /**
     * @return
     */
    public FDUser getFDUser() {
        return this.user;
    }
}
