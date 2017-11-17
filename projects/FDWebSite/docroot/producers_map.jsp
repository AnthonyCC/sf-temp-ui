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
<%@page import="com.freshdirect.fdstore.content.PopulatorUtil"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.fdstore.FDNotFoundException"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_PRODUCERS_MAP = 925;
%>

<fd:CheckLoginStatus />
<%
BrowserInfo bi = new BrowserInfo(request);

String catId = request.getParameter("catId");

if (catId == null){
	throw new FDNotFoundException("Missing category identifier.");
}

CategoryModel cat = (CategoryModel) PopulatorUtil.getContentNode(catId);

%><fd:ProducerList id="prodz" needsValidGeolocation="<%= true %>" skipBodyOnEmptyResult="<%= false %>">
<tmpl:insert template='/common/template/gmap_nav.jsp'>
	<tmpl:put name='leftnav' direct='true'> <%-- <<< some whitespace is needed here --%></tmpl:put>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - What's Local"/>
    </tmpl:put>
	<tmpl:put name='title' direct='true'>FreshDirect - What's Local</tmpl:put>
	<%-- SCRIPTS IN HEADER --%>
	<tmpl:put name='head_content'>

<script type="text/javascript">
	
	
	/**
	* Returns the zoom level at which the given rectangular region fits in the map view. 
	* The zoom level is computed for the currently selected map type. 
	* @param {google.maps.Map} map
	* @param {google.maps.LatLngBounds} bounds 
	* @return {Number} zoom level
	**/
	function getZoomByBounds( map, bounds ){
	  var MAX_ZOOM = map.mapTypes.get( map.getMapTypeId() ).maxZoom || 21 ;
	  var MIN_ZOOM = map.mapTypes.get( map.getMapTypeId() ).minZoom || 0 ;
	
	  var ne= map.getProjection().fromLatLngToPoint( bounds.getNorthEast() );
	  var sw= map.getProjection().fromLatLngToPoint( bounds.getSouthWest() ); 
	
	  var worldCoordWidth = Math.abs(ne.x-sw.x);
	  var worldCoordHeight = Math.abs(ne.y-sw.y);
	
	  //Fit padding in pixels 
	  var FIT_PAD = 40;
	
	  for( var zoom = MAX_ZOOM; zoom >= MIN_ZOOM; --zoom ){ 
	      if( worldCoordWidth*(1<<zoom)+2*FIT_PAD < $(map.getDiv()).width() && 
	          worldCoordHeight*(1<<zoom)+2*FIT_PAD < $(map.getDiv()).height() )
	          return zoom;
	  }
	  return 0;
	}
	
	var map;

	// initialize map
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
		
		// Geocoder API
		var geocoder = new google.maps.Geocoder();
			
		// put markers
		var boundz = new google.maps.LatLngBounds();
		
		var point, ic, marker;
		
		var infowindow = new google.maps.InfoWindow();

<%
	for (ProducerModel p : prodz) {
		ProducerModel.Geolocation loc = p.getGeolocation();		
		if (loc != null) {
%>			// '<%= p.getFullName() %>'
			point = new google.maps.LatLng(<%= loc.lat %>, <%= loc.lng %>);
			boundz.extend(point);

<%			Image icon = p.getIconImage();			
			Image shadow = p.getIconShadowImage();
			if (icon != null) {
			
%>			ic = {};
			ic.url = '<%= StringEscapeUtils.escapeJavaScript( icon.getPath() )  %>';
			ic.size = new google.maps.Size(<%= icon.getWidth() %>, <%= icon.getHeight() %>);
			ic.anchor = new google.maps.Point(<%= icon.getWidth()/2 %>, <%= icon.getHeight() %>);
			
			marker = new google.maps.Marker({
						position : point,
						icon : ic,
						map : map
			});
			
<%
		} else {
%>
			marker = new google.maps.Marker({
						position : point,			
						map : map
			});
<%
		}
%>							
			google.maps.event.addListener(marker, 'click', (function(marker) {
			    return function() {
			    	var winId = 'prod-<%= p.getContentKey().getId() %>';
					var kontent = document.getElementById(winId);
					var klone = kontent.cloneNode(true);
					klone.setAttribute("id", winId+'-act');
			        infowindow.setContent(klone);
			        infowindow.open(map, marker);
			    }
   			  })(marker));			
<%
		}
	}
%>
			// fit all markers
			//map.setZoom(getZoomByBounds(map, boundz));
			map.fitBounds(boundz);
			map.setCenter(boundz.getCenter());
		
    }
</script>

	</tmpl:put>
	<%-- CONTENT --%>
	<tmpl:put name='content' direct='true'>
	<div id="inner-container" style="width: <%=W_PRODUCERS_MAP%>px">
		<%-- CATEGORY HEADER START--%>
		<div id="category-header">
			<% if (cat != null) {
			    // Category Label (Image)
			    if (cat.getCategoryLabel() != null) {
			        String categoryLabelPath = cat.getCategoryLabel().getPath();
					%><img src="<%= categoryLabelPath %>" border="0" alt="<%= cat.getFullName() %>"><br/><%
    			}    
			    // Render Editorial (partial HTML)
			    Html editorialMedia = cat.getEditorial();
			    if ( editorialMedia != null && !editorialMedia.isBlank() ) { %>
		            <div style="padding: 5px 0 8px 0">
			            <fd:IncludeMedia name='<%= editorialMedia.getPath() %>'/><br/>
		            </div>
				<% }
			} %>
		</div>
		<%-- CATEGORY HEADER END --%>
		<!-- MAP -->
		<div style="padding: 1em 0 1em 0;">
			<div id="map_canvas" style="width: <%=W_PRODUCERS_MAP-2%>px; height: <%=W_PRODUCERS_MAP-2%>px; border: 2px solid #aaa"></div>
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
%>			<br><a href="/unsupported.jsp" onclick="popup('/shared/brandpop.jsp?brandId=<%= bm %>', 'large'); return false;" style="font-weight: bold;">Learn more &hellip;</a>
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