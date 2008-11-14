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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SearchQueryStemmer;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDRunSearchTag extends com.freshdirect.framework.webapp.BodyTagSupport {

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

		SearchResults res = ContentSearch.getInstance().searchOld(searchFor);
		
		//System.err.println("<<< "+res.getProducts().size());
		
		// set search results in PageContext
		pageContext.setAttribute(searchResults, res);

		return EVAL_BODY_BUFFERED;
	}
}
