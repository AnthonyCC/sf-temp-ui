<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Google Maps Location Match</title>
    <script src="http://maps.google.com/maps?file=api&v=2&key=<%= request.getParameter("mapkey") %>"
      	type="text/javascript"></script>
    <script type="text/javascript">

    //<![CDATA[

   var geocoder;
   var map;

   var address = '<%= request.getParameter("address") %>';
   var latitude = '<%= request.getParameter("latitude") %>';
   var longitude = '<%= request.getParameter("longitude") %>';

   // On page load, call this function

   function load()
   {  
         
      // Create new map object
      map = new GMap2(document.getElementById("map"));
      
      map.addControl(new GSmallMapControl());
	  map.addControl(new GMapTypeControl());
	  map.enableDragging();
      

      // Create new geocoding object
      geocoder = new GClientGeocoder();

      // Retrieve location information, pass it to addToMap()
      geocoder.getLocations(address, addToMap);
   }

   // This function adds the point to the map

   function addToMap(response)
   {      
      // Retrieve the object
      place = response.Placemark[0];
      
      // Retrieve the latitude and longitude
      point = new GLatLng(latitude, longitude);
      
      addressPoint = new GLatLng(place.Point.coordinates[1],
                          place.Point.coordinates[0]);

      // Center the map on this point
      map.setCenter(point, 13);
            
      // Create a marker
      markerPoint = createMarker(point,"P",("Latitude="+latitude+"<br/>"+"Longitude="+longitude));
          
      addressMarker = createMarker(addressPoint,"A",address+"<br/>"
      					+("Latitude="+place.Point.coordinates[1]+"<br/>"+"Longitude="+place.Point.coordinates[0]));

      // Add the marker to map
      map.addOverlay(addressMarker);
      
      map.addOverlay(markerPoint);

   }
   
  
	  
	  // Creates a marker whose info window displays the letter corresponding
        // to the given index.
    function createMarker(point, letterCode, info) {
      var baseIcon = new GIcon();
	    baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
	    baseIcon.iconSize = new GSize(20, 34);
	    baseIcon.shadowSize = new GSize(37, 34);
	    baseIcon.iconAnchor = new GPoint(9, 34);
	    baseIcon.infoWindowAnchor = new GPoint(9, 2);
	    baseIcon.infoShadowAnchor = new GPoint(18, 25);     	
      // Create a lettered icon for this point using our icon class
      var letter = String.fromCharCode(letterCode.charCodeAt(0));
      var letteredIcon = new GIcon(baseIcon);
      letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";

      // Set up our GMarkerOptions object
      markerOptions = { icon:letteredIcon, draggable: true };
      var marker = new GMarker(point, markerOptions);

      GEvent.addListener(marker, "click", function() {
        marker.openInfoWindowHtml(info);
      });
      return marker;
    } 
  

    //]]>
    </script>
  </head>
  <body onload="load()" onunload="GUnload()">
    <div id="map" style="width: <%= request.getParameter("width") %>px; height: <%= request.getParameter("height") %>px"></div>
  </body>
</html>