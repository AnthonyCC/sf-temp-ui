<%@page import="com.freshdirect.customer.EnumChargeType"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartModel"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartonDetail"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartonInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.crm.CrmMasqueradeUtil"%>
<%@page import="com.freshdirect.security.ticket.MasqueradeParams"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@page import="com.freshdirect.fdstore.customer.FDOrderI"%>
<%@page import="com.freshdirect.security.ticket.MasqueradePurposeBuilder"%>
<%@page import="com.freshdirect.common.context.MasqueradeContext"%>
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

	final MasqueradeParams params = new MasqueradeParams(request);

	final String customerId = params.userId;
	
	out.print( "loginKey = " + loginKey + "<br/>" );
	out.print( "agentId = " + agentId + "<br/>" );
	out.print( "customerId = " + params.userId + "<br/>" );
	out.print( "forceOrderAvailable = " + params.forceOrderAvailable + "<br/>" );
	out.print( "makeGoodFromOrderId = " + params.makeGoodFromOrderId + "<br/>" );
	out.print( "autoApproveAuthorized = " + params.autoApproveAuthorized + "<br/>" );
	out.print( "autoApprovalLimit = " + params.autoApprovalLimit + "<br/>" );
	out.print( "shopFromOrderId = " + params.shopFromOrderId + "<br/>" );
	out.print( "modifyOrderId = " + params.modifyOrderId + "<br/>" );
	out.print( "parentOrderId = " + params.parentOrderId + "<br/>" );

	try {
		
		Ticket ticket = TicketService.getInstance().use( loginKey, agentId, 
			MasqueradePurposeBuilder.buildPurpose(params)
		);
		
	} catch ( IllegalArgumentException ex ) {
		LOGGER.error(ex);
		out.print("Missing arguments.<br/>");
		return;
	} catch ( NoSuchTicketException ex ) {
		LOGGER.error(ex);
		out.print("Ticket does not exist.<br/>");
		return;
	} catch ( TicketExpiredException ex ) {
		LOGGER.error(ex);
		out.print("Ticket is expired.<br/>");
		return;
	} catch ( IllegalTicketUseException ex ) {
		LOGGER.error(ex);
		out.print("Ticket is invalid.<br/>");
		return;
	} catch ( FDResourceException ex ) {
		LOGGER.error(ex);
		out.print("Validating ticket failed due to FDResourceException.<br/>");
		return;
	} 
	
	out.print("Ticket is valid. Logging in.<br/>");
	
	TestSupport s = TestSupport.getInstance();
	
	String erp_id = s.getErpIDForUserID(customerId);
	String cust_id = s.getFDCustomerIDForErpId(erp_id);
	FDIdentity identity = new FDIdentity(erp_id, cust_id);	
	
	if ( erp_id == null || cust_id == null || identity == null ) {
		out.print("Customer not found.");	
	}
	FDUser loginUser =null;


	try {
		
    	// masquerade
    	MasqueradeContext ctx = CrmMasqueradeUtil.build(identity, params, agentId);

    	// recognize user and set masquerade mode here
    	loginUser = FDCustomerManager.recognize(identity, ctx);
    	UserUtil.createSessionUser(request, response, loginUser);
    	
		CrmMasqueradeUtil.postInit(ctx, session);    	
    	
	} catch ( FDAuthenticationException ex ) {
		LOGGER.error(ex);
		out.print("Authentication failed.");
		return;	
	} catch ( FDResourceException ex ) {
		LOGGER.error(ex);
		out.print("Login failed due to FDResourceException.");
		return;
	}
	
	// Log Masquerade
	LOGGER.info("session ID: " + session.getId());
	LOGGER.info("masquerade agent: " + agentId);
	LOGGER.info("customer ID: " + customerId);
	
	try {
		FDActionInfo.setMasqueradeAgentTL(agentId);
		FDActionInfo ai = new FDActionInfo( loginUser.getUserContext().getStoreContext().getEStoreId(), EnumTransactionSource.WEBSITE, identity, "Masquerade login", agentId + " logged in as " + customerId, null, EnumAccountActivityType.MASQUERADE_LOGIN, loginUser.getPrimaryKey());
		ActivityLog.getInstance().logActivity( ai.createActivity() );
	} catch ( FDResourceException ex ) {
		LOGGER.error(ex);
		ex.printStackTrace();
		out.print( "Activity logging failed due to FDResourceException. <br/>" );
	}
	
	out.print("Logged in. Redirecting to StoreFront.<br/>");

	// make redirection based on params
	String redirectUri = CrmMasqueradeUtil.getRedirectionUri(request, loginUser, params);
	
	if(!loginUser.getTcAcknowledge()){
		session.setAttribute("nextSuccesspage", redirectUri );
		session.setAttribute("fdTcAgree", false);
	}
	
	response.sendRedirect(redirectUri);
%>