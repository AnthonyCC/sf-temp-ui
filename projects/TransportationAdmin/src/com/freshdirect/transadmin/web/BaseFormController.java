package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TimeOfDayPropertyEditor;

public class BaseFormController extends SimpleFormController {

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {				
		CustomDateEditor editor = new CustomDateEditor(TransStringUtil.dateFormat, true);
		dataBinder.registerCustomEditor(Date.class, editor);	
		dataBinder.registerCustomEditor(TimeOfDay.class, new TimeOfDayPropertyEditor());
            
		dataBinder.registerCustomEditor(TimeOfDay.class, new TimeOfDayPropertyEditor());
           
	}

	public void saveMessage(HttpServletRequest request, Object msg) {
		List messages = (List) request.getSession().getAttribute("messages");
		if (messages == null) {
			messages = new ArrayList();
		}
		if(msg instanceof Collection) {
			messages.addAll((Collection)msg);
		} else {
			messages.add(msg);
		}
		request.getSession().setAttribute("messages", messages);
	}
	
	public void saveErrorMessage(HttpServletRequest request, Object msg) {
		List messages = (List) request.getSession().getAttribute("apperrors");
		if (messages == null) {
			messages = new ArrayList();
		}
		if(msg instanceof Collection) {
			messages.addAll((Collection)msg);
		} else {
			messages.add(msg);
		}
		request.getSession().setAttribute("apperrors", messages);
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

	protected String getServerDate(String dispDate) {
		String retDate = null;
		try {
			retDate = TransStringUtil.getServerDate(dispDate);
		} catch(ParseException parExp) {
			parExp.printStackTrace();
		}
		return retDate;
	}
	
}
