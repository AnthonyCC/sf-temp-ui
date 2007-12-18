<%@ taglib uri='template' prefix='tmpl' %>
<% String servletContext = request.getContextPath(); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><tmpl:get name='title'/></title>
	<link rel="stylesheet" href="<%= servletContext %>/common/css/store_admin.css" type="text/css">
	<script language="Javascript" src="<%= servletContext %>/common/javascript/common.js"></script>
</head>
<body class="basecolor" scroll="no" style="font-size: 8pt;">

    <%-- content left and tree in rightnav --%>
    
	<%
	String type = request.getParameter("type");

		/*if (dir.equalsIgnoreCase("domain")) {
			tree += "domains";
		} else if (dir.equalsIgnoreCase("media")) {
			tree += "media";
		} else {
			tree += "hierarchy";
		}
	
		tree += "/includes/tree.jsp";*/
	
	%>
	
    <div style="float:left;width:70%;height:100%;overflow-y:scroll;overflow-x:scroll;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
	<tr class="popHeader">
	<td width="1%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="25"></td>
	<td width="92%"><tmpl:get name='header'/></td>
	<td width="6%" align="center"><a href="javascript:window.close();"><b>X</b></a></td>
	<td width="1%">&nbsp;</td>
	</tr>
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
	<tr><td colspan="4" class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
	</table>
	<tmpl:get name='content'/>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
	<tr><td colspan="4" class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr class="popHeader">
	<td width="1%">&nbsp;</td>
	<td width="92%">&nbsp;</td>
	<td width="6%" align="center"><a href="javascript:window.close();"><b>X</b></a></td>
	<td width="1%">&nbsp;</td>
	</tr>
	</table>
    </div>
    
	<div style="float:right;width:30%;height:100%;overflow-y:scroll;overflow-x:scroll;">
	<table cellpadding="0" cellspacing="0" border="0" class="tree">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="8" height="1"></td><td>
		<tmpl:get name='navHeader'/>
		<% if (!type.equalsIgnoreCase("img") && !type.equalsIgnoreCase("txt")) { %>
		<br><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>
		<tmpl:get name='button'/><table cellpadding="0" cellspacing="0" border="0"><tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr></table>
		<% } %>
		<jsp:include page='/includes/storeTree.jsp'>
                    <jsp:param name="deptLink" value="#"/>
                    <jsp:param name="catLink"  value="#"/>
                    <jsp:param name="prodLink" value="#"/>
                </jsp:include>
		<% if (!type.equalsIgnoreCase("img") && !type.equalsIgnoreCase("txt")) { %>
		<img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>
		<tmpl:get name='button'/>
		<% } %>
	</td></tr>
	</table>
	
    </div>
	
</body>
</html>
