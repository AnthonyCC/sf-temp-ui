<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.lang.management.ManagementFactory"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.io.FileOutputStream"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="en-US">
<title>Session Dump</title>
</head>
<body>
<%
String filename = "/tmp/session-" + ManagementFactory.getRuntimeMXBean().getName() + Thread.currentThread().getId() + ".ser";
ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
os.writeObject(session);
os.close();
os = null;
%>
Session dumped to <%= filename %>
</body>
</html>