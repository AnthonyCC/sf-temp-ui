<%@ page import="com.freshdirect.enums.CaptchaType" %>
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
<%@ page import="com.freshdirect.webapp.util.CaptchaUtil" %>
<%@ page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import="java.util.Calendar" %>
<%@ page import='java.util.Date' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy"%>

<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" />

<%
	if(!FDStoreProperties.isDlvPassFreeTrialOptinFeatureEnabled()){
		response.sendRedirect("/");
	}
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String template = "/expressco/includes/ec_template.jsp";
	if (mobWeb) {
		template = "/common/template/mobileWeb.jsp"; //mobWeb template
	}

	MasqueradeContext masqueradeContext = user.getMasqueradeContext();
	
	boolean showCaptchaInPayment = CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidPaymentAttempt(), session, SessionName.PAYMENT_ATTEMPT);
%>
<tmpl:insert template='<%= template %>'>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_pass_sub"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>
	
	<tmpl:put name="globalnav">
	<%-- we need the ad_server.jsp include here because it's in the globalnav (which is not used on this page) --%>
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
	
  	<%-- MASQUERADE HEADER STARTS HERE --%>
  	<% if (masqueradeContext != null) {
  		String makeGoodFromOrderId = masqueradeContext.getMakeGoodFromOrderId();
  	%>
	<div id="topwarningbar">
		You (<%=masqueradeContext.getAgentId()%>) are masquerading as <%=user.getUserId()%> (Store: <%= user.getUserContext().getStoreContext().getEStoreId() %> | Facility: <%= user.getUserContext().getFulfillmentContext().getPlantId() %>)
		<%if (makeGoodFromOrderId!=null) {%>
			<br>You are creating a MakeGood Order from <a href="/quickshop/shop_from_order.jsp?orderId=<%=makeGoodFromOrderId%>">#<%=makeGoodFromOrderId%></a>
			(<a href="javascript:if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes', width: 600, height: 800, opacity: .5}) } else {pop('/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes','600','800')};">Carton Contents</a>)
			<a class="imgButtonRed" href="/cancelmakegood.jsp">Cancel MakeGood</a>
		<%}%>
	</div>
  	<% } %>
  	<%-- MASQUERADE HEADER ENDS HERE --%>
  	<soy:render template="expressco.checkoutheader" />
  	</tmpl:put>
	
	<tmpl:put name="jsmodules">
	    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	    <jwr:script src="/expressco.js" useRandomParam="false" />
	</tmpl:put>
	
	<tmpl:put name="bottomnav">
	    <div class="container checkout__footer">
	        <p class="checkout__footer-rights"><%@ include file="/shared/template/includes/copyright.jspf" %></p>
	        <p class="checkout__footer-links"><a href='/help/privacy_policy.jsp' data-ifrpopup="/help/privacy_policy.jsp?type=popup" data-ifrpopup-width="600">Privacy Policy</a> | <a href="/help/terms_of_service.jsp" data-ifrpopup="/help/terms_of_service.jsp?type=popup" data-ifrpopup-width="600">Customer Agreement</a></p>
	    </div>
  	</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
	    <fd:DlvPassSignupController result="result" callCenter="false">
			<fd:ErrorHandler result='<%=result%>' name='dlvpass_discontinued' id='errorMsg'>
			   <%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>
	    
	        <fd:WebViewDeliveryPass id='viewContent'>
	        	<div class="deliverypass-content">
	        		<div id="deliverypasscontent" data-dlvpasscart="true"></div>
	        		<script>
		        		$jq( document ).ready(function() {
		        			var notlogged = ((FreshDirect.user.recognized || FreshDirect.user.guest) ? true : false);
		        			$jq.ajax({
			        	        url: '/api/expresscheckout/deliverypass',
			        	        type: 'GET',
			        	        success: function(result){
			        	        	$jq("#deliverypasscontent").html(expressco.deliverypasspopup({data:result.deliveryPass, notloggedin:notlogged}));
			        	        }
			        	 	});
		        		});
	        		</script>
					<script async type="text/javascript" src="https://js.braintreegateway.com/js/braintree-2.21.0.min.js"></script>
	        	</div>
	        </fd:WebViewDeliveryPass>
		</fd:DlvPassSignupController>
    </tmpl:put>
    
    <tmpl:put name="extraCss">
		<jwr:style src="/expressco.css" media="all" />
	</tmpl:put>
	
	<tmpl:put name="extraJs">
	  	<%
	  		/* skip yui on this page */
	  		request.setAttribute("noyui", true);
	  	%>
		<jwr:script src="/protoscriptbox.js" useRandomParam="false" />
    	<fd:javascript src="/assets/javascript/timeslots.js" />
	</tmpl:put>
	<tmpl:put name="leastPrioritizeJs">
		<jwr:script src="/assets/javascript/fd/captcha/captchaWidget.js" useRandomParam="false" />
		<script>
			if (<%=showCaptchaInPayment%>) {
		  		FreshDirect.components.captchaWidget.init('cc-payment-g-recaptcha', '<%=FDStoreProperties.getRecaptchaPublicKey(CaptchaType.PAYMENT)%>');
		  		FreshDirect.user = FreshDirect.user || {};
		  		FreshDirect.user.showCaptchaInPayment = true;
		  	} else {
		  		FreshDirect.components.captchaWidget.setKey('cc-payment-g-recaptcha', '<%=FDStoreProperties.getRecaptchaPublicKey(CaptchaType.PAYMENT)%>');
		  	}
			var checkout;
			//While loading the screen get the Device ID from braintress
			jQuery(document).ready(function($){
				var $ = jQuery;
				 $("#isPayPalDown").val("false");
			       $.ajax({
				  			url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"PPSTART\",\"formdata\":{\"action\":\"PP_Connecting_Start\",\"ewalletType\":\"PP\"}}",
			          type: 'post',
			          contentType: "application/json; charset=utf-8",
			          dataType: "json",
			          success: function(result){
			        	  if(result.submitForm.success && result.submitForm.result.eWalletResponseData.token != null){
			        		  $('#PP_ERROR').css("display", "none");
			                  $("#isPayPalDown").val("false");
				          	var deviceObj = "";
				  	    	braintree.setup(result.submitForm.result.eWalletResponseData.token, "custom", {
				 	    		  dataCollector: {
				 	    			    paypal: true
				 	    			  },
				 	    		  onReady: function (integration) {
				 	    			 checkout = integration;
				 	    		    deviceObj = JSON.parse(integration.deviceData);
				 	    		 $('#ppDeviceId').val(deviceObj.correlation_id);
				 	    		  },
				 	    		 onPaymentMethodReceived: function (payload) {
				 	    		    $.ajax({
				 	                      url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EPP\",\"formdata\":{\"action\":\"PP_Pairing_End\",\"ewalletType\":" +
				 	                      		"\"PP\",\"paymentMethodNonce\":\""+payload.nonce+"\",\"email\":\""+payload.details.email+"\",\"firstName\":\""+payload.details.firstName+"\"," +
				 	                      				"\"lastName\":\""+payload.details.lastName+"\" ,\"deviceId\":\""+deviceObj.correlation_id+"\"}}",
				 	                      type: 'post',
				 	                      success: function(id, result){
											/* don't reload page, just update payments display */
											$.get('/api/expresscheckout/payment?dlvPassCart=true').done( function (d) {
												FreshDirect.common.dispatcher.signal('payment', d);
												if($(".deliverypass-payment").length > 0){
													dialogWindowRealignFunc();
												}
											});
				 	                      }
				 	    	        });
				 	    		  },
				 	    		  paypal: {
				 	    		    singleUse: false,
				 	    		    headless: true
				 	    		  }
				  	    	});
			        	  }else {
			              	$("#isPayPalDown").val("true");
			              }
			          },
			        failure:function (id, result) {
			  			$("#isPayPalDown").val("true");
			  		},
			  		error:function (id, result) {
			  			$("#isPayPalDown").val("true");
			  		},
			  		fail:function (id, result) {
			  			$("#isPayPalDown").val("true");
			  		}
				 });
	
			       if (document.querySelector('#PP_button') != null) {
			           document.querySelector('#PP_button').addEventListener('click', function(event) {
			               if (event.preventDefault) {
			                   event.preventDefault();
			               } else {
			                   event.returnValue = false;
			               }
			               console.log("-- Connect With Paypal click handler  --");
			               if (checkout != null) {
			                   checkout.paypal.initAuthFlow();
			               }
	
			               var isPayPalDown = $('#isPayPalDown').val();
			               if (isPayPalDown == 'true') {
			               	$('#PP_ERROR').css("display", "inline-block");
			               }else{
			               	$('#PP_ERROR').css("display", "none");
			               }
			           });
			       }
	
			});
		</script>
	</tmpl:put>
</tmpl:insert>

