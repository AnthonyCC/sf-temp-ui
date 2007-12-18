/*
 * Created on Feb 9, 2005
 */
package com.freshdirect.cms.ui.tapestry.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.html.BasePage;

/**
 * @author vszathmary
 */
public abstract class BasePopupPage extends BasePage {

	public Map getScriptSymbols() {
		HttpServletRequest request = getRequestCycle().getRequestContext().getRequest();
		String context = request.getContextPath() + request.getServletPath();

		Map symbols = new HashMap();
		symbols.put("context", context);
		symbols.put("pagename", getOpenerPageName());

		return symbols;
	}

	public abstract String getOpenerPageName();

	public abstract void setOpenerPageName(String pageName);

	public abstract void setClosing(boolean closing);

}