package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.freshdirect.mobileapi.model.ResultBundle;

/**
 * @author Rob
 *
 */
public abstract class HttpContextWrapper {

    protected PageContext pageContext = new PageContextWrapper();

    public static final String REQUEST = "request";

    public static final String SESSION = "session";

    /**
     * Used to pass session and request object to controller so that any values set in the tag 
     * wrapper can be propagated.
     * @param result
     */
    public void setParams(ResultBundle result) {
        result.addExtraData(REQUEST, pageContext.getRequest());
        result.addExtraData(SESSION, pageContext.getSession());
    }

    public void addExpectedSessionValues(String[] gets, String[] sets) {
        ((HttpSessionWrapper) pageContext.getSession()).addExpectedGets(gets);
        ((HttpSessionWrapper) pageContext.getSession()).addExpectedSets(sets);
    }

    public void setSessionId(String sessionId) {
        ((HttpSessionWrapper) pageContext.getSession()).setId(sessionId);
    }

    public void setRequestUrl(String requestURI) {
        ((HttpRequestWrapper) pageContext.getRequest()).setRequestURI(requestURI);
    }

    public void setServerName(String serverName) {
        ((HttpRequestWrapper) pageContext.getRequest()).setServerName(serverName);
    }

    public void setQueryString(String queryString) {
        ((HttpRequestWrapper) pageContext.getRequest()).setQueryString(queryString);
    }

    public void addExpectedRequestValues(String[] gets, String[] sets) {
        ((HttpRequestWrapper) pageContext.getRequest()).addExpectedGets(gets);
        ((HttpRequestWrapper) pageContext.getRequest()).addExpectedSets(sets);
    }

    protected void setMethodMode(boolean isPost) {
        ((HttpRequestWrapper) this.pageContext.getRequest()).setMethod((isPost ? HttpRequestWrapper.METHOD_POST
                : HttpRequestWrapper.METHOD_GET));
    }

    /**
     * @param values
     */
    public void addRequestValues(Map<String, String> values) {
        for (String key : values.keySet()) {
            addRequestValue(key, values.get(key));
        }
    }

    public void addRequestValue(String key, Object value) {
        addExpectedRequestValues(new String[] { key }, new String[] { key });
        this.pageContext.getRequest().setAttribute(key, value);
    }

    public void addSessionValue(String key, Object value) {
        addExpectedSessionValues(new String[] { key }, new String[] { key });
        this.pageContext.getSession().setAttribute(key, value);
    }

}
