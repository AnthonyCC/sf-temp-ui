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
            //Recognize method loads the user info and also evaluates promotion
            //through updateUserState() method call.
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
   if ("true".equalsIgnoreCase(request.getParameter("info"))) {
%>
      <b><%= currentUser != null ? "Login Successful." : "Login Failure." %></b>
<%
   }
%>

</body>
</html>


