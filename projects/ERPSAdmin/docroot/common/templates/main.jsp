<%@ taglib uri='template' prefix='tmpl' %>
<%@ include file="/common/css/popcalendar.css" %>
<html>
<head>
    <title><tmpl:get name='title'/></title>

<link rel="stylesheet" href="/ERPSAdmin/common/css/erpsadmin.css" type="text/css">
    
    <script language="Javascript" type="text/javascript">
		<%@ include file='/common/includes/popcalendar.js'%>
    </script>

</head>
<body>

<div id="titlebar">
	<table width="98%" border=0 cellspacing=0 cellpadding=0>
	<tr><td align="left" width=200>
	 <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>"><img src="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/images/fd_logo_sm_gl_nv.gif" border="0" width="182" height="38" align="top"></a>
	<td>
	<td class="title_text" align="center">ERPSy-Daisy</td>
	</tr>
	</table>
</div>

<div id="menubar">
		<table width="98%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/batch/">Batch Manager</a>
        |
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/attribute/material/material_search.jsp">View/Edit Material Attributes</a>
        |
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/attribute/class/class_search.jsp">View/Edit Class Attributes</a>
        |
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/product/product_search.jsp">Products</a>
        |
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/product/upload.jsp">Nutrition Upload</a>
				</td>
				<td align="right">
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/product/reports.jsp">Reports</a>
        |
        Admin
        |
        Logout
				</td>
			</tr>
		</table>
    </div>
</div>

<div id="main">
   <tmpl:get name='leftnav'/>
	
	<div id="content">
		<tmpl:get name='content'/>
	</div>
</div>

</body>
</html>
