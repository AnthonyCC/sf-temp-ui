<%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrdersManager"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.common.address.ContactAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpPaymentMethodI"%>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderInfoI"%>
<%@ page import="com.freshdirect.webapp.crm.util.DeliveryTimeWindowFormatter"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ page import="com.freshdirect.webapp.util.OrderPermissionsImpl"%>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderHistory" %>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderInfoI"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.text.DateFormatSymbols"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='logic' prefix='logic' %>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false'  />

<fd:ManageStandingOrders id="lists">
<%	
	request.setAttribute("__yui_load_calendar__", Boolean.TRUE);

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
	
	request.setAttribute("isQuickShopCartVisible", Boolean.FALSE);
	if (so != null) {
		
		//find so instances
		List<FDOrderInfoI> instances = new ArrayList<FDOrderInfoI>();
		if(user!=null){
			FDOrderHistory history = (FDOrderHistory) FDCustomerManager.getOrderHistoryInfo(user.getIdentity());
			instances = (List<FDOrderInfoI>) history.getStandingOrderInstances(so.getId());
		}
		
    	boolean holidayMovement = false; 
    	for(FDOrderInfoI soi: instances) { 
    		if (!holidayMovement && soi.isSoHolidayMovement()) {
    			holidayMovement=true;
    		}
    	}
		
		ErpAddressModel addr = so.getDeliveryAddress();		
		final String nextDlvDateText = FDStandingOrder.DATE_FORMATTER.format( so.getNextDeliveryDate() );
		final String nextDlvDateTextShort = FDStandingOrder.DATE_FORMATTER_SHORT.format( so.getNextDeliveryDate() );
		final String nextDlvDateTextLong = FDStandingOrder.DATE_FORMATTER_LONG.format( so.getNextDeliveryDate() );
		
		final ErpPaymentMethodI paymentMethod = so.getPaymentMethod();
%>
	
	<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
		<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Standing Order</tmpl:put>
	    <tmpl:put name='extrahead' direct='true'>
			<fd:css href="/assets/css/standingorder.css"/>
			<fd:javascript src="/assets/javascript/standingorder.js"/>
			<fd:css href="/assets/css/fix.css"/>
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
				<div class="title17" style="float: left; width: 100%; margin-bottom:15px; margin-top:30px;">
					<%String soName = StringUtil.escapeHTML(so.getCustomerListName()); %>
					<script type="text/javascript">var soNameJs = '<%=StringUtil.escapeJavaScript(so.getCustomerListName())%>';</script>
					<span style="float: left;"><%=soName%></span>
					<div class="title11" style="float: right;">
						<a href="/unsupported.jsp" onclick="CCL.delete_so({soId:'<%= so.getId() %>', soName: soNameJs}, this); return false;">Delete</a> |
						<a href="/unsupported.jsp" onclick="FormChangeUtil.checkSignature('qs_cart',false); return CCL.rename_so_list(soNameJs, this);">Rename</a>
					</div>
				</div>
			</div>
			
			<script type="text/javascript">setSoId(<%= so.getId() %>);</script>
			
			<% if(instances.size()>1) { %>
			   	<% String errorTitle = "The number of deliveries this week exceeds the frequency you have chosen for this Standing Order.";
            	String errorText = "If you are only expecting one of these deliveries, please view the order details and cancel the appropriate delivery."; %>
            	<%@ include file="/includes/i_error_with_title.jspf" %>
			<%	}%>
			
			<% String soCsEmail = FDStoreProperties.getStandingOrderCsEmail();
			if(holidayMovement) { %>
            	<% String errorTitle = "Your delivery this week has been moved.";
            	String errorText = "FreshDirect is closed on your Standing Order delivery day this week. We have moved your delivery to the next available day and kept your chosen timeslot. Click \"View/Modify\" below to make any necessary changes. <span style=\"font-weight: bold\">Questions or concerns about this change?</span> Please contact your FreshDirect At The Office hospitality team at <a href=\"mailto:"+soCsEmail+"\">"+soCsEmail+"</a>."; %>
            	<%@ include file="/includes/i_error_with_title.jspf" %>
			<%} %>
			<% if ( so.isError() && (
					so.getLastError() != ErrorCode.NO_ADDRESS || 
					so.getLastError() == ErrorCode.NO_ADDRESS && addr != null )) { %>
				<!-- old error display -->
				<div class="text12" style="text-align: center; font-weight: bold; color: #CC3300;">
					IMPORTANT NOTE: <br/>We were not able to schedule a delivery for <%= nextDlvDateText %><br/><br/><%=so.getErrorHeader()%><br/>
				</div>			
				<div style="text-align: center;">
					<%=so.getErrorDetail()%><br/><br/>
					<a href="<%= FDURLUtil.getStandingOrderLandingPage(so, "modify") %>">Click here to change the schedule or options for all future deliveries.</a><br/><br/>
				</div>
			<% } %>
			<% if ( addr == null ) { %>
				<% String errorText = "Your Standing Order can not be processed or delivered without a Delivery Address. Please open your Standing Order in the \"Change Standing Order Global Settings\" " +
					"section below to add a Delivery Address. (Note: changing your Delivery Address may affect timeslot availability.)<br />" +
					"Need help? Contact your FreshDirect At The Office hospitality team at <a href=\"mailto:"+soCsEmail+"\">"+soCsEmail+"</a>.";
				String errorTitle = "This Standing Order no longer has a Delivery Address associated with it."; %>
				<%@ include file="/includes/i_error_with_title.jspf" %>
			<% } %>
			<% if( paymentMethod == null ) {%>
				<% String errorText = "Your Standing Order can not be processed or delivered without a Payment Option. Please open your Standing Order in the \"Change Standing Order Global Settings\" " +
					"section below to add a Payment Option. Need help? Contact your FreshDirect At The Office hospitality team at <a href=\"mailto:"+soCsEmail+"\">"+soCsEmail+"</a>.";
				String errorTitle = "This Standing Order no longer has a Payment Option associated with it."; %>
				<%@ include file="/includes/i_error_with_title.jspf" %>
			<% } %>	
			<% if( request.getParameter("tmpl_saved") != null ) { /* [APPDEV-2149] SO template has been successfully saved */ %>
				<div style="border: 1px solid #969; padding: 5px 5px 5px 5px; border-radius: 5px 5px; margin-bottom: 18px; color: #969" class="text12">
					<div style="font-weight: bold;">Standing Order Successfully Saved</div>
					<div class="text10">Your Standing Order has been modified successfully. Please note that next delivery date is adjusted according to the changed delivery date and frequency.</div>
				</div>
			<% } %>	
					
			<fd:GetStandingOrderHelpInfo id="helpSoInfo" so="<%=so%>">
				<script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script>
				<!-- orders list -->
				<table width="100%" bgcolor="#333333" style="margin-bottom: 18px;">
					<tr><td><img src="/media_stat/images/template/quickshop/modify_next_delivery.png" width="192" height="22"></td>
					<td>
					<a class="text13" style="float: right; text-align: right; color: white; vertical-align: middle; padding-right:7px;" href="/unsupported.jsp" onclick="return CCL.help_so(helpSoInfo, this);">Need Help?</a>
					</td></tr>
				</table>		
				<div style="margin: 0.5em 0.5em 1.5em">
					The next delivery of this Standing Order is displayed below. If your delivery is due within the next 7 days, you may make changes by clicking "View/Modify". <b>Changes in this section will affect only this delivery</b>.
				</div>
				
				<div id="so_orders" style="text-align: left; font-weight: bold; padding: 3px 5px; overflow: hidden; margin-bottom: 15px; margin-top: 15px;">
					<%@ include file="/quickshop/includes/so_orders.jspf" %>
				</div>
				
				<table width="100%" bgcolor="#333333" style="margin-bottom: 18px;">
					<tr><td><img src="/media_stat/images/template/quickshop/change_so_global_settings.png" width="229" height="22"></td>
					<td>
					<a class="text13" style="float: right; text-align: right; color: white; vertical-align: middle; padding-right:7px;" href="/unsupported.jsp" onclick="return CCL.help_so(helpSoInfo, this);">Need Help?</a>
					</td></tr>
				</table>
			</fd:GetStandingOrderHelpInfo>
			
			
			<fd:QuickShopController id="quickCart" soListId="<%= so.getCustomerListId() %>" action="<%= actionName %>">
			<table style="padding: 0.5em;">
				<tr>
					<td valign="top">
						Here are the current settings of your Standing Order. <b>To make permanent, recurring 
						changes</b>, you must open your Standing Order, make your changes and resubmit it via 
						checkout.<br /><br />
						Once your Standing Order is reopened, you may add or remove items from your cart, adjust 
						your delivery frequency or timeslot, and change your delivery address and payment options. 
						<b>When finished, complete checkout to confirm your changes.</b>
						
						<div class="text13" style="margin-bottom: 20px; padding-top:20px;">
							<%
							boolean modInstanceToo = false; // modify only the template or the belonging order too
							if (instances.size()>0) {
								FDOrderInfoI latestSoi = instances.get(0);%>
								<fd:OrderPermissionsTag id='orderPermissions' orderId='<%= latestSoi.getErpSalesId()%>'>
									<%if(orderPermissions.allowModifyOrder()) {
										modInstanceToo = true;
										Date requestedDate = latestSoi.getRequestedDate();%>
										<script type="text/javascript">
										var modifySoInfo = {
											msotNoMsoiUrl : '<%= StringEscapeUtils.unescapeHtml(FDURLUtil.getStandingOrderLandingPage(so, "modify")) %>',
											msotMsoiUrl: '<%= StringEscapeUtils.unescapeHtml(FDURLUtil.getStandingOrderLandingPage(so, "modify", latestSoi.getErpSalesId())) %>',
											soiDate:'<%= new SimpleDateFormat("M/d/y").format(requestedDate)%>',
											soiDay:'<%= new SimpleDateFormat("EEEE").format(requestedDate)%>',
											soName: soNameJs
										}
										</script>
										<a href="/unsupported.jsp" onclick="CCL.modify_so(modifySoInfo, this); return false;">
											<img style="margin-bottom: 1em" src="/media_stat/images/template/quickshop/update_standing_order.png" width="189" height="22" />
										</a>
									<%}%>
								</fd:OrderPermissionsTag>
							<%} %>
			
							<% if (!modInstanceToo) { %>
							<a href="<%= FDURLUtil.getStandingOrderLandingPage(so, "modify") %>">
							   <img style="margin-bottom: 1em" src="/media_stat/images/template/quickshop/update_standing_order.png" width="189" height="22" />
							</a>
							<% } %>
						</div>
						
					</td>
					<td width="20px"></td>
					<td valign="top">
							<table width="300" bgcolor="#ececec">
								<tr bgcolor="#8d6a95">
									<th colspan="2">
										<img src="/media_stat/images/template/quickshop/so_current_settings.png" width="354" height="20" />
									</th>
								</tr>
								<tr>
									<td height="8px" colspan="2"></td>
								</tr>
								<tr>
									<td style="padding-left:1em;font-weight:bold;">Delivery Frequency:</td>
									<% String freqDesc = so.getFrequencyDescription(); %>
									<td><%= Character.toUpperCase(freqDesc.charAt(0)) %><%= freqDesc.substring(1) %></td>
								</tr>
								<tr>
									<td style="padding-left:1em;font-weight:bold;">Timeslot:</td>
									<td><%= StandingOrderHelper.getDeliveryDate(so,false) %></td>
								</tr>
								<tr>
									<% if(addr!=null) { %>
										<td style="padding-left:1em;font-weight:bold;">Delivery Address:</td>
										<td><%= addr.toShortString1(EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) %></td>
									<% } else { %>
										<td style="padding-left:1em;font-weight:bold;color:red;">
											<img src="/media_stat/images/template/quickshop/so_exclamation.png" width="8" height="8"/>
											Delivery Address:
										</td>
										<td style="color:red;font-style:italic;">No address selected!</td>
									<% } %>
								</tr>
								<tr>
									<% if(paymentMethod!=null) { %>
											<td style="padding-left:1em;font-weight:bold;">Payment Option:</td>
											<td><%= paymentMethod.getCardType() %> <%= paymentMethod.getMaskedAccountNumber() %></td>
										<% } else { %>
											<td style="padding-left:1em;font-weight:bold;color:red;">
												<img src="/media_stat/images/template/quickshop/so_exclamation.png" width="8" height="8"/>
												Payment Option:
											</td>
											<td style="color:red;font-style:italic;">No payment option selected!</td>
										<% } %>
								</tr>
								<tr>
									<% FDStandingOrderList l = (FDStandingOrderList)FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.SO, so.getCustomerListName()); %>
									<td style="padding-left:1em;font-weight:bold;">Cart Contents:</td>
									
									<%int lineNum=0; %>
									<logic:iterate id="orderLine" collection="<%= quickCart.getProducts() %>" type="com.freshdirect.fdstore.customer.FDProductSelectionI" indexId="idx">
										<%ProductModel productNode = orderLine.lookupProduct();
										if(!((productNode==null || productNode.getSku(orderLine.getSkuCode()).isUnavailable() || orderLine.isInvalidConfig()))) {
												lineNum++;
										}%>
									</logic:iterate>
									<td><%= lineNum %> item<%=lineNum > 1 ? "s" : "" %></td>
								</tr> 
								<tr>
									<td height="8px" colspan="2"></td>
								</tr>
							</table>
					</td>
				</tr>
			</table>
						
			
			<hr style="margin-bottom: 20px; width: 100%; height: 1px; background-color: #ff8520; color: #ff8520; line-height: 1px; border: none;"/>	
		
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
			<fd:css href="/assets/css/fix.css"/>
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

<script type="text/javascript">
window.onload = function() {
    checkLock();
};
</script>