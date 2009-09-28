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
<tmpl:insert template='/common/template/giftcard.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - User Name, Password, & Contact Info</tmpl:put>
    <tmpl:put name='content' direct='true'>


<%


FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();

String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
%>
<fd:GetOrder id='cart' saleId='<%=orderNumber%>'>

<%ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) cart.getPaymentMethod();

String pymtDetailWidth="630";

String lineWidth = "290";

int idx = 0;
%>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=pymtDetailWidth%>">
<tr>
	<td colspan="6"class="text11">
		<span class="title18">Thank you for buying Gift Cards.</span><br>
		<br>
		<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0" vspace="8"><br><br>
	</td>
</tr>

<TR VALIGN="TOP">
    <TD WIDTH="290">
        <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="290">
        <TR VALIGN="TOP">
            <TD WIDTH="290" COLSPAN="2"><span class="title18"><b>PLEASE NOTE:</b></span><BR>
            It may take up to <b>TWO HOURS OR MORE</b> to activate your Gift Cards.  Thank you for your patience.<BR><BR>
            We will send <b>confirmation to you via email</b> once your newly purchased Gift Cards are active.<br><br>
            <a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>">
            View this order (and download printable PDFs of your gifts) in Your Account.</a></TD>
        </TR>
        <TR VALIGN="TOP">
            <TD WIDTH="10"><BR></TD>
            <TD WIDTH="280">
            <font class="space4pix"><br></font>
            <FONT CLASS="text12">
     </TD>
        </TR>
        </TABLE>
    </TD>
    <TD valign="top" align="CENTER" width="40">
    	 	<img src="/media_stat/images/layout/cccccc.gif" width="1" height="280"><br>
	    </td>
    <TD WIDTH="300">
        <img src="/media_stat/images/navigation/payment_info.gif" WIDTH="91" HEIGHT="9" border="0" alt="PAYMENT INFO"><BR>
        <IMG src="/media_stat/images/layout/999966.gif" WIDTH="<%=lineWidth%>" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
        <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="300">
        <TR VALIGN="TOP">
            <TD WIDTH="10"><BR></TD>
            <TD WIDTH="300">
            <font class="space4pix"><br></font>
            <FONT CLASS="text12">
            <%if(cart.getCustomerCreditsValue() > 0) { %>
            	<b>FreshDirect Store Credit:</b><BR>
            	<font class="space4pix"><br></font>
		<%=currencyFormatter.format(cart.getCustomerCreditsValue())%><BR>
            	<br><br>
            <% } %>
            <b>Order Total:</b><BR>
            <font class="space4pix"><br></font>
            <%=currencyFormatter.format(cart.getTotal())%><BR>
            <br><br>
			<b>Credit Card</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<BR>
            <font class="space4pix"><br></font>
            
            <font class="space4pix"><br></font>
	                <%= paymentMethod.getName()%><BR>
	                <%= paymentMethod.getCardType() %> - <%= paymentMethod.getMaskedAccountNumber() %><br>
            <BR>
            
            
            
            <% if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {%>
	                <b>Billing Address:</b><BR>
	                <font class="space4pix"><br></font>
	                <%= paymentMethod.getName() %><BR>
	                <%= paymentMethod.getAddress1() %><% if(paymentMethod.getApartment()!=null && paymentMethod.getApartment().trim().length()>0) { %>, Apt. <%=paymentMethod.getApartment()%><% } %><BR>
	                <% if(paymentMethod.getAddress2()!=null && paymentMethod.getAddress2().trim().length()>0) { %>
	                <%=paymentMethod.getAddress2()%><BR>
	                <%}%>
	                <%= paymentMethod.getCity() %>, <%= paymentMethod.getState() %> <%= paymentMethod.getZipCode() %><BR><BR>
	                <%}%>
	                <%
	                if(EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())){%>
	                    <b>Billing Reference/Client Code:</b><BR>
	                    <font class="space4pix"><br></font>
	                    <%= paymentMethod.getBillingRef() %><br>
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
 <IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"><br>
<IMG src="/media_stat/images/layout/cccccc.gif" width="693" height="1"><br>
<br /><br />
<% 
//for display of recipient number
int indx = 1;
FDRecipientList recipients = cart.getGiftCardRecipients();
%>

 
<table width="675" cellspacing="0" cellpadding="0" border="0" valign="middle">
		<tr>
			<td><span class="title18"><b>RECIPIENT LIST FOR ORDER <font color="#FF9933">#<%=orderNumber%></font></b></span><br /><br />
				<a href="<%=response.encodeURL("/your_account/gc_order_details.jsp?orderId="+orderNumber)%>">
            <span class="title12"><b>View this order (and download printable PDFs of your gifts) in Your Account.</b></span></a>
			</td>
		</tr>
</table>

<table class="recipTable">
		<tr>
			<td><div class="recipAmount">Amount</div></td>
		</tr>
			<%
			ListIterator i = recipients.getRecipents().listIterator();
			while(i.hasNext()) {
				ErpRecipentModel erm = (ErpRecipentModel)i.next();
			%>
		<tr>
			<td>
				<div class="recipRow" id="<%=erm.getSale_id()%>Row">
					<div class="recipNumber" id="<%=erm.getSale_id()%>Number"><%= indx %>.&nbsp;</div>
					<div class="recipName" id="<%=erm.getSale_id()%>Name"><%= erm.getRecipientName()%></div>

					<div class="recipAmount" id="<%=erm.getSale_id()%>Amount">$<%= erm.getFormattedAmount() %></div>

					
				</div>
			<td>
		</tr>
			<%
				indx++;
			}
			%>

</table>
<table width="675" cellspacing="0" cellpadding="0" border="0" valign="middle" >
		<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		</tr>

        <tr>
            <td class="recipTotal">TOTAL: <%=  recipients.getFormattedSubTotal() %></td>
        </tr>
</table>

</fd:GetOrder>
</tmpl:put>
</tmpl:insert>
