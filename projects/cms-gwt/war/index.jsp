<%@page import="java.util.Random"%><%
	response.sendRedirect("cmsgwt.html?rnd="+new Random().nextInt());
%>