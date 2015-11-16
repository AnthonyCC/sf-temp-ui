<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="fd" uri="freshdirect" %>
<%
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
%>
<tmpl:get name="topwarningbar" />

<div id="locationbar">
	<div class="locabar-spacer"></div>
	<tmpl:get name="tab_fdx" />
	<tmpl:get name="tab_cos" />
	
	
	
	<%-- fright sections --%>
	<div class="locabar-right-sections">
		<tmpl:get name="alerts" /><tmpl:get name="zip_address" /><tmpl:get name="sign_in" /><tmpl:get name="cartTotal" />
	</div>
</div>

<div id="location-messages" class="invisible"><tmpl:get name="location_message" /></div>
<div id="unrecognized" class="invisible" data-type="sitemessage">
	<div class="unrecognized error-message">
		<p>
			<span class="orange">Sorry, we're unable to recognize the ZIP code <b>{{zip}}</b> you entered, please make sure it's entered correctly.</span><br>
			<span><a href="/help/delivery_zones.jsp" class="delivery-popuplink green">Click here</a> to see current delivery zones. To enter a different zip code, please enter in the box to the upper left.</span>
		</p>
	</div>
</div>

<fd:css href="/assets/css/common/locationbar_fdx.css" />
<fd:javascript src="/assets/javascript/locationbar.js" />
<fd:javascript src="/assets/javascript/locationbar_fdx.js" />

