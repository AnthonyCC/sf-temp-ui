<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM.dd.yyyy");
	SimpleDateFormat deliveryDateFormatter = new SimpleDateFormat("EEEE, MMM d yyyy");
	SimpleDateFormat deliveryTimeFormatter = new SimpleDateFormat("h:mm a");
	SimpleDateFormat monthYearFormatter = new SimpleDateFormat("MM.yyyy");
	DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Order Confirmation</tmpl:put>
<%
    
	boolean returnPage = false;
	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    
	String orderId = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
	FDOrderI order = FDCustomerManager.getOrder(user.getIdentity(), orderId);

	ErpAddressModel dlvAddress = order.getDeliveryAddress(); 
	ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) order.getPaymentMethod();
    Date lastOrdrDate = null;
	
    Calendar dlvStart = Calendar.getInstance(); 
    dlvStart.setTime(order.getDeliveryReservation().getStartTime());
    Calendar dlvEnd = Calendar.getInstance();
	dlvEnd.setTime(order.getDeliveryReservation().getEndTime());
	int startHour = dlvStart.get(Calendar.HOUR_OF_DAY);
	int endHour = dlvEnd.get(Calendar.HOUR_OF_DAY); 
	
	String sStartHour = startHour==12 ? "noon" : (startHour > 12 ? ""+(startHour-12) : ""+startHour);
	String sEndHour = endHour==0 ? "12am" : (endHour==12 ? "noon" : (endHour > 12 ? (endHour - 12)+"pm" : endHour+"am"));
%>

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>

<fd:ComplaintGrabber order="<%= order %>" complaints="complaints" lineComplaints="lineComplaints" deptComplaints="deptComplaints" miscComplaints="miscComplaints" fullComplaints="fullComplaints" restockComplaints="restockComplaints" retrieveApproved="true" retrievePending="false" retrieveRejected="false">

<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
	<TR><TD>&nbsp;Order Placed # <a href="/main/order_details.jsp?orderId=<%= orderId %>" class="order_number" style="padding-left: 4px; padding-right: 4px;"><%= orderId %></a>, Review Receipt</TD></TR>
</TABLE>
<%@ include file="/includes/order_summary.jspf"%>

<div class="content_scroll" style="height: 72%;">
<%	boolean showPaymentButtons = false;
	boolean showAddressButtons = false;
	boolean showDeleteButtons = false; 
    boolean displayDeliveryInfo = true;    
%>
	
<%@ include file="/includes/i_order_dlv_payment.jspf"%>
<hr class="gray1px">
<%	boolean showEditOrderButtons = false;
	boolean showFeesSection = false;
	boolean cartMode = true; %>
	
<%@ include file="/includes/i_cart_details.jspf"%>

<%@ include file="/includes/i_customer_service_message.jspf"%>

</div>

</fd:ComplaintGrabber>

</tmpl:put>
</tmpl:insert>

