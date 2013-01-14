<%@ page import = "com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import ='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>

<%@ page import="java.text.*" %>
<%@ page import='java.util.List.*' %>

<%@ taglib uri='template' prefix='tmpl' %> 
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_ORDER_DETAILS_TOTAL = 970;
final int W_YA_ORDER_DETAILS_3C_GAP = 41;
final int W_YA_ORDER_DETAILS_3C_COLUMN = 268;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%  String orderId = request.getParameter("orderId"); %>
<fd:ModifyOrderController orderId="<%= orderId %>" result="result" successPage='<%= "/your_account/order_details.jsp?orderId=" + orderId %>'>
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Order Details</tmpl:put>
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
    
        StringBuffer custName = new StringBuffer(50);
        custName.append(customerModel.getFirstName());
        if (customerModel.getMiddleName()!=null && customerModel.getMiddleName().trim().length()>0) {
            custName.append(" ");
            custName.append(customerModel.getMiddleName());
        }
        custName.append(" ");
        custName.append(customerModel.getLastName());
        
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
%>
<!-- error message handling here -->

<% 
    String errorMsg="";
     if (cart.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) {
        errorMsg= "Pending Order: Please contact us at "+user.getCustomerServiceContact()+" as soon as possible to reschedule delivery.";
%>
<% } else if (EnumSaleStatus.AUTHORIZATION_FAILED.equals(cart.getOrderStatus())) {
        errorMsg= PaymentMethodUtil.getAuthFailWarningMessage(cart.getAuthFailDescription());
%>
<%@ include file="/includes/i_pymt_warning_messages.jspf" %>
<% } %>

<form name="viewcart" method="post" action="/view_cart.jsp" style="margin:0px ! important" id="viewcart">
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
					<br /><img onclick="Modalbox.hide(); return false;" src="/media_stat/images/buttons/close_window.gif" width="141" height="19" alt="" /><br />
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


<table width="<%= W_YA_ORDER_DETAILS_TOTAL %>" align="center" border="0" cellpadding="0" cellspacing="0">
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
    if (hasCredit) { %>
    <td align="right" class="text11"><i>Credit was issued for this order.</i></td>
    <% } %>
    <% if (user.isEligibleForClientCodes() && cart.hasClientCodes()) { %>
    <td class="text11" style="text-align: right; vertical-align: middle;">
    	<a href="/api/clientCodeReport.jsp?sale=<%= orderId %>" style="text-decoration: none; outline: none;"><img src="/media_stat/images/buttons/export_client_codes.gif" width="167" height="17" style="border: none;"></a>
    </td>
    <% } %>
</tr>
</table>
<IMG src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="8" border="0"><br>
<IMG src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_ORDER_DETAILS_TOTAL %>" HEIGHT="1" border="0"><br>
<IMG src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="15" border="0"><br>
<%@ include file="/includes/your_account/i_order_detail_delivery_payment.jspf" %><br>
<IMG src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_ORDER_DETAILS_TOTAL %>" HEIGHT="1" border="0"><br>
<IMG src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="4" border="0"><br><FONT CLASS="space4pix"><br></FONT>
<table cellpadding="0" cellspacing="0" border="0" style="padding-bottom: 20px; margin: 0px auto;">
<tr valign="top">

	<%  if (!cart.isPending()) { %>
    <td>
        <A HREF="/quickshop/shop_from_order.jsp?orderId=<%= orderId %>"><IMG src="/media_stat/images/buttons/shop_from_order.gif" width="144" HEIGHT="16" border="0" ALT="SHOP FROM THIS ORDER"></A><br>
        <FONT CLASS="space4pix"><br></FONT>
        Click here to reorder items from this order in Quickshop.<br>
    </td>
    <td width="<%= W_YA_ORDER_DETAILS_3C_GAP %>"><br></td>
    <%  } %>
    
    <% if (allowModifyOrder.booleanValue()) { %>
    <td>
        <a href="/your_account/modify_order.jsp?orderId=<%=orderId%>"><img src="/media_stat/images/buttons/change_this_order.gif" border="0" ALT="CHANGE THIS ORDER"></a>

        <FONT CLASS="space4pix"><br></FONT>
        Click here to:<br>
        - add, remove, or change items<br>
        - change delivery address<br>
        - change delivery time slot<br>
        - change payment information<br>

    </td>
    <td width="<%= W_YA_ORDER_DETAILS_3C_GAP %>"><br></td>
    <% } %>
    
    <% if (allowCancelOrder.booleanValue()) { %>
    <td>
        <a href="/your_account/cancel_order.jsp?orderId=<%=orderId%>"><img src="/media_stat/images/buttons/cancel_this_order_mrn.gif" width="112" HEIGHT="16" border="0" ALT="CANCEL THIS ORDER"></a>
        <FONT CLASS="space4pix"><br></FONT>
        Click here to cancel this order.
        
    </td>
    <td width="<%= W_YA_ORDER_DETAILS_3C_GAP %>"><br></td>
   <% } %>
</tr>
</table>
<%@ include file="/includes/your_account/i_order_detail_cart_details.jspf" %><br>
<%  } %>
<br>
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
                        creditRow.append("<tr><td colspan=\"7\"><img src=\"/media_stat/images/layout/clear.gif\" width=\"1\" height=\"3\"></td></tr>");
                    } // if ccc.getAmount() > 0.0
                } //for cc
            } //c!=null
        } //for comp
        creditRow.append("<tr><td colspan=\"7\"><img src=\"/media_stat/images/layout/clear.gif\" width=\"1\" height=\"3\"></td></tr>");
%>
<table width="<%= W_YA_ORDER_DETAILS_TOTAL %>" cellpadding="0" cellspacing="0" border="0" align="center">
    <tr>
    <td><img src="/media_stat/images/layout/clear.gif" width="40" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="40" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="357" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="75" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="85" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
    </tr>
    <tr>
    <td colspan="3"><b>Credit issued for...</b></td>
    <td><b>Date</b></td>
    <td><b>Type</b></td>
    <td align="right"><b>Amount</b></td>
    <td>&nbsp;</td>
    </tr>
    <tr><td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
    <%=creditRow%>
    <tr><td colspan="7" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
    <tr><td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
    <tr>
    <td colspan="5" align="right"><b>Total credit issued for this order:</b>&nbsp;&nbsp;</td>
    <td align="right"><font class="text10bold"><%=currencyFormatter.format(totalCredit)%></font></td>
    <td>&nbsp;</td>
    </tr>
    <tr><td colspan="7"><br><br></td></tr>
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
