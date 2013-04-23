
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.customer.EnumAccountActivityType"%>
<%@page import="com.freshdirect.customer.ActivityLog"%>
<%@page import="com.freshdirect.crm.CrmAgentModel"%>
<%@page import="com.freshdirect.customer.EnumTransactionSource"%>
<%@page import="com.freshdirect.fdstore.customer.FDActionInfo"%>
<%@ page import="com.freshdirect.customer.ErpActivityRecord"%>
<%@ page import="com.freshdirect.fdstore.FDResourceException"%>
<%@ page import="com.freshdirect.fdstore.customer.FDAuthenticationException"%>
<%@ page import="com.freshdirect.security.ticket.TicketExpiredException"%>
<%@ page import="com.freshdirect.security.ticket.IllegalTicketUseException"%>
<%@ page import="com.freshdirect.security.ticket.NoSuchTicketException"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUser"%>
<%@ page import="com.freshdirect.test.TestSupport"%>
<%@ page import="com.freshdirect.security.ticket.Ticket"%>
<%@ page import="com.freshdirect.security.ticket.TicketService"%>

<%-- The filename is deliberately confuscated--%>

<%
	session.invalidate();
	session = request.getSession(true);
	final Logger LOGGER = LoggerFactory.getInstance("masquerade.jsp");
	//session.invalidate();
	//session = request.getSession(true);
	String loginKey = request.getParameter("loginKey");
	String agentId = request.getParameter("agentId");
	String customerId = request.getParameter("customerId");
	
	out.print( "loginKey = " + loginKey + "<br/>" );
	out.print( "agentId = " + agentId + "<br/>" );
	out.print( "customerId = " + customerId + "<br/>" );
	
	try {
		
		Ticket ticket = TicketService.getInstance().use( loginKey, agentId, customerId );
		
	} catch ( IllegalArgumentException ex ) {
		out.print("Missing arguments.<br/>");
		return;
	} catch ( NoSuchTicketException ex ) {
		out.print("Ticket does not exist.<br/>");
		return;
	} catch ( TicketExpiredException ex ) {
		out.print("Ticket is expired.<br/>");
		return;
	} catch ( IllegalTicketUseException ex ) {
		out.print("Ticket is invalid.<br/>");
		return;
	} catch ( FDResourceException ex ) {
		out.print("Validating ticket failed due to FDResourceException.<br/>");
		return;
	}
	
	out.print("Ticket is valid. Logging in.<br/>");
	
	TestSupport s = TestSupport.getInstance();
	
	String erp_id = s.getErpIDForUserID(customerId);;
	String cust_id = s.getFDCustomerIDForErpId(erp_id);
	FDIdentity identity = new FDIdentity(erp_id, cust_id);	
	
	if ( erp_id == null || cust_id == null || identity == null ) {
		out.print("Customer not found.");	
	}
	FDUser loginUser =null;
	try {
		
		loginUser = FDCustomerManager.recognize( identity );
    	// masquerade
    	loginUser.setMasqueradeAgent(agentId);

    	UserUtil.createSessionUser(request, response, loginUser);
	} catch ( FDAuthenticationException ex ) {
		out.print("Authentication failed.");
		return;	
	} catch ( FDResourceException ex ) {
		out.print("Login failed due to FDResourceException.");
		return;
	}
	
	// Masquerade
	LOGGER.info("session ID: " + session.getId());
	LOGGER.info("masquerade agent: " + agentId);
	LOGGER.info("customer ID: " + customerId);
	
	try {
		FDActionInfo ai = new FDActionInfo( EnumTransactionSource.WEBSITE, identity, "Masquerade login", agentId + " logged in as " + customerId, null, EnumAccountActivityType.MASQUERADE_LOGIN, loginUser.getPrimaryKey());
		ActivityLog.getInstance().logActivity( ai.createActivity() );
	} catch ( FDResourceException ex ) {
		ex.printStackTrace();
		out.print( "Activity logging failed due to FDResourceException. <br/>" );
	}
	
	out.print("Logged in. Redirecting to StoreFront.<br/>");
	
	response.sendRedirect("/index.jsp");
	
%>