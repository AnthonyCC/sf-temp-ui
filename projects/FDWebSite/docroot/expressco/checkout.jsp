<%@page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
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
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage="/checkout/signup_ckt.jsp" />
<%
MasqueradeContext masqueradeContext = user.getMasqueradeContext();
%>
<potato:pendingExternalAtcItem/>
<potato:singlePageCheckout />
<potato:cartData />

<tmpl:insert template='/expressco/includes/ec_template.jsp'>
  <tmpl:put name="soytemplates"><soy:import packageName="expressco"/></tmpl:put>

  <tmpl:put name="jsmodules">
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    <script>
    FreshDirect.terms = <%=fdTcAgree%>;
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

<script src="<%= FDStoreProperties.getMasterpassLightBoxURL() %>" type="text/javascript"></script>

	 <!-- APPDEV-4287  Dstillery pixel -->
         <script type="text/javascript" async>
		function asyncPixelWithTimeout() {
		var img = new Image(1, 1);
		img.src = '//action.media6degrees.com/orbserv/hbpix?pixId=26209&pcv=49';
		setTimeout(function ()
		{ if (!img.complete) img.src = ''; /*kill the request*/ }

		, 33);
		};
		asyncPixelWithTimeout();
		</script>
 	 <%if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){%>
		<script type="text/javascript">
		doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'400px\' height=\'350px\' frameborder=\'0\' ></iframe>');
		</script>
	<%}%>
  </tmpl:put>
  
  <tmpl:put name="globalnav">
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
        <p class="checkout__footer-rights">&copy; 2002 - 2015 Fresh Direct, LLC. All Rights Reserved.</p>
        <p class="checkout__footer-links"><a href='/help/privacy_policy.jsp' data-ifrpopup="/help/privacy_policy.jsp?type=popup" data-ifrpopup-width="600">Privacy Policy</a> | <a href="/help/terms_of_service.jsp" data-ifrpopup="/help/terms_of_service.jsp?type=popup" data-ifrpopup-width="600">Customer Agreement</a></p>
    </div>
  </tmpl:put>

  <tmpl:put name="ecpage">checkout</tmpl:put>

  <tmpl:put name="title">Checkout</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <div id="expresscheckout">
      <div class="container">
        <div class="header cartheader">
          <div class="cartheader__text">
            <h1 class="checkout icon-cart_fast-before">Checkout</h1>
          </div><div class="cartheader__action_w_subtotal">
            <div fdform-error-container="checkout">
            </div>
            <form fdform="checkout" action="#" id="checkoutbutton_top" fdform-disable-resubmit="true" fdform-disable-resubmit-selector=".cssbutton.orange" fdform-disable-resubmit-release="manual">
              <soy:render template="expressco.checkoutButton" data="${singlePageCheckoutPotato}" />
            </form>
          </div>
        </div>
		
		
        <div id="modifyorder">
          <soy:render template="expressco.modifyorder" data="${cartDataPotato}" />
        </div>

        <%-- drawer --%>
        <%-- TODO: render soy here --%>
        <div id="ec-drawer">
        </div>
        
       
        <div class="checkout-contentheader">
          <h2>Cart Details</h2>
          <a class="cssbutton green" href="/expressco/view_cart.jsp">Make Changes<span class="offscreen">in My Cart</span></a>
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
  
<!-- This change is for Voucher Redemption. We are hiding the change control for these particular voucher users -->
  <tmpl:put name="extraCss">
  	<% if (user.isVoucherHolder() && null == masqueradeContext) { %>
	  	<style>
	  		[data-drawer-id="address"]>button {
		    	display: none !important;
			}
	  	</style>
  	<% } %>
    <fd:css href="/assets/css/timeslots.css" media="all" />
  </tmpl:put>

  <tmpl:put name="extraJs">
    <fd:javascript src="/assets/javascript/timeslots.js" />
  </tmpl:put>

</tmpl:insert>
