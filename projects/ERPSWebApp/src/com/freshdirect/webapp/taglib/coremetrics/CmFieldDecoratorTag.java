package com.freshdirect.webapp.taglib.coremetrics;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class CmFieldDecoratorTag extends AbstractCmTag {
	
	public static final String CM_PAGE_ID = "FreshDirect.Coremetrics.pageId";
	public static final String CM_PAGE_CONTENT_HIERARCHY = "FreshDirect.Coremetrics.pageContentHierarchy";
	public static final String CM_VIRTUAL_CATEGORY = "FreshDirect.Coremetrics.virtualCategory";
	
	@Override
	public void doCmTag(StringBuilder sb) throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.println("<input type='hidden' name='coremetricsPageId' value=''/>");
		out.println("<input type='hidden' name='coremetricsPageContentHierarchy' value=''/>");
		out.println("<input type='hidden' name='coremetricsVirtualCategory' value=''/>");
		out.println(
				wrapIntoScriptTag(	"window.coremetricsQueue = window.coremetricsQueue || [];window.coremetricsQueue.push(function(){" + 
									"FreshDirect.Coremetrics.setDecorationFieldValue('coremetricsPageId', " + CM_PAGE_ID + ");" +
									"FreshDirect.Coremetrics.setDecorationFieldValue('coremetricsPageContentHierarchy', " + CM_PAGE_CONTENT_HIERARCHY + ");" + 
									"FreshDirect.Coremetrics.setDecorationFieldValue('coremetricsVirtualCategory', " + CM_VIRTUAL_CATEGORY + ");" + "})"));
	}
	
	/**
	 * Class overrides doCmTag so getTagJs is not used
	 */
	@Override
	public String getTagJs() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getFunctionName() {
		return null;
	}
}
