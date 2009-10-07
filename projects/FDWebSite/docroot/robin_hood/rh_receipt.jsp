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
<tmpl:insert template='/common/template/robinhood.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Donation - Details</tmpl:put>
    <tmpl:put name='content' direct='true'>


<%
//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/robin_hood/rh_receipt.jsp");
        request.setAttribute("listPos", "ReceiptBotLeft,ReceiptBotRight,SystemMessage");
%>
<%


FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getRobinHoodSkucode());
ProductModel productModel = ContentFactory.getInstance().getProduct(FDStoreProperties.getRobinHoodSkucode());
Integer totalQuantity = user.getDonationTotalQuantity();
	

String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
%>
<fd:GetOrder id='cart' saleId='<%=orderNumber%>'>

<%ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) cart.getPaymentMethod();
double totalPrice = cart.getSubTotal();
String pymtDetailWidth="630";

String lineWidth = "290";
FDCartLineI cartLine = cart.getOrderLine(0);
int idx = 0;
%>

<div class="rhContactPrefsBox">
		<div style="text-align: left;" class="rhContactPrefsBoxContent" id="rhContactPrefsBox">
			<!-- <img src="/media_stat/images/giftcards/your_account/resend_hdr.gif" width="169" height="16" alt="Resend Gift Card" /> -->Contact Preferences<br />
			Please select one.
			<a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/donation/robinhood/close_x.gif" width="50" height="11" alt="close" border="0" style="float: right;" /></a>
			<br />
			<br /><img src="/media_stat/images/layout/cccccc.gif" width="320" height="1" border="0"><br /><br />
			<table border="0" cellspacing="0" cellpadding="2" width="100%">
				<tr>
					<td valign="top"><input type="radio" name="optInd" id="rhOptIn" value="rhOptIn" checked /></td>
					<td align="left">
						<b>Yes, I authorize FreshDirect to share my personal info with Robin Hood.</b><br /><br />
						Leave checked in order to receive a letter from Robin Hood confirming your tax-deductible gift.
					</td>
				</tr>
				<tr>
					<td><img colspan="2" src="/media_stat/images/layout/clear.gif" width="8" height="11" border="0" alt="" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" name="optInd" id="rhOptOut" value="rhOptOut" /></td>
					<td align="left">
						<b>No, please do not share my personal info with Robin Hood.</b><br />
						You will not receive a tax deduction letter.
					</td>
				</tr>
				<tr>
					<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="8" height="11" border="0" alt="" /></td>
				</tr>
				<tr>
					<td colspan="2" style="padding-left: 20px;"><a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/donation/robinhood/cancel_btn.gif" width="47" height="20" alt="cancel" border="0" /></a>
					&nbsp;<a href="#" onclick="rhContactPrefsSave(); return false;"><img src="/media_stat/images/donation/robinhood/save_btn.gif" width="69" height="20" alt="save" border="0" /></a></td>
				</tr>
			</table>
			<div id="rhContactPrefsBoxErr">&nbsp;</div>
		</div>
	</div>

<table border="0" cellspacing="0" cellpadding="0" width="<%=pymtDetailWidth%>">
	<tr>
		<td colspan="6"class="text11">
			<span class="title18">Thank you for your donation.</span><br>
			<br>
			<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0" vspace="8"><br><br>
		</td>
	</tr>
	<tr>
		<td colspan="6"class="text11">
			<span class="title18"><span class="title18or">ORDER INFORMATION</span> for ORDER NUMBER <%= orderNumber%></span><br>
			<br>
			 <img src="/media_stat/images/layout/clear.gif" width="1" height="1"><br><br><br>
		</td>
	</tr>
	<tr valign="top">
    <td width="320">
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
					<b><%=productModel.getFullName() %></b> Subtotal&nbsp;(<%=cartLine.getOrderedQuantity()%>&nbsp;Meals):
				</td>
				<td style="padding: 2px;" width="70" align="right" valign="bottom"><%=JspMethods.currencyFormatter.format(cart.getSubTotal())%></td>
			</tr>
			<tr>
				<td colspan="2" style="padding: 2px;" align="left" class="text13bold">
					<span>TOTAL:</span> &nbsp;<%=JspMethods.currencyFormatter.format(totalPrice)%></td>
				<td style="padding: 2px;" width="70" align="left" class="text13bold"></td>
			</tr>
			<tr>
				<td colspan="3" style="padding: 2px;">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0" alt="" /><br />
				</td>
			</tr>
		</table>
        <table cellpadding="0" cellspacing="0" border="0" width="320">
			<TR VALIGN="TOP">
				<TD WIDTH="320" COLSPAN="2">
			   Thank you for your donation to Robin Hood. Your gift will help feed New York City's hungry families during the holiday season.<br><br>
				You may view this donation in <a href="<%=response.encodeURL("/your_account/order_history.jsp")%>">
			  Your Orders.</a><br/><br/>
				
				FreshDirect and Robin Hood respect your privacy. To view of change your contact preferences (and indicate whether you'd like to receive a letter that confirms your tax-deductible gift), please <a href="#" onclick="rhContactPrefsShow(); return false;">click here</a>.</td>
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


<table width="680" cellspacing="0" cellpadding="0" border="0">

	<tr align="center">
			<td class="text11bold" align="center">&nbsp;&nbsp;&nbsp;&nbsp;Quantity<br/></td>
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
			<img src="/media_stat/images/donation/robinhood/landing/color_swatch_F0F0E7.gif" width="1" height="8" border="0"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; FRESHDIRECT
			</td>
	</tr>
		<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		</tr>
		
	<tr>
		
			<td class="text11bold" align="center"><%= cartLine.getOrderedQuantity() %></td>
			<td style="padding-left:30px;" class="text11bold" align="center"><%=productModel.getFullName() %></td>
			<td></td>
			<td align="center"><%= JspMethods.currencyFormatter.format( productInfo.getDefaultPrice() ) %>/<%= productInfo.getDefaultPriceUnit().toLowerCase() %></td>
			<td></td>
			<td align="center"  style="padding-left:4px;" class="text11bold"> <%= JspMethods.currencyFormatter.format( cart.getSubTotal() ) %>
		
		

	</tr>
	<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
	</tr>
	

</table> 

<table width="680" cellspacing="0" cellpadding="0" border="0">
	<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		</tr>
	<tr>
		    <td colspan="4"><img height="1" width="170" src="/media_stat/images/layout/clear.gif"/></td>
			<td align="right" class="orderTotal" colspan="2">
			<b>ORDER TOTAL:&nbsp;<%= JspMethods.currencyFormatter.format( cart.getSubTotal() ) %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
    </tr>

</table>

<table width="680" cellspacing="0" cellpadding="0" border="0">
		
		
        <% if (FDStoreProperties.isAdServerEnabled()) { %>
			<tr><td><br><br></td></tr>
			<tr><td bgcolor="#ccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
			<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"></td></tr>
			<tr><td>
			<table width="630" cellpadding="0" cellspacing="0">
			<tr><td width="50%" style="border-right: solid 1px #CCCCCC; padding-right: 10px;" align="center">
			<SCRIPT LANGUAGE=JavaScript>
					<!--
					OAS_AD('ReceiptBotLeft');
					//-->
			</SCRIPT>
			</td>
			<td width="50%" style="padding-left: 10px;" align="center">
			<SCRIPT LANGUAGE=JavaScript>
					<!--
					OAS_AD('ReceiptBotRight');
					//-->
			</SCRIPT>
			</td>		
			</table><br>
			</td></tr>
		<% } %>
</table>

</fd:GetOrder>
</tmpl:put>
</tmpl:insert>
