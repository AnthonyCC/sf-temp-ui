<%@page import="com.freshdirect.webapp.crm.CrmMasqueradeUtil"%>
<%@page import="com.freshdirect.webapp.crm.security.MenuManager"%>
<%@page import="com.freshdirect.crm.CrmAgentRole"%>
<%@page import="com.freshdirect.webapp.crm.security.CrmSecurityManager"%>
<%@page import="com.freshdirect.security.ticket.MasqueradePurposeBuilder"%>
<%@ taglib uri="crm" prefix="crm" %>

<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.security.ticket.Ticket"%>
<%@ page import="com.freshdirect.security.ticket.TicketService"%>
<%@ page import="com.freshdirect.ErpServicesProperties"%>
<%@ include file="/includes/i_globalcontext.jspf" %>

<%-- === Masquerade! === --%>

<head>
	<title>CRM - Masquerade</title>
</head>

<body>
<crm:GetCurrentAgent id="agent">
<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);  %>

<% if ( user == null || agent == null ) { %>
	Sorry, your session seems to be invalid. Please try to refresh the page, or logout/login.<br/>
<%-- } else if ( !agent.isMasqueradeAllowed() ) { --%>
	<!-- Sorry, masquerade feature is not allowed for <%= agent.getUserId() %>.<br/> -->
<% } else { %>
	You are : <%= agent.getUserId() + " - " + agent.getFirstName() + ", " + agent.getLastName() %><br/>
	Customer is : <%= user.getUserId() + " - " + user.getFirstName() + ", " + user.getLastName() + " [" + user.getFDCustomer().getErpCustomerPK() + "]" %><br/>
	<%
		try {
			String eStoreId = request.getParameter("estore");
			if ("All".equalsIgnoreCase(eStoreId) || "All".equalsIgnoreCase(globalContextStore)) { //fallback
				eStoreId = "FD";
			}
			if (eStoreId == null) {
				eStoreId = globalContextStore;
			}
			
			final String url = CrmMasqueradeUtil.generateLaunchURL(agent, request, user, eStoreId); 
			
			response.sendRedirect( url );
			%><%= url %><%
		} catch ( Exception ex ) {
			ex.printStackTrace();
			%> Sorry, masquerade feature is not available due to some technical error. Please try again later.<br/> Error message: <%= ex.getMessage() %><br/><%
		}
	%>
	<br/>
<% } %>
</crm:GetCurrentAgent>
</body>