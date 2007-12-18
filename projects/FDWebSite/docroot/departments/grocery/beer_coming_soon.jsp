<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<% 

String catId = request.getParameter("catId"); 


%>
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Beer</tmpl:put>
    <tmpl:put name='content' direct='true'>

<table border="0" cellpadding="0" cellspacing="0" width="470">
<tr>
	<td>
		<img src="/media_stat/images/template/grocery/dept_manager_BRIAN_beer.jpg" width="126" height="235" alt="" border="0">
	</td>	
	<td valign="top">
			<img src="/media_stat/images/template/grocery/beer_mugs.jpg" width="490" height="100" alt="" border="0"><br>
			<img src="/media_stat/images/template/grocery/here_here.gif" width="483" height="103" alt="" border="0"><br>
				We'll carry a wide selection of your favorite domestics, imports, and microbrews. Come back soon and get all your beer here.			
	</td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>
