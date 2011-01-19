<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.delivery.EnumReservationType" %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;

//boolean zoneCtActive = false;
if("true".equals(request.getParameter("chefstable"))) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}
%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="true" recognizedAllowed="true" />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

if (user.isChefsTable()){
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}
//will be null.
FDReservation rsv = user.getReservation();

request.setAttribute("sitePage", "www.freshdirect.com/help");
request.setAttribute("listPos", "SystemMessage,CategoryNote");

boolean isStaticSlot = true;
boolean hasPreReserved = false;
boolean hasReservation = false;
boolean hasWeeklyReservation = false;
String timeSlotId="";
String preReserveSlotId="";
ErpCustomerInfoModel customerInfo =null;
boolean isCheckAddress =false;
String[] checkErrorType=null;
ActionResult result=null;

TimeslotContext timeSlotCtx = TimeslotContext.CHECK_AVAIL_SLOTS_NO_USER;
String addressId="";

SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>

			<%//Finds the address%>
			<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>
			<%//Timeslot display%>
			<%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>
		</tmpl:put>
</tmpl:insert>
