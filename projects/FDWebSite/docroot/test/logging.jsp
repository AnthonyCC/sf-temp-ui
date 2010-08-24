<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
final Logger LOG = LoggerFactory.getInstance("logging.jsp");
%>
<%
System.out.println("logging.jsp: standard output");
System.err.println("logging.jsp: standard error");
try {
	throw new Exception("logging.jsp: exception.printStackTrace()");
} catch (Exception e) {
	e.printStackTrace();
}
try {
	throw new Exception("logging.jsp: exception.printStackTrace(System.out)");
} catch (Exception e) {
	e.printStackTrace(System.out);
}
try {
	throw new Exception("logging.jsp: exception.printStackTrace(System.err)");
} catch (Exception e) {
	e.printStackTrace(System.err);
}
LOG.info("log4j info message test");
LOG.warn("log4j warn message test");
LOG.error("log4j error message test");
try {
	throw new Exception("log4j exception test");
} catch (Exception e) {
	LOG.error("log4j exception test", e);
}
%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Logging test page</title>
</head>
<body>
This page does not print out anything just generates various different types of log entries. Please take a look at the log files.
</body>
</html>