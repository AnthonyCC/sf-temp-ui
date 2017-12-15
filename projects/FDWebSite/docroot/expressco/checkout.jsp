<%@page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
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

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/expressco/includes/ec_template.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
	}
}
%>
<potato:pendingExternalAtcItem/>
<potato:singlePageCheckout />

<tmpl:insert template='<%= pageTemplate %>'>
  <tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>

  <tmpl:put name="jsmodules">
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    <script>
    FreshDirect.terms = true;
    </script>
    <jwr:script src="/expressco.js" useRandomParam="false" />

    <script>
      (function () {
        FreshDirect.common.dispatcher.signal('drawer', FreshDirect.expressco.data.drawer);
        FreshDirect.common.dispatcher.signal('payment', FreshDirect.expressco.data.payment);
        FreshDirect.common.dispatcher.signal('address', FreshDirect.expressco.data.address);
        FreshDirect.common.dispatcher.signal('timeslot', FreshDirect.expressco.data.timeslot);
        FreshDirect.common.dispatcher.signal('atpFailure', FreshDirect.expressco.data.atpFailure);
        if(FreshDirect.expressco.data.redirectUrl){
          FreshDirect.common.dispatcher.signal('redirectUrl', FreshDirect.expressco.data.redirectUrl);
        }
      }());
    </script>
 <!-- <script type="text/javascript" src="https://js.braintreegateway.com/v2/braintree.js"></script> -->
 	<script type="text/javascript" src="https://js.braintreegateway.com/js/braintree-2.21.0.min.js"></script>
	<script src="<%= FDStoreProperties.getMasterpassLightBoxURL() %>" type="text/javascript"></script>


<script src="<%= FDStoreProperties.getMasterpassLightBoxURL() %>" type="text/javascript"></script>

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
    <soy:render template="expressco.checkoutheader" data="${singlePageCheckoutPotato}" />
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
	      	<div class="header cartheader">
	          <% if (!mobWeb) { %>
	          <div class="cartheader__text">
	            <h1 class="checkout icon-cart_fast-before">Checkout</h1>
	          </div><% } /* no spacing allowed */ %><div class="cartheader__action_w_subtotal">
				<% if (!mobWeb) { %><div fdform-error-container="checkout"></div><% } %>
	            <form fdform="checkout" action="#" id="checkoutbutton_top" fdform-disable-resubmit="true" fdform-disable-resubmit-selector=".cssbutton.orange" fdform-disable-resubmit-release="manual">
	              <soy:render template="expressco.checkoutButton" data="${singlePageCheckoutPotato}" />
	            </form>
	            <a class="etipping-addtip-text" href='#tipDropdown'>Add a Tip</a>
	          </div>
	        </div>
	        <%-- this needs to be AFTER the main header in mobweb (but still IN the header) --%>
			<% if (mobWeb) { %><div fdform-error-container="checkout"></div><% } %>
        </div>


         <% if (mobWeb) { %>
         <div class="cartheader__text">
           <h1 class="checkout icon-cart_fast-before">Checkout</h1>
         </div>
         <% } %>
        <div id="modifyorder">
          <soy:render template="expressco.modifyorder" data="${singlePageCheckoutPotato}" />
        </div>

        <%-- drawer --%>
        <%-- TODO: render soy here --%>
        <div id="ec-drawer">
        </div>


        <% if (!mobWeb) { /* no mobWeb for now */  %>
          <% if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user)) { %>
            <div id="cartCarousels">
     			<script>
		          	$jq.ajax('/carousel/carousel.jsp?type=checkout').then(function(page) {
		          		$jq('#expresscheckout #cartCarousels').html(page);
		          	});
		        </script>
            </div>
          <% } %>
        <% } %>

        <div class="checkout-contentheader">
          <h2>Cart Details</h2>
          <a class="cssbutton green" href="/expressco/view_cart.jsp">Edit Cart</a>
        </div>

        <%-- cart content --%>
        <div id="cartcontent" class="checkout" data-ec-linetemplate="expressco.checkoutlines" data-drawer-disabled>

        </div>

      </div>
    </div>

    <script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${singlePageCheckoutPotato}" noHeaders="true"/>
      window.FreshDirect.metaData = window.FreshDirect.expressco.data.formMetaData;
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

  <tmpl:put name="extraJs">
    <jwr:script src="/assets/javascript/timeslots.js" useRandomParam="false" />

	<script>
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
