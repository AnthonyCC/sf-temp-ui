package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.mobileapi.model.Order;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.GetOrderTag;

/**
 * @author Rob
 *
 */
public class GetOrderTagWrapper extends GetterTagWrapper {

    public GetOrderTagWrapper(SessionUser user) {
        super(new GetOrderTag(), user);
    }

    public Order getOrder(String saleId) throws FDException {
        ((GetOrderTag) this.wrapTarget).setSaleId(saleId);
        return Order.wrap((FDOrderI) getResult());
    }
}
