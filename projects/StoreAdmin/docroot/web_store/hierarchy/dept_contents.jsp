<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%@ page import ="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="java.text.*"%>
<%!
  SimpleDateFormat modByFormat = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");
%>

<%
String servletContext = request.getContextPath();
String params = request.getQueryString();
String URL = request.getRequestURI() + "?" + request.getQueryString();
String currentPage = request.getRequestURI();
String currentDir = currentPage.substring(0, currentPage.lastIndexOf("/")+1);
String nodeId=request.getParameter("nodeId");


String action = request.getParameter("action")==null ? "" : request.getParameter("action");
String saveHref = "javascript:departmentContents.submit();";

try {
%>
<sa:DepartmentController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='dm'>

<%
    if (dm.getPK()!=null) {
       action = "update";
    }else {
        action = "create";
    }

    String cancelHref;
    cancelHref = "dept_contents.jsp?nodeId="+dm.getPK().getId();
                       // +"&parentNodeId="+dm.getParentNode().getPK().getId() ;

    String productLink = currentDir + "product_info.jsp";
    List categories = dm.getCategories();
%>

<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
    <form name="departmentContents" method="post" action="dept_contents.jsp">
      <input type="hidden" name="action" value="<%=action%>">
<%  if (request.getParameter("parentNodeId")!=null && dm.getPK()==null) {    %>
      <input type="hidden" name="parentNodeId" value="<%=request.getParameter("parentNodeId")%>">
<%  }
Collection myErrors = result.getErrors();
if (myErrors.size() > 0 ){ %>
  <table>
<%  for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
      ActionError ae = (ActionError)itE.next();
      System.out.println(ae.getType()+"-->"+ae.getDescription());  %>
      <tr><td class="error"><%=ae.getDescription()%></td></tr>
<% }   %>
  </table>
<%
}
%>

	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2" class="breadcrumb"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>Current Path:
   <%@ include file="includes/breadcrumb.jspf"%>
     <%=dm.getFullName()%>
    </td>
	</tr>
	<tr>
	<td valign="bottom">
		<table cellpadding="3" cellspacing="0" border="0">
		<tr><td colspan="5"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
		<tr>
		 <td class="tab_on"><font class="tab">&nbsp;&nbsp;CONTENTS&nbsp;&nbsp;</font></td>
		 <td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		 <td class="tab_off"><a href="<%= servletContext %>/web_store/hierarchy/dept_attributes.jsp?<%= params %>" class="tab">&nbsp;&nbsp;ATTRIBUTES&nbsp;&nbsp;</a></td>
		 <td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
<%    if (dm.getPK()!=null ){        %>
         <td class="tab_off"><a href="/StoreAdmin/web_store/hierarchy/attributes.jsp?action=create&parentNodeId=<%=dm.getPK().getId()%>" class="tab">&nbsp;ADD CATEGORY&nbsp;</a></td>
<%    } else {  %>
		 <td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
<%    }       %>
        </tr>
		</table>
	</td>
	<td align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr align="center">
<%      if(dm.getPK()!=null) {	%>
		<td class="preview"><a href="javascript:preview('/department.jsp?deptId=<%=dm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
		<td class="preview">&nbsp;PREVIEW&nbsp;</td>
<%      }  %>
        <td class="cancel"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="save"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td colspan="2" class="header">
		<table width="100%" cellpadding="2">
		<tr>
		<td width="30%" align="left" class="tabDetails">Categories:&nbsp;<%= categories.size() %></td>
		<td align="right" class="tabDetails">Created <%=dm.getCreated()!=null ? modByFormat.format(dm.getCreated()) : ""%>  &middot; Last Modified <%=dm.getLastModified()!=null ? modByFormat.format(dm.getLastModified()) : ""%>  By: <%=dm.getLastModifiedBy()%> </td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td colspan="2">
		<table cellpadding="3">
		<tr>
		<td class="menu"><a href="product_info.jsp?catId="><font size="4">*</font> <b>new</b></a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="menu"><a href="javascript:popup('pop_add_product.jsp','s');"><font size="4">+</font> <b>add</b></a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="menu"><a href="javascript:deleteThis('product','<%=URL%>');"><font size="4">&ndash;</font> <b>delete</b></a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="menu"><a href="javascript:popup('pop_move_product.jsp','s');"><font size="4">>></font> <b>move</b></a></td>
		</tr>
		</table>
	</td>
	</tr>
     <tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td>
	</tr>
	</table>

	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<% if (false==true) {  //!!!!  revist, for removal %>
     <tr class="colHeader">
	<td width="2%">&nbsp;</td>
	<td width="24%">Category ID</td>
	<td width="36%">Name</td>
	<td width="12%" align="center">Priority</td>
	<td width="20%">Modified</td>
	<td width="6%" align="center">By</td>
	<td width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></td>
	</tr>
     <%} else {%>
     <tr class="colHeader">
	<td width="2%"><img src="<%= servletContext %>/images/clear.gif" width="18" height="1"></td>
	<td width="24%">Product ID</td>
	<td width="40%">Name</td>
	<td width="5%" align="center">Type</td>
	<td width="5%" align="center">Priority</td>
	<td width="7%" align="center">Complete?</td>
	<td width="13%">Modified</td>
	<td width="4%" align="center">By</td>
	<td width="10"><img src="<%= servletContext %>/images/clear.gif" width="10" height="1"></td>
	</tr>
     <% } %>
	</table>

	<div style="width:100%; position:relative; left:0; top:0; height:70%; overflow-y:scroll;">
	<table height="width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
	<tr>
	<td width="2%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="24%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="36%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="12%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="20%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	<td width="6%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<% if (categories != null) { %>
	<% for (Iterator cats=categories.iterator(); cats.hasNext();) {
			CategoryModel cMod = ((CategoryModel)cats.next());
	%>
	<tr>
	<td>&nbsp;</td>
	<td><a href="/StoreAdmin/web_store/hierarchy/cat_contents.jsp?nodeId=<%=cMod.getPK().getId()%>&parentNodeId=<%=cMod.getParentNode().getPK().getId()%>&catId=<%= cMod.getContentName() %>"><%= cMod.getContentName() %></a></td>
	<td><a href="/StoreAdmin/web_store/hierarchy/cat_contents.jsp?nodeId=<%=cMod.getPK().getId()%>&parentNodeId=<%=cMod.getParentNode().getPK().getId()%>&catId=<%= cMod.getContentName() %>"><%= cMod.getFullName() %></a></td>
	<td align="center"><input name="cat_<%=cMod.getPK().getId()%>" type="text" value="<%=cMod.getPriority()%>" size="4" class="textbox1"></td>
	<td><%=cMod.getLastModified()!=null ? modByFormat.format(cMod.getLastModified()) : ""%> </td>
	<td align="center"><%=cMod.getLastModifiedBy()%></td>
	</tr>
	<% }  } %>

	</table>
	</div> <%-- end scroll section --%>

	<div>
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr>
	<td colspan="2">
		<table cellpadding="3">
		<tr class="menu">
		<td class="menu"><a href="product_info.jsp?catId="><font size="4">*</font> <b>new</b></a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="menu"><a href="javascript:popup('pop_add_product.jsp','s');"><font size="4">+</font> <b>add</b></a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="menu"><a href="javascript:deleteThis('product','<%=URL%>');"><font size="4">&ndash;</font> <b>delete</b></a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="menu"><a href="javascript:popup('pop_move_product.jsp','s');"><font size="4">>></font> <b>move</b></a></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr><td colspan="2" class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td align="right" width="78%"><table><tr><td class="legend"> <i>p = primary home<br>a = alias</i></td></tr></table></td>
	<td align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center">
<%      if(dm.getPK()!=null) {
%>
		<td class="preview"><a href="javascript:preview('/department.jsp?deptId=<%=dm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
		<td class="button">&nbsp;PREVIEW&nbsp;</td>
<%      }  %>
		<td class="cancel" class="button"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td class="save" class="button"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table><br>
	</td>
	</tr>
	</table>
	</div>
    </form>
    </tmpl:put>

</tmpl:insert>
</sa:DepartmentController>
<% } catch (Throwable t) {
	t.printStackTrace();
}
%>