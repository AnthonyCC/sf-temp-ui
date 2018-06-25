package com.freshdirect.webapp.checkout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.HttpContextAware;
import com.freshdirect.webapp.action.ResultAware;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public abstract class CheckoutManipulator {
	HttpServletRequest	request;
	HttpSession 		session;
	HttpServletResponse	response;
	ActionResult		result;
	String 				actionName;
//	boolean dlvPassCart;
	
    public CheckoutManipulator(HttpServletRequest request, HttpServletResponse response, ActionResult result, String actionName) {
        this.request = request;
        this.session = request.getSession();
        this.response = response;
		this.result = result != null ? result : new ActionResult();
		this.actionName = actionName;
//		this.dlvPassCart = null !=request.getParameter("dlvPassCart") && "true".equalsIgnoreCase(request.getParameter("dlvPassCart")) ? true: false;
	}

	/*public boolean isDlvPassCart() {
		return dlvPassCart;
	}*/

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
		setCart(cart, getUser(), getActionName(), session, false);
	}
	
	protected static void setCart(FDCartModel cart, FDUserI user, String actionName, HttpSession session, boolean dlvPassCart) {
		if ( actionName.indexOf( "gc_" ) != -1 ) {
			user.setGiftCart( cart );
		} else if ( actionName.indexOf( "rh_" ) != -1 ) {
			user.setDonationCart( cart );
		} else if(StandingOrderHelper.isSO3StandingOrder(user)){
				user.setSoTemplateCart(cart);
		} else if (dlvPassCart){
			user.setDlvPassCart(cart);
		}else {
			user.setShoppingCart( cart );
		}
		session.setAttribute( SessionName.USER, user );
	}

	protected static FDCartModel getCart(FDUserI user, String actionName, boolean dlvPassCart) {
		return UserUtil.getCart(user, actionName, dlvPassCart);
		/*if (actionName.indexOf("gc_") != -1) {
			return user.getGiftCart();
		} else if (actionName.indexOf("rh_") != -1) {
			return user.getDonationCart();
		}else if(StandingOrderHelper.isSO3StandingOrder(user)){
			try {
				if(null!=user.getCurrentStandingOrder().getAddressId()){
				user.getSoTemplateCart().setDeliveryAddress(user.getCurrentStandingOrder().getDeliveryAddress());
				}
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
			return user.getSoTemplateCart();
		} else if(dlvPassCart){
			return user.getDlvPassCart();
		}
		return user.getShoppingCart();*/
	}
	
	protected FDCartModel getCart() {
		return getCart(getUser(), getActionName(), false);
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
