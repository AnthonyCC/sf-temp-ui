<%
	response.setStatus(301);
	response.setHeader( "Location", response.encodeRedirectURL("/quickshop/qs_shop_from_list.jsp"));
	response.setHeader( "Connection", "close" );
%>