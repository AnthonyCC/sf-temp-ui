<%@ page import='java.util.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature" %>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
	final int W_YA_CANCEL_ORDER = 970; //expanded page dimensions

	String orderId = request.getParameter("orderId");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
	
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
	ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_nav.jsp";
	String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/your_account/cancel_order.jsp" : request.getAttribute("sitePage").toString();

	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
%>
<fd:ModifyOrderController orderId="<%=orderId %>" result="result" successPage='<%= "/your_account/cancel_order_confirm.jsp?orderId=" + orderId %>'>
<tmpl:insert template='<%= pageTemplate %>'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Cancel Order"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Cancel Order</tmpl:put>
    <tmpl:put name='content' direct='true'>
	    
		<table style="width: <%= (mobWeb) ? "100%" : W_YA_CANCEL_ORDER+"px" %>;" border="0" cellpadding="0" cellspacing="0">
		<%
			FDOrderI cartOrOrder = FDCustomerManager.getOrder(orderId);
			
			if (allowCancelOrder.booleanValue() && cartOrOrder != null) {
				
				StringBuffer custName = new StringBuffer(50);
				custName.append(customerModel.getFirstName());
				if (customerModel.getMiddleName()!=null && customerModel.getMiddleName().trim().length()>0) {
				    custName.append(" ");
				    custName.append(customerModel.getMiddleName());
				    custName.append(" ");
				}
				custName.append(customerModel.getLastName());
				
				//
				// get delivery info
				//
				ErpAddressModel dlvAddress = cartOrOrder.getDeliveryAddress();
				      SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MM/dd/yy");
				      String fmtDlvDateTime = dateFormatter.format(cartOrOrder.getDeliveryReservation().getStartTime()).toUpperCase();
				      Calendar dlvStart = Calendar.getInstance();
				      dlvStart.setTime(cartOrOrder.getDeliveryReservation().getStartTime());
				      Calendar dlvEnd =   Calendar.getInstance();
				      dlvEnd.setTime(cartOrOrder.getDeliveryReservation().getEndTime());
				int startHour =  dlvStart.get(Calendar.HOUR_OF_DAY);
				int endHour = dlvEnd.get(Calendar.HOUR_OF_DAY); 
				
				String sStartHour = startHour==12? "noon" : (startHour>12 ? ""+(startHour-12) : ""+startHour);
				String sEndHour = endHour==0 ? "12 am" : (endHour==12 ? "noon" : (endHour>12 ? (endHour-12)+" pm" : endHour+" am"));
				
				//
				// get payment info
				//
				ErpPaymentMethodI paymentMethod =(ErpPaymentMethodI) cartOrOrder.getPaymentMethod();
				
				//
				// get order line info
				//
			%>
				<tr>
					<td class="title18" colspan="3">Cancel <% if (cartOrOrder.getStandingOrderName() != null){ %><%= cartOrOrder.getStandingOrderName() %>, <%= cartOrOrder.getSODeliveryDate() %> Delivery, <% } %>Order # <%= orderId %> ?</td>
				</tr>
				<tr>
					<td style="width: 1%; max-width: 10px;"><img src="/media_stat/images/layout/clear.gif" height="6" alt="" /></td>
					<td style="width: <%= (mobWeb) ? "50%" : (W_YA_CANCEL_ORDER - 210)+"px" %>;"><img src="/media_stat/images/layout/clear.gif" height="6" alt="" /></td>
					<td style="width: <%= (mobWeb) ? "49%" : 200+"px" %>;"><img src="/media_stat/images/layout/clear.gif" height="6" alt="" /></td>
				</tr>
				<tr style="background-color: #FF9933;">
					<td>&nbsp;</td>
					<td class="text10w" colspan="1" height="16" valign="middle"><img src="/media_stat/images/template/youraccount/currently_scheduled.gif" width="124" height="8" border="0" alt="CURRENTLY SCHEDULED" vspace="2" align="absbottom"><%= (mobWeb) ? "<br />" : "" %>&nbsp;&nbsp;<%=fmtDlvDateTime%>@<%=sStartHour%>-<%=sEndHour%></td>
					<td align="right" class="text11wbold" valign="middle">Estimated Total: <%= currencyFormatter.format(cartOrOrder.getTotal()) %>&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td colspan="2" class="text13">
						<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10"><br />
						You are about to cancel this order. If you cancel it, you will not receive a delivery and your account will not be charged. We will save the items from this order in "Reorder."<br />
						<br />
						If you would like to change this order instead, <a href="/your_account/modify_order.jsp?orderId=<%= orderId %>">click here</a>.<br />
						<br />
						For full details of our cancellation policy, <a href="javascript:popup('/help/faq_index.jsp?show=shopping#question7','large')">click here</a>.<br />
						<img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" />
						<div align="center">
							<form name="cancel_order" method="POST" action="/your_account/cancel_order.jsp">
								<input type="hidden" name="action" value="cancel" />
								<input type="hidden" name="orderId" value="<%= orderId %>" />
								<button type="submit" class="imgButtonRed">cancel order</button>
							</form>
						</div>
						<img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" />
					</td>
				</tr>
				<tr class="NOMOBWEB"><td bgcolor="#CCCCCC" colspan="3"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%= W_YA_CANCEL_ORDER %>" height="1" border="0"></td></tr>
				<tr>
					<td colspan="3"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8"><br />
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
							    <td rowspan="2"><a href="order_details.jsp?orderId=<%= orderId %>"><img src="/media_stat/images/template/youraccount/cross.gif" border="0"></a></td>
							    <td><a href="order_details.jsp?orderId=<%= orderId %>"><img src="/media_stat/images/template/youraccount/do_not_cancel_order.gif" border="0" alt="Do not Cancel Order"></a></td>
							</tr>
							<tr><td>and deliver as originally specified</td></tr>
						</table>
					</td>
				</tr>
			<% } %>
			<tr>
				<td colspan="3"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="20"><br /><b>Having Problems?</b><br /><%@ include file="/includes/i_footer_account.jspf" %></td>
			</tr>
		</table>
	</tmpl:put>
</tmpl:insert>
</fd:ModifyOrderController>
