<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>


<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.framework.util.CSVUtils"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.TreeMap"%>

<%@page import="java.io.PrintWriter"%>

<html>

<%
if (ServletFileUpload.isMultipartContent(request)) {
	try {
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List /* FileItem */ items = upload.parseRequest(request);

		CohortSelector selector = CohortSelector.getInstance();

		Map tallies = new TreeMap();


		int t = 0;
		int m = 0;

		for(Iterator it = CohortSelector.getCohortNames().iterator(); it.hasNext(); ) {
			tallies.put(it.next().toString(), new HashMap());
		}

		String cc = null;
		String id = null;

		for(Iterator it = items.iterator(); it.hasNext(); ) {
			FileItem fileItem = (FileItem)it.next();
			if ("csv".equals(fileItem.getFieldName())) {
				for(Iterator i = CSVUtils.rowIterator(fileItem.getInputStream(),false,false); i.hasNext(); ) {

					List row = (List)i.next();
					if (row.size() > m) {
						m = row.size();	
					}
					for(int j=0; j< row.size(); ++j) {
						String name = selector.getCohortName(row.get(j).toString());	
						Map tally = (Map)tallies.get(name);
						if (tally == null) {
							tally = new HashMap();
							tallies.put(name,tally);
						}
						Integer ji = new Integer(j);
						Integer c = (Integer)tally.get(ji);
						tally.put(ji, c== null ? new Integer(1) : new Integer(c.intValue() + 1));
					}
					++t;
				}
			} else if ("id".equals(fileItem.getFieldName())) {
				id = fileItem.getString();
				cc = selector.getCohortName(id);
			}
		}

		double E = t/(double)tallies.size();

%>
		<%=t%> rows uploaded.<br/>
		<%=E%> Expected.<br/>
		<br/>

		<table>
<%
		double max[] = new double[m];
		double ave[] = new double[m];

		for(Iterator i = tallies.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry)i.next();
%>
			<tr>
				<td bgcolor="#cccccc"><b><%=e.getKey()%></b></td>
<%
			for(int j=0; j< m; ++j) {
				Integer ji = (Integer)((Map)e.getValue()).get(new Integer(j));
				int v = ji == null ? 0 : ji.intValue();
				double d = 100.*Math.abs(E-v)/E;
				if (d > max[j]) {
					max[j] = d;
				}
				ave[j] += d;
%>
				<td bgcolor="#dddddd"><%=v%> <%=d%>%</td>
<%
			}
%>
			</tr>
<%			
			
			
		}
%>
		<tr></tr>
		<tr>
			<td bgcolor="#ccddcc"><i>Max</i></td>
<%
			for(int i=0; i< max.length; ++i) {
%>
				<td bgcolor="#ddccdd"><%=max[i]%></td>
<%
			}	
%>
		</tr>
		<tr>
			<td bgcolor="#ccddcc"><i>Average</i></td>
<%
			for(int i=0; i< ave.length; ++i) {
%>
				<td bgcolor="#ddccdd"><%=ave[i]/tallies.size()%></td>
<%
			}
	
%>
		</tr>
		<table>


<%
		if (cc != null) {
%>
		<b>Cohort for "<tt><%=id%></tt>" is <b><%=cc%><b/>.

		<br/>
		<br/>
		<a href="<%=request.getRequestURI()%>"/>New test</a>
<%
		}
%>
<%
	} catch(Exception e) {
%>
Valami baj van;
<%
		e.printStackTrace(new PrintWriter(out));
	}
} else {
%>
When uploading a file, it must be in CSV format.
Each row is considered a user and each column an id. 
For example if you want to test the spread over ERP ids and FDUSER ids, then the two columns would be these ids. 
You can also figure out what the cohort is for the id of your choice.

<form name="cvsform" method="post" enctype="multipart/form-data">
	<table>
	<tr>
		<td>CSV file of comma separated ids</td><td><input type="file" name="csv"/></td>
	</tr>
	<tr>
		<td>An id I want to know: <input type="text" name="id"></td>
	</tr>
	<tr>
		<td><input type="submit"></td>
	</tr>
	</table>
</form>
<%
}
%>
</html>


