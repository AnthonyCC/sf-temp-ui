/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ComplaintReasonsTag extends BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ComplaintReasonsTag.class);

	private String department;
	private String reasons;

	public void setDepartment(String s) {
		this.department = s;
	}

	public void setReasons(String s) {
		this.reasons = s;
	}

	public int doStartTag() throws JspException {
		try {
			List reasonList = ComplaintUtil.getReasonsForDepartment(department);
			pageContext.setAttribute(reasons, reasonList);
			return EVAL_BODY_BUFFERED;
		} catch (FDResourceException ex) {
			LOGGER.warn("Error getting complaint reasons.", ex);
			throw new JspException(ex.getMessage());
		}
	}

}