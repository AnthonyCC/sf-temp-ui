<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUser"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>

<html>
<head>
<title>SAVE CART</title>
</head>
<body>
<%
   boolean saved = false;
   FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
   if (user != null) {
      user.saveCart(true);
      saved = true;
   }
%>

<% 
   if ("true".equalsIgnoreCase(request.getParameter("info"))) {
%>
      <b><%= saved ? "Saved." : "Failed." %></b>
<%
   }
%>
</body>
