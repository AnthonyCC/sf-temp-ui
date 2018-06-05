<%@ page import='java.util.*' %>
<%@ page import = "com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import ='com.freshdirect.fdstore.*'%>
<%@ page import ='com.freshdirect.fdlogistics.model.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.common.pricing.EnumDiscountType' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDModifyCartModel" %>

<%@ page import="java.text.*" %>
<%@ page import='java.util.List.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<% //expanded page dimensions
final int W_YA_ORDER_DETAILS_TOTAL = 970;
final int W_YA_ORDER_DETAILS_3C_GAP = 41;
final int W_YA_ORDER_DETAILS_3C_COLUMN = 268;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%  String orderId = request.getParameter("orderId");
if(orderId==null){
	orderId = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
}

if (orderId == null){
    throw new FDNotFoundException("No orderId was provided");
}
%>
<fd:ModifyOrderController orderId="<%= orderId %>" result="result" successPage='<%= "/your_account/order_details.jsp?orderId=" + orderId %>'>
<tmpl:insert template='/common/template/dnav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Order Details"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Order Details</tmpl:put> --%>
	<tmpl:put name='extraCss' direct='true'>
		<jwr:style src="/your_account.css" media="all" />
		<link rel="stylesheet" type="text/css" media="all" href="/assets/css/expressco/cartcontent.css" media="all" />
	</tmpl:put>
	<tmpl:put name='printdata' direct='true'>order_details</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%
    NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
    SimpleDateFormat dateOnlyFormatter = new SimpleDateFormat("MM/dd/yy");
    //String orderId = request.getParameter("orderId");

    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
    FDIdentity identity  = user.getIdentity();
    ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
%>

<fd:GetOrder id='cart' saleId='<%= orderId %>'>
<%
    if (cart != null) {
        // !!! REFACTOR: duplicates code from checkout pages

        //
        // get delivery info
        //
        ErpAddressModel dlvAddress = cart.getDeliveryAddress();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
        FDReservation reservation = cart.getDeliveryReservation();
        String fmtDlvDateTime = dateFormatter.format(reservation.getStartTime()).toUpperCase();
        Calendar dlvStart = Calendar.getInstance();
        dlvStart.setTime(reservation.getStartTime());
        Calendar dlvEnd =   Calendar.getInstance();
        dlvEnd.setTime(reservation.getEndTime());
        //int startHour =  dlvStart.get(Calendar.HOUR_OF_DAY);
        //int endHour = dlvEnd.get(Calendar.HOUR_OF_DAY);

        //String sStartHour = startHour==12? "noon" : (startHour>12 ? ""+(startHour-12) : ""+startHour);
        //String sEndHour = endHour==0 ? "12 am" : (endHour==12 ? "noon" : (endHour>12 ? (endHour-12)+" pm" : endHour+" am"));
        String deliveryTime = getTimeslotString(dlvStart, dlvEnd);
        //
        // get payment info
        //
        ErpPaymentMethodI paymentMethod =(ErpPaymentMethodI) cart.getPaymentMethod();
        //
        // get order line info
        //
        boolean isSubmitted = cart.getOrderStatus().equals(EnumSaleStatus.SUBMITTED) || cart.getOrderStatus().equals(EnumSaleStatus.AUTHORIZED) ||cart.getOrderStatus().equals(EnumSaleStatus.AUTHORIZATION_FAILED);

        boolean isFdxOrder = false;
    	EnumEStoreId EStoreIdEnum = null;
    	EStoreIdEnum = cart.getEStoreId();
    	if (EStoreIdEnum != null && (EStoreIdEnum).equals(EnumEStoreId.FDX)) { isFdxOrder = true; }

	%>
	<!-- error message handling here -->

	<%
	    String errorMsg="";
	     if (cart.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) {
	        errorMsg= "Pending Order: Please contact us at "+user.getCustomerServiceContact()+" as soon as possible to reschedule delivery.";
	%>
	<% } else if (EnumSaleStatus.AUTHORIZATION_FAILED.equals(cart.getOrderStatus())) {
	        errorMsg= PaymentMethodUtil.getAuthFailErrorMessage(cart.getAuthFailDescription());
	%>
		<%@ include file="/includes/i_error_messages.jspf" %>
	<% } %>

	<%
		/*
		 *	Why is this form tag here? ... And no end form tag.
		 *
		 *	<form name="viewcart" method="post" action="/view_cart.jsp" style="margin:0px ! important" id="viewcart">
		 */
	%>
	<div class="groupScaleBox" style="display:none"><!--  -->
		<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="groupScaleBoxContent" id="groupScaleBox" >
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">GROUP DISCOUNT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/layout/clear.gif" width="10" height="10" border="0" alt="" /></a></td>
				<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td>
					<%-- all your content goes in this div, it controls the height/width --%>
					<div id="group_info" style="display:none">This is the more info hidden div.<br /><br /></div>
					<div style="height: auto; width: 200px; text-align: center; font-weight: bold;">
					<br /><img onclick="Modalbox.hide(); return false;" src="/media_stat/images/buttons/close_window.gif" width="141" height="19" alt="Close Window" /><br />
					</div>
				</td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr>
				<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_left_curve_8A6637.gif" width="6" height="6" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" /></td>
				<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_right_curve_8A6637.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
		</table>
	</div>
	<%-- order details/CTA row (under top nav bar) --%>
	<% if (cart.getStandingOrderName() != null){ %>
		<div class="order-details-so-info"><%= cart.getStandingOrderName() %>, <%= cart.getSODeliveryDate() %> Delivery</div>
	<% }%>
	<%
		boolean hasCredit = false;
	    Collection comp = cart.getComplaints();
	    if (comp != null) {
			ErpComplaintModel c = null;
	        for (Iterator i=comp.iterator(); i.hasNext(); ) {
	        	c = (ErpComplaintModel)i.next();

				if (c != null && EnumComplaintStatus.APPROVED.equals(c.getStatus())){
	            	hasCredit = true;
				}
			}
		}
	    boolean hasClientCodes = (user.isEligibleForClientCodes() && cart.hasClientCodes());
	    boolean hasModify = allowModifyOrder.booleanValue();
	    boolean hasCancel = allowCancelOrder.booleanValue();
	    boolean isViewingCurrentModifyOrder = orderId != null && orderId.equals(FDUserUtil.getModifyingOrderId(user));
	    boolean isModifyOrder_order_details = (user.getShoppingCart() instanceof FDModifyCartModel);
	%>
	<table width="<%= W_YA_ORDER_DETAILS_TOTAL %>" align="center" border="0" cellpadding="0" cellspacing="0" style="margin-bottom: 10px;">
		<tr>
		    <td class="text11">
		       <font class="title18" >Order # <%= orderId %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Status:
		       <% if( EnumSaleStatus.AUTHORIZATION_FAILED.equals(cart.getOrderStatus())) {%>
		       <font color="#FF0000"><%=cart.getOrderStatus().getDisplayName()%></font>
		       <%} else {%><%=cart.getOrderStatus().getDisplayName()%>
		       <%}%>

		       </font> &nbsp;&nbsp;&nbsp;<br>
		        <%-- Having trouble, send an e-mail to <A HREF="mailto:accounthelp@freshdirect.com">accounthelp@freshdirect.com</A> or call 1-866-2UFRESH.--%>
		    </td>
		    <td>
		    	<% if (hasCredit) { %>
					<i>Credit was issued for this order.</i>
	    		<% } %>
		    </td>
		    
		</tr>
	</table>
	
	<div class="order-details noprint actions">
		<%-- PRINT BUTTON --%>
		<% if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.printinvoice, user)) { %>
			<button onclick="window.print()" class="cssbutton medium green transparent" title="Click here to print invoice for this order">Print</button>
		<% } %>
		    
		<%-- RE-ORDER BUTTON --%>
		<% if (user.getMasqueradeContext() != null && EnumSaleType.REGULAR.equals(cart.getOrderType())) { %>
			<button type="button" name="Reorder" onclick="FreshDirect.components.reorderPopup.openPopup(<%= orderId %>)" class="cssbutton medium purple transparent">Reorder</button>
		<% } %>
		
		<%-- SHOP FROM ORDER BUTTON --%>
		<%  if (!cart.isPending()) { %>
				<button class="cssbutton medium green transparent" onclick="location.href='/quickshop/shop_from_order.jsp?orderId=<%= orderId %>'" title="Click here to reorder items from this order in Quickshop">Shop From Order</button>
		<% } %>
		
		<%-- EXPORT CLIENT CODES BUTTON --%>
		<% if (hasClientCodes) { %>
			<button class="cssbutton medium green transparent" title="Click here to export client codes for this order" onclick="location.href='/api/clientCodeReport.jsp?sale=<%= orderId %>'">
				Export Client Codes
			</button>
		<% } %>
		<% if(isViewingCurrentModifyOrder) {%><%-- MODIFY - CANCEL CHANGES BUTTON --%>
			<button class="cssbutton medium orange transparent cancel-modify-order-btn" data-gtm-source="order-details" title="Click here to cancel changes"  onclick="location.href='/your_account/cancel_modify_order.jsp'">Cancel Changes</button>
	   	<% } else if (!isModifyOrder_order_details && hasModify) { %><%-- MODIFY - MODIFY ORDER BUTTON --%>
			<button class="cssbutton medium orange modify-order-btn" data-gtm-source="order-details" title="Click here to modify this order"  onclick="location.href='/your_account/modify_order.jsp?orderId=<%= orderId %>&action=modify'">Modify Order</button>
		<% } %>
		
  		<%-- CANCEL ORDER BUTTON - this button is right aligned --%>
  		<% if (hasCancel) { %>
			<button class="cssbutton medium red transparent cancel-order-btn" title="Click here to cancel this order" onclick="location.href='/your_account/cancel_order.jsp?orderId=<%=orderId%>'">Cancel Order</button>
		<% } %>
	</div>
	
	<table width="<%= W_YA_ORDER_DETAILS_TOTAL %>" align="center" border="0" cellpadding="0" cellspacing="0">
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_ORDER_DETAILS_TOTAL %>" height="1" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="15" border="0" alt="" /><br />
	<%@ include file="/includes/your_account/i_order_detail_delivery_payment.jspf" %><br />
	<img src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_ORDER_DETAILS_TOTAL %>" height="1" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0" alt="" ><br /><span class="space4pix"><br /></span>
	<%@ include file="/includes/your_account/i_order_detail_cart_details.jspf" %><br />
<%  } %>
<br />
<%
    double totalCredit = 0.0;
    int orderLine = 0;
    StringBuffer creditRow = new StringBuffer(2000);

    Collection comp = cart.getComplaints();
    if (comp != null) {
        for (Iterator i=comp.iterator(); i.hasNext(); ) {
            ErpComplaintModel c = (ErpComplaintModel) i.next();
            if (c != null && EnumComplaintStatus.APPROVED.equals(c.getStatus())) {
                totalCredit += c.getAmount();
            }
        }
    }
    if (totalCredit > 0) {
        ErpComplaintModel c = null;
        for (Iterator i=comp.iterator(); i.hasNext(); ) {
            c = (ErpComplaintModel)i.next();
            if (c != null && EnumComplaintStatus.APPROVED.equals(c.getStatus())) {
                List cc = c.getComplaintLines();
                for (Iterator ii=cc.iterator(); ii.hasNext(); ) {
                    ErpComplaintLineModel ccc = (ErpComplaintLineModel)ii.next();
                    if (ccc.getAmount() > 0.0) {
                        if (ccc.getQuantity() != 0.0) {
                            creditRow.append("<tr><td align=\"right\">"+(int)ccc.getQuantity()+"</td>");
                        } else {
                            creditRow.append("<tr><td>&nbsp;</td>");
                        }
                        creditRow.append("<td>&nbsp;</td>");
                        creditRow.append("<td><font class=\"text10bold\">");
                        if ("ORLN".equalsIgnoreCase(ccc.getType().toString())) {
                            try {
                                orderLine = Integer.parseInt(ccc.getComplaintLineNumber());
                            } catch (NumberFormatException Ex){
                                //do what?...
                            }
                            creditRow.append(cart.getOrderLine(orderLine).getDescription()+"</font>&nbsp");
                            if (cart.getOrderLine(orderLine).getConfigurationDesc()!=null && !"".equals(cart.getOrderLine(orderLine).getConfigurationDesc().trim())) {
                                creditRow.append("("+cart.getOrderLine(orderLine).getConfigurationDesc()+")");
                            }
                        } else if ("DEPT".equalsIgnoreCase(ccc.getType().toString())) {
                            creditRow.append(ccc.getDepartmentName()+" Department</font>");
                        } else {
                            creditRow.append("General</font>");
                        }
                        creditRow.append("</td>");
                        creditRow.append("<td>"+dateOnlyFormatter.format(c.getApprovedDate())+"</td>");
                        creditRow.append("<td>"+ccc.getMethod().getName()+"</td>");
                        creditRow.append("<td align=\"right\"><font class=\"text10bold\">"+currencyFormatter.format(ccc.getAmount())+"</font></td>");
                        creditRow.append("<td>&nbsp;</td></tr>");
                        creditRow.append("<tr><td colspan=\"7\"><img src=\"/media_stat/images/layout/clear.gif\" alt=\"\" width=\"1\" height=\"3\"></td></tr>");
                    } // if ccc.getAmount() > 0.0
                } //for cc
            } //c!=null
        } //for comp
        creditRow.append("<tr><td colspan=\"7\"><img src=\"/media_stat/images/layout/clear.gif\" alt=\"\" width=\"1\" height=\"3\"></td></tr>");
%>
<table width="<%= W_YA_ORDER_DETAILS_TOTAL %>" cellpadding="0" cellspacing="0" border="0" align="center">
    <tr>
	    <td><img src="/media_stat/images/layout/clear.gif" width="40" height="1" alt="" /></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="40" height="1" alt="" /></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="357" height="1" alt="" /></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="75" height="1" alt="" /></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="85" height="1" alt="" /></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="1" alt="" /></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1" alt="" /></td>
    </tr>
    <tr>
	    <td colspan="3"><b>Credit issued for...</b></td>
	    <td><b>Date</b></td>
	    <td><b>Type</b></td>
	    <td align="right"><b>Amount</b></td>
	    <td>&nbsp;</td>
    </tr>
    <tr><td colspan="7"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4"></td></tr>
    <%=creditRow%>
    <tr><td colspan="7" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td></tr>
    <tr><td colspan="7"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4"></td></tr>
    <tr>
	    <td colspan="5" align="right"><b>Total credit issued for this order:</b>&nbsp;&nbsp;</td>
	    <td align="right"><font class="text10bold"><%=currencyFormatter.format(totalCredit)%></font></td>
	    <td>&nbsp;</td>
    </tr>
    <tr><td colspan="7"><br /><br /></td></tr>
</table>
<%  } %>

</fd:GetOrder>

</tmpl:put>
</tmpl:insert>
</fd:ModifyOrderController>

<%!
    private String getTimeslotString(Calendar startTimeCal, Calendar endTimeCal){
        StringBuffer sb = new StringBuffer();
        int startHour = startTimeCal.get(Calendar.HOUR_OF_DAY);
        sb.append(startHour==12 ? "noon" : (startHour > 12 ? startHour - 12+"": startHour+""));
        int startMin = startTimeCal.get(Calendar.MINUTE);
        if(startMin != 0){
            sb.append(":"+startMin);
        }
        int amPm = startTimeCal.get(Calendar.AM_PM);
        if(amPm == 1){
            sb.append(" pm");
        }else{
            sb.append(" am");
        }
        sb.append(" - ");
        int endHour = endTimeCal.get(Calendar.HOUR_OF_DAY);
        sb.append(endHour == 0 ? "12" : (endHour == 12 ? "noon" : (endHour > 12 ? endHour - 12+"" : endHour+"")));
        int endMin = endTimeCal.get(Calendar.MINUTE);
        if(endMin != 0){
            sb.append(":"+endMin);
        }
        if(sb.toString().indexOf("noon") < 0){
            amPm = endTimeCal.get(Calendar.AM_PM);
            if(amPm == 1){
                sb.append(" pm");
            }else{
                sb.append(" am");
            }
        }
        return sb.toString();
    }
%>
