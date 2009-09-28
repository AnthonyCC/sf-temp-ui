<%@ page import='java.text.*, java.util.*' %>
<%@ page import='com.freshdirect.delivery.depot.*' %>
<%@ page import='com.freshdirect.payment.EnumPaymentMethodType' %>
<%@ page import='com.freshdirect.customer.EnumUnattendedDeliveryFlag' %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.giftcard.ErpRecipentModel' %>
<%@ page import='com.freshdirect.fdstore.promotion.PromotionI' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.common.pricing.Discount' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ page import='java.util.Calendar' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	java.text.SimpleDateFormat cutoffDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE, MM/d/yy");
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM.dd.yyyy");
	SimpleDateFormat deliveryDateFormatter = new SimpleDateFormat("EEEE, MMM d yyyy");
	SimpleDateFormat deliveryTimeFormatter = new SimpleDateFormat("h:mm a");
	SimpleDateFormat monthYearFormatter = new SimpleDateFormat("MM.yyyy");
	DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<tmpl:insert template='/template/top_nav.jsp'>
    <tmpl:put name='title' direct='true'>Gift Card : Receipt</tmpl:put>
    <tmpl:put name='content' direct='true'>

		<jsp:include page="/includes/giftcard_nav.jsp"/>
		
		<%
			FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			FDIdentity identity  = user.getIdentity();
			String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
		%>
		
		<fd:GetOrder id='cart' saleId='<%=orderNumber%>'>
			<%
				ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) cart.getPaymentMethod();
				String pymtDetailWidth="630";
				String lineWidth = "290";
				int idx = 0;
			%>

			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
				<tr>
					<td>
						<table border="0" cellspacing="0" cellpadding="0" width="<%=pymtDetailWidth%>">
							<tr>
								<td colspan="6"class="text11">
									<span class="title18">Thank you for buying Gift Cards.</span><br /><br />
									<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0" vspace="8"><br /><br />
								</td>
							</tr>
							<tr valign="top">
								<td width="290">
									<table cellpadding="0" cellspacing="0" border="0" width="290">
										<tr valign="top">
											<td width="290" colspan="2">
												<span class="title18"><b>PLEASE NOTE:</b></span><br />
												It may take up to <b>TWO HOURS OR MORE</b> to activate your Gift Cards.  Thank you for your patience.<br /><br />
												We will send <b>confirmation to you via email</b> once your newly purchased Gift Cards are active.
											</td>
										</tr>
										<tr valign="top">
											<td width="10"><br /></td>
											<td width="280">
												<font class="space4pix"><br /></font>
											</td>
										</tr>
									</table>
								</td>
								<td valign="top" align="center" width="40">
									<img src="/media_stat/images/layout/cccccc.gif" width="1" height="280"><br />
								</td>
								<td width="300">
									<img src="/media_stat/images/navigation/payment_info.gif" width="91" height="9" border="0" alt="PAYMENT INFO"><br />
									<img src="/media_stat/images/layout/999966.gif" width="<%=lineWidth%>" height="1" border="0" vspace="3"><br />
									<table cellpadding="0" cellspacing="0" border="0" width="300">
										<tr valign="top">
											<td width="10"><br /></td>
											<td width="300">
												<font class="space4pix"><br /></font>
												<font class="text12"><b>Order Total:</b><br />
												<font class="space4pix"><br /></font>
												<%=currencyFormatter.format(cart.getTotal())%><br /><br /><br />
												<b>Credit Card</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />
												<font class="space4pix"><br /></font>
												<font class="space4pix"><br /></font>
												<%= paymentMethod.getName()%><br />
												<%= paymentMethod.getCardType() %> - <%= paymentMethod.getMaskedAccountNumber() %><br /><br />
												<% if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) { %>
												<b>Billing Address:</b><br />
												<font class="space4pix"><br /></font>
												<%= paymentMethod.getName() %><br />
												<%= paymentMethod.getAddress1() %><% if (paymentMethod.getApartment()!=null && paymentMethod.getApartment().trim().length()>0) { %>, Apt. <%=paymentMethod.getApartment()%><% } %><br />
												<% if(paymentMethod.getAddress2()!=null && paymentMethod.getAddress2().trim().length()>0) { %>
												<%=paymentMethod.getAddress2()%><br />
												<%}%>
												<%= paymentMethod.getCity() %>, <%= paymentMethod.getState() %> <%= paymentMethod.getZipCode() %><br /><br />
												<%}%>
												<% if (EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) { %>
												<b>Billing Reference/Client Code:</b><br />
												<font class="space4pix"><br /></font>
												<%= paymentMethod.getBillingRef() %><br />
												<% } %></font>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>  
						<br />
						<img src="/media_stat/images/layout/clear.gif" width="1" height="1"><br />
						<img src="/media_stat/images/layout/cccccc.gif" width="693" height="1"><br />
						<br /><br />
						<% 
							//for display of recipient number
							int indx = 1;
							FDRecipientList recipients = cart.getGiftCardRecipients();
						%>
						<table width="675" cellspacing="0" cellpadding="0" border="0" valign="middle">
							<tr>
								<td>
									<span class="title18"><b>RECIPIENT LIST FOR ORDER <font color="#FF9933">#<%=orderNumber%></font></b></span>
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
						<table width="675" cellspacing="0" cellpadding="0" border="0" valign="middle">
							<tr>
								<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
							</tr>
							<tr>
								<td class="recipTotal">Total <%=currencyFormatter.format(cart.getTotal())%></td>
								<td width="20"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fd:GetOrder>
	</tmpl:put>
</tmpl:insert>

