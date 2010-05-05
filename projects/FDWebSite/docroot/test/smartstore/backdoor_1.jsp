<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<%@ page import="java.util.*"
%><%@ page import="com.freshdirect.fdstore.customer.FDIdentity"
%><%@ page import="com.freshdirect.test.TestSupport"
%><%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"
%><%@ page import="com.freshdirect.fdstore.customer.FDUser"%>
<%

//
// Simple test page to test whether Test Support Session Bean is connected and alive.
// Usage: load this page and look for 'ping' message in logs
//
// @author segabor
//

TestSupport s = TestSupport.getInstance();

FDUser loginUser = null;
String erp_id = request.getParameter("erpId");
String user_id = request.getParameter("userId");
if (user_id != null) {
	// determine ERP ID from Customer's User ID (ie. email address)
	erp_id = s.getErpIDForUserID(user_id);
}
if (erp_id != null) {
	String cust_id = s.getFDCustomerIDForErpId(erp_id);
	if (cust_id != null) {
		FDIdentity me = new FDIdentity(erp_id, cust_id);
        loginUser = FDCustomerManager.recognize(me);

        UserUtil.createSessionUser(request, response, loginUser);
	}
}

%>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Find User by ERP ID</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="Sebestyén Gábor">
	<!-- Date: 2008-05-21 -->
</head>
<body>
<% if (loginUser != null) { %>
<b>User <%= loginUser.getFirstName() %> <%= loginUser.getLastName() %> logged in successfully!</b><br/>
FD Customer ID: <%= loginUser.getPK().getId() %><br/>
<% } else { %>
<b>User not found with ERP ID <%= erp_id %> / User ID: <%= user_id %></b><br/>
<% } %>
</body>
</html>
