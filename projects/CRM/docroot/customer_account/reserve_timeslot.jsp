<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel"%>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotModel"%>
<%@ page import="com.freshdirect.logistics.delivery.model.EnumReservationType"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdlogistics.model.FDReservation"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.logistics.delivery.model.TimeslotContext" %>
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
		<link rel="stylesheet" type="text/css" href="/assets/css/global/standingorder.css"/>
		<script type="text/javascript" language="javascript" src="/assets/javascript/timeslots.js"></script>

		
	</tmpl:put>

</tmpl:insert>