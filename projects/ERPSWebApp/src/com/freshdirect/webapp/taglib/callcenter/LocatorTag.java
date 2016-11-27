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
					if (isWildcardOrPercentageCharacter((FDCustomerSearchCriteria) this.criteria)) {
						doErrorRedirect("Narrow down search criteria.");
					}
					else {
					searchResults = doLocateCustomers((FDCustomerSearchCriteria) this.criteria);
					}
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
			FDCustomerSearchCriteria fdSearchCriteria) {

		boolean result = true;

		// if  either one of the parameter among, email, customerId ,apartment, zipcode, order number
		//is present then proceed with the search without validating other search parameters.
		if (fdSearchCriteria.getEmail() != null
				|| fdSearchCriteria.getCustomerId() != null
				|| fdSearchCriteria.getSapId() != null
				|| fdSearchCriteria.getApartment() != null
				|| fdSearchCriteria.getZipCode() != null
				|| fdSearchCriteria.getOrderNumber() != null) {
			result = false;
		}
		// check to see if the either of the parameter does not falls under broad search category and if yes
		// proceed with the Search 
		else if (validateSearchParam(fdSearchCriteria.getFirstName())
				|| validateSearchParam(fdSearchCriteria.getLastName())
				|| validatePhoneParam(fdSearchCriteria.getPhone())
				|| validateAddressParam(fdSearchCriteria.getAddress())) {
			result = false;
		}
		return result;
	}

	private boolean validateSearchParam(String inputStr) {
		if (inputStr == null
				|| (inputStr.trim().equals("*") || inputStr.trim().equals("%") || !validateExceptionPattern(inputStr))) {
			return false;
		}

		return true;

	}
	
	private boolean validateAddressParam(String inputStr) {
		if (inputStr == null
				|| (inputStr.trim().equals("*") || inputStr.trim().equals("%") || !isPatternSearchable(inputStr,0))) {
			return false;
		}

		return true;

	}
	
	private boolean validatePhoneParam(String inputStr) {
		if (inputStr == null
				|| (inputStr.trim().equals("*") || inputStr.trim().equals("%") || !validatePhonePattern(inputStr))) {
			return false;
		}

		return true;

	}
	// Exception Pattern for first name and last name : "*a*","%a%",*a** are not supported for
	// search
	private boolean validateExceptionPattern(String inputStr) {
		
		boolean isTextPatternSearchable = true;
		
		if (inputStr.length() == 3
				&& (inputStr.charAt(0) == '*' || inputStr.charAt(0) == '%')
				&& (inputStr.charAt(2) == '*' || inputStr.charAt(2) == '%')) {
			isTextPatternSearchable = false;			
		}
		else if(inputStr.length() > 3){		
			 isTextPatternSearchable = isPatternSearchable(inputStr,2);
			
		}
		else if(inputStr.length() < 3){		
			 isTextPatternSearchable = isPatternSearchable(inputStr,0);
			
		}
		return isTextPatternSearchable;
	}
	
	// This Utility Checks if substring "inputStr.substring(index, inputStr.length())" has any character not equal to '*'
	//if yes then return false else return true
	private boolean isPatternSearchable(String inputStr,int index) {
		boolean result = true;
		inputStr = inputStr.replace('%', '*');
		if (inputStr.charAt(inputStr.length() - 1) == '*') {
			char[] arry = inputStr.substring(index, inputStr.length())
					.toCharArray();
			boolean hasValidTextForSearch = false;
			for (char c : arry) {
				if (c != '*') {
					hasValidTextForSearch = true;
					break;
				}
			}
			if (hasValidTextForSearch) {
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}	
	
	// Exception Pattern for phone is if the num of character =< 4 followed by "*" 
	// e.g Invalid Search Pattern e.g (212)*, 1*2*,12*,1* , 212*******
	
	//Valid Search Pattern *123,*12,1*2, 212****12
	private boolean validatePhonePattern(String inputStr) {
		boolean isPhonePatternSearchable = true;
		if ((inputStr.contains("*")) && inputStr.length() <= 4) {
			if (inputStr.charAt(inputStr.length() - 1) == '*' || countWildChars(inputStr)) {
				isPhonePatternSearchable = false;
			}
		} else{
			isPhonePatternSearchable = isPatternSearchable(inputStr,3);
		}
		return isPhonePatternSearchable;
	}				

	private boolean countWildChars(String inputStr) {

		int countWildCard = inputStr.length()
				- inputStr.replaceAll("\\*", "").length();
		if (countWildCard > 1) {
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