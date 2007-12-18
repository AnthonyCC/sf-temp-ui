<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String URL = pageURI + "?" + request.getQueryString();



%>
<%--div style="border:1px solid #CCCCCC; margin-top:3px;" F6EDE2 --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0" bgcolor="#006600">
<tr>
<td class="treeTitle"><img src="images/clear.gif" width="1" height="4"><br>&nbsp;&nbsp;<a href="<%= servletContext %>/web_store/hierarchy/index.jsp" class="treeTitle"><b>Store Hierarchy</b></a></td>
</tr>
<%--tr><td bgcolor="#E7E7D6"><img src="images/clear.gif" width="1" height="1"></td></tr--%>
<tr>
<td class="treeHeader">&nbsp;&nbsp;<a href="/StoreAdmin/web_store/hierarchy/dept_attributes.jsp?action=create&parentNodeId=<%=ContentFactory.getInstance().getStore().getPK().getId()%>" class="treeTitle" title="ADD"><b>&nbsp;+&nbsp;</b></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:deleteThis('folder','<%=URL%>');" class="treeTitle" title="DELETE"><b>&nbsp;&mdash;&nbsp;</b></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:popup('pop_move_folder.jsp','s');" class="treeTitle" title="MOVE"><b>&nbsp;>>&nbsp;</b></a><br><img src="images/clear.gif" width="1" height="2"></td>
</tr>
<tr><td bgcolor="#E7E7D6"><img src="images/clear.gif" width="1" height="1"></td></tr>
<%--tr><td bgcolor="#CCCCCC"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr><td class="tree">&nbsp;&nbsp;<%--Filter&nbsp;&middot;&nbsp;--%><%--Search<%--&nbsp;&middot;&nbsp;Recent--%><%--/td></tr>
<tr><td bgcolor="#CCCCCC"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr><td><img src="images/clear.gif" width="1" height="6"></td></tr--%>
</table>
<%-- overflow-x:scroll; --%>
<div style="padding:3px;font-size:11pt;position:relative;width:100%;left:0;top:0;height:95%;overflow-y:scroll; background-color:#FFFFFF;">
<jsp:include page='/includes/storeTree.jsp'/>
</div>

<%--/div--%>