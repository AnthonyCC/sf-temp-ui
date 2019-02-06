<%@ page import='java.util.*' %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.fdstore.customer.FDComplaintReportCriteria" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentList" %>
<%@ page import="com.freshdirect.crm.CrmManager" %>
<%@ page import="com.freshdirect.crm.CrmAgentRole" %>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>


<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Credit Report</tmpl:put>

<tmpl:put name='content' direct='true'>
<%! DateFormatSymbols symbols = new DateFormatSymbols();    

final static CrmAgentRole[] ISSUING_ROLES = {
	
	CrmAgentRole.getEnum(CrmAgentRole.COS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.OPS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SOP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.NCS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SCS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.FDX_CODE)
	};
	
final static CrmAgentRole[] APPROVAL_ROLES = {
	CrmAgentRole.getEnum(CrmAgentRole.COS_CODE),	
	CrmAgentRole.getEnum(CrmAgentRole.SOP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.FDX_CODE)
	};
%>

<%
	FDComplaintReportCriteria criteria = new FDComplaintReportCriteria();
    Calendar today = Calendar.getInstance();
    int curryear  = today.get(Calendar.YEAR);
	criteria.setStartMonth(NVL.apply(request.getParameter("startMonth"), String.valueOf(today.get(Calendar.MONTH))));
	criteria.setStartDay(NVL.apply(request.getParameter("startDay"), String.valueOf(today.get(Calendar.DATE))));
	criteria.setStartYear(NVL.apply(request.getParameter("startYear"), String.valueOf(today.get(Calendar.YEAR))));
    
    today.add(Calendar.DATE,1);
	
	criteria.setEndMonth(NVL.apply(request.getParameter("endMonth"), String.valueOf(today.get(Calendar.MONTH))));
	criteria.setEndDay(NVL.apply(request.getParameter("endDay"), String.valueOf(today.get(Calendar.DATE))));
	criteria.setEndYear(NVL.apply(request.getParameter("endYear"), String.valueOf(today.get(Calendar.YEAR))));
	
	criteria.setIssuedBy(NVL.apply(request.getParameter("issuedBy"), "").trim());
	criteria.setApprovedBy(NVL.apply(request.getParameter("approvedBy"), "").trim());
	criteria.setStoreCredits(request.getParameter("storeCredits") != null);
	criteria.setCashbacks(request.getParameter("cashbacks") != null);
	
	CrmAgentList agents = CrmManager.getInstance().getAllAgents(true);
%>

<jsp:include page="/includes/reports_nav.jsp" />
<div class="sub_nav">

</tmpl:put>

</tmpl:insert>
