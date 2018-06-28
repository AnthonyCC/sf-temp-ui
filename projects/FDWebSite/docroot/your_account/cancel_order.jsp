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
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
	final int W_YA_CANCEL_ORDER = 605; //expanded page dimensions

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
  <temp:put name="extraCss" direct="true">
	<jwr:style src="/your_account.css" media="all"/>
  </temp:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Cancel Order</tmpl:put> --%>
    <tmpl:put name='content' direct='true'>
    	<%=mobWeb?"<br>": "" %>
		<table class="text-align-center cancel-order-table" style="width: <%= (mobWeb) ? "100%" : W_YA_CANCEL_ORDER+"px" %>;" border="0" cellpadding="0" cellspacing="0" >
		<%
			FDOrderI cartOrOrder = FDCustomerManager.getOrder(orderId);
			
			if (allowCancelOrder.booleanValue() && cartOrOrder != null) {
								
				//
				// get delivery info
				//
				ErpAddressModel dlvAddress = cartOrOrder.getDeliveryAddress();
				SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE M/d/yy");
				SimpleDateFormat monthDayFormatter = new SimpleDateFormat("M/d");
				String fmtDlvDateTime = dateFormatter.format(cartOrOrder.getDeliveryReservation().getStartTime());
				String deliveryMonthDay = monthDayFormatter.format(cartOrOrder.getDeliveryReservation().getStartTime());
				Calendar dlvStart = Calendar.getInstance();
				dlvStart.setTime(cartOrOrder.getDeliveryReservation().getStartTime());
				Calendar dlvEnd =   Calendar.getInstance();
				dlvEnd.setTime(cartOrOrder.getDeliveryReservation().getEndTime());
				int startHour =  dlvStart.get(Calendar.HOUR_OF_DAY);
				int endHour = dlvEnd.get(Calendar.HOUR_OF_DAY); 
				
				String sStartHour = startHour==12? "NOON" : (startHour>12 ? ""+(startHour-12) : ""+startHour);
				String sEndHour = endHour==0 ? "12 AM" : (endHour==12 ? "NOON" : (endHour>12 ? (endHour-12)+" PM" : endHour+" AM"));
				
				//
				// get order line info
				//
			%>
				<tr>
					<td class="title19" colspan="3">
						<% if (mobWeb) { %>
							Cancel <%=(cartOrOrder.getStandingOrderName() != null? cartOrOrder.getStandingOrderName() : "Order") %>
						<% } else { %> 
							Cancel Order<%=(cartOrOrder.getStandingOrderName() != null? ":" + cartOrOrder.getStandingOrderName() : "") %>
						<% } %>
					</td>
				</tr>
					<td class="text19" colspan="3">
						<%=deliveryMonthDay %> Delivery, Order # <%=orderId %>
					</td>
				</tr>
				<tr>
					<td class="title15" colspan="3">
						<div class="delivery-info-container" style="width:<%=mobWeb? "100%": "605px"%>" >
							Currently Scheduled:<%=mobWeb?"<br>":" "%><%=fmtDlvDateTime%> @ <%=sStartHour%>-<%=sEndHour%>
							<br>
							Estimated Total: <%= currencyFormatter.format(cartOrOrder.getTotal()) %>
						</div>
					</td>
				</tr>
				<tr>
					<td class="text15<%=mobWeb?" padding-left-10 padding-right-10": "" %>" colspan="3">
					You are about to cancel this order. 
					If you cancel it, you will not receive a delivery and your account will not be charged. We will save the items from this order in "Reorder".
					</td>
				</tr>
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>
					<td class="text15<%=mobWeb?" padding-left-10 padding-right-10": "" %>" colspan="3">
						For full details of our cancellation policy, see our <a href="javascript:popup('/help/faq_index.jsp?show=shopping#question7','large')">Shopping FAQ</a>
					</td>
				</tr>
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>
					<td class="buttons">
						<span class="<%=mobWeb?"block padding-left-10 padding-right-10": "inline" %>">
							<a class="cssbutton orange <%=mobWeb?"full-width":""%>" href="order_details.jsp?orderId=<%= orderId %>">Do Not Cancel Order<span class="offscreen"> number <%= orderId %></span></a>
						</span>
						
						<form class="cancel-order-form <%=mobWeb?"block padding-left-10 padding-right-10": "inline" %>" name="cancel_order" method="POST" action="/your_account/cancel_order.jsp">
							<input type="hidden" name="action" value="cancel" />
							<input type="hidden" name="orderId" value="<%= orderId %>" />
							<button type="submit" class="cssbutton red transparent <%=mobWeb?"full-width":""%>">Cancel Order<span class="offscreen"> number <%= orderId %></span></button>
						</form>
						
					</td>
				</tr>
			<% } %>
		</table>
		<div class="having-problem-container padding-left-10 padding-right-10">
			<b>Having Problems?</b><br /><%@ include file="/includes/i_footer_account.jspf" %>
		</div>
	</tmpl:put>
</tmpl:insert>
</fd:ModifyOrderController>