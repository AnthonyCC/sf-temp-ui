package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.mobileapi.model.CartEvent;
import com.freshdirect.mobileapi.model.SessionUser;

public abstract class CartEventTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {

    public CartEventTagWrapper(BodyTagSupport wrapTarget, SessionUser user) {
        super(wrapTarget, user.getFDSessionUser());
    }

    protected void setCartEventLoggingSetsAndGets(CartEvent cartEvent) {
        setRequestUrl(cartEvent.getRequestURI());

        setQueryString(cartEvent.getQueryString());

        setServerName(cartEvent.getServer());

        addRequestValue(REQ_PARAM_IMPRESSESION_ID, cartEvent.getImpressionId());

        //For Add to Cart Only
        if (CartEvent.FD_ADD_TO_CART_EVENT.equalsIgnoreCase(cartEvent.getEventType())) {
            addRequestValue(REQ_PARAM_TRACKING_CODE, cartEvent.getTrackingCode());
            addRequestValue(REQ_PARAM_TRACKING_CODE_EXT, cartEvent.getTrackingCodeEx());
        }
    }


    @Override
    protected void setResult() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Object getResult() throws FDException {
        // TODO Auto-generated method stub
        return null;
    }
}
