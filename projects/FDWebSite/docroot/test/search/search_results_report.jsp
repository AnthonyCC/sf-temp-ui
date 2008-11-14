<html>
<%@ page import='java.util.Date' %>
<%@ page import='java.util.Iterator' %>
<%@ page import='java.util.List' %>
<%@ page import='java.util.Map' %>
<%@ page import='java.util.TreeMap' %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>

<%@ page import='java.util.TreeMap' %>

<%@ page import='java.net.URLEncoder' %>

<%@ page import='com.freshdirect.framework.util.CSVUtils' %>
<head>
	<title>Search terms report</title>
	<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>
	<style type="text/css">
body, td       { font-size: small; font-family: arial; color: #003333 }
td.even        { background: #ffeeee }
td.odd         { background: #eeeeff }
td.even-stat   { background: #eedddd }
td.odd-stat    { background: #ddddee }
td.head        { background: #000033; color: #aaaa00 }
div.high       { background: #ddddff }
	</style>
</head>
<%!

	private static class Info {
		public File file;
		public Date started;
		public long samples;

		public Info(File file) {
			this(file.getName());
			this.file = file;
		}

		public Info(String fn) {
			int s1 = fn.indexOf('_');
			int s2 = fn.indexOf('_',s1+1);

			samples = Long.parseLong(fn.substring(s1+1,s2));
			started = new Date(Long.parseLong(fn.substring(s2+1,fn.length()-4)));
		}
	};

	

	private static class InfoRow {
	        private int id;
		private String query;
		private int products;
		private int recipes;
		private boolean moreRelevant;
		private int frequency;

		private InfoRow(List row) {
			id = Integer.parseInt(row.get(0).toString());
			query = row.get(1).toString();
			products = Integer.parseInt(row.get(2).toString());
			recipes = Integer.parseInt(row.get(3).toString());
			moreRelevant = Boolean.getBoolean(row.get(4).toString());
			frequency = Integer.parseInt(row.get(5).toString());
		}

		public int getId() { return id; }
		public String getQuery() { return query; }
		public int getProducts() { return products; }
		public int getRecipes() { return recipes; }
		public boolean isMoreRelevant() { return moreRelevant; }
		public int getFrequency() { return frequency; }
	}

	private static class OddEven {

		private boolean odd = true;

		public void reset() { odd = true; }
		public void flip() { odd = !odd; }
		public String toString() {
			return odd ? "class=\"even\"" : "class=\"odd\"";
		}

		public String statField() {
			return odd ? "class=\"even-stat\"" : "class=\"odd-stat\"";
		}
	};


%>


<body>
<%

String[] files = request.getParameterValues("file");

if (files == null || files.length == 0) {
%>
<form action="/test/search/search_results_report.jsp" method="post">
<%
	File f = new File("work" + File.separatorChar + "search");

	File[] subs = f.listFiles();

	Map sortedFiles = new TreeMap();

	for(int i=0; subs!= null && i< subs.length; ++i) {
		String name = subs[i].getName();
		if (name.startsWith("SearchResultTest") && name.endsWith(".csv")) {
			try {
				Info info = new Info(subs[i]);
				sortedFiles.put(info.started,info);
			} catch(Exception e) {
				continue;
			}
		}
	} // each csv file

	for(Iterator i = sortedFiles.values().iterator(); i.hasNext(); ) {
		Info info = (Info)i.next();
%>
<table>
<tr>
<td>
		<input name="file" type="checkbox" value="<%=info.file%>"/> <i><%=info.samples%></i> samples, started at <i><%=info.started%></i>
</td>
</tr>
<%
	}
%>
<tr>
<td>
	<br/><br/>
	<input type="submit"/>
</td>
</tr>
</table>
</form>

<%
} else { // We have files

	for(int i=0; i< files.length; ++i) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(files[i]);
			Info info = new Info(files[i]);
			Iterator it = CSVUtils.rowIterator(is,false,false);
			int id = -1;
			OddEven oddEven = new OddEven();

%>
	<b>Run started <%=info.started%></b>
	<table>
			<tr>
				<th>QUERY</th>
				<th>FREQUENCY</th>
				<th>PRODUCTS</th>
				<th>RECIPES</th>
			</tr>
<%
			while(it.hasNext()) {
				List row = (List)it.next();
				if (row.size() < 8) continue;
				InfoRow ir = new InfoRow(row);
				if (ir.getId() != id) {
					oddEven.flip();
					id = ir.getId();
				}
%>
			<tr>
				<td <%=oddEven%> align="left" ><a href="/search.jsp?searchParams=<%=URLEncoder.encode(ir.getQuery(),"utf-8")%>"><%=ir.getQuery()%></a></td>
				<td <%=oddEven%> align="right"><%=ir.getFrequency()%></td>
				<td <%=oddEven%> align="right"><%=ir.getProducts()%></td>
				<td <%=oddEven%> align="right"><%=ir.getRecipes()%></td>
			</tr>
				
<%
			}

%>
	</table>

<%


		} catch(IOException e) {
			continue;
		} finally {
			try {
				is.close();
			} catch(Throwable t) {}
		}
	}
	
}
%>

</body>
</html>
