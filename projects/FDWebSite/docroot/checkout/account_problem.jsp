<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" /> 
<html>
<head>
    <title>FreshDirect - Talent</title>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<% session.removeAttribute(SessionName.USER); %>

<BODY BGCOLOR="White" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10" leftmargin=0 topmargin=0>
<CENTER>
<table width="525" border="0" cellspacing="0" cellpadding="0">
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0"></td></tr>
<tr>
	<td>
	 <table border="0" cellspacing="0" cellpadding="0">
	 <tr>
	 	<td align="left"><img src="/media_stat/images/template/kitchen/kb_pop_logo.gif" width="117" height="26" alt="" border="0"></td>
		<td valign="bottom" align="right"></td>
	 </tr>
	 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	 <tr>
	 	<td colspan=2 bgcolor="#669933" ><img src="/media_stat/images/layout/669933.gif" width="525" height="2" alt="" border="0"></td>
	 </tr>
	 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	 <tr>
	 	<td align="left"></td>
		<td align="right">
		<img src="/media_stat/images/layout/clear.gif" width="200" height="1" alt="" border="0"><br>
		
		</td>
	 </tr>	 
	 </table>

	</td>
</tr>
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="25" alt="" border="0"></td></tr>
<tr>
	<td align="center">

	We are unable to process your order because there is a problem with your account.<br> 
	To complete Checkout please contact FreshDirect Customer Service at <%=user.getCustomerServiceContact()%>. 


	</td>
</tr>
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0"></td></tr>
<tr>
	<td>
		 <table border="0" cellspacing="0" cellpadding="0">
		 <tr>
		 	<td align="left"></td>
			<td valign="bottom" align="right"></td>
		 </tr>
		 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
		 <tr>
		 	<td colspan=2 bgcolor="#669933"><img src="/media_stat/images/layout/669933.gif" width="525" height="1" alt="" border="0"></td>
		 </tr>
		 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr> 
		 </table>
	</td>
</tr>
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0"></td></tr>
</table>
</CENTER>
</BODY>
</HTML>

