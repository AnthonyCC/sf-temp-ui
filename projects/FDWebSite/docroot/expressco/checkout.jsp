<%@ page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.webapp.util.CaptchaUtil" %>
<%@ page import="java.util.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<%  //--------OAS Page Variables-----------------------
  request.setAttribute("sitePage", "www.freshdirect.com/expressco/checkout/");
  request.setAttribute("listPos", "SystemMessage"); // TODO
  Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
%>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage="/login/login.jsp?successPage=/expressco/checkout.jsp" />
<%
MasqueradeContext masqueradeContext = user.getMasqueradeContext();
Map<String,String> checkoutHeaderMap = new HashMap<String,String>();
checkoutHeaderMap.put("phoneNumber", user.getCustomerServiceContact());
checkoutHeaderMap.put("label", "Checkout");
boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/expressco/includes/ec_template.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
	}
}

boolean showCaptchaInPayment = CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidPaymentAttempt(), session, SessionName.PAYMENT_ATTEMPT);
%>
<potato:pendingExternalAtcItem/>
<potato:singlePageCheckout action="resetContext" />
<tmpl:insert template='<%= pageTemplate %>'>
  <tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>

  <tmpl:put name="jsmodules">
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    <script>
    FreshDirect.terms = true;
    </script>
    <jwr:script src="/expressco.js" useRandomParam="false" />

    <script>
    </script>
 <!-- <script type="text/javascript" src="https://js.braintreegateway.com/v2/braintree.js"></script> -->
 	<script async type="text/javascript" src="https://js.braintreegateway.com/js/braintree-2.21.0.min.js"></script>
	<script async src="<%= FDStoreProperties.getMasterpassLightBoxURL() %>" type="text/javascript"></script>

 	 <%if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){%>
		<script type="text/javascript">
		FreshDirect.terms=<%=fdTcAgree.booleanValue()%>;
		doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>');
		</script>
	<%}%>
  </tmpl:put>

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
  	<soy:render template="expressco.checkoutheader" data="<%=checkoutHeaderMap %>" injectAdditonalData="false" />
  </tmpl:put>

  <tmpl:put name="bottomnav">
    <div class="container checkout__footer">
        <p class="checkout__footer-rights"><%@ include file="/shared/template/includes/copyright.jspf" %></p>
		<fd:IncludeMedia name="/media/layout/nav/globalnav/footer/after_copyright_footer.ftl">
			<p class="checkout__footer-links"><a href='/help/privacy_policy.jsp' data-ifrpopup="/help/privacy_policy.jsp?type=popup" data-ifrpopup-width="600">Privacy Policy</a> | <a href="/help/terms_of_service.jsp" data-ifrpopup="/help/terms_of_service.jsp?type=popup" data-ifrpopup-width="600">Customer Agreement</a></p>
		</fd:IncludeMedia>
    </div>
  </tmpl:put>

  <tmpl:put name="ecpage">checkout</tmpl:put>

  <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="Checkout" pageId="ec_checkout"></fd:SEOMetaTag>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <div id="expresscheckout">
      <div class="container">

      	<div id="cartheader_co">
      	<% if (mobWeb) { %>
	      	<div class="header cartheader">
	          <div class="cartheader__action_w_subtotal">
	            <form fdform="checkout" action="#" id="checkoutbutton_bottom" fdform-disable-resubmit="true" fdform-disable-resubmit-selector=".cssbutton.orange" fdform-disable-resubmit-release="manual">
	            </form>
	          </div>
	        </div>
			<div fdform-error-container="checkout"></div>
		<% } %>
        </div>

        <%-- drawer --%>
        <%-- TODO: render soy here --%>
        <div id="ec-drawer">
        </div>
        <div id="checkout-cart-header"  data-ec-linetemplate="expressco.checkoutcartheader">

        </div>
        <div id="checkout-drawer-footer">
        </div>

        <% if (!mobWeb) { /* no mobWeb for now */  %>
          <% if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user)) { %>
            <div id="cartCarousels">
     			<script>
     				$jq(document).ready(function() {
		          		$jq.ajax('/api/carousel?type=checkout').then(function(data) {
			          		$jq('#expresscheckout #cartCarousels').html(common.checkoutTabbedCarousel(data));
                  FreshDirect.components.carousel.changePage($jq('#cartCarousels [data-component="carousel"]').first(), null);
			          	});
	     			});
		        </script>
            </div>
          <% } %>
        <% } %>

        <div class="checkout-contentheader">
          <div class="checkout-contentheader-text">Cart Details</div>
          <div class="checkout-contentheader-actions right">
          	<a class="cssbutton green transparent small" href="/expressco/view_cart.jsp">Modify Cart</a>
          </div>
          <div class="clear"></div>
        </div>

        <%-- cart content --%>
        <div id="cartcontent" class="checkout" data-ec-linetemplate="expressco.checkoutlines" data-drawer-disabled>

        </div>

      </div>
    </div>

    <script>
      // makes ajax calls to retreive data and render soy components
      $jq(document).ready(function($) {
      	fd.expressco.checkout.initSoyComponents();
      });
      window.FreshDirect.expressco = {};
      window.FreshDirect.properties = window.FreshDirect.properties || {};
      window.FreshDirect.properties.displayDeliveryFeeForCosUserInHeader = <%=FDStoreProperties.shouldShowDeliveryFeeForCheckoutPageCosCustomer()%>
      window.FreshDirect.properties.isPaymentMethodVerificationEnabled = <%=FDStoreProperties.isPaymentMethodVerificationEnabled()%>
      window.FreshDirect.pendingCustomizations = <fd:ToJSON object="${pendingExternalAtcItemPotato}" noHeaders="true"/>
    </script>
  </tmpl:put>

  <tmpl:put name="extraCss">
    <jwr:style src="/expressco.css" media="all" />
  	<% if (user.isVoucherHolder() && null == masqueradeContext) { %>
	<!-- This change is for Voucher Redemption. We are hiding the change control for these particular voucher users -->
	  	<style>
	  		[data-drawer-id="address"]>button {
		    	display: none !important;
			}
	  	</style>
  	<% } %>
    <jwr:style src="/timeslots.css" media="all" />
  </tmpl:put>

  <tmpl:put name="leastPrioritizeJs">
    <jwr:script src="/assets/javascript/timeslots.js" async="true" useRandomParam="false" 
    onload="fd && fd.expressco && fd.expressco.checkout && fd.expressco.checkout.timeslotDrawerDependencyLoaded()" />
	<jwr:script src="/assets/javascript/fd/captcha/captchaWidget.js" useRandomParam="false" />
	
	<script>
		if (<%=showCaptchaInPayment%>) {
	  		FreshDirect.components.captchaWidget.init('<%=FDStoreProperties.getRecaptchaPublicKey()%>');
	  		FreshDirect.user = FreshDirect.user || {};
	  		FreshDirect.user.showCaptchaInPayment = true;
	  	} else {
	  		FreshDirect.components.captchaWidget.setKey('<%=FDStoreProperties.getRecaptchaPublicKey()%>');
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
			 	                    	 //location.reload(true);
			 	                    	 window.location.assign("/expressco/checkout.jsp");
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
