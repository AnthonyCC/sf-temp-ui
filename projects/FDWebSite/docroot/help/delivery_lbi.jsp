<% 
	response.setStatus(301);
	response.setHeader( "Location", response.encodeRedirectURL("/help/delivery_jersey_shore.jsp"));
	response.setHeader( "Connection", "close" );
%>