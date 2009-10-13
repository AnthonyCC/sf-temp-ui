<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@
page import="java.util.ArrayList"%><%@
page import="java.text.NumberFormat"%><%@
page import="java.util.HashSet"%><%@
page import="java.util.Iterator"%><%@
page import="java.util.List"%><%@
page import="java.util.Map"%><%@
page import="java.util.Set"%><%@
page import="java.util.StringTokenizer"%><%@
page import="com.freshdirect.fdstore.content.ContentFactory"%><%@
page import="com.freshdirect.fdstore.content.ContentNodeModel"%><%@
page import="com.freshdirect.framework.util.CSVUtils"%><%@
page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%><%@
page import="org.apache.commons.fileupload.FileItemFactory"%><%@
page import="org.apache.commons.fileupload.FileItem"%><%@
page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%><%@
taglib uri="freshdirect" prefix="fd"%><%!
static class Escaper {
	static String escape(String input) {
		if (input == null)
			return null;
		
		return CSVUtils.escape(input);
	}
}
%><%

boolean defaultFromFile = false;
boolean fromFile = defaultFromFile;
boolean defaultToCSV = false;
boolean toCSV = defaultToCSV;
String idsStr = "";
List ids = new ArrayList();
String defaultNoOfCycles = "5";

if (ServletFileUpload.isMultipartContent(request)) {
	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	List items = upload.parseRequest(request);
	for (int i = 0; i < items.size(); i++) {
		FileItem item = (FileItem) items.get(i);
		if ("source".equals(item.getFieldName())) {
			String source = item.getString();
			if (source != null) {
				if (source.equals("text"))
					fromFile = false;
				else if (source.equals("file"))
					fromFile = true;
			}
			//System.err.println("fromFile: " + fromFile);
		}
	}

	Iterator it = items.iterator();
	while (it.hasNext()) {
		FileItem item = (FileItem) it.next();
		if (!fromFile) {
			if ("ids".equals(item.getFieldName())) {
				idsStr = item.getString();
				if (idsStr != null && idsStr.trim().length() != 0) {
					StringTokenizer st = new StringTokenizer(idsStr, ", \t\r\n");
					while (st.hasMoreTokens()) {
						ids.add(st.nextToken());
					}
				}
			}
		} else {
			if ("fileSource".equals(item.getFieldName())) {
				try {
					Iterator rowIt = CSVUtils.rowIterator(item.getInputStream(), false, false);
					while (rowIt.hasNext()) {
						List row = (List) rowIt.next();
						if (row.size() < 1)
							continue;

						ids.add(((String) row.get(0)).trim());
					}
				} catch (Exception e) {
					
				}
			}
		}
		if ("toCSV".equals(item.getFieldName())) {
			if ("1".equals(item.getString()))
				toCSV = true;
		}
	} 
}

%><%
if (toCSV) {
	response.setContentType("text/plain");
	response.addHeader("Content-Disposition", "attachment; filename=full_names.csv");
	Iterator it = ids.iterator();
	while (it.hasNext()) {
		String id = (String) it.next();
		ContentNodeModel node = ContentFactory.getInstance().getContentNode(id);
		String fullName = null;
		ContentNodeModel parent = null;

		if (node != null) {
			fullName = Escaper.escape(node.getFullName());
			parent = node.getParentNode();
		}
			
		if (fullName == null)
			fullName = "<unknown>";
			
		String parentId = null;
		String parentFullName = null;
		
		if (parent != null) {
			parentId = Escaper.escape(parent.getContentKey().getId());
			parentFullName = Escaper.escape(parent.getFullName());
		}
		
		if (parentId == null)
			parentId = "<unknown>";

		if (parentFullName == null)
			parentFullName = "<unknown>";
			
		response.getWriter().println(id + "," + fullName +
				"," + parentId + "," + parentFullName);
		
		response.flushBuffer();
	}
%><%
} else {
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>YMAL PERFORMANCE TEST PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page table{border-collapse:collapse;border-spacing:0px;width:100%;}
.test-page .rec-chooser{margin:0px 0px 6px;text-align:right;}
.test-page .rec-options{border:1px solid black;font-weight:bold;}
.test-page .rec-options .view-label{text-transform:capitalize;}
.test-page .rec-options table td{vertical-align: top; padding: 5px 10px 10px;}
.test-page .rec-options table td p.label{padding-bottom:4px;}
.test-page .rec-options table td p.result{padding-top:4px;}
.test-page .rec-options table div{padding-right:20px;}
table.rec-inner td {padding: 0px 2px !important; vertical-align: top !important;}
p.head{padding:10px 0px 20px;}
.test-page table.t1{width:auto;margin:20px 0px;}
.test-page table.t1 td{border:1px solid black;padding:4px 8px;}
p.red { color: #990000; }
p.red a { color: #990000; font-weight: bold; text-decoration: underline; }
p.green { color: #009900; }

	</style>
</head>
<body class="test-page">
	<form method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    <div class="rec-options" class="rec-chooser title14">
    	<table style="width: auto;">
    		<tr>
				<td>
    				<p class="label">
    					Input of IDs:
    				</p>
					<table class="rec-inner">
						<tr>
							<td>
								<p class="label">
									<input type="radio" name="source"
											value="text"<%= !fromFile ? " checked=\"checked\"" : "" %>>
								</p>
							</td>
							<td style="vertical-align: top; padding: 2px;">
			    				<p class="label" title="customer; product&lt;new line&gt; ...">
			    					From text (<span style="font-weight: normal;">id&lt;new line&gt; ...</span>):
			    				</p>
								<p>
									<textarea name="ids" id="ids" rows="4" 
										cols="30"><%= idsStr %></textarea>
								</p>
							</td>
						</tr>
						<tr>
							<td>
			    				<p class="label result">
									<input type="radio" name="source"
											value="file"<%= fromFile ? " checked=\"checked\"" : "" %>>
								</p>
							</td>
							<td>
			    				<p class="label result">
			    					From file:
			    				</p>
								<p>
									<input type="file" name="fileSource">
								</p>
							</td>
						</tr>
					</table>
				</td>
    			<td class="text12">
    				<p class="label">
    					<span>Output format</span>
    				</p>
    				<p>
    					<input type="checkbox" name="toCSV"
    							value="1"<%= toCSV ? " checked=\"checked\"" : "" %>>Export to CSV file
    				</p>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="2">
    				<p>
    					<input type="submit" value="Submit">
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
    <table class="t1">
		<%
			Iterator it = ids.iterator();
			while (it.hasNext()) {
				String id = (String) it.next();
				ContentNodeModel node = ContentFactory.getInstance().getContentNode(id);
				String fullName = null;
				ContentNodeModel parent = null;

				if (node != null) {
					fullName = node.getFullName();
					parent = node.getParentNode();
				}
					
				if (fullName == null)
					fullName = "&lt;unknown&gt;";
					
				String parentId = null;
				String parentFullName = null;
				
				if (parent != null) {
					parentId = parent.getContentKey().getId();
					parentFullName = parent.getFullName();
				}
				
				if (parentId == null)
					parentId = "&lt;unknown&gt;";

				if (parentFullName == null)
					parentFullName = "&lt;unknown&gt;";
		%>
    	<tr>
	    	<td class="text13" title="Id"><%= id %></td>
			<td class="text13" title="Full Name"><%= fullName %></td>
	    	<td class="text13" title="Parent Id"><%= parentId %></td>
			<td class="text13" title="Parent's Full Name"><%= parentFullName %></td>
		</tr>
		<%
			}
		%>
	</table>
	</form>
</body>
</html><% }
%>