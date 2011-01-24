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
String userId = CrmSecurityManager.getUserName(request);
String userRole = CrmSecurityManager.getUserRole(request);
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
	<%
if(null != cm){
	isSecurityQueue=  cm.getSubject().getQueue().getCode().equals("SEQ");
}
String erpCustId = request.getParameter("erpCustId");
String fdCustId = request.getParameter("fdCustId");

%><crm:GetFDUser id="user" useId="true" erpCustId="<%= erpCustId %>" fdCustId="<%= fdCustId %>">
	<div class="<%= isSecurityQueue?"case_summary_header_seq":"case_summary_header" %>">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="case_header_text">
		<tr>
			<td width="50%">
				<table width="100%" class="case_summary_header_text">
					<tr>
					<td width="20%">Case # <span class="case_header_value"><b><%= cm.getPK().getId() %></b></span></td>
					<td width="25%">Status: <span class="case_header_value"><%= cm.getState().getName() %></span></td>
					<td width="30%">
						Locked: <span class="case_header_value">
							<% if (cm.getLockedAgentUserId()!=null) { %>								
									<%= cm.getLockedAgentUserId()%>								
							<% } %>
						</span>
					</td>
					<td width="25%" align="right" style="padding-right: 16px;">Origin: <span class="case_header_value"><%= cm.getOrigin().getName() %></span></td>
					</tr>
				</table>
			</td>
			<td width="50%">
				<table width="100%" cellpadding="0" cellspacing="0" border="0" class="case_header_text">
					<tr>
						<td width="50%"><b>Actions</b></td>
						<td width="50%" align="right">
								<% if (userRole.equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE).getLdapRoleName()) || (cm.getLockedAgentUserId() == null || "".equals(cm.getLockedAgentUserId())) ||userId.equals(cm.getLockedAgentUserId())) { %>
									<input type="submit" class="case_action" value="EDIT CASE FIELDS" onClick="parent.window.location.href='/case/case.jsp?case=<%=caseId%>&erpCustId=<%=erpCustId%>'">
								<% } %>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</div>

	<div class="case_content_scroll" style="width: 50%; height: <%= height %>%; padding-top: 0px; <% if (!userId.equals(cm.getLockedAgentUserId())) { %>background-color: #FFFFFF;<% } %>">
		<div class="case_subheader">
			<table width="100%" class="case_subheader_text">
				<tr>
					<td>
                        Customer: 
                        <% if (cm.getCustomerPK()!=null) { %>
                            <b><%= cm.getCustomerFirstName().toUpperCase().charAt(0) %> 
                               <%= cm.getCustomerLastName().toUpperCase().charAt(0) + cm.getCustomerLastName().substring(1,cm.getCustomerLastName().length()).toLowerCase()%>
                            </b>
                        <% } else { %>
                            <span class="not_set">-None-</span>
                        <% } %>
                    </td>
					<td style="text-align: center;">Order #: <% if (cm.getSalePK()!=null) { %><b><%= cm.getSalePK().getId() %></b><% } else { %><span class="not_set">-None-</span><% } %>
					</td>
					<%
					
					ErpOrderHistory hist = (ErpOrderHistory) user.getOrderHistory();

					// display truck and stop no if available
					if (cm.getSalePK() != null) {
						final String saleId = cm.getSalePK().getId();
						ErpSaleInfo esi = null;

						for (Iterator it=hist.getErpSaleInfos().iterator(); it.hasNext();) {
							ErpSaleInfo esi2 = (ErpSaleInfo) it.next();
							if (saleId.equals(esi2.getSaleId()) ) {
								esi = esi2;
								break;
							}
						}

						if (esi != null) {
					%>
					<td>
					T: <span style="font-weight: bold;"><%= esi.getTruckNumber() != null ? new Integer(esi.getTruckNumber()).toString() : "-" %></span>
					</td><td>
					S: <span style="font-weight: bold;"><%= esi.getStopSequence() != null ? new Integer(esi.getStopSequence()).toString() : "-" %></span>
					</td>
					<%		}
						} %>
					<td style="text-align: right;">Created: <b><%= CCFormatter.formatCaseDate(cm.getCreateDate()) %></b></td>
				</tr>
			</table>
		</div>
		<div class="<%= isSecurityQueue?"case_content_seq":"case_content" %>">
			<table width="100%" cellpadding="0" cellspacing="8" border="0" class="case_content_text">
				<tr valign="top">
					<td width="10%" align="right" class="case_content_field">Queue:</td>
					<td width="20%"><%= cm.getSubject().getQueue().getCode() %></td>
					<td width="45%" align="right" class="case_content_field">Priority:</td>
					<td width="15%"><%= cm.getPriority().getName() %></td>
				</tr>
				<tr valign="top">
					<td align="right" class="case_content_field">Subject:</td>
					<td colspan="3"><%= cm.getSubject().getName() %></td>
				</tr>
				<tr valign="top">
					<td align="right" class="case_content_field">Quantity:</td>
					<td colspan="2">Reported: <%= cm.getProjectedQuantity() %>
					&nbsp;&nbsp;Actual: <%= cm.getActualQuantity() %>
					</td>
				</tr>
				<tr valign="top">
					<td align="right" class="case_content_field">Summary:</td>
					<td colspan="3"><%= cm.getSummary() %></td>
				</tr>
				<tr valign="top">
					<td class="case_content_field" align="right">Assigned:</td>
					<td colspan="3">
					<%if(cm.getAssignedAgentUserId() != null){%>						
						<%= cm.getAssignedAgentUserId() %>						
					<% } else { %>
						<span class="not_set">-None-</span>
					<% } %>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="case_content_field">Related Departments:</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="3"><i>
						<logic:iterate id='department' indexId='i' collection='<%= cm.getDepartments() %>' type='com.freshdirect.crm.CrmDepartment'>
							<%= department.getName() %>&nbsp;&nbsp;
						</logic:iterate>
					</i></td>
				</tr>
			</table>
			</div>
	</div>

	
	<div class="case_content<%=(isSecurityQueue?"_seq":"_scroll")%>" style="width: <%= width %>%; height: <%= height %>%; <% if (!userId.equals(cm.getLockedAgentUserId())) { %>background-color: #FFFFFF;<% } %>">
		<%@ include file="/includes/case_actions.jspf" %>
	</div>

</crm:GetFDUser>

</crm:GetCase>
	
<%	}	%>	
	
		