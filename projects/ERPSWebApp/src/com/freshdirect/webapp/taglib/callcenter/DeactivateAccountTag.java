/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class DeactivateAccountTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private String action;
	private String notes;

	private String successPage;
	private String results;

	public void setSuccessPage(String s) {
		this.successPage = s;
	}

	public void setResults(String s) {
		this.results = s;
	}

    public int doStartTag() throws JspException {

		ActionResult result = new ActionResult();
		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {
			this.getFormData(request, result);
			if ( result.isSuccess() ) {
				try {
					if ( "deactivate".equalsIgnoreCase(this.action) ) {

						FDCustomerManager.setActive(AccountActivityUtil.getActionInfo(session, this.notes), false);

					} else if ( "activate".equalsIgnoreCase(this.action) ) {

						FDCustomerManager.setActive(AccountActivityUtil.getActionInfo(session, this.notes), true);

					}
					
					FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
					FDUser user = FDCustomerManager.recognize(currentUser.getIdentity());
					if(currentUser.getShoppingCart() != null){
						user.setShoppingCart(currentUser.getShoppingCart());
					}
					session.setAttribute(SessionName.USER, new FDSessionUser(user, session));
					
					if (this.successPage!=null) {
						HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
						try {
							response.sendRedirect(response.encodeRedirectURL(this.successPage));
							JspWriter writer = pageContext.getOut();
							writer.close();

							return SKIP_BODY;
						} catch (IOException ioe) {
							// if there was a problem redirecting, well... dagnabit... :)
							throw new JspException("Error redirecting "+ioe.getMessage());
						}
					}
					
				} catch (FDResourceException fe) {
					result.addError(new ActionError("technical_difficulty", "Could not deactivate customer due to a technical error."));
				} catch (FDAuthenticationException e) {
					result.addError(new ActionError("technical_difficulty", "Could not Refresh customer due to a technical error."));
				}
				
			}
		}

		pageContext.setAttribute(this.results, result);

		return EVAL_BODY_BUFFERED;

    }

    private void getFormData(HttpServletRequest request, ActionResult result) {
		this.action = request.getParameter("action");
		this.notes = request.getParameter("deactivate_notes");

		if ( notes == null || notes.equals("") ) {
			result.addError(new ActionError("empty_field", "A required field is empty or contains invalid data."));
		}
	}


}
