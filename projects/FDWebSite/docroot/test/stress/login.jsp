<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUser"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.CookieMonster"%>
<%@page import="com.freshdirect.fdstore.customer.FDAuthenticationException"%>


<html>
<head>
<title>LOGIN</title>
</head>
<body>
<%

   FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);

   if ("new".equalsIgnoreCase(request.getParameter("session"))) {

      Enumeration e = session.getAttributeNames();
      while (e.hasMoreElements()) session.removeAttribute((String)e.nextElement());
      CookieMonster.clearCookie(response);
      currentUser = null;
   }

   if (currentUser == null) {
      try {
         String userId = request.getParameter("userid");
         String password = request.getParameter("password");

         if (userId != null && password != null) {
            FDIdentity identity = FDCustomerManager.login(userId,password);
            FDUser loginUser = FDCustomerManager.recognize(identity);
            
            if (currentUser == null) {
               UserUtil.createSessionUser(request, response, loginUser);
               currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
            } 
         }
      } catch (FDAuthenticationException e) {
         session.setAttribute(SessionName.USER,null);
         currentUser = null;
      }
   }
%>

<%
   if (currentUser != null && "true".equalsIgnoreCase(request.getParameter("info"))) {
      FDUser user = currentUser.getUser();
%>
      <ul>
         <li><b>Cookie:</b> <tt><%= user.getCookie() %></tt>
<%
     if (user.getIdentity() != null) {
%>
         <li><b>FD PK:</b> <tt><%= user.getIdentity().getFDCustomerPK() %></tt>
         <li><b>ERP PK:</b> <tt><%= user.getIdentity().getErpCustomerPK() %></tt>
<%
     }
%>
         <li><b>Level:</b> <tt><%= user.getLevel() %></tt>
         <li><b>New session:</b> <tt><%= session.isNew() %></tt>
      </ul>
<%
   } else {
%>
<b>Login failed.</b>
<%
   }
%>

</body>
</html>


