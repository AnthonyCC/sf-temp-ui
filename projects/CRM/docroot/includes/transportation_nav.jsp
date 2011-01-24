<%@ page import="com.freshdirect.framework.util.*"%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ taglib uri='crm' prefix='crm' %>
<%
String trans_pageURI = request.getRequestURI();
String trans_qryString = NVL.apply(request.getQueryString(),"");
String selected_date = request.getParameter("dlvDate");
boolean incident_entry = trans_pageURI.indexOf("/transportation/crmLateIssues.jsp") > -1 && trans_qryString.indexOf("lateLog")==-1;
boolean incident_log = trans_pageURI.indexOf("/transportation/crmLateIssues.jsp") > -1   && trans_qryString.indexOf("lateLog")!=-1;
boolean trans_issues = trans_pageURI.indexOf("/transportation/crmTrqCases.jsp") > -1;
boolean isRouteStopReport = trans_pageURI.indexOf("/reports/route_stop_report.jsp") > -1;
boolean isEdit = trans_qryString.indexOf("lateIssuePK")!=-1;; 
String userId = CrmSecurityManager.getUserName(request);
String userRole = CrmSecurityManager.getUserRole(request);
%>
<div class="sup_nav_bg">
<a href="/transportation/crmLateIssues.jsp?lateLog=true<%= selected_date != null && !"".equals(selected_date) ? "&dlvDate=" + selected_date : ""%>" class="<%= incident_log ?"translog_nav_on":"translog_nav"%>">Late Route Incident Log</a>
<% if(CrmSecurityManager.hasAccessToPage(userRole,"route_stop_report.jsp")){ %>
<a href="/reports/route_stop_report.jsp" class="<%=isRouteStopReport?"trans_nav_on":"trans_nav"%>">Orders by Route & Stop</a>
<% } %>
<a href="/transportation/crmLateIssues.jsp<%= selected_date != null && !"".equals(selected_date) ? "?dlvDate=" + selected_date : ""%>" class="<%= incident_entry && !isEdit ?"trans_nav_on":"trans_nav"%>">Report Late Routes</a>
<a href="/transportation/crmTrqCases_step1.jsp<%= selected_date != null && !"".equals(selected_date) ? "?dlvDate=" + selected_date : ""%>" class="<%= trans_issues ?"trans_nav_on":"trans_nav"%>">Report New Transportation Issues</a>

<!-- <a href="/case_mgmt/index.jsp?action=searchCase&queue=TRQ&state=OPEN" class="translog_nav">Current Transportation Issues</a> -->
</div>