package com.freshdirect.webapp.features.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.features.service.FeaturesRedirectorService;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.FDURLUtil;

public class FeatureRedirectorTag extends BodyTagSupport {

	private static final long serialVersionUID = -518038229201771022L;

	private static final Logger LOGGER = LoggerFactory.getInstance(FeatureRedirectorTag.class);
	private String featureName;
    private String checkout;
    
	@Override
	public int doStartTag() throws JspException {
		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		EnumRolloutFeature feature = FeaturesService.defaultService().parseFeatureName(featureName);
		String originalUrl = request.getServletPath();
		String redirectUrl = FeaturesRedirectorService.defaultService().getRedirectUrl(feature, originalUrl, user, request.getCookies(),checkout);
		int result = EVAL_BODY_BUFFERED;
		if (redirectUrl != null) {
			redirectUrl = FDURLUtil.decorateRedirectUrl(redirectUrl, request);
			LOGGER.debug("Redirecting from " + originalUrl + " to " + redirectUrl);
			redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + redirectUrl;
			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", redirectUrl);
			result = SKIP_PAGE;
		}
		return result;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	/**
	 * @return the checkout
	 */
	public String getCheckout() {
		return checkout;
	}

	/**
	 * @param checkout the checkout to set
	 */
	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}
	
	

}
