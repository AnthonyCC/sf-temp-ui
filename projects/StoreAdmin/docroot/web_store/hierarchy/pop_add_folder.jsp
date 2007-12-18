<%@ taglib uri='template' prefix='tmpl' %>
<%
String servletContext = request.getContextPath();
%>
<tmpl:insert template='/common/template/pop_nonav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - Add Folder</tmpl:put>
<tmpl:put name='header' direct='true'>Add Folder</tmpl:put>
<tmpl:put name='direction' direct='true'>to</tmpl:put>
<tmpl:put name='path' direct='true'>/department/category/subcategory/product</tmpl:put>
<tmpl:put name='button' direct='true'><table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="#" class="button">&nbsp;add +&nbsp;</a></td></tr></table></tmpl:put>

<tmpl:put name='content' direct='true'>
<div style="float:right;width:98%;height:50%;overflow-y:scroll;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="20"></td></tr>
<tr><td width="25%" align="right">Folder Name&nbsp;&nbsp;</td><td width="75%"><input type="text" style="width:200px;" size="20" class="textbox2"></td>
</tr>
<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td></tr>
<tr><td align="right">Folder ID&nbsp;&nbsp;</td><td><input type="text" style="width:200px;" size="20" class="textbox2"></td>
</tr>
</table>
</div>
</tmpl:put>

</tmpl:insert>

