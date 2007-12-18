package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class BaseFormController extends SimpleFormController {

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {
		super.initBinder(request, dataBinder);
	}

	public void saveMessage(HttpServletRequest request, Object msg) {
		List messages = (List) request.getSession().getAttribute("messages");
		if (messages == null) {
			messages = new ArrayList();
		}
		messages.add(msg);
		request.getSession().setAttribute("messages", messages);
	}

	public String getMessage(String key, Object[] param) {

		return getMessageSourceAccessor().getMessage(key, param);
	}
	
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

	
}
