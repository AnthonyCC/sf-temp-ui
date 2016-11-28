package com.freshdirect.mobileapi.model.tagwrapper;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTag;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public abstract class SimpleTagWrapper extends HttpContextWrapper {

    protected SimpleTag wrapTarget;
    
    public SimpleTagWrapper(SimpleTag wrapTarget, FDUserI user) {
        this.wrapTarget = wrapTarget;
        //Some global get/sets
        addExpectedSessionValues(new String[] { SessionName.USER }, new String[] { SessionName.USER }); //gets,sets
        addExpectedRequestValues(new String[] { GET_RESULT }, new String[] { GET_RESULT }); //gets,sets

        ((HttpRequestWrapper) this.pageContext.getRequest()).setMethod(HttpRequestWrapper.METHOD_GET);

        if (null != user) {
            this.pageContext.getSession().setAttribute(SessionName.USER, user);
            ((HttpSessionWrapper) this.pageContext.getSession()).addValue(SessionName.APPLICATION, user.getApplication().getCode());
        }
        this.wrapTarget.setJspContext(pageContext);
    }
    
    protected ActionResult executeTagLogic(String resultProperty) throws FDException {
        try {
            wrapTarget.doTag();
        } catch (JspException e) {
            throw new FDException(e);
        } catch (IOException e) {
            throw new FDException(e);
        }
        return (ActionResult) pageContext.getAttribute(resultProperty);
    }
    
    protected ActionResult executeTagLogic() throws FDException {
        return executeTagLogic(GET_RESULT);
    }
}
