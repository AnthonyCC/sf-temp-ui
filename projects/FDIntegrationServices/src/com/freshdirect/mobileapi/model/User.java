package com.freshdirect.mobileapi.model;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;

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

    /**
     * @return
     */
    public FDUser getFDUser() {
        return this.user;
    }
}
