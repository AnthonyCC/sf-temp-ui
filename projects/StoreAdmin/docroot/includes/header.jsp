<% String servletContext = request.getContextPath(); %>
<table width="100%" cellpadding="4" cellspacing="0" border="0">
<tr>
<td width="33%" height="36" <%--background="<%= servletContext %>/images/gummy_bears.gif" style="background-repeat:no-repeat;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>>&nbsp;<a href="<%= servletContext %>/index.jsp"><b><font size="4" color="#000000">FD Content Manager</font></b></a></td>
<form name="navigation" method="post" action="">
<td width="33%" align="center" valign="top" <%--background="<%= servletContext %>/images/lobsters_revenge.jpg" style="background-repeat:no-repeat;"--%>>
  <select name="select" onChange="javascript:jumpTo(this.options[this.selectedIndex].value)" class="pulldownNav" style="width:140px;">
    <option>JUMP TO:</option>
	<option></option>
    <option value="<%= servletContext %>/index.jsp">HOME</option>
    <option>------------------</option>
    <option value="<%= servletContext %>/web_store/hierarchy/index.jsp">WEB STORE</option>
    <option value="<%= servletContext %>/web_store/hierarchy/index.jsp">- Hierarchy</option>
    <option value="<%= servletContext %>/web_store/brands/index.jsp">- Brands</option>
    <option value="<%= servletContext %>/web_store/media/index.jsp">- Media</option>
    <option value="<%= servletContext %>/web_store/domains/index.jsp">- Domains</option>
    <option>------------------</option>
    <option style="color:#CCCCCC;">ERPS DATA</option>
    <option style="color:#CCCCCC;">- SKUs</option>
    <option style="color:#CCCCCC;">- Materials</option>
    <option style="color:#CCCCCC;">- Classes</option>
    <option style="color:#CCCCCC;">- Batches</option>
    <option>------------------</option>
    <option style="color:#CCCCCC;">REPORTS</option>
    <option style="color:#CCCCCC;">- Standard Reports</option>
    <option style="color:#CCCCCC;">- Custom Reports</option>
    <option>------------------</option>
    <option value="<%= servletContext %>/admin/versions/index.jsp">ADMIN</option>
    <option value="<%= servletContext %>/admin/versions/index.jsp">- Versions</option>
    <option value="<%= servletContext %>/admin/import/index.jsp">- Import</option>
	<option></option>
  </select>
</td>
</form>
<td width="33%" align="right" class="Menu">Reports | <a href="<%= servletContext %>/admin/versions/index.jsp">Admin</a> | Logout | Help&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>