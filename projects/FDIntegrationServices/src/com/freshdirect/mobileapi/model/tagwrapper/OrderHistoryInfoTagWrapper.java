package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.OrderHistoryInfoTag;

public class OrderHistoryInfoTagWrapper extends GetterTagWrapper {

    protected OrderHistoryInfoTagWrapper(AbstractGetterTag wrapTarget, SessionUser user) {
        super(wrapTarget, user);
    }

    public OrderHistoryInfoTagWrapper(SessionUser sessionUser) {
        this(new OrderHistoryInfoTag(), sessionUser);
    }

    public List<FDOrderInfoI> getOrderHistoryInfo() throws FDException {
        return (List<FDOrderInfoI>) getResult();
    }
}
