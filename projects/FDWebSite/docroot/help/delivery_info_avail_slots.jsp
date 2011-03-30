<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.delivery.EnumReservationType" %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="true" recognizedAllowed="true" />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

request.setAttribute("sitePage", "www.freshdirect.com/help");
request.setAttribute("listPos", "SystemMessage,CategoryNote");

boolean isStaticSlot = true;
boolean isCheckAddress =false;
ActionResult result=null;
FDReservation rsv = null;
String actionName = null;

TimeslotContext timeSlotCtx = TimeslotContext.CHECK_AVAIL_SLOTS_NO_USER;
String addressId="";

SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>
			<%//Finds the address%>
			<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>
		</tmpl:put>
</tmpl:insert>
