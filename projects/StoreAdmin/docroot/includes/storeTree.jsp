<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%

    String deptId = request.getParameter("deptId");
    String catId = request.getParameter("catId");
    String prodId = request.getParameter("prodId");
    String currentPage = request.getRequestURI();

    String catPage;
    String deptPage;
    String prodPage = "/StoreAdmin/web_store/hierarchy/product_info.jsp";


    if (currentPage.toLowerCase().indexOf("attributes.jsp")!=-1 || currentPage.toLowerCase().indexOf("_info.jsp")!=-1 )  {
       catPage = "/StoreAdmin/web_store/hierarchy/attributes.jsp";
       deptPage = "/StoreAdmin/web_store/hierarchy/dept_attributes.jsp";
    }  else {
       catPage = "/StoreAdmin/web_store/hierarchy/cat_contents.jsp";
       deptPage = "/StoreAdmin/web_store/hierarchy/dept_contents.jsp";
    }


%>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tree">
<sa:Tree id="node">
    <%
      String contentType = node.getContentType();
      if (ContentNodeModel.TYPE_DEPARTMENT.equals(contentType)) {
          String expandPath= (request.getParameter("expand")==null || request.getParameter("expand").indexOf(path)==-1) ? path : path.substring(0,path.lastIndexOf(node.getContentName()));
%>
   	<tr valign="top">
		<td style="padding-left:<%= 10 * (depth.intValue()-1) %>px;" class="tree"><%--img src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%= 10 * (depth.intValue()-1) %>"--%>
           <a href="<%= currentPage %>?expand=<%= expandPath %>&nodeId=<%=node.getPK().getId()%>&parentNodeId=<%=node.getParentNode().getPK().getId()%>&deptId=<%= node.getContentName() %>" class="tree">&curren;</a>&nbsp;
           <a href="<%=deptPage%>?nodeId=<%= node.getPK().getId()%>&parentNodeId=<%=node.getParentNode().getPK().getId()%>&deptId=<%= node.getContentName() %>" class="tree"><%= node.getFullName() %></a>
        </td>
	</tr>
    <%  } else if (ContentNodeModel.TYPE_CATEGORY.equals(contentType)) {
          String expandPath= (request.getParameter("expand")==null || request.getParameter("expand").indexOf(path)==-1) ? path : path.substring(0,path.lastIndexOf(node.getContentName()));
     %>
		<tr valign="top">
	        <td style="padding-left:<%= 10 * (depth.intValue()-1) %>px;">
			<a name="<%= node.getContentName() %>" href="<%= currentPage %>?expand=<%= expandPath %><%= deptId != null ? ("&deptId="+ deptId) : "" %><%= catId != null ? ("&catId="+ catId) : "" %><%= prodId != null ? ("&prodId="+ prodId) : "" %>" class="tree">&ordm;</a>&nbsp;
			<a href="<%=catPage%>?nodeId=<%=node.getPK().getId()%>&parentNodeId=<%=node.getParentNode().getPK().getId()%>&catId=<%= node.getContentName() %>" class="tree"><%= node.getFullName() %></a>
            </td>
		</tr>
    <%  } else if (ContentNodeModel.TYPE_PRODUCT.equals(contentType)) { %>
	<tr valign="top">
        <td style="padding-left:<%= 10 * (depth.intValue()-1) %>px;">
           <%--img src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%= 10 * (depth.intValue()-1) %>"--%>
           &middot;&nbsp;
           <a name="<%= node.getContentName() %>" href="<%=prodPage%>?nodeId=<%=node.getPK().getId()%>&parentNodeId=<%= node.getParentNode().getPK().getId() %>&catId=<%= node.getParentNode().getContentName() %>&prodId=<%= node.getContentName() %>" class="tree"><%= node.getFullName() %></a>
        </td>
	</tr>
    <%  } %>
</sa:Tree>
</table>