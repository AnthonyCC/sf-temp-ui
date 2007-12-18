<%@ taglib uri='template' prefix='tmpl' %>
<%
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
<body class="basecolor" scroll="no" style="font-size: 8pt;">

    <%-- header on top and content below --%>
    
    <div style="position:relative;width:100%;height:auto;left:0;top:0">
        <jsp:include page='/includes/header.jsp'/>
    </div>
	
	<div style="position:relative;width:100%;height:auto;left:0;top:0">
	<%
	String menu = currentDir + "includes/menu.jsp";
	%>
		<jsp:include page='<%=menu%>'/>
    </div>
    
    <div style="position:relative;width:100%;height:100%;">
        <tmpl:get name='content'/>
    </div>
    
</body>
</html>