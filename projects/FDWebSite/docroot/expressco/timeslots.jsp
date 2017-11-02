<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.logistics.delivery.model.TimeslotContext" %>
<%@ taglib uri="template" prefix="tmpl"%>
<%@ taglib uri="logic" prefix="logic"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
    boolean isStaticSlot = false;
    boolean isCheckAddress = false;
    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
    TimeslotContext timeSlotCtx = TimeslotContext.CHECKOUT_TIMESLOTS;
    boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
%>

<form fdform="timeslot" name="reserveTimeslot">
  <%

	    //Render the timeslots
	%>
  <% if (!mobWeb) { %>
	<div class="tsWrapper"><%@ include file="/expressco/includes/i_delivery_timeslots.jspf"%></div>
	<c:if test="${advancedTimeslotMediaEnabled}">
    <div class="timeslot-note">
      <div class="note-message">
        Please note
      </div>
      <div class="note-content">
        <fd:IncludeMedia name='/media/editorial/holiday/advance_order/delivbar_adv_msg.html' />
      </div></div>
	</c:if>
  <%} else {%>
    <div class="timeslot-selector"></div>
    <script type="text/javascript">
      var DISPATCHER = FreshDirect.common.dispatcher;

      DISPATCHER.signal('server', {
        url: '/api/expresscheckout/timeslot',
        method: 'GET'
      });
    </script>
    <% }%>
</form>
