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
String saveHref = "javascript:categoryContents.submit();";

try {
%>
<sa:CategoryController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='cm'>

<%
    if (cm.getPK()!=null) {
       action = "update";
    }else {
        action = "create";
    }

    String cancelHref;
    if (cm.getPK() !=null) {
       ContentNodeModel prevNode = ContentFactory.getInstance().getContentNode(request.getParameter("parentId"));
       cancelHref = "cat_contents.jsp?nodeId="+cm.getPK().getId()
                        +"&parentNodeId="+cm.getParentNode().getPK().getId()
                        +"&catId="+cm.getContentName();
    }else {
       cancelHref = "attributes.jsp?action=create";
    }

    String productLink = currentDir + "product_info.jsp";
    List categories = cm.getSubcategories();
    Collection products = cm.getProducts();
%>

<tmpl:insert template='/common/template/leftnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
    <form name="categoryContents" method="post" action="cat_contents.jsp">
      <input type="hidden" name="action" value="<%=action%>">
<%  if (request.getParameter("parentNodeId")!=null && cm.getPK()==null) {    %>
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
     <%=cm.getFullName()%>
    </td>
    </tr>
    <tr>
    <td valign="bottom">
        <table cellpadding="3" cellspacing="0" border="0">
        <tr><td colspan="5"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
        <tr>
         <td class="tab_on"><font class="tab">&nbsp;&nbsp;CONTENTS&nbsp;&nbsp;</font></td>
         <td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
         <td class="tab_off"><a href="<%= servletContext %>/web_store/hierarchy/attributes.jsp?<%= params %>" class="tab">&nbsp;&nbsp;ATTRIBUTES&nbsp;&nbsp;</a></td>
         <td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
<%    if (cm.getPK()!=null ){        %>
         <td class="tab_off"><a href="/StoreAdmin/web_store/hierarchy/attributes.jsp?action=create&parentNodeId=<%=cm.getPK().getId()%>" class="tab">&nbsp;ADD CATEGORY&nbsp;</a></td>
<%    } else {  %>
         <td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
<%    }       %>
        </tr>
        </table>
    </td>
    <td align="right" valign="top">
        <table cellpadding="1" cellspacing="3" border="0">
        <tr align="center">
<%      if(cm.getPK()!=null) {  %>
        <td class="preview"><a href="javascript:preview('/category.jsp?catId=<%=cm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
        <td class="button">&nbsp;PREVIEW&nbsp;</td>
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
        <td width="30%" align="left" class="tabDetails">Categories:&nbsp;<%= categories.size() %>&nbsp;&nbsp;Products:&nbsp;<%= products.size() %></td>
        <td align="right" class="tabDetails">Created <%=cm.getCreated()!=null ? modByFormat.format(cm.getCreated()) : ""%>  &middot; Last Modified <%=cm.getLastModified()!=null ? modByFormat.format(cm.getLastModified()) : ""%>  By: <%=cm.getLastModifiedBy()%> </td>
        </tr>
        </table>
    </td>
    </tr>
    <tr>
    <td colspan="2">
        <table cellpadding="3">
        <tr>
        <td class="menu"><a href="product_info.jsp?action=create&parentNodeId=<%=cm.getPK().getId()%>&catId=<%=cm.getContentName()%>"><font size="4">*</font> <b>new</b></a></td>
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
    <% if (false==true) {  //*ToDo:!!!!  revist, for removal %>
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
    <td width="24%">(Prod/Cat) ID</td>
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

    <div style="width:100%;left:0;top:0;height:50%;overflow-y:scroll;">
    <table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="colDetails">
    <%-- contents --%>
     <%-- spacer row --%>
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
    <td><a href="<%= currentPage %>?catId=<%= cMod.getContentName() %>"><%= cMod.getContentName() %></a></td>
    <td><a href="<%= currentPage %>?nodeId=<%=cMod.getPK().getId()%>&parentNodeId=<%=cMod.getParentNode().getPK().getId()%>&catId=<%= cMod.getContentName() %>"><%= cMod.getFullName() %></a>
    </td>
    <td align="center"><input name="subcat_<%=cMod.getPK().getId()%>" type="text" value="<%=cMod.getPriority()%>" size="4" class="textbox1"></td>
    <td><%=cMod.getLastModified()!=null ? modByFormat.format(cMod.getLastModified()) : ""%> </td>
    <td align="center"><%=cMod.getLastModifiedBy()%></td>
    </tr>
    <% }  } %>


     <%-- spacer row --%>
    <tr>
    <td width="2%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    <td width="24%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    <td width="40%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    <td width="5%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    <td width="5%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    <td width="7%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    <td width="13%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    <td width="4%"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    </tr>
    <% if (products != null) { %>
    <% for (Iterator prod=products.iterator(); prod.hasNext();) {
            ProductModel pm = ((ProductModel)prod.next());
    %>
    <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox"></td>
    <td><a href="<%= productLink %>?nodeId=<%=pm.getPK().getId()%>&parentNodeId=<%=pm.getParentNode().getPK().getId()%>&catId=&<%=pm.getParentNode()%>&prodId=<%= pm.getContentName() %>"><%= pm.getContentName() %></a></td>
    <td><a href="<%= productLink %>?nodeId=<%=pm.getPK().getId()%>&parentNodeId=<%=pm.getParentNode().getPK().getId()%>&catId=&<%=pm.getParentNode()%>&prodId=<%= pm.getContentName() %>"><%= pm.getFullName() %></a></td>
    <td align="center">p</td>
    <td align="center"><input name="prod_<%=pm.getPK().getId()%>" type="text" size="4" class="textbox1" value="<%=pm.getPriority()%>"></td>
    <td align="center">y</td>
    <td><%=pm.getLastModified()!=null ? modByFormat.format(pm.getLastModified()) : ""%> </td>
    <td align="center"><%=pm.getLastModifiedBy()%></td>
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
        <td class="menu"><a href="product_info.jsp?action=create&parentNodeId=<%=cm.getPK().getId()%>&catId=<%=cm.getContentName()%>"><font size="4">*</font> <b>new</b></a></td>
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
<%      if(cm.getPK()!=null) {  %>
        <td class="preview"><a href="javascript:preview('/category.jsp?catId=<%=cm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
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
</sa:CategoryController>
<% } catch (Throwable t) {
    t.printStackTrace();
}
%>