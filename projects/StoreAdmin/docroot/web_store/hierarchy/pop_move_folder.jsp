<%@ taglib uri='template' prefix='tmpl' %>
<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String currentDir = pageURI.substring(servletContext.length(), pageURI.lastIndexOf("/")+1);
%>
<tmpl:insert template='/common/template/pop_nonav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - Move Folder</tmpl:put>
<tmpl:put name='header' direct='true'>Move Folder</tmpl:put>
<tmpl:put name='direction' direct='true'>From</tmpl:put>
<tmpl:put name='path' direct='true'>/department/category/subcategory/product</tmpl:put>
<tmpl:put name='button' direct='true'><table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="#" class="button">&nbsp;move >>&nbsp;</a></td></tr></table></tmpl:put>

<tmpl:put name='content' direct='true'>
<div>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<tr><td width="1%">&nbsp;</td><td width="99%">To:</td></tr>
</table>
</div>
<%
	String tree = currentDir + "includes/tree.jsp";
%>
<div style="float:right;width:98%;height:70%;overflow-y:scroll;" class="textbox1">
<jsp:include page='<%=tree%>'/>
</div>
</tmpl:put>
</tmpl:insert>