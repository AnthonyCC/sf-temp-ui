<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.search.SearchHit"%>
<%@page import="com.freshdirect.fdstore.content.SearchResults"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearchUtil"%>
<%@page import="java.net.URLEncoder"%><html>
<head>
	<title>Search Compare Page</title>
	<link rel="stylesheet" type="text/css" href="config.css" />
</head>
<body>
<%
	String s1 = request.getParameter("search1");
	String s2 = request.getParameter("search2");
	if (s1 == null) {
	    s1 = "";
	}
	if (s2 == null) {
	    s2 = "";
	}

    SearchResults res1 = s1.trim().length() > 0 ? ContentSearch.getInstance().search(s1) : null;
    SearchResults res2 = s2.trim().length() > 0 ? ContentSearch.getInstance().search(s2) : null;

	
%>

<form method="get">
	Search term 1 : <input type="text" name="search1" value="<%= s1 %>"/> 
	<% if (s1.trim().length() > 0 ) { %><a href="/search.jsp?searchParams=<%= URLEncoder.encode(s1) %>">Live Search</a><% } %>
	<br/>  
	Search term 2 : <input type="text" name="search2" value="<%= s2 %>"/>
	<% if (s2.trim().length() > 0 ) { %><a href="/search.jsp?searchParams=<%= URLEncoder.encode(s2) %>">Live Search</a><% } %>
	<br/>
	<input type="submit" value="Search!">  
</form>



<% if (res1 != null && res2 != null) {
    List<SearchHit> l1 = res1.getProductSearchHit();
    Map<ContentKey, SearchHit> map = ContentSearchUtil.toMap(res2.getProductSearchHit());
    
    %>
    Search result count for <b><%= s1 %></b> is <i><%= l1.size() %></i>, for <b><%= s2 %></b> is <i><%= res2.getProductSearchHit().size() %></i>. 
	<table>
	<tr class="head">
		<td class="head">Content Key</td>
		<td class="head">Full Name</td>
		<td class="head">Score for <%= s1 %></td>
		<td class="head">Keywords</td>
		<td class="head">Score for <%= s2 %></td>
	</tr>
    <%
    boolean even = false;
    for (SearchHit p : l1) {
        SearchHit other = map.remove(p.getContentKey());
        even = !even;
        String cls = even ? "even":"odd";
        %>
    	<tr <%= other == null ? "style=\"background-color:red\"" : "" %>>
    	<td class="<%= cls %>"><%= p.getContentKey().getId() %></td>
    	<td class="<%= cls %>"><%= p.getNode() != null ? p.getNode().getFullName() : "<b>empty</b>" %></td>
    	<td class="<%= cls %>"><%= p.getScore() %></td>
    	<td class="<%= cls %>"><%= p.getKeywords() %></td>
    	<% if (other != null) { %>
    	<td class="<%= cls %>"><%= other.getScore() %></td>
    	<% } else { %>
    	<td class="<%= cls %>"><b>Missing : <i><%= s2 %></i>!</b></td>
    	<% } %>
    </tr> 
    <%
    }
    %>
    <%
    for (SearchHit p : map.values()) {
        even = !even;
        String cls = even ? "even":"odd";
    	%><tr <%= (even ? "class=\"even\"" : "class=\"odd\"") %>>
    	<td class="<%= cls %>"><%= p.getContentKey() %></td>
    	<td class="<%= cls %>"><%= p.getNode() != null ? p.getNode().getFullName() : "<b>empty</b>" %></td>
    	<td class="<%= cls %>"><b>Missing : <i><%= s1 %></i>!</b></td>
    	<td class="<%= cls %>"><%= p.getKeywords() %></td>
    	<td class="<%= cls %>"><%= p.getScore() %></td>
        <%
    }
    %></table><% 
}
%>
</body>
</html>