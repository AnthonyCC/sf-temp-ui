<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.customer.ErpShippingInfo" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Case Details</tmpl:put>
	<crm:GetCase id="cm" caseId='<%= request.getParameter("case") %>'>
    <%
        String fdCustId = "";
        String erpCustId = "";
        if(cm.getCustomerPK() != null){
            erpCustId = cm.getCustomerPK().getId();
            fdCustId = CrmSession.getFDCustomerId(session, erpCustId);
        }
    %>
	<%-- get user put in session --%>
	<crm:GetFDUser id="user" useId="true" erpCustId="<%=erpCustId%>" fdCustId="<%=fdCustId%>">
	<crm:LockCase case="<%= cm %>"/>
	
	<%
		boolean forPrint = "YES".equalsIgnoreCase(request.getParameter("forPrint"));
	%>
	
    	<tmpl:put name='content' direct='true'>
			
			<div class="case_header" style="padding-top: 2px; padding-bottom: 2px;">
				<table width="100%" cellpadding="0" cellspacing="0" border="0" class="case_header_text">
					<tr>
						<td width="20%" class="case_header_title_text"><b>Case # <span class="case_header_title_value"><%= cm.getPK().getId() %></span></b> &raquo; <%= cm.getState()==null ? "Open" : cm.getState().getName() %></td>
						<td width="60%">
							<table width="100%" cellpadding="0" cellspacing="0" class="case_subheader" style="color: #000000; font-size: 8pt; border-bottom: none; padding: 2px;">
								<tr>
									<td>Customer:
									<% if (cm.getCustomerPK()!=null) { 
										boolean noCaseCust = false;
										FDIdentity identity = null;
										if (user != null) {
											identity = user.getIdentity();
										}
								
										if ( user == null || (identity != null && !erpCustId.equals(identity.getErpCustomerPK())) ) {
											noCaseCust = true;
										}
										if (noCaseCust) {%><a href="/main/account_details.jsp?erpCustId=<%= cm.getCustomerPK().getId() %>&fdCustId=<%= fdCustId %>" class="case_subheader_action"><% } %><b><%= cm.getCustomerFirstName().toUpperCase().charAt(0) %> <%= cm.getCustomerLastName().toUpperCase().charAt(0) + cm.getCustomerLastName().substring(1,cm.getCustomerLastName().length()).toLowerCase()%></b><% if (noCaseCust) {%></a><% } %>
									<% } else { %>
										<span class="not_set">-None-</span>
									<% } %>
									</td><td>
									<% if (cm.getSalePK()!=null) {
									%>Order #: <a href="/main/order_details.jsp?orderId=<%= cm.getSalePK().getId() %>" class="case_subheader_action"><b><%= cm.getSalePK().getId() %></b></a>
									<% 
										ErpShippingInfo si = CrmSession.getOrder(session, cm.getSalePK().getId()).getShippingInfo();
										if (si != null) {
									%>
									</td><td>
									Truck: <span style="font-weight: bold;"><%= si.getTruckNumber() %></span>
									</td><td>
									Stop: <span style="font-weight: bold;"><%= si.getStopSequence() %></span>
									<%	}
									} else {
									%>Order #: <span class="not_set">-None-</span>
									<% } %>
									</td><td>
									Origin: <span style="font-weight: bold;"><%= cm.getOrigin().getName() %></span>
									</td>
									<td align="right">Created: <b><%= CCFormatter.formatCaseDate(cm.getCreateDate()) %></b>&nbsp;</td>
								</tr>
							</table>
						</td>
						<td width="20%" align="center"><a href="/case/unlock.jsp" class="case_header_text"><img src="/media_stat/crm/images/unlock.gif" width="10" height="15" border="0" hspace="0" vspace="0"> unlock</a></td>
					</tr>
				</table>
			</div>
			
			<div class="case_content" <%= (session.getAttribute(SessionName.USER) != null) ? "style='height: 78%;'" : "" %>>

				<%@ include file="/includes/case_details.jspf" %>

				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td width="100%" style="vertical-align: bottom;">
						<div class="case_header_color" style="padding: 0px; padding-left: 11px; padding-right: 11px; float: left;"><span class="case_header_title_text"><b>Actions</b></span></div>
						<div style="float: right; margin-right: 10px; background: #FFFFFF; padding-left: 5px; padding-right: 5px;"><a href="<%=request.getRequestURI()%>?case=<%= cm.getPK().getId() %><%= forPrint ?"":"&forPrint=YES"%>" class="case_action_text"><%= forPrint ?"Regular":"Print"%> View</a></div>
						</td>
					</tr>
					<tr>
						<td colspan="1" class="case_header_color"><img src="/media_stat/crm/images/clear.gif" width="1" height="3"></td>
					</tr>
				</table>
				<div class="case_content<%=forPrint?"":"_scroll"%>" style="width: 100%; bottom: 0px; height: <%= (session.getAttribute(SessionName.USER) != null) ? "35": "42" %>%;">
					<%@ include file="/includes/case_actions.jspf" %>
				</div>

			</div>
		</tmpl:put>
	</crm:GetFDUser>
    </crm:GetCase>
</tmpl:insert>
