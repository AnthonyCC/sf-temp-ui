<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:CheckLoginStatus id="user" />
<%
  String serviceType = (user.isCorporateUser()) ? "CORPORATE" : "HOME";
  String url = "/index.jsp?serviceType=" + serviceType;
  response.sendRedirect(url);
%>
