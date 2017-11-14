package com.freshdirect.cms.ui.serviceimpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.properties.service.PropertyResolverService;
import com.freshdirect.cms.ui.editor.permission.domain.GwtUserBuilder;
import com.freshdirect.cms.ui.editor.permission.domain.Persona;
import com.freshdirect.cms.ui.editor.permission.domain.PersonaWrapper;
import com.freshdirect.cms.ui.model.GwtUser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class GwtServiceBase extends RemoteServiceServlet {

    private static final long serialVersionUID = -4312434773038708335L;

    protected static final String CMS_USER_SESSION_NAME = "CMS_USER";

    private static PropertyResolverService propertyResolverService = CmsServiceLocator.propertyResolverService();

    /**
     * Obtain CMS User from request
     *
     * @param request
     * @return
     */
    public static GwtUser getGwtUserFromRequest(HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        return populateCmsUser(request, userName);
    }

    private static GwtUser populateCmsUser(HttpServletRequest request, String userName) {
        PersonaWrapper personaWrapper = EditorServiceLocator.personaService().loadPermissionsForUser(userName);
        GwtUser cmsUser = (GwtUser) request.getSession().getAttribute(CMS_USER_SESSION_NAME);
        if (personaWrapper.isUpdateUser() || cmsUser == null) {
            cmsUser = buildUser(request.getSession(), userName, personaWrapper.getPersona());
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
        HttpServletRequest request = getThreadLocalRequest();
        DraftContext draftContext = (DraftContext) request.getSession().getAttribute(DraftService.CMS_DRAFT_CONTEXT_SESSION_NAME);
        if (draftContext == null) {
            draftContext = DraftContext.MAIN;
        }
        return draftContext;
    }

    public GwtUser getUser() {
        HttpServletRequest request = getThreadLocalRequest();
        GwtUser user = (GwtUser) request.getSession().getAttribute(CMS_USER_SESSION_NAME);
        return user;
    }

    private static GwtUser buildUser(HttpSession session, String userName, Persona persona) {
        GwtUserBuilder userBuilder = new GwtUserBuilder(userName);
        userBuilder.setPersona(persona);
        GwtUser gwtUser = userBuilder.build();

        if (gwtUser.isHasAccessToPermissionEditorApp()) {
            gwtUser.setCmsAdminURL(propertyResolverService.getCmsAdminUiUri());
        }

        session.setAttribute(CMS_USER_SESSION_NAME, gwtUser);
        return gwtUser;
    }

    private static String populateLoginErrorMessage(HttpServletRequest request, GwtUser cmsUser) {
        String loginErrorMessage = (String) request.getSession().getAttribute(DraftService.CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME);
        cmsUser.setLoginErrorMessage(loginErrorMessage);
        return loginErrorMessage;
    }

    private static void storeDraftContext(HttpServletRequest request, GwtUser cmsUser) {
        final DraftContext draftContext = (DraftContext) request.getSession().getAttribute(DraftService.CMS_DRAFT_CONTEXT_SESSION_NAME);
        if (!DraftContext.MAIN.equals(draftContext)) {
            if (populateLoginErrorMessage(request, cmsUser) == null) {
                // update user with context when available and allowed
                if (draftContext == null) {
                    cmsUser.setDraftContextName(DraftContext.MAIN.getDraftName());
                    cmsUser.setDraftActive(false);
                } else {
                    cmsUser.setDraftContextName(draftContext.getDraftName());
                    cmsUser.setDraftActive(true);
                }
            }
        }
    }

}
