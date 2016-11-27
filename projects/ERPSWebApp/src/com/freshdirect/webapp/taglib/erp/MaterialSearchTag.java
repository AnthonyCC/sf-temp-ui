/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.erp;

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.erp.*;
import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class MaterialSearchTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(MaterialSearchTag.class);
    
    private Collection searchResults        = null;
    private String resultsSessionName       = "freshdirect.material.searchresults";
    private String searchtermSessionName    = "freshdirect.material.searchterm";
    private String searchtypeSessionName    = "freshdirect.material.searchtype";
    
    private String results = null;
    
    public String getResults() {
        return this.results;
    }
    
    public void setResults(String results) {
        this.results = results;
    }
    
    private String searchterm = "";
    
    public String getSearchterm() {
        return this.searchterm;
    }
    
    public void setSearchterm(String sterm) {
        this.searchterm = sterm;
    }
    
    private String searchtype = "";
    
    public String getSearchtype() {
        return this.searchtype;
    }
    
    public void setSearchtype(String stype) {
        this.searchtype = stype;
    }
    
    public int doStartTag() throws JspException {
        //
        // get the user's session, current request
        //
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        //
        // do a little parameter cleanup
        //
        if (searchterm != null)
            searchterm = searchterm.trim();
        else
            searchterm = "";
        if (searchtype != null)
            searchtype = searchtype.trim();
        else
            searchtype = "";
        
        if (!("".equals(searchterm) || "".equals(searchtype))) {
            //
            // do a search if both searchterm and searchtype are defined
            //
            doSearch(request);
        } else {
            //
            // otherwise recall the results from the last search
            //
            searchterm = (String) session.getAttribute(searchtermSessionName);
            if (searchterm == null) searchterm = "";
            searchtype = (String) session.getAttribute(searchtypeSessionName);
            if (searchtype == null) searchtype = "";
            searchResults = (ArrayList) session.getAttribute(resultsSessionName);
            if (searchResults == null) searchResults = new ArrayList();
        }
        //
        // put useful stuff in the session context for recall later if needed
        //
        session.setAttribute(searchtermSessionName, searchterm);
        session.setAttribute(searchtypeSessionName, searchtype);
        session.setAttribute(resultsSessionName, searchResults);
        //
        // put stuff in the page context as scripting variables
        //
        pageContext.setAttribute("searchterm", searchterm);
        pageContext.setAttribute("searchtype", searchtype);
        pageContext.setAttribute(results, searchResults);
        //
        // evaluate the body
        //
        return EVAL_BODY_BUFFERED;
    }
    
    
    private void doSearch(HttpServletRequest request) throws JspException {
    
        try {
            ErpFactory factory = ErpFactory.getInstance();
            
            if ("SAPID".equalsIgnoreCase(searchtype)) {
                searchResults = factory.findMaterialsBySapId(searchterm);
            } else if ("WEBID".equalsIgnoreCase(searchtype)) {
                searchResults = factory.findMaterialsBySku(searchterm);
            } else if ("DESCR".equalsIgnoreCase(searchtype)) {
                searchResults = factory.findMaterialsByDescription(searchterm);
            } else if ("CLASS".equalsIgnoreCase(searchtype)) {
                searchResults = factory.findMaterialsByClass(searchterm);
            } else if ("CHARC".equalsIgnoreCase(searchtype)) {
                searchResults = factory.findMaterialsByCharacteristic(searchterm);
            } else if ("BATCH".equalsIgnoreCase(searchtype)) {
                try {
                    int batchNum = Integer.parseInt(searchterm);
                    searchResults = factory.findMaterialsByBatch(batchNum);
                } catch (NumberFormatException nfe) {
                    // eat it, if its not a number, no results found...
                }
            }
            
        } catch (FDResourceException fdre) {
            LOGGER.debug(fdre);
            throw new JspException(fdre.getMessage());
        }
        
    }
    
    
}
