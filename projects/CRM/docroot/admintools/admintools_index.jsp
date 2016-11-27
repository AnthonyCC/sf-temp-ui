<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>
<%
String userRole = CrmSecurityManager.getUserRole(request);
%>
<% if(CrmSecurityManager.hasAccessToPage(userRole,"resubmit_orders.jsp") ) { %>
<jsp:forward page="/admintools/resubmit_orders.jsp" />
<% } else if(CrmSecurityManager.hasAccessToPage(userRole,"delivery_restrictions.jsp")) { %>
<jsp:forward page="/admintools/delivery_restrictions.jsp" />
<% } %>