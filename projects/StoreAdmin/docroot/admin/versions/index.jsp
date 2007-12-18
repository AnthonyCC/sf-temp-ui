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
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
	<tr>
	<td width="50%" class="title"><b>Administer Versions</b></td>
	<td width="50%" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr align="center">
		<td class="admin"><a href="#" class="button">&nbsp;&nbsp;PUBLISH NEW&nbsp;&nbsp;</a></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
	</table>
	
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr class="sectionHeader">
	<th width="1%">&nbsp;</th>
	<th width="21%">Version</th>
	<th width="28%">Published</th>
	<th width="5%">By</th>
	<th width="24%">Staged</th>
	<th width="5%">By</th>
	<th width="8%">&nbsp;</th>
	<th width="8%">&nbsp;</th>
	<th width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></th>
	</tr>
	</table>
	
	<div style="width:100%;left:0;top:0;height:60%;overflow-y:scroll;">
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<%-- spacer row --%>
	<tr>
	<td width="1%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="21%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="28%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="5%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="24%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="5%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="8%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="8%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>BB</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>AA</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>BB</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>AA</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>BB</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>AA</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>BB</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>AA</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>n/a</td>
	<td></td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr>
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>stage</td>
	<td>delete</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>BB</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>AA</td>
	<td></td>
	<td>deleted</td>
	</tr>
	
	<tr class="contrast">
	<td></td>
	<td>1234567</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td>02/28/03 @ 10:45:06pm</td>
	<td>GH</td>
	<td></td>
	<td>deleted</td>
	</tr>

	</table>
	</div> <%-- end scroll section --%>

    </tmpl:put>

</tmpl:insert>
