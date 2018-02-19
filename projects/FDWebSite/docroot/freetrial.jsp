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

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" />

<%
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String template = "/expressco/includes/ec_template.jsp";
	if (mobWeb) {
		template = "/common/template/mobileWeb.jsp"; //mobWeb template
	}

	MasqueradeContext masqueradeContext = user.getMasqueradeContext();
%>

<tmpl:insert template='<%= template %>'>
    <tmpl:put name='title' direct='true'>DeliveryPass | FreshDirect</tmpl:put>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_pass"></fd:SEOMetaTag>
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
  	<soy:render template="expressco.checkoutheader" />
  	</tmpl:put>
	
	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf"%>
	</tmpl:put>
	
	<tmpl:put name="bottomnav">
	    <div class="container checkout__footer">
	        <p class="checkout__footer-rights"><%@ include file="/shared/template/includes/copyright.jspf" %></p>
	        <p class="checkout__footer-links"><a href='/help/privacy_policy.jsp' data-ifrpopup="/help/privacy_policy.jsp?type=popup" data-ifrpopup-width="600">Privacy Policy</a> | <a href="/help/terms_of_service.jsp" data-ifrpopup="/help/terms_of_service.jsp?type=popup" data-ifrpopup-width="600">Customer Agreement</a></p>
	    </div>
  	</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
		<script type="text/javascript">
		    	function redirectToSignup() {
				    var form = document.forms['signup'];
				    form.elements['action'].value='signup';
				    form.method='POST';
				    form.submit();
				    return false;		    	
		    	}
		    	
		    	function flipAutoRenewalOFF() {
				    var form = document.forms['autoRenew'];
				    form.elements['action'].value='FLIP_AUTORENEW_OFF';
				    form.method='POST';
				    form.submit();
				    return false;
		    	}
		    	
		    	function flipAutoRenewalON() {
				    var form = document.forms['autoRenew'];
				    form.elements['action'].value='FLIP_AUTORENEW_ON';
				    form.method='POST';
				    form.submit();
				    return false;
		    	}
		    	
		    	function getFreeDelivery(){
		    		$jq.ajax({
		    	        url: '/api/freetrial',
		    	        type: 'POST',
		    	        success: function() {
		    	        	window.location.href = "/freetrialsuccess.jsp";
		    	        },
		    	        error: function() {
		    	        	doOverlayDialogNew("/includes/error_message.jspf");
		    	        }
		    	 	});
		    	}
		</script>
    <fd:DlvPassSignupController result="result" callCenter="false">
		<fd:ErrorHandler result='<%=result%>' name='dlvpass_discontinued' id='errorMsg'>
		   <%@ include file="/includes/i_error_messages.jspf" %>
		</fd:ErrorHandler>
    
        <fd:WebViewDeliveryPass id='viewContent'>
        	<div class="dpn-content">
        		<%@ include file="/includes/freetrial.jspf" %>
        	</div>
        </fd:WebViewDeliveryPass>
	</fd:DlvPassSignupController>
    </tmpl:put>
    
    <tmpl:put name="extraCss">
		<jwr:style src="/expressco.css" media="all" />
		<fd:css href="/assets/css/delivery_pass.css" />
	</tmpl:put>

	<tmpl:put name="extraJs">
		<fd:javascript src="/assets/javascript/timeslots.js" />
		<jwr:script src="/qsstandingorder.js" />
	</tmpl:put>
</tmpl:insert>

