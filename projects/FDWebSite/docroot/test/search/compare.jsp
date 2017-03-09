<%@page import="com.freshdirect.cms.search.LuceneSearchService"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collection"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.search.SearchHit"%>
<%@page import="com.freshdirect.fdstore.content.SearchResults"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearchUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.hivemind.Registry"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="com.freshdirect.cms.search.ContentSearchServiceI"%>
<%@page import="java.util.HashMap"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<html>
<head>
	<title>Search Compare Page</title>
	<fd:css href="/assets/css/test/search/config.css" />
</head>
<body>
<%
	String s1 = request.getParameter("search1");
	String s2 = request.getParameter("search2");
	boolean approximate = request.getParameter("approximate") != null;
		
	if (s1 == null) {
	    s1 = "";
	}
	if (s2 == null) {
	    s2 = "";
	}

	Registry registry = FDRegistry.getInstance();
	ContentSearchServiceI search = new LuceneSearchService();

	Collection<SearchHit> res1 = s1.trim().length() > 0 ? search.searchProducts(s1, !approximate, !approximate, 2000) : null;
	Collection<SearchHit> res2 = s2.trim().length() > 0 ? search.searchProducts(s2, !approximate, !approximate, 2000) : null;

	
%>

<form method="get">
	Search term 1 : <input type="text" name="search1" value="<%= s1 %>"/> 
	<% if (s1.trim().length() > 0 ) { %><a href="/search.jsp?searchParams=<%= URLEncoder.encode(s1) %>">Live Search</a><% } %>
	<br/>  
	Search term 2 : <input type="text" name="search2" value="<%= s2 %>"/>
	<% if (s2.trim().length() > 0 ) { %><a href="/search.jsp?searchParams=<%= URLEncoder.encode(s2) %>">Live Search</a><% } %>
	<br/>
	<input type="checkbox" name="approximate"<%= approximate ? " checked=\"checked\"" : "" %>> Approximate
	<br/>
	<input type="submit" value="Search!">  
</form>



<% if (res1 != null && res2 != null) {
    Map<ContentKey, SearchHit> map = new HashMap<ContentKey, SearchHit>(res2.size() + 1);
    for (SearchHit hit : res2)
    	map.put(hit.getContentKey(), hit);
    
    %>
    Search result count for <b><%= s1 %></b> is <i><%= res1.size() %></i>, for <b><%= s2 %></b> is <i><%= res2.size() %></i>. 
	<table>
	<tr class="head">
		<td class="head">Content Key</td>
		<td class="head">Full Name</td>
		<td class="head">Score for <%= s1 %></td>
		<td class="head">Score for <%= s2 %></td>
	</tr>
    <%
    boolean even = false;
    for (SearchHit p : res1) {
        SearchHit other = map.remove(p.getContentKey());
        even = !even;
        String cls = even ? "even":"odd";
        %>
    	<tr <%= other == null ? "style=\"background-color:red\"" : "" %>>
    	<td class="<%= cls %>"><%= p.getContentKey().getId() %></td>
    	<td class="<%= cls %>"><%= p.getNode() != null ? p.getNode().getFullName() : "<b>empty</b>" %></td>
    	<td class="<%= cls %>"><%= p.getScore() %></td>
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
    	<td class="<%= cls %>"><%= p.getScore() %></td>
        <%
    }
    %></table><% 
}
%>
</body>
</html>