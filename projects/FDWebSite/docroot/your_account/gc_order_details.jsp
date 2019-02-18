<%@ page import = "com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import ='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.content.ContentFactory' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.giftcard.*"%>
<%@ page import='com.freshdirect.fdstore.giftcard.*' %>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.text.*" %>
<%@ page import='java.util.List.*' %>
<%@ page import="java.util.Locale"%>

<%@ taglib uri='template' prefix='tmpl' %> 
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_GC_ORDER_DETAILS = 970;
%>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" />
<%  String orderId = request.getParameter("orderId"); %>
<fd:ModifyOrderController orderId="<%= orderId %>" result="result" successPage='<%= "/your_account/order_details.jsp?orderId=" + orderId %>'>
<tmpl:insert template='/common/template/dnav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Order Details"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Order Details</tmpl:put>
    <tmpl:put name='content' direct='true'>

<%
    NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
    SimpleDateFormat dateOnlyFormatter = new SimpleDateFormat("MM/dd/yy");
    //String orderId = request.getParameter("orderId");
    
    FDIdentity identity  = user.getIdentity();
    ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
%>
	<div class="gcResendBox">
		<div style="text-align: left;" class="gcResendBoxContent" id="gcResendBox"><form fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter>
			<span class="title18">Resend Gift Card</span>
			<a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/close.gif" width="50" height="11" alt="close" border="0" style="float: right;" /></a>
			<br />If your Recipient never received their Gift Card, you may resend it by clicking Resend Now. If there was an error in the Recipient's email address, or to use a new one, edit the email field.
			<br /><br /><img src="/media_stat/images/layout/cccccc.gif" alt="" width="390" height="1" border="0" /><br /><br />
			<input type="hidden" id="gcSaleId" value="" />
			<input type="hidden" id="gcCertNum" value="" />
			<input type="hidden" id="gcType" value="" />
			<table border="0" class="accessibilitySpacing" cellspacing="0" cellpadding="4" width="100%">
				<tr>
					<td width="130" align="right"><label for=""gcResendRecipName"" >Recipient Name:</label></td>
					<td><input id="gcResendRecipName" value="" /></td>
				</tr>
				<tr valign="middle">
					<td width="130" align="right"><label for="gcResendRecipEmail">Recipient Email (edit):</label></td>
					<td><input id="gcResendRecipEmail" value="" /></td>
				</tr>
				<tr>
					<td width="130" align="right"><label for="gcResendRecipAmount">Amount:</label></td>
					<td><span class="text14" id="gcResendRecipAmount"><!--  --></span></td>
				</tr>
				<tr>
					<td width="130" align="right"><label for="gcResendRecipMsg">Personal Message:</label></td>
					<td><textarea id="gcResendRecipMsg"></textarea></td>
				</tr>
				<tr><td colspan="2">&nbsp;&nbsp;</td></tr>
				<tr>
					<td width="150" align="right"><button class="cssbutton transparent small green" onclick="Modalbox.hide(); return false;">CANCEL</button></td>
					<td><button class="cssbutton small green space"  onclick="recipResendEmail(); return false;">RESEND</button></td>
				</tr>
			</table>
			<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /><br />
			PLEASE NOTE: You will NOT receive a confirmation email for resent email Gift Cards.<br /><br />
			<div id="gcResendErr">&nbsp;</div>
		</form></div>
	</div>
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
        // get payment info
        //
        ErpPaymentMethodI paymentMethod =(ErpPaymentMethodI) cart.getPaymentMethod();
        //
        // get order line info
        //
        boolean isSubmitted = cart.getOrderStatus().equals(EnumSaleStatus.SUBMITTED) || cart.getOrderStatus().equals(EnumSaleStatus.AUTHORIZED);
%>
<!-- error message handling here -->

<% if (cart.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) {
        String errorMsg= "Pending Order: Please contact us at "+user.getCustomerServiceContact()+" as soon as possible to reschedule delivery.";
%>
<%@ include file="/includes/i_error_messages.jspf" %>
<% } %>

<table width="<%= W_YA_GC_ORDER_DETAILS %>" align="center" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td class="text11">
        <font class="title18">Order # <%= orderId %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </font> &nbsp;&nbsp;&nbsp;<br>
        <%-- Having trouble, send an e-mail to <A HREF="mailto:accounthelp@freshdirect.com">accounthelp@freshdirect.com</A> or call 1-866-2UFRESH.--%>
    </td>
    <%
    boolean hasCredit = false;
    Collection<ErpComplaintModel> comp = cart.getComplaints();
    if (comp != null) {
            ErpComplaintModel c = null;
            for (Iterator<ErpComplaintModel> i=comp.iterator(); i.hasNext(); ) {
                c = (ErpComplaintModel)i.next();

                if (c != null && EnumComplaintStatus.APPROVED.equals(c.getStatus())){
                    hasCredit = true;
                }
            }
        }
    if (hasCredit) { %>
    <td align="right" class="text11"><i>Credit was issued for this order.</i></td>
    <% } %>
</tr>
</table>
<IMG src="/media_stat/images/layout/clear.gif" alt="" width="1" HEIGHT="8" border="0"><br>
<IMG src="/media_stat/images/layout/ff9933.gif" alt="" width="<%= W_YA_GC_ORDER_DETAILS %>" HEIGHT="1" border="0"><br>
<IMG src="/media_stat/images/layout/clear.gif" alt="" width="1" HEIGHT="15" border="0"><br>
<%@ include file="/includes/your_account/i_gc_order_detail_payment.jspf" %><br>
<IMG src="/media_stat/images/layout/ff9933.gif" alt="" width="<%= W_YA_GC_ORDER_DETAILS %>" HEIGHT="1" border="0"><br>
<IMG src="/media_stat/images/layout/clear.gif" alt="" width="1" HEIGHT="4" border="0"><br><FONT CLASS="space4pix"><br></FONT>

<%  } %>
<br>
<% 
//for display of recipient number
int indx = 1;
FDRecipientList recipients = cart.getGiftCardRecipients();
%>

 
<table width="<%= W_YA_GC_ORDER_DETAILS %>" cellspacing="0" cellpadding="0" border="0" valign="middle" >
		<tr>
			<td><span class="title18"><b>RECIPIENT LIST FOR ORDER <font color="#FF9933">#<%=orderId%></font></b></span><br /><br />
			</td>
		</tr>
</table>

<table class="gcYourActTableList">
    <tr>
        <td colspan="7"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
    </tr>
    <tr class="th">
        <td width="85">Certificate #</td>
        <td width="100">Date Purchased</td>
        <td width="80">Gift Amount</td>
        <td width="80">Card Type</td>
        <td>Recipient</td>
        <td width="70">Status</td>
        <td width="60">&nbsp;</td>
        <td width="60">&nbsp;</td>
    </tr>
    <%
                DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
                if(cart.isPending()) {
                    Iterator<RecipientModel> i = recipients.getRecipients().iterator();
                    while(i.hasNext()) {
                        ErpRecipentModel erm = (ErpRecipentModel)i.next();
			%>
						<tr>
							<td>&nbsp;</td>
							<td><%= dateFormatter.format(cart.getDatePlaced()) %></td>
							<td>$<%= erm.getFormattedAmount() %></td>    
                            <%  
                                String cardType = "";
                                try {
                                    cardType = ContentFactory.getInstance().getDomainValueById(erm.getTemplateId()).getLabel();
                                }catch(Exception e){
                                    cardType = "Unknown";
                                }
                            %>
                            <td><%= cardType %></td>    

							<td><%= erm.getRecipientName() %></td>
							<td>In Process</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
			<%
					indx++;
				}
            } else {
                ErpGiftCardDlvConfirmModel model = cart.getGCDeliveryInfo();
                Iterator<ErpGCDlvInformationHolder> j = model.getDlvInfoTranactionList().iterator();
                while(j.hasNext()) {
					ErpGCDlvInformationHolder recipient = (ErpGCDlvInformationHolder)j.next();
	                ErpRecipentModel recModel =  cart.getGCResendInfoFor(recipient.getGiftCardId());
	                if(recModel != null) {
	                    //Set the latest resend info.
	                    recipient.setRecepientModel(recModel);
	                }
			%>
            <tr>
                <td><%= recipient.getCertificationNumber() != null ? recipient.getCertificationNumber() : "" %></td>
                <td><%= dateFormatter.format(cart.getDatePlaced()) %></td>
                <td>$<%= recipient.getRecepientModel().getFormattedAmount() %></td>    
                <%  
                    String cardType = "";
                    try {
                        cardType = ContentFactory.getInstance().getDomainValueById(recipient.getRecepientModel().getTemplateId()).getLabel();
                    }catch(Exception e){
                        cardType = "Unknown";
                    }
                %>
                <td><%= cardType %></td>    

                <td><%= recipient.getRecepientModel().getRecipientName() != null ? recipient.getRecepientModel().getRecipientName() : "" %></td>
                <%
                    String status = "";
                    boolean isPending = false;
                    if (recipient.getCertificationNumber() == null) {
                        status = "In Process";
                        isPending = true;
                    } else {
                        if(recipient.getRecepientModel().getDeliveryMode().equals(EnumGCDeliveryMode.PDF)) status = "Printed";
                        else status = "Sent";
                        FDGiftCardI gc =user.getGiftCardList().getGiftCard(recipient.getCertificationNumber());
                        if( gc!= null && gc.getBalance() != recipient.getRecepientModel().getAmount()) status = "Redeemed";
                    }
                    
                %>
                <td><%= status %></td>
                <% if(!isPending) { %>
                    <td><a href="#" onClick="recipResendFetch('<%= cart.getErpSalesId() %>','<%= recipient.getCertificationNumber() %>'); return false;"><%= status.equals("Printed") ? "Send" : "Resend" %></a></td>
                    <td><a href="/gift_card/postbacks/pdf_gen.jsp?saleId=<%= cart.getErpSalesId() %>&certNum=<%= recipient.getCertificationNumber() %>" >View/Print</a></td>
                <% } else { %>    
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                
                <% } %>
            </tr>
			<%
				indx++;
			}
            }       
            %>            
</table>

</fd:GetOrder>

</tmpl:put>
</tmpl:insert>
</fd:ModifyOrderController>


