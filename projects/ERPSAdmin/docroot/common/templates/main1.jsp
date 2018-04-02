<div id="titlebar">
	<table width="98%" border=0 cellspacing=0 cellpadding=0>
	<tr><td align="left" width=200>
	 <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>"><img src="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/images/fd_logo_sm_gl_nv.gif" border="0" width="182" height="38" align="top"></a>
	<td>
	<td class="title_text" align="center">ERPSy-Daisy</td>
	</tr>
	<%  String uRole = "Admin";
		if(!request.isUserInRole("ErpsyAdminGrp")) {
			uRole = "Viewer";
		}
	%>
	<tr><td colspan="3"  align="right">
		<table> <tr><td align="right">User logged in: </td><td align="left"><%= request.getRemoteUser() %></td></tr>	
				<tr><td align="right">Role: </td><td align="left"><%= uRole %></td></tr>	
		</table>
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
		|
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/batch/batch_update.jsp">Batch Update</a>
				</td>
				<td align="right">
        <a href="<%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>/logout.jsp">Logout</a>
				</td>
			</tr>
		</table>
    </div>
</div>