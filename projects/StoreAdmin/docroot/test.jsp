<%@ taglib uri='storeadmin' prefix='sa' %>
<html>
<head><title>test store tree tag</title></head>
<body>

<sa:Directory id="node">
    <%  if (node.isDirectory()) { %>
        <img src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%= 20 * (depth.intValue()-1) %>">
        <a href="test.jsp?expand=<%= path %>"><%= path %></a><br>
    <%  } else if (node.isFile()) { %>
        <img src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%= 20 * (depth.intValue()-1) %>">
        <%= path %><br>
    <%  } %>
</sa:Directory>

</body>
</html>
