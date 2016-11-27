package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDStoreProperties;

/**
 * Tag that renders its body only if the Seafood Sustainable property is true
 */
public class SeafoodSustainCheckTag extends com.freshdirect.framework.webapp.TagSupport {

	public int doStartTag() throws JspException {
		
		if (!FDStoreProperties.isSeafoodSustainEnabled()) {
			return SKIP_BODY;
		}

		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
