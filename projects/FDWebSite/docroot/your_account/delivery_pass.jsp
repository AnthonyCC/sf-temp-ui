<%@page import="com.freshdirect.fdstore.ewallet.EnumEwalletType"%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>
<%@ page import='com.freshdirect.common.customer.EnumCardType'%>
<%@ page import='com.freshdirect.common.context.MasqueradeContext' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import='com.freshdirect.deliverypass.EnumDlvPassStatus' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ page import='com.freshdirect.deliverypass.EnumDPAutoRenewalType' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo' %>
<%@ page import='com.freshdirect.deliverypass.DeliveryPassModel' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.storeapi.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import="java.util.Calendar" %>
<%@ page import='java.util.Date' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:CheckLoginStatus id="userdp" guestAllowed="false" recognizedAllowed="false" />

<%
	//expanded page dimensions
	final int W_YA_DELIVERY_PASS_TOTAL = 970;
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>
<%
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, userdp) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String template = "/common/template/dnav.jsp";
	if (mobWeb) {
		template = "/common/template/mobileWeb.jsp"; //mobWeb template
	}
%>

<tmpl:insert template='<%= template %>'>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Your Account - FreshDirect DeliveryPass</tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Account - FreshDirect DeliveryPass" pageId="delivery_pass"></fd:SEOMetaTag>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
<script>
$jq( document ).ready(function() {
	$jq(".dp-container").on("click", "[dp-learn-more]", function(){
		pop("/shared/template/generic_popup.jsp?contentPath=/media/editorial/picks/deliverypass/dp_tc.html&windowSize=large&name=Delivery Pass Information',400,560,alt='Delivery Pass Information");
	});
	$jq(".dp-container").on("click", "[autorenew-learn-more]", function(){
		pop('/about/aboutRenewal.jsp?sku=MKT9000222&amp;term=Six-Month Mid-Week');
	});
});
function deliveryPassAutoRenew(item){
	item.disabled = true;
	if(item.checked){
		dataString = "action=FLIP_AUTORENEW_ON";
	} else {
		dataString = "action=FLIP_AUTORENEW_OFF";
	}
	$jq(".dp-autorenew-error").addClass("dp-display-autorenew-loading").removeClass("dp-display-autorenew-error");
	$jq.ajax({
        url: '/api/expresscheckout/deliverypass',
	    data: dataString,
        type: 'POST',
        success: function() {
        	$jq(".dp-autorenew-error").removeClass("dp-display-autorenew-loading");
        	if(item.checked){
        		$jq(".dp-renew-date").addClass("dp-dispay-renew-date");
        	} else {
        		$jq(".dp-renew-date").removeClass("dp-dispay-renew-date");
        	}
        },
        error: function() {
        	$jq(".dp-autorenew-error").addClass("dp-display-autorenew-error").removeClass("dp-display-autorenew-loading");
        	if(item.checked){
         		item.checked = false;
         	} else {
         		item.checked = true;
         	}
        },
        complete: function() {
        	item.disabled = false;
        }
 	});
}
</script>
    	<% if(user.isDPFreeTrialOptInEligible()){ %>
    		<script>window.location = "/freetrial.jsp";</script>
    	<% } else if(!user.isDlvPassActive()){
    		if(user.getDpFreeTrialOptin() && !user.isDlvPassExpired()){ %>
    			<div class="dp-container">
    				<div class="dp-page-header">DeliveryPass</div>
    				<div class="dp-content">
    					<p>You have opted into the DeliveryPass Free Trial on <%= user.getDpFreeTrialOptinStDate() %></p>
    					<p>You will start receiving free deliveries on your next order.</p>
    					<p><a href="#" dp-learn-more>Learn more about FreshDirect DeliveryPass.</a></p>
    					<p><a href="/" class="dpn-success-start-shopping cssbutton cssbutton-flat green">Start Saving</a></p>
    				</div>
    			</div>
    		<% } else {%>
	    		<div class="dp-container">
	    			<div class="dp-page-header">DeliveryPass</div>
	    			<div class="dp-content">
    					<p>You are not currently a member.</p>
    					<p>DeliveryPass offers you a new way to save on delivery fees. Place as many orders as you'd like while your pass is active, all for a low, one-time fee for delivery.</p>
    					<p><a href="https://www.freshdirect.com/pdp.jsp?productId=mkt_dpass_auto14mo&catId=gro_gear_dlvpass">Sign up for DeliveryPass today!</a></p>
	    			</div>
	    		</div>
    		<% } %>
    	<% } else { %>
    		<fd:WebViewDeliveryPass id='viewContent'>
    		<div class="dp-container">
    			<div class="dp-page-header">DeliveryPass</div>
    			<div class="dp-content">
    			<div class="dp-plan">
						<div class="dp-header">Current Plan:</div>
						<div class="dp-plan-name"><%= viewContent.getPassName() %> </div>
					</div>
					<div class="dp-benefits">
						<div class="dp-header">Your Benefits:</div>
						<div class="dp-benefits-list-item">
							<div class="dp-benefits-list-icon"><img style="width: 36px; height: 22px;" src="/media/editorial/site_pages/deliverypass/images/truck.svg" alt="truck"></div>
							<div class="dp-benefits-list-text"><%= user.hasMidWeekDlvPass() ? "Order as often as you want Tuesday-Thursday and never be charged a delivery fee":"Unlimited Free Deliveries<br/>7 Days a Week" %></div>
						</div>
						<div class="dp-benefits-list-item">
							<div class="dp-benefits-list-icon"><img src="/media/editorial/site_pages/deliverypass/images/alarm-clock.svg" alt="alarm clock"></div>
							<div class="dp-benefits-list-text"><%= user.hasMidWeekDlvPass() ? "Reserve a Tuesday-Thursday delivery timeslot":"Timeslot Reservations" %></div>
						</div>
						<div class="dp-benefits-list-item">
							<div class="dp-benefits-list-icon"><img style="width: 30px; height: 30px;" src="/media/editorial/site_pages/deliverypass/images/tag.svg" alt="check"></div>
							<div class="dp-benefits-list-text">Exclusive DeliveryPass Perks</div>
						</div>
					</div>
					
					<div class="clear"></div>
					<div class="dp-plan-data">
						<div class="dp-plan-activated">
							<div class="dp-plan-activate-header">Active Since:</div>
							
							<div class="dp-plan-activate-text"><%= null != user ? DeliveryPassUtil.getPurchaseDate(user):null %> </div>
						</div>
						<%-- <% if(null != user && null != user.getDlvPassInfo() )%>
						<% if(user.getDlvPassInfo().getDPSavings() > FDStoreProperties.getMinimumAmountSavedDpAccPage()) { %>
						<div class="dp-plan-saved">
							<div class="dp-plan-activate-header">You've Saved:</div>
							<div class="dp-plan-activate-text"><%= null != user.getDlvPassInfo() ? user.getDlvPassInfo().getDPSavings():null %></div>
						</div>
						<% } %> --%>
						<div class="clear"></div>
					</div>
					
					<div class="dp-renew">
						<div class="dp-renew-date <%= EnumDPAutoRenewalType.YES.equals(user.hasAutoRenewDP()) ? "dp-dispay-renew-date":"" %> ">
						<span class="dp-renew-date-text">Your plan will be renewed automatically on</span><span class="dp-expire-date-text">Your plan will expire on</span> <span class="dp-renew-text-bold">
						<%= null != user ? DeliveryPassUtil.getAutoRenewalDate(user):null%></span><span class="dp-expire-text-bold"><%= null != user ? DeliveryPassUtil.getExpDate(user):null%></span></div>
						<div class="dp-auto-renew">
							<div class="dp-auto-renew-text">Auto-Renewal</div>
							<label class="switch">
								<input type="checkbox" onchange="deliveryPassAutoRenew(this)" <%= EnumDPAutoRenewalType.YES.equals(user.hasAutoRenewDP()) ? "checked":""  %>>
								<span class="slider"></span>
							</label>
							<div class="dp-autorenew-error"><div class="spinner"></div><div class="dp-autorenew-error-text error"> Something went wrong. Please try again.</div></div>
						</div>
						<div class="dp-renew-text">By turning off auto-renewal, you will continue on your current plan until it expires. If you wish to cancel your plan, please call <%= null != user ? user.getCustomerServiceContact():null %>.
							<br/><a href="#" autorenew-learn-more>Learn more about auto-renewals.</a>
						</div>
					</div>
					<div class="dp-payment">
						<div class="dp-header">Payment Method:</div>
						<% if(null != user.getDefaultPaymentMethod()){ %>
							<div class="dp-payment-data">
							<%if(user.getDefaultPaymentMethod().getCardType() != user.getDefaultPaymentMethod().getCardType().PAYPAL && user.getDefaultPaymentMethod().getCardType() != user.getDefaultPaymentMethod().getCardType().ECP  && user.getDefaultPaymentMethod().getCardType() != user.getDefaultPaymentMethod().getCardType().GCP ) {%>
								 <% if (EnumCardType.AMEX.equals(user.getDefaultPaymentMethod().getCardType())){ %>
									<div class="dp-payment-img"><img src="/media/editorial/site_pages/deliverypass/images/amex.png" alt="American Express"></div>
								<% } else if (EnumCardType.MC.equals(user.getDefaultPaymentMethod().getCardType())){ %>
									<div class="dp-payment-img"><img src="/media/editorial/site_pages/deliverypass/images/mastercard.png" alt="MasterCard"></div>
								<% } else if (EnumCardType.VISA.equals(user.getDefaultPaymentMethod().getCardType())){ %>
									<div class="dp-payment-img"><img src="/media/editorial/site_pages/deliverypass/images/visa.png" alt="Visa"></div>
								<% } else if (EnumCardType.DISC.equals(user.getDefaultPaymentMethod().getCardType())){ %>
									<div class="dp-payment-img"><img src="/media/editorial/site_pages/deliverypass/images/discover.png" alt="Discover"></div>
								<% } %>
								<div class="dp-payment-text">
									<%=  user.getDefaultPaymentMethod().getCardType().getDisplayName() %>
									 - Ending: <%=com.freshdirect.fdstore.payments.util.PaymentMethodUtil.getLast4AccNumber(user.getDefaultPaymentMethod()) %> 
									 Exp: <%=  com.freshdirect.fdstore.payments.util.PaymentMethodUtil.getExpDateMMYYYY(user.getDefaultPaymentMethod()) %> 
								 </div>
							<%}
							else if(null != user.getDefaultPaymentMethod() &&  user.getDefaultPaymentMethod().getCardType() == user.getDefaultPaymentMethod().getCardType().PAYPAL) {%>
								<div class="dp-payment-img"><img src="//www.paypalobjects.com/webstatic/mktg/Logo/pp-logo-100px.png" alt="paypal"></div>
								<div class="dp-payment-text">
									<%=  user.getDefaultPaymentMethod().getName() %>
									<%= user.getDefaultPaymentMethod().getEmailID() %>
								</div>
							<%} 
							else if(null != user.getDefaultPaymentMethod() &&  user.getDefaultPaymentMethod().getCardType() == user.getDefaultPaymentMethod().getCardType().ECP) {%>
							<div class="dp-payment-text">
								<%= user.getDefaultPaymentMethod().getBankAccountType() %>
								<%= user.getDefaultPaymentMethod().getAbaRouteNumber() %>
								<%=  user.getDefaultPaymentMethod().getMaskedAccountNumber() %>
								<%=  user.getDefaultPaymentMethod().getBankName()%>
							</div>
							<%}
							else if(null != user.getDefaultPaymentMethod() &&  user.getDefaultPaymentMethod().getCardType() == user.getDefaultPaymentMethod().getCardType().EBT) {%>
							<div class="dp-payment-text">
								<%= user.getDefaultPaymentMethod().getCardType().getDisplayName() %>
								<%=  user.getDefaultPaymentMethod().getMaskedAccountNumber() %>
							</div>
							<% } %>
							</div>
							<div class="dp-renew-text">Your default payment method will be charged for auto-renewal.
								<br/><%	if (viewContent.getModel() != null) { %>
								<fd:GetOrder id="dpOrder" saleId="<%= viewContent.getModel().getPurchaseOrderId() %>">
									<% if ("SUB".equalsIgnoreCase(dpOrder.getOrderType().getSaleType())) { %>
										<a href="/your_account/order_details.jsp?orderId=<%= dpOrder.getErpSalesId() %>">View your most recent DeliveryPass invoice.</a>
									<% } %>
								</fd:GetOrder>
								<% } %>
							</div>
							<% } else { %>
							<div class="dp-payment-data">
								<div class="dp-payment-text"><a href="/your_account/payment_information.jsp">Visit the Payment Methods page to add a payment method</a></div>
							</div>
						<% } %>
					</div>
					<div style=" text-align: center;">
						<a class="dp-learn-more" href="#" dp-learn-more>
							Learn more about DeliveryPass
						</a>
					</div>
				</div>
    		</div>
    		</fd:WebViewDeliveryPass>
    	<% } %>
    </tmpl:put>
</tmpl:insert>

