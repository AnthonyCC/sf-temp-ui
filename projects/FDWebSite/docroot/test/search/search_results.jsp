<%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload' %>
<%@ page import='org.apache.commons.fileupload.FileItemFactory' %>
<%@ page import='org.apache.commons.fileupload.FileItem' %>
<%@ page import='org.apache.commons.fileupload.disk.DiskFileItemFactory' %>

<%@ page import='java.util.Iterator' %>
<%@ page import='java.util.List' %>

<%@ page import='java.io.File' %>
<%@ page import='java.io.FileOutputStream' %>

<%@ page import='com.freshdirect.framework.util.CSVUtils' %>

<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.content.ContentSearch' %>
<%@ page import='com.freshdirect.fdstore.content.SearchResults' %>

<html>
<head>
<title>Search Result Tester</title>
</head>

<body style="font-family: arial">

<%

if (ServletFileUpload.isMultipartContent(request)) {
// Create a factory for disk-based file items
FileItemFactory factory = new DiskFileItemFactory();

// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload(factory);

List queries = null;
// Parse the request
List /* FileItem */ items = upload.parseRequest(request);
for(Iterator i = items.iterator(); i.hasNext(); ) {
	FileItem item = (FileItem)i.next();
	if ("file".equals(item.getFieldName())) {
  		queries = CSVUtils.parse(item.getInputStream(),false,false);
%>
   		<%= queries.size() %> rows!<br/>

<%
	} 
}

if (queries != null) {
	final List Q = queries;
	new Thread(
		new Runnable() {
			public void run() {
				try {
					new File("work" + File.separatorChar + "search").mkdirs();
					FileOutputStream os = new FileOutputStream("work" + File.separatorChar + "search" + File.separatorChar + 
							"SearchResultTest_" + Q.size() + '_' + System.currentTimeMillis() + ".csv");
					int ind = 0;
					ContentSearch searcher = ContentSearch.getInstance();
					for(Iterator i = Q.iterator(); i.hasNext(); ) {
						boolean odd = true;
						for(Iterator j= ((List)i.next()).iterator(); j.hasNext(); odd = !odd) {
							String col = j.next().toString();
							if (odd) { 
								os.write(("" + ind).getBytes());
								os.write(',');
								os.write(CSVUtils.escape(col).getBytes("utf-8"));
								os.write(',');
								SearchResults SR = searcher.search(col);
								os.write(("" + SR.getProducts().size()).getBytes());
								os.write(',');
								os.write(("" + SR.getRecipes().size()).getBytes());
								os.write(',');
								os.write(("" + SR.isSuggestionMoreRelevant()).getBytes());
							} else {
								os.write(',');
								os.write(col.getBytes());
								os.write('\n');
							}
						}
						++ind;
					}
				} catch(Exception e) {
				}
			}
		}
	).start();
}

%>
Working!

See <a href="/test/search/search_results_report.jsp">how</a> I am doing, or <a href="/test/search/search_results.jsp">submit</a> another test.
<%


} else {
%>

<form action="/test/search/search_results.jsp" method="post" enctype="multipart/form-data">
<div>
CSV file: <input type="file" name="file"/>
</div>
<br/>
<input type="submit"/>


</form>

<br/>
<br/>
<b>What is this?</b>
<p>
Supply a csv (comma separated values) file in the following format:

<pre>
   term1, freq1, term2, freq2, ..., termk, freqk
   term1, freq1, ...
</pre>

The terms (with compulsory frequencies) in the same line will be reported as a group (e.g. they have the same stem, or they should produce the same search results).
Good luck.
</p>

<%
}
%>


</body>

