package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.QuickShopControllerTag;

/**
 * Wrapper for {@see com.freshdirect.webapp.taglib.fdstore.QuickShopControllerTag} 
 * @author fgarcia
 *
 */
public class QuickShopControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {
    public static final String QUICK_CART_ID = "quickCart";

    public QuickShopControllerTagWrapper(SessionUser user) {
        super(new QuickShopControllerTag(), user);
    }

    public ResultBundle getQuickCartFromCustomerCreatedList(String customerCreatedListId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_USER, SESSION_PARAM_FD_QUICKCART },
                new String[] { SESSION_PARAM_FD_QUICKCART }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_ORDER_ID, REQ_PARAM_FD_ACTION, REQ_PARAM_QUICK_SHOP_DEPT_ID, REQ_PARAM_SORT_BY,
                REQ_PARAM_QUICK_CART }, new String[] { REQ_PARAM_QUICK_CART, "loadedCclList" });//gets,sets

        ((QuickShopControllerTag) wrapTarget).setCcListId(customerCreatedListId);
        ((QuickShopControllerTag) wrapTarget).setId(QUICK_CART_ID);
        ((QuickShopControllerTag) wrapTarget).setAction("");

        ResultBundle result = new ResultBundle(executeTagLogic(), this);

        result.addExtraData(QUICK_CART_ID, getResult());

        return result;
    }

    public ResultBundle getQuickCartFromOrder(String orderId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_USER, SESSION_PARAM_FD_QUICKCART },
                new String[] { SESSION_PARAM_FD_QUICKCART }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_ORDER_ID, REQ_PARAM_FD_ACTION, REQ_PARAM_QUICK_SHOP_DEPT_ID, REQ_PARAM_SORT_BY,
                REQ_PARAM_QUICK_CART }, new String[] { REQ_PARAM_QUICK_CART });//gets,sets

        ((QuickShopControllerTag) wrapTarget).setOrderId(orderId);
        ((QuickShopControllerTag) wrapTarget).setId(QUICK_CART_ID);

        ResultBundle result = new ResultBundle(executeTagLogic(), this);

        result.addExtraData(QUICK_CART_ID, getResult());

        return result;
    }

    @Override
    protected void setResult() {
        // Nothing
    }

    @Override
    protected Object getResult() throws FDException {
        return this.pageContext.getAttribute(QUICK_CART_ID);
    }
}
