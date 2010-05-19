<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.ProducerModel"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.fdstore.content.BrandModel"%>
<%@page import="com.freshdirect.fdstore.content.EnumPopupType"%>
<%@page import="com.freshdirect.fdstore.content.TitledMedia"%>
<%@page import="com.freshdirect.fdstore.content.Image"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
	final BrowserInfo bi = new BrowserInfo(request);
	System.err.println(bi);
%><fd:ProducerList id="prodz" needsValidGeolocation="<%= true %>" skipBodyOnEmptyResult="<%= false %>">
<tmpl:insert template='/common/template/gmap_nav.jsp'>
	<tmpl:put name='leftnav' direct='true'> <%-- <<< some whitespace is needed here --%></tmpl:put>
	<tmpl:put name='title' direct='true'>FreshDirect - What's Local</tmpl:put>
	<%-- SCRIPTS IN HEADER --%>
	<tmpl:put name='head_content'>

<script type="text/javascript">
	// define default FD marker icon
	var FD_ICON = new GIcon(G_DEFAULT_ICON);
	FD_ICON.image = 'http://www.google.com/mapfiles/marker.png';


	// initialize map
	function initialize() {
		if (GBrowserIsCompatible()) {
			var map = new GMap2(document.getElementById("map_canvas"));
			map.setCenter(new GLatLng(41.75902,-72.625122), 8);
			map.setUIToDefault();

			// Geocoder API
			var geocoder = new GClientGeocoder();
			
			// customize UI
			var customUI = map.getDefaultUI();
			customUI.controls.scalecontrol = false;
			map.setUI(customUI);

			// put markers
			var boundz = new GLatLngBounds();

			var point, ic, marker;

<%
	for (ProducerModel p : prodz) {
		ProducerModel.Geolocation loc = p.getGeolocation();
		if (loc != null) {
%>			// '<%= p.getFullName() %>'
			point = new GLatLng(<%= loc.lat %>, <%= loc.lng %>);
			boundz.extend(point);

<%			Image icon = p.getIconImage();
			Image shadow = p.getIconShadowImage();
			if (icon != null) {
			
%>			ic = new GIcon(G_DEFAULT_ICON);
			ic.image = '<%= StringEscapeUtils.escapeJavaScript( icon.getPath())  %>';
			ic.iconSize = new GSize(<%= icon.getWidth() %>, <%= icon.getHeight() %>);
<%
			if (shadow != null) {
%>			ic.shadowImage = '<%= StringEscapeUtils.escapeJavaScript( shadow.getPath())  %>';
			ic.shadowSize = new GSize(<%= icon.getWidth() %>, <%= shadow.getHeight() %>);
<%
			}
%>			ic.iconAnchor = new GPoint(<%= icon.getWidth()/2 %>, <%= icon.getHeight() %>);
<% 			if (bi.isFirefox()) {%>
			ic.imageMap = [0, 0, <%= icon.getWidth()-1 %>, 0, <%= icon.getWidth()-1 %>, <%= icon.getHeight()-1 %>, 0, <%= icon.getHeight()-1 %>];
<% } %>
			
			marker = new GMarker(point, { icon: ic });
<%
		} else {
%>			marker = new GMarker(point, { icon: FD_ICON });
<%
		}
%>			GEvent.addListener(marker, "click", function() {
				var kontent = document.getElementById('prod-<%= p.getContentKey().getId() %>');
				this.openInfoWindowHtml(kontent.innerHTML);
			});

			map.addOverlay(marker);

<%
		}
	}
%>
			// fit all markers
			map.setZoom(map.getBoundsZoomLevel(boundz));
			map.setCenter(boundz.getCenter());

		}
    }
</script>

	</tmpl:put>
	<%-- CONTENT --%>
	<tmpl:put name='content' direct='true'>
	<div id="inner-container" style="width: 720px">
		<div id="inner-header" style="text-align: left;">
			<div class="title16">We’ve Made a Stand for Local Farms!</div>
			<div>
			From the Finger Lakes to the tip of Long Island, FreshDirect has traveled within 300 miles of New York City to source our Local Market products from the best farms, dairies and artisans. Click the icons on this map to learn more about our favorite local producers.
			</div>
		</div>
		<!-- MAP -->
		<div style="padding: 1em 0 1em 0;">
			<div id="map_canvas" style="width: 718px; height: 718px; border: 2px solid #aaa"></div>
			<div id="message"></div>
		</div>
	</div>
<div id="les_bubbles" style="display: none">
<%
	for (ProducerModel p : prodz) {
%>
	<div id="prod-<%= p.getContentKey().getId() %>">
		<div class="title12" style="text-align: left"><%= p.getFullName() %></div>
		<% if (p.getBubbleContent() != null ) {
			Html content = p.getBubbleContent();
		%>
		<div style="text-align: left; overflow: hidden;">
			<fd:IncludeMedia name='<%=p.getBubbleContent().getPath() %>'/>
		</div>
		<% }
		
		%>
		<div style="padding: 1em 0 1em 0; text-align: left">
			<a href="<%= FDURLUtil.getCategoryURI(p.getBrandCategory(), "lpmp") %>" style="font-weight: bold"><%= p.getFullName() %></a>
<%
	for (String line : p.getAddress().split("\n")) {
%>			<div><%= line %></div>
<%
	}

	// Show brand popup
	BrandModel bm = p.getBrand();
	Html popupContent = bm.getPopupContent();
		
	TitledMedia tm = (TitledMedia)popupContent;
	// EnumPopupType popupType = EnumPopupType.LARGE /* EnumPopupType.getPopupType(tm.getPopupSize()) */;
%>			<div><br><a href="/unsupported.jsp" onclick="popup('/brandpop.jsp?brandId=<%= bm %>', 'large'); return false;" style="font-weight: bold;">Learn more &hellip;</a></div>
		</div>
	</div>

<%	
	}
%>
</div>
	
	</tmpl:put>
</tmpl:insert>
</fd:ProducerList>