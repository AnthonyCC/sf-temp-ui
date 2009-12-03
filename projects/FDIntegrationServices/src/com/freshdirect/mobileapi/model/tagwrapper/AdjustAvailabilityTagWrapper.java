package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.CartEvent;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.AdjustAvailabilityTag;

public class AdjustAvailabilityTagWrapper extends CartEventTagWrapper {

    public AdjustAvailabilityTagWrapper(SessionUser user) {
        super(new AdjustAvailabilityTag(), user);
    }


    public ResultBundle removeUnavailableItemsFromCart(CartEvent cartEvent) throws FDException {
        setCartEventLoggingSetsAndGets(cartEvent);
        addExpectedRequestValues(new String[] { REQ_PARAM_CUSTOMER_CREATED_LIST_ID },
                new String[] { REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG }); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_SKUS_ADDED }); //gets,sets

        return new ResultBundle(executeTagLogic(), this);
    }

}
