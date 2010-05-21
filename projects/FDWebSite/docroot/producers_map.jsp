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
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
BrowserInfo bi = new BrowserInfo(request);

String catId = request.getParameter("catId");

if (catId == null)
	throw new JspException("Missing category identifier.");

CategoryModel cat = (CategoryModel) ContentFactory.getInstance().getContentNode(catId);
if (cat == null)
	throw new JspException("Missing category " + catId);

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
			ic.image = '<%= StringEscapeUtils.escapeJavaScript( icon.getPath() )  %>';
			ic.iconSize = new GSize(<%= icon.getWidth() %>, <%= icon.getHeight() %>);
<%
			if (shadow != null) {
%>			ic.shadow = '<%= StringEscapeUtils.escapeJavaScript( shadow.getPath() )  %>';
			ic.shadowSize = new GSize(<%= shadow.getWidth() %>, <%= shadow.getHeight() %>);
<%
			}
%>			ic.iconAnchor = new GPoint(<%= icon.getWidth()/2 %>, <%= icon.getHeight() %>);
<% 			if (bi.isFirefox()) {%>
				ic.imageMap = [0, 0, <%= icon.getWidth()-1 %>, 0, <%= icon.getWidth()-1 %>, <%= icon.getHeight()-1 %>, 0, <%= icon.getHeight()-1 %>];
<% 			} %>
			
			marker = new GMarker(point, { icon: ic });
<%
		} else {
%>			marker = new GMarker(point, { icon: FD_ICON });
<%
		}
%>			GEvent.addListener(marker, "click", function() {
				var winId = 'prod-<%= p.getContentKey().getId() %>';
				var kontent = document.getElementById(winId);
				var klone = kontent.cloneNode(true);
				klone.setAttribute("id", winId+'-act');
				this.openInfoWindowHtml(klone);
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
		<%-- CATEGORY HEADER START--%>
		<div id="category-header">
<%
if (cat != null) {
    //
    // Category Label (Image)
    //
    if (cat.getCategoryLabel() != null) {
        String categoryLabelPath = cat.getCategoryLabel().getPath();
%>
            <IMG SRC="<%= categoryLabelPath %>" border="0" ALT="<%= cat.getFullName() %>"><br>
<%
    }
    
    //
    // Render Editorial (partial HTML)
    //
    Html editorialMedia = cat.getEditorial();
    if (true &&
    		editorialMedia != null &&
    		cat.getCategoryLabel() != null &&
    		!editorialMedia.isBlank()) {
%>
            <div style="padding: 5px 0 8px 0">
	            <fd:IncludeMedia name='<%= editorialMedia.getPath() %>'/><br>
            </div>
<%
    }
}
%>
		</div>
		<%-- CATEGORY HEADER END --%>
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
	<div id="prod-<%= p.getContentKey().getId() %>" style="clear:both; line-height: 12px">
		<div class="title12" style="text-align: left"><%= p.getFullName() %></div>
		<% if (p.getBubbleContent() != null ) {
			Html content = p.getBubbleContent();
		%>
		<br>
		<div style="text-align: left;">
			<fd:IncludeMedia name='<%=p.getBubbleContent().getPath() %>'/>
		</div>
		<% }
		
		%>
		<br>
		<div style="text-align: left">
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
%>			<br><a href="/unsupported.jsp" onclick="popup('/brandpop.jsp?brandId=<%= bm %>', 'large'); return false;" style="font-weight: bold;">Learn more &hellip;</a>
		</div>
		<br>
	</div>

<%	
	}
%>
</div>
	
	</tmpl:put>
</tmpl:insert>
</fd:ProducerList>