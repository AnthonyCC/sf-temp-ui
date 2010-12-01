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
			session.setAttribute("__actual_so",so);
			break;
		}
	}
	
	if (so != null) {
		ErpAddressModel addr = so.getDeliveryAddress();		
		final String nextDlvDateText = FDStandingOrder.DATE_FORMATTER.format( so.getNextDeliveryDate() );
		final String nextDlvDateTextShort = FDStandingOrder.DATE_FORMATTER_SHORT.format( so.getNextDeliveryDate() );
%>
	<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
		<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Standing Order</tmpl:put>
	    <tmpl:put name='extrahead' direct='true'>
			<link href="/assets/css/fix.css" rel="stylesheet" type="text/css" />
	    </tmpl:put>
		<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br/></font>
			<a href="/quickshop/standing_orders.jsp" ><img src="/media_stat/images/template/quickshop/standingorders_catnav.gif" border="0" width="80" height="38"></a>
			<font class="space4pix"><br/></font>
			<% String selectedSoId = so.getId(); %>
			<%@ include file="/quickshop/includes/so_list_nav.jspf"%>
		</tmpl:put>
		<tmpl:put name='content' direct='true'>
		<div id="inner-container" style="width: 100%">
			<div style="padding-top: 1em; overflow: hidden;">
				<div class="title17" style="float: left; width: 100%">
					<span style="float: left;"><%= so.getCustomerListName() %></span>
					<div class="title11" style="float: right;">
						<a href="/unsupported.jsp" onclick="FormChangeUtil.checkSignature('qs_cart',false); return CCL.rename_so_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(so.getCustomerListName()))%>', this);">RENAME</a>
					</div>
				</div>
			</div>
			<hr style="margin-bottom: 10px; width: 100%; height: 1px; background-color: #996699; color: #996699; line-height: 1px; border: none;"/>	
			<% if ( so.isError() ) { %>
				<!-- error display -->
				<div class="text12" style="text-align: center; font-weight: bold; color: #CC3300;">
					IMPORTANT NOTE: <br/>We were not able to schedule a delivery for <%= nextDlvDateText %><br/><br/><%=so.getErrorHeader()%><br/>
				</div>			
				<div style="text-align: center;">
					<%=so.getErrorDetail()%><br/><br/>
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
					<%@ include file="/quickshop/includes/so_next_delivery_alt.jspf" %>
				</div>
			<% } %>
			
			<div style="background-color: #996699; color: white; padding: 3px 5px; overflow: hidden; margin-bottom: 15px; margin-top: 15px;">
				<span class="title17" style="float: left;">EDIT THIS STANDING ORDER</span>
				<a class="text13" style="float: right; text-align: right; color: white; vertical-align: middle;" href="/media/editorial/site_pages/standing_orders/so_help_checkout.html" target="_blank" onClick="popup('/media/editorial/site_pages/standing_orders/so_help_checkout.html','large'); return false;">Help/FAQs</a>
			</div>	
						
			<!-- details -->
			
			<div class="title17" style="color: #996699; margin-bottom: 5px;">1. DELIVERY FREQUENCY &amp; OPTIONS</div>
			
			<div class="title13" style="margin-bottom: 25px;">
				Make one-time or permanent changes to the delivery of your order.
				Changes will affect deliveries of this order starting <span style="color: #CC3300;"><%=nextDlvDateTextShort%></span>.
			</div>
			
			<div class="text13" style="margin-bottom: 25px; padding-left: 15px;">
				<ul style="margin: 0; list-style: disc; padding-left: 0px;">
					<li><a href="<%= FDURLUtil.getStandingOrderLandingPage(so, "modify") %>"><b>Resubmit this standing order to make permanent changes</b></a></li>
					<% if ( !so.isError() ) { %><li><a href="/unsupported.jsp" onclick="CCL.shift_so_delivery('<%= so.getId() %>', '<%= nextDlvDateText %>', this); return false;">Skip upcoming delivery</a></li><% } %>
					<% if ( !so.isError() ) { %><li><a href="/unsupported.jsp" onclick="CCL.change_so_frequency('<%= so.getId() %>', <%= so.getFrequency() %>, '<%= nextDlvDateText %>', this); return false;">Adjust delivery frequency</a></li><% } %>
					<li><a href="/unsupported.jsp" onclick="CCL.delete_so('<%= so.getId() %>', this); return false;">Delete this standing order</a></li>
				</ul>
			</div>
			
			<div class="text13" style="margin-bottom: 25px;">
				<div class="title13">Your Current Settings:</div>
				<table>
					<tr><td class="text13" style="padding-right: 10px;">Frequency:</td><td class="text13">Delivered <%= so.getFrequencyDescription() %></td></tr>
					<tr><td class="text13" style="padding-right: 10px;">Delivery Address:</td><td class="text13"><%if(addr!=null){%><%= addr.getScrubbedStreet() %>, <%= addr.getApartment() %><%}%></td></tr>
					<tr><td class="text13" style="padding-right: 10px;">Delivery Timeslot:</td><td class="text13"><%= StandingOrderHelper.getDeliveryDate(so,false) %></td></tr>
				</table>	
			</div>
			
			<hr style="margin-bottom: 10px; width: 100%; height: 1px; background-color: #996699; color: #996699; line-height: 1px; border: none;"/>	
			
			<div class="title17" style="color: #996699; margin-bottom: 5px;">2. STANDING ORDER CONTENTS</div>
			
			<div class="text13" style="margin-bottom: 15px;">
				<b>To change quantities:</b> Changes to Quantity boxes only take effect if you immediately resubmit your order. Use the &quot;Modify&quot; link instead to make changes for future orders.
			</div>
			
			<div class="text13" style="margin-bottom: 25px;">
				<b>To add products:</b> Shop as usual and click &quot;Save to Shopping List&quot; on any product page. Then choose the list for this standing order. <br/><a href="/index.jsp">Click here to shop now.</a>
			</div>
						
			<%--LIST OF STANDING ORDER ITEMS --%>
			<fd:QuickShopController id="quickCart" soListId="<%= so.getCustomerListId() %>" action="<%= actionName %>">
				<%
					final String qsPage = "so_details.jsp";
					final String qsDeptId = null;
					final boolean hasDeptId = false;
					final String orderId = null;			
					final boolean isStandingOrderPage = true;
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
	    <tmpl:put name='extrahead' direct='true'>
			<link href="/assets/css/fix.css" rel="stylesheet" type="text/css" />
	    </tmpl:put>
		<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br/></font>
			<a href="/quickshop/standing_orders.jsp" ><img src="/media_stat/images/template/quickshop/standingorders_catnav.gif" border="0" width="80" height="38"></a>
			<font class="space4pix"><br/></font>
			<% String selectedSoId = ""; %>
			<%@ include file="/quickshop/includes/so_list_nav.jspf"%>
		</tmpl:put>
		<tmpl:put name='content' direct='true'>
		<div id="inner-container" style="width: 100%">
			<div style="padding-top: 1em; overflow: hidden;">
				<div class="title15">This standing order is deleted.</div>
			</div>
		</div>
		</tmpl:put>
	</tmpl:insert>
<%
	}
%>
</fd:ManageStandingOrders>
