<%@ page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartModel"%>
<%@ page import="com.freshdirect.fdstore.EnumCheckoutMode"%>
<%@ page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.fdstore.customer.ejb.EnumCustomerListType"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerListInfo"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerList"%>
<%@ page import="com.freshdirect.fdstore.lists.FDListManager"%>
<%@ page import="com.freshdirect.framework.util.DateUtil"%>
<%@ page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import="com.freshdirect.fdstore.lists.FDStandingOrderList"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ taglib uri="https://developers.google.com/closure/templates"
	prefix="soy"%>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<fd:CheckLoginStatus id="user" guestAllowed='false'
	recognizedAllowed='false' />
<fd:QuickShopRedirector user="<%=user%>" from="<%=QuickShopRedirector.FROM.NEW_SO3_DETAIL %>"/>
<%
MasqueradeContext masqueradeContext = user.getMasqueradeContext();
FDStandingOrder currentStandingOrder=null;
String isNewSo= request.getParameter("newso");
currentStandingOrder=new FDStandingOrder();
currentStandingOrder.setCustomerId(user.getIdentity().getErpCustomerPK());
currentStandingOrder.setId("");
currentStandingOrder.setNewSo(true);
user.setCurrentStandingOrder(currentStandingOrder);
user.setCheckoutMode(EnumCheckoutMode.CREATE_SO);
user.setSoTemplateCart(new FDCartModel());
%>

<potato:pendingExternalAtcItem standingOrder="true"/>
<potato:singlePageCheckout standingOrder="true"/>

<tmpl:insert template='/expressco/includes/ec_template.jsp'>

	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="New Standing Order" pageId="standing_orders_new"></fd:SEOMetaTag>
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
	    
		<!-- Changes for Skip to Content Fix -->
		<div tabindex="-1" id="skip_to_content"></div>
  	</tmpl:put>
	<tmpl:put name="soytemplates">
		<soy:import packageName="expressco" />
	</tmpl:put>

	<tmpl:put name='soSelected'>selected</tmpl:put>

	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf"%>
		<jwr:script src="/expressco.js" useRandomParam="false" />
	</tmpl:put>
	
	<tmpl:put name="bottomnav">
	    <div class="container checkout__footer">
	        <p class="checkout__footer-rights"><%@ include file="/shared/template/includes/copyright.jspf" %></p>
	        <p class="checkout__footer-links"><a href='/help/privacy_policy.jsp' data-ifrpopup="/help/privacy_policy.jsp?type=popup" data-ifrpopup-width="600">Privacy Policy</a> | <a href="/help/terms_of_service.jsp" data-ifrpopup="/help/terms_of_service.jsp?type=popup" data-ifrpopup-width="600">Customer Agreement</a></p>
	    </div>
  	</tmpl:put>
  
	<tmpl:put name='content' direct='true'>
	
		<div class="standing-orders-3-cont">

		<% if (user.isEligibleForStandingOrders()) {%>
		
		<div class="standing-orders-3 w970p">

			<div class="standing-orders-3-create-header">
				Complete your settings
				<button class="standing-orders-3-new-start-shop cssbutton cssbutton-flat purple nontransparent" disabled onclick="submitFormNewSO('create');">Start Shopping</a>
				<button class="standing-orders-3-new-cancel-button cssbutton cssbutton-flat green transparent" type="button" onclick="standingOrderNewCancel()">Cancel</button>
				<hr>
				<!-- 
					<div class="standing-orders-3-saved-container">
						<div class="standing-orders-3-so-new-saved">Saved!</div>
					</div>
				-->
			</div>
			<div class="standing-orders-3-set-form-header"><label for="soName">Name<span class="offscreen">your Standing Order (e.g. "Weekly Meeting")</span></label></div>
			<div class="standing-orders-3-set-form">
					<input type="hidden" name="action" value="create">
					<% if(null!=user.getCurrentStandingOrder() && null!=user.getCurrentStandingOrder().getCustomerListName()) {
						String soName=user.getCurrentStandingOrder().getCustomerListName();%>
						<input class="standing-orders-3-name-input" type="text" name="soName" id="soName" value="<%=soName%>" placeholder='Name your Standing Order (e.g. "Weekly Meeting")' maxlength="25" autocomplete="off">
					<%} else { %>
						<input class="standing-orders-3-name-input" type="text" name="soName" id="soName" placeholder='Name your Standing Order (e.g. "Weekly Meeting")' maxlength="25" autocomplete="off">
					<%} %>
					<div class="standing-orders-3-name-input-change">
						<div class="standing-orders-3-char-count"></div>
						<!-- 
							<button class="standing-orders-3-name-input-ok-button" type="button" onmousedown="newStandingOrderNameInputChangeOK()">OK</button>
						 -->
					</div>						
			</div>
			<div style="position: relative;">
				<div id="newsoErroMessage"></div>
			</div>

			<div class="standing-orders-3-newso-drawer-container">
				<div id="ec-drawer"></div>
				<!--  
					<div class="standing-orders-3-newso-shop-buttons-container">
						<hr>
						<div class="standing-orders-3-newso-shop-buttons">
							<button class="standing-orders-3-newso-shop-buttons-past cssbutton cssbutton-flat purple" onclick="window.open('/quickshop/qs_past_orders.jsp', '_self');">Shop from Past Order</button>
							<button class="standing-orders-3-newso-shop-buttons-now cssbutton cssbutton-flat green" onclick="window.open('/index.jsp', '_self');">Shop Now &gt;</button>
						</div>
						<div class="clear"></div>
					</div>
				-->
			</div>
		</div>

		<% } %>
		</div>

		<script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${singlePageCheckoutPotato}" noHeaders="true"/>;
      window.FreshDirect.metaData = window.FreshDirect.expressco.data.formMetaData;
      window.FreshDirect.pendingCustomizations = <fd:ToJSON object="${pendingExternalAtcItemPotato}" noHeaders="true"/>;
    </script>

	</tmpl:put>

	<tmpl:put name="extraCss">
		<jwr:style src="/timeslots.css" media="all" />
		<jwr:style src="/expressco.css" media="all" />
		<jwr:style src="/quickshop.css" media="all" />
	</tmpl:put>

	<tmpl:put name="extraJs">
		<jwr:script src="/assets/javascript/timeslots.js" useRandomParam="false" />
		<jwr:script src="/qsstandingorder.js" useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
