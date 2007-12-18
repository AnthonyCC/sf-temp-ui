<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%
        String servletContext = request.getContextPath();
        String currentPage = request.getRequestURI();
        String currentDir = currentPage.substring(0, currentPage.lastIndexOf("/")+1);
        String expandLink = "";
        boolean incDisplay = true;
                if (currentPage.indexOf("view_edit") > -1){
                        incDisplay = false;
                        expandLink = currentDir + "index.jsp";
                } else {
                        expandLink = currentPage;
                }
%>
<table width="432" cellpadding="0" cellspacing="0" border="0" class="tree">

<sa:Directory id="node">
    <% 
 if (node.isDirectory()) { %>
        <tr valign="top">
		<td><img src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%= 10 * (depth.intValue()-1) %>"><a href="<%=expandLink%>?expand=<%= path %><% if (incDisplay){%>&display=<%=request.getParameter("display")%><%}%>">&curren;</a>&nbsp;<a href="<%=currentDir%>contents.jsp?display=<%= path %>"><%= path.substring(path.lastIndexOf(java.io.File.separator)+1) %></a></td></tr>
    <%  } %>
</sa:Directory>
</table>
