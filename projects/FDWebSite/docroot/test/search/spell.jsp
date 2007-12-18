
<%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload' %>
<%@ page import='org.apache.commons.fileupload.FileItemFactory' %>
<%@ page import='org.apache.commons.fileupload.FileItem' %>
<%@ page import='org.apache.commons.fileupload.disk.DiskFileItemFactory' %>

<%@ page import='com.freshdirect.framework.util.CSVUtils' %>
<%@ page import='com.freshdirect.framework.util.DiscreteRandomSampler' %>

<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.content.SearchResults' %>

<%@ page import='java.util.List' %>
<%@ page import='java.util.Random' %>

<%@ page import='java.io.FileOutputStream' %>

<html>

<head>
<title>Spell tester</title>
</head>

<body style="font-family: arial">

<% 
if (ServletFileUpload.isMultipartContent(request)) {
// Create a factory for disk-based file items
FileItemFactory factory = new DiskFileItemFactory();

// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload(factory);

// Parse the request
List /* FileItem */ items = upload.parseRequest(request);

long samples = 0;

DiscreteRandomSampler sampler = new DiscreteRandomSampler();

for(Iterator i = items.iterator(); i.hasNext(); ) {
	FileItem item = (FileItem)i.next();
	if ("file".equals(item.getFieldName())) {
  		List distro = CSVUtils.parse(item.getInputStream(),false,false);
%>
   		<%= distro.size() %> rows!<br/>

<%
		for(Iterator di=distro.iterator(); di.hasNext(); ) {
			List row = (List)di.next();
			sampler.addValue(row.get(0),Long.parseLong(row.get(1).toString()));
		}
	} else if ("n".equals(item.getFieldName())) {
		samples = Long.parseLong(item.getString());
		%>Will use <%=samples%> samples!<%
	}
}

%>

<%
	final DiscreteRandomSampler threadSampler = sampler;
	final long threadSamples = samples;
	new Thread(
		new Runnable() {
			public void run() {
				FileOutputStream os = null;
				Random R = new Random();
				try {
					new File("work" + File.separatorChar + "search").mkdirs();
					os = new FileOutputStream("work" + File.separatorChar + "search" + File.separatorChar + 
						"SpellerTest_" + threadSamples + '_' + System.currentTimeMillis() + ".csv");
					for(int i=0; i< threadSamples; ++i) {
						try {
							StringBuffer row = new StringBuffer();
							String query = threadSampler.getRandomValue(R).toString();
							long freq = threadSampler.getFrequency(query);
							SearchResults results = ContentFactory.getInstance().search(query);

							String suggestion = results.getSpellingSuggestion();
							if (suggestion == null) suggestion = "-";

							row.
								append(CSVUtils.escape(query)).append(',').
								append(freq).append(',').
								append(results.getExactCategories().size()).append(',').
								append(results.getExactProducts().size()).append(',').
								append(results.getFuzzyProducts().size()).append(',').
								append(results.getRecipes().size()).append(',').
								append(results.isProductsRelevant()).append(',').
								append(results.isSuggestionMoreRelevant()).append(',');

							SearchResults.SpellingResultsDifferences diffs = results.getSpellingResultsDifferences();
							if (diffs == null) row.append("-1,-1,-1,");
							else {
								row.
									append(diffs.getOriginal()).append(',').
									append(diffs.getSuggested()).append(',').
									append(diffs.getIntersection()).append(',');
							}
							row.append(CSVUtils.escape(suggestion)).append('\n');

							os.write(row.toString().getBytes());
							os.flush();
						} catch(Exception e) {
							continue;
						}

					}
				} catch(Exception e) {
				} finally {
					try {
						os.close();
					} catch(Throwable t) {
					}
				}
				
			}
		}
	).start();

%>
       <p><b>Csoki! Dolgozom!</b></p>
       <p><a href="/test/search/report.jsp"><i>Click here to see how I am doing</i></a></p>
       <p><a href="/test/search/spell.jsp">Start another test</a></p>
<%
%>
<% } else { // work file
%>
<b>Upload the Distribution CSV file and the number of samples you want!</b>
<p>
<form action="/test/search/spell.jsp" method="post" enctype="multipart/form-data">

<input type="file" name="file"/><br/>
How many?  <input type="text" name="n" size="10"/><br/>
<input type="submit">
</form>
</p>
<hr/>
<p>
<b>What is this?</b>
<p>
You need to obtain (or create) a CSV file with two columns. The first is a search term and the second is its 
frequency (or weight if you wish). E.g.
<pre>
   "<a href="/search.jsp?searchParams=dite+coki">dite coki</a>",100
   "<a href="/search.jsp?searchParams=majonaise">majonaise</a>",212
   "<a href="/search.jsp?searchParams=mgnum+condoms">mgnum condoms</a>",2003
   ...
</pre>
Then provide how many samples you want to pull from this file. It will sample according to the distribution
you provided. The test will run in its own thread, so you can inspect partial results as the report is
being compiled.
</p>
</p>
<%
   }
%>

</body>

</html>
