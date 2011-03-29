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
<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
<%--fd:PickupLoginController actionName='' successPage='/index.jsp' result='result'--%>

<div align="center">
<table width="640" cellpadding="0" cellspacing="0" border="0">
<%--form name="fi_splash" method="post"--%>
<tr>
     <td align="right"><b>Current customer?</b> <a href="/login/login_main.jsp" title="Sign In">Log In</a></td>
</tr>
<tr><td align="center" class="text12"><br><a href="/index.jsp"><img src="/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" alt="FreshDirect" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br><img src="/media_stat/images/template/pickup/fi_cancelled_hdr.gif" width="502" height="85" alt="We're sorry, Fire Island Delivery has been cancelled" border="0" vspace="10"><br><img src="/media_stat/images/template/pickup/fi_cancelled_img.jpg" width="408" height="155" alt="" border="0"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br>
We were looking forward to bringing FreshDirect food (and prices) to our<br>customers in Fire Island this summer. Unfortunately, we've had to cancel our<br>plans for dockside deliveries this summer.<br><br><a href="/index.jsp"><b>Click here find out if home delivery is available in your area. 
</b></a><br><br>If you are heading out from New York City, you may be interested in our new<br><a href="/pickup"><b>Long Island City pickup</b></a> option - just minutes from the midtown tunnel.
</td>
</tr>

<%--/form--%>
</table>
<br><br>
<%@ include file="/shared/template/includes/copyright.jspf" %>
<br><br><br>
</div>

<%--/fd:PickupLoginController--%>
</body>
</html>
