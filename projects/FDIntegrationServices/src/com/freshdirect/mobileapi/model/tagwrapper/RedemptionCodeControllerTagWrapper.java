package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.RedemptionCodeControllerTag;

public class RedemptionCodeControllerTagWrapper extends ControllerTagWrapper implements RequestParamName, SessionParamName {

    public static final String ACTION_REMOVE_CODE = "removeCode";

    public static final String ACTION_REMOVE_GIFT_CARD = "removeGiftCard";

    public static final String ACTION_APPLY_CODE = "redeemCode";

    public static final String ACTION_APPLY_GIFT_CARD = "applyGiftCard";

    public static final String ACTION_DELETE_GIFT_CARD = "deleteGiftCard";

    protected RedemptionCodeControllerTagWrapper(RedemptionCodeControllerTag wrapTarget, SessionUser user) {
        this(wrapTarget, user.getFDSessionUser());
    }

    protected RedemptionCodeControllerTagWrapper(RedemptionCodeControllerTag wrapTarget, FDUserI user) {
        super(wrapTarget, user);
        ((HttpResponseWrapper) this.pageContext.getResponse()).setSuccessUrl(wrapTarget.getSuccessPage());
    }

    public RedemptionCodeControllerTagWrapper(FDUserI user) {
        this(new RedemptionCodeControllerTag(), user);
    }

    public RedemptionCodeControllerTagWrapper(SessionUser sessionUser) {
        this(new RedemptionCodeControllerTag(), sessionUser.getFDSessionUser());
    }

    /**
     * @param redemptionCode
     * @return
     * @throws FDException
     */
    public ResultBundle applyRedemptionCode(String redemptionCode) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_RECENT_ORDER_NUMBER }, new String[] { REQ_PARAM_REDEEM_OVERRIDE_MSG });//gets,sets

        addRequestValue(REQ_PARAM_REDEMPTION_CODE, redemptionCode);
        addRequestValue(REQ_PARAM_IS_PROMO_ELIGIBLE, false);
        addRequestValue(REQ_PARAM_PROMO_ERROR_FLAG, "");
        getWrapTarget().setActionName(ACTION_APPLY_CODE);
        setMethodMode(true);
        ResultBundle result = new ResultBundle(executeTagLogic(), this);
        return result;
    }

    /**
     * @param redemptionCode
     * @return
     * @throws FDException
     */
    public ResultBundle removeRedemptionCode(String redemptionCode) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_ACTION, REQ_PARAM_RECENT_ORDER_NUMBER },
                new String[] { REQ_PARAM_REDEEM_OVERRIDE_MSG });//gets,sets

        addRequestValue(REQ_PARAM_REDEMPTION_CODE, redemptionCode);
        addRequestValue(REQ_PARAM_ACTION, ACTION_REMOVE_CODE);
        //getWrapTarget().setActionName(ACTION_REMOVE_CODE);
        
        ResultBundle result = new ResultBundle(executeTagLogic(), this);
        return result;
    }

}
