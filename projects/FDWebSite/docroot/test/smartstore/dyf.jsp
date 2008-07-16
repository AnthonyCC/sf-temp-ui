<%@ page import='java.io.File'
%><%@ page import='java.io.*'
%><%@ page import='java.text.NumberFormat'
%><%@ page import='java.util.List'
%><%@ page import='java.util.Set'
%><%@ page import='java.util.TreeSet'
%><%@ page import='java.util.Map'
%><%@ page import='java.util.TreeMap'
%><%@ page import='java.util.Iterator'
%><%@ page import='java.util.Date'
%><%@ page import='com.freshdirect.framework.util.CSVUtils'
%><%@ page import='com.freshdirect.framework.util.StringUtil'
%><%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload'
%><%@ page import='org.apache.commons.fileupload.FileItemFactory'
%><%@ page import='org.apache.commons.fileupload.FileItem'
%><%@ page import='org.apache.commons.fileupload.disk.DiskFileItemFactory'
%><%@ page import="weblogic.xml.util.StringInputStream"
%><%@ page import="weblogic.utils.FileUtils"
%><%

//
// SmartStore Recommendations= - DYF Test Page
//
// @author segabor
//

String action = request.getParameter("action");
String errorMsg = null;
Exception trouble = null;

if ("upload".equalsIgnoreCase(action)) {
	//
	// CVS File upload
	//
	try {
		if (ServletFileUpload.isMultipartContent(request)) {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
	
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
	
			// Parse the request
			List /* FileItem */ items = upload.parseRequest(request);

			if (items.size() > 0) {
				// make sure smartstore folder exists in work.
				//
				File SSdir = new File("work" + File.separatorChar + "smartstore");
				/// if (!SSdir.exists()) {
					SSdir.mkdirs();
				/// }

				File inFile = new File(SSdir, "cust_sku_input.csv");
				
				// remove old file
				if (inFile.exists()) {
					inFile.delete();
				}
				
				/* if (!inFile.canWrite()) {
					throw new Exception("File cannot be written: " + inFile.getPath());
				} */

				// store file in local space
				//
				FileWriter fw = new FileWriter(inFile);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader( ((FileItem) items.get(0)).getInputStream() ));
				String line;
				while ( (line = reader.readLine()) != null) {
					fw.write(line + "\n");
				}
				fw.flush();
				fw.close();
				reader.close();
				
				// we have a file!
				response.sendRedirect("dyf_test.jsp");
			} else {
				errorMsg = "Invalid file!";
			}
		}
	} catch (Exception exc) {
		trouble = exc;
	}
}


%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>SmartStore DYF Test Page</title>
	<style>
		body {
			margin: 0;
			padding: 0;
		}
	</style>
</head>
<body>
<% if (errorMsg != null ) { %>
	<div style="padding: 3px 3px; background-color: red; color: white; font-weight: bold" id="debug">ERROR: <%= errorMsg %></div>
<% }
   if (trouble != null) {
%>
	<span style="width: 40px; height: 40px; background-color: red; text-align: center; vertical-align: middle; color: white;" onclick="var x = document.getElementById('exception'); x.style.display = (x.style.display=='none'?'':'none');"><b>+</b></span>
	<span style="color: red; font-weight: bold">EXCEPTION: <%= trouble.getMessage() %></span><br/>
	<div style="font-size: 10px; border: 1px solid red; padding: 5px 5px; display: none;" id="exception">
<%
		trouble.printStackTrace(new PrintWriter(out));
%>
	</div>
<%
   }
%>
	<!--  phase 0: upload form -->
	<div style="width: 350px; padding: 15px 15px; margin: 25px 25px; border: 3px dotted #ddd; background-color: #e0ecff">
		<form name="cvsform" action="/test/smartstore/dyf.jsp?action=upload" method="post" enctype="multipart/form-data">
			<span style="font-size: 16px; font-weight: bold; font-family: MS Trebuchet; padding-right: 20px;">CSV File</span><input type="file" name="upfile"/><br/>
			<input type="submit" value="Upload" style="margin-top: 20px"/>
		</form>
	</div>
	<span style="position: absolute; left: 2px; bottom: 2px" id="debug"><%= action %></span>
</body>
</html>
