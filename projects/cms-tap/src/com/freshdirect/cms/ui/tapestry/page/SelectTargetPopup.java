/*
 * Created on Mar 17, 2005
 */
package com.freshdirect.cms.ui.tapestry.page;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 * @author vszathmary
 */
public abstract class SelectTargetPopup extends BasePage implements IExternalPage {

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
		setTitle((String) parameters[0]);

		Map symbols = new HashMap();
		symbols.put("fieldId", parameters[1]);
		symbols.put("buttonId", parameters[2]);
		setScriptSymbols(symbols);
	}

	public abstract void setScriptSymbols(Map symbols);

	public abstract void setTitle(String title);

}