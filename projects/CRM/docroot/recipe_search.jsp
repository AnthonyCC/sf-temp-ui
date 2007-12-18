<%@ page import='java.text.*' %>

<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


 <%
  // since there is no recipe search, go to the crm build order search page.
  String keyword = request.getParameter("keyword");
  String redirURL=null;
  if (keyword!=null && !"".equals(keyword.trim()) ) {
  	redirURL="/order/place_order_batch_search_results.jsp?new_term="+keyword;
  } else {
  	redirURL="/order/place_order_build.jsp";
  }
  response.sendRedirect(response.encodeURL(redirURL));
%>
