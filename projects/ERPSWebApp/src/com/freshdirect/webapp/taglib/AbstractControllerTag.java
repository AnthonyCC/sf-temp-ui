/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.freshdirect.framework.webapp.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import com.freshdirect.webapp.util.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class AbstractControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(AbstractControllerTag.class);

	private String actionName;
	private String successPage;
	private String result;

	public String getSuccessPage() {
		return successPage;
	}

	public void setSuccessPage(String sp) {
		if (sp != null && sp.indexOf("://") != -1) {
			throw new IllegalArgumentException("Invalid successPage specified");
		}
		this.successPage = sp;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setResult(String resultName) {
		this.result = resultName;
	}

	public int doStartTag() throws JspException {
		//
		// perform any actions requested by the user if the request was a POST
		//
		ActionResult actionResult = new ActionResult();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		if ("POST".equalsIgnoreCase(request.getMethod())) {

			boolean proceed = this.performAction(request, actionResult);
			if (!proceed) {
				return SKIP_BODY;
			}

			//
			// redirect to success page if an action was successfully performed
			// and a success page was defined
			//
			if (actionResult.isSuccess() && (successPage != null)) {
				LOGGER.debug("Success, redirecting to: " + successPage);
				this.redirectTo(successPage);
				return SKIP_BODY;
			}
		}else if ("GET".equalsIgnoreCase(request.getMethod())) {
			boolean proceed = this.performGetAction(request, actionResult);
			if (!proceed) {
				return SKIP_BODY;
			}
		}
		//
		// place the result as a scripting variabl1e in the page
		//
		pageContext.setAttribute(this.result, actionResult);
		return EVAL_BODY_BUFFERED;

	}

	protected void redirectTo(String destination) throws JspException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(destination));
			JspWriter writer = pageContext.getOut();
			writer.close();
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	protected void forward(String destination) throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			 RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
			 if (dispatcher != null)
		         dispatcher.forward(request, response);
			 
		} catch (ServletException se) {
			throw new JspException(se.getMessage());
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
	/**
	 * @return false to SKIP_BODY without redirect
	 */
	protected abstract boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException;
	
	/**
	 * template method to handle get request if need be
	 */
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		return true;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				 new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED)};
		}
	}

}
