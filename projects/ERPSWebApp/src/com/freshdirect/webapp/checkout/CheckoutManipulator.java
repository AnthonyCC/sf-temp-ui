package com.freshdirect.webapp.checkout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.HttpContextAware;
import com.freshdirect.webapp.action.ResultAware;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public abstract class CheckoutManipulator {
	HttpServletRequest	request;
	HttpSession 		session;
	HttpServletResponse	response;

	ActionResult		result;
	
	String 				actionName;
	
	
	public CheckoutManipulator(PageContext context, ActionResult result, String actionName) {
		this.request = (HttpServletRequest) context.getRequest();
		this.session = context.getSession();
		this.response = (HttpServletResponse) context.getResponse();

		this.result = result != null ? result : new ActionResult();
		this.actionName = actionName;
	}



	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	
	
	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public ActionResult getResult() {
		return result;
	}

	public void setResult(ActionResult result) {
		this.result = result;
	}
	
	
	
	


	public FDUserI getUser() {
		return (FDUserI) session.getAttribute( SessionName.USER );	
	}

	public FDIdentity getIdentity() {
		return this.getUser().getIdentity();
	}


	protected void setCart( FDCartModel cart ) {
		FDUserI user = this.getUser();
		if ( this.getActionName().indexOf( "gc_" ) != -1 ) {
			user.setGiftCart( cart );
		} else if ( this.getActionName().indexOf( "rh_" ) != -1 ) {
			user.setDonationCart( cart );
		} else {
			user.setShoppingCart( cart );
		}
		session.setAttribute( SessionName.USER, user );
	}

	protected FDCartModel getCart() {
		if ( this.getActionName().indexOf( "gc_" ) != -1 ) {
			return this.getUser().getGiftCart();
		} else if ( this.getActionName().indexOf( "rh_" ) != -1 ) {
			return this.getUser().getDonationCart();
		}
		return this.getUser().getShoppingCart();
	}



	public void configureAction( Action action, ActionResult result ) {
		if ( action instanceof HttpContextAware ) {
			HttpContext ctx = new HttpContext( session, request, response );

			( (HttpContextAware)action ).setHttpContext( ctx );
		}

		if ( action instanceof ResultAware ) {
			( (ResultAware)action ).setResult( result );
		}
	}
}
