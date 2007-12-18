<%@ taglib uri='template' prefix='tmpl' %>
<%
String servletContext = request.getContextPath();
String params = request.getQueryString();
String URL = request.getRequestURI() + "?" + request.getQueryString();

%>
<tmpl:insert template='/common/template/nonav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
	
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
	<tr>
	<td class="title"><b>Import</b></td>
	</tr>
	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr>
	<th width="1%"></th>
	<th width="99%" colspan="3" class="sectionHeader">Import type</th>
	</tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<%-- spacers --%>
	<tr>
	<td width="1%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td>
	<td width="2%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td>
	<td width="1%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td>
	<td width="96%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td>
	</tr>
	
	<tr>
	<td></td>
	<td><input type="radio" name="radiobutton" value="radiobutton"></td>
	<td></td>
	<td>Media: Images</td>
	</tr>
	
	<tr>
	<td></td>
	<td><input type="radio" name="radiobutton" value="radiobutton"></td>
	<td></td>
	<td>Media: Text</td>
	</tr>
	
	<tr>
	<td></td>
	<td><input type="radio" name="radiobutton" value="radiobutton"></td>
	<td></td>
	<td>Product Attributes</td>
	</tr>
	
	<tr>
	<td></td>
	<td><input type="radio" name="radiobutton" value="radiobutton"></td>
	<td></td>
	<td>Erps Attributes</td>
	</tr>
	
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	<tr><td></td><td colspan="3">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr align="center">
		<td class="admin"><a href="#" class="button">&nbsp;&nbsp;IMPORT&nbsp;&nbsp;</a></td>
		</tr>
		</table>
	</td></tr>
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr class="sectionHeader">
	<th width="1%">&nbsp;</th>
	<th width="41%">Date</th>
	<th width="8%">By</th>
	<th width="20%">Type</th>
	<th width="30%">Status</th>
	<th width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></th>
	</tr>
	</table>
	
	<div style="width:100%;left:0;top:0;height:30%;overflow-y:scroll;">
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<%-- spacer row --%>
	<tr>
	<td width="1%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="41%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="8%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="20%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="30%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Images</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Text</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Product Attributes</td>
	<td>failed</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Erps Attributes</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Images</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Text</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Product Attributes</td>
	<td>failed</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Erps Attributes</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Images</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Text</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Product Attributes</td>
	<td>failed</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Erps Attributes</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Images</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Media: Text</td>
	<td>ok</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Product Attributes</td>
	<td>failed</td>
	</tr>
	
	<tr>
	<td></td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>SD</td>
	<td>Erps Attributes</td>
	<td>ok</td>
	</tr>

	</table>
	</div> <%-- end scroll section --%>

    </tmpl:put>

</tmpl:insert>
