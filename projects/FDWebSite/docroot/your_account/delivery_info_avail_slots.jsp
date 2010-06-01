<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.customer.ErpDepotAddressModel'%>
<%@ page import='com.freshdirect.delivery.depot.DlvLocationModel' %>
<%@ page import='com.freshdirect.delivery.depot.DlvDepotModel' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.delivery.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine' %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ page import='com.freshdirect.fdstore.attributes.*'%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
//boolean zoneCtActive = false;
if("true".equals(request.getParameter("chefstable"))) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}
%>

<fd:CheckLoginStatus />

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
boolean isStaticSlot = true;
boolean hasPreReserved = false;
boolean hasReservation = false;
boolean hasWeeklyReservation=false;
String timeSlotId="";
String preReserveSlotId="";
boolean isCheckAddress =false;
String[] checkErrorType=null;
ActionResult result=null;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
TimeslotContext timeSlotCtx = TimeslotContext.CHECK_AVAILABLE_TIMESLOTS; 
ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());

if (user.isChefsTable()) {
	//request.setAttribute("chefstable","true");
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}
//will be null.
FDReservation rsv = user.getReservation();

request.setAttribute("sitePage", "www.freshdirect.com/your_account");
request.setAttribute("listPos", "SystemMessage,CategoryNote,TimeslotBottom");

String zone = null;
String addressId = request.getParameter("addressId");

Calendar tomorrow = Calendar.getInstance();
tomorrow.add(Calendar.DATE, 1);
SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");

%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>
				
				<%//Finds the address%>
				<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>

				<%//Delivery timeslot display%>
				<%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>

<%// move to Reservation page%>
<table width="693" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td colspan="7">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
	<% if(user.isEligibleForPreReservation() && (!"true".equals(request.getParameter("chefstable"))) && user.isDlvPassActive()) { %>
			<div align="center"><br>
				<a href="/your_account/reserve_timeslot.jsp"><img src="/media_stat/images/template/youraccount/reserve_delivery_time.gif" width="200" height="15" border="0" alt="Reserve a Delivery Time" vspace="4"></a><br>
				<span class="text12">Reserve a delivery timeslot before you place your order.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
					<a href="/your_account/reserve_timesolt.jsp"><b>Click here</b></a>
				</span><br>
				<img src="/media_stat/images/template/homepages/truck.gif" width="61" height="43" border="0" vspace="6">
			</div>
	<% }else if(!user.isDlvPassActive() && !(user.isEligibleForPreReservation() && (!"true".equals(request.getParameter("chefstable"))))) {%>
			<div align="center"><br>
				<% if (FDStoreProperties.isAdServerEnabled()) { %>
					<SCRIPT LANGUAGE=JavaScript>
						<!--
						OAS_AD('TimeslotBottom');
						//-->
					</SCRIPT><br><br>
				<% } %>
			 </div>
	<% }else if(!user.isDlvPassActive() && user.isEligibleForPreReservation() && (!"true".equals(request.getParameter("chefstable")))) {%>
			<div align="center">
				 <table>
					<tr>
						<td align="center">
							<a href="/your_account/reserve_timeslot.jsp">
								<img src="/media_stat/images/template/youraccount/reserve_delivery_time.gif" width="200" height="15" border="0" alt="Reserve a Delivery Time" vspace="4">
							</a><br>
							<span class="text12">Reserve a delivery timeslot before you place your order.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><a href="/your_account/reserve_timeslot.jsp"><b>Click here</b></a></span><br>
						</td>
						<td align="center">
							<img src="/media_stat/images/layout/clear.gif" width="1" height="34"><br>
								<% if (FDStoreProperties.isAdServerEnabled()) { %>
									<SCRIPT LANGUAGE=JavaScript>
											<!--
											OAS_AD('TimeslotBottom');
											//-->
									</SCRIPT><br><br>
								 <% } %>
						</td>
					</tr>
				</table>
			</div>
	<%}%>
		</td>
	</tr>
</table>
</tmpl:put>
</tmpl:insert>
