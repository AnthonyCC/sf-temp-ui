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

<%  
	boolean modifyOrderMode = false; 	
	if(session.getAttribute("MODIFIED" + orderId) != null && session.getAttribute("MODIFIED" + orderId).equals(orderId)) {
		modifyOrderMode = true;
	}
	
	if(!modifyOrderMode) {

	FDIdentity curridentity  = user.getIdentity();
	ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(curridentity);
	if(cm.getMobilePreference() == null) {
		//session.removeAttribute("SMSSubmission"+ orderId);
		if(session.getAttribute("SMSSubmission" + orderId) == null) { 
		%>
		<script type="text/javascript" src="/assets/javascript/rounded_corners.inc.js"></script>
<script language="javascript">
	function curvyCornersHelper(elemId, settingsObj) {
		if (document.getElementById(elemId)) {
			var temp = new curvyCorners(settingsObj, document.getElementById(elemId)).applyCornersToAll();
		}
	}
	var ccSettings = {
		tl: { radius: 6 },
		tr: { radius: 6 },
		bl: { radius: 6 },
		br: { radius: 6 },
		topColour: "#FFFFFF",
		bottomColour: "#FFFFFF",
		antiAlias: true,
		autoPad: true
	};
	
/* display an overlay containing a remote page */
	function doRemoteOverlay(olURL) {
		var olURL = olURL || '';
		if (olURL == '') { return false; }

		Modalbox.show(olURL, {
			loadingString: 'Loading Preview...',			
			title: ' ',
			overlayOpacity: .80,
			width: 750,
			centered: true,
			method: 'post',			
			afterLoad: function() {
					$('MB_frame').style.border = '1px solid #CCCCCC';
					$('MB_header').style.border = '0px solid #CCCCCC';
					$('MB_header').style.display = 'block';
					window.scrollTo(0,0);					
					$('MB_window').style.width = 'auto';
					$('MB_window').style.height = 'auto';
					$('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
					$('MB_content').style.padding = '0px';
					$('MB_close').style.color = 'gray';
					$('MB_close').style.background = "url(/media/editorial/site_access/images/round_x.gif) no-repeat";

					curvyCornersHelper('MB_frame', ccSettings);
			},
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }	
		});
	}
	
	function doRemoteOverlay1(olURL) {
			var olURL = olURL || '';
			if (olURL == '') { return false; }

			Modalbox.show(olURL, {
				title: ' ',
				width: 750,
				params: Form.serialize('smsform'),
				afterLoad: function() {
						$('MB_frame').style.border = '1px solid #CCCCCC';
						$('MB_header').style.border = '0px solid #CCCCCC';
						$('MB_header').style.display = 'block';
						window.scrollTo(0,0);					
						$('MB_window').style.width = 'auto';
						$('MB_window').style.height = 'auto';
						$('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
						$('MB_content').style.padding = '0px';
						$('MB_close').style.color = 'gray';
						$('MB_close').style.background = "url(/media/editorial/site_access/images/round_x.gif) no-repeat";

						curvyCornersHelper1('MB_frame', ccSettings1);
				},
				afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }	
			});
		}
</script>
<script language="javascript">
doRemoteOverlay('sms_capture.jsp');
</script>
<% } } } %>


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
<% String receipt = "true"; %>
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

