<% 
	response.setStatus(301);
	response.setHeader( "Location", response.encodeRedirectURL("/help/delivery_hamptons.jsp"));
	response.setHeader( "Connection", "close" );
%>