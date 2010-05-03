<%@ taglib uri='crm' prefix='crm' %>
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
%>
<crm:GetCurrentAgent id='currentAgent'>
<div class="sup_nav_bg">
<a href="/admintools/index.jsp" class="<%=resubmit_orders?"sup_nav_on":"sup_nav"%>">Resub. Orders</a>
<a href="/admintools/resubmit_customers.jsp" class="<%=resubmit_customers?"sup_nav_on":"sup_nav"%>">Resub. Customers</a>
<a href="/admintools/delete_reservations.jsp" class="<%=delete_reservations?"sup_nav_on":"sup_nav"%>">Del. Reservations</a>
<a href="/admintools/mass_cancellation.jsp" class="<%=mass_cancellation?"sup_nav_on":"sup_nav"%>">Mass Cancellations</a>
<a href="/admintools/mass_returns.jsp" class="<%=mass_returns?"sup_nav_on":"sup_nav"%>">Mass Returns</a>
<a href="/admintools/delivery_restrictions.jsp" class="<%=delivery_restrictions?"sup_nav_on":"sup_nav"%>">Delivery Restrictions</a>
<a href="/admintools/address_restrictions.jsp" class="<%=address_restrictions?"sup_nav_on":"sup_nav"%>">Address Restrictions</a>
<a href="/admintools/settlement_batch.jsp?method=GET" class="<%=settlement_batch?"sup_nav_on":"sup_nav"%>">settlement batch</a>
<a href="/admintools/top_faqs.jsp" class="<%=top_faqs?"sup_nav_on":"sup_nav"%>">Top 5 FAQ</a>
<a href="/admintools/click_to_call.jsp" class="<%=click_to_call?"sup_nav_on":"sup_nav"%>">Click to Call Campaign</a>
</div>
</crm:GetCurrentAgent>