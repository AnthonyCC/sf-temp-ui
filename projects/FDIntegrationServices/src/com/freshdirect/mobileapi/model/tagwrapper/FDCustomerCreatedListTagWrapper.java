package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.mobileapi.model.CustomerCreatedList;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCreatedListTag;

/**
 * @author Rob
 *
 */
public class FDCustomerCreatedListTagWrapper extends GetterTagWrapper implements RequestParamName, SessionParamName {

    public FDCustomerCreatedListTagWrapper(SessionUser user) {
        super(new FDCustomerCreatedListTag(), user);
    }

    public List<CustomerCreatedList> getCustomerCreatedList() throws FDException {

        addExpectedRequestValues(new String[] { REQ_PARAM_CUSTOMER_LIST }, new String[] { REQ_PARAM_CUSTOMER_LIST });//gets,sets

        ((FDCustomerCreatedListTag) this.wrapTarget).setAction("loadLists");
        return CustomerCreatedList.wrap((List<FDCustomerCreatedList>) getResult());
    }
}
