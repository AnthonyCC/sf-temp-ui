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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.framework.core.PrimaryKey;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class PlaceAlertTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {
	
	private String customerAlertId;
	private String action;
	private String alertType;
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
					FDActionInfo actionInfo = AccountActivityUtil.getActionInfo(session, "Alert Type: " + alertType + ", Note: " + this.notes);
					ErpCustomerAlertModel customerAlert = new ErpCustomerAlertModel();
					customerAlert.setCustomerId(actionInfo.getIdentity().getErpCustomerPK());
					customerAlert.setAlertType(this.alertType);
					customerAlert.setCreateUserId(actionInfo.getInitiator());
					customerAlert.setCreateDate(new Date());
					customerAlert.setNote(notes);
					if ( "remove_alert".equalsIgnoreCase(this.action) ) {
						customerAlert.setPK(new PrimaryKey(customerAlertId));
						FDCustomerManager.setAlert(actionInfo, customerAlert, false);
					} else if ( "place_alert".equalsIgnoreCase(this.action) ) {
						FDCustomerManager.setAlert(actionInfo, customerAlert, true);
					}
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
					result.addError(new ActionError("technical_difficulty", "Could not place or remove an alert on customer due to a technical error."));
				}
			}
		}

		pageContext.setAttribute(this.results, result);

		return EVAL_BODY_BUFFERED;

    }

    private void getFormData(HttpServletRequest request, ActionResult result) {
		this.action = request.getParameter("action");
		this.customerAlertId = request.getParameter("customer_alert_id");
		this.alertType = request.getParameter("alert_type");
		this.notes = request.getParameter("alert_notes");

		if ("remove_alert".equalsIgnoreCase(action) && customerAlertId == null) {
			result.addError(new ActionError("alert_note", "Customer alert id is required."));			
		}
		if (alertType == null || "".equals(alertType)) {
			result.addError(new ActionError("alert_type", "Type is a required field."));
		}

		if (notes == null || "".equals(notes)) {
			result.addError(new ActionError("alert_notes", "Notes is a required field."));
		}
	}

}
