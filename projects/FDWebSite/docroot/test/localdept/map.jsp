<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>

<%@page import="com.freshdirect.fdstore.content.ProducerModel"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.cms.application.ContentTypeServiceI"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.cms.application.service.xml.XmlTypeService"%>
<%@page import="com.freshdirect.cms.application.service.CompositeTypeService"%>
<%@page import="com.freshdirect.cms.application.service.xml.XmlContentService"%>
<%@page import="com.freshdirect.cms.application.service.xml.FlexContentHandler"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.cms.ContentNodeI"%><html>
<head>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
	<title>Producers' Map</title>
	<!-- Google Maps API -->
	<script type="text/javascript">
		if (/internal\.euedge\.com$/.test(window.location.host)) {
			// alert("OK");
		}
	</script>
	<!-- API key for euedge LAN -->
	<script type="text/javascript" src="http://www.google.com/jsapi?key=ABQIAAAA_vQMRhd3qlUmT3zj0jdnbRQ_Otc3iLgBzHJWFB--uAmRBu3eWhQE2TzLNhULb6hoCW_ruK3nho0pDg"></script>
	<script type="text/javascript">
	google.load("maps", "2");

	// define default FD marker icon
	var FD_ICON = new GIcon();
	FD_ICON.image = 'http://www.google.com/mapfiles/marker.png';
	 
    // Creates a marker whose info window displays the letter corresponding
    // to the given index.
    function createMarker(point) {
		// Create a lettered icon for this point using our icon class
		// var letter = String.fromCharCode("A".charCodeAt(0) + index);
		// var letteredIcon = new GIcon(baseIcon);
		// letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
		
		// Set up our GMarkerOptions object
		var marker = new GMarker(point, { icon: FD_ICON });
		
		GEvent.addListener(marker, "click", function() {
			marker.openInfoWindowHtml("Marker <b>Valami</b>");
		});
		return marker;
    }

    function initialize() {
		if (GBrowserIsCompatible()) {
			var map = new GMap2(document.getElementById("map_canvas"));
			map.setCenter(new GLatLng(41.75902,-72.625122), 8);
			map.setUIToDefault();
			
			// customize UI
			var customUI = map.getDefaultUI();
			customUI.controls.scalecontrol = false;
			map.setUI(customUI);

			// put markers
<%
	CmsManager mgr = CmsManager.getInstance();
	final Set<ContentKey> keys = mgr.getContentKeysByType(ContentType.get("Producer"));

	for (ContentKey k : keys) {
		ProducerModel p = (ProducerModel) ContentFactory.getInstance().getContentNode(k.getType(), k.getId());
		String[] coords = p.getLocation().split(",");
%>			map.addOverlay(createMarker(new GLatLng(<%= coords[0] %>, <%= coords[1] %>)));
<%
	}
%>
		}
	}
 
    </script> 
</head>
<body onload="initialize()" onunload="GUnload()">
	<!-- MAP --> 
	<div id="map_canvas" style="width: 695px; height: 695px"></div>
	<div id="message"></div>
</body> 
</html>