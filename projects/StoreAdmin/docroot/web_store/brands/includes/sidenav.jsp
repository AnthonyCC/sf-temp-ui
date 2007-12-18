<%
String servletContext = request.getContextPath();
%>

<table width="98%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td class="treeTitle"><img src="images/clear.gif" width="1" height="8"><br><a href="<%= servletContext %>/web_store/brands/index.jsp" class="treeTitle"><b>Brands</b></a><br><img src="images/clear.gif" width="1" height="6"></td>
</tr>
<tr><td bgcolor="#CCCCCC"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr>
<td class="treeTitle">&nbsp;&nbsp;<a href="attributes.jsp"><b>+</b></a>&nbsp;&nbsp;|&nbsp;&nbsp;<b>&ndash;</b>&nbsp;&nbsp;|&nbsp;&nbsp;<b>>></b></td>
</tr>
<tr><td bgcolor="#CCCCCC"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr><td><img src="images/clear.gif" width="1" height="6"></td></tr>
</table>

<div style="position:relative;width:100%;left:0;top:0;height:60%;overflow-y:scroll;">
<jsp:include page='tree.jsp'/>
</div>