<%@ page import='java.util.*' %>
<%@ page import='java.io.*' %>
<%-- JSP file to support dynamic inclusion of i_generic_recommendations --%><%
	final String smartStoreFacility = request.getParameter("smartStoreFacility");
%><%@ include file="i_generic_recommendations.jspf" %>