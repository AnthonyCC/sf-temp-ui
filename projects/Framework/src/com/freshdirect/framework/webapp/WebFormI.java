package com.freshdirect.framework.webapp;

import javax.servlet.http.HttpServletRequest;

public interface WebFormI {

	public abstract void populateForm(HttpServletRequest request);

	public abstract void validateForm(ActionResult result);

}
