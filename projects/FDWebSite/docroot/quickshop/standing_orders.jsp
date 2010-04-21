<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.fdstore.customer.ejb.EnumCustomerListType"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerListInfo"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerList"%>
<%@ page import="com.freshdirect.fdstore.lists.FDListManager"%>
<%@ page import="com.freshdirect.framework.util.DateUtil"%>
<%@ page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import="com.freshdirect.fdstore.lists.FDStandingOrderList"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' redirectPage='/quickshop/index_guest.jsp?successPage=/quickshop/index.jsp' />
<tmpl:insert template='/common/template/quick_shop_nav.jsp'>

    <tmpl:put name='extrahead' direct='true'>
        <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
        <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    </tmpl:put>
	<%-- PAGE TITLE --%>
	<tmpl:put name='title' direct='true'>FreshDirect - Quickshop</tmpl:put>
	<%-- SIDE NAV --%>
	<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br/></font>
		<img src="/media_stat/images/template/quickshop/standingorders_catnav.gif" border="0" width="80" height="38">
		<font class="space4pix"><br/></font>
		<%-- <a href="/quickshop/every_item.jsp"><img src="/media_stat/images/template/quickshop/qs_every_item_catnav.gif" width="80" height="38" border="0"></a><br/><font class="space4pix"> --%>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<div id="inner-container" style="width: 100%">
			<div class="title18" style="padding-top: 1em">Standing Orders</div>
			<div style="margin: 1em 0 1em 0; width: 100%; height: 1px; background-color: #996699"></div>
			<div class="title14">Hassle-free shopping just got even more, well... hassle-free.</div>
			<div style="margin-bottom: 1em">FreshDirect's standing orders make it even easier to get everything you love without lifting a finger. There will be more copy here that describes the many benefits of standing orders. It will explain how easy they are to use and that you can update them at any time, pause or cancel them... even ask us just to skip a week</div>
			<div style="margin-bottom: 1em">Each Standing Order uses a Shopping List and a schedule that you can update at any time. We'll automatically place your order 7 days before the next recurring delivery (and send you an email notification when we do). You'll have plenty of time to add or remove items, change that specific order, or cancel it if you don't need it at all. 
			<a href="/media_stat/editorial/site_pages/standing_orders/so_help.html" target="_blank" onClick="popup('/media_stat/editorial/site_pages/standing_orders/so_help.html','large'); return false;">Click here to learn more.</a></div>
			
<% if (user.isEligibleForStandingOrders()) { %>			
	<fd:ManageStandingOrders id="sorders">
		<% 
		for (FDStandingOrder so : sorders) {
			String listName = FDListManager.getListName( user.getIdentity(), so.getCustomerListId() );
			FDStandingOrderList list = (FDStandingOrderList) FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.SO, listName);
		    int n = list.getLineItems().size();

		    ErpAddressModel addr = so.getDeliveryAddress();		
			final String nextDlvDateText = new SimpleDateFormat("EEEE, MMMM d.").format( so.getNextDeliveryDate() );
			%>			
			<h2><a href="<%= FDURLUtil.getStandingOrderLandingPage(so, null) %>"><%= so.getCustomerListName() %></a></h2>
			<div class="title12" style="margin: 1em 1em 2em 1em">
				
				<% if (so.isError()) { %>				
					<div style="padding: 1em 0 1em 0">
						<div style="color: #CC3300">IMPORTANT NOTE: We were not able to schedule a delivery for <%= nextDlvDateText %></div>
						<div style="margin-top: 1em; color: #CC3300"><%=so.getErrorHeader()%></div>
					</div>
				<% } else { %>				
					<div style="text-align: left; font-weight: bold;">
						<%@ include file="/quickshop/includes/so_next_delivery.jspf" %>
						<div style="padding: 1em 0 1em 0">
						<div style="font-weight: normal;">Delivered <%= so.getFrequencyDescription() %>, <%= StandingOrderHelper.getDeliveryDate(so,false) %></div>
							<% if (addr != null) { %>
								<div style="font-weight: normal;"><%= addr.getScrubbedStreet() %>, <%= addr.getApartment() %></div>
							<% } %>				
						</div>					
					</div>
				<% } %>
								
				<a class="title13" href="<%= FDURLUtil.getStandingOrderLandingPage(so, null) %>">View or Edit Details &raquo;</a>
			</div>
		<% } %>
	</fd:ManageStandingOrders>

	<div style="margin: 2em 0 1em 0; width: 100%; height: 1px; background-color: #996699"></div>
	
	<fd:CreateStandingOrder result="result">
		<div id="create-so-box" style="padding: 1em 1em">
			<div class="title12" style="margin-bottom: 0.5em;">Start a new standing order:</div>
			<fd:ErrorHandler result='<%= result %>' field='<%= new String[]{"SO_NAME", "SO_FREQ"} %>' id='errorMsg'>
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>
			<form method="POST">
				<input type="hidden" name="action" value="create">
				<table style="width: 100%; border: 0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="text10">Name:</td>
						<td style="width: 20px">&nbsp;</td>
						<td class="text10">Deliver my order:</td>
						<td style="width: 20px">&nbsp;</td>
						<td></td>
					</tr>
					<tr>
						<td><input name="soName" type="text"></input></td>
						<td style="width: 20px">&nbsp;</td>
						<td>
							<select name="soFreq">
								<% for (EnumStandingOrderFrequency frqItem : EnumStandingOrderFrequency.values()) { %>
									<option value="<%= frqItem.getFrequency() %>"><%= frqItem.getTitle() %></option>
								<% } %>								
							</select>
						</td>
						<td style="width: 20px">&nbsp;</td>
						<td>
							<input type="image" src="/media_stat/images/buttons/so_start_shopping.gif" alt="Start shopping"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</fd:CreateStandingOrder>
			
<% } else { %>			

	<div id="create-so-box" style="padding: 1em 1em">
		<div class="title14" style="margin-bottom: 1em;">Creating a Standing Order is easy!</div>
		<div class="text13" style="margin-bottom: 1em;">1. Tell us how you often want us to deliver. (You can change at any time.)</div>
		<div class="text13" style="margin-bottom: 1em;">2. Fill your cart with everything you love, just as you normally do.</div>
		<div class="text13" style="margin-bottom: 1em;">3. Go to Checkout to choose a day and time by placing the first order.</div>
		<div class="text13" style="margin-bottom: 1em;">4. We'll create a shopping list you can edit at any time and place orders automatically... according to your schedule.</div>
	</div>
	<div style="margin: 1em 0 1em 0; width: 100%; height: 1px; background-color: #996699"></div>
	<div class="title14" style="margin-left: 1em; margin-right: 1em; color: #996699">Please Note: Come back here to set up a standing order once you've received your first corporate delivery.</div>
	<div style="margin: 1em 0 1em 0; width: 100%; height: 1px; background-color: #996699"></div>
	
<% } %>

</div>
</tmpl:put>
</tmpl:insert>

