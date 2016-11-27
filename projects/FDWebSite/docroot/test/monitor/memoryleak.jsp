<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	if (request.getParameter("delete") != null) {
		int index = Integer.parseInt(request.getParameter("delete"));
		MemoryLeakCache.getInstance().removeLeak(index);
		response.sendRedirect(request.getRequestURI());
		return;
	} else if (request.getParameter("size") != null) {
		int size = MemoryLeakCache.parseLeakSize(request.getParameter("size"));
		MemoryLeakCache.getInstance().addLeak(size);
		response.sendRedirect(request.getRequestURI());
		return;
	}
%>
<%@page import="com.freshdirect.framework.util.MemoryLeakCache"%>
<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" lang="en-US">
<title>Memory Leak Administrator Page</title>
</head>
<body>
<form action="<%= request.getRequestURI() %>" method="post" style="padding: 30px;">
	<input type="text" name="size"> <input type="submit" value="Submit">
</form>
<div style="padding: 0px 30px;">
	<table border="1" cellpadding="0" cellspacing="0">
		<tr>
			<th>Size</th>
			<th>Action</th>
		</tr>
		<%
			List<Integer> sizes = MemoryLeakCache.getInstance().getLeakSizes();
			for (int i = 0; i < sizes.size(); i++) {
				int size = sizes.get(i);
		%>
		<tr>
			<td><%= MemoryLeakCache.formatSize(size) %></td>
			<td><input type="button" value="Delete" onclick="window.location.href = '<%= request.getRequestURI() %>?delete=<%= i %>';"></td>
		</tr>
		<%
			}
		%>
	</table>
</div>
</body>
</html>