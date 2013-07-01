package com.freshdirect.webapp.taglib.coremetrics;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class CmFieldDecoratorTag extends AbstractCmTag {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(CmFieldDecoratorTag.class);
	public static final String CM_PAGE_ID = "FreshDirect.Coremetrics.pageId";
	public static final String CM_PAGE_CONTENT_HIERARCHY = "FreshDirect.Coremetrics.pageContentHierarchy";
	
	public void doCmTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.println("<input type='hidden' name='coremetricsPageId' value=''/>");
		out.println("<input type='hidden' name='coremetricsPageContentHierarchy' value=''/>");
		out.println(
				wrapIntoScriptTag(	"FreshDirect.Coremetrics.setDecorationFieldValue('coremetricsPageId', " + CM_PAGE_ID + ");" +
									"FreshDirect.Coremetrics.setDecorationFieldValue('coremetricsPageContentHierarchy', " + CM_PAGE_CONTENT_HIERARCHY + ");"));
	}
	
	/**
	 * Class overrides doCmTag so getTagJs is not used
	 */
	@Override
	protected String getTagJs() {
		throw new UnsupportedOperationException();
	}
}
