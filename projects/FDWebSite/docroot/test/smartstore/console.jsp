<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="bsh.Interpreter"%>
<%@page import="java.io.PrintStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/test/search/config.css" />
	<title>Console</title>
</head>
<body>
<%
    String script = request.getParameter("script");
	String result = null;
	if (request.getParameter("execute") != null) {
	    Interpreter i = new Interpreter();
		ByteArrayOutputStream b = new ByteArrayOutputStream();
	    PrintStream p = new PrintStream(b, true, "UTF-8");
	    i.setOut(p);
	    i.set("request", request);
	    i.set("session", session);
	    i.set("response", response);
	    i.set("out", out);
	    if (session != null) {
	        i.set("user", session.getAttribute(SessionName.USER));
	    }
	    try {
			i.eval(script);
	    } catch (Exception e) { 
	        p.println("<span style=\"color:red\">");
	        e.printStackTrace(p);
	        p.println("</span>");
	    }
		result = new String(b.toByteArray(), "UTF-8").replaceAll("\\\n", "<br/>");
	}

%>
	Result of the script:
	<div class="code"><code><%= result %></code></div>

 <form method="post">
 	<input type="hidden" name="execute" value="1"/>
 	<textarea name="script" rows="16" cols="160"><%= script %></textarea>
 	<input type="submit" value="Run"/>
 </form>
</body>
</html>	
