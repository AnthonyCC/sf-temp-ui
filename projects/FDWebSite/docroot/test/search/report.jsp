<%@ page import='java.io.File' %>
<%@ page import='java.io.FileInputStream' %>
<%@ page import='java.io.InputStream' %>
<%@ page import='java.io.IOException' %>

<%@ page import='java.text.NumberFormat' %>

<%@ page import='java.util.List' %>
<%@ page import='java.util.Set' %>
<%@ page import='java.util.TreeSet' %>
<%@ page import='java.util.Map' %>
<%@ page import='java.util.TreeMap' %>
<%@ page import='java.util.Iterator' %>
<%@ page import='java.util.Date' %>

<%@ page import='com.freshdirect.framework.util.CSVUtils' %>
<%@ page import='com.freshdirect.framework.util.StringUtil' %>

<%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload' %>
<%@ page import='org.apache.commons.fileupload.FileItemFactory' %>
<%@ page import='org.apache.commons.fileupload.FileItem' %>
<%@ page import='org.apache.commons.fileupload.disk.DiskFileItemFactory' %>

<%!
	private static NumberFormat nf = NumberFormat.getInstance();

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

	private interface QueryInfo {
		public String getText();
		public long getFreq();
	};

	private static class Query implements Comparable, QueryInfo {
		public String text;
		public long freq;

		public Query(List row) {
			this.text = (String)row.get(0);
			this.freq = Long.parseLong((String)row.get(1));
		}

		public boolean equals(Object o) {
			if (!(o instanceof Query)) return false;
			Query oq = (Query)o;
			return freq == oq.freq && text.equals(oq.text);
		}

		public int compareTo(Object o) {
			Query oq = (Query)o;
			return 
				freq > oq.freq ? -1 :
				freq < oq.freq ? +1 : text.compareTo(oq.text);
		}

		public String getText() { return text; }
		public long getFreq() { return freq; }
	};

	private static class Sugg implements Comparable, QueryInfo {
		public Query query;
		public String suggestion;

		public long cats = 0;
		public long exts = 0;
		public long fuzz = 0;
		public long reci = 0;

		public long total = 0;

		public long S1_S2;
		public long S2_S1;
		public long S1S2;

		public Sugg(List row) {
			this.query = new Query(row);
			this.suggestion = (String)row.get(11);

			this.total += this.cats = Long.parseLong((String)row.get(2));
			this.total += this.exts = Long.parseLong((String)row.get(3));
			this.total += this.fuzz = Long.parseLong((String)row.get(4));
			this.total += this.reci = Long.parseLong((String)row.get(5));

			this.S1_S2 = Long.parseLong((String)row.get(8));
			this.S2_S1 = Long.parseLong((String)row.get(9));
			this.S1S2 = Long.parseLong((String)row.get(10));
		}

		public boolean equals(Object o) {
			if (!(o instanceof Sugg)) return false;
			Sugg os = (Sugg)o;
			return query.equals(os.query);
		}

		public int compareTo(Object o) {
			Sugg s2 = (Sugg)o;
			return query.compareTo(((Sugg)o).query);
		}

		public String getText() { return query.getText(); }
		public long getFreq() { return query.getFreq(); }
	};

	private static class SegmentStat {
		private Set repo = new TreeSet();

		public long total;
		public long totalTotal;

		public void add(Object o) { 
			if (repo.contains(o)) return;
			QueryInfo info = (QueryInfo)o;
			repo.add(info);
			++total;
			totalTotal += info.getFreq();
		}

		public int size() { return repo.size(); }
		public Iterator iterator() { return repo.iterator(); }
	};

	private static class Stats {
		public long total = 0;
		public long totalTotal = 0;
		public long hasSearchResults = 0;
		public SegmentStat betterSuggestions = new SegmentStat();
		public SegmentStat onlySuggestions = new SegmentStat();
		public SegmentStat noSuggestions = new SegmentStat();
		public SegmentStat goodResults = new SegmentStat();
		public Set seen = new TreeSet();


		public static double perc(long part, long whole) {
			long nice = (100000 * part)/whole;
			return ((double)nice)/1000.0;
		}

		public String percReport(SegmentStat stat) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<b>").append(perc(stat.total,total)).append("%</b> of cases (")
				.append(nf.format(stat.total)).append(" instances) and <b>")
				.append(perc(stat.totalTotal,totalTotal)).append("%</b> of actual searches (")
				.append(nf.format(stat.totalTotal)).append(" instances)");
			return buffer.toString();
		}
	};

	public Stats calculateStats(List results) {
		Stats stats = new Stats();
		for(Iterator i = results.iterator(); i.hasNext(); ) {
			List row = (List)i.next();
			if (stats.seen.contains(row.get(0))) continue;
			stats.seen.add(row.get(0));

			Sugg sug = new Sugg(row);
			if ( /* There are results */
				!"0".equals(row.get(2)) ||
				!"0".equals(row.get(3)) ||
				!"0".equals(row.get(4)) ||
				!"0".equals(row.get(5))) { 

				++stats.hasSearchResults;
				if ("true".equals(row.get(7))) stats.betterSuggestions.add(sug);
				else stats.goodResults.add(sug.query);
			} else { /* no results */

				if (!"-".equals(row.get(11))) {
					stats.onlySuggestions.add(sug);
				} else {
					stats.noSuggestions.add(sug.query);
				}
			}
			++stats.total;
			stats.totalTotal += sug.query.freq;
		}

		return stats;
	};

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

<%@ page import='java.util.Iterator' %>

<html>

<head>
<title>Spell test report</title>

<style>
body, td       { font-size: small; font-family: arial; color: #003333 }
td.even        { background: #ffeeee }
td.odd         { background: #eeeeff }
td.even-stat   { background: #eedddd }
td.odd-stat    { background: #ddddee }
td.head        { background: #000033; color: #aaaa00 }
div.high       { background: #ddddff }
</style>
</head>

<body>

<%

boolean wantStats = "true".equals(request.getParameter("stats"));

String[] files = request.getParameterValues("file");

if (files != null && files.length > 0) {

	for(int i=0; i< files.length; ++i) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(files[i]);
			List results = CSVUtils.parse(is,false,false);
			Stats stats = calculateStats(results);

			Info info = new Info(files[i]);

			OddEven oddEven = new OddEven();
%>
		<b>Run started <%=info.started%>, requested <%=nf.format(info.samples)%> samples </b>(actual CSV file: <tt><%=files[i]%></tt>)
		<br/><br/>

		<div class="high">
		<ul>
			<li><font size="+1"><b>Executive Summary</b></font><br/><br/>

			<li>There are <b><%= nf.format(stats.total) %></b> unique samples (representing <%= nf.format(stats.totalTotal)%> actual searches).<br/><br/>
			<li>There were <b><%= nf.format(stats.hasSearchResults) %></b> cases with some search results (thus
				<b><%= nf.format(stats.total - stats.hasSearchResults) %></b> times there were no results).<br/><br/>
			<li><a href="#sugs_<%=files[i]%>"><i>No search results, only suggestions</i></a>: <%= stats.percReport(stats.onlySuggestions) %>
			<li><a href="#dym_<%=files[i]%>"><i>Search resutls with "didyoumeans?"</i></a>: <%= stats.percReport(stats.betterSuggestions) %>
			<li><a href="#nos_<%=files[i]%>"><i>No search results, no suggestions</i></a>: <%= stats.percReport(stats.noSuggestions) %>
			<li><a href="#good_<%=files[i]%>"><i>Good search results</i></a>: <%= stats.percReport(stats.goodResults) %>
			<br/><br/>
		</ul>
		<ul>
		</div>
			

			<li><a name="sugs_<%=files[i]%>"/>These were the cases when there were no results but we could offer a suggestion.
				<font color="#990000"><%=stats.percReport(stats.onlySuggestions)%></font><br/><br/>
				<table>
				<tr>
					<td class="head"><b>Freq</b></td>
					<td class="head"><b>Query</b></td>
					<td class="head"><b>Suggestion</b></td>
<%					if (wantStats) { %>
					<td class="head"><b>% In class</b></td>
					<td class="head"><b>% Searches</b></td>
<%					} %>

				</tr>
				<tr>
					<td/><td/><td/>
				</tr>
<%
			oddEven.reset();
			for(Iterator it = stats.onlySuggestions.iterator(); it.hasNext();oddEven.flip()) {
				Sugg sug = (Sugg)it.next();
%>
				<tr>
					<td <%=oddEven%> align="right"><%= nf.format(sug.query.freq)%></td>
					<td <%=oddEven%> ><a href="/search.jsp?searchParams=<%=StringUtil.escapeUri(sug.query.text)%>"><%=sug.query.text%></td>
					<td <%=oddEven%> ><a href="/search.jsp?searchParams=<%=StringUtil.escapeUri(sug.suggestion)%>"><%=sug.suggestion%></td>
<%					if (wantStats) { %>
					<td <%=oddEven.statField()%> align="right" ><%= nf.format(Stats.perc(sug.query.freq,stats.onlySuggestions.totalTotal)) %></td>
					<td <%=oddEven.statField()%> align="right" ><%= nf.format(Stats.perc(sug.query.freq,stats.totalTotal)) %></td>
<%					} %>
				</tr>
<%
			}

%>
 				</table><br/><br/>

			<li><a name="dym_<%=files[i]%>"/>These were the cases when we believed that our suggestion could be better
				than the actual query.
				<font color="#990000"><%=stats.percReport(stats.betterSuggestions)%></font><br/><br/>
				<table>
				<tr>
					<td class="head"><b>Freq</b></td> 
					<td class="head"><b>Query</b></td>
					<td class="head"><b>Suggestion</b></td>
<%				if (wantStats) { %>
					<td class="head"><b>Hits Total</b></td>
					<td class="head"><b>Result diffs</b></td>
					<td class="head"><b>% In class</b></td>
					<td class="head"><b>% Searches</b></td>
<%				} %>
				</tr>

<%				if (wantStats) { %>
				<tr>
					<td class="head"/>
					<td class="head"/>
					<td class="head"/>
					<td class="head">(<i>Cat</i> + <i>Exact Prod</i> + <i>Fuzzy Prod</i> + <i>Reci</i>)</td>
					<td class="head">[ (<i>O</i>+<i>S</i>-(<i>O</i> and <i>S</i>))/(<i>O</i> + <i>S</i>) ]</td>
					<td class="head"/>
					<td class="head"/>
				</tr>
<%				} %>
				<tr>
					<td/><td/><td/>
<%				if (wantStats) { %>
					<td/><td/><td/></td>
<%				} %>
				</tr>
<%
			for(Iterator it = stats.betterSuggestions.iterator(); it.hasNext();oddEven.flip()) {
				Sugg sug = (Sugg)it.next();
				long STotal = sug.S1_S2 + sug.S2_S1 + sug.S1S2;
%>
				<tr>
					<td <%=oddEven%>  align="right"><%=nf.format(sug.query.freq)%></td>
					<td <%=oddEven%> ><a href="/search.jsp?searchParams=<%=StringUtil.escapeUri(sug.query.text)%>"><%=sug.query.text%></a></td>
					<td <%=oddEven%> ><a href="/search.jsp?searchParams=<%=StringUtil.escapeUri(sug.suggestion)%>"><%=sug.suggestion%></td>
<%				if (wantStats) { %>
					<td <%=oddEven.statField() %> ><%=sug.total%> = <%=sug.cats%> + <%=sug.exts%> + <%=sug.fuzz%> + <%=sug.reci%></td>
					<td <%=oddEven.statField() %> ><%=sug.S1_S2%>, <%=sug.S2_S1%>, <%=sug.S1S2%> [<%=(double)(sug.S1_S2 + sug.S2_S1)/(double)STotal%>]</td>
					<td <%=oddEven.statField() %> align="right"><%= nf.format(Stats.perc(sug.query.freq,stats.betterSuggestions.totalTotal)) %></td>
					<td <%=oddEven.statField() %> align="right"><%= nf.format(Stats.perc(sug.query.freq,stats.totalTotal)) %></td>
<%				} %>
				</tr>
<%
			}
%>
				</table><br/><br/>

			<li><a name="nos_<%=files[i]%>"/>These are the queries which did not return results and we could not suggest either.
				<font color="#990000"><%=stats.percReport(stats.noSuggestions)%></font><br/><br/>
			<table>
			<tr>
			<td class="head"><b>Freq</b></td>
			<td class="head"><b>Query</b></td>
<%			if (wantStats) { %>
			<td class="head"><b>% Searches</b></td>
<%			} %>
			</tr>
			<tr>
			<td/><td/>
<%			if (wantStats) { %>
			<td/>
<%			} %>
			</tr>
<%
			oddEven.reset();
			for(Iterator it=stats.noSuggestions.iterator(); it.hasNext(); oddEven.flip()) {
				Query query = (Query)it.next();
%>
			<tr>
				<td <%=oddEven%>  align="right"><%=nf.format(query.freq)%></td>
				<td <%=oddEven%> ><a href="/search.jsp?searchParams=<%=StringUtil.escapeUri(query.text)%>"><%=query.text%></td>
<%				if (wantStats) { %>
				<td <%=oddEven.statField()%> align="right" ><%= nf.format(Stats.perc(query.freq,stats.totalTotal)) %></td>
<%				} %>
			</tr>
<%
			}
%>
 			</table><br/><br/>

			<li><a name="good_<%=files[i]%>"/>Good search results:
				<font color="#990000"><%=stats.percReport(stats.goodResults)%></font><br/><br/>
		</ul>
<%
		} catch(IOException e) {
			continue;
		} finally {
			try {
				is.close();
			} catch(Throwable t) {}
		}
	}
} else {
%>
<form action="/test/search/report.jsp" method="post">
<%
	File f = new File("work" + File.separatorChar + "search");

	File[] subs = f.listFiles();

	Map sortedFiles = new TreeMap();

	for(int i=0; subs!= null && i< subs.length; ++i) {
		String name = subs[i].getName();
		if (name.startsWith("SpellerTest") && name.endsWith(".csv")) {
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
	<input type="checkbox" name="stats" value="true"/> <i>I want to see all statistics!</i><br/>
	<input type="submit"/>
</td>
</tr>
</table>
</form>

<%
}
%>

</body>
</html>
