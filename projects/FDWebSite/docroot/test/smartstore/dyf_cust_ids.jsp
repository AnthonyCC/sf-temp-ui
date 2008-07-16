<%
boolean CSVoutput = !("html".equalsIgnoreCase(request.getParameter("output")));

if (!CSVoutput) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<%
}
%><%@ page import="java.util.*"
%><%@ page import="com.freshdirect.test.TestSupport"
%><%

//
// Simple test page to test whether Test Support Session Bean is connected and alive.
// Usage: load this page and look for 'ping' message in logs
//
// @author segabor
//

TestSupport s = TestSupport.getInstance();

List ids = s.getDYFEligibleCustomerIDs();

if ("html".equalsIgnoreCase(request.getParameter("output"))) {
%>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>untitled</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="Sebesty�n G�bor">
	<!-- Date: 2008-05-21 -->
</head>
<body>
<h2>List of Eligible Customers</h2>
<%

Iterator it = ids.iterator();
while (it.hasNext()) {
	Long c_id = (Long) it.next();
	%><%= c_id %><br/>
<%
}

%>
</body>
</html>
<%
} else {
	// CSV output
	response.setHeader("Content-Type", "text/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=dyf_eligible_customer_ids.csv");
}
Iterator it = ids.iterator();
while (it.hasNext()) {
	Long c_id = (Long) it.next();
	%><%= c_id %>
<%
}
%>