package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class HttpSessionWrapper implements HttpSession {

    private final static Category LOGGER = LoggerFactory.getInstance(HttpSessionWrapper.class);

    Map<Object, Object> backingMap = new HashMap<Object, Object>();

    Set<String> expectedSets = new HashSet<String>();

    Set<String> expectedGets = new HashSet<String>();

    private String id;

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

    public void addValue(Object key, Object value) {
        backingMap.put(key, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String name) {
        //Coding defensively. Is something unexpected happens, throw exception
        Object attribute = null;
        if (!expectedGets.contains(name)) {
            throw new IllegalAccessError("getAttribute with param name: [" + name
                    + "] was not expected.  Perhaps JSP and Mobile Middle Layer are out of sync?");
        } else {
            attribute = backingMap.get(name);
        }
        return attribute;
    }

    private Object getAttributeUnchecked(String name) {
        return backingMap.get(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public long getCreationTime() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String sessionId) {
        this.id = sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public int getMaxInactiveInterval() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public ServletContext getServletContext() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Object getValue(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String[] getValueNames() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void invalidate() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isNew() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void putValue(String arg0, Object arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void removeAttribute(String name) {
        //Coding defensively. Is something unexpected happens, throw exception
        if (!expectedSets.contains(name)) {
            throw new IllegalAccessError("setAttribute with param name: [" + name
                    + "] was not expected.  Perhaps JSP and Mobile Middle Layer are out of sync?");
        } else {
            backingMap.remove(name);
        }
    }

    @Override
    public void removeValue(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    public void propogateSetValues(HttpSession actualSession) {
        for (String name : this.expectedSets) {
            Object value = getAttributeUnchecked(name);
            if (LOGGER.isDebugEnabled()) {
                //Since value.toString may be expensive 
                LOGGER.debug("passing name=[" + name + "] from wrapper to actual session with value="
                        + (value == null ? "null" : value.toString()));
            }
            actualSession.setAttribute(name, value);
        }
    }

    public Set<String> getExpectedSetValues() {
        return this.expectedSets;
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
    public void setMaxInactiveInterval(int arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

}
