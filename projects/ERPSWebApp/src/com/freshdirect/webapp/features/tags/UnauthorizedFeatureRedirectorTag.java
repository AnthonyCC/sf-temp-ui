package com.freshdirect.webapp.features.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class UnauthorizedFeatureRedirectorTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(UnauthorizedFeatureRedirectorTag.class);

    private boolean featureActive;
    private String featureName;

    public boolean isFeatureActive() {
        return featureActive;
    }

    public void setFeatureActive(boolean featureActive) {
        this.featureActive = featureActive;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (!featureActive) {
            LOGGER.error("Unauthorized access of feature: " + featureName);
            PageContext ctx = (PageContext) getJspContext();
            HttpServletResponse httpServletResponse = (HttpServletResponse) ctx.getResponse();
            httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
