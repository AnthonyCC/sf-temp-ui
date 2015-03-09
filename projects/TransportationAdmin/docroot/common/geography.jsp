<%@ page import='com.freshdirect.transadmin.util.*' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Google Maps Location Match</title>
    <script type="text/javascript"
      src="//maps.googleapis.com/maps/api/js?v=<%= TransportationAdminProperties.getGMapVersion() %>&sensor=false&key=<%= request.getParameter("mapkey") %>">
	</script>
	<script type="text/javascript" language="javascript" src="../js/gmap/mapiconmaker.js"></script>
    <script type="text/javascript">

    //<![CDATA[

   var geocoder;
   var map;   
   var infoPanel;
   var geocodeMarker; 
   var boundz;

   var address = '<%= request.getParameter("address") %>';
   var latitude = '<%= request.getParameter("latitude") %>';
   var longitude = '<%= request.getParameter("longitude") %>';

   // On page load, call this function

   function load()
   {  
         
	   var mapOptions = {
		          zoom: 8,
		          // Center the map on New York, USA.
				  center: new google.maps.LatLng(40.739878, -73.947262),
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
		          scaleControl: true,
		          scaleControlOptions: {
		              position: google.maps.ControlPosition.BOTTOM_RIGHT
		          },
		          
				  // Street Control
		          streetViewControl: true,
		          streetViewControlOptions: {
		              position: google.maps.ControlPosition.LEFT_TOP
		          }

		};
 
	  map = new google.maps.Map(document.getElementById('map'), mapOptions);
      
      // Create new geocoding object
      geocoder = new google.maps.Geocoder();
      
   	  // put markers
	  boundz = new google.maps.LatLngBounds();

      // Retrieve location information, pass it to addToMap()
      geocodeAddress();      
   	  
   }
  
	
	function geocodeAddress() {		  
		
		var pointLatLng = new google.maps.LatLng(latitude, longitude);
		map.setCenter(pointLatLng, 12);
		boundz.extend(pointLatLng);
		
		var mapIconResponse = MapIconMaker.createLabeledMarkerIcon({label:'P', addStar: false});
	     
	    var infowindow = new google.maps.InfoWindow({
		      content: "Latitude="+ latitude + ", " + "Longitude=" + longitude
		});
	    
	    
	    var pointMarker = new google.maps.Marker({
				position : pointLatLng,
				icon : mapIconResponse.icon,
				map : map
			});
	    
	    google.maps.event.addListener(pointMarker, 'click', function() {
		    infowindow.open(map, pointMarker);
		});		
				
		geocoder.geocode({'address': address}, geocode_result_handler);

	}	

	function geocode_result_handler(result, status) {
		if (status != google.maps.GeocoderStatus.OK) {
			alert('Geocoding failed. ' + status);
		} else {
			map.fitBounds(result[0].geometry.viewport);					
			var latLng = result[0].geometry.location;
			map.setCenter(latLng, 10);
			boundz.extend(latLng);
			
			var marker_title = result[0].formatted_address + ' at '	+ latLng;
			
			var infowindow = new google.maps.InfoWindow({
			      content: marker_title
			});
			
			var mapIconResponse = MapIconMaker.createLabeledMarkerIcon({label:'A', addStar: false});
			
			if (geocodeMarker) {
				geocodeMarker.setPosition(latLng);
				geocodeMarker.setTitle(marker_title);
			} else {
				geocodeMarker = new google.maps.Marker({
					position : latLng,
					title : marker_title,
					icon : mapIconResponse.icon,
					map : map
				});				
				
				google.maps.event.addListener(geocodeMarker, 'click', function() {
				    infowindow.open(map, geocodeMarker);
				});
			}
			
			map.fitBounds(boundz);
			map.setCenter(boundz.getCenter());
		}
	}

</script>

  </head>
  <body onload="load()">
  	<div id="info-panel"></div>
    <div id="map" style="width: <%= request.getParameter("width") %>px; height: <%= request.getParameter("height") %>px"></div>
  </body>
</html>