package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.webapp.ActionResult;
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
        addExpectedRequestValues(new String[] { REQ_PARAM_CUSTOMER_CREATED_LIST_ID, REQ_PARAM_CUSTOMER_EBT },
                new String[] { REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG, REQ_PARAM_CUSTOMER_EBT }); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_SKUS_ADDED }); //gets,sets

        return new ResultBundle(executeTagLogic(), this);
    }
    
    public ResultBundle removeSpecialRestrictedItemsFromCart(CartEvent cartEvent) throws FDException {
    	addRequestValue(RequestParamName.REQ_PARAM_CUSTOMER_EBT, "true");
    	setCartEventLoggingSetsAndGets(cartEvent);
        addExpectedRequestValues(new String[] { REQ_PARAM_CUSTOMER_CREATED_LIST_ID, REQ_PARAM_CUSTOMER_EBT },
                new String[] { REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG, REQ_PARAM_CUSTOMER_EBT }); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_SKUS_ADDED }); //gets,sets
        
        ActionResult actionResult = executeTagLogic();
        
        if (actionResult == null) {
            actionResult = new ActionResult();
        }
        
        return new ResultBundle(actionResult, this);
        
        
    }

}
