package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;

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
		FDSessionUser yser = (FDSessionUser)request.getSession().getAttribute(SessionName.USER);

		if (yser!=null && yser.isRobot()){
			return SKIP_BODY; //robot user should not be stopped by alcoholic warning
		}
		
		try {
			if (FDStoreProperties.isUSQLegalWarningSwitchedOn()) {
				if (!skuCode.equals("")) {
					prodModel = ContentFactory.getInstance().getProduct(skuCode);
					categoryModel = prodModel.getCategory();
				}

				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for(Cookie cookie : cookies) {
						if (cookie.getName().equals("freshdirect.healthwarning")) {
							if (normalizeSessionId(cookie.getValue()).equals("1@" + normalizeSessionId(request.getSession().getId()))) {
								return SKIP_BODY;
							}
						}
					}
				}
				
				if ((categoryModel == null && prodModel == null && "false".equals(noProduct)) || (categoryModel != null && !categoryModel.isHavingBeer() && prodModel!= null && !prodModel.getSku(skuCode).getProduct().isAlcohol())) {
					return SKIP_BODY;
				}
			} else {
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