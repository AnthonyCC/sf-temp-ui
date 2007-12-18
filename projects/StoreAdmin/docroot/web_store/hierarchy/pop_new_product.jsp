<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%@ page import ="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="java.text.*"%>
<%
String servletContext = request.getContextPath();
String action = request.getParameter("action")==null ? "create" : request.getParameter("action");
String nodeId = request.getParameter("nodeId");
%>
<sa:ProductController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='pm'>
<%
// *************************  debug stuff ************************
Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}
%>
<tmpl:insert template='/common/template/pop_nonav.jsp'>

<tmpl:put name='title' direct='true'>FD Admin - New Product</tmpl:put>
<tmpl:put name='header' direct='true'>New Product</tmpl:put>
<tmpl:put name='direction' direct='true'>to</tmpl:put>
<tmpl:put name='path' direct='true'>/department/category/subcategory/product</tmpl:put>
<tmpl:put name='button' direct='true'><table cellpadding="1" cellspacing="0" border="0" align="center" bgcolor="#009999"><tr><td class="button"><a href="javascript:createProduct.submit()" class="button">&nbsp;create new *&nbsp;</a></td></tr></table></tmpl:put>

<tmpl:put name='content' direct='true'>
<div style="float:right;width:98%;height:75%;overflow-y:scroll;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<form name="createProduct" method="post" action="pop_new_product.jsp">
<tr>
<td width="30%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td>
<td width="5%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td>
<td width="25%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td>
<td width="5%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td>
<td width="30%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td>
<td width="5%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></td>
</tr>
<tr>
<td align="right">Product ID&nbsp;&nbsp;</td>
<td colspan="5"><input  name="productId" type="text" style="width:120px;" size="12" class="textbox1" value="<%=pm.getContentName()%>"></td>
</tr>
<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
<tr>
<td align="right">Full Name&nbsp;&nbsp;</td>
<td colspan="5"><input  name="fullName" type="text" style="width:120px;" size="12" class="textbox1" value="<%=pm.getFullName()%>"></td>
</tr>
<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
<tr>
<td align="right">Nav Name&nbsp;&nbsp;</td>
<td colspan="5"><input  name="navName" type="text" style="width:120px;" size="12" class="textbox1" value="<%=pm.getNavName()%>"></td>
</tr>
<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
<tr>
<td align="right">Glance Name&nbsp;&nbsp;</td>
<td colspan="5"><input  name="glanceName" type="text" style="width:120px;" size="12" class="textbox1" value="<%=pm.getGlanceName()%>"></td>
</tr>
<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
<tr>
<td align="right">Keywords&nbsp;&nbsp;</td>
<td colspan="5"><input  name="keywords" type="text" style="width:120px;" size="12" class="textbox1" value="<%=pm.getKeywords()%>"></td>
</tr>
<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
<tr>
<td align="right">Qty Min&nbsp;&nbsp;</td>
<td><input  name="quantityMin" type="text" style="width:40px;" size="4" class="textbox1" value="<%=pm.getQuantityMinimum()%>"></td>
<td align="right">Qty Max&nbsp;&nbsp;</td>
<td><input name="quantityMax" type="text" style="width:40px;" size="4" class="textbox1" value="<%=pm.getQuantityMaximum()%>"></td>
<td align="right">Qty Increment&nbsp;&nbsp;</td>
<td><input name="quantityInc" type="text" style="width:40px;" size="4" class="textbox1" value="<%=pm.getQuantityIncrement()%>"></td>
</tr>
<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
<tr>
<td align="right">SKU Web ID&nbsp;&nbsp;</td>
<td colspan="5"><input type="text" style="width:120px;" size="12" class="textbox1"></td>
</tr>
<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
<tr>
<td align="right">SKU Web ID&nbsp;&nbsp;</td>
<td colspan="5"><input type="text" style="width:120px;" size="12" class="textbox1"></td>
</tr>
</table>
</div>
</tmpl:put>

</tmpl:insert>
</sa:ProductController>

