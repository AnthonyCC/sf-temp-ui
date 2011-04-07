<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@ page import="com.freshdirect.delivery.depot.DlvLocationModel"%>
<%@ page import="com.freshdirect.delivery.depot.DlvDepotModel"%>
<%@ page import="com.freshdirect.delivery.EnumReservationType"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.FDReservation"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="template" prefix="tmpl"%>
<%@ taglib uri="logic" prefix="logic"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri="crm" prefix="crm"%>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Account Details > Edit Timeslot Reservation</tmpl:put>

	<tmpl:put name="header" direct="true">
		<jsp:include page="/includes/customer_header.jsp" />
	</tmpl:put>

	<tmpl:put name='content' direct='true'>
		
		<link rel="stylesheet" type="text/css" href="/assets/css/timeslots.css"/>
		<script type="text/javascript" language="javascript" src="/assets/javascript/timeslots.js"></script>

		<crm:GetFDUser id="user">
		<%String actionName = request.getParameter("actionName");%>
		<fd:ReserveTimeslotController	actionName="<%=actionName%>" result="result">
			
		<%
			boolean isStaticSlot = false;
			boolean isCheckAddress =false;

			String addressId = request.getParameter("addressId");
			String pageURI = request.getRequestURI();
			boolean fromCheckout = "checkout".equalsIgnoreCase(request.getParameter("from"));
			boolean fromCheckoutReview = "checkout_review".equalsIgnoreCase(request.getParameter("from"));
			boolean fromShopping = "order".equalsIgnoreCase(request.getParameter("from"));
			String cancelLink = fromCheckout ? "/checkout/checkout_select_addr_payment.jsp" : "/main/account_details.jsp";
			if (fromCheckoutReview) cancelLink = "/checkout/checkout_review_items.jsp";
			if (fromShopping) cancelLink = "/order/place_order_build.jsp";
			String backPage = fromCheckout ? "CHECKOUT STEP 2" : "ACCOUNT DETAILS";
			if (fromCheckoutReview) backPage = "CHECKOUT STEP 1";
			if (fromShopping) backPage = "BUILD ORDER";
			
			int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
			if("true".equals(request.getParameter("chefstable"))|| user.isChefsTable()) {
				timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
			}

			FDReservation rsv = user.getReservation();
			boolean hasReservation = rsv != null && addressId.equals(rsv.getAddressId());
			TimeslotContext timeSlotCtx=TimeslotContext.RESERVE_TIMESLOTS_CRM; 
		%>

		<div class="cust_module" style="width: 90%;">
			<div class="cust_module_header">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="cust_module_header_text" width="25%">Reserve a Delivery Time</td>
						<td width="50%" align="center"><a href="<%=cancelLink%>"
							class="cancel" style="width:250px;">BACK TO <%=backPage%></a><%--&nbsp;&nbsp;<a href="javascript:document.delivery_address.submit();" class="save">SAVE</a--%></td>
						<td width="25%" align="right" class="note" style="padding-right: 8px;">* Required</td>
					</tr>
				</table>
			</div>

			<div class="cust_module_content">
				
			<%//Finds the address & render the timeslots %>
			<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>

<form name="reserveTimeslot" method="POST" action="/customer_account/reserve_timeslot.jsp?chefstable=<%=user.isChefsTable()%>&addressId=<%=request.getParameter("addressId")%>" name="reserveTimeslot">
				<input type="hidden" name="chefstable" value="<%= user.isChefsTable() %>"/>
				<input type="hidden" name="addressId" value="<%=address.getPK().getId()%>" />
				<input type="hidden" name="actionName" value="" />

				<% String timeSlotId = ""; %> 
					<table width="90%" cellpadding="0" cellspacing="0" border="0" align="center">
						<tr>
							<td>
								<%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>
							</td>
						</tr>	
					</table>
				
					<table width="90%" cellpadding="0" cellspacing="0" border="0" align="center">
						<tr>
							<td colspan="7"><img src="/media_stat/images/template/youraccount/choose_reservation_type.gif"
								width="256" height="10" vspace="10"></td>
						</tr>
						<tr valign="top">
							<td>
								<input type="radio" name="reservationType"
								<%=(rsv == null || EnumReservationType.ONETIME_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %>
								value="<%=EnumReservationType.ONETIME_RESERVATION.getName()%>"
								class="radio">&nbsp;</td>
							<td colspan="6">
								<span class="text12"><b>Reserve for this week only.</b></span>
							</td>
						</tr>
						
						<tr valign="top">
							<td><input type="radio" name="reservationType"
								<%=(rsv != null && EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %>
								value="<%=EnumReservationType.RECURRING_RESERVATION.getName()%>"
								class="radio">&nbsp;</td>
							<td colspan="6">
								<span class="text12">
									<b>Reserve this day and time for me every week</b>
								</span><br/>
								Select this option to make this a standing, weekly reservation.
								Please note that reservations not used for two weeks will be
								cleared. At any time you can return to re-place or update your
								reservation.
							</td>
						</tr>

						<tr>
							<td colspan="7" align="center"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
							<%if((rsv == null || rsv.isAnonymous()) && !hasReservation){%> 
								<input type="image"
										src="/media_stat/images/buttons/reserve_delivery.gif"
											onclick="reserveTimeslot.actionName.value='reserveTimeslot'">
							<%} else {%>
								<input type="image"
										src="/media_stat/images/buttons/reserve_delivery_cancel.gif"
											onclick="reserveTimeslot.actionName.value='cancelReservation'">&nbsp;&nbsp;&nbsp;
								<input	type="image"
										src="/media_stat/images/buttons/reserve_delivery_save_changes.gif"
											onclick="reserveTimeslot.actionName.value='changeReservation'">
							<%}%>
							</td>
						</tr>
					</table>
		</form>

		</fd:ReserveTimeslotController>
			</div>
		</div>
		</crm:GetFDUser>
	</tmpl:put>

</tmpl:insert>