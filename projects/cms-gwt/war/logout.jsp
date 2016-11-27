<%@page import="com.freshdirect.cms.application.permission.service.PersonaService"%>
<%@page import="com.freshdirect.fdstore.cache.EhCacheUtil"%>
<%@page import="java.util.Random"%><%
    if (request.getSession(false)!=null) {
        if (request.getUserPrincipal() != null) {
            PersonaService.defaultService().invalidatePersona(request.getUserPrincipal().getName());
            request.getSession(false).invalidate();
        }
    }
    response.sendRedirect("Login.html");
%>