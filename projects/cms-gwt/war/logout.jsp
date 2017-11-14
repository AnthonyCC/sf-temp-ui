<%@page import="com.freshdirect.cms.ui.editor.permission.service.PersonaService"%>
<%@page import="com.freshdirect.cms.ui.serviceimpl.EditorServiceLocator"%><%
    if (request.getSession(false)!=null) {
        if (request.getUserPrincipal() != null) {
            EditorServiceLocator.personaService().invalidatePersona(request.getUserPrincipal().getName());
            request.getSession(false).invalidate();
        }
    }
    response.sendRedirect("index.jsp");
%>