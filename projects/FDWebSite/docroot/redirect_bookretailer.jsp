<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
String bookRetailerId = request.getParameter("bookRetailerId");
BookRetailer bookRetailer = (BookRetailer) PopulatorUtil.getContentNode(bookRetailerId);

String recipeSourceId = request.getParameter("recipeSourceId");
RecipeSource recipeSource = (RecipeSource) PopulatorUtil.getContentNode(recipeSourceId);

if (bookRetailer != null && recipeSource != null) {
	String link = bookRetailer.createIsbnLink(recipeSource.getIsbn());
	response.sendRedirect(response.encodeRedirectURL(link));
}
%>