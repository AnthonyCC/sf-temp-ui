<%@ page import='com.freshdirect.webapp.util.JspMethods' %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>FreshDirect</title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body bgcolor="#FFFFFF" text="#333333" class="text10" leftmargin="0" topmargin="0" onLoad="<%= request.getParameter("depotAccessCode")!=null && request.getMethod().equals("GET") ? "document.site_access.submit();" : ""%>">

<br><br><br>
<table border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td align="center">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0"><br>
			<tmpl:get name='splash'/>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><br>
			<img src="/media_stat/images/layout/cccccc.gif" width="270" height="1" border="0"><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="15" border="0"><br>
		</td>
	</tr>
	<tr>
		<td align="center">
		<table width="300" cellpadding="0" cellspacing="0" border="0">
		<form name="site_access" method="post">
		<tr><td align="center">
			Enter your Freshdirect access code:<br> 
			<img src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"><br>
			<input class="text11" type="text" size="21" value="<%= request.getParameter("depotAccessCode")!=null?request.getParameter("depotAccessCode"):"" %>"  name="depotAccessCode" required="true" tabindex="1">
			<input type="image" onclick src="/media_stat/images/template/site_access/zip_go.gif" width="28" height="16" name="site_access" border="0" value="Check My Area" alt="GO" hspace="4" tabindex="2"><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"><br>
			<tmpl:get name='error'/>

		<img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
		<img src="/media_stat/images/layout/cccccc.gif" width="270" height="1" alt="" border="0"><br>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0">
		</td></tr>
		</form>
		</table>
		</td>
		<td></td>
	</tr>
	<tr>
		<td align="center">
			<img src="/media_stat/images/template/depot/current_customers.gif" width="113" height="11" border="0"><br>
			<a href="/login/login_main.jsp">Sign in here</a><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><br>
			<img src="/media_stat/images/template/site_access/9managers.jpg" width="585" height="143" alt="Department Managers" border="0"><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><br>
			<tmpl:get name='blurb'/>
		</td>
	</tr>
	<tr>
		<td align="center">
			<img src="/media_stat/images/layout/cccccc.gif" width="585" height="1" vspace="8"><br>
			<%@ include file="/shared/template/includes/copyright.jspf" %>
			<br><br>
		</td>
	</tr>
</table>

</body>
</html>
