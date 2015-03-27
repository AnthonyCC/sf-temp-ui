package com.freshdirect.webapp.features.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.features.service.FeaturesService;

public class FeaturesPotatoTag extends SimpleTagSupport {
	
	private String name = "featuresPotato";
	
	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();
		Map<String, Object> features = new HashMap<String, Object>();
		Cookie[] cookies = ((HttpServletRequest) context.getRequest()).getCookies();
		FDUserI user = QuickShopHelper.getUserFromSession(context.getSession());
		features.put("active", FeaturesService.defaultService().getActiveFeaturesMapped(cookies, user));
		context.setAttribute(name, features);
	}

}
