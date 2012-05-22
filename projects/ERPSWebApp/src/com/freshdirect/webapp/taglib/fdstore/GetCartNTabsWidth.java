package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.framework.webapp.BodyTagSupport;

public class GetCartNTabsWidth extends BodyTagSupport {
	private static final long serialVersionUID = -316473622762249043L;

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		int totalWidth = 970;
		final String __uri = request.getRequestURI();
		if(__uri.indexOf("quickshop") > -1) {
			if (__uri.endsWith("index.jsp"))
				totalWidth = 740; // QS main page
			else
				totalWidth = 601; // QS inner pages
		}

		pageContext.setAttribute("tabsTotalWidth", totalWidth);
		
		return EVAL_BODY_INCLUDE;
	}
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
					new VariableInfo(
							"tabsTotalWidth",
							"java.lang.Integer",
							true, VariableInfo.AT_BEGIN)
			};
		}
	}
}
