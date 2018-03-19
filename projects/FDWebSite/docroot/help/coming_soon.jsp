<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Help"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put> --%>
		<tmpl:put name='content' direct='true'>

<table border="0" cellpadding="0" cellspacing="0" width="470">
<tr>
	<td width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="200" alt="" border="0"></td>
	<td>
		<img src="/media_stat/images/template/help/pg_coming_soon.gif" width="426" height="113" alt="this page comming soon" border="0">
	</td>
</tr>
		<tr><td></td>
		    <TD align="center">
				<a href="/index.jsp" onmouseover="swapImage('home_img','/media_stat/images/template/help/help_home_r.gif')" onmouseout="swapImage('home_img','/media_stat/images/template/help/help_home.gif')"><img src="/media_stat/images/template/help/help_home.gif" name="home_img" width="71" height="26" alt="return to home page" border="0"></a>
				<br>Continue shopping from <a href="/index.jsp">Home Page</a>
				<br><br>
			</TD>		
		</tr>
</table>

</tmpl:put>
</tmpl:insert>