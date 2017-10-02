<!DOCTYPE html>
<%@page import='java.util.*' %>
<%@page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@page import="com.freshdirect.framework.mail.EmailAddress"%>
<%@page import="com.freshdirect.fdstore.mail.FDStandingOrderEmail"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerInfo"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="javax.naming.Context"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@page import="com.freshdirect.mail.ejb.MailerGatewayHome"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrdersManager"%>
<%@page import="com.freshdirect.mail.ejb.MailerGatewaySB"%>
<%@page import="com.freshdirect.framework.mail.XMLEmailI"%>
<%@page import="com.freshdirect.fdstore.mail.FDEmailFactory"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDOrderInfoI"%>
<%@page import="com.freshdirect.fdstore.customer.FDOrderI"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.fdstore.lists.FDCustomerProductListLineItem"%>
<%@page import="com.freshdirect.fdstore.lists.FDCustomerListItem"%>
<%@page import="com.freshdirect.fdstore.lists.FDStandingOrderList"%>
<%@page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.FDEcommProperties"%>
<%@page import="com.freshdirect.payment.service.FDECommerceService"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='logic' prefix='logic'%>
<fd:CheckLoginStatus id="fduser" guestAllowed='false' recognizedAllowed='false'  />
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Confirmation Email Send Test page</title>
</head>
<%
	MailerGatewayHome		mailerHome			= null;
	Context ctx = null;
	try {
		ctx = FDStoreProperties.getInitialContext();
		mailerHome = (MailerGatewayHome) ctx.lookup( "freshdirect.mail.MailerGateway" );
	} catch ( NamingException ne ) {
		System.err.println("BANG1");
		ne.printStackTrace();
	} finally {
		try {
			if ( ctx != null ) {
				ctx.close();
			}
		} catch ( NamingException ne ) {
			System.err.println("BANG2");
			ne.printStackTrace();
		}
	}

	FDStandingOrder myso = null;
	String soId = request.getParameter("soId");
	
	boolean emailSent = false;
	if (soId != null && "confirm_email".equals(request.getParameter("action"))) {
		FDStandingOrder so = null;
		for (FDStandingOrder o : FDStandingOrdersManager.getInstance().loadCustomerStandingOrders( fduser.getIdentity() ) ) {
			if (soId.equals(o.getId())) {
				so = o;
				break;
			}
		}
		
		if (so != null) {
			List<FDCartLineI> unavCartItems = new ArrayList<FDCartLineI>();
			
			FDOrderI order = FDCustomerManager.getOrder( request.getParameter("orderId") );
			
			for (FDCartLineI x : order.getOrderLines()) {
				if (request.getParameter(x.getSkuCode() ) != null) {
					unavCartItems.add(x);
				}
			}
			FDCustomerInfo customerInfo = so.getUserInfo();
			customerInfo.getUserInfo(fduser);
			FDStandingOrderEmail mail = (FDStandingOrderEmail) FDEmailFactory.getInstance().createConfirmStandingOrderEmail( customerInfo, order, so, unavCartItems );		
			if (request.getParameter("to") != null && !"".equals( request.getParameter("to") ) ) {
				mail.setRecipient(request.getParameter("to"));
			}
			if (request.getParameter("from") != null && !"".equals( request.getParameter("from") ) ) {
				mail.setFromAddress( new EmailAddress("Vajas Ubul", request.getParameter("from") ) );
			}
			
			if (request.getParameter("html_email") != null && !"".equals( request.getParameter("html_email") ) ) {
				mail.setHtmlEmail( true );
			} else {
				mail.setHtmlEmail( false );
			}

			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MailerGatewaySB)) {
				FDECommerceService.getInstance().enqueueEmail(mail);
			} else {
				MailerGatewaySB mailer = mailerHome.create();
				mailer.enqueueEmail( mail );
			}
			emailSent = true;
		}
	}
%>
<body>
	<div>User: <%= fduser.getUserId() %>; ID: <%= fduser.getIdentity().getErpCustomerPK() %></div>
	<h2>Available Standing Orders</h2>
	<ol>
	<fd:ManageStandingOrders id="solist">
	<logic:iterate id="so" collection="<%= solist %>" type="FDStandingOrder">
	<%
		if (so.getId().equals(soId)) {
			myso = so;
		}
	%>
		<li><a href="?soId=<%= so.getId() %>"><%= so.getCustomerList().getName() %></a> (<a href="/quickshop/so_details.jsp?ccListId=<%= so.getCustomerListId() %>">details</a>)</li>
	</logic:iterate>
	</fd:ManageStandingOrders>
	</ol>
	<% if (myso != null) {
		FDStandingOrderList list = myso.getCustomerList();
	%>
	<h2>Selected: <a href="/quickshop/so_details.jsp?ccListId=<%= myso.getCustomerListId() %>"><%= myso.getCustomerListName() %></a></h2>
	<h3>List Content</h3>
	<ul>
	<logic:iterate id="item" collection="<%= list.getLineItems() %>" type="FDCustomerListItem">
	<%
		FDCustomerProductListLineItem it = (FDCustomerProductListLineItem) item;
	%>
	<li><a href="<%= FDURLUtil.getProductURI(it.getProduct(), "") %>"><%= it.getProduct().getFullName() %></a> (<%= it.getSkuCode() %>)</li>
	</logic:iterate>
	</ul>
	
	<%
	
		final FDOrderInfoI order = myso.getLastOrder();
		final FDCustomerInfo ci = myso.getUserInfo();
		
		if (order != null) {
			String from = null;
			
			if (request.getParameter("from") != null && !"".equals( request.getParameter("from")) ) {
				from = request.getParameter("from");
			}
			else
			{
				String depotCode = ci.getDepotCode();
				from = depotCode != null ? FDDeliveryManager.getInstance().getCustomerServiceEmail(depotCode) : FDStoreProperties.getCustomerServiceEmail();
			}
			
			String to = null;
			if (request.getParameter("to") != null && !"".equals( request.getParameter("to")) ) {
				to = request.getParameter("to");
			}
			else
			{
				to = ci.getEmailAddress();
			}
			
	%>
	<div>
		<a href="/your_account/order_details.jsp?orderId=<%= order.getErpSalesId() %>">Last Order Info</a>
	</div>

	<!-- MAIL SENDER FORM -->

	<h2>Send Confirmation E-mail</h2>

	<form action="?action=confirm_email&soId=<%= myso.getId() %>&orderId=<%= order.getErpSalesId() %>" method="post">
		<fieldset title="Basic Parameters">
			<div>Sender: <input name="from" type="email" value="<%= from %>"></div>
			<div>To: <input name="to" type="email" value="<%= to %>"></div>
			<div><label for="html_mode"><input id="html_mode" name="html_email" type="checkbox" <%= request.getParameter("html_email") != null ? "checked=\"checked\"" : "" %>> HTML mode</label> </div>
		</fieldset>
		<fieldset title="Unavailable Items">
			<logic:iterate id="item" collection="<%= myso.getCustomerList().getLineItems() %>" type="FDCustomerListItem">
			<%
				FDCustomerProductListLineItem it = (FDCustomerProductListLineItem) item;
			%>
			<div>
				<label for="ck_<%= it.getSkuCode() %>"><input id="ck_<%= it.getSkuCode() %>" name="<%= it.getSkuCode() %>" <%= request.getParameter(it.getSkuCode()) != null ? "checked=\"checked\"" : "" %> type="checkbox"> <%= it.getProduct().getFullName() %> (<%= it.getSkuCode() %>)</label>
			</div>
		</logic:iterate>
		</fieldset>

		<button type="submit">Send</button>
	</form>
	<% if (emailSent) { %>
	<div style="color: #00cc00">Confirmation E-mail Sent</div>
	<% } %>
	<%
		} else {
	%><span>This standing order has no order instances yet.</span><%
		}
	%>
	<% } %>
</body>
</html>