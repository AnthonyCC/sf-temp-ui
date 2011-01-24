<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%
String lAgentId = CrmSecurityManager.getUserName(request);
String lAgentRole = CrmSecurityManager.getUserRole(request);
String snav_pageURI = request.getRequestURI();
boolean agent_worklists = snav_pageURI.indexOf("/supervisor/index.jsp") > -1;
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
%>

<div class="side_nav_module_supervisor" >
<!-- <a href="/supervisor/index.jsp" class="<%=agent_worklists?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Agents</a>  -->
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"account_deactivations.jsp")){ %>
	<div  class="<%=account_deactivations?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/account_deactivations.jsp" class="<%=account_deactivations?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Acct. Deactivations</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"payment_exceptions_list.jsp")){ %>
	<div  class="<%=payment_exceptions?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/payment_exceptions_list.jsp" class="<%=payment_exceptions?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Pymt. Exceptions</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"pending_credits_list.jsp")){ %>
	<div  class="<%=pending_credits?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/pending_credits_list.jsp" class="<%=pending_credits?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Pend. Credits</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"refusal_list.jsp")){ %>
	<div  class="<%=refusal_list?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/refusal_list.jsp" class="<%=refusal_list?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Refusals</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"pending_return_list.jsp")){ %>
	<div  class="<%=pending_return?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/pending_return_list.jsp" class="<%=pending_return?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Pend. Returns</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"order_cc_authorization.jsp")){ %>
	<div  class="<%=cc_authorization?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/order_cc_authorization.jsp" class="<%=cc_authorization?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Orders by CC Info</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"address_exception.jsp")){ %>
	<div  class="<%=address_exception?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/address_exception.jsp" class="<%=address_exception?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Address Excep.</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"geocode_exception.jsp")){ %>
	<div  class="<%=geocode_exception?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/geocode_exception.jsp" class="<%=geocode_exception?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Geocode Excep.</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"make_good_order.jsp")){ %>
	<div  class="<%=make_good_order?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/make_good_order.jsp" class="<%=make_good_order?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Make Good Orders</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"bad_account_list.jsp")){ %>
	<div  class="<%=bad_account_list?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/bad_account_list.jsp" class="<%=bad_account_list?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Bad Acct.</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"broken_acct_list.jsp")){ %>
	<div  class="<%=broken_accounts?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/broken_acct_list.jsp?method=GET" class="<%=broken_accounts?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Broken Accounts</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"acct_lookup.jsp")){ %>
	<div  class="<%=acct_lookup?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="javascript:pop('/supervisor/acct_lookup.jsp', '500', '580')" class="<%=acct_lookup?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Acct. Lookup</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"canned_text.jsp")){ %>
	<div  class="<%=canned_text?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/canned_text.jsp" class="<%=canned_text?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Canned Text</a></div>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(lAgentRole,"connect.jsp")){ %>
	<div  class="<%=connect?"sup_nav_on_supervisor":""%>" style="height: auto;"><a href="/supervisor/connect.jsp" class="<%=connect?"sup_nav_on_supervisor":"sup_nav_supervisor"%>">Givex Admin</a></div>
<% } %>

</div>

