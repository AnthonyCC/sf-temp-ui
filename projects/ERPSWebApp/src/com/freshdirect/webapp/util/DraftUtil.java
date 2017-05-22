package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.multistore.MultiStoreContextUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.CheckDraftContextTag;

public class DraftUtil {

    private final static Category LOGGER = LoggerFactory.getInstance(CheckDraftContextTag.class);

    private static final String DRAFT_ID_PARAMETER = "draftId";
    private static final String DRAFT_NAME_PARAMETER = "draftName";
    private static final String ACTIVE_DRAFT_PARAMETER_NAME = "activeDraft";
    private static final String ACTIVE_DRAFT_ID_COOKIE_NAME = "activeDraftId";
    private static final String ACTIVE_DRAFT_NAME_COOKIE_NAME = "activeDraftName";

    public static void draft(HttpServletRequest request, HttpServletResponse response) {
        // Determine if storefront runs in CMS preview mode
        if (MultiStoreContextUtil.getContext(CmsManager.getInstance()).isPreviewNode()) {
            LOGGER.debug("CMS Draft is enabled");
            String draftId = RequestUtil.getInputValue(request, DRAFT_ID_PARAMETER, ACTIVE_DRAFT_ID_COOKIE_NAME);
            String draftName = RequestUtil.getInputValue(request, DRAFT_NAME_PARAMETER, ACTIVE_DRAFT_NAME_COOKIE_NAME);
            if (draftId != null && draftName != null) {
                DraftService.defaultService().updateDraftContext(draftId, draftName);
                int sessionTimeout = request.getSession().getMaxInactiveInterval();
                response.addCookie(RequestUtil.createCookie(ACTIVE_DRAFT_ID_COOKIE_NAME, draftId, sessionTimeout));
                response.addCookie(RequestUtil.createCookie(ACTIVE_DRAFT_NAME_COOKIE_NAME, draftName, sessionTimeout));
                request.setAttribute(ACTIVE_DRAFT_PARAMETER_NAME, draftName);
            } else {
                DraftService.defaultService().updateDraftContext(DraftContext.MAIN_DRAFT_ID, DraftContext.MAIN_DRAFT_NAME);
            }
        } else {
            LOGGER.debug("CMS Draft is disabled");
        }
    }

}
