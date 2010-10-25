package com.freshdirect.webapp.taglib.content;

import javax.servlet.jsp.JspException;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class WineFilterClearCacheTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 6403305657110181230L;

	@Override
	public int doStartTag() throws JspException {
		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		PricingContext pricingContext = null;
		if (user != null)
			pricingContext = user.getPricingContext();
		if (pricingContext == null)
			pricingContext = PricingContext.DEFAULT;
		return SKIP_BODY;
	}
}
