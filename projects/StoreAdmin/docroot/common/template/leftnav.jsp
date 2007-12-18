<%@ taglib uri='template' prefix='tmpl' %>
<%
try {
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String currentDir = pageURI.substring(servletContext.length(), pageURI.lastIndexOf("/")+1);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><tmpl:get name='title'/></title>
	<link rel="stylesheet" href="<%= servletContext %>/common/css/store_admin.css" type="text/css">
	<script language="Javascript" src="<%= servletContext %>/common/javascript/common.js"></script>
</head>
<body class="basecolor" scroll="yes" style="font-size: 8pt;">

    <%-- header on top and store tree in the leftnav background="<%= servletContext %>/images/lobsters_revenge.jpg" --%>

    <div style="position:relative;width:100%;height:auto;left:0;top:0;">
        <jsp:include page='/includes/header.jsp'/>
    </div>

	<div style="position:relative;width:100%;height:auto;left:0;">
	<%
	String menu = currentDir + "includes/menu.jsp";
	%>
		<jsp:include page='<%=menu%>'/>
    </div>

    <div style="padding-top:2px; padding-bottom:3px; margin-right:8px;position:relative;left:0;width:25%;float:left;height:80%;"><%--overflow-y:scroll"--%>
	<%
	String sidenav = currentDir + "includes/sidenav.jsp";
	%>
        <jsp:include page='<%=sidenav%>'/>
    </div>

    <div style="position:relative;width:100%;height:80%;">
        <tmpl:get name='content'/>
    </div>
	
</body>
</html>
<% } catch (Throwable t) {
	t.printStackTrace();
}
%>