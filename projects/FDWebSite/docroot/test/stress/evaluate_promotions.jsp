<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUser"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.promotion.FDPromotionVisitor"%>
<%@page import="com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter"%>

<html>
<head>
<title>SAVE CART</title>
</head>
<body>
<%
   boolean evaluated = false;
   FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
   if (user != null) {
      FDPromotionVisitor.applyPromotions(new PromotionContextAdapter(user.getUser()));
      evaluated = true;
   }
%>

<% 
   if ("true".equalsIgnoreCase(request.getParameter("info"))) {
%>
      <b><%= evaluated ? "Evaluation Sucessful." : "Evaluation Failed." %></b>
<%
   }
%>
</body>
