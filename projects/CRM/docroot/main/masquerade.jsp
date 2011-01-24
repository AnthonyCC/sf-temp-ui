<%@ taglib uri="crm" prefix="crm" %>

<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.security.ticket.Ticket"%>
<%@ page import="com.freshdirect.security.ticket.TicketService"%>
<%@ page import="com.freshdirect.ErpServicesProperties"%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>

<%-- === Masquerade! === --%>

<head>
	<title>CRM - Masquerade</title>
</head>

<body>

<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
   String agentId = CrmSecurityManager.getUserName(request);
   String agentRole = CrmSecurityManager.getUserRole(request);
%>

<% if ( user == null || agentId == null ) { %>
	Sorry, your session seems to be invalid. Please try to refresh the page, or logout/login.<br/>
<%-- } else if ( !agent.isMasqueradeAllowed() ) { %>
	Sorry, masquerade feature is not allowed for <%= agent.getUserId() --%>.<br/>
<% } else { %>
	You are : <%= agentId  %><br/>
	Customer is : <%= user.getUserId() + " - " + user.getFirstName() + ", " + user.getLastName() + " [" + user.getFDCustomer().getErpCustomerPK() + "]" %><br/>
	<%
		try {
			Ticket token = TicketService.getInstance().create( agentId, user.getUserId(), ErpServicesProperties.getMasqueradeSecurityTicketExpiration() );
			String url = ErpServicesProperties.getMasqueradeStoreFrontBaseUrl()	+ "980ff88b1adf961750ca413752af6f10/d56b699830e77ba53855679cb1d252da.jsp?"
					+ "agentId=" + agentId
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

</body>