package com.freshdirect.webapp.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.ads.AdQueryStringFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;


public class AdQueryStringTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(AdQueryStringTag.class);

    @Override
    public void doTag() throws JspException {
        final PageContext pageContext = (PageContext) getJspContext();

        final FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);

        final boolean isMobile = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(((HttpServletRequest) pageContext.getRequest()).getHeader("User-Agent"));

        try {
            final String queryString = AdQueryStringFactory.composeAdQueryString(user, (HttpServletRequest) pageContext.getRequest(), isMobile);

            pageContext.getOut().append(queryString);
        } catch (Exception exc) {
            LOGGER.error("Failed to get ad query string", exc);
            throw new JspException(exc);
        }
    }
}
