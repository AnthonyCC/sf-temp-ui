<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDInvoiceAdapter"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<% boolean isGuest = false; %>
	<crm:GetCurrentAgent id="currentAgent">
		<% isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); %> 
	</crm:GetCurrentAgent>
<%
    boolean forPrint = "print".equalsIgnoreCase(request.getParameter("for"));
    String tmpl = "/template/" + (forPrint && !isGuest ? "print" : "top_nav") + ".jsp";
    String orderId = request.getParameter("orderId");
%>
<tmpl:insert template='<%= tmpl %>'>

	<tmpl:put name='title' direct='true'>Order <%= orderId%> Details</tmpl:put>
<%
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
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    if (user == null || user.getIdentity() == null || !custId.equals(user.getIdentity().getErpCustomerPK())) {%>
        <fd:LoadUser newIdentity="<%= new FDIdentity(custId) %>" />
        <%user = (FDSessionUser) session.getAttribute(SessionName.USER); %> 
<%  }
    // WHEN RESUBMITTING, WE JUST WANT TO RELOAD THE ORDER DETAILS PAGE, SO EDIT SUCCESSPAGE ATTRIBUTE OF MODIFY TAG
    String successPage = ("resubmit".equalsIgnoreCase(request.getParameter("action"))? "/main/order_details.jsp?orderId=" : "/main/order_modify.jsp?orderId=") + orderId;
    // Get DELIVERY ADDRESS info, PAYMENT info, APPLIED CREDIT info
    ErpAddressModel dlvAddress = order.getDeliveryAddress();
    ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
%>

<tmpl:put name='content' direct='true'>

<% if (!forPrint) { %>
	<%@ include file="/includes/order_nav.jspf"%>
<% } else if (!isGuest) { %>
	<crm:GetErpCustomer id="customer" user="<%= user %>">
		<% ErpCustomerInfoModel custInfo = customer.getCustomerInfo(); %>
		<div class="content_fixed">
            Customer: <b><%= custInfo.getFirstName() %> <%= custInfo.getMiddleName() %> <%= custInfo.getLastName() %></b> &middot; 
            ID: <b><%= custId %></b> &middot; 
            Email: <%= custInfo.getEmail() %>
        </div>
	</crm:GetErpCustomer>
<% } %> 

<fd:ComplaintGrabber order="<%= order %>" complaints="complaints" lineComplaints="lineComplaints" deptComplaints="deptComplaints" miscComplaints="miscComplaints" fullComplaints="fullComplaints" restockComplaints="restockComplaints" retrieveApproved="true" retrievePending="false" retrieveRejected="false">

<%	boolean isNotSubmitted = EnumSaleStatus.NOT_SUBMITTED.equals( order.getOrderStatus() ); %>
<% boolean resubmitted = "resubmitted".equalsIgnoreCase(request.getParameter("status")); %>
<% if (isNotSubmitted) { %>
	<div class="content_fixed">
		<% if (resubmitted) {%>
			<span class="correct"><b>&raquo; Order resubmitted</b></span>, please wait a few minutes for the update to take place <a href="javascript:pop('/main/order_not_submitted.jsp?orderId=<%= orderId %>&scroll=yes','600','800');" class="error_detail">(Error Details)</a>.
		<% } else { %>	
			<span class="error">&raquo; Order not submitted <a href="javascript:pop('/main/order_not_submitted.jsp?orderId=<%= orderId %>&scroll=yes','600','800');" class="error">What went wrong?</a></span>
		<% } %>
	</div>
<% } %>

<%@ include file="/includes/order_summary.jspf"%>

<%	boolean showPaymentButtons = false;
	boolean showAddressButtons = false;
	boolean showDeleteButtons = false;
    boolean displayDeliveryInfo = true;
    if(EnumSaleType.SUBSCRIPTION.equals(order.getOrderType()))
            displayDeliveryInfo = false;
%>
<div class="content_<%=forPrint?"fixed":"scroll"%>" style="height:72%;">
	<%@ include file="/includes/i_order_dlv_payment.jspf"%>
	
	<%	boolean showEditOrderButtons = false;
		boolean showFeesSection = true;
		boolean cartMode = false; %>
	
	<%@ include file="/includes/i_cart_details.jspf"%>
	
	<%-- ^^^^^^^^^^^^^^^^^^^^^^^ BEGIN ORDER NOTES SECTION ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ --%>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" class="list_header">
		<tr>
			<td class="list_header_detail" style="padding: 2px; padding-left: 6px;">NOTES ON THIS ORDER</td>
		</tr>
	</table>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center" class="order_detail">
		<tr valign="top">
			<td width="3%">&nbsp;</td>
			<td width="97%">
				<logic:iterate id="complaint" collection="<%= order.getComplaints() %>" type="com.freshdirect.customer.ErpComplaintModel"> 
	<%	if ( complaint.getDescription() != null && !"".equals( complaint.getDescription().trim() ) ) { %>
					<b><%= CCFormatter.formatDeliveryDate( complaint.getCreateDate() ) %></b> by <b><%= complaint.getCreatedBy() %></b>: <%= complaint.getDescription() %><BR>
	<%	} %>
				</logic:iterate>
    <%if(EnumSaleStatus.CANCELED.equals(order.getOrderStatus())){%>
        <fd:AccountActivity activities="activities" activityType="<%=EnumAccountActivityType.CANCEL_ORDER%>">
            <logic:iterate id="activity" collection="<%= activities %>" type="com.freshdirect.customer.ErpActivityRecord">
                <%if(activity.getNote() != null && activity.getNote().indexOf(orderId) != -1 ) { %>
                    <b><%= CCFormatter.formatDate(activity.getDate()) %></b> by <b><%= activity.getInitiator() %></b>: <%= activity.getActivityType().getName() %>&nbsp; <%= (activity.getNote() != null && !"".equals(activity.getNote())) ? "-" : "" %>&nbsp;&nbsp;<%= activity.getNote() %><br>
                <% } %>
            </logic:iterate>
        </fd:AccountActivity>
    <%}%>
			</td>
		</tr>
	</table><BR>
	
	<%-- ^^^^^^^^^^^^^^^^^^^^^^^ END ORDER NOTES SECTION ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ --%>
	</div>
</fd:ComplaintGrabber>

</tmpl:put>

</tmpl:insert>