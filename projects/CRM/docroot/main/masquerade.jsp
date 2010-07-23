<%@ taglib uri="crm" prefix="crm" %>

<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.security.ticket.Ticket"%>
<%@ page import="com.freshdirect.security.ticket.TicketService"%>
<%@ page import="com.freshdirect.ErpServicesProperties"%>

<%-- === Masquerade! === --%>

<head>
	<title>CRM - Masquerade</title>
</head>

<body>
<crm:GetCurrentAgent id="agent">
<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);  %>

<% if ( user == null || agent == null ) { %>
	Sorry, your session seems to be invalid. Please try to refresh the page, or logout/login.<br/>
<% } else if ( !agent.isMasqueradeAllowed() ) { %>
	Sorry, masquerade feature is not allowed for <%= agent.getUserId() %>.<br/>
<% } else { %>
	You are : <%= agent.getUserId() + " - " + agent.getFirstName() + ", " + agent.getLastName() %><br/>
	Customer is : <%= user.getUserId() + " - " + user.getFirstName() + ", " + user.getLastName() + " [" + user.getFDCustomer().getErpCustomerPK() + "]" %><br/>
	<%
		try {
			Ticket token = TicketService.getInstance().create( agent.getUserId(), user.getUserId(), ErpServicesProperties.getMasqueradeSecurityTicketExpiration() );
			String url = ErpServicesProperties.getMasqueradeStoreFrontBaseUrl()	+ "980ff88b1adf961750ca413752af6f10/d56b699830e77ba53855679cb1d252da.jsp?"
					+ "agentId=" + agent.getUserId()
					+ "&customerId=" + user.getUserId()
					+ "&loginKey=" + token.getKey();
			response.sendRedirect( url );
			%><%
		} catch ( Exception ex ) {
			ex.printStackTrace();
			%> Sorry, masquerade feature is not available due to some technical error. Please try again later.<br/> Error message: <%= ex.getMessage() %><br/><%
		}
	%>
	<br/>
<% } %>
</crm:GetCurrentAgent>
</body>