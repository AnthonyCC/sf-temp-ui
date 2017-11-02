<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotModel" %>
<%@ page import="com.freshdirect.logistics.delivery.model.EnumReservationType" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDReservation" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDTimeslot" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.webapp.util.TimeslotPageUtil" %>
<%@ page import="com.freshdirect.logistics.delivery.model.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import='com.freshdirect.fdstore.content.CategoryModel' %>
<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<% //expanded page dimensions
final int W_RESERVE_TIMESLOTS_TOTAL = 970;
%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
String addressId = NVL.apply(request.getParameter("addressId"), "");
boolean isStaticSlot = false;
boolean isCheckAddress =false;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

// redirect, if not eligible
if(!user.isEligibleForPreReservation()) {
	response.sendRedirect("/your_account/");
	return;
}

TimeslotContext timeSlotCtx=TimeslotContext.RESERVE_TIMESLOTS;
String actionName = request.getParameter("actionName");


boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/dnav.jsp";
String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/your_account/reserve_timeslot.jsp" : request.getAttribute("sitePage").toString();

if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
	}
}

%>

<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Reserve Timeslot</tmpl:put>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="reserve_timeslot"></fd:SEOMetaTag>
	</tmpl:put>

	<tmpl:put name="extraCss">
	  <fd:css href="/assets/css/timeslots.css" media="all" />
	</tmpl:put>
	<tmpl:put name="extraJs">
	  <fd:javascript src="/assets/javascript/timeslots.js" />
	</tmpl:put>
  	<tmpl:put name="jsmodules">
    	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	</tmpl:put>

    <tmpl:put name='content' direct='true'>

    	<div class="delivery_info_mobweb_nav" <%= mobWeb ? "" : "style='display: none;'" %>>
			<%@ include file="/help/delivery_info_nav.jspf" %>
		</div>


	<fd:ReserveTimeslotController actionName="<%=actionName%>" result="result">

	<%
		FDReservation rsv = user.getReservation();
		boolean hasReservation = rsv != null && addressId.equals(rsv.getAddressId());

		// [APPDEV-2149] Display abstract timeslot table (Just days of week, no restrictions, etc.)
		final boolean abstractTimeslots = false;
	%>

		<%//Finds the address & render the timeslots %>
		<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>

<form name="reserveTimeslot" method="POST" action="/your_account/reserve_timeslot.jsp" id="reserveTimeslot" name="reserveTimeslot">
		<input type="hidden" name="chefstable" value="<%= user.isChefsTable() %>"/>
		<input type="hidden" name="addressId" value="<%=address.getPK()!=null ? address.getPK().getId(): null %>">
		<input type="hidden" name="actionName" value="">

		<%//Render the timeslots %>
    <% if (!mobWeb) { %>
  		<div class="tsWrapper">
  			<%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>
  		</div>
    <%} else {%>
      <div class="timeslot-selector reserve-timeslot"></div>
      <% }%>

		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10">
		<%//Reservation stuff%>
		<table style="width: <%= (mobWeb) ? "100%": W_RESERVE_TIMESLOTS_TOTAL+"px" %>;" cellpadding="0" cellspacing="0" border="0">
			<%-- <tr>
				<td colspan="7"><img src="/media_stat/images/layout/dotted_line_w.gif" alt="" style="width: <%= (mobWeb) ? "100%": W_RESERVE_TIMESLOTS_TOTAL+"px" %>;" height="1"></td>
			</tr> --%>
			<tr>
				<td colspan="7"><!-- <img src="/media_stat/images/template/youraccount/choose_reservation_type.gif" width="256" height="10" vspace="10" alt="Please Choose a Reservation Type"></td> -->
				<!-- <span class="Container_Top_YourAccountTimeslot">Please Choose a Reservation Type</span> -->
			</tr>
			</table>
			<fieldset><legend class="offscreen">please choose a reservation type:</legend><table>
						<%-- <tr valign="top">
				<td>
					<input type="radio" id="reservationType_field1" name="reservationType" <%=(rsv == null || EnumReservationType.ONETIME_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %> value="<%=EnumReservationType.ONETIME_RESERVATION.getName()%>" class="radio">&nbsp;
				</td>
				<td colspan="6"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
					<span class="text12"><b><label for="reservationType_field1"> Reserve for this week only.</label></b>
					</span>
				</td>
			</tr> --%>
			<tr>
				<td colspan="7"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4"></td>
			</tr>
			<tr>
				<td>
					<input type="checkbox" id="reservationType_field2" name="reservationType" <%=(rsv == null || EnumReservationType.ONETIME_RESERVATION.equals(rsv.getReservationType())) ? "" : "checked" %> value="<%=EnumReservationType.ONETIME_RESERVATION.getName()%>" class="checkbox" onclick="javascript:changeMe(this)">&nbsp;
					<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3">
					<span class="text12"><b><label for="reservationType_field2"> Make this a Weekly Reservation* </label></b></span><br>
				</td>
				<td colspan="6">
<!-- 					<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3"><br>
 					<span class="text12"><b><label for="reservationType_field2"> Reserve this day and time for me every week</label></b></span><br>
					Select this option to make this a standing weekly reservation. Please note that reservations not used will be released for good. You will have to return to this page to reset your reservation settings.
				</td> -->
			</tr>
			</table></fieldset>
			<table>
			<tr>
				<td colspan="7" align="center">
					<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="14"><br>

					<%if((rsv == null || rsv.isAnonymous()) && !hasReservation){%>
						<input type="image" src="/media_stat/images/buttons/reserve_delivery.gif" alt="Reserve Delivery" onclick="reserveTimeslot.actionName.value='reserveTimeslot'"><br>
						*A weekly reservation must be used or you'll lose it. <br>
						 Timeslot discounts may vary from week to week.
					<%} else {%>
						<button class="cssbutton red nontransparent small" onclick="reserveTimeslot.actionName.value='cancelReservation'">CANCEL RESERVATION</button>
						<button class="cssbutton green small" onclick="reserveTimeslot.actionName.value='changeReservation'">SAVE CHANGES</button>
					<%}%>
				</td>
			</tr>
		</table>
</form>
		</fd:ReserveTimeslotController>

<script type="text/javascript">
var FreshDirect = FreshDirect || {};
FreshDirect._page_options = {rsvType: { RECURRING: 'WRR', ONETIME: 'OTR' }};
changeMe($('reservationType_field2'));
</script>
<% if (mobWeb) { %>
  <script type="text/javascript">
  var getTimeslots = function () {
    if (FreshDirect && FreshDirect.common && FreshDirect.common.dispatcher) {
      var DISPATCHER = FreshDirect.common.dispatcher;

      DISPATCHER.signal('server', {
        url: '/api/expresscheckout/timeslot',
        method: 'GET'
      });
    } else {
      setTimeout(getTimeslots, 100);
    }
  };
  getTimeslots();
  </script>
<% }%>
	</tmpl:put>
</tmpl:insert>
