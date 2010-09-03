<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title><tmpl:get name='title'/></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	</head>
	<body bgcolor="#ffffff" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" class="text10">
		<center>
		<%@ include file="/common/template/includes/globalnav.jspf" %> 

		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
		<%@ include file="/common/template/includes/footer.jspf" %>
		</center>
	</body>
</html> 
