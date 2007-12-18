package com.freshdirect.webapp.action;

import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;

public abstract class WebActionSupport implements Action, HttpContextAware, ResultAware {

	private HttpContext webActionContext;
	
	private ActionResult result;
	
	public void setHttpContext(HttpContext webActionContext) {
		this.webActionContext = webActionContext;
	}

	public HttpContext getWebActionContext() {
		return webActionContext;
	}

	public ActionResult getResult() {
		return result;
	}

	public void setResult(ActionResult result) {
		this.result = result;
	}
	
	public void addError(String field, String message) {
		this.result.addError(new ActionError(field, message));
	}

}
