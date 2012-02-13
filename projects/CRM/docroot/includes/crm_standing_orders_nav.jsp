<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%
String rnav_pageURI = request.getRequestURI();
boolean crm_standing_orders = rnav_pageURI.indexOf("/main/crm_standing_orders.jsp") > -1;
boolean crm_standing_orders_alt_dates = rnav_pageURI.indexOf("/main/crm_standing_orders_alt_dates.jsp") > -1;
%>
<crm:GetCurrentAgent id='currentAgent'>
<div class="rep_nav_bg">
<%  String agentRole = currentAgent.getRole().getLdapRoleName(); 
	if(CrmSecurityManager.hasAccessToPage(agentRole,"crm_standing_orders.jsp")){ %>
<a href="/main/crm_standing_orders.jsp" class="<%=crm_standing_orders?"rep_nav_on":"rep_nav"%>">View Standing Orders</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(agentRole,"crm_standing_orders_alt_dates.jsp")){ %>
<a href="/main/crm_standing_orders_alt_dates.jsp" class="<%=crm_standing_orders_alt_dates?"rep_nav_on":"rep_nav"%>">Manage Alternative Delivery Dates</a>
<% } %>

</div>
</crm:GetCurrentAgent>