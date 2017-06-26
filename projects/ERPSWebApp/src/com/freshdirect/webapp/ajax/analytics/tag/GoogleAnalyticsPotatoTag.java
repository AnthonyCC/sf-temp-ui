package com.freshdirect.webapp.ajax.analytics.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.analytics.domain.GAPageTypeDistinguisher;
import com.freshdirect.webapp.ajax.analytics.service.GoogleAnalyticsDataService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GoogleAnalyticsPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(GoogleAnalyticsPotatoTag.class);

    private String name = "googleAnalyticsPotato";

    @Override
    public void doTag() throws JspException, IOException {
        
        PageContext context = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        HttpSession session = context.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
        String loginType = (String) session.getAttribute(SessionName.SOCIAL_LOGIN_PROVIDER);
        session.removeAttribute(SessionName.SOCIAL_LOGIN_PROVIDER);
        Boolean loginSuccess = (Boolean) session.getAttribute(SessionName.LOGIN_SUCCESS);
        if (loginSuccess != null && !loginSuccess) {
            session.removeAttribute(SessionName.LOGIN_SUCCESS);
        }

        try {
            GAPageTypeDistinguisher distinguisher = parseParameters(request);
            context.setAttribute(name,
                    SoyTemplateEngine.convertToMap(GoogleAnalyticsDataService.defaultService().populateBasicGAData(user, loginType, loginSuccess, distinguisher)));

        } catch (FDResourceException e) {
            LOGGER.error("this is the exception: ", e);
        }
    }

    private GAPageTypeDistinguisher parseParameters(HttpServletRequest request) {
        GAPageTypeDistinguisher distinguisher = new GAPageTypeDistinguisher();
        distinguisher.setPageType(request.getParameter("pageType"));
        distinguisher.setId(request.getParameter("id"));
        distinguisher.setRequestURI(request.getRequestURI());
        return distinguisher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
