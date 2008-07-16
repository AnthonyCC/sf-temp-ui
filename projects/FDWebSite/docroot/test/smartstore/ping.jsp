<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<%@ page import="com.freshdirect.test.TestSupport" %><%

//
// Simple test page to test whether Test Support Session Bean is connected and alive.
// Usage: load this page and look for 'ping' message in logs
//
// @author segabor
//

TestSupport s = TestSupport.getInstance();

s.ping();
%>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>TestSupport - Ping Page</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="Sebestyén Gábor">
	<!-- Date: 2008-05-21 -->
</head>
<body>
	<b>PING</b>
</body>
</html>
