<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.webapp.util.*,
				   java.io.InputStream,
				   com.freshdirect.cms.fdstore.*'
%><%@ page import="com.freshdirect.framework.webapp.*"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.*"
%><%@ page import="com.freshdirect.content.attributes.*"
%><%@ page import="com.freshdirect.fdstore.util.URLGenerator"
%><%@ page import="com.freshdirect.fdstore.*"
%><%@ page import="com.freshdirect.fdstore.content.*"
%><%@ page import='com.freshdirect.fdstore.attributes.*'
%><%@ page import="java.util.*"
%><%@ page import="java.net.URLEncoder"
%><%@ page import="java.text.DecimalFormat"
%><%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload' %>
<%@ page import='org.apache.commons.fileupload.FileItemFactory' %>
<%@ page import='org.apache.commons.fileupload.FileItem' %>
<%@ page import='org.apache.commons.fileupload.disk.DiskFileItemFactory' %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="com.freshdirect.webapp.taglib.test.SearchSnapshot"%>
<%@page import="com.freshdirect.webapp.taglib.test.SearchSnapshot.SearchData"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.webapp.taglib.test.SnapshotGenerator"%>
<%@page import="com.freshdirect.smartstore.fdstore.ProductStatisticsProvider"%>
<%@page import="com.freshdirect.cms.ContentKey.InvalidContentKeyException"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%
/**
* author: gmark
*/

	URLGenerator g = new URLGenerator(request);

	FDUserI fdUser;
	String userId = null;
	if (request.getParameter("userID") != null) {
		userId = request.getParameter("userID").trim();
		fdUser = FDCustomerManager.getFDUser(new FDIdentity(userId));
	} else {
 		fdUser = (FDUserI) session.getAttribute(SessionName.USER);
 		if (fdUser != null && fdUser.getIdentity() != null) {
 			userId = fdUser.getIdentity().getErpCustomerPK();
 		}
 	}
	
	SearchSnapshot snapshot = null;
	
	// Snapshot constructing
	if (g.get("snapshot1") != null && !"".equals(g.get("snapshot1")) &&
		g.get("snapshot2") != null && !"".equals(g.get("snapshot2"))) {
		snapshot = new SearchSnapshot();		
		snapshot.init(SnapshotGenerator.getSnapshotStream(g.get("snapshot1")),
					  SnapshotGenerator.getSnapshotStream(g.get("snapshot2")),
					  (String)g.get("sortBy")
		);
	}
	
%>
<html>
<head>
<title>SmartSearch Test Page</title>
<style>
	body {
		font-family: arial, sans;
	}
	ul, h3, h4 {
		margin: 0px;
		padding: 0px;
	}
	h3 {
		margin-bottom: 10px;
	}
	ul {
		padding-left: 20px;
	}
	li {
		list-style-type: decimal;
		margin-left: 20px;		
	}
	table {
		border-collapse: collapse;		
	}
	table thead td {
		text-align: center;		
		width: 300px;
	}
	table td {
		border: 1px solid black;		
		padding: 5px; 
		vertical-align: top;
	}	
	.product {
		width: 280px;
		text-indent: -10px;
		padding-left: 10px;
		font-weight: bold;
	}
	
	.productname {				
		color: #336600;		
	}
	
	.productinfo {
		font-size: smaller;
		font-weight: normal;
		width: 280px;
		padding-left: 10px;
		text-indent: -10px;
	}
	label {
		margin-right: 10px;
	}
	
	a {
		color: blue;
	}
	
	.bold {
		font-weight: bold;
	}
	
	.error {
		color: red;
		margin-top: 5px;
		margin-bottom: 5px;
	}
	
	.error li {
		list-style-type: disc;
	}
	
</style>
</head>
<body>

<!-- Snapshot chooser -->
<div>

<form method="post" enctype="multipart/form-data">
<fieldset style="margin-bottom: 10px;width: 974px;">
<legend>Generate Search Snapshot</legend>
<%
	boolean running = SnapshotGenerator.isRunning();
	String uploadError = "";
	String snapshotname = "";
	String searchTerms = null;

	if (ServletFileUpload.isMultipartContent(request)) {
		// A process is already running, but the user posted another file
		if (!running) {								
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
	
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
	
			FileItem csvfile = null;
			// Parse the request
			List /* FileItem */ items = upload.parseRequest(request);
			for(Iterator i = items.iterator(); i.hasNext(); ) {
				FileItem item = (FileItem)i.next();	
				if ("snapshotname".equals(item.getFieldName())) {
					snapshotname = item.getString();
					
				}
				if ("csvfile".equals(item.getFieldName())) {
					csvfile = item;
				}
				if ("searchTerms".equals(item.getFieldName())) { 
				    searchTerms = item.getString();
				}
			}
			if ( snapshotname.equals("") ) {
				uploadError += "<li>Missing snapshot name!</li>"; 
			}
			
			if (csvfile == null || csvfile.getSize() == 0) {
			    if (searchTerms == null || searchTerms.trim().length() == 0) {
					uploadError += "<li>Missing CSV file!</li>";
			    } else {
			        csvfile = null;
			    }
			}
			
			if (uploadError.equals("")) {
				running = true;
				
				// Start generating
				SnapshotGenerator.startGenerating(csvfile != null ? csvfile.getInputStream() : new ByteArrayInputStream(searchTerms.getBytes()), 
				        snapshotname, fdUser);
			}
		}
		if (!uploadError.equals("")) {
%>
	<ul class="error">
		<%=uploadError%>
	</ul> 
<%			
		}
	}		

	if (!running) {
%>	
	<div><label for="snapshotname">Snapshot name: </label><input type="text" name="snapshotname" id="snapshotname" value="<%=snapshotname%>"></div>
	<div><label for="csvfile">Search terms in a CSV file:</label><input type="file" id="csvfile" name="csvfile"></div>
	<div><label for="searchTerms">Search terms:</label><textarea name="searchTerms" id="searchTerms"><%= searchTerms != null ? searchTerms : "" %></textarea></div>
	<div><label for="userID">User ID:</label><input type="text" name="userID" id="userID" value="<%= (userId != null ? userId : "") %>"></input></div>
	<div><input type="submit" value="Upload file"></div>
<%
	} else {
%>
	<div>A term list is already under processing... <a href="/test/search/smart_search_log.jsp">Show log</a></div> 
<%
	}
%>
</fieldset> 
</form>

<form method="get" action="/test/search/smart_search.jsp">
<% if (request.getParameter("sortBy") != null) { %>
<input type="hidden" name="sortBy" value="<%= request.getParameter("sortBy") %>"></input>
<% } %>
<fieldset style="margin-bottom: 10px;width: 974px;">
<legend>Search Result Snapshots</legend>
	<div><label for="snapshot1">Baseline:</label><select id="snapshot1" name="snapshot1">
<%
	// Get snapshot names for the select inputs
	Iterator snapshotIt;
	if (SnapshotGenerator.getSnapshotNames() != null) {
		snapshotIt = Arrays.asList(SnapshotGenerator.getSnapshotNames()).iterator();
		while (snapshotIt.hasNext()) {				
			String snapshotName = (String) snapshotIt.next();
%>
	<option value="<%=snapshotName %>" <%=g.get("snapshot1") != null && snapshotName.equals(g.get("snapshot1")) ? "selected" : "" %>><%=snapshotName%></option>
<%		
		}
	}

%>
	</select></div>
	<div><label for="snapshot2">Current:</label><select id="snapshot2" name="snapshot2">
<%
	// Get snapshot names for the select inputs	
	if (SnapshotGenerator.getSnapshotNames() != null) {
		snapshotIt = Arrays.asList(SnapshotGenerator.getSnapshotNames()).iterator();
		while (snapshotIt.hasNext()) {				
			String snapshotName = (String) snapshotIt.next();
%>
	<option value="<%=snapshotName %>" <%=g.get("snapshot2") != null && snapshotName.equals(g.get("snapshot2")) ? "selected" : "" %>><%=snapshotName%></option>
<%		
		}
	}

%>
	</select></div>
<script type="text/javascript">
	function swapSnapshots() {
		var s1obj = document.getElementById("snapshot1");
		var s2obj = document.getElementById("snapshot2");
		var v1 = s1obj.value;
		var v2 = s2obj.value;
		s1obj.value = v2; s2obj.value = v1;
	}
</script>
	<div><input type="submit" value="Compare snapshots" style="margin-top: 10px;"></div>
</fieldset>
</form>
</div>
<!-- /Snapshot chooser -->
<%
	if (snapshot != null ) {
		if (snapshot.getSearchTerms() != null) {
			
%>

<!-- Search term list -->
<div style="width: 300px; float: left">
<h3>Search Terms</h3>
<div style="margin-bottom: 10px">
	Order by:
	<a class="<%=g.get("sortBy") == null || g.get("sortBy").equals("") || g.get("sortBy").equals("popularity") ? "bold" : ""%>" href="<%=g.set("sortBy", "popularity").buildNew() %>">popularity</a>
	<a class="<%=g.get("sortBy") != null && g.get("sortBy").equals("correlation") ? "bold" : ""%>" href="<%=g.set("sortBy", "correlation").buildNew() %>">correlation</a>
	<a class="<%=g.get("sortBy") != null && g.get("sortBy").equals("moves") ? "bold" : ""%>" href="<%=g.set("sortBy", "moves").buildNew() %>">difference</a>
</div>
<div style="margin-bottom: 10px">
	<span>User ID of 1st snapshot: <%= snapshot.getUserId(0) %></span><br/>
	<span>User ID of 2nd snapshot: <%= snapshot.getUserId(1) %></span><br/>
	<span>Factor of 1st snapshot: <%= snapshot.getFactor(0) %></span><br/>
	<span>Factor of 2nd snapshot: <%= snapshot.getFactor(1) %></span>
</div>
<ul>
<%
			
			Iterator termIt = snapshot.getSearchTerms().iterator(); 
			while (termIt.hasNext()) {
				String term = (String) termIt.next();
%>
<li><a href="<%=g.set("term", term).buildNew()%>" class="<%=g.get("term") != null && g.get("term").equals(term) ? "bold" : "" %>"><%= term %> (<%=((double)Math.round(snapshot.getCorrelationForTerm(term)*100))/100 %>)</a></li>
<%
			}
%>
</ul>
<%
		}
%>

</div>
<!-- /Search term list -->

<!-- Search result list -->
<%
		if (snapshot.getResultList(g.get("term"), 0) != null &&
			snapshot.getResultList(g.get("term"), 1) != null
		) {
			StringBuffer swuri = new StringBuffer();
			swuri.append(request.getRequestURI());
			swuri.append("?");
			int t = 0;
			Map m = request.getParameterMap();
			for (Iterator it=m.keySet().iterator(); it.hasNext();) {
				String key = (String)it.next();
				String n_key = key;
				if ("snapshot1".equalsIgnoreCase(key)) {
					n_key = "snapshot2";
				} else if ("snapshot2".equalsIgnoreCase(key)) {
					n_key = "snapshot1";
				}

				if (t>0)
					swuri.append("&");
				
				String vals[] = (String[]) m.get(n_key);
				for (int k=0; k<vals.length; k++)
					swuri.append(key + "=" + vals[k]);
				++t;
			}
%>
<div style="float: left; width: 690px;">
<table>
	<caption>
		<h3>Search Results for "<%=g.get("term")%>"</h3>
		<h4 style="margin-bottom:0px"><a href="/search.jsp?searchParams=<%=g.get("term") %>">See results as a real search</a></h4>
		<div style="margin-bottom: 10px;margin-top:10px;">
			Order by:
			<a class="<%=g.get("resultSortBy") == null || !g.get("resultSortBy").equals("diff") ? "bold" : ""%>" href="<%=g.set("resultSortBy", "original").buildNew() %>">result order</a>
			<a class="<%=g.get("resultSortBy") != null && g.get("resultSortBy").equals("diff") ? "bold" : ""%>" href="<%=g.set("resultSortBy", "diff").buildNew() %>">difference</a>
			&nbsp;&bull;&nbsp;<a href="<%= swuri.toString() %>">Swap Snapshots</a>
		</div>
	</caption>	

<%
			if (g.get("term") != null && !g.get("term").equals("")) {
				Iterator resultIt = snapshot.getAggregateResultList(g.get("term"), g.get("resultSortBy") != null && g.get("resultSortBy").equals("diff")).iterator();

				while (resultIt.hasNext()) {
					SearchData item = (SearchData) resultIt.next();
					try {
					    ContentKey key = ContentKey.create(FDContentTypes.PRODUCT, item.getId());
						ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(key);
						if (product!=null) {
							int diff = Math.max(0,item.getPositionA()) - item.getPositionB();
							int pos = item.getPositionB();	
							String style = "";
							if (item.getPositionB() == -1) {
								style = "style=\"background-color: red\"";
							}
							if (item.getPositionA() == -1) {
								style = "style=\"background-color: lightgreen\"";	
							}
							
							// get parent categories
							ContentNodeModel parent = product.getParentNode();
							List catList = new ArrayList();
							while (parent instanceof CategoryModel) {
								catList.add(parent);
								parent = parent.getParentNode();
							}
							
							String taxonomy = "";
							
							for (int k=catList.size()-1; k>=0; --k) {
								CategoryModel c = (CategoryModel) catList.get(k);
								if (k>0) {
									taxonomy += c.getFullName() + " &raquo; ";
								} else {
									taxonomy += c.getFullName();
								}
							}
							
							taxonomy = ((DepartmentModel) parent).getFullName().toUpperCase() + " &raquo; " +taxonomy;
							String diffStyle = "";
							if (diff > 0) diffStyle = "color: green";
							if (diff < 0) diffStyle = "color: red";
%>
	<tr <%=style %>>
		<td style="text-align: right; font-weight: bold"><%=(pos == -1 ? "" : Integer.toString(pos + 1)) %>.</td>
		<td style="text-align: right; font-weight: bold;<%=diffStyle %>"><%=(diff > 0 ? "+" : "") + Integer.toString(diff) %></td>
		<td><display:ProductImage product="<%=product %>" prefix="http://www.freshdirect.com"/></td>
		<td>
			<div><span style="font-weight: bold"><%=product.getFullName() %></span></div>
			<div style="margin-bottom: 5px;"><span style="font-style: italic;font-size: smaller"><%=taxonomy %></span></div>
			<div>Popularity: <%=ProductStatisticsProvider.getInstance().getGlobalProductScore(ContentKey.create(FDContentTypes.PRODUCT, item.getId())) %></div>
			<div>Baseline factors : <span class="baseLineFactor"><%= item.getFactorValue(0) %></span></div>
			<div>Current factors :  <span class="currentFactor"><%= item.getFactorValue(1) %></span></div>
		</td>		
	</tr>
<%
						}
					} catch (ContentKey.InvalidContentKeyException e) {
					    e.printStackTrace();
					}
				}
			}
%>
</table>
</div>
<%
		}
	}
%>
<!-- /Search result list -->
</body>
</html>