package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.ModifyOrderControllerTag;

/**
 * @author Rob
 *
 */
public class ModifyOrderControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {

    public static final String ACTION_CANCEL_ORDER = "cancel";

    public static final String ACTION_MODIFY_ORDER = "modify";

    public static final String ACTION_CANCEL_ORDER_MODIFY = "cancelModify";

    public ModifyOrderControllerTagWrapper(SessionUser user) {
        super(new ModifyOrderControllerTag(), user);
    }

    public ResultBundle cancelOrder(String orderId) throws FDException {

        addExpectedSessionValues(new String[] { SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_APPLICATION },
                new String[] { SESSION_PARAM_DLV_PASS_SESSION_ID }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CANCEL_REASON, REQ_PARAM_CANCEL_NOTES }, new String[] {
                REQ_PARAM_ALLOW_MODIFY_ORDER, REQ_PARAM_ALLOW_RETURN_ORDER, REQ_PARAM_IS_REFUSED_ORDER, REQ_PARAM_ALLOW_CANCEL_ORDER,
                REQ_PARAM_ALLOW_COMPLAINT, REQ_PARAM_ALLOW_NEW_CHARGES, REQ_PARAM_HAS_PAYMENT_EXCEPTION, REQ_PARAM_ALLOW_RESUBMIT_ORDER });//gets,sets
        addRequestValue(REQ_PARAM_ACTION, ACTION_CANCEL_ORDER);

        ((ModifyOrderControllerTag) wrapTarget).setOrderId(orderId);
        //addRequestValue(REQ_PARAM_ORDER_ID, orderId);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    @Override
    protected void setResult() {
        ((ModifyOrderControllerTag) wrapTarget).setResult(ACTION_RESULT);
    }

    @Override
    protected Object getResult() throws FDException {
        if (null == pageContext.getAttribute(ACTION_RESULT)) {
            throw new IllegalAccessError("ex" + "ecuteTagLogic() should be called before calling get result");
        }
        return pageContext.getAttribute(GET_RESULT);
    }

    /**
     * @param orderId
     * @return
     * @throws FDException
     */
    public ResultBundle loadOrderToCartForUpdate(String orderId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_APPLICATION },
                new String[] { SESSION_PARAM_DLV_PASS_SESSION_ID, SESSION_PARAM_SMART_STORE_PREV_RECOMMENDATIONS }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CANCEL_REASON, REQ_PARAM_CANCEL_NOTES }, new String[] {
                REQ_PARAM_ALLOW_MODIFY_ORDER, REQ_PARAM_ALLOW_RETURN_ORDER, REQ_PARAM_IS_REFUSED_ORDER, REQ_PARAM_ALLOW_CANCEL_ORDER,
                REQ_PARAM_ALLOW_COMPLAINT, REQ_PARAM_ALLOW_NEW_CHARGES, REQ_PARAM_HAS_PAYMENT_EXCEPTION, REQ_PARAM_ALLOW_RESUBMIT_ORDER });//gets,sets

        addRequestValue(REQ_PARAM_ACTION, ACTION_MODIFY_ORDER);
        ((ModifyOrderControllerTag) wrapTarget).setOrderId(orderId);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    public ResultBundle cancelModify(String orderId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_APPLICATION },
                new String[] { SESSION_PARAM_DLV_PASS_SESSION_ID, SESSION_PARAM_SMART_STORE_PREV_RECOMMENDATIONS }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CANCEL_REASON, REQ_PARAM_CANCEL_NOTES }, new String[] {
                REQ_PARAM_ALLOW_MODIFY_ORDER, REQ_PARAM_ALLOW_RETURN_ORDER, REQ_PARAM_IS_REFUSED_ORDER, REQ_PARAM_ALLOW_CANCEL_ORDER,
                REQ_PARAM_ALLOW_COMPLAINT, REQ_PARAM_ALLOW_NEW_CHARGES, REQ_PARAM_HAS_PAYMENT_EXCEPTION, REQ_PARAM_ALLOW_RESUBMIT_ORDER });//gets,sets

        //Need to set 'action' slightly differently since tag is doing it differently for get.
        ((ModifyOrderControllerTag) wrapTarget).setAction(ACTION_CANCEL_ORDER_MODIFY);
        ((ModifyOrderControllerTag) wrapTarget).setOrderId(orderId);
        return new ResultBundle(executeTagLogic(), this);
    }

}
