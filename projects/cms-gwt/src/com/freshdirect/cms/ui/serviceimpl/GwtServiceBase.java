package com.freshdirect.cms.ui.serviceimpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.controller.DraftPopulatorServlet;
import com.freshdirect.cms.application.permission.domain.CmsUserBuilder;
import com.freshdirect.cms.application.permission.domain.Persona;
import com.freshdirect.cms.application.permission.domain.PersonaWrapper;
import com.freshdirect.cms.application.permission.service.PersonaService;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.service.PublishStatusService;
import com.freshdirect.cms.publish.service.impl.BasicPublishStatusService;
import com.freshdirect.cms.ui.service.ServerException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class GwtServiceBase extends RemoteServiceServlet {

    private static final long serialVersionUID = -4312434773038708335L;

    protected static final String CMS_USER_SESSION_NAME = "CMS_USER";

    protected ContentServiceI contentService;

    public GwtServiceBase() {
        this.contentService = CmsManager.getInstance();
    }

    public CmsUser getCmsUser() {
        HttpServletRequest request = getThreadLocalRequest();
        return getCmsUserFromRequest(request);
    }

    /**
     * Obtain CMS User from request
     * 
     * @param request
     * @return
     */
    public static CmsUser getCmsUserFromRequest(HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        return populateCmsUser(request, userName);
    }

    /**
     * If there is a publish in progress then nobody should be able to save changes on the MAIN draft.
     * 
     * @param draftContext
     *            the draftContext on which the save request come
     * @return true if node modification is okay, false otherwise
     */
    public static boolean isNodeModificationEnabled(DraftContext draftContext) {
        boolean canSave = true;
        try {
            EnumPublishStatus lastPublishStatus = EnumPublishStatus.getEnum(getLastPublishStatusName());
            if (draftContext == DraftContext.MAIN && lastPublishStatus == EnumPublishStatus.PROGRESS) {
                canSave = false;
            }
        } catch (ServerException e) {
            canSave = false;
        }
        return canSave;
    }

    protected static PublishStatusService getPublishStatusService() {
        return BasicPublishStatusService.defaultService();
    }

    protected static String getLastPublishStatusName() throws ServerException {
        return getPublishStatusService().getLastPublish().getStatus().getName();
    }

    private static CmsUser populateCmsUser(HttpServletRequest request, String userName) {
        PersonaWrapper personaWrapper = PersonaService.defaultService().loadPermissionsForUser(userName);
        CmsUser cmsUser = (CmsUser) request.getSession().getAttribute(CMS_USER_SESSION_NAME);
        if (personaWrapper.isUpdateUser() || cmsUser == null) {
            cmsUser = buildCmsUser(request.getSession(), userName, personaWrapper.getPersona());
        }
        storeDraftContext(request, cmsUser);
        return cmsUser;
    }

    /**
     * Obtain draft context available for the session
     * 
     * @return
     */
    public DraftContext getDraftContext() {
        DraftContext draftContext = getCmsUser().getDraftContext();
        return draftContext != null ? draftContext : DraftContext.MAIN;
    }

    private static void storeDraftContext(HttpServletRequest request, CmsUser cmsUser) {
        final DraftContext draftContext = (DraftContext) request.getSession().getAttribute(DraftPopulatorServlet.CMS_DRAFT_CONTEXT_SESSION_NAME);
        if (!DraftContext.MAIN.equals(draftContext)) {
            if (populateLoginErrorMessage(request, cmsUser) == null) {
                // update user with context when available and allowed
                if (draftContext == null) {
                    cmsUser.setDraftContext(DraftContext.MAIN);
                } else {
                    cmsUser.setDraftContext(draftContext);
                }
            }
        }
    }

    private static String populateLoginErrorMessage(HttpServletRequest request, CmsUser cmsUser) {
        String loginErrorMessage = (String) request.getSession().getAttribute(DraftPopulatorServlet.CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME);
        cmsUser.setLoginErrorMessage(loginErrorMessage);
        return loginErrorMessage;
    }

    private static CmsUser buildCmsUser(HttpSession session, String userName, Persona persona) {
        CmsUserBuilder permissionBuilder = new CmsUserBuilder(userName);
        permissionBuilder.setPersona(persona);
        CmsUser cmsUser = permissionBuilder.build();
        session.setAttribute(CMS_USER_SESSION_NAME, cmsUser);
        return cmsUser;
    }
}
