<%
	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	response.setHeader("Location", "/login/login.jsp");
	response.flushBuffer();
	//response.sendRedirect("/login/login.jsp");
%>