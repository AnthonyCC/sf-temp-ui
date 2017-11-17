<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.customer.ErpDepotAddressModel'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel' %>
<%@ page import='com.freshdirect.fdlogistics.model.FDDeliveryDepotModel' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.delivery.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>
<%@ page import="com.freshdirect.logistics.delivery.model.TimeslotContext" %>
<%@ page import='com.freshdirect.fdlogistics.model.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.content.CategoryModel' %>
<%@ page import='com.freshdirect.storeapi.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine' %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ page import='com.freshdirect.storeapi.attributes.*'%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
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
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
boolean isStaticSlot = true;
String timeSlotId="";
boolean isCheckAddress =false;
FDReservation rsv = null;
String actionName = null;
ActionResult result=null;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
TimeslotContext timeSlotCtx = TimeslotContext.CHECK_AVAILABLE_TIMESLOTS;

if (user.isChefsTable()) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}

request.setAttribute("sitePage", "www.freshdirect.com/your_account");
request.setAttribute("listPos", "SystemMessage,CategoryNote,TimeslotBottom");

String addressId = request.getParameter("addressId");

Calendar tomorrow = Calendar.getInstance();
tomorrow.add(Calendar.DATE, 1);
SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");

// [APPDEV-2149] Display abstract timeslot table (Just days of week, no restrictions, etc.)
final boolean abstractTimeslots = false;

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/delivery_info_nav.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/your_account/delivery_info_avail_slots.jsp" : request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
	}
}
%>
<tmpl:insert template='<%= pageTemplate %>'>
	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_info_avail"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>

		<div class="delivery_info_mobweb_nav" <%= mobWeb ? "" : "style='display: none;'" %>>
			<%@ include file="/help/delivery_info_nav.jspf" %>
		</div>

		<%//Finds the address%>
		<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>

		<%//Finds the address & render the timeslots %>
    <% if (!mobWeb) { %>
      <%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>
    <%} else {%>
      <div class="timeslot-selector delivery-info-timeslot"></div>
      <% }%>

	</tmpl:put>
	<tmpl:put name="extraCss">
	  <fd:css href="/assets/css/timeslots.css" media="all" />
	</tmpl:put>
	<tmpl:put name="extraJs">
	  <fd:javascript src="/assets/javascript/timeslots.js" />
	</tmpl:put>
  	<tmpl:put name="jsmodules">
    	<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
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
