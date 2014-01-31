/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import java.util.*;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.apache.log4j.*;

import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class LocatorTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(LocatorTag.class);

	private String originalPage;
	private String results;
	private String searchType;

	private FDSearchCriteria criteria;

	public void setOriginalPage(String s) {
		this.originalPage = s;
	}

	public void setResults(String s) {
		this.results = s.trim();
	}

	public void setSearchType(String s) {
		this.searchType = s.trim();
	}

	public void setSearchCriteria(FDSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public int doStartTag() throws JspException {
		//
		// get the user's session, current request
		//
		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		List searchResults = null;

		try {
			if (request.getMethod().equals("POST")) {
				if(this.criteria.isBlank()){
					System.out.println("trying to locate the blank");
					searchResults = Collections.EMPTY_LIST;
				} else if (this.criteria instanceof FDCustomerSearchCriteria) {
					System.out.println("trying to locate the customers");
					if (isWildcardOrPercentageCharacter(criteria)) {
						doErrorRedirect("Narrow down search criteria.");
					}
					searchResults = doLocateCustomers((FDCustomerSearchCriteria) this.criteria);
				} else if (this.criteria instanceof FDOrderSearchCriteria) {
					System.out.println("trying to locate the orders");
					searchResults = doLocateOrders((FDOrderSearchCriteria) this.criteria);
				} else {
					//
					// Unexpected condition
					//
					LOGGER.debug("Unexpected search type (" + searchType + ") found");
				}
				session.setAttribute(SessionName.CC_SEARCH_RESULTS, searchResults);
			} else {
				searchResults = (List) session.getAttribute(SessionName.CC_SEARCH_RESULTS);
			}
			if (searchResults.isEmpty()) {
				doErrorRedirect("No results found for the criteria entered.");
				return SKIP_BODY;
			}

		} catch (FDResourceException fe) {
			LOGGER.error("FDResourceException trying to locate " + searchType, fe);
			doErrorRedirect("Sorry, we're experiencing technical difficulties. Please try again later.");
			return SKIP_BODY;
		}

		pageContext.setAttribute(results, searchResults);
		return EVAL_BODY_BUFFERED;

	}

	// APPDEV-2801
	private boolean isWildcardOrPercentageCharacter(
			FDSearchCriteria fdSearchCriteria) {

		boolean result = false;

		// if email or customerId is present go ahead with search and no need to
		// validate other search parameters.
		if (fdSearchCriteria.getEmail() != null
				|| fdSearchCriteria.getCustomerId() != null) {
			result = false;
		}

		// check to see if the parameters falls under broad category and if yes
		// show the validation message.
		else if (validateSearchParam(fdSearchCriteria.getFirstName())
				&& validateSearchParam(fdSearchCriteria.getLastName())
				&& validateSearchParam(fdSearchCriteria.getPhone())) {
			result = true;
		}
		return result;
	}

	private boolean validateSearchParam(String inputStr) {
		if (inputStr == null
				|| (inputStr.trim().equals("*") || inputStr.trim().equals("%") || validateExceptionPattern(inputStr))) {
			return true;
		}

		return false;

	}

	// Exception Pattern for example "*a*" is not supported for search
	private boolean validateExceptionPattern(String inputStr) {
		if (inputStr.length() == 3 && inputStr.charAt(0) == '*'
				&& inputStr.charAt(2) == '*') {
			return true;
		}
		return false;
	}

	private void doErrorRedirect(String errorMsg) {
		HttpSession session = pageContext.getSession();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			session.setAttribute("search_type", searchType);
			session.setAttribute("error_msg", errorMsg);
			response.sendRedirect(response.encodeRedirectURL(this.originalPage));
			JspWriter writer = pageContext.getOut();
			writer.close();

		} catch (IOException ioe) {
			//
			// Error during redirect
			//
			LOGGER.error("IOException while redirecting...", ioe);
		}

	}

	/**
	 * @return Collection of CustomerSearchResult objects
	 *
	 */
	private List doLocateCustomers(FDCustomerSearchCriteria criteria) throws FDResourceException {

		return (FDCustomerManager.locateCustomers(criteria));

	}

	/**
	 * @return Collection of FDOrderI objects
	 *
	 */
	private List doLocateOrders(FDOrderSearchCriteria criteria) throws FDResourceException {
		return FDCustomerManager.locateOrders(criteria);
	}

}