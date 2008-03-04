
<%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload' %>
<%@ page import='org.apache.commons.fileupload.FileItemFactory' %>
<%@ page import='org.apache.commons.fileupload.FileItem' %>
<%@ page import='org.apache.commons.fileupload.disk.DiskFileItemFactory' %>

<%@ page import='com.freshdirect.framework.util.CSVUtils' %>
<%@ page import='com.freshdirect.framework.util.DiscreteRandomSampler' %>

<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.content.SearchResults' %>

<%@ page import='java.util.List' %>
<%@ page import='java.util.Iterator' %>
<%@ page import='java.util.ArrayList' %>
<%@ page import='java.util.Random' %>

<%@ page import='java.io.FileOutputStream' %>

<html>

<head>
<title>Spell tester</title>
</head>

<body style="font-family: arial">

<%!

private interface Sampler {
	public void addValue(Object o, long freq);
	public String getNextQuery();
	public long getFrequency();
};

private class RandomSampler implements Sampler {
	private DiscreteRandomSampler sampler = new DiscreteRandomSampler();
	private Random R = new Random();
	private long freq = -1;

	public void addValue(Object o, long freq) {
		sampler.addValue(o,freq);
	}

	public String getNextQuery() {
		Object query = sampler.getRandomValue(R);
		freq = sampler.getFrequency(query);
		return query.toString();
	}

	public long getFrequency() {
		return freq;
	}
};

private class ExhaustiveSampler implements Sampler {
	private class Query {
		private String query;
		private long frequency;
		private Query(String query, long frequency) {
			this.query = query;
			this.frequency = frequency;
		}
	};

	private List values = new ArrayList();
	private Query q = null;
	private Iterator qI = null;

	private ExhaustiveSampler() {
	}

	public void addValue(Object o, long freq) {
		values.add(new Query(o.toString(),freq));
	}

	public String getNextQuery() {
		if (qI == null) qI = values.iterator();
		q= (Query)qI.next();
		return q.query;
	}
		
	public long getFrequency() {
		return q.frequency;
	}
};



%>

<% 
if (ServletFileUpload.isMultipartContent(request)) {
// Create a factory for disk-based file items
FileItemFactory factory = new DiskFileItemFactory();

// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload(factory);

// Parse the request
List /* FileItem */ items = upload.parseRequest(request);

long samples = 0;

boolean exhaustive = false;
long max = Long.MAX_VALUE;

for(Iterator i = items.iterator(); i.hasNext(); ) {
	FileItem item = (FileItem)i.next();
	if ("exhaustive".equals(item.getFieldName())) {
		exhaustive = true;
	} else if ("n".equals(item.getFieldName())) {
		max = Long.parseLong(item.getString());
	}
}

Sampler sampler = exhaustive ?
	(Sampler)new ExhaustiveSampler() :
	(Sampler)new RandomSampler();

for(Iterator i = items.iterator(); i.hasNext(); ) {
	FileItem item = (FileItem)i.next();
	if ("file".equals(item.getFieldName())) {
  		List distro = CSVUtils.parse(item.getInputStream(),false,false);
		if (exhaustive) samples = (long)distro.size();
%>
   		<%= distro.size() %> rows!<br/>

<%
		long c = 0;
		for(Iterator di=distro.iterator(); c < max && di.hasNext(); ++c) {
			List row = (List)di.next();
			sampler.addValue(row.get(0),Long.parseLong(row.get(1).toString()));
		}
	} else if ("n".equals(item.getFieldName())) {
		samples = Long.parseLong(item.getString());
		%>Will use <%=exhaustive ? (max == Long.MAX_VALUE ? "all" : "" + max) : ("" + samples)%> samples!<%
	} 
}

%>

<%
	final Sampler threadSampler = sampler;
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
							String query = threadSampler.getNextQuery();
							long freq = threadSampler.getFrequency();
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

<script type="text/javascript">
function toggleExhaustive() {
	var toggle = document.getElementById('toggle');
	var label = document.getElementById('lab');
	var boo = document.getElementById('boo');
	if (toggle.checked) {
		label.innerHTML = "first entries. (If not set, ALL!)";	
		boo.innerHTML = " and the ";
	} else {
		label.innerHTML = "random samples.";
		boo.innerHTML = " or ";
	}

}
</script>

<b>Upload the Distribution CSV file and the number of samples you want!</b>
<p>
<form action="/test/search/spell.jsp" method="post" enctype="multipart/form-data">

<div>
CSV file: <input type="file" name="file"/>
</div>
<br/>
<div>
<label>Exhaustive <input id="toggle" type="checkbox" name="exhaustive" value="true" onclick="toggleExhaustive()"/></label>
<span id="boo">or</span> <input type="text" id="n" name="n" size="10"/> <span id="lab">random samples</span>  
</div>
<input type="submit" value="go"/>
</form>
</p>
<hr/>
<p>
<b>What is this?</b>
<div>
You need to obtain (or create) a CSV file with two columns. The first is a search term and the second is its 
frequency (or weight if you wish). E.g.
<pre>
   "<a href="/search.jsp?searchParams=dite+coki">dite coki</a>",100
   "<a href="/search.jsp?searchParams=majonaise">majonaise</a>",212
   "<a href="/search.jsp?searchParams=mgnum+condoms">mgnum condoms</a>",2003
   ...
</pre>
</div>
</p>

<p>
<b>Sampling</b>
<div>
In random (non-exhaustive) sampling, this tool will sample according to the distribution you provided. 
In the case of exhaustive sampling, all queries in the file will be searched for in order and exactly once. For 
exhaustive sampling make sure that the file is <i>sufficiently small</i> or set the <i>Only the first ...</i> field!
</div>
</p>
<p>
The test will run in its own thread, so you can inspect partial results as the report is
being compiled.
</p>
<%
   }
%>

</body>

</html>
