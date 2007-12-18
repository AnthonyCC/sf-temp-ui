<%
String servletContext = request.getContextPath();
String URL = request.getRequestURI() + "?" + request.getQueryString();
%>

<table width="98%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td class="treeTitle"><img src="images/clear.gif" width="1" height="8"><br><a href="<%= servletContext %>/web_store/media/index.jsp" class="treeTitle"><b>Media</b></a><br><img src="images/clear.gif" width="1" height="6"></td>
</tr>
<tr><td bgcolor="#CCCCCC"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr>
<td class="treeTitle">&nbsp;&nbsp;<a href="javascript:popup('pop_add_folder.jsp','s');"><b>+</b></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:deleteThis('media folder','<%=URL%>');"><b>&ndash;</b></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:popup('pop_move_folder.jsp','s');"><b>>></b></a></td>
</tr>
<tr><td bgcolor="#CCCCCC"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr><td><img src="images/clear.gif" width="1" height="6"></td></tr>
</table>

<div style="position:relative;width:100%;left:0;top:0;height:60%;overflow-x:scroll;overflow-y:scroll;">
<jsp:include page='/includes/mediaTree.jsp'/>
</div>