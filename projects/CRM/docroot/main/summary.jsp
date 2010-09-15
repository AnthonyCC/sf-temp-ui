<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="template" prefix="tmpl" %>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.crm.CrmCaseModel"%>
<%@ page import="com.freshdirect.crm.CrmCaseTemplate"%>
<%@ page import="com.freshdirect.crm.CrmLateIssueModel"%>
<%@ page import="com.freshdirect.crm.CrmManager"%>
<%@ page import="com.freshdirect.customer.EnumComplaintDlvIssueType"%>
<%@ page import="com.freshdirect.customer.EnumSaleType"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderI"%>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderInfoI"%>
<%@ page import="com.freshdirect.framework.core.PrimaryKey"%>
<%@ page import="com.freshdirect.webapp.crm.util.CustomerSummaryUtil"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpComplaintModel"%>
<%@ page import="com.freshdirect.customer.ErpComplaintReason"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@ page import="com.freshdirect.webapp.crm.util.DeliveryTimeWindowFormatter"%>
<%@ page import="com.freshdirect.fdstore.standingorders.ejb.FDStandingOrdersSessionBean"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.payment.ejb.PaymentManagerSessionBean"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil"%>
<%@ page import="com.freshdirect.customer.adapter.PaymentMethodAdapter"%>
<%@ page import="com.freshdirect.customer.ErpPaymentMethodI"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.common.address.ContactAddressModel"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrdersManager"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerList"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerListInfo"%>
<%@ page import="com.freshdirect.fdstore.lists.FDListManager"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerCreatedList"%>
<%@ page import="com.freshdirect.security.ticket.TicketService"%>
<%@ page import="com.freshdirect.security.ticket.Ticket"%>
<%
String orderId = request.getParameter("orderId");
FDSessionUser user = null;

if (orderId != null) {
	// clear any lingering search terms
	session.removeAttribute(SessionName.LIST_SEARCH_RAW);
	CrmSession.getSessionStatus(session).setSaleId(orderId);
	
	FDOrderI order = CrmSession.getOrder(session, orderId);
	// Remove and replace any existing RECENT_ , RECENT_ORDER_NUMBER in session
	session.removeAttribute(SessionName.RECENT_ORDER);
	session.removeAttribute(SessionName.RECENT_ORDER_NUMBER);
	session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderId);
	
	// Get customer info from the order
    String custId = order.getCustomerId();
	user = (FDSessionUser) session.getAttribute(SessionName.USER);
    if (user == null || user.getIdentity() == null || !custId.equals(user.getIdentity().getErpCustomerPK())) {
		%>
		<fd:LoadUser newIdentity="<%= new FDIdentity(custId) %>" /><%
    	user = (FDSessionUser) session.getAttribute(SessionName.USER);
	}
} else {
	//CrmSession.getSessionStatus(session).setSaleId(null);
	%><crm:GetFDUser id="otherUser"><% user = (FDSessionUser) otherUser; %></crm:GetFDUser><%
}

// helper class
CustomerSummaryUtil util = new CustomerSummaryUtil(request, user);

final Map orderDlvIssueTypes = CrmManager.getInstance().getDeliveryIssueTypes(user.getIdentity().getErpCustomerPK());

// determine page layout template
final String PAGE_TEMPLATE = "print".equalsIgnoreCase(request.getParameter("for")) ? "/template/print.jsp" : "/template/top_nav.jsp";

final List<FDOrderInfoI> recentOrders = util.getRecentOrders(5);
%>
<crm:GetLockedCase id="cm">
</crm:GetLockedCase>

<tmpl:insert template="<%= PAGE_TEMPLATE %>">
    <tmpl:put name='title' direct='true'>Summary Screen</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%-- CONTENT STARTS HERE --%>
<style type="text/css" media="screen">
.sum-panel {
	margin: 5px 5px;
	padding: 5px 5px;
	border: 2px solid black;
	display: inline;
}
</style>

<%--
  ===== FIRST ROW =====
--%>
<table id="contain-er" style="x-width: 100%">
	<tr>
		<td style="width: 200px">
			<%-- NAME AND CONTACT INFO --%>
			<% ErpCustomerInfoModel custInfo = util.getCustomerInfo(); %>
			<h2>Name &amp; Contact Info</h2>
			<table class="cust_module_content_text">
				<tr>
					<td align="right" width="40%" class="cust_module_content_note">Name:&nbsp;&nbsp;</td>
					<td><%=custInfo.getTitle()%> <%=custInfo.getFirstName()%>&nbsp;<%=custInfo.getMiddleName()%>&nbsp;<%=custInfo.getLastName()%></td>
				</tr>
				<tr>
					<td align="right" class="cust_module_content_note">Home #:&nbsp;&nbsp;</td>
					<td><%=custInfo.getHomePhone()!=null?custInfo.getHomePhone().getPhone():""%><%=custInfo.getHomePhone()!=null && custInfo.getHomePhone().getExtension()!=null && !"".equals(custInfo.getHomePhone().getExtension()) ?" <span class=\"cust_module_content_note\">x</span>"+custInfo.getHomePhone().getExtension():""%></td>
				</tr>
				<tr>
					<td align="right" class="cust_module_content_note">Work #:&nbsp;&nbsp;</td>
					<td><%=custInfo.getBusinessPhone()!=null?custInfo.getBusinessPhone().getPhone():""%><%=custInfo.getBusinessPhone()!=null && custInfo.getBusinessPhone().getExtension()!=null && !"".equals(custInfo.getBusinessPhone().getExtension()) ?" <span class=\"cust_module_content_note\">x</span>"+custInfo.getBusinessPhone().getExtension():""%></td>
				</tr>
				<tr>
					<td align="right" class="cust_module_content_note">Cell #:&nbsp;&nbsp;</td>
					<td><%=custInfo.getCellPhone()!=null?custInfo.getCellPhone().getPhone():""%></td>
				</tr>
				<tr>
					<td align="right" class="cust_module_content_note">Alt. Email:&nbsp;&nbsp;</td>
					<td><%=custInfo.getAlternateEmail()%></td>
				</tr>
				<tr>
					<td align="right" class="cust_module_content_note">Dept/Division:&nbsp;&nbsp;</td>
					<td><%=custInfo.getWorkDepartment()%></td>
				</tr>
				<tr>
					<td align="right" class="cust_module_content_note">Employee Id:&nbsp;&nbsp;</td>
					<td><%=custInfo.getEmployeeId()%></td>
				</tr>
			</table>
		</td>

		<!-- LATE DELIVERY INFO -->
		<td style="vertical-align: top;">
<%

FDOrderI ldlv = util.getLastDeliveredOrder();
if (ldlv != null) {
	CrmLateIssueModel recentLateIssue = null;
%>
			<h2>Last Delivery Info</h2>
			<div style="font-size: 120%; padding-bottom: 1em;">Last Delivery: <%= CCFormatter.formatDate(ldlv.getRequestedDate()) %> ORDER # <a href="/main/order_details.jsp?orderId=<%= ldlv.getErpSalesId() %>"><%= ldlv.getErpSalesId() %></a></div>
<%
	recentLateIssue = util.getLateIssueForOrder( ldlv );
	if (recentLateIssue != null) {
%>			<div style="font-size: 120%; padding-bottom: 1em; color: red; font-weight: bold;">Late Report: <%=recentLateIssue.getDelayMinutes()%> minutes, <%=recentLateIssue.getComments()%></div>
<%
	}
	
	ErpComplaintModel comp = util.getTheMostImportantComplaint(ldlv);
	if (comp != null) {
%>			<div>
<%
		for (ErpComplaintReason r : util.getSortedReasons(comp)) {
			%><span style="color: green; padding-right: 1em;"><%= r.getReason() %></span><%
		}
%>			</div>
<%
	}
}


%>
		</td>
	</tr>
</table>

<%-- === Masquerade! === --%>
<crm:GetCurrentAgent id="agent">
<% if ( agent.isMasqueradeAllowed() ) { %>
	<hr/>
		<a href="masquerade.jsp" target="_blank">Masquerade <%= user.getUserId() %></a><br/>
	<hr/>
<% } %>
</crm:GetCurrentAgent>

<%--
  ===== SECOND ROW =====
--%>
<style type="text/css">
.cases-header td {
	padding: 2px 2px;
	font-weight: bold;
	text-align: center;
	color: white;
	background: blue;
	font-size: 90%;
}

.cases-row td {
	padding: 2px 2px;
	border-top: 1px solid lightgray;
	border-bottom: 1px solid darkgray;
	border-right: 1px dotted darkgray;
	background: #eee;
	font-size: 90%;
}

.cases-row-mover td {
	padding: 2px 2px;
	border-top: 1px solid lightgray;
	border-bottom: 1px solid darkgray;
	border-right: 1px dotted darkgray;
	background-color: #7aa;
	color: yellow;
	font-size: 90%;
	x-font-weight: bold;
}

.cases-row-sel td {
	padding: 2px 2px;
	border-top: 1px solid lightgray;
	border-bottom: 1px solid darkgray;
	border-right: 1px dotted darkgray;
	background-color: #006666;
	color: yellow;
	font-size: 90%;
	x-font-weight: bold;
}

.case-row-hdr td {
	color: yellow;
	background-color: #006666;
	font-size: 9pt;
	font-weight: bold;
	padding-top: 1px;
	padding-bottom: 2px; 
}

.case-row td {
	vertical-align: top;
	font-size: 9pt;
	border-bottom: 1px dotted gray;
	border-right: 1px dotted gray;
	padding: 2px 2px;
	background: white;
}

.case-edit-button {
	font-size: 8pt;
	background-color: yellow;
	border: 1px solid black;
}

</style>
<h2>RECENT CASES</h2>
<table cellspacing="0" cellpadding="0">
	<tr>
		<td style="vertical-align: top;">
			<table style="width: 600px;" cellspacing="0" cellpadding="0">
				<tr class="cases-header">
					<td>Created</td>
					<td>Origin</td>
					<td>Queue</td>
					<td>Subject</td>
					<td>Summary</td>
				</tr>
				
<%
// List cases
int k=0;
for (CrmCaseModel aCase : util.getRecentCases(5)) {
%>				<tr id="c-tr-<%= aCase.getId() %>" class="cases-row" caseId="<%= aCase.getId() %>">
					<td><%= CCFormatter.formatDateTime(aCase.getCreateDate()) %></td>
					<td><%= aCase.getOrigin().getName() %></td>
					<td><%= aCase.getSubject().getQueue() %></td>
					<td><%= aCase.getSubjectName() %></td>
					<td><%= aCase.getSummary() %></td>
				</tr>
<%
	k++;
}

%>			</table>	
		</td>
		<td style="width: 20px;">&nbsp;</td>
		<td id="td-selcase" style="vertical-align: top; display: none">
		</td>
	</tr>
</table>
<script type="text/javascript">
var selCase = null;
<%
for (CrmCaseModel aCase : util.getRecentCases(5)) {
%>
$E.on($('c-tr-<%= aCase.getId() %>'), 'mouseover', function(e) {
	this.className = 'cases-row-mover';
});
$E.on($('c-tr-<%= aCase.getId() %>'), 'mouseout', function(e) {
	this.className = this.getAttribute('id') == selCase ? 'cases-row-sel' : 'cases-row';
});
$E.on($('c-tr-<%= aCase.getId() %>'), 'click', function(e) {
	// ajax
	if (selCase != null) {
		$(selCase).className = 'cases-row';
	}
	selCase = this.getAttribute('id');
	
	this.className = 'cases-row-sel';
	
	// async
	var uri = '/main/summary_case_actions.jsp?case='+this.getAttribute("caseId");
	$C.asyncRequest('GET', uri, {
		success: function(resp) {
			$('td-selcase').innerHTML = resp.responseText;
			$('td-selcase').style.display = '';
		}
	});
});
<%
}
%>
</script>

<%--
  ===== THIRD ROW / ORDERS =====
--%>
<style type="text/css">
.orders-header td {
	font-weight: bold;
	text-align: center;
	color: white;
	background: blue;
	font-size: 90%;
}

.orders-row td {
	padding-left: 3px;
	padding-right: 3px;
	border-top: 1px solid lightgray;
	border-bottom: 1px solid darkgray;
	border-right: 1px dotted darkgray;
	background: #eee;
	font-size: 90%;
}

.orders-row-tickbox {
	text-align: center;
	font-weight: bold;
	color: red;
}

</style>
<h2>RECENT ORDERS</h2>
<script type="text/javascript">
var sopanels = [];
</script>
<%
// List cases
for (FDOrderInfoI orderInfo : recentOrders) {
	if (orderInfo.getStandingOrderId() != null) {
		String oid = orderInfo.getStandingOrderId();		
		FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(oid));
		ContactAddressModel addr = FDCustomerManager.getAddress(user.getIdentity(), so.getAddressId());
		ErpPaymentMethodI pm = FDCustomerManager.getPaymentMethod(user.getIdentity(), so.getPaymentMethodId());	
%>
	<div id="soDetailPanel-<%= oid %>"> 
	    <div class="hd"></div> 
	    <div class="bd">
	    	<table cellspacing="0" cellpadding="0" border="0">
	    		<tr>
	    			<td>Standing Order :</td>
	    			<td><%= so == null ? "-null-" : so.getCustomerListName() %></td>
	    		</tr>
	    		<tr>
	    			<td>Address:</td>
	    			<td><%= addr == null ? "-null-" : addr.getScrubbedStreet()+", "+addr.getApartment() %></td>
	    		</tr>
	    		<tr>
	    			<td>Payment Method:</td>
	    			<td><%= pm == null ? "-null-" : pm.getMaskedAccountNumber() %></td>
	    		</tr>
	    	</table>
	    </div> 
	    <div class="ft"></div> 
	</div> 
	<script type="text/javascript">
		//The second argument passed to the
		//constructor is a configuration object:
		sopanels['<%= oid %>'] = new YAHOO.widget.Panel("soDetailPanel-<%= oid %>", {
			width:"400px", 
			fixedcenter: true, 
			constraintoviewport: true, 
			underlay:"shadow", 
			close:true, 
			visible:false, 
			draggable:true} );
		//If we haven't built our panel using existing markup,
		//we can set its content via script:
		sopanels['<%= oid %>'].setHeader("The Panel Control");
		// sopanels['<%= oid %>'].setBody("The Panel is a powerful UI control that enables you to create floating windows without using browser popups.  Effects like drag and drop and constrain-to-viewport are easy to configure.");
		//Although we configured many properties in the
		//constructor, we can configure more properties or 
		//change existing ones after our Panel has been
		//instantiated:
		sopanels['<%= oid %>'].cfg.setProperty("underlay","matte");
	
		sopanels['<%= oid %>'].render(document.body);
	</script>
<%		}
	}
%>
<table style="width: 100%;" cellspacing="0" cellpadding="0">
	<tr class="orders-header">
		<td>Order #</td>
		<td>Delivery</td>
		<td>Route</td>
		<td>Stop</td>
		<td>Window</td>
		<td>Delivery Address</td>
		<td>Status</td>
		<td>Amount</td>
		<td>Method</td>
		<td>Created</td>
		<td>by</td>
<%
	for (EnumComplaintDlvIssueType obj : EnumComplaintDlvIssueType.getEnumList()) {
%>		<td><%= obj.getName() %></td>
<%
	}
%>		<td>Credits<br>Appr./Pend.</td>
	</tr>
<%
// List cases
for (FDOrderInfoI orderInfo : recentOrders) {
	// load order
	FDOrderI order = FDCustomerManager.getOrder(orderInfo.getErpSalesId());
%>
	<tr class="orders-row">
		<td style="font-weight: bold;"><a href="/main/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>"><%= orderInfo.getErpSalesId() %></a></td>
		<td><% if(EnumSaleType.REGULAR.equals(orderInfo.getSaleType())){%><%= CCFormatter.formatDate(orderInfo.getRequestedDate()) %><%} else {%>&nbsp;<%}%></td>
<% if (orderInfo.getTruckNumber() == null) { %>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
<% } else { %>
		<td><%= orderInfo.getTruckNumber() %></td>
		<td><%= orderInfo.getStopSequence() %></td>
<% } %>
		<td style="text-align: center"><%= DeliveryTimeWindowFormatter.formatTime(order.getDeliveryReservation().getStartTime(), order.getDeliveryReservation().getEndTime()) %></td>
		<td style="text-align: left"><%= order.getDeliveryAddress().getAddress1() %></td>
		<td style="text-align: center; font-weight: bold;"><%= orderInfo.getSaleStatus() %></td>
		<td style="text-align: right;  font-weight: bold;"><%= JspMethods.formatPrice(orderInfo.getTotal()) %></td>
		<td style="font-weight: bold;"><%= orderInfo.getPaymentMethodType() %></td>
		<td><%= CCFormatter.formatDateTime(orderInfo.getCreateDate()) %></td>
<%
	if (orderInfo.getStandingOrderId() != null) {
%>		<td id="hs-<%= orderInfo.getStandingOrderId() %>" style="font-weight: bold;"><%= util.getCreatedBy(orderInfo) %></td>
<%
	} else {
%>		<td style="font-weight: bold;"><%= util.getCreatedBy(orderInfo) %></td>
<%
	}

	Set types = (Set) orderDlvIssueTypes.get(order.getErpSalesId());

	for (EnumComplaintDlvIssueType obj : EnumComplaintDlvIssueType.getEnumList()) {
%>		<td class="orders-row-tickbox"><%= types != null && types.contains(obj) ? "x" : "&nbsp;" %></td>
<%
	}
%>		<td style="text-align: center;"><%= JspMethods.formatPrice(orderInfo.getApprovedCreditAmount()) %><%= orderInfo.getPendingCreditAmount() > 0 ? " / "+JspMethods.formatPrice(orderInfo.getPendingCreditAmount()) : "" %></td>
	</tr>
<%
}

%>
</table>
<script type="text/javascript">
<%
// List cases
for (FDOrderInfoI orderInfo : recentOrders) {
	if (orderInfo.getStandingOrderId() != null) {
		String oid = orderInfo.getStandingOrderId();
%>
$E.addListener("hs-<%= oid %>", "mouseover", sopanels['<%= oid %>'].show, sopanels['<%= oid %>'], true); 
$E.addListener("hs-<%= oid %>", "mouseout", sopanels['<%= oid %>'].hide, sopanels['<%= oid %>'], true); 

<%
	}
}
%>
</script>
<%-- CONTENT ENDS HERE --%>
    </tmpl:put>
</tmpl:insert>
