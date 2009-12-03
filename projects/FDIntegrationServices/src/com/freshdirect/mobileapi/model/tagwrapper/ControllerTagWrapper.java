package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

/**
 * @author Rob
 *
 */
public abstract class ControllerTagWrapper extends TagWrapper {

    public static final String ACTION_RESULT = "actionResult";

    public ControllerTagWrapper(AbstractControllerTag wrapTarget, SessionUser user) {
        this(wrapTarget, user.getFDSessionUser());
    }

    public ControllerTagWrapper(AbstractControllerTag wrapTarget, FDUserI user) {
        super(wrapTarget, user);
        wrapTarget.setResult(ACTION_RESULT);
        addExpectedRequestValues(new String[] { ACTION_RESULT }, new String[] { ACTION_RESULT }); //gets,sets
    }

    public AbstractControllerTag getWrapTarget() {
        return (AbstractControllerTag) wrapTarget;
    }

    public ActionResult getActionResult() {
        return (ActionResult) pageContext.getAttribute(ACTION_RESULT);
    }

    protected ActionResult executeTagLogic() throws FDException {
        try {
            getWrapTarget().doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
        return getActionResult();
    }

    @Override
    public Object getResult() throws FDException {
        if (null == pageContext.getAttribute(ACTION_RESULT)) {
            throw new IllegalAccessError("executeTagLogic() should be called before calling get result");
        }
        return pageContext.getAttribute(GET_RESULT);
    }

}
