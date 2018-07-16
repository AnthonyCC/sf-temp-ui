<%@ page import='com.freshdirect.fdstore.*' %>
<%
  Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
%>

<fd:CheckLoginStatus id="userDPP" guestAllowed="true" recognizedAllowed="true" />

<div class="dpn">
	<div class="dpn-success-container deliverypass-payment">
		<div class="dpn-success">
			<div class="deliverypass-payment-header"></div>
		</div>
		<div id="expresscheckout" class="deliverypasscheckout">
			<div id="ec-drawer"></div>
			<div id="cartcontent" class="checkout" data-ec-linetemplate="expressco.checkoutlines" data-ec-request-uri="/api/expresscheckout/cartdata?isDlvPassCart=true" data-drawer-disabled></div>
		</div>
	</div>
</div>

<script>
	fd.expressco.checkout.initSoyDrawers();
	fd.expressco.cartcontent.listen();
	fd.expressco.cartcontent.watchChanges();
	fd.expressco.cartcontent.update();
	$jq(".deliverypass-payment").on('click', "[data-deliverypass-payment-close]", function(){
		$jq(".overlay-dialog-new .ui-dialog-titlebar-close").click();
	});
	window.FreshDirect.properties.isPaymentMethodVerificationEnabled = <%=FDStoreProperties.isPaymentMethodVerificationEnabled()%>
	if(typeof FreshDirect.deliveryPassSelectedTitle !== 'undefined'){
		$jq(".deliverypass-payment-header").html(FreshDirect.deliveryPassSelectedTitle);
	}
	FreshDirect.terms = true;
	<% if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){ %>
		FreshDirect.terms=<%=fdTcAgree.booleanValue()%>;
		doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>');
	<% } %>
</script>
<script async src="<%= FDStoreProperties.getMasterpassLightBoxURL() %>" type="text/javascript"></script>