package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.SessionUser;

/**
 * @author Rob
 *
 */
public abstract class NonStandardControllerTagWrapper extends TagWrapper {

    public static final String ACTION_RESULT = "actionResult";

    public NonStandardControllerTagWrapper(BodyTagSupport wrapTarget, SessionUser user) {
        this(wrapTarget, user.getFDSessionUser());
    }

    public NonStandardControllerTagWrapper(BodyTagSupport wrapTarget, FDUserI user) {
        super(wrapTarget, user);
        addExpectedRequestValues(new String[] { ACTION_RESULT }, new String[] { ACTION_RESULT }); //gets,sets
        setResult();
    }

    /**
     * Work around since non-standard controller tags don't have common base class that does setResult.
     * we're creating a abstract method that must be implemented on the concrete wrapper class as a reminder to
     * do wrapTarget.setResult(ACTION_RESULT);
     */
    protected abstract void setResult();

    public BodyTagSupport getWrapTarget() {
        return wrapTarget;
    }

    public ActionResult getActionResult() {
        ActionResult result = (ActionResult) pageContext.getAttribute(ACTION_RESULT);
        return (result != null ? result : new ActionResult());
    }

    protected ActionResult executeTagLogic() throws FDException {
        try {
            getWrapTarget().doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
        return getActionResult();
    }

}
