<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@page import="java.util.List"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<% 
String catId = request.getParameter("catId");
CategoryModel press = (CategoryModel)ContentFactory.getInstance().getContentNode(catId);

List ra = press.getArticles();

int max = ra.size();

max--;

String articleIndex = request.getParameter("articleIndex");

int ai;

try {

ai = Integer.parseInt(articleIndex);

} catch (NumberFormatException Ex){
	ai = 0;
}

String article_path = "/about/press/article.jsp?catId=about_press_recent&articleIndex=";
int next_ai;

if (ai < max) {
	next_ai = ai + 1;
} else {
	next_ai = 0;
}
%>
<tmpl:insert template='/common/template/left_dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - About FreshDirect: Press</tmpl:put>
<tmpl:put name='content' direct='true'>
<table cellpadding="0" cellspacing="0" border="0" width="568">
<tr><td align="right"><a href="<%=article_path%><%=next_ai%>"><img src="/media_stat/images/template/about/press/next_recent_article.gif" width="127" height="13" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>
<tr><td>
<fd:IncludeMedia name="<%= ((ArticleMedia)ra.get(ai)).getPath() %>" />
</tr></td>
</table>
</tmpl:put>
</tmpl:insert>