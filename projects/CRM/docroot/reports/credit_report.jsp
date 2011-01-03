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
	CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE) };
	
final static CrmAgentRole[] APPROVAL_ROLES = {
	CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE) };
%>

<%
	FDComplaintReportCriteria criteria = new FDComplaintReportCriteria();
    Calendar today = Calendar.getInstance();
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
<fd:CrmReportController result="result" id="infos" criteria="<%=criteria%>">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
<form name="credit_report" method="post" action="credit_report.jsp">
    <tr>
        <td width="20%"><span class="sub_nav_title">Credit Report <% if (infos!= null && infos.size()>0) {%>(<span class="result"><%=infos.size()%></span>) <span class="note" style="font-weight:normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result_list','list_content','content_fixed');"> Print View</span><% } %></span></td>
        <td width="65%">
            <table align="center">
                <tr>
                    <td align="right">From:&nbsp;</td>
                    <td>
                        <select name="startMonth" required="true" class="pulldown">
                            <option value="">Month</option>
							<%String[] monthNames = symbols.getShortMonths();%>
							<%  for (int i=0; i < 12; i++) {  %>
								<option value="<%= i %>" <%= (String.valueOf(i).equals(criteria.getStartMonth()))?"selected":"" %>><%= monthNames[i] %></option>
							<%  }   %>
                        </select>
                    </td>
                    <td>
                        <select name="startDay" required="true" class="pulldown">
                            <option value="">Date</option>
							<%  for (int i=1; i<=31; i++) { %>
								<option value="<%= i %>" <%= (String.valueOf(i).equals(criteria.getStartDay()))?"selected":"" %>><%= i %></option>
							<%  } %>
                        </select>
                    </td>
                    <td>
                        <select name="startYear" required="true" class="pulldown">
                            <option value="">Year</option>
							<%  for (int i=2005; i<2016; i++) { %>
								<option value="<%= i %>" <%= (String.valueOf(i).equals(criteria.getStartYear()))?"selected":"" %>><%= i %></option>
							<%  } %>
                        </select>
                    </td>
					<td rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td rowspan="2">&nbsp;<!--Type:<br> 
						<input type="checkbox" name="storeCredits" value="store">&nbsp;Store<br>
                        <input type="checkbox" name="cashbacks" value="refund">&nbsp;Refund-->
                    </td>
					<td rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td align="right">Issued by:&nbsp;</td>
					<td>
						<select name="issuedBy" class="pulldown">
                            <option value="">Any</option>
							<logic:iterate id='role' collection="<%= ISSUING_ROLES %>" type="com.freshdirect.crm.CrmAgentRole">
								<option class="header"><%= role.getName() %></option>
								<logic:iterate id='agent' collection="<%= agents.getAgents(role) %>" type="com.freshdirect.crm.CrmAgentModel">
								<% if (agent.isActive()) { %>
									<option value="<%=agent.getUserId()%>" <%=agent.getUserId().equals(criteria.getIssuedBy()) ? "selected" : "" %>>
										&nbsp;<%= agent.getUserId() %>
									</option>
								<% } %>
								</logic:iterate>
								<option></option>
							</logic:iterate>
                        </select>
					</td>
					<td rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td rowspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td rowspan="2">
                        <input type="submit" class="submit" onClick="javascript:checkForm(credit_report); return false;" value="GO">
                    </td>
                </tr>
                <tr>
                    <td align="right">To:&nbsp;</td>
                    <td>
                        <select name="endMonth" required="true" class="pulldown">
                            <option value="">Month</option>
							<%  for (int i=0; i < 12; i++) {  %>
								<option value="<%= i %>" <%= (String.valueOf(i).equals(criteria.getEndMonth()))?"selected":"" %>><%= monthNames[i] %></option>
							<%  }   %>
                        </select>
                    </td>
                    <td>
                        <select name="endDay" required="true" class="pulldown">
                            <option value="">Date</option>
							<%  for (int i=1; i<=31; i++) { %>
								<option value="<%= i %>" <%= (String.valueOf(i).equals(criteria.getEndDay()))?"selected":"" %>><%= i %></option>
							<%  } %>
                        </select>
                    </td>
                    <td>
                        <select name="endYear" required="true" class="pulldown">
                            <option value="">Year</option>
							<%  for (int i=2005; i<2016; i++) { %>
								<option value="<%= i %>" <%= (String.valueOf(i).equals(criteria.getEndYear()))?"selected":"" %>><%= i %></option>
							<%  } %>
                        </select>
                    </td>
					<td align="right">Approved by:&nbsp;</td>
					<td>
						<select name="approvedBy" class="pulldown">
                            <option value="">Any</option>
							<option class="header">System</option>
							<option value="SYSTEM" <%="SYSTEM".equals(criteria.getApprovedBy()) ? "selected" : "" %>>&nbsp;SYSTEM</option>
							<option value="AUTO_APPROVED" <%="AUTO_APPROVED".equals(criteria.getApprovedBy()) ? "selected" : "" %>>&nbsp;AUTO_APPROVED</option>
							<logic:iterate id='role' collection="<%= APPROVAL_ROLES %>" type="com.freshdirect.crm.CrmAgentRole">
								<option class="header"><%= role.getName() %></option>
								<logic:iterate id='agent' collection="<%= agents.getAgents(role) %>" type="com.freshdirect.crm.CrmAgentModel">
								<% if (agent.isActive()) { %>
									<option value="<%=agent.getUserId()%>" <%=agent.getUserId().equals(criteria.getApprovedBy()) ? "selected" : "" %>>
										&nbsp;<%= agent.getUserId() %>
									</option>
								<% } %>
								</logic:iterate>
								<option></option>
							</logic:iterate>
                        </select>
					</td>
                </tr>
            </table>
        </td>
        <td width="15%" align="right"><a href="/reports/index.jsp">All Reports >></a></td>
    </tr>
	<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.credit_report.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>
    </form>
</table>
</div>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
	<tr>
		<td width="12%">Credit Type</td>
		<td width="12%" align="right">Amount</td>
		<td width="12%" align="center">Status</td>
		<td width="16%">Issued by</td>
		<td width="16%">Approved by</td>
		<td width="14%">Order #</td>
		<td width="18%">Customer</td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
</table>
</div>
<%if(infos != null && !infos.isEmpty()){%>
<div class="list_content" id="result_list">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
	<logic:iterate id="info" collection="<%= infos %>" type="com.freshdirect.fdstore.customer.FDComplaintInfo" indexId="counter">
		<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> >
			<td width="12%" class="border_bottom"><%= info.getComplaintType()%>&nbsp;</td>
			<td width="12%" align="right" class="border_bottom">&nbsp;<%= JspMethods.formatPrice(info.getComplaintAmount()) %></td>
			<td width="12%" align="center" class="border_bottom"><%= info.getComplaintStatus().getName()!=null ? info.getComplaintStatus().getName():"&nbsp;" %></td>
			<td width="16%" class="border_bottom"><%= info.getIssuedBy() %>&nbsp;</td>
			<td width="16%" class="border_bottom"><%= info.getApprovedBy() %>&nbsp;</td>
			<td width="14%" class="border_bottom"><%= info.getSaleId() %>&nbsp;</td>
			<td width="18%" class="border_bottom"><%= info.getLastName() %>, <%= info.getFirstName() %></td>
		</tr>
	</logic:iterate>
    </table>
</div>
<%}else{%>
	<%if("POST".equalsIgnoreCase(request.getMethod()) && criteria.getStartDate() != null && criteria.getEndDate() != null){%>
		<div class="content_fixed" align="center"><br><b>No complaints found for <%= CCFormatter.formatDate(criteria.getStartDate())%> - <%= CCFormatter.formatDate(criteria.getEndDate())%></b><br><br></div>
	<%}%>
<%}%>
</fd:CrmReportController>
</tmpl:put>

</tmpl:insert>
