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

import com.freshdirect.erp.model.*;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ProductSearchTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ProductSearchTag.class);
    
    private Collection searchResults        = null;
    private String resultsSessionName       = "freshdirect.product.searchresults";
    private String searchtermSessionName    = "freshdirect.product.searchterm";
    private String searchtypeSessionName    = "freshdirect.product.searchtype";
    
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
                searchResults = factory.findProductsBySapId(searchterm);
            } else if ("WEBID".equalsIgnoreCase(searchtype)) {
                if (searchterm.length() > 7) {
                    searchResults = new ArrayList();
                    ErpProductInfoModel infoModel = factory.findProductBySku(searchterm);
                    if (infoModel != null) searchResults.add(infoModel);
                } else {
                    searchResults = factory.findProductsLikeSku(searchterm);
                }
            } else if ("DESCR".equalsIgnoreCase(searchtype)) {
                searchResults = factory.findProductsByDescription(searchterm);
            } else if ("UPC".equalsIgnoreCase(searchtype)) {
                if ((searchterm.length() > 2) && (searchterm.length() < 8)) {
                    searchResults = factory.findProductsLikeUPC(searchterm);
                } else {
                    searchResults = factory.findProductsByUPC(searchterm);
                }
            }
            
        } catch (FDResourceException fdre) {
            LOGGER.debug(fdre);
            throw new JspException(fdre.getMessage());
        }
        
    }
    
    
}
