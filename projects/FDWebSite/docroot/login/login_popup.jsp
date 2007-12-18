<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%--
String template = "/common/template/no_nav.jsp";
	//diff nav for popup login
	//if ("popup".equals( request.getParameter("type") ))
String sPage = request.getParameter("successPage");
	if (sPage != null && sPage.indexOf("type=popup") != -1)
	template = "/common/template/large_pop.jsp";
--%>
<fd:CheckLoginStatus/>
<tmpl:insert template='/common/template/large_pop.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Log In</tmpl:put>
<tmpl:put name='content' direct='true'>
<table border="0" cellspacing="0" cellpadding="0" width="500" align="center">
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="20"></td></tr>
        <tr>
            <td width="300">
				<img src="/media_stat/images/navigation/current_cust_log_in_now.gif" width="222" HEIGHT="13" border="0" alt="CURRENT CUSTOMERS LOG IN NOW"></td>
        	<td width="200" align="right"><font class="text9">* Required Information</font></td>
		</tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
		<tr><td colspan="2" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
		<tr><td colspan="2">
		<table border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td width="110"><IMG src="/media_stat/images/layout/clear.gif" width="110" HEIGHT="1" border="0"><BR></td>
			<td class="text13" width="390">
				<%@ include file="/includes/i_login_field.jspf" %>
				<br><br>
			</td>
		</tr>
		</table>
		</td>
		</tr>
</table>

</tmpl:put>
</tmpl:insert>
