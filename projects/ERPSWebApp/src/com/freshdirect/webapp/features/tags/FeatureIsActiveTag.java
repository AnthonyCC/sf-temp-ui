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

	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();
		Cookie[] cookies = ((HttpServletRequest) context.getRequest()).getCookies();
		FDUserI user = QuickShopHelper.getUserFromSession(context.getSession());
		EnumRolloutFeature feature = FeaturesService.defaultService().parseFeatureName(featureName);
		boolean result = FeaturesService.defaultService().isFeatureActive(feature, cookies, user);
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
