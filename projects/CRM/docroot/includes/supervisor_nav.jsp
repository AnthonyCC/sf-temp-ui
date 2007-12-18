<%@ taglib uri='crm' prefix='crm' %>
<%
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
%>
<crm:GetCurrentAgent id='currentAgent'>
<div class="sup_nav_bg">
<a href="/supervisor/index.jsp" class="<%=agent_worklists?"sup_nav_on":"sup_nav"%>">Agents</a>
<a href="/supervisor/account_deactivations.jsp" class="<%=account_deactivations?"sup_nav_on":"sup_nav"%>">Acct. Deactivations</a>
<a href="/supervisor/payment_exceptions_list.jsp" class="<%=payment_exceptions?"sup_nav_on":"sup_nav"%>">Pymt. Exceptions</a>
<a href="/supervisor/pending_credits_list.jsp" class="<%=pending_credits?"sup_nav_on":"sup_nav"%>">Pend. Credits</a>
<a href="/supervisor/refusal_list.jsp" class="<%=refusal_list?"sup_nav_on":"sup_nav"%>">Refusals</a>
<a href="/supervisor/pending_return_list.jsp" class="<%=pending_return?"sup_nav_on":"sup_nav"%>">Pend. Returns</a>
<a href="/supervisor/order_cc_authorization.jsp" class="<%=cc_authorization?"sup_nav_on":"sup_nav"%>">Orders by CC Info</a>
<a href="/supervisor/address_exception.jsp" class="<%=address_exception?"sup_nav_on":"sup_nav"%>">Address Excep.</a>
<a href="/supervisor/geocode_exception.jsp" class="<%=geocode_exception?"sup_nav_on":"sup_nav"%>">Geocode Excep.</a>
<a href="/supervisor/make_good_order.jsp" class="<%=make_good_order?"sup_nav_on":"sup_nav"%>">Make Good Orders</a>
<a href="/supervisor/bad_account_list.jsp" class="<%=bad_account_list?"sup_nav_on":"sup_nav"%>">Bad Acct.</a>
<a href="/supervisor/broken_acct_list.jsp?method=GET" class="<%=broken_accounts?"sup_nav_on":"sup_nav"%>">Broken Accounts</a>
<a href="javascript:pop('/supervisor/acct_lookup.jsp', '500', '580')" class="<%=acct_lookup?"sup_nav_on":"sup_nav"%>">Acct. Lookup</a>
</div>
</crm:GetCurrentAgent>