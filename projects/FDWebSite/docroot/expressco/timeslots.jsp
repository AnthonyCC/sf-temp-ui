<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext"%>
<%@ taglib uri="template" prefix="tmpl"%>
<%@ taglib uri="logic" prefix="logic"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
    boolean isStaticSlot = false;
    boolean isCheckAddress = false;
    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
    TimeslotContext timeSlotCtx = TimeslotContext.CHECKOUT_TIMESLOTS;
%>

<form fdform="timeslot" name="reserveTimeslot">
	<%
	    //Render the timeslots
	%>
	<%@ include file="/expressco/includes/i_delivery_timeslots.jspf"%>
	<c:if test="${advancedTimeslotMediaEnabled}">
		<fd:IncludeMedia name='/media/editorial/holiday/advance_order/delivbar_adv_msg.html' />
	</c:if>
</form>
