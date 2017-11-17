<%@page import="com.freshdirect.cms.ui.serviceimpl.EditorServiceLocator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Random"%>
<%@page import="com.freshdirect.cms.draft.domain.Draft"%>
<%@page import="com.freshdirect.cms.draft.service.DraftService"%>
<%@page import="com.freshdirect.cms.CmsServiceLocator"%>
<%@page
	import="com.freshdirect.cms.ui.editor.permission.service.PersonaService"%>
<%@page
	import="com.freshdirect.cms.ui.editor.permission.domain.PersonaWrapper"%>
<%@page
	import="com.freshdirect.cms.ui.editor.permission.domain.GwtUserBuilder"%>
<%@page import="com.freshdirect.cms.ui.model.GwtUser"%>
<%
    boolean redirectToDraftSelector = false;
    boolean redirectToCmsGwtPage = false;
    boolean redirectToCmsGwtPageWithSelectMain = false;
    final DraftService draftService = CmsServiceLocator.draftService();

    String userName = request.getUserPrincipal().getName();
    GwtUserBuilder builder = new GwtUserBuilder(userName);
    PersonaWrapper personaWrapper = EditorServiceLocator.personaService().loadPermissionsForUser(userName);
    builder.setPersona(personaWrapper.getPersona());
    GwtUser user = builder.build();
    if (request.getSession().getAttribute(DraftService.CMS_DRAFT_CONTEXT_SESSION_NAME) != null) {
        // draft already selected
        redirectToCmsGwtPage = true;
    } else {
        if (user.isHasAccessToDraftBranches()) {
            List<Draft> drafts = draftService.getDrafts();
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

    if (redirectToDraftSelector) {
        draftService.setupDraftContext(DraftService.CMS_DRAFT_DEFAULT_ID, request);
        response.sendRedirect("draft_selector.html");
    } else if (redirectToCmsGwtPage) {
        response.sendRedirect("cmsgwt.html?rnd=" + new Random().nextInt());
    } else if (redirectToCmsGwtPageWithSelectMain) {
        draftService.setupDraftContext(DraftService.CMS_DRAFT_DEFAULT_ID, request);
        response.sendRedirect("cmsgwt.html?rnd=" + new Random().nextInt());
    }
%>
