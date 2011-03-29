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
<body bgcolor="#FFFFFF" text="#333333" class="text10" leftmargin="0" topmargin="0">

	<table width="500" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td align="center">
		<br><br>
			<tmpl:get name='splash'/>
		<br><br>
		</td>
	</tr>
	<tr>
		<td class="text12">
		<tmpl:get name='depot_discontinued'/>
		Customers who have accounts with FreshDirect can still log in to view their <a href="/your_account/manage_account.jsp"><b>account details</b></a>.
		<br><br>
		Anyone in the Tri-State area (including existing depot and home delivery customers) can place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens.
		<span class="space8pix"><br><br><br></span>
		<div align="center" class="text13"><a href="/index.jsp"><b>Click here to start shopping.</b></a></div> 
		</td>
	</tr>
	<tr>
		<td align="center">
			<span class="space4pix"><br><br><br></span>
			<img src="/media_stat/images/layout/cccccc.gif" width="250" height="1" vspace="8"><br>
			<%@ include file="/shared/template/includes/copyright.jspf" %>
			<br><br>
		</td>
	</tr>
	</table>

</body>
</html>
