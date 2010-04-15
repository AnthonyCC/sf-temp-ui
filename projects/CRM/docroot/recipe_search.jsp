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
    String recipeSource = request.getParameter("recipeSource");
    String recipeAuthor = request.getParameter("recipeAuthor");
     if (recipeSource!=null && !"".equals(recipeSource.trim()) ) {
        redirURL="/order/place_order_recipe_search_results.jsp?recipeSource="+recipeSource;
     }else if (recipeAuthor!=null && !"".equals(recipeAuthor.trim()) ) {
        redirURL="/order/place_order_recipe_search_results.jsp?recipeAuthor="+recipeAuthor;
     }
  }
     response.sendRedirect(response.encodeURL(redirURL));
 %>


