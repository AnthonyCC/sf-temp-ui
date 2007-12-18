<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>FreshDirect</title>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
<fd:PickupLoginController actionName='' successPage='/index.jsp' result='result'>

<div align="center">
<table width="640" cellpadding="0" cellspacing="0" border="0">
<form name="fi_splash" method="post">
<tr>
     <td align="right" colspan="3"><b>Current customer?</b> <a href="/login/login_main.jsp" title="Sign In">Log In</a></td>
</tr>
<tr>
<td width="62" rowspan="3">&nbsp;</td>
     <td width="517" align="center"><a href="javascript:fi_splash.submit();"><img src="/media_stat/images/template/pickup/lic_splash_hdr.gif" width="517" height="152" alt="FreshDirect Pickup Service" border="0" vspace="14"></a><br><font class="title14"><b>Better food. Better Prices. 
Convenient pickup in Long Island City.</b></font><br><br></td>
<td width="62" rowspan="3">&nbsp;</td>
</tr>
<tr><td><a href="javascript:fi_splash.submit();"><img src="/media_stat/images/template/pickup/lic_splash_img.jpg" width="212" height="161" alt="" border="0" hspace="8" vspace="8" align="left"></a><br>
<font class="text12">You don't have to live in an area where<br>
FreshDirect offers home delivery to get the<br>
best food at the best prices in New York!<br>
Anyone in the Tri-State area (including<br>existing home delivery customers) can now<br>
place an order online for pickup at our<br>facility, located just outside the Midtown<br>Tunnel in Long Island City, Queens. It's also<br>very convenient if you're headed out to the Hamptons or Fire Island.</td></tr>
<tr><td align="center"><br><input type="image" src="/media_stat/images/template/pickup/lic_splash_start.gif" width="234" height="42" name="lic_splash_start" border="0" alt="Start Shopping" vspace="5"></td></tr>
</form>
</table>
<br><br>
<%@ include file="/shared/template/includes/copyright.jspf" %>
<br><br><br>
</div>

</fd:PickupLoginController>
</body>
</html>
