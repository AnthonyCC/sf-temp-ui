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
<jwr:script src="/expressco.js" useRandomParam="false" />
<script>
	$jq(document).ready(function($) {
		fd.expressco.checkout.initSoyDrawers();
		$(".deliverypass-payment").on('click', "[data-deliverypass-payment-close]", function(){
			$jq(".overlay-dialog-new .ui-dialog-titlebar-close").click()
		});
	});
	if(typeof FreshDirect.deliveryPassSelectedTitle !== 'undefined'){
		$jq(".deliverypass-payment-header").html(FreshDirect.deliveryPassSelectedTitle);
	}
	FreshDirect.terms = true;
	<% if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){ %>
		FreshDirect.terms=<%=fdTcAgree.booleanValue()%>;
		doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>');
	<% } %>
</script>