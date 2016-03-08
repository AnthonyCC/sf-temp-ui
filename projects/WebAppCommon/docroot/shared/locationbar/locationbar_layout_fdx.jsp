<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="fd" uri="freshdirect" %>
<%
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
	String uri = request.getRequestURI().toLowerCase();
%>
<tmpl:get name="topwarningbar" />

<div id="locationbar" class="<%= (uri.contains("/checkout/") || uri.contains("view_cart.jsp") || uri.contains("merge_cart.jsp")) ? "disableCart" : "" %>">
	<div class="locabar-spacer"></div>
	<tmpl:get name="tab_fdx" />
	<tmpl:get name="tab_cos" />
	
	<%-- fright sections --%>
	<div class="locabar-right-sections">
		<tmpl:get name="modify_order" /><tmpl:get name="messages" /><tmpl:get name="zip_address" /><tmpl:get name="sign_in" /><tmpl:get name="cartTotal" />
	</div>
</div>

<div id="location-alerts" class="">
	<tmpl:get name="location_out_of_area_alert" />
	<tmpl:get name="error_so_alerts" />
	<tmpl:get name="activate_so_alerts" />
	<tmpl:get name="modify_order_alerts" />	
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
<fd:css href="/assets/css/common/locationbar_fdx.css" />
<fd:javascript src="/assets/javascript/locationbar.js" />
<fd:javascript src="/assets/javascript/locationbar_fdx.js" />

