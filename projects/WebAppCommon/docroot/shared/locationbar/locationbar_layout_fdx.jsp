<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserUtil"%>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="fd" uri="freshdirect" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
	String uri = request.getRequestURI().toLowerCase();
	boolean mobWeb_locationbar_layout_fdx = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	boolean inMobWebTemplate = (request.getAttribute("inMobWebTemplate") != null) ? (Boolean)request.getAttribute("inMobWebTemplate") : false;
	FDOrderAdapter modifyingOrder = FDUserUtil.getModifyingOrder(user);
	boolean isModifyingOrder = user != null &&
			user.getLevel() >= FDUserI.RECOGNIZED &&
			modifyingOrder != null &&
			modifyingOrder.getDeliveryReservation() != null &&
			modifyingOrder.getDeliveryReservation().getTimeslot() != null;
			
%>
<% if (inMobWebTemplate && mobWeb_locationbar_layout_fdx) { %>
	<%-- for now, output nothing... except address since it sets the JS for locabar --%>
	<tmpl:get name="zip_address" />
<% } else {  %>
	<tmpl:get name="topwarningbar" />
	
	<div id="locationbar" class="<%= (uri.contains("/checkout/") || uri.contains("view_cart.jsp") || uri.contains("merge_cart.jsp") || uri.contains("/gift_card/")) ? "disableCart" : "" %>">
		<% if (!mobWeb_locationbar_layout_fdx) { %>
			<%@ include file="/common/template/includes/i_modifyorderbar.jspf" %>
			
		<% } else { %>
			<div id="location-tabs">
				<div class="locabar-spacer"></div>
				<tmpl:get name="tab_fd" />
				<tmpl:get name="tab_fdx" />
				<tmpl:get name="tab_cos" />
			</div>
		<%} %>
		<%-- fright sections --%>
		<div class="locabar-right-sections" role="menubar">
			<tmpl:get name="modify_order" /><tmpl:get name="messages" />
			<% if (!isModifyingOrder){ %>
			<tmpl:get name="zip_address" />
			<% } %>
			<tmpl:get name="sign_in" /><tmpl:get name="cartTotal" />
		</div>
	</div>
	
	<tmpl:get name="fdx_promo" />
	
	<div id="location-alerts" class="">
		<tmpl:get name="location_out_of_area_alert" />
		<tmpl:get name="error_so_alerts" />
		<tmpl:get name="min_so_alerts" />
		<tmpl:get name="activate_so_alerts" />
		<tmpl:get name="modify_order_alerts" />	
		<tmpl:get name="test_alerts" />	
	</div>
	<div id="unrecognized" class="invisible" data-type="sitemessage">
		<div class="unrecognized error-message">
			<p>
				<span class="orange">Sorry, we're unable to recognize the ZIP code <b>{{zip}}</b> you entered, please make sure it's entered correctly.</span><br>
				<span><a href="/help/delivery_zones.jsp" class="delivery-popuplink green">Click here</a> to see current delivery zones. To enter a different zip code, please enter in the box to the upper left.</span>
			</p>
		</div>
	</div>
	
	
	<%-- TEST alerts
	
		<div class="alerts invisible" id="alert2" data-type="alert2" data-addto="#alert2_cont">
			this is a test alert - closer
		</div>
		<div class="alerts invisible" id="alert3" data-type="alert3" data-addto="#alert4,#alert6_cont,#alert5_cont" data-closehandleraddto="#alert3_testcontainer">alert3</div>
		<div id="alert4">test other content</div>
		
		<div class="alert-cont" id="alert3_testcontainer">
			<div id="alert5_cont" class="alert">alert5_cont</div>
			<div id="alert6_cont" class="alert">alert6_cont</div>
			<div class="alerts invisible" id="alert1" data-type="alert1">this is a test alert - no close</div>
		</div>
	
	--%>
    <jwr:style src="/locabarfdx.css" media="all" />
    <jwr:script src="/locabarcomp.js" useRandomParam="false" />

<% } %>