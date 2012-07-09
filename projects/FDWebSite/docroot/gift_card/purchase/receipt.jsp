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
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Locale" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ page import='com.freshdirect.cms.*'%>
<%@ page import='com.freshdirect.cms.application.CmsManager'%>
<%@ page import='com.freshdirect.cms.fdstore.FDContentTypes'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>

<% //expanded page dimensions
final int W_GIFTCARD_RECEIPT_TOTAL = 970;
%>

<%!
java.text.SimpleDateFormat cutoffDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE, MM/d/yy");
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/giftcard.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - User Name, Password, & Contact Info</tmpl:put>
    <tmpl:put name='content' direct='true'>


<%
//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/gift_card/purchase/receipt.jsp");
        request.setAttribute("listPos", "ReceiptBotLeft,ReceiptBotRight,SystemMessage");
%>
<%
FDSessionUser sessionuser = (FDSessionUser)session.getAttribute(SessionName.USER);
EnumGiftCardType gcType = sessionuser.getGiftCardType();



String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
%>
<fd:GetOrder id='cart' saleId='<%=orderNumber%>'>

<%
	FDRecipientList recipients = cart.getGiftCardRecipients();

	String _donOrgName = "";
	String _donOrgLogo = "";
	if(gcType != null && EnumGiftCardType.DONATION_GIFTCARD.equals(gcType)) {
		List<DonationOrganization> donationOrgList = new ArrayList<DonationOrganization>();
		CmsManager manager = CmsManager.getInstance();	
		ContentKey contentKey = new ContentKey(FDContentTypes.FDFOLDER, "donationOrganizationList");
		ContentNodeI contentNode = manager.getContentNode(contentKey);

		if(null != contentNode){
			Set subNodes = contentNode.getChildKeys();
			for (Object object : subNodes) {
				ContentKey subContentKey = (ContentKey) object;
				if(null != subContentKey){
					ContentType contentType = subContentKey.getType(); 
					if(FDContentTypes.DONATION_ORGANIZATION.equals(contentType)){
						DonationOrganization _org = new DonationOrganization(subContentKey);
						donationOrgList.add(_org);
					}
				}
			}
			for (DonationOrganization _org : donationOrgList) {			
				Iterator<RecipientModel> i = recipients.getRecipients().listIterator();
				while(i.hasNext()) {
					ErpRecipentModel erm = (ErpRecipentModel)i.next();
					if(erm.getRecipientEmail().equalsIgnoreCase(_org.getEmail())){
						_donOrgName = _org.getOrganizationName();
						_donOrgLogo = (_org.getOrganizationLogoSmall() != null) ? _org.getOrganizationLogoSmall().toHtml() : ""; 
					}
				}
			}
		}
	}

	ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) cart.getPaymentMethod();

	String lineWidth = "290";

	int idx = 0;
%>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_GIFTCARD_RECEIPT_TOTAL%>">
<tr>
	<td colspan="6"class="text11">

		<% if(gcType != null && EnumGiftCardType.REGULAR_GIFTCARD.equals(gcType)) {%>
			<span class="title18">
			Thank you for buying Gift Cards.
			</span>
		<% } else if(gcType != null && EnumGiftCardType.DONATION_GIFTCARD.equals(gcType)) {%>
			<table>
				<tr>
					<td><%= _donOrgLogo %></td>
					<td class="title18">Thank you for supporting <%= _donOrgName %>!</td>
				</tr>
			</table>
		<% }%>
		<img src="/media_stat/images/layout/ff9933.gif" width="<%=W_GIFTCARD_RECEIPT_TOTAL%>" height="1" border="0" vspace="8"><br><br>
	</td>

</tr>

<TR VALIGN="TOP">
    <TD WIDTH="<%=W_GIFTCARD_RECEIPT_TOTAL-340%>">
        <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=W_GIFTCARD_RECEIPT_TOTAL-340%>">
        <TR VALIGN="TOP">
            <TD WIDTH="<%=W_GIFTCARD_RECEIPT_TOTAL-340%>" COLSPAN="2"><span class="title18"><b>PLEASE NOTE:</b><br/></span><BR>
            It may take up to <b>TWO HOURS OR MORE</b> to activate your Gift Cards.  Thank you for your patience.<BR><BR>
            We will send <b>confirmation to you via email</b> once your newly purchased Gift Cards are active.<br><br>
            <a href="<%=response.encodeURL("/your_account/gc_order_details.jsp?orderId="+orderNumber)%>">
            View this order (and download printable PDFs of your gifts) in Your Account.</a></TD>
        </TR>
        <TR VALIGN="TOP">
            <TD WIDTH="10"><BR></TD>
            <TD WIDTH="<%=W_GIFTCARD_RECEIPT_TOTAL-350%>">
            <font class="space4pix"><br></font>
     </TD>
        </TR>
        </TABLE>
    </TD>
    <TD valign="top" align="CENTER" width="40">
    	 	<img src="/media_stat/images/layout/cccccc.gif" width="1" height="280"><br>
	    </td>
    <TD WIDTH="300">
        <img src="/media_stat/images/navigation/payment_info.gif" WIDTH="100" HEIGHT="15" border="0" alt="PAYMENT INFO"><BR>
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
	                <%= paymentMethod.getAddress1() %><% if(paymentMethod.getApartment()!=null && paymentMethod.getApartment().trim().length()>0) { %>, <% if(EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())){ %>Floor/Suite <% }else{ %>Apt. <% } %><%=paymentMethod.getApartment()%><% } %><BR>
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
<IMG src="/media_stat/images/layout/cccccc.gif" width="<%=W_GIFTCARD_RECEIPT_TOTAL%>" height="1"><br>
<br /><br />
<% 
//for display of recipient number
int indx = 1;
%>

 
<table width="<%=W_GIFTCARD_RECEIPT_TOTAL%>" cellspacing="0" cellpadding="0" border="0" valign="middle">
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
			Iterator<RecipientModel> i = recipients.getRecipients().listIterator();
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
<table width="<%=W_GIFTCARD_RECEIPT_TOTAL%>" cellspacing="0" cellpadding="0" border="0" valign="middle" >
		<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		</tr>

        <tr>
            <td class="recipTotal">TOTAL: $<%=  recipients.getFormattedSubTotal(gcType) %></td>
        </tr>
<% if (FDStoreProperties.isAdServerEnabled()) { %>
<tr><td><br><br></td></tr>
<tr><td bgcolor="#ccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"></td></tr>
<tr><td>
<table width="<%=W_GIFTCARD_RECEIPT_TOTAL%>" cellpadding="0" cellspacing="0">
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
