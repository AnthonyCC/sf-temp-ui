package com.freshdirect.mobileapi.model.tagwrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpRequestWrapper implements HttpServletRequest {

    Map<Object, Object> backingMap = new HashMap<Object, Object>();

    HttpSessionWrapper session;

    /*
     * @see com.freshdirect.mobileapi.model.tagwrapper.HttpSessionWrapper
     */
    Set<String> expectedSets = new HashSet<String>();

    /*
     * @see com.freshdirect.mobileapi.model.tagwrapper.HttpSessionWrapper
     */
    Set<String> expectedGets = new HashSet<String>();

    public void addExpectedSets(String[] params) {
        for (String param : params) {
            this.expectedSets.add(param);
        }
    }

    public void addExpectedGets(String[] params) {
        for (String param : params) {
            this.expectedGets.add(param);
        }
    }

    public HttpRequestWrapper(HttpSessionWrapper session) {
        this.session = session;
    }

    public void addValue(Object name, Object value) {
        //Coding defensively. Is something unexpected happens, throw exception
        if (!expectedSets.contains(name)) {
            throw new IllegalAccessError("setAttribute with param name: [" + name
                    + "] was not expected.  Perhaps JSP and Mobile Middle Layer are out of sync?");
        } else {
            backingMap.put(name, value);
        }
    }

    @Override
    public String getAuthType() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getContextPath() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Cookie[] getCookies() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public long getDateHeader(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getHeader(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Enumeration getHeaderNames() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Enumeration getHeaders(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public int getIntHeader(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    public static final String METHOD_POST = "POST";

    public static final String METHOD_GET = "GET";

    private String masqueradeMethod;

    public void setMethod(String masqueradeMethod) {
        this.masqueradeMethod = masqueradeMethod;
    }

    @Override
    public String getMethod() {
        return this.masqueradeMethod;
    }

    @Override
    public String getPathInfo() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getPathTranslated() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    @Override
    public String getRemoteUser() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    private String requestURI = "";

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public String getRequestURI() {
        return this.requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getRequestedSessionId() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getServletPath() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    @Override
    public HttpSession getSession(boolean arg0) {
        return session;
    }

    @Override
    public Principal getUserPrincipal() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isUserInRole(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Object getAttribute(String name) {
        //Coding defensively. Is something unexpected happens, throw exception
        Object param = null;
        if (!expectedGets.contains(name)) {
            throw new IllegalAccessError("getAttribute with param name: [" + name
                    + "] was not expected.  Perhaps JSP and Mobile Middle Layer are out of sync?");
        } else {
            param = backingMap.get(name);
        }
        return param;
    }

    @Override
    public Enumeration getAttributeNames() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getCharacterEncoding() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public int getContentLength() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getContentType() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getLocalAddr() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getLocalName() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public int getLocalPort() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Locale getLocale() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Enumeration getLocales() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getParameter(String name) {
        //Coding defensively. Is something unexpected happens, throw exception
        String param = null;
        if (!expectedGets.contains(name)) {
            throw new IllegalAccessError("getParameter with param name: [" + name
                    + "] was not expected.  Perhaps JSP and Mobile Middle Layer are out of sync?");
        } else {
            if (null != backingMap.get(name)) {
                param = backingMap.get(name).toString();
            } else {
                param = null;
            }
        }
        return param;
    }

    @Override
    public Map getParameterMap() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Enumeration getParameterNames() {
        //To simulate real method, we only return the ones w/ non-null values.
        ArrayList<String> filteredNonNullValues = new ArrayList<String>();
        for (Object key : backingMap.keySet()) {
            if (null != (backingMap.get(key))) {
                filteredNonNullValues.add((String) key);
            }
        }

        final Iterator wrappedKeys = filteredNonNullValues.iterator();
        return new Enumeration() {
            public boolean hasMoreElements() {
                return wrappedKeys.hasNext();
            }

            public Object nextElement() {
                return wrappedKeys.next();
            }
        };
    }

    @Override
    public String[] getParameterValues(String arg0) {
        //To simulate real method, we only return the ones w/ non-null values.
        ArrayList<String> filteredNonNullValues = new ArrayList<String>();
        for (Object value : backingMap.values()) {
            if (value != null) {
                filteredNonNullValues.add((String) value);
            }
        }
        return filteredNonNullValues.toArray(new String[filteredNonNullValues.size()]);
    }

    @Override
    public String getProtocol() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getRealPath(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getRemoteAddr() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getRemoteHost() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public int getRemotePort() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getScheme() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    private String serverName;

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getServerName() {
        return this.serverName;
    }

    @Override
    public int getServerPort() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isSecure() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void removeAttribute(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setAttribute(String name, Object value) {
        //Coding defensively. Is something unexpected happens, throw exception
        if (!expectedSets.contains(name)) {
            throw new IllegalAccessError("setAttribute with param name: [" + name
                    + "] was not expected.  Perhaps JSP and Mobile Middle Layer are out of sync?");
        } else {
            backingMap.put(name, value);
        }
    }

    @Override
    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    private String queryString;

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

}
