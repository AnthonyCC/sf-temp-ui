<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%
//String lAgentId = CrmSecurityManager.getUserName(request);

String snav_pageURI = request.getRequestURI();
boolean account_deactivations = snav_pageURI.indexOf("account_deactivations") > -1;
boolean payment_exceptions = snav_pageURI.indexOf("payment_exceptions_list") > -1;
boolean pending_credits = snav_pageURI.indexOf("pending_credits_list") > -1;
boolean refusal_list = snav_pageURI.indexOf("refusal_list") > -1;
boolean pending_return = snav_pageURI.indexOf("pending_return_list") > -1;
boolean make_good_order = snav_pageURI.indexOf("make_good_order") > -1;
boolean address_exception = snav_pageURI.indexOf("address_exception") > -1;
boolean geocode_exception = snav_pageURI.indexOf("geocode_exception") > -1;
boolean cc_authorization = snav_pageURI.indexOf("order_cc_authorization") > -1;
boolean bad_account_list = snav_pageURI.indexOf("bad_account_list") > -1;
boolean broken_accounts = snav_pageURI.indexOf("broken_acct_list") > -1;

boolean acct_lookup = snav_pageURI.indexOf("acct_lookup") > -1;
boolean canned_text = snav_pageURI.indexOf("canned_text") > -1;
boolean connect = snav_pageURI.indexOf("connect") > -1;
boolean crm_auths_view= snav_pageURI.indexOf("crm_auths_view") > -1;
boolean auto_late_dlv_credits_view = snav_pageURI.indexOf("auto_late_dlv_credits") > -1 || snav_pageURI.indexOf("auto_late_credit_orders") > -1;
boolean paymentGateway = snav_pageURI.indexOf("paymentGateway") > -1;
%>
<crm:GetCurrentAgent id='currentAgent'>
<div class="side_nav_module_supervisor">
<% String lAgentRole = currentAgent.getRole().getLdapRoleName(); %>


<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"account_deactivations.jsp")){ %>
	<a href="/supervisor/account_deactivations.jsp" class="sup_nav_link <%=account_deactivations?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Acct.<br />Deactivations</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"payment_exceptions_list.jsp")){ %>
	<a href="/supervisor/payment_exceptions_list.jsp" class="sup_nav_link <%=payment_exceptions?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Pymt.<br />Exceptions</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"pending_credits_list.jsp")){ %>
	<a href="/supervisor/pending_credits_list.jsp" class="sup_nav_link <%=pending_credits?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Pend.<br />Credits</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"refusal_list.jsp")){ %>
	<a href="/supervisor/refusal_list.jsp" class="sup_nav_link <%=refusal_list?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Refusals</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"pending_return_list.jsp")){ %>
	<a href="/supervisor/pending_return_list.jsp" class="sup_nav_link <%=pending_return?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Pend.<br />Returns</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"order_cc_authorization.jsp")){ %>
	<a href="/supervisor/order_cc_authorization.jsp" class="sup_nav_link <%=cc_authorization?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Orders by<br />CC Info</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"address_exception.jsp")){ %>
	<a href="/supervisor/address_exception.jsp" class="sup_nav_link <%=address_exception?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Address<br />Excep.</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"geocode_exception.jsp")){ %>
	<a href="/supervisor/geocode_exception.jsp" class="sup_nav_link <%=geocode_exception?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Geocode<br />Excep.</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"make_good_order.jsp")){ %>
	<a href="/supervisor/make_good_order.jsp" class="sup_nav_link <%=make_good_order?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Make Good<br />Orders</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"bad_account_list.jsp")){ %>
	<a href="/supervisor/bad_account_list.jsp" class="sup_nav_link <%=bad_account_list?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Bad<br />Accounts</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"broken_acct_list.jsp")){ %>
	<a href="/supervisor/broken_acct_list.jsp?method=GET" class="sup_nav_link <%=broken_accounts?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Broken<br />Accounts</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"acct_lookup.jsp")){ %>
	<a href="javascript:pop('/supervisor/acct_lookup.jsp', '500', '580')" class="sup_nav_link <%=acct_lookup?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Acct.<br />Lookup</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"canned_text.jsp")){ %>
	<!-- <div  class="<%=canned_text?"sup_nav_on_supervisor":""%>"><a href="/supervisor/canned_text.jsp" class="<%=canned_text?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Canned Text</a></div> -->
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"connect.jsp")){ %>
	<a href="<%= FDStoreProperties.getGivexWebServerURL() %>" target="_blank" class="hasTooltip sup_nav_link <%=connect?"sup_nav_on_supervisor":"sup_nav_supervisor"%>" title="<span style='font-family: monospace;'>Open GiveX Login<br /><br />L: <%= FDStoreProperties.getFDGivexWebUser() %><br />P: <%= FDStoreProperties.getFDGivexWebUserPassword() %></span>">Givex<br />Admin</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"crm_auths_view.jsp")){ %>
	<a href="/supervisor/crm_auths_view.jsp" class="sup_nav_link <%=crm_auths_view?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">View<br />Auths</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"auto_late_dlv_credits.jsp")){ %>
	<a href="/supervisor/auto_late_dlv_credits.jsp" class="sup_nav_link <%=auto_late_dlv_credits_view?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Auto Late<br />Dlv. Credits</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"paymentGateway.jsp")){ %>
	<a href="/supervisor/paymentGateway.jsp" target="_blank" class="sup_nav_link <%=paymentGateway?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Payment<br />Gateway</a>
<% } %>
</div>
</crm:GetCurrentAgent>
