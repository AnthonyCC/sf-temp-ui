package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

/**
 * This tag is used to decide whether the current product id defines an alcoholic product
 */
public class IsAlcoholicTag extends BodyTagSupportEx {

	private static final long serialVersionUID = -6698194047077787106L;
	private static final Logger LOGGER = LoggerFactory.getInstance(IsAlcoholicTag.class); 
	
	private String skuCode = "";
	private String noProduct = "false";

	public int doStartTag() throws JspException {
		
		ProductModel prodModel = null;
		CategoryModel categoryModel = null;

		try {
			if (FDStoreProperties.isUSQLegalWarningSwitchedOn()) {
				if (!skuCode.equals("")) {
					prodModel = ContentFactory.getInstance().getProduct(skuCode);
					categoryModel = prodModel.getCategory();
				}
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				Cookie sessionCookie = null;
				Cookie[] cookies = request.getCookies();
				for(Cookie cookie : cookies) {
					if (cookie.getName().equals("freshdirect.usq")) {
						cookie.setValue(request.getSession().getId());
						response.addCookie(cookie);
						sessionCookie = cookie;
					}
					if (cookie.getName().equals("freshdirect.healthwarning")) {
						if (normalizeSessionId(cookie.getValue()).equals("1@" + normalizeSessionId(request.getSession().getId()))) {
							return SKIP_BODY;
						}
					}
				}
				if (sessionCookie == null || !sessionCookie.getValue().equals(request.getSession().getId())) {
					sessionCookie = new Cookie("freshdirect.usq", request.getSession().getId());
					sessionCookie.setPath("/");
					response.addCookie(sessionCookie);
				}
				
				if ((categoryModel == null && prodModel == null && "false".equals(noProduct)) || (categoryModel != null && !categoryModel.isHavingBeer() && prodModel!= null && !prodModel.getSku(skuCode).getProduct().isAlcohol())) {
					return SKIP_BODY;
				}
			} else {
				FDSessionUser yser = (FDSessionUser)request.getSession().getAttribute(SessionName.USER);
				if("true".equalsIgnoreCase(noProduct) && yser != null 
						&& !yser.isHealthWarningAcknowledged()) {
					return EVAL_BODY_INCLUDE;
				} else {
					return SKIP_BODY;
				}
			}
			
		} catch (FDSkuNotFoundException e) {
			LOGGER.error("Not valid SKU code", e);
			return EVAL_BODY_INCLUDE;
		} catch (FDResourceException e) {
			LOGGER.error("Not valid product info", e);
			return EVAL_BODY_INCLUDE;
		}
		
		return EVAL_BODY_INCLUDE;
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
}