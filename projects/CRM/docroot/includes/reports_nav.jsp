<%@ taglib uri='crm' prefix='crm' %>
<%
String rnav_pageURI = request.getRequestURI();
boolean subject_report = rnav_pageURI.indexOf("/reports/index.jsp") > -1 || rnav_pageURI.indexOf("subject_report") > -1;
boolean late_delivery_report = rnav_pageURI.indexOf("late_delivery_report") > -1;
boolean route_stop_report = rnav_pageURI.indexOf("route_stop_report") > -1;
boolean order_status_report = rnav_pageURI.indexOf("order_status_report") > -1;
boolean credit_summary_report = rnav_pageURI.indexOf("/reports/credit_summary_report") > -1;
boolean credit_report = rnav_pageURI.indexOf("/reports/credit_report") > -1;
boolean settlement_problem_report = rnav_pageURI.indexOf("/reports/settlement_problem_report") > -1;
%>
<crm:GetCurrentAgent id='currentAgent'>
<div class="rep_nav_bg">
<a href="/reports/subject_report.jsp" class="<%=subject_report?"rep_nav_on":"rep_nav"%>">Cases by Queues & Subjects</a>
<a href="/reports/late_delivery_report.jsp" class="<%=late_delivery_report?"rep_nav_on":"rep_nav"%>">Late Delivery Orders</a>
<a href="/reports/route_stop_report.jsp" class="<%=route_stop_report?"rep_nav_on":"rep_nav"%>">Orders by Route & Stop</a>
<a href="/reports/order_status_report.jsp" class="<%=order_status_report?"rep_nav_on":"rep_nav"%>">Orders by Status</a>
<a href="/reports/credit_summary_report.jsp" class="<%=credit_summary_report?"rep_nav_on":"rep_nav"%>">Credit Summary Report</a>
<a href="/reports/credit_report.jsp" class="<%=credit_report?"rep_nav_on":"rep_nav"%>">Credit Full Report</a>
<a href="/reports/settlement_problem_report.jsp" class="<%=settlement_problem_report?"rep_nav_on":"rep_nav"%>">Settlement Problem Report</a>
</div>
</crm:GetCurrentAgent>