<%@page import="java.util.Random"%><%
	if (request.getSession(false)!=null) {
	    request.getSession(false).invalidate();
	}
	response.sendRedirect("index.jsp?rnd="+new Random().nextInt());
%>