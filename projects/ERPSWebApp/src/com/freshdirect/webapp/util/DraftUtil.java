package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.multistore.MultiStoreContextUtil;
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
        if (MultiStoreContextUtil.getContext().isPreviewNode()) {
            LOGGER.debug("CMS Draft is enabled");
            String draftId = RequestUtil.getInputValue(request, DRAFT_ID_PARAMETER, ACTIVE_DRAFT_ID_COOKIE_NAME);
            String draftName = RequestUtil.getInputValue(request, DRAFT_NAME_PARAMETER, ACTIVE_DRAFT_NAME_COOKIE_NAME);
            if (draftId != null && draftName != null) {
                updateDraftContext(draftId, draftName);
                int sessionTimeout = request.getSession().getMaxInactiveInterval();
                response.addCookie(RequestUtil.createCookie(ACTIVE_DRAFT_ID_COOKIE_NAME, draftId, sessionTimeout));
                response.addCookie(RequestUtil.createCookie(ACTIVE_DRAFT_NAME_COOKIE_NAME, draftName, sessionTimeout));
                request.setAttribute(ACTIVE_DRAFT_PARAMETER_NAME, draftName);
            } else {
                updateDraftContext(DraftContext.MAIN_DRAFT_ID, DraftContext.MAIN_DRAFT_NAME);
            }
        } else {
            LOGGER.debug("CMS Draft is disabled");
        }
    }

    public static void updateDraftContext(String draftId, String draftName) {
        updateDraftContext(CmsServiceLocator.draftService().parseDraftId(draftId), draftName);
    }

    public static void updateDraftContext(long draftId, String draftName) {
        DraftContext currentDraftContext = CmsServiceLocator.draftContextHolder().getDraftContext();
        if (draftId != currentDraftContext.getDraftId()) {
            CmsServiceLocator.draftContextHolder().setDraftContext(CmsServiceLocator.draftService().getDraftContext(draftId, draftName));
        }
    }



}
