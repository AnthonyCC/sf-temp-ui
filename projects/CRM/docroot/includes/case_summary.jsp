<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='java.util.Iterator' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.customer.ErpSaleInfo" %>
<%@ page import="com.freshdirect.customer.ErpOrderHistory" %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="weblogic.utils.StringUtils"%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
<%
String pageURI = request.getRequestURI();
String height = "85";
String width = "48";

boolean case_mgmt = pageURI.indexOf("case_mgmt") > -1;
boolean has_user = session.getAttribute(SessionName.USER) != null;
boolean isSecurityQueue= false;
String caseId = request.getParameter("case");

if (case_mgmt){
	height = "100";
	width = "48";
		
} else if (has_user && !case_mgmt){
	height = "100";
}

if (caseId==null) {
%>
    <div class="case_summary_empty">Click on case row above to view details below | Idle time in hours(h) minutes(m), -- indicates closed case | Click on column headers to sort</div>
	<div class="content" style="height: <%= height %>%"></div>
<%	
} else {
%>
	<crm:GetCase id="cm" caseId="<%= caseId %>">
	<crm:GetCurrentAgent id='currAgent'><%

String erpCustId = request.getParameter("erpCustId");
String fdCustId = request.getParameter("fdCustId");
String userRole = currAgent.getRole().getLdapRoleName();
CrmAgentRole crmRole = CrmAgentRole.getEnumByLDAPRole(userRole);
if(null != cm){
	isSecurityQueue=  cm.getSubject().getQueue().getCode().equals("SEQ");
}
boolean isSecuredCase = isSecurityQueue || cm.isPrivateCase();
%>
</crm:GetCurrentAgent>
</crm:GetCase>
	
<%	}	%>	
	
		