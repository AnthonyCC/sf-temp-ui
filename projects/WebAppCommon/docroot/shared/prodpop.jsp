<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='java.util.*'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:Department id='department' departmentId='<%= request.getParameter("deptId") %>'/>
<%
    ContentNodeModel currentFolder = department;
%>

<tmpl:insert template='/shared/template/pop_sm.jsp'>
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>

<tmpl:put name='title' direct='true'>FreshDirect - <%= productNode.getFullName() %></tmpl:put>

<tmpl:put name='content' direct='true'>
<table  width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td>
	<%if( (Image)productNode.getZoomImage()!=null ){%>
	<a href="javascript:window.close();"><IMG SRC="<%= ((Image)productNode.getZoomImage()).getPath() %>" width="<%= ((Image)productNode.getZoomImage()).getWidth() %>" HEIGHT="<%= ((Image)productNode.getZoomImage()).getHeight() %>" ALT="<%= productNode.getFullName() %>  (click to close window)" border="0"></a>
	<%}else{%>
	<div align="center">
	<a href="javascript:window.close();"><IMG SRC="/media/images/temp/soon_260x260.gif" ALT="Photo Coming Soon  (click to close window)" border="0"></a>
	</div>
	<%}%>
	</td>
</tr>
<tr><td align="right" class="text11"><b><%= productNode.getFullName() %></b><br><FONT CLASS="space2pix"><br></FONT></td>
</tr>
<tr><td align="right" class="text11"><font color="#999999"><%@ include file="/shared/template/includes/copyright.jspf" %></font></td></tr>
</table>
</tmpl:put>
</fd:ProductGroup>
</tmpl:insert>
