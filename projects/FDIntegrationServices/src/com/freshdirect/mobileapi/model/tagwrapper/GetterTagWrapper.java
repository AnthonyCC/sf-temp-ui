package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public abstract class GetterTagWrapper extends TagWrapper { 

    public GetterTagWrapper(AbstractGetterTag wrapTarget) {
        super(wrapTarget);
    }

    public GetterTagWrapper(AbstractGetterTag wrapTarget, SessionUser user) {
        super(wrapTarget, user.getFDSessionUser());
    }

    public AbstractGetterTag getWrapTarget() {
        return (AbstractGetterTag) wrapTarget;
    }

    @Override
    protected Object getResult() throws FDException {
        try {
            wrapTarget.doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
        return pageContext.getAttribute(GET_RESULT);
    }
}
