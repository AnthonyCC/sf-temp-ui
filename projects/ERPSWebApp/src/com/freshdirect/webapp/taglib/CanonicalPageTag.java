package com.freshdirect.webapp.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * A simple tag to inject <link rel="canonical" href="https://www.freshdirect.com" /> tag
 * into homepage head
 *
 * ticket: APPDEV-6138
 */
public class CanonicalPageTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(CanonicalPageTag.class);

    @Override
    public void doTag() throws JspException, IOException {
        final HttpServletRequest request = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();

        if (isHomepage(request)) {
            getJspContext().getOut().append("<link rel=\"canonical\" href=\"https://www.freshdirect.com\" />");
        }
    }

    private boolean isHomepage(HttpServletRequest request) {
        final String serverName = request.getServerName();
        final String requestURI = request.getRequestURI();

        return "www.freshdirect.com".equalsIgnoreCase(serverName) && "/index.jsp".equalsIgnoreCase(requestURI);
    }
}
