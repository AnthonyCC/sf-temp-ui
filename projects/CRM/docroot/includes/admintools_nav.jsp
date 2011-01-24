<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%
String snav_pageURI = request.getRequestURI();
boolean resubmit_orders = snav_pageURI.indexOf("/admintools/index.jsp") > -1;
boolean resubmit_customers = snav_pageURI.indexOf("resubmit_customers") > -1;
boolean delete_reservations = snav_pageURI.indexOf("delete_reservations") > -1;
boolean mass_cancellation = snav_pageURI.indexOf("mass_cancellation") > -1;
boolean mass_returns = snav_pageURI.indexOf("mass_returns") > -1;
boolean delivery_restrictions = snav_pageURI.indexOf("delivery_restrictions") > -1;
boolean address_restrictions = snav_pageURI.indexOf("address_restrictions") > -1;
boolean settlement_batch = snav_pageURI.indexOf("settlement_batch") > -1;
boolean top_faqs = snav_pageURI.indexOf("top_faqs") > -1;
boolean click_to_call = snav_pageURI.indexOf("click_to_call") > -1;
boolean search_orders = snav_pageURI.indexOf("search_orders_by_sku") > -1;
boolean modify_orders = snav_pageURI.indexOf("modify_orders_by_sku") > -1;
String lAgentId = CrmSecurityManager.getUserName(request);
String lAgentRole = CrmSecurityManager.getUserRole(request);
%>

<div class="sup_nav_bg">
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"admintools_index.jsp")){ %>
	<a href="/admintools/admintools_index.jsp" class="<%=resubmit_orders?"sup_nav_on":"sup_nav"%>">Resub. Orders</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"resubmit_customers.jsp")){ %>
	<a href="/admintools/resubmit_customers.jsp" class="<%=resubmit_customers?"sup_nav_on":"sup_nav"%>">Resub. Customers</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"delete_reservations.jsp")){ %>
	<a href="/admintools/delete_reservations.jsp" class="<%=delete_reservations?"sup_nav_on":"sup_nav"%>">Del. Reservations</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"mass_cancellation.jsp")){ %>
	<a href="/admintools/mass_cancellation.jsp" class="<%=mass_cancellation?"sup_nav_on":"sup_nav"%>">Mass Cancellations</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"mass_returns.jsp")){ %>
	<a href="/admintools/mass_returns.jsp" class="<%=mass_returns?"sup_nav_on":"sup_nav"%>">Mass Returns</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"delivery_restrictions.jsp")){ %>
	<a href="/admintools/delivery_restrictions.jsp" class="<%=delivery_restrictions?"sup_nav_on":"sup_nav"%>">Delivery Restrictions</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"address_restrictions.jsp")){ %>
	<a href="/admintools/address_restrictions.jsp" class="<%=address_restrictions?"sup_nav_on":"sup_nav"%>">Address Restrictions</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"settlement_batch.jsp")){ %>
	<a href="/admintools/settlement_batch.jsp?method=GET" class="<%=settlement_batch?"sup_nav_on":"sup_nav"%>">settlement batch</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"top_faqs.jsp")){ %>
	<a href="/admintools/top_faqs.jsp" class="<%=top_faqs?"sup_nav_on":"sup_nav"%>">Top 5 FAQ</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"click_to_call.jsp")){ %>
	<a href="/admintools/click_to_call.jsp" class="<%=click_to_call?"sup_nav_on":"sup_nav"%>">Click to Call Campaign</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"search_orders_by_sku.jsp")){ %>
	<a href="/admintools/search_orders_by_sku.jsp" class="<%=search_orders?"sup_nav_on":"sup_nav"%>">Search Orders By Skus</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"modify_orders_by_sku.jsp")){ %>
	<a href="/admintools/modify_orders_by_sku.jsp?method=GET" class="<%=modify_orders?"sup_nav_on":"sup_nav"%>">Modify Orders By Skus</a>
<% } %>

</div>
