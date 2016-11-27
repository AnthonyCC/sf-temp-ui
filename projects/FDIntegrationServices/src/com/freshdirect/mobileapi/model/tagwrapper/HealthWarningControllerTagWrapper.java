package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.AgeVerificationControllerTag;
import com.freshdirect.webapp.taglib.fdstore.HealthWarningControllerTag;

public class HealthWarningControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {

    public HealthWarningControllerTagWrapper(SessionUser user) {
        super(new HealthWarningControllerTag(), user);
    }

    public ResultBundle acknowledgeHealthWarning() throws FDException {
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    @Override
    protected void setResult() {
        ((HealthWarningControllerTag) wrapTarget).setResult(ACTION_RESULT);
    }

    @Override
    protected Object getResult() throws FDException {
        if (null == pageContext.getAttribute(ACTION_RESULT)) {
            throw new IllegalAccessError("executeTagLogic() should be called before calling get result");
        }
        return pageContext.getAttribute(GET_RESULT);
    }

}
