package com.freshdirect.mobileapi.model.tagwrapper;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.webapp.taglib.fdstore.AgeVerificationControllerTag;

public class AgeVerificationControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName,
        MessageCodes {

    private static Category LOG = LoggerFactory.getInstance(AgeVerificationControllerTagWrapper.class);

    public AgeVerificationControllerTagWrapper(SessionUser user) {
        super(new AgeVerificationControllerTag(), user);
    }

    public ResultBundle verifyAlcoholAge() throws FDException {
        addRequestValue(REQ_PARAM_AGE_VERIFIED, "yes"); //Any non-null value here will do...
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    @Override
    protected void setResult() {
        ((AgeVerificationControllerTag) wrapTarget).setResult(ACTION_RESULT);
    }

    @Override
    protected Object getResult() throws FDException {
        if (null == pageContext.getAttribute(ACTION_RESULT)) {
            throw new IllegalAccessError("executeTagLogic() should be called before calling get result");
        }
        return pageContext.getAttribute(GET_RESULT);
    }

    private final String FAKE_BLOCK_ADDRESS_URL = "FAKE_BLOCK_ADDRESS_URL"; //Just need something to set and check against

    public ResultBundle verifyAddress(String id, DeliveryAddressType type) throws FDException {
        ((AgeVerificationControllerTag) wrapTarget).setBlockedAddressPage(FAKE_BLOCK_ADDRESS_URL);
        //addRequestValue(REQ_PARAM_AGE_VERIFIED, "yes"); //Any non-null value here will do...
        setMethodMode(false);
        ActionResult actionResult = executeTagLogic();
        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        String redirectUrl = ((HttpResponseWrapper) pageContext.getResponse()).getSendRedirectUrl();
        LOG.debug("redirectUrl is:" + redirectUrl);

        if (FAKE_BLOCK_ADDRESS_URL.equals(redirectUrl)) {
            actionResult.addError(new ActionError(ERR_ALCOHOL_DELIVERY_AREA_RESTRICTION, "Alcohol cannot be delivery to the address specified."));
        }
        return new ResultBundle(actionResult, this);
    }

}
