<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>
<%
String userRole = CrmSecurityManager.getUserRole(request);
CrmAgentRole crmRole = CrmAgentRole.getEnumByLDAPRole(userRole);
%>
<%= crmRole.getCode() %>
<% if(CrmAgentRole.ADM_CODE.equals(crmRole.getCode())||CrmAgentRole.DEV_CODE.equals(crmRole.getCode())||CrmAgentRole.QA_CODE.equals(crmRole.getCode())||CrmAgentRole.SUP_CODE.equals(crmRole.getCode()) ) { %>
<jsp:forward page="/reports/subject_report.jsp" />

<% } else if(CrmAgentRole.TRN_CODE.equals(crmRole.getCode())||CrmAgentRole.OPS_CODE.equals(crmRole.getCode())||CrmAgentRole.SOP_CODE.equals(crmRole.getCode())) { %>
<jsp:forward page="/reports/late_delivery_report.jsp" />
<% } else if(CrmAgentRole.FIN_CODE.equals(crmRole.getCode()) || CrmAgentRole.BUS_CODE.equals(crmRole.getCode())){ %>
<jsp:forward page="/reports/credit_summary_report.jsp" />
<% } else if(CrmAgentRole.SEC_CODE.equals(crmRole.getCode())){ %>
<jsp:forward page="/reports/settlement_problem_report.jsp" />
<% } %>