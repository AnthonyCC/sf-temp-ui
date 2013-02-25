package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

/**
 * This tag is used to intercept an add-to-cart form post on either product page
 * 
 * @author csongor
 */
public class USQLegalWarningTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -7823382251523879100L;

	private FDUserI user;
	
	private String event = "";
	private String elementId = "";
	private String instant = "";
	private String decorate = "";
	private String quantityCheck = "";
	private int tagCounter = 100;
	private String skuCode = "";
	private String inputTagPostFix = "";
	boolean isAlcoholic = true;

	public int doStartTag() throws JspException {
		isAlcoholic = isAlcoholic();
		StringBuffer buf = new StringBuffer();
		if (!isCallcenterApplication(pageContext.getSession())) {
			beginJavaScriptFrame(buf);
			handlePending(buf);
			endJavaScriptFrame(buf);
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

	private void handlePending(StringBuffer buf) {
		buf.append("    if (typeof window.parent.FreshDirect != \"undefined\" && typeof window.parent.FreshDirect.USQLegalWarning != \"undefined\") {\r\n");
		buf.append("    	window.parent.FreshDirect.USQLegalWarning.attachForm(product_form, location.href, '" + (event == "" ? "onsubmit" : event) + "'");
		buf.append(", document.getElementById('" + elementId + "')");
		buf.append(", '" + instant + "'");
		buf.append(", '" + quantityCheck + "'");
		buf.append(", " + tagCounter);
		buf.append(", '" + decorate + "'");
		buf.append(isAlcoholic?", 'true'":", 'false'");
		buf.append(");\r\n");
		buf.append("    }\r\n");
	}

	private void beginJavaScriptFrame(StringBuffer buf) {
		buf.append("<script type=\"text/javascript\">\r\n");
		buf.append("YAHOO.util.Event.onDOMReady(function() {\r\n");
		if (getId() != null) {
			buf.append("    var product_form = document.getElementById('" + getId() + "');\r\n");
			buf.append("    if (product_form) { ");
		} else
			buf.append("    if (false) { // inproper use AddToCartPending\r\n");
	}

	private void endJavaScriptFrame(StringBuffer buf) {
		buf.append("    }\r\n");
		buf.append("});\r\n");
		buf.append("</script>\r\n");
		if (isAlcoholic) {
			buf.append("<input type='hidden' class='usq_quantity' name='alcoholic_" + tagCounter + "' id='alcoholic_" + tagCounter + "' value='quantity_" + ("".equals(inputTagPostFix)?"":(inputTagPostFix + "_")) + tagCounter + "'/>");
		}
	}
	//temporary solution, soon to be merged ModifyBrd will refactor this class
	private boolean isAlcoholic() {
		
		ProductModel prodModel = null;
		CategoryModel categoryModel = null;

		try {
			if (!skuCode.equals("")) {
				prodModel = ContentFactory.getInstance().getProduct(skuCode);
				categoryModel = prodModel.getCategory();
			}

			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for(Cookie cookie : cookies) {
					if (cookie.getName().equals("freshdirect.healthwarning")) {
						if (normalizeSessionId(cookie.getValue()).equals("1@" + normalizeSessionId(request.getSession().getId()))) {
							return false;
						}
					}
				}
			}
			
			if (categoryModel != null && !categoryModel.isHavingBeer() && prodModel!= null && !prodModel.getSku(skuCode).getProduct().isAlcohol()) {
				return false;
			}
			
		} catch (FDSkuNotFoundException e) {
			return false;
		} catch (FDResourceException e) {
			return false;
		}
		
		return true;
	}

	public FDUserI getFDUser() {
		if (user == null) {
			user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		}
		return user;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getInstant() {
		return instant;
	}

	public void setInstant(String instant) {
		this.instant = instant;
	}

	public String getQuantityCheck() {
		return quantityCheck;
	}

	public void setQuantityCheck(String quantityCheck) {
		this.quantityCheck = quantityCheck;
	}

	public int getTagCounter() {
		return tagCounter;
	}

	public void setTagCounter(int tagCounter) {
		this.tagCounter = tagCounter;
	}

	public String getDecorate() {
		return decorate;
	}

	public void setDecorate(String decorate) {
		this.decorate = decorate;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	private String normalizeSessionId(String sessionId) {
		int pos = sessionId.indexOf("!");//Removing WebLogic specific JVM id(s) from session id
		if (pos > 0) sessionId = sessionId.substring(0, pos);
		return sessionId;
	}

	public String getInputTagPostFix() {
		return inputTagPostFix;
	}

	public void setInputTagPostFix(String inputTagPostFix) {
		this.inputTagPostFix = inputTagPostFix;
	}
	
}