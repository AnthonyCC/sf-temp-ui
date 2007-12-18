<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
String bookRetailerId = request.getParameter("bookRetailerId");
BookRetailer bookRetailer = (BookRetailer) ContentFactory.getInstance().getContentNode(bookRetailerId);

String recipeSourceId = request.getParameter("recipeSourceId");
RecipeSource recipeSource = (RecipeSource) ContentFactory.getInstance().getContentNode(recipeSourceId);

if (bookRetailer != null && recipeSource != null) {
	
	FDEventUtil.logBookRetailerRedirect(request);	
	
	String link = bookRetailer.createIsbnLink(recipeSource.getIsbn());
	response.sendRedirect(response.encodeRedirectURL(link));
}
%>