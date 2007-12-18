<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>

<%
CrmSession.getSessionStatus(session).clear(true);

session.invalidate();
response.sendRedirect("/index.jsp");
%>