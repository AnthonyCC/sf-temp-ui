<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.freshdirect.fdstore.cache.ExternalSharedCacheMBean"%>
<%@page import="com.freshdirect.fdstore.cache.FDAbstractCache"%>
<%@page import="java.net.SocketAddress"%>
<%@page import="net.spy.memcached.MemcachedClient"%>
<%@page import="com.freshdirect.framework.cache.MemcacheConfiguration"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@page import="com.freshdirect.fdstore.FDCachedFactory"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.framework.cache.CacheStatisticsProvider"%>
<%@page import="com.freshdirect.fdstore.FDNutritionCache"%>
<%@page import="com.freshdirect.fdstore.FDCOOLInfoCache"%>
<%@page import="com.freshdirect.fdstore.FDProductInfoCache"%>
<%@page import="com.freshdirect.fdstore.FDProductCache"%>
<%@page import="com.freshdirect.fdstore.FDAttributesCache"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.framework.cache.CacheI"%>
<%@page import="java.util.List"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/test/search/config.css" />
	<title>Cache Statistics</title>
</head>
<%!
	static String getEvenOdd(int i) {
		return i  % 2 == 0 ? "even" : "odd";
	}

	static String getEvenOddStat(int i) {
		return i  % 2 == 0 ? "even-stat" : "odd-stat";
	}
%>
<%
	if (request.getParameter("command") != null) {
	    try {
		    String host = request.getParameter("host");
		    int port = Integer.parseInt(request.getParameter("port"));
		    MemcacheConfiguration.configureClient(host, port);
	    } catch (Exception e) {
	    }
	}
	List<CacheI> caches = new ArrayList();
	caches.add(FDAttributesCache.getInstance());
	caches.add(FDProductCache.getInstance());
	caches.add(FDProductInfoCache.getInstance());
	caches.add(FDCOOLInfoCache.getInstance());
	caches.add(FDNutritionCache.getInstance());
	caches.add(FDCachedFactory.getZoneCache());
	if (request.getParameter("refresh") != null) {
	    String refresh = request.getParameter("refresh");
	    for (CacheI c : caches) {
	        if (refresh.equals(c.getName()) && c instanceof FDAbstractCache) {
	            ((FDAbstractCache)c).refreshCache();
	            response.sendRedirect(request.getRequestURI()+"?msg=refreshResult");
	            return;
	        }
	    }
	}
	if (request.getParameter("syncExternal") != null) {
	    String refresh = request.getParameter("syncExternal");
	    for (CacheI c : caches) {
	        if (refresh.equals(c.getName()) && c instanceof ExternalSharedCacheMBean) {
	            int x = ((ExternalSharedCacheMBean)c).storeDataToExternalCache();
	            response.sendRedirect(request.getRequestURI() + "?msg=syncResult&count="+x);
	            return;
	        }
	    }
	}
	if (request.getParameter("countExternal") != null) {
	    String refresh = request.getParameter("countExternal");
	    for (CacheI c : caches) {
	        if (refresh.equals(c.getName()) && c instanceof ExternalSharedCacheMBean) {
	            int x = ((ExternalSharedCacheMBean)c).getAlreadyStoredItemCount();
	            response.sendRedirect(request.getRequestURI() + "?msg=countExternal&count="+x);
	            return;
	        }
	    }
	}
	String message = null;
	
	if (request.getParameter("msg") != null) {
	    String msg = request.getParameter("msg");
	    if ("refreshResult".equals(msg)) {
	        message = "Cache refreshed.";
	    } else if ("syncResult".equals(msg)) {
	        message = "In VM data stored into the external cache ["+Integer.parseInt(request.getParameter("count"))+" items]";
	    } else if ("countExternal".equals(msg)) {
	        message = "Compare VM data with the external cache : "+Integer.parseInt(request.getParameter("count"))+" already in memcached.";
	    }
	}
	
	int lineCount = 0;
%>
<body >
<% if (message != null) { %>
	<h2><%= message %></h2>
<% } %>

	<table>
		<thead>
			<tr>
				<td colspan="2" class="head">
					Memcache Configuration
				</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="even">Memcached working:</td>
				<td class="even"><%= (MemcacheConfiguration.isEnabled() && (MemcacheConfiguration.getClient() != null)) %></td>
			</tr>
			<tr>
				<td class="odd">fdstore.properties:</td>
				<td class="odd"><%= FDStoreProperties.getMemCachedHost()%> : <%= FDStoreProperties.getMemCachedPort() %></td>
			</tr>
			<tr>
				<td class="even">Current configuration:</td>
				<td class="even"><%= MemcacheConfiguration.getHost() %> : <%= MemcacheConfiguration.getPort() %></td>
			</tr>
			<form method="post">
				<tr>
					<td class="odd">Host:</td>
					<td class="odd"><input type="text" name="host" value="<%= MemcacheConfiguration.getHost() %>"/></td>
				</tr>
				<tr>
					<td class="even">Port:</td>
					<td class="even"><input type="text" name="port" value="<%= MemcacheConfiguration.getPort() %>"/></td>
				</tr>
				<tr>
					<td  class="odd" colspan="2">
						<input type="submit" name="command" value="Change">
					</td>
				</tr>
			</form>				
		</tbody>
	</table>
<%   
	MemcachedClient client = MemcacheConfiguration.getClient(); 
	if (client != null) {
%>
	<table>
		<thead>
			<tr>
				<td colspan="2" class="head">Memcached Statistics</td>
			</tr>
		</thead>
		<tbody>
<%	    
        Map<SocketAddress, Map<String, String>> stats = client.getStats();
        for (Map.Entry<SocketAddress, Map<String, String>> k : stats.entrySet()) {
			for (Map.Entry<String, String> e : k.getValue().entrySet()) {
			    lineCount++;
			    %>
				<tr>
					<td class="<%= getEvenOdd(lineCount) %>"> <%= e.getKey() %></td>
					<td class="<%= getEvenOddStat(lineCount) %>"> <%= e.getValue() %></td>
				</tr>
			    <%
			}
        }
%>
		</tbody>
	</table>		
<%
	}
%>		
		
	<table>
		<thead>
			<tr>
				<td colspan="2" class="head">Cache Statistics</td>
			</tr>
		</thead>
		<tbody>
		<% for (CacheI c : caches) { 
			Map<String, String> map = (c instanceof CacheStatisticsProvider) ? ((CacheStatisticsProvider)c).getStats() : Collections.EMPTY_MAP;
		%>
			<tr>
				<td colspan="2" class="head"><%= c.getName() %>
				<% if (c instanceof FDAbstractCache) { %>
					<a href="?refresh=<%= c.getName() %>">[check db]</a>
				<% }  %>
				<% if (c instanceof ExternalSharedCacheMBean) { %>
					<a href="?syncExternal=<%= c.getName() %>">[Sync to memcache]</a>
					<a href="?countExternal=<%= c.getName() %>">[Count items already in memcache]</a>
				<% } %>
				</td>
			</tr>
			<tr>
				<td>Size</td><td><%= c.size() %></td>
			</tr>
			<tr>
				<td colspan="2">
					<table>
					<% for (String key : new TreeSet<String> (map.keySet())) {
					    lineCount ++;
						/*if (!key.startsWith("memcache.")) {*/ 
					%>
						<tr>
							<td class="<%= getEvenOdd(lineCount) %>"><%= key %></td>
							<td class="<%= getEvenOddStat(lineCount) %>"><%= map.get(key) %></td>
						</tr>
					<% 	/*}*/
					  } %>
					</table>
				</td>
			</tr>
		<% } %>
		
		</tbody>
	
	</table>


</body>
