package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Category;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.multistore.MultiStoreContextUtil;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CheckDraftContextTag extends SimpleTagSupport {

    private final static Category LOGGER = LoggerFactory.getInstance(CheckDraftContextTag.class);

    private static final String DRAFT_ID_PARAMETER = "draftId";
    private static final String DRAFT_NAME_PARAMETER = "draftName";
    private static final String ACTIVE_DRAFT_PARAMETER_NAME = "activeDraft";
    private static final String ACTIVE_DRAFT_DIRECT_LINK = "activeDraftDirectLink";
    private static final String ACTIVE_DRAFT_ID_COOKIE_NAME = "activeDraftId";
    private static final String ACTIVE_DRAFT_NAME_COOKIE_NAME = "activeDraftName";

    @Override
    public void doTag() throws JspException {

        // Determine if storefront runs in CMS preview mode
        final boolean isPreviewNode = MultiStoreContextUtil
                .getContext( CmsManager.getInstance() )
                .isPreviewNode();

        if (isPreviewNode) {
            LOGGER.debug("CMS Draft tag is enabled");
            
            PageContext ctx = (PageContext) getJspContext();
            HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
            HttpServletResponse response = (HttpServletResponse) ctx.getResponse();

            String draftId = request.getParameter(DRAFT_ID_PARAMETER);
            if (draftId == null) {
                draftId = getValueFromCookie(request, ACTIVE_DRAFT_ID_COOKIE_NAME);
            }

            String draftName = request.getParameter(DRAFT_NAME_PARAMETER);
            if (draftName == null) {
                draftName = getValueFromCookie(request, ACTIVE_DRAFT_NAME_COOKIE_NAME);
            }

            DraftContext receivedDraftContext = DraftService.defaultService().getDraftContext(draftId, draftName);
            DraftContext currentDraftContext = ContentFactory.getInstance().getCurrentDraftContext();
            if (receivedDraftContext.getDraftId() != currentDraftContext.getDraftId()) {
                int sessionTimeout = request.getSession().getMaxInactiveInterval();
                response.addCookie(createCookie(ACTIVE_DRAFT_ID_COOKIE_NAME, String.valueOf(receivedDraftContext.getDraftId()), sessionTimeout));
                response.addCookie(createCookie(ACTIVE_DRAFT_NAME_COOKIE_NAME, receivedDraftContext.getDraftName(), sessionTimeout));
                ContentFactory.getInstance().setCurrentDraftContext(receivedDraftContext);
            }
            ctx.setAttribute(ACTIVE_DRAFT_PARAMETER_NAME, receivedDraftContext.getDraftName());
            ctx.setAttribute(ACTIVE_DRAFT_DIRECT_LINK, getDraftDirectLink(request, receivedDraftContext.getDraftId(), receivedDraftContext.getDraftName()));
        } else {
            LOGGER.debug("CMS Draft tag is disabled");
        }
    }

    private String getDraftDirectLink(HttpServletRequest request, long draftId, String draftName) {
        String url = null;
        if (draftId != DraftContext.MAIN_DRAFT_ID){
            url = getFullURL(request);
            if (!url.contains(DRAFT_ID_PARAMETER)){
                url = DraftService.defaultService().decorateUrlWithDraft(url, draftId, draftName);
            }
        }
        return url;
    }

    private String getValueFromCookie(HttpServletRequest request, String cookieKey) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieKey.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }

    private Cookie createCookie(String cookieKey, String cookieValue, int maxAge) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        LOGGER.debug("Set cookie " + cookieKey + " = " + cookieValue);
        return cookie;
    }

    private String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
