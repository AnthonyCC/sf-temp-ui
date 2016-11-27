<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>
<%@ page import='java.net.URLEncoder' %>
<%
    String redirectURL = "/site_access/site_access_address.jsp?successPage=/index.jsp&serviceType=" + EnumServiceType.CORPORATE.getName();
    response.sendRedirect(redirectURL);
%>

