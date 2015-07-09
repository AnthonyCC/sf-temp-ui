<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@ page import="com.freshdirect.delivery.depot.DlvLocationModel" %>
<%@ page import="com.freshdirect.delivery.depot.DlvDepotModel" %>
<%@ page import="com.freshdirect.delivery.EnumReservationType" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ page import="com.freshdirect.fdstore.FDReservation" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.FDTimeslot" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import="com.freshdirect.webapp.util.TimeslotPageUtil" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder" %>
<%@ page import="com.freshdirect.fdstore.standingorders.DeliveryInterval" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
	boolean isStaticSlot = false;
	boolean isCheckAddress = false;
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	TimeslotContext timeSlotCtx = TimeslotContext.CHECKOUT_TIMESLOTS;
%>

<form fdform="timeslot" name="reserveTimeslot">
		<%//Render the timeslots %>
		<%@ include file="/expressco/includes/i_delivery_timeslots.jspf"%>
</form>
