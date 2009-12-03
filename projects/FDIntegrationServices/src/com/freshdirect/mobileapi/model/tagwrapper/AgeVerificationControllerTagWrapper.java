package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.AgeVerificationControllerTag;

public class AgeVerificationControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {

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

}
