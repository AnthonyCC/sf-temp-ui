package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public abstract class TagWrapper extends HttpContextWrapper {

    protected static final String[] EMPTY_ARRAY = new String[0];

    public static final String GET_RESULT = "getResult";

    private final static Category LOGGER = LoggerFactory.getInstance(TagWrapper.class);

    protected TagSupport wrapTarget;

    protected abstract Object getResult() throws FDException;

    protected FDUserI getUser() {
        return (FDUserI) this.pageContext.getSession().getAttribute(SessionName.USER);
    }

    public TagWrapper(TagSupport wrapTarget, FDUserI user) {
        this.wrapTarget = wrapTarget;
        this.wrapTarget.setId(GET_RESULT);
        //Some global get/sets
        addExpectedSessionValues(new String[] { SessionName.USER }, new String[] { SessionName.USER }); //gets,sets
        addExpectedRequestValues(new String[] { GET_RESULT }, new String[] { GET_RESULT }); //gets,sets

        //Are we working pretending post or get? Default to GET
        ((HttpRequestWrapper) this.pageContext.getRequest()).setMethod(HttpRequestWrapper.METHOD_GET);

        if (null != user) {
            this.pageContext.getSession().setAttribute(SessionName.USER, user);
        }

        ((HttpSessionWrapper) this.pageContext.getSession()).addValue(SessionName.APPLICATION, user.getApplication().getCode());
        this.wrapTarget.setPageContext(pageContext);

    }

    public TagWrapper(BodyTagSupport wrapTarget) {
        this(wrapTarget, null);
    }
}
