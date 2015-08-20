<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%
String snav_pageURI = request.getRequestURI();
boolean resubmit_orders = snav_pageURI.indexOf("/admintools/resubmit_orders.jsp") > -1;
boolean resubmit_customers = snav_pageURI.indexOf("resubmit_customers") > -1;
boolean delete_reservations = snav_pageURI.indexOf("delete_reservations") > -1;
boolean mass_cancellation = snav_pageURI.indexOf("mass_cancellation") > -1;
boolean mass_returns = snav_pageURI.indexOf("mass_returns") > -1;
boolean delivery_restrictions = snav_pageURI.indexOf("delivery_restrictions") > -1||snav_pageURI.indexOf("platterRestrictionForm") > -1||snav_pageURI.indexOf("platter_restrictions") > -1||snav_pageURI.indexOf("alcohol_restrictions") > -1;
boolean address_restrictions = snav_pageURI.indexOf("address_restrictions") > -1;
boolean settlement_batch = snav_pageURI.indexOf("settlement_batch") > -1;
boolean top_faqs = snav_pageURI.indexOf("top_faqs") > -1;
boolean click_to_call = snav_pageURI.indexOf("click_to_call") > -1;
boolean search_orders = snav_pageURI.indexOf("search_orders_by_sku") > -1;
boolean modify_orders = snav_pageURI.indexOf("modify_orders_by_sku") > -1;
boolean trans_email = snav_pageURI.indexOf("top_faqs") > -1;
boolean reverseauth_orders=snav_pageURI.indexOf("reverseauth_orders") > -1;
boolean voidcapture_orders=snav_pageURI.indexOf("voidcapture_orders") > -1;
%>
<crm:GetCurrentAgent id='currentAgent'>
<% String lAgentRole = currentAgent.getRole().getLdapRoleName(); %>
<div class="sup_nav_bg">
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"resubmit_orders.jsp")){ %>
	<a href="/admintools/resubmit_orders.jsp" class="sup_nav_link <%=resubmit_orders?"sup_nav_on":"sup_nav"%>">Resubmit<br />Orders</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"resubmit_customers.jsp")){ %>
	<a href="/admintools/resubmit_customers.jsp" class="sup_nav_link <%=resubmit_customers?"sup_nav_on":"sup_nav"%>">Resubmit<br />Customers</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"delete_reservations.jsp")){ %>
	<a href="/admintools/delete_reservations.jsp" class="sup_nav_link <%=delete_reservations?"sup_nav_on":"sup_nav"%>">Delete<br />Reservations</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"mass_cancellation.jsp")){ %>
	<a href="/admintools/mass_cancellation.jsp" class="sup_nav_link <%=mass_cancellation?"sup_nav_on":"sup_nav"%>">Mass<br />Cancellations</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"mass_returns.jsp")){ %>
	<a href="/admintools/mass_returns.jsp" class="sup_nav_link <%=mass_returns?"sup_nav_on":"sup_nav"%>">Mass<br />Returns</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"delivery_restrictions.jsp")){ %>
	<a href="/admintools/delivery_restrictions.jsp" class="sup_nav_link <%=delivery_restrictions?"sup_nav_on":"sup_nav"%>">Delivery<br />Rests</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"address_restrictions.jsp")){ %>
	<a href="/admintools/address_restrictions.jsp" class="sup_nav_link <%=address_restrictions?"sup_nav_on":"sup_nav"%>">Address<br />Rests</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"settlement_batch.jsp")){ %>
	<a href="/admintools/settlement_batch.jsp?method=GET" class="sup_nav_link <%=settlement_batch?"sup_nav_on":"sup_nav"%>">Settlement<br />Batch</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"top_faqs.jsp")){ %>
	<a href="/main/masquerade.jsp?destination=top_faqs" target="_blank" class="sup_nav_link <%=top_faqs?"sup_nav_on":"sup_nav"%>">Top 5<br />FAQ</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"click_to_call.jsp")){ %>
	<a href="/admintools/click_to_call.jsp" class="sup_nav_link <%=click_to_call?"sup_nav_on":"sup_nav"%>">Click to Call<br />Campaign</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"search_orders_by_sku.jsp")){ %>
	<a href="/admintools/search_orders_by_sku.jsp" class="sup_nav_link <%=search_orders?"sup_nav_on":"sup_nav"%>">Search Orders<br />By Skus</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"modify_orders_by_sku.jsp")){ %>
	<a href="/admintools/modify_orders_by_sku.jsp?method=GET" class="sup_nav_link <%=modify_orders?"sup_nav_on":"sup_nav"%>">Modify Orders<br />By Skus</a>
<% } %>

<a href="/admintools/reverseauth_orders.jsp" class="sup_nav_link <%=reverseauth_orders?"sup_nav_on":"sup_nav"%>">Reverse<br />Auth</a>
<a href="/admintools/voidcapture_orders.jsp" class="sup_nav_link <%=voidcapture_orders?"sup_nav_on":"sup_nav"%>">Void<br />Captures</a>
</div>
</crm:GetCurrentAgent>