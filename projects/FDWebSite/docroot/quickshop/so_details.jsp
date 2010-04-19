<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrdersManager"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.common.address.ContactAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderInfoI"%>
<%@ page import="com.freshdirect.webapp.crm.util.DeliveryTimeWindowFormatter"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ page import="java.text.DateFormatSymbols"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd'%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false'  />
<fd:ManageStandingOrders id="lists">
<%
	String ccListId = request.getParameter("ccListId");
	String actionName = request.getParameter("fdAction");

	final FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);


	FDStandingOrder so = null;

	// find referred standing order
	for (FDStandingOrder s : lists) {
		if (s.getCustomerListId().equals(ccListId)) {
			so = s;
			break;
		}
	}
	
	if (so != null) {
		ErpAddressModel addr = so.getDeliveryAddress();		
		final String nextDlvDateText = new SimpleDateFormat("EEEE, MMMM d.").format( so.getNextDeliveryDate() );
%>
	<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
		<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Standing Order</tmpl:put>
		<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br/></font>
			<img src="/media_stat/images/template/quickshop/standingorders_catnav.gif" border="0" width="80" height="38">
			<font class="space4pix"><br/></font>
			<% String selectedSoId = so.getId(); %>
			<%@ include file="/quickshop/includes/so_list_nav.jspf"%>
		</tmpl:put>
		<tmpl:put name='content' direct='true'>
		<div id="inner-container" style="width: 100%">
			<div style="padding-top: 1em; overflow: hidden;">
				<div class="title18" style="float: left; width: 100%"><%= so.getCustomerListName() %><div class="text12" style="float: right;">
					<a href="/unsupported.jsp" onclick="FormChangeUtil.checkSignature('qs_cart',false); return CCL.rename_so_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(so.getCustomerListName()))%>', this);">RENAME</a>
				</div></div>
			</div>
			<div style="margin: 1em 0 1em 0; width: 100%; height: 1px; background-color: #996699"></div>
			<% if ( so.isError() ) { %>
				<!-- error display -->
				<div style="text-align: center; font-weight: bold; margin-top: 1em; color: #CC3300;">
					IMPORTANT NOTE: We were not able to schedule a delivery for <%= nextDlvDateText %><br/><br/><%=so.getErrorHeader()%><br/>
				</div>			
				<div style="text-align: center;">
					Click the link below to modify this standing order.<br/><br/>
					<a href="<%= FDURLUtil.getStandingOrderLandingPage(so, "modify") %>">Click here to change the schedule or options for all future deliveries.</a><br/><br/>
				</div>			
			<% } else if ( addr == null ) { %>
				<div style="text-align: left; font-weight: bold;">
					<% String errorText = "You no longer have a delivery address for this standing order."; %>
					<%@ include file="/quickshop/includes/error.jspf" %>
				</div>			
			<% } else { %>
				<!-- last order -->
				<div style="text-align: left; font-weight: bold">
					<%@ include file="/quickshop/includes/so_next_delivery.jspf" %>
				</div>
			<% } %>
			<div style="margin: 1em 0 1em 0; width: 100%; height: 1px; background-color: #996699"></div>
			
			<!-- details -->
			<table id="so-details" style="border: 0; width: 100%">
				<tr>
					<td style="vertical-align: top">
						<div class="title12">Order Details</div>
						<div style="margin-left: 1.5em">
							<div>Delivered every <%= so.getFrequencyDescription() %></div>
							<% if (addr != null) { %>
								<div><%= addr.getScrubbedStreet() %>, <%= addr.getApartment() %></div>
							<% } %>
							<div><%= StandingOrderHelper.getDeliveryDate(so,false) %></div>
						</div>
					</td>
					<td style="vertical-align: top">
						<div class="title12">Need to make changes?</div>
						<ul style="margin: 0 0">
							<li><a href="<%= FDURLUtil.getStandingOrderLandingPage(so, "modify") %>">Change the schedule or options for all future deliveries.</a></li>
							<% if ( !so.isError() ) { %><li><a href="/unsupported.jsp" onclick="CCL.shift_so_delivery('<%= so.getId() %>', '<%= nextDlvDateText %>', this); return false;">Skip an upcoming delivery ...</a></li><% } %>
							<% if ( !so.isError() ) { %><li><a href="/unsupported.jsp" onclick="CCL.change_so_frequency('<%= so.getId() %>', <%= so.getFrequency() %>, '<%= nextDlvDateText %>', this); return false;">Change the frequency of deliveries.</a></li><% } %>
							<li><a href="/unsupported.jsp" onclick="CCL.delete_so('<%= so.getId() %>', this); return false;">Delete this standing order.</a></li>
						</ul>
					</td>
				</tr>
			</table>
			
			<div style="margin: 1em 0 1em 0; width: 100%; height: 1px; background-color: #996699"></div>
			<div class="text16" style="font-weight: bold; color: #996699">
				You can change the items in <% if ( so.isError() ) { %> future deliveries <% } else { %>all deliveries starting <%= nextDlvDateText %> <% } %>by editing this shopping list. <a href="/media_stat/editorial/site_pages/standing_orders/so_help_checkout.html" target="_blank" onClick="popup('/media_stat/editorial/site_pages/standing_orders/so_help_checkout.html','large'); return false;">Learn more.</a>
			</div>
			
			<%--LIST OF STANDING ORDER ITEMS --%>
			<fd:QuickShopController id="quickCart" soListId="<%= so.getCustomerListId() %>" action="<%= actionName %>">
				<%
					final String qsPage = "so_details.jsp";
					final String qsDeptId = null;
					final boolean hasDeptId = false;
					final String orderId = null;				
				%>
				<%@ include file="/shared/quickshop/includes/i_vieworder.jspf"%>
			</fd:QuickShopController>
		</div>
		</tmpl:put>
	</tmpl:insert>
<%
	} else {
%>
	<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
		<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Standing Order</tmpl:put>
		<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br/></font>
			<img src="/media_stat/images/template/quickshop/standingorders_catnav.gif" border="0" width="80" height="38">
			<font class="space4pix"><br/></font>
			<% String selectedSoId = ""; %>
			<%@ include file="/quickshop/includes/so_list_nav.jspf"%>
		</tmpl:put>
		<tmpl:put name='content' direct='true'>
		<div id="inner-container" style="width: 100%">
			<div style="padding-top: 1em; overflow: hidden;">
				<div class="title16">This standing order is deleted.</div>
			</div>
		</div>
		</tmpl:put>
	</tmpl:insert>
<%
	}
%>
</fd:ManageStandingOrders>
