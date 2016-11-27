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
FDStandingOrder currentStandingOrder=null;
String isNewSo= request.getParameter("newso");
if("false".equals(isNewSo)){
	currentStandingOrder=user.getCurrentStandingOrder(); 
%><script>var newsoID = <%= currentStandingOrder.getId() %>;</script>
<%	
} else {//if(!StandingOrderHelper.isStandingOrder(user))
	currentStandingOrder=new FDStandingOrder();
	currentStandingOrder.setCustomerId(user.getIdentity().getErpCustomerPK());
	currentStandingOrder.setId("");
}
currentStandingOrder.setNewSo(true);
user.setCurrentStandingOrder(currentStandingOrder);
user.setCheckoutMode(EnumCheckoutMode.CREATE_SO);
user.setSoTemplateCart(new FDCartModel());
%>

<potato:pendingExternalAtcItem standingOrder="true"/>
<potato:singlePageCheckout standingOrder="true"/>
<potato:cartData standingOrder="true"/>

<tmpl:insert template='/quickshop/includes/standing_order.jsp'>
	<tmpl:put name="soytemplates">
		<soy:import packageName="expressco" />
	</tmpl:put>

	<tmpl:put name='soSelected'>selected</tmpl:put>

	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf"%>
		<jwr:script src="/expressco.js" useRandomParam="false" />
	</tmpl:put>
	<tmpl:put name='content' direct='true'>

		<% if (user.isEligibleForStandingOrders()) {%>

		<div class="standing-orders-3 w970p">

			<div class="standing-orders-3-create-header">
				Your Standing Orders
				<div class="standing-orders-3-create-text">
					We do the rest and orders arrives as scheduled.
					<div class="standing-orders-3-learn-more">
						Learn More
						<div style="position: relative;">
							<div class="standing-orders-3-popup">
								<p>Why Use a Standing Order?</p>
								<ul>
									<li>Standing orders are like shopping lists that shop for
										you. Add items you need on a regular basis and we'll send them
										on the schedule you choose.</li>
									<li>Select weekly, biweekly or monthly delivery at times
										that are convenient for you.</li>
									<li>You can make changes or cancel deliveries just like
										all Fresh Direct orders.</li>
									<li>Never run out of bananas again!</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="standing-orders-3-h-settings">
				Standing Order Settings
				<hr>
				<div class="standing-orders-3-saved-container">
					<div class="standing-orders-3-so-new-saved">Saved!</div>
				</div>
			</div>

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
						<button class="standing-orders-3-name-input-ok-button" type="button" onmousedown="newStandingOrderNameInputChangeOK()">OK</button>
					</div>
					<button class="standing-orders-3-new-cancel-button cssbutton cssbutton-flat black" type="button" onclick="standingOrderNewCancel()">Cancel</button>										
			</div>
			<div style="position: relative;">
				<div id="newsoErroMessage"></div>
			</div>

			<div class="standing-orders-3-newso-drawer-container">
				<div id="ec-drawer"></div>
				<div class="standing-orders-3-newso-shop-buttons-container">
					<hr>
					<div class="standing-orders-3-newso-shop-buttons">
						<button class="standing-orders-3-newso-shop-buttons-past cssbutton cssbutton-flat purple" onclick="window.open('/quickshop/qs_past_orders.jsp', '_self');">Shop from Past Order</button>
						<button class="standing-orders-3-newso-shop-buttons-now cssbutton cssbutton-flat green" onclick="window.open('/index.jsp', '_self');">Shop Now &gt;</button>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>

		<% } %>

		<script>
      // potato loading
      window.FreshDirect.expressco = {};
      window.FreshDirect.expressco.data = <fd:ToJSON object="${singlePageCheckoutPotato}" noHeaders="true"/>
      window.FreshDirect.metaData = window.FreshDirect.expressco.data.formMetaData;
      window.FreshDirect.pendingCustomizations = <fd:ToJSON object="${pendingExternalAtcItemPotato}" noHeaders="true"/>
    </script>

	</tmpl:put>

	<tmpl:put name="extraCss">
		<fd:css href="/assets/css/timeslots.css" media="all" />
		<jwr:style src="/expressco.css" media="all" />
	</tmpl:put>

	<tmpl:put name="extraJs">
		<fd:javascript src="/assets/javascript/timeslots.js" />
	</tmpl:put>
</tmpl:insert>
