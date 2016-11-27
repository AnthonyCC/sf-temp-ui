/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.apache.log4j.*;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDParseSearchTermsTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER 			= LoggerFactory.getInstance( FDParseSearchTermsTag.class );

    //private String searchListSessionName	= "freshdirect.search.searchlist";
    //private String rawSearchListSessionName	= "freshdirect.search.searchlist.raw";

	private String searchList				= null;		// searchList key
    //private ArrayList searchListValue		= null;		// searchList value (for use with bulk searches)

    private String searchFor				= null;		// searchFor key
    private String searchForValue			= null;		// searchFor value

    private String searchParams				= null;
    private String isBulkSearch				= null;


    public String getSearchList() {
        return this.searchList;
    }

    public void setSearchList(String s) {
        this.searchList = s;
    }

    public String getSearchFor() {
        return this.searchFor;
    }

    public void setSearchFor(String s) {
		this.searchFor = s;
    }

    public String getSearchParams() {
        return this.searchParams;
    }

    public void setSearchParams(String s) {
        this.searchParams = s;
    }

    public String getIsBulkSearch() {
        return this.isBulkSearch;
    }

    public void setIsBulkSearch(String s) {
		this.isBulkSearch = s;
    }


    public int doStartTag() throws JspException {

		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		List searchListValue = null;
		//
		// Check for SINGLE or BULK search
		//
		if ( !"true".equalsIgnoreCase(isBulkSearch) ) {
			//
			// SINGLE search
			//
			if (searchParams == null || "".equals( searchParams.trim() ) ) {
				searchForValue = "";
			} else {
				searchForValue = searchParams.trim();
			}

		} else {
			//
			// BULK search
			//
			LOGGER.debug("This is a bulk search.");

			if (request.getParameter("search_pad") != null) {
				//
				// Build the search list from the comma-delimited String in request or session
				//
				LOGGER.debug("Found search string in REQUEST");
				searchListValue = getSearchList(searchParams);
				session.setAttribute(LIST_SEARCH_RAW, searchParams);
			}
			else {
				//
				// No search string in request; check the session
				//
				LOGGER.debug("Looking for search string in SESSION");
				searchListValue = getSearchList((String) session.getAttribute(LIST_SEARCH_RAW));
			}
			//
			// Retrieve searchIndex from request & sanity check it.
			//
			int searchIndex = 0;

			if (request.getParameter("searchIndex") != null) {
				searchIndex = Integer.parseInt( request.getParameter("searchIndex") );
			}

			if ( searchIndex != 0 && searchIndex >= searchListValue.size() ) {
				searchIndex = searchListValue.size() - 1;
			}
			
			LOGGER.debug("searchIndex is " + searchIndex);
			LOGGER.debug("searchListValue.size() is " + searchListValue.size());

			//
			// Check for additional search term and insert into current position (i.e., searchIndex)
			//
			if (request.getParameter("new_term") != null && !"".equals( request.getParameter("new_term").trim() ) ) {
				searchIndex = searchListValue.size();
				searchListValue.add( searchIndex, request.getParameter("new_term") );
				//
				// Reconstruct the searchParams string to keep this term around for next time
				//
				StringBuffer paramsBuf = new StringBuffer();
				for (Iterator it = searchListValue.iterator(); it.hasNext(); ) {
					paramsBuf.append( (String) it.next() );
					if ( it.hasNext() ) { paramsBuf.append(","); }
				}
				session.removeAttribute(LIST_SEARCH_RAW);
				session.setAttribute( LIST_SEARCH_RAW, paramsBuf.toString() );
			}
			//
			// Set search term variable
			//
			if (searchListValue.size() > 0) {
				searchForValue = (String) searchListValue.get(searchIndex);
			} else {
				searchForValue = "";
			}
			//
			// Set only for bulk searches
			//
			pageContext.setAttribute(searchList, searchListValue);
		}

        pageContext.setAttribute(searchFor, searchForValue);
        return (EVAL_BODY_BUFFERED);

    } // method doStartTag


    private List getSearchList(String params) {
		List list = new ArrayList();
		if (params != null && !"".equals(params.trim())) {
			StringTokenizer tokenizer = new StringTokenizer(params, ",\r\n\f\"\'");
			while (tokenizer.hasMoreTokens()) {
				String currentToken = tokenizer.nextToken().trim();
				if ( !"".equals(currentToken) ) {
					list.add(currentToken);
				}
			}
		}
		return list;
	}


} // class FDParseSearchTermsTag
