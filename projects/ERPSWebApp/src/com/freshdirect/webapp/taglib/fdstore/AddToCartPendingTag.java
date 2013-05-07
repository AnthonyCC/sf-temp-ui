package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

/**
 * This tag is used to intercept an add-to-cart form post on either product page
 * 
 * @author csongor
 */
public class AddToCartPendingTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -7823382251523879100L;

	private String action;
	
	private boolean ajax;
	
	private FDUserI user;
	
	private boolean rebindSubmit;

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public boolean isAjax() {
		return ajax;
	}

	public void setRebindSubmit(boolean rebindSubmit) {
		this.rebindSubmit = rebindSubmit;
	}
	
	public boolean isRebindSubmit() {
		return rebindSubmit;
	}

	public int doStartTag() throws JspException {
		StringBuffer buf = new StringBuffer();
		
		Boolean hasPendingOrder = (Boolean)pageContext.getAttribute("hasPendingOrder");
		if ( hasPendingOrder == null ) {
			hasPendingOrder = hasPendingModifiableOrder();
		}
		
		if (!isCallcenterApplication(pageContext.getSession()) && hasPendingOrder ) {
			String action = this.action != null ? this.action : FDShoppingCartControllerTag.peekAction(pageContext);
			if (action != null) {
				buf.append("<input type=\"hidden\" name=\"atc_pending_action\" value=\"" + action + "\" />\r\n");
			}
/*			beginJavaScriptFrame(buf, ajax);
			handlePending(buf, ajax, rebindSubmit);
			endJavaScriptFrame(buf, ajax);*/
		}

		if (buf.length() != 0) {
			JspWriter out = pageContext.getOut();
			try {
				out.append(buf);
			} catch (IOException e) {
				throw new JspException();
			}
		}
		return SKIP_BODY;
	}

	private boolean isCallcenterApplication(HttpSession session) {
		return "CALLCENTER".equalsIgnoreCase((String) session.getAttribute(SessionName.APPLICATION));
	}

	private void handlePending(StringBuffer buf, boolean ajax, boolean rebindSubmit) {
		buf.append("    if (typeof FreshDirect != \"undefined\" && typeof FreshDirect.ATC_Pending != \"undefined\") {\r\n");
		buf.append("    	FreshDirect.ATC_Pending.attachForm(product_form, location.href");
		if (ajax)
			buf.append(", 1");
		if (rebindSubmit) {
			if (!ajax)
				buf.append(", 0");
			buf.append(", 1");
		}
		buf.append(");\r\n");
		buf.append("    }\r\n");
	}

	private void beginJavaScriptFrame(StringBuffer buf, boolean ajax) {
		buf.append("<script type=\"text/javascript\">\r\n");
		if (!ajax)
			buf.append("YAHOO.util.Event.onDOMReady(function() {\r\n");
		if (getId() != null) {
			buf.append("    var product_form = document.getElementById('" + getId() + "');\r\n");
			buf.append("    if (product_form) { // AddToCartPending\r\n");
		} else
			buf.append("    if (false) { // inproper use AddToCartPending\r\n");
	}

	private void endJavaScriptFrame(StringBuffer buf, boolean ajax) {
		buf.append("    }\r\n");
		if (!ajax)
			buf.append("});\r\n");
		buf.append("</script>\r\n");
	}

	/**
	 * TODO Think about refactoring this into a Strategy pattern so that we can
	 * inject multiple interceptors
	 * 
	 * @return
	 */
	private boolean hasPendingModifiableOrder() {
		FDUserI user = getFDUser();
		if (user == null)
			return false;

		return user.isPopUpPendingOrderOverlay();
	}

	public FDUserI getFDUser() {
		if (user == null) {
			user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		}
		return user;
	}
}
