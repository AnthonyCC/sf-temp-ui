<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.freshdirect.fdstore.content.ProducerModel"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
	// prepare data
	CmsManager mgr = CmsManager.getInstance();
	final Set<ContentKey> keys = mgr.getContentKeysByType(ContentType.get("Producer"));

	List<ProducerModel> prods = new ArrayList<ProducerModel>();
	
	// sort out bad producers
	for (ContentKey k : keys) {
		ProducerModel p = (ProducerModel) ContentFactory.getInstance().getContentNode(k.getType(), k.getId());
		if (p.getLocation() != null) {
			prods.add(p);
		}
	}

	final BrowserInfo bi = new BrowserInfo(request);
	
%><tmpl:insert template='/common/template/gmap_nav.jsp'>
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
			var n_producers = <%= prods.size() %>;
<%
	for (ProducerModel p : prods) {
%>
			geocoder.getLatLng('<%= StringEscapeUtils.escapeJavaScript(p.getLocation()) %>', function(point) {
				n_producers--;
				if (point) {
					boundz.extend(point);

<%		if (p.getProducerType().getIconImage() != null) {
%>					var ic = new GIcon(G_DEFAULT_ICON);
					ic.image = '<%= StringEscapeUtils.escapeJavaScript( p.getProducerType().getIconImage().getPath())  %>';
<%
		} else {
%>					var ic = FD_ICON;
<%
		}
%>					var marker = new GMarker(point, { icon: ic });
	
					GEvent.addListener(marker, "click", function() {
						var kontent = document.getElementById('prod-<%= p.getContentKey().getId() %>');
						marker.openInfoWindowHtml(kontent.innerHTML);
					});
	
					map.addOverlay(marker);

				}

				// fit all markers
				if (n_producers == 0) {
					map.setZoom(map.getBoundsZoomLevel(boundz));
					map.setCenter(boundz.getCenter());
				}
			});
<%
	}
%>

		}
    }
</script>
	</tmpl:put>
	<%-- CONTENT --%>
	<tmpl:put name='content' direct='true'>
	<div id="inner-container" style="width: 720px">
		<div id="inner-header" style="text-align: left;">
			<div class="title16">Location, Location, Location</div>
			<div>
			Freshdirect local products are, of course, local &ndash; only items raised, fished or made in New York, New Jersey and Connecticut are considered for inclusion.
			You'll find seafood and produce from the East End of Long Island, wine from the vineyards of the Finger Lakes region. artisanal cheeses from the dairy farms of the Hudson Valley, and much more.
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
	for (ProducerModel p : prods) {
%>
	<div id="prod-<%= p.getContentKey().getId() %>">
		<div class="title12" style="text-align: left"><%= p.getFullName() %></div>
		<% if (p.getIconImage() != null) { %><div style="text-align: center;">
			<img src="<%= p.getIconImage().getPath() %>"></img>
		</div><% } %>
		<% if (p.getBubbleContent() != null ) {
			Html content = p.getBubbleContent();
			String dimStr = "";
			
			if (content.getWidth() > 0) {
				int w = content.getWidth();
				if (w > 300)
					w = 300;
				dimStr += "width: "+w+"px; ";
			} else {
				dimStr += bi.isInternetExplorer() ? "width: 300px; " : "max-width: 300px; ";
			}
			if (content.getHeight() > 0) {
				dimStr += "height: "+content.getHeight()+"px; ";
			}
		
		%>
		<div style="overflow: hidden; <%= dimStr %>">
			<fd:IncludeMedia name='<%=p.getBubbleContent().getPath() %>'/>
		</div>
		<% } %>
		<div style="padding: 1em 0 1em 0; text-align: left">
			<a href="<%= FDURLUtil.getCategoryURI(p.getBrandCategory(), "lpmp") %>" style="font-weight: bold"><%= p.getFullName() %></a>
<%
	for (String line : p.getAddress().split("\n")) {
%>			<div><%= line %></div>
<%
	}
%>			<a href="<%= FDURLUtil.getCategoryURI(p.getBrandCategory(), "lpmp") %>" style="padding-top: 1em; display: block; font-weight: bold">Learn more &hellip;</a>
		</div>
	</div>

<%	
	}
%>
</div>
	
	</tmpl:put>
</tmpl:insert>
