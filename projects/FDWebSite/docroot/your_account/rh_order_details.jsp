<%@ page import='com.freshdirect.delivery.depot.*'%>
<%@ page import='com.freshdirect.payment.EnumPaymentMethodType'%>
<%@ page import='com.freshdirect.customer.EnumUnattendedDeliveryFlag'%>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import="java.text.MessageFormat" %>
<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import="com.freshdirect.fdstore.promotion.PromotionI" %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.framework.util.FormatterUtil"%>
<%@ page import="com.freshdirect.giftcard.*"%>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!
java.text.SimpleDateFormat cutoffDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE, MM/d/yy");
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%  String orderId = request.getParameter("orderId"); %>
<fd:ModifyOrderController orderId="<%= orderId %>" result="result" successPage='<%= "/your_account/order_details.jsp?orderId=" + orderId %>'>
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Order Details</tmpl:put>
    <tmpl:put name='content' direct='true'>


<%


FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getRobinHoodSkucode());
Integer totalQuantity = user.getDonationTotalQuantity();


//String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
 String orderNumber = request.getParameter("orderId"); 
%>
<fd:GetOrder id='cart' saleId='<%=orderNumber%>'>

<%ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) cart.getPaymentMethod();

String pymtDetailWidth="630";

String lineWidth = "290";
FDCartLineI cartLine = cart.getOrderLine(0);

int idx = 0;
%>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=pymtDetailWidth%>">
<tr>
	<td colspan="6"class="text11">
		<span class="title18">Order # <%= orderNumber%></span><br />
		<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0" vspace="8"><br /><br />
	</td>
</tr>

<TR VALIGN="TOP">
    <TD WIDTH="320">
		<table cellpadding="0" cellspacing="0" border="0" width="320">
			<tr>
				<td colspan="3" style="padding: 2px;" align="left">
					<img src="/media_stat/images/donation/robinhood/robin_hood_logo_sm.gif" height="23" width="130" alt="Robin Hood" /><br />
				</td>
			</tr>
			<tr>
				<td colspan="3" style="padding: 2px;">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding: 2px;" align="left" valign="bottom">
					<span class="text11bold">Robin Hood</span>&nbsp;Donation Subtotal&nbsp;(<%=totalQuantity%>&nbsp;Meals):
				</td>
				<td style="padding: 2px;" width="70" align="right" valign="bottom"><%= JspMethods.currencyFormatter.format( cart.hasInvoice() ? cart.getInvoicedSubTotal() : cart.getSubTotal() ) %></td>
			</tr>
			<tr>
				<td colspan="2" style="padding: 2px;" align="left">
					<span class="text13bold">TOTAL:</span>&nbsp;<%= JspMethods.currencyFormatter.format( cart.hasInvoice() ? cart.getInvoicedSubTotal() : cart.getSubTotal() ) %>
				</td>
				<td style="padding: 2px;" width="70" align="left" class="text13bold"></td>
			</tr>
			<tr>
				<td colspan="3" style="padding: 2px;">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0" alt="" /><br />
				</td>
			</tr>
		</table>
    </TD>
    <TD valign="top" align="CENTER" width="40">
    	 	<img src="/media_stat/images/layout/cccccc.gif" width="1" height="280"><br />
	    </td>
    <TD WIDTH="300">
        <img src="/media_stat/images/navigation/payment_info.gif" WIDTH="91" HEIGHT="9" border="0" alt="PAYMENT INFO"><br />
        <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%=lineWidth%>" HEIGHT="1" BORDER="0" VSPACE="3"><br />
        <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="300">
        <TR VALIGN="TOP">
            <TD WIDTH="10"><br /></TD>
            <TD WIDTH="300">
            <font class="space4pix"><br /></font>
            <FONT CLASS="text12">
            <%if(cart.getCustomerCreditsValue() > 0) { %>
            	<b>FreshDirect Store Credit:</b><br />
            	<font class="space4pix"><br /></font>
		<%=currencyFormatter.format(cart.getCustomerCreditsValue())%><br />
            	<br /><br />
            <% } %>
            <b>Order Total:</b><br />
            <font class="space4pix"><br /></font>
            <%=currencyFormatter.format(cart.getTotal())%><br />
            <br /><br />
			<b>Credit Card</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />
            <font class="space4pix"><br /></font>
            
            <font class="space4pix"><br /></font>
	                <%= paymentMethod.getName()%><br />
	                <%= paymentMethod.getCardType() %> - <%= paymentMethod.getMaskedAccountNumber() %><br />
            <br />
            
            
            
            <% if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {%>
	                <b>Billing Address:</b><br />
	                <font class="space4pix"><br /></font>
	                <%= paymentMethod.getName() %><br />
	                <%= paymentMethod.getAddress1() %><% if(paymentMethod.getApartment()!=null && paymentMethod.getApartment().trim().length()>0) { %>, Apt. <%=paymentMethod.getApartment()%><% } %><br />
	                <% if(paymentMethod.getAddress2()!=null && paymentMethod.getAddress2().trim().length()>0) { %>
	                <%=paymentMethod.getAddress2()%><br />
	                <%}%>
	                <%= paymentMethod.getCity() %>, <%= paymentMethod.getState() %> <%= paymentMethod.getZipCode() %><br /><br />
	                <%}%>
	                <%
	                if(EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())){%>
	                    <b>Billing Reference/Client Code:</b><br />
	                    <font class="space4pix"><br /></font>
	                    <%= paymentMethod.getBillingRef() %><br />
	                <%
            }%>
            
			
            </FONT>
            </TD>
        </TR>
        </TABLE>
        
        </TD>
        
    </TR>
        
 </TABLE>  
 <br />
 <IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"><br />
<IMG src="/media_stat/images/layout/cccccc.gif" width="693" height="1"><br />
<br /><br />


<table width="680" cellspacing="0" cellpadding="0" border="0">

	<tr>
			<td class="text11bold" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quantity<br/></td>
			<td></td>
			<td></td>
			<td class="text11bold" align="center">Unit<br/>Price</td>
			<td></td>
			<td class="text11bold" align="center">Final<br/>Price</td>
	</tr>
		<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		</tr>
		
	<tr>
			<td align="left" class="text11bold" bgcolor="#DDDDDD" colspan="6">
			<img src="/media_stat/images/donation/robinhood/landing/color_swatch_F0F0E7.gif" width="0" height="8" border="0"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FRESHDIRECT
			</td>
	</tr>
		<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		</tr>
		
	<tr>
		
			<td class="text11bold" align="center"><%= cartLine.getOrderedQuantity() %></td>
			<td style="padding-left:30px;" class="text11bold" align="center">Robin Hood Holiday Meal for Eight	</td>
			<td></td>
			<td align="center"><%= JspMethods.currencyFormatter.format( productInfo.getDefaultPrice() ) %>/<%= productInfo.getDefaultPriceUnit().toLowerCase() %></td>
			<td></td>
			<td  align="center"  style="padding-left:4px;" class="text11bold"> <%= JspMethods.currencyFormatter.format( cart.hasInvoice() ? cart.getInvoicedSubTotal() : cart.getSubTotal() ) %>
		
		

	</tr>
	<tr>
			<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
	</tr>
</table> 

<table width="680" cellspacing="0" cellpadding="0" border="0" valign="middle" >
		<tr>
			<td ><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
			<td></td>			<td></td>			<td></td>			<td></td>			<td></td>

		</tr>
		
        <tr>
		<td colspan="4" >&nbsp;</td>
		<td align="right" class="orderTotal" colspan="6" width="680">
		<b>ORDER TOTAL:  <%= JspMethods.currencyFormatter.format(cart.hasInvoice() ? cart.getInvoicedSubTotal() : cart.getSubTotal()) %> &nbsp;&nbsp;&nbsp;&nbsp;  </b></td>
        </tr>
</table>

</fd:GetOrder>
</tmpl:put>
</tmpl:insert>
</fd:ModifyOrderController>