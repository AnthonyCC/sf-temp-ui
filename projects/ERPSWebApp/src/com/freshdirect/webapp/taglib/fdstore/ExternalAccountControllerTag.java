package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ExternalAccountControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

    private static final long serialVersionUID = -7466852553179595103L;
    private static Category LOGGER = LoggerFactory.getInstance(ExternalAccountControllerTag.class);
    private Object result;

    @Override
    public int doStartTag() throws JspException {

        HttpSession session = this.pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        String source = (request.getParameter("source") == null) ? "" : (String) request.getParameter("source");

        String redirectPage = AccountServiceFactory.getService(source).login(session, request, response);

        if (!FDStoreProperties.isLocalDeployment()) {
            if (null != redirectPage && !redirectPage.contains("https")) {
                redirectPage = redirectPage.replace("http", "https");
            }
        }

        if (session != null && checkSocialLogignSource(source)) {
            FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
            if (user.hasJustLoggedIn(false)) {
                session.setAttribute(SessionName.SOCIAL_LOGIN_SUCCESS, true);
            }
        }

        doRedirect(redirectPage);

        return SKIP_BODY;

    }

    private boolean checkSocialLogignSource(String source) {
        return StringUtils.isEmpty(source) || source.equals(EnumExternalLoginSource.SOCIAL.value());
    }

    private int doRedirect(String url) throws JspException {
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        try {
            response.sendRedirect(url);
            //JspWriter writer = pageContext.getOut();
            //writer.close();
            return SKIP_BODY;

        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }

    public void setResult(String result) {
        this.result = result;
    }

}
