<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.framework.util.NVL'%>

<fd:CheckLoginStatus id="user" guestAllowed="true" /> 

<%
    String successPage = NVL.apply(request.getParameter("successPage"), "");
    // default to index.jsp
    if ("".equals(successPage)) {
	    successPage = "/index.jsp";
    }
    request.setAttribute("survey_source","SiteAccess Page");
%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>FreshDirect</title>
		<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	</head>
	<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
<div align="center">
    <table width="500" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="center" class="text12">
			<img src="/media_stat/images/logos/fd_cos_logo.gif" width="232" height="67" border="0" alt="FreshDirect At The Office">
			<br><img src="/media_stat/images/layout/999966.gif" width="100%" height="1" border="0" vspace="8"><br>
			<a name="survey"></a><%@ include file="/survey/includes/cos.jsp" %><% if ("thankyou#survey".equals(request.getParameter("info"))) { %><br><br><% } %><br>
			<a href="<%= successPage %>"><img src="/media_stat/images/template/site_access/continue_to_store.gif" width="124" height="16" border="0"></a><br><br>
		</td>
	</tr>
	<tr>
        <td><img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10"></td>
    <tr>
</table>

</div>
</body>
</html>
