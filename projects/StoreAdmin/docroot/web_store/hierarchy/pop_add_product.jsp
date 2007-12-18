<%@ taglib uri='template' prefix='tmpl' %>
<%
String servletContext = request.getContextPath();
%>
<tmpl:insert template='/common/template/pop_nonav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - Add Product</tmpl:put>
<tmpl:put name='header' direct='true'>Add Product</tmpl:put>
<tmpl:put name='direction' direct='true'>to</tmpl:put>
<tmpl:put name='path' direct='true'>/department/category/subcategory/product</tmpl:put>
<tmpl:put name='button' direct='true'><table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="#" class="button">&nbsp;add +&nbsp;</a></td></tr></table></tmpl:put>

<tmpl:put name='content' direct='true'>
<div style="float:right;width:98%;height:35%;overflow-y:scroll;" class="textbox1">
<jsp:include page='includes/tree.jsp'/>
</div>
<div style="float:right;width:98%;" align="center">
<table width="100%" cellpadding="5" cellspacing="5" border="0">
<tr><td width="50%" align="right">
<a href="#"><b><font color="#336600">/\</font></b></a>
<br></td><td width="50%"><a href="#"><b><font color="#CC0000">\/</font></b></a></td><td widht="2%"></td></tr>
</table>
</div>
<div style="float:right;width:98%;height:30%;overflow-y:scroll;" class="textbox1">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<tr><td>Product 1b<br>Product 2a</td></tr>
</table>
</div>
</tmpl:put>

</tmpl:insert>

