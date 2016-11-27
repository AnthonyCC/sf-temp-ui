<%@page import="java.util.List"%>
<%@page import="com.freshdirect.cmsadmin.domain.Draft"%>
<%@page import="com.freshdirect.http.HttpService"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@page import="com.freshdirect.cms.application.draft.service.DraftService"%>
<%@page import="com.freshdirect.cms.application.permission.service.PersonaService"%>
<%@page import="com.freshdirect.cms.application.permission.domain.PersonaWrapper"%>
<%@page import="com.freshdirect.cms.application.permission.domain.CmsUserBuilder"%>
<%@page import="java.util.Random"%>
<%@page import="com.freshdirect.cms.application.CmsUser" %>
<%
    boolean redirectToLogin = false;
    boolean redirectToDraftSelector = false;
    boolean redirectToCmsGwtPage = false;
    boolean redirectToCmsGwtPageWithSelectMain = false;
    if (request.getUserPrincipal() != null) {
        String userName = request.getUserPrincipal().getName();
        CmsUserBuilder builder = new CmsUserBuilder(userName);
        PersonaWrapper personaWrapper = PersonaService.defaultService().loadPermissionsForUser(userName);
        builder.setPersona(personaWrapper.getPersona());
        CmsUser user = builder.build();
        if (request.getSession().getAttribute(DraftService.CMS_DRAFT_CONTEXT_SESSION_NAME) != null) {
            // draft already selected
            redirectToCmsGwtPage = true;
        } else {
            if (user.isHasAccessToDraftBranches()) {
                List<Draft> drafts = DraftService.defaultService().getDrafts();
                if (drafts.isEmpty()) {
                    // not selected draft, has draft editor permission but no available draft
                    redirectToCmsGwtPageWithSelectMain = true;
                } else {
                    // not selected draft, has draft editor permission and drafts available
                    redirectToDraftSelector = true;
                }
            } else {
                // not selected draft, has no draft editor permission
                redirectToCmsGwtPageWithSelectMain = true;
            }
        }
    } else {
        // no user
        redirectToLogin = true;
    }

    if (redirectToLogin) {
        response.sendRedirect("Login.html");
    } else if (redirectToDraftSelector) {
        DraftService.defaultService().setupDraftContext(DraftService.CMS_DRAFT_DEFAULT_ID, request);
        response.sendRedirect("draft_selector.html");
    } else if (redirectToCmsGwtPage) {
        response.sendRedirect("cmsgwt.html?rnd=" + new Random().nextInt());
    } else if (redirectToCmsGwtPageWithSelectMain) {
        DraftService.defaultService().setupDraftContext(DraftService.CMS_DRAFT_DEFAULT_ID, request);
        response.sendRedirect("cmsgwt.html?rnd=" + new Random().nextInt());
    }
%>
