<%@ page import="com.freshdirect.framework.util.*"%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ taglib uri='crm' prefix='crm' %>
<%
String trans_pageURI = request.getRequestURI();
String trans_qryString = NVL.apply(request.getQueryString(),"");
String selected_date = request.getParameter("dlvDate");
boolean incident_entry = trans_pageURI.indexOf("/transportation/crmLateIssues.jsp") > -1 && trans_qryString.indexOf("lateLog")==-1;
boolean incident_log = trans_pageURI.indexOf("/transportation/VSStatusLog.jsp") > -1 ;
boolean trans_issues = trans_pageURI.indexOf("/transportation/crmTrqCases.jsp") > -1;
boolean isRouteStopReport = trans_pageURI.indexOf("/reports/route_stop_report.jsp") > -1;
boolean isCampaigns = trans_pageURI.indexOf("campaign") > -1;
boolean isEdit = trans_qryString.indexOf("lateIssuePK")!=-1;;

String userRole = CrmSession.getCurrentAgent(request.getSession()).getRole().getLdapRoleName();
%>
<div class="sup_nav_bg">
<% if(CrmSecurityManager.hasAccessToPage(userRole,"lateLog")){ %>
<a href="/transportation/VSStatusLog.jsp" class="<%= incident_log ?"translog_nav_on":"translog_nav"%>">Voiceshot Status Log</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(userRole,"route_stop_report.jsp")){ %>
<a href="/reports/route_stop_report.jsp" class="<%=isRouteStopReport?"trans_nav_on":"trans_nav"%>">Orders by Route & Stop</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(userRole,"route_stop_report.jsp")){ %>
<a href="/transportation/campaigns.jsp" class="<%=isCampaigns?"trans_nav_on":"trans_nav"%>">Campaigns</a>
<% } %>
<% if(CrmSecurityManager.hasAccessToPage(userRole,"crmTrqCases_step1.jsp")){ %>
<a href="/transportation/crmTrqCases_step1.jsp<%= selected_date != null && !"".equals(selected_date) ? "?dlvDate=" + selected_date : ""%>" class="<%= trans_issues ?"trans_nav_on":"trans_nav"%>">Report New Transportation Issues</a>
<% } %>
<!-- <a href="/case_mgmt/index.jsp?action=searchCase&queue=TRQ&state=OPEN" class="translog_nav">Current Transportation Issues</a> -->
</div>