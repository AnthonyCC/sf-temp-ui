package com.freshdirect.webapp.features.tags;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.features.service.FeaturesService;

public class FeatureIsActiveTag extends SimpleTagSupport {

	private String name;
	private String featureName;
	
	private static String ENABLEFEATURE_CODE = "999";

	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = ((HttpServletRequest) context.getRequest());
		Cookie[] cookies = request.getCookies();
		FDUserI user = QuickShopHelper.getUserFromSession(context.getSession());
		EnumRolloutFeature feature = FeaturesService.defaultService().parseFeatureName(featureName);
		boolean result = FeaturesService.defaultService().isFeatureActive(feature, cookies, user);
		
		//This is to allow performance monitoring and other forced feature testing (Added for Keynote HomePageRedesign monitoring but can be used for any)
		if(request.getParameter(featureName) != null && request.getParameter(featureName).equalsIgnoreCase(ENABLEFEATURE_CODE)) {
			result = true;
		}
		context.setAttribute(name, result);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the featureName
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * @param featureName the featureName to set
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

}
