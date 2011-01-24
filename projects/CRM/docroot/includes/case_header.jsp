<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ taglib uri='crm' prefix='crm' %>

<% 
	String pageURI = request.getRequestURI(); 
	String agentId = CrmSecurityManager.getUserName(request);
	String agentRole = CrmSecurityManager.getUserRole(request);
	String redirect = pageURI.indexOf("/main/worklist") > -1 ? "?redirect=worklist" : (pageURI.indexOf("/case_mgmt/index.jsp") > -1) ? "?redirect=case_mgmt" : "";
	boolean notCaseOwner = false;
	boolean isSecurityQueue = false;
	if (pageURI.indexOf("/case/case.jsp") < 0 ) { %>
		<crm:GetLockedCase id="cm">
		
		<%if (cm!=null) {
			FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
			String erpCustId = cm.getCustomerPK().getId();
			if(cm.getCustomerPK() != null && user != null && !user.getIdentity().getErpCustomerPK().equals(erpCustId)) {
				notCaseOwner = true;
			}else{
				isSecurityQueue=  cm.getSubject().getQueue().getCode().equals("SEQ");
			}
		%>
	<%= notCaseOwner ? "<span class='mismatch_note'><b>Customer is NOT current case owner.</b> Options: <b>Create a new case or select another case for this customer</b> OR <b>Click on case customer name below to switch customers</b> OR <b>Unlock case</b>.</span>" : "" %>
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="<%= notCaseOwner ? "mismatch" : (isSecurityQueue?"case_header_seq":"case_header")%>">
			<tr>
				<% if(isSecurityQueue){ %>
				<td width="5%" class="case_content_field_seq"><b>SECURITY</b>&nbsp;</td>
				<% } %>
				<td width="8%" class="case_summary_header_text"></span>Case <a href="/case/case.jsp?case=<%= cm.getPK().getId() %>" class="case_summary_header_value"><%= cm.getPK().getId() %></a></td>
				<% if (cm.getCustomerPK()!=null) {%>
				<td width="10%" class="case_summary_header_text">Customer <span class="case_summary_header_value">
				<% if (!notCaseOwner) {%>
					<a href="/main/account_details.jsp?erpCustId=<%= cm.getCustomerPK().getId() %>" class="case_summary_header_value">
				<% } %>
					<%= cm.getCustomerFirstName().toUpperCase().charAt(0) %> 
					<%= cm.getCustomerLastName().toUpperCase().charAt(0) + cm.getCustomerLastName().substring(1,cm.getCustomerLastName().length()).toLowerCase()%>
					<% if (!notCaseOwner) {%>
						</a>
					<% } %>
				</span>
				</td>
				<% } %>
				<% if (cm.getSalePK()!=null) { %>
					<td width="12%" class="case_summary_header_text">
						Order # <span class="case_summary_header_value">
							<% if (pageURI.indexOf("/main/order_details.jsp") < 0 ) { %>
								<a href="/main/order_details.jsp?orderId=<%= cm.getSalePK().getId() %>" class="case_summary_header_value">
							<% } %>
							<%= cm.getSalePK().getId() %>
							<% if (pageURI.indexOf("/main/order_details.jsp") < 0 ) { %>
								</a>
							<% } %>
						</span>
					</td>
				<% } %>
				<td width="4%" class="case_summary_header_text"><b><%= cm.getSubject().getQueue().getCode() %></b></td>
				<td width="16%" class="case_summary_header_text"><%= cm.getSubject().getName() %></td>
				<td width="10%" class="case_summary_header_text">Origin: <span class="case_summary_header_value"><%= cm.getOrigin().getName() %></span></td>
				<td width="16%" class="case_summary_header_value">Created: <span class="case_summary_header_value"><%= CCFormatter.formatCaseDate( cm.getCreateDate() ) %></span></td>
				<td width="13%" class="case_summary_header_text">
					Assigned: <%= cm.getAssignedAgentUserId() %>
					
				</td>
				<td width="7%" align="right" class="case_summary_header_text"><a href="/case/unlock.jsp<%= redirect %>" class="case_summary_header_text">
				<img src="/media_stat/crm/images/unlock_s.gif" width="8" height="12" border="0"> unlock</a></td>
			</tr>
		</table>
	<% } %>
	
	</crm:GetLockedCase>
<% } %>