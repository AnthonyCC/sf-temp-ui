/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.batch;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class BatchSearchTag extends com.freshdirect.framework.webapp.BodyTagSupport {

    private Collection searchResults = null;
    private String sessionName = "freshdirect.batch.searchresults";
    
    private String results = null;
    
    public String getResults() {
        return this.results;
    }
    
    public void setResults(String results) {
        this.results = results;
    }
    
    public int doStartTag() throws JspException {
        //
        // get the user's session, current request, and intended action
        //
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        doSearch(request);
        //
        // put useful stuff in the session context
        //
        session.setAttribute(sessionName, searchResults);
        
        return EVAL_BODY_BUFFERED;
    }
    
    
    private void doSearch(HttpServletRequest request) throws JspException {
        //
        // get the search parameters
        //  
        try {
            
            pageContext.setAttribute(results, ErpFactory.getInstance().getBatches());
           
        } catch (FDResourceException fdre) {
            throw new JspException(fdre.getMessage());
        }
    }
    
    
}
