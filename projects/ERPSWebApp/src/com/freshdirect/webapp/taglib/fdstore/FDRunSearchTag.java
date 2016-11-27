/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.http.HttpHeaders;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.search.SearchService;
import com.freshdirect.webapp.util.RequestUtil;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDRunSearchTag extends com.freshdirect.framework.webapp.BodyTagSupport {

    private static final long serialVersionUID = 8024414108195783512L;

    private static Category LOGGER = LoggerFactory.getInstance(FDRunSearchTag.class);

    private String searchFor;
    private String errorPage;
    private String searchResults; // search results key

    public void setSearchFor(String s) {
        this.searchFor = s;
    }

    public void setErrorPage(String s) {
        this.errorPage = s;
    }

    public void setSearchResults(String s) {
        this.searchResults = s;
    }

    @Override
    public int doStartTag() throws JspException {
        //
        // Sanity check the search criteria before performing search
        //
        if (searchFor == null || "".equals(searchFor.trim())) {
            LOGGER.debug("search criteria was null or empty");
            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
            try {
                response.sendRedirect(errorPage);
                return SKIP_BODY;
            } catch (IOException ex) {
                throw new JspException();
            }
        }

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
        SearchResults res = SearchService.getInstance().searchProducts(searchFor, request.getCookies(), user, RequestUtil.getFullRequestUrl(request),
                request.getHeader(HttpHeaders.REFERER));

        // set search results in PageContext
        pageContext.setAttribute(searchResults, res);

        return EVAL_BODY_BUFFERED;
    }
}
