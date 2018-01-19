<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.util.ClientDataValidator" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<%
	String encodedUrlQuery = request.getQueryString() != null? (URLEncoder.encode("&" + request.getQueryString(), "UTF-8")): ""; 
	pageContext.setAttribute("encodedUrlQuery", encodedUrlQuery);
	
%>
	<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage="/login/login.jsp?template=oauth${encodedUrlQuery}" />
<%
	String redirectUrl = request.getParameter("successPage");
	response.sendRedirect(redirectUrl);
	
%>
	
