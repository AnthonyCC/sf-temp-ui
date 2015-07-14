package com.freshdirect.mktAdmin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;

@Controller
public class AbstractMktAdminForm  {


	@Autowired
	protected MarketAdminServiceIntf marketAdminService;

	

	public void afterPropertiesSet() {
		if (this.marketAdminService == null) {
			throw new IllegalArgumentException("'adminService' is required");
		}
	}

//	/**
//	 * Set up a custom property editor for the application's date format.
//	 */


	/**
	 * Method disallows duplicate form submission.
	 * Typically used to prevent duplicate insertion of entities
	 * into the datastore. Shows a new form with an error message.
	 */
	
	
//	protected ModelAndView disallowDuplicateFormSubmission(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		BindException errors = getErrorsForNewForm(request);
//		errors.reject("duplicateFormSubmission", "Duplicate form submission");
//		return showForm(request, response, errors);
//	}

	
	
}
