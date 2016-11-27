package com.freshdirect.framework.webapp.ui;

import javax.servlet.http.*;

import com.freshdirect.framework.webapp.ActionResult;

public abstract class PElement implements PPrintableI {

	private final String name;

	public PElement(String name) {
		this.name = name;	
	}

	public String getName() {
		return this.name;
	}
	
	public abstract String[] getValues();
	
	public abstract void initialize(HttpServletRequest request);
	
	public abstract void validate(ActionResult result);

}

