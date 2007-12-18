package com.freshdirect.mktAdmin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.freshdirect.mktAdmin.service.MarketAdminServiceIntf;

public class AbstractMktAdminForm  extends SimpleFormController{

	private MarketAdminServiceIntf marketAdminService;

	

	public void afterPropertiesSet() {
		if (this.marketAdminService == null) {
			throw new IllegalArgumentException("'adminService' is required");
		}
	}

//	/**
//	 * Set up a custom property editor for the application's date format.
//	 */
//	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		dateFormat.setLenient(false);
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
//	}

	/**
	 * Method disallows duplicate form submission.
	 * Typically used to prevent duplicate insertion of entities
	 * into the datastore. Shows a new form with an error message.
	 */
	protected ModelAndView disallowDuplicateFormSubmission(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		BindException errors = getErrorsForNewForm(request);
		errors.reject("duplicateFormSubmission", "Duplicate form submission");
		return showForm(request, response, errors);
	}

	public MarketAdminServiceIntf getMarketAdminService() {
		return marketAdminService;
	}

	public void setMarketAdminService(MarketAdminServiceIntf marketAdminService) {
		this.marketAdminService = marketAdminService;
	}

	
}
