package com.freshdirect.transadmin.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractFormController extends BaseFormController {

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");

		if (StringUtils.hasText(id)) {
			Object  tmp = getBackingObject(id);
			return tmp;
		} else {
			return getDefaultBackingObject();
		}
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
				
		String messageKey = isNew(command) ? "app.actionmessage.101"
				: "app.actionmessage.102";

		preProcessDomainObject(command);
		List errorList = saveDomainObject(command);

		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));
		if(errorList == null || errorList.isEmpty()) {
			saveMessage(request, getMessage(messageKey,
					new Object[] { getDomainObjectName() }));
		} else {
			saveErrorMessage(request, errorList);
		}
		return mav;
	}

	public abstract Object getBackingObject(String id);

	public abstract Object getDefaultBackingObject();

	public abstract boolean isNew(Object command);

	public abstract String getDomainObjectName();

	public abstract List saveDomainObject(Object domainObject);

	protected void preProcessDomainObject(Object domainObject) {
		// Default Impl
	}
		
}
