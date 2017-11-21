package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;

/**
 * This tag is used to intercept an add-to-cart form post on either product page
 * 
 * @author csaszi
 */
public class PopupHandlerTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -7823382251523879100L;
	private static final Logger LOGGER = LoggerFactory.getInstance(PopupHandlerTag.class); 

	private FDUserI user;
	
	private String event = "";
	private String elementId = "";
	private String instant = "";
	private String decorate = "";
	private String quantityCheck = "";
	private int tagCounter = 0;
	private String skuCode = "";
	private String noProduct = "false";
	private String multiForm = "false";
	private String action;
	private boolean ajax;
	private boolean rebindSubmit;
	private String inputTagPostFix = "";
	private Boolean hasPendingOrder;

	public int doStartTag() throws JspException {
		boolean usq = false, atc = false;
		
		if(hasPendingOrder==null){
			hasPendingOrder=hasPendingModifiableOrder();
		}

		if (FDStoreProperties.isUSQLegalWarningSwitchedOn() && hasPendingOrder) {
			
			atc = true;
			if (isAlcoholic(true)) {
				usq = true;
			}
			
		} else if (!FDStoreProperties.isUSQLegalWarningSwitchedOn() && hasPendingOrder) {

			atc = true;
			
		} else if (FDStoreProperties.isUSQLegalWarningSwitchedOn() && !hasPendingOrder) {
			
			if (!isAlcoholic(true)) {
				return SKIP_BODY;
			}
			usq = true;
			
		} else if (!FDStoreProperties.isUSQLegalWarningSwitchedOn() && !hasPendingOrder) {
			FDSessionUser yser = (FDSessionUser)request.getSession().getAttribute(SessionName.USER);
			if("true".equalsIgnoreCase(noProduct) && yser != null
					&& !yser.isHealthWarningAcknowledged()) {
				return proceedWithPopup(false, false);
			} else {
				return SKIP_BODY;
			}
		}
		
		return proceedWithPopup(usq, atc);
		
	}

	private int proceedWithPopup(boolean usq, boolean atc) throws JspException {
		StringBuffer buf = new StringBuffer();
		if (!isCallcenterApplication(pageContext.getSession())) {
			
			beginJavaScriptFrame(buf, ajax);
			handlePending(buf, ajax, rebindSubmit, usq, atc);
			endJavaScriptFrame(buf, ajax);
		}

		if (buf.length() != 0) {
			JspWriter out = pageContext.getOut();
			try {
				out.append(buf);
			} catch (IOException e) {
				throw new JspException();
			}
		}
//		if ("true".equals(forceOnClick) && usq == false) {
//			return SKIP_BODY;
//		}
		return EVAL_BODY_INCLUDE;
		
	}
	
	private boolean isCallcenterApplication(HttpSession session) {
		return "CALLCENTER".equalsIgnoreCase((String) session.getAttribute(SessionName.APPLICATION));
	}

	private void handlePending(StringBuffer buf, boolean ajax, boolean rebindSubmit,boolean usq, boolean atc) {
		buf.append("    if (typeof window.parent.FreshDirect != \"undefined\" && typeof window.parent.FreshDirect.PopupDispatcher != \"undefined\") {\r\n");
		buf.append("    	window.parent.FreshDirect.PopupDispatcher.attachForm(product_form, location.href, '" + (event == "" ? "onsubmit" : event) + "'");
		buf.append(", document.getElementById('" + elementId + "')");
		buf.append(", '" + instant + "'");
		buf.append(", '" + quantityCheck + "'");
		buf.append(", " + tagCounter);
		buf.append(", '" + decorate + "'");
		if (ajax)
			buf.append(", 1");
		if (rebindSubmit) {
			if (!ajax)
				buf.append(", 0");
			buf.append(", 1");
		} else {
			if (!ajax)
				buf.append(", 0");
			buf.append(", 0");
		}
		buf.append(usq?", 'true'":", 'false'");
		buf.append(atc?", 'true'":", 'false'");
		buf.append(isAlcoholic(false)?", 'true'":", 'false'");
		buf.append("true".equals(multiForm)?", 'true'":", 'false'");
		buf.append(");\r\n");
		buf.append("    }\r\n");
	}

	private void beginJavaScriptFrame(StringBuffer buf, boolean ajax) {
		buf.append("<script type=\"text/javascript\">\r\n");
		if (!ajax)
			buf.append("YAHOO.util.Event.onDOMReady(function() {\r\n");
		if (getId() != null) {
			buf.append("    var product_form = document.getElementById('" + getId() + "');\r\n");
			buf.append("    if (product_form) { ");
		} else
			buf.append("    if (false) { // inproper use AddToCartPending\r\n");
	}

	private void endJavaScriptFrame(StringBuffer buf, boolean ajax) {
		buf.append("    }\r\n");
		if (!ajax)
			buf.append("});\r\n");
		buf.append("</script>\r\n");
		if (isAlcoholic(false)) {
			buf.append("<input type='hidden' class='wine_quantity' name='alcoholic_" + tagCounter + "' id='alcoholic_" + tagCounter + "' value='quantity_" + ("".equals(inputTagPostFix)?"":(inputTagPostFix + "_")) + tagCounter + "'/>");
		}

	}
	
	private boolean isAlcoholic(boolean withNoProduct) {

		ProductModel prodModel = null;
		CategoryModel categoryModel = null;
		
		try {
			if (null!=skuCode && !skuCode.equals("")) {
				prodModel = ContentFactory.getInstance().getProduct(skuCode);
				categoryModel = prodModel.getCategory();
			}
	
			Cookie[] cookies = request.getCookies();//TODO:null pointer check
			if (cookies != null) {
				for(Cookie cookie : cookies) {
					if (cookie.getName().equals("freshdirect.healthwarning")) {
						if (normalizeSessionId(cookie.getValue()).equals("1@" + normalizeSessionId(request.getSession().getId()))) {
							return false;
						}
					}
				}
			}
			
			if (withNoProduct /*&& categoryModel == null && prodModel == null*/ && "true".equals(noProduct)) {
				return true;
			}
			if ((categoryModel != null && categoryModel.isHavingBeer()) || (prodModel!= null && prodModel.getSku(skuCode).getProduct().isAlcohol())) {
				return true;
			}
		} catch (FDSkuNotFoundException e) {
			LOGGER.error("Not valid SKU code", e);
			return false;
		} catch (FDResourceException e) {
			LOGGER.error("Not valid product info", e);
			return false;
		}

		
		return false;
	}
	
	
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
	
	private String normalizeSessionId(String sessionId) {
		int pos = sessionId.indexOf("!");//Removing WebLogic specific JVM id(s) from session id
		if (pos > 0) sessionId = sessionId.substring(0, pos);
		return sessionId;
	}
	
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getNoProduct() {
		return noProduct;
	}

	public void setNoProduct(String noProduct) {
		this.noProduct = noProduct;
	}

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

	public String getInputTagPostFix() {
		return inputTagPostFix;
	}

	public void setInputTagPostFix(String inputTagPostFix) {
		this.inputTagPostFix = inputTagPostFix;
	}

	public String getMultiForm() {
		return multiForm;
	}

	public void setMultiForm(String multiForm) {
		this.multiForm = multiForm;
	}

	public void setHasPendingOrder(Boolean hasPendingOrder) {
		this.hasPendingOrder = hasPendingOrder;
	}
	
}