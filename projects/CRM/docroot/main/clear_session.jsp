<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>

<%
    CrmSession.getSessionStatus(session).clear(false);
    response.sendRedirect(response.encodeRedirectURL("/main/main_index.jsp"));
%>