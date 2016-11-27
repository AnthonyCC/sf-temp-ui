/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.customer.FDUserI;

/**
 * Tag that renders its body if CCL functionality should be displayed for this user.
 * 
 * @version $Revision$
 * @author $Author$
 */
public class ProduceRatingCheckTag extends com.freshdirect.framework.webapp.TagSupport {

	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session
				.getAttribute(SessionName.USER);
		
		if (user == null ||  !user.isProduceRatingEnabled()) {
			return SKIP_BODY;
		}

		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
