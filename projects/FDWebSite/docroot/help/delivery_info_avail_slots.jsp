<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import='com.freshdirect.fdlogistics.model.*' %>
<%@ page import="com.freshdirect.logistics.delivery.model.TimeslotContext" %>
<%@ page import='com.freshdirect.fdlogistics.model.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.logistics.delivery.model.EnumReservationType" %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>

<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
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

// [APPDEV-2149] Display abstract timeslot table (Just days of week, no restrictions, etc.)
final boolean abstractTimeslots = false;
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="Available Delivery Slots" pageId="delivery_info_avail_slots"></fd:SEOMetaTag>
	</tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put> --%>
		<tmpl:put name='content' direct='true'>
			<%//Finds the address%>
			<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>

			<%//Render the timeslots %>
			<%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>			
			<script>window.FreshDirect = window.FreshDirect || {};
					window.FreshDirect.gtm = window.FreshDirect.gtm || {};
					window.FreshDirect.gtm.afterPageView = window.FreshDirect.gtm.afterPageView || [];
					window.FreshDirect.gtm.afterPageView.push({ timeslotOpened:{ action: 'timeslot-pageview', pageName:'Reserve Timeslots'} });</script>
			
		</tmpl:put>
		
  	<tmpl:put name="jsmodules">
      <soy:import packageName="common"/>
      <jwr:script src="/fdmodules.js"  useRandomParam="false" />
      <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
  	</tmpl:put>
	<tmpl:put name="extraCss">
	  <jwr:style src="/timeslots.css" media="all" />
	</tmpl:put>
	<tmpl:put name="extraJs">
	  <jwr:script src="/assets/javascript/timeslots.js" useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
