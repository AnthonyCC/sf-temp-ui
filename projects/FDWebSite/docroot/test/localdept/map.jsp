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
<%@page import="com.freshdirect.cms.ContentNodeI"%><html lang="en-US" xml:lang="en-US">
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<head>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8" lang="en-US">
	<title>Producers' Map</title>
	<!-- Google Maps API -->
	<script type="text/javascript">
		if (/internal\.euedge\.com$/.test(window.location.host)) {
			// alert("OK");
		}
	</script>
	<!-- API key for euedge LAN -->
	<script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?sensor=false&key=<%= FDStoreProperties.getGoogleMapsAPIKey() %>">
	</script>
	<script type="text/javascript">
	google.load("maps", "2");
	 
    // Creates a marker whose info window displays the letter corresponding
    // to the given index.
    function createMarker(point) {
		// Create a lettered icon for this point using our icon class
		// var letter = String.fromCharCode("A".charCodeAt(0) + index);
		// var letteredIcon = new GIcon(baseIcon);
		// letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
		
		// Set up our GMarkerOptions object
		var marker = new google.maps.Marker({
							position : point,			
							map : map
		});
		
		var infowindow = new google.maps.InfoWindow({
		    content: "Marker <b>kkanuganti</b>"
		});
		
		google.maps.event.addListener(marker, 'click', function() {				
	    	infowindow.open(map, marker);
		});
		
		return marker;
    }

    function initialize() {
		
			 var mapOptions = {
			          zoom: 8,
			          // Center the map on New York, USA.
					  center: new google.maps.LatLng(41.75902,-72.625122),
			          mapTypeId: google.maps.MapTypeId.ROADMAP,
			
			          // map type control
			          mapTypeControl: true,
			          mapTypeControlOptions: {
			              style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
			              position: google.maps.ControlPosition.TOP_LEFT
			          },
			          
			          // zoom control
			          zoomControl: true,
			          zoomControlOptions: {
			            style: google.maps.ZoomControlStyle.LARGE
			          },		
			
			          // scale control
			          scaleControl: false,
			          scaleControlOptions: {
			              position: google.maps.ControlPosition.BOTTOM_RIGHT
			          },
			          
					  // Street Control
			          streetViewControl: true,
			          streetViewControlOptions: {
			              position: google.maps.ControlPosition.LEFT_TOP
			          }

			};
	   
			map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);

			// put markers
<%
	CmsManager mgr = CmsManager.getInstance();
	final Set<ContentKey> keys = mgr.getContentKeysByType(ContentType.get("Producer"));

	for (ContentKey k : keys) {
		ProducerModel p = (ProducerModel) ContentFactory.getInstance().getContentNode(k.getType(), k.getId());
		String[] coords = p.getLocation().split(",");
%>			createMarker(new google.maps.LatLng(<%= coords[0] %>, <%= coords[1] %>));
<%
	}
%>
		
	}
 
    </script> 
</head>
<body onload="initialize()">
	<!-- MAP --> 
	<div id="map_canvas" style="width: 695px; height: 695px"></div>
	<div id="message"></div>
</body> 
</html>