package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.webapp.util.FDURLUtil;

public class ExternalAccountControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

    private static final long serialVersionUID = -7466852553179595103L;
    private String result;

    @Override
    public int doStartTag() throws JspException {

        HttpSession session = this.pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        String source = (request.getParameter("source") == null) ? "" : (String) request.getParameter("source");

        String redirectUrl = request.getParameter("successPage");
        String urlStyle = request.getParameter("urlStyle");

        if (redirectUrl != null) {
            if ("fragment".equals(urlStyle)) {
                redirectUrl = FDURLUtil.convertQueryToFragmentUrl(redirectUrl);
            }
            session.setAttribute(SessionName.PREV_SUCCESS_PAGE, redirectUrl);
        }

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
            return SKIP_BODY;
        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }

    public void setResult(String result) {
        this.result = result;
    }

}
