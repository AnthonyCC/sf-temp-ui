<% 
	response.setStatus(301);
	response.setHeader( "Location", response.encodeRedirectURL("/quickshop/qs_past_orders.jsp"));
	response.setHeader( "Connection", "close" );
%>