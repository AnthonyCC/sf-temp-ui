<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"
%><%@page import="org.apache.commons.fileupload.FileItemFactory"
%><%@page import="org.apache.commons.fileupload.FileItem"
%><%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"
%><%@page import="java.util.Collections"
%><%@page import="java.util.Iterator"
%><%@page import="java.util.Set"
%><%@page import="java.util.Map"
%><%@page import="java.util.Arrays"
%><%@page import="java.util.List"
%><%@page import="java.util.ArrayList"
%><%@page import="java.io.PrintWriter"
%><%@page import="java.text.DecimalFormat"
%><%@page import="java.text.NumberFormat"
%><%@page import="com.freshdirect.smartstore.fdstore.ScoreProvider"
%><%@page import="com.freshdirect.smartstore.fdstore.ScoresTable"
%><%@page import="com.freshdirect.framework.util.CSVUtils"%><%!

private String makeLinkString(Set loadedFactors, String factor, boolean glob) {
	StringBuffer buffer = new StringBuffer();
	buffer.
		append("<li><input type=\\\"checkbox\\\" name=\\\"LF\\\" value=\\\"").
		append(factor).
		append("\\\" ");
	if (ScoreProvider.getInstance().isStoreLookup(factor)) {
		buffer.append("disabled=\\\"true\\\" checked=\\\"true\\\"");
	} else if (loadedFactors.contains(factor)) {
		buffer.append("checked=\\\"true\\\"");
	}
	buffer.append('>');
	if (ScoreProvider.getInstance().isGlobal(factor)) {
		buffer.append("<i>");
	} 
	buffer.append("<tt><span style=\\\"color: ");
	if (loadedFactors.contains(factor)) {
		if (ScoreProvider.getInstance().isGlobal(factor)) {
			buffer.append("red");
		} else if (glob) {
			buffer.append("#aaaaaa");
		} else {
			buffer.append("blue");
		}
	} else {
		buffer.append("#999999");
	}
	buffer.
		append("\\\" onclick=\\\"append(\'${").
		append(factor).
		append("}\')\\\">").
		append(factor).
		append("</tt>");
	buffer.append("</span></li>");
	return buffer.toString();
}

%><%
if (ServletFileUpload.isMultipartContent(request)) {

	NumberFormat nf = new DecimalFormat("#,####,###.######");

	try {
		ScoresTable T = null;
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List /* FileItem */ items = upload.parseRequest(request);

		boolean html = true;

		String formula = null;


		for(Iterator it = items.iterator(); it.hasNext(); ) {
			FileItem fileItem = (FileItem)it.next();
			if ("customer".equals(fileItem.getFieldName())) {
				T = ScoreProvider.getInstance().getAllScores(Collections.singletonList(fileItem.getString()));	
			} else if ("customers".equals(fileItem.getFieldName())) {
				List customers = new ArrayList();
				for(Iterator i = CSVUtils.rowIterator(fileItem.getInputStream(),false,false); i.hasNext();) {
					List row = (List)i.next();
					for(Iterator r= row.iterator(); r.hasNext();) {
						customers.add(r.next().toString());
					}
				}
				T = ScoreProvider.getInstance().getAllScores(customers);
			} else if ("cust".equals(fileItem.getFieldName())) {
				if ("cached".equals(fileItem.getString())) {
					T = ScoreProvider.getInstance().getAllScores(new ArrayList(ScoreProvider.getInstance().getCachedCustomers()));
				}
			} else if ("format".equals(fileItem.getFieldName())) {
				html = "html".equals(fileItem.getString());
			} else if ("formula".equals(fileItem.getFieldName())) {
				if (!"no".equals(fileItem.getString())) {
					formula = fileItem.getString();
				}
			}
		}

		if (T == null) {
			T = ScoreProvider.getInstance().getAllScores(null);
		}

	
		if (html) {	
%>
<html>
<head>
<title>Factors</title>
<style>
body {
	font-family: sans-serif;
}


li span {
	cursor: pointer;
}
</style>
</head>
<body>
<table>
<tr>
<%
			for(Iterator i = T.getColumnNames().iterator(); i.hasNext(); ) {
				String name = i.next().toString();
%>
				<td bgcolor="#aaffaa"><b><%=name%></b></td>							
				
<%

			}
%>
</tr>
<%

			for(Iterator i = T.getRows(); i.hasNext(); ) {
				List row = (List)i.next();
%>
<tr>
<%
				for(Iterator j = row.iterator(); j.hasNext(); ) {
					Object obj = j.next();
					if (obj instanceof Number) {
%>
						<td bgcolor="#ffffee"><%=nf.format(((Number)obj).doubleValue())%></td>
<%
					} else {
%>
						<td bgcolor="#aaffaa"><%=obj.toString()%></td>
<%
					}
				}
%>
</tr>
<%
			}
%>
</table>
</body>
</html>
<%
		} else { // EXCEL

			List formulas = Collections.EMPTY_LIST;
			if (formula != null) {
				System.err.println(T.compileFormula(formula,0));
				formulas = Collections.singletonList(formula);
			}

			response.setHeader("Content-type","application/xls");
			response.setHeader("Content-disposition","inline; filename=factors.xls");
			T.writeExcel(response.getOutputStream(),formulas);
		}

	} catch (Exception e) {
%>
<b>Valami baj van!</b>
<br/>
<%
		e.printStackTrace(new PrintWriter(out));
	}

} else {
%>

<html>
<head>
<style>
body {
	font-family: sans-serif;
}
</style>
<script>
<%
	if (request.getParameter("reload") != null) {
		String[] values = request.getParameterValues("LF");
		if (values != null) {
			Set factorNames = new HashSet(Arrays.asList(values));
			ScoreProvider.getInstance().acquireFactors(factorNames);
		}
	}
%>
<%
	Set factors = ScoreProvider.getInstance().getLoadedFactors();
	Set availableFactors = ScoreProvider.getInstance().getAvailableFactors();

	List sortedFactorNames = new ArrayList(availableFactors);
	Collections.sort(sortedFactorNames);
%>


var GLOBAL_FACTORS = "<ol><% 
	for(Iterator i = sortedFactorNames.iterator(); i.hasNext(); ) {
		String factor = i.next().toString();
		%><%=makeLinkString(factors,factor,true)%><%
	}
%></ol>";

var ALL_FACTORS = "<ol><%
	for(Iterator i = sortedFactorNames.iterator(); i.hasNext(); ) {
		String factor = i.next().toString();
		%><%=makeLinkString(factors,factor,false)%><%
	}
%></ol>";

function append(v) {
	var e = document.getElementById('formula_element');
	if (e) {
		e.value = e.value + v;
	} 
}

function toggle() {
	var inps = document.getElementsByName("cust");
	var avalon = document.getElementById("avalon");
	var ch = "";
	for(i=0; i< inps.length; ++i) {
		if (inps[i].checked) {
			ch = inps[i].value;
		}
	}

	var inp = document.getElementById("input");

	if (ch == "one") {
		inp.innerHTML = 'Customer: <input type="text" name="customer" value="62423"/>';	
		avalon.innerHTML = ALL_FACTORS;
	} else if (ch == "some") {
		inp.innerHTML = 'Customers: <input type="file" name="customers"/>';
		avalon.innerHTML = ALL_FACTORS;
	} else if (ch == "cached") {
		inp.innerHTML = '';
		avalon.innerHTML = ALL_FACTORS;
	} else {
		inp.innerHTML = '';
		avalon.innerHTML = GLOBAL_FACTORS;
	} 
}

function togglef(checkbox) {

        var inp = document.getElementById("formula_span");
	if (!checkbox.checked) {
		inp.innerHTML = '<input type="hidden" name="formula" value="no"/>';	
	} else {
		var ch = "";
		var inps = document.getElementsByName("format");
		for(i=0; i< inps.length; ++i) {
			if (inps[i].checked) {
				ch = inps[i].value;
			}
		}
		if (ch == "excel") {
			inp.innerHTML = '<input id="formula_element" type="text" name="formula" size="40"/>'
		} else {
			chekbox.checked = false;
		}
	}
}

function enable() {
	var inps = document.getElementsByName("format");
	var ch = "";
	for(i=0; i< inps.length; ++i) {
		if (inps[i].checked) {
			ch = inps[i].value;
		}
	}

	var checkbox = document.getElementById("formula_switch");

	if (ch == "html" && checkbox.checked) {
		checkbox.checked = false;
		togglef(checkbox);
	} 
	if (ch == "html") {
		checkbox.disabled = true;
	} else {	
		checkbox.disabled = false;
	}
}

function toggle_help() {
	if (document.getElementById('help_button').checked) {
		document.getElementById('help').style.display = "";
	} else {
		document.getElementById('help').style.display = "none";
	}
}


</script>
<title>Factors</title>
</head>
<body onload="document.getElementById('avalon').innerHTML = GLOBAL_FACTORS">
<table>
<tr>
<td bgcolor="#eeffdd" valign="top">
<b>Available factors</b>
<form name="reload">
<input type="hidden" name="reload" value="do"/>
<input type="submit" value="load only selected factors"/>
<span id="avalon"></span>
</form>
</td>
<td bgcolor="#eeffdd" valign="top">
<form name="customers_form" method="post" enctype="multipart/form-data">
	<table>
	<tr>
		<td bgcolor="#ddeeff" width="500">
			<input type="radio" name="cust" value="one" onclick="toggle()"/> One customer<br/>
			<input type="radio" name="cust" value="none" onclick="toggle()" checked/> No customer<br/>
			<input type="radio" name="cust" value="some" onclick="toggle()"/> Several customers</br>
			<input type="radio" name="cust" value="cached" onclick="toggle()"/> All cached customers
		</td>
	</tr>
	<tr>
		<td bgcolor="#ddeeff"><span id="input"> </span></td>
	</tr>
	<tr>
		<td bgcolor="#ddeeff">
			<input type="checkbox" id="formula_switch" onclick="togglef(this)"> Formula 
				<span id="formula_span"><input type="hidden" name="formula" value="no"></span><br/>
		</td>
	</tr>
	<tr>
		<td bgcolor="#ddeeff">
			<input type="radio" name="format" value="html" onclick="enable()" /> <tt>HTML</tt><br/>
			<input type="radio" name="format" value="excel" checked="" onclick="enable()" /> <tt>EXCEL</tt><br/><br/>
			<input type="submit"/>
		</td>
	</tr>
	</table>
</form>
</td>
</tr>
<tr>
<td colspan="2" style="background-color:#660000; color: #99ffff" width="800">
<input id="help_button" type="checkbox" onclick="toggle_help()"/><b>Help!</b>
<div id="help" style="display: none">
<ul>
	<li><b>What is this?</b>
		<ul>
		This page lets you investigate and evaluate factors in both global and user specific contexts.
		</ul>
		<br/>
	</li>
	<li><b>Available factors</b>
		<ul>
		The factors displayed are available for the customer selection (see below). Global factors are 
		shown in red, and personalized factors in blue. If there are users in the context, personalized
		factors <i>and</i> global factors are both available.
		</ul>
		<br/>
	</li>
	<li><b>Customer selection</b>
		<ul>
		<i>No customer</i> means that only global factors are available. <i>Several customers</i> can be 
		uploaded in a text file where each line contains one ERP customer id. 
		</ul>
		<br/>
	</li>
	<li><b>Output</b>
		<ul>
		When choosing <tt>EXCEL</tt>, a <i>MS EXCEL 97</i> format spreadsheet is prepared. If customers are
		supplied, the first column is the <tt>customer id</tt>, the second is the <tt>product id</tt> and 
		the rest of the columns are the factor values. In case a formula is provided, its value will be in 
		the last column (see below). The <tt>HTML</tt> output is analogous but a formula cannot be supplied.
		</ul>
		<br/>
	</li>
	<li><b>Formula</b>
		<ul>
		EXCEL equation without the starting <tt>=</tt> sign. Factors can be refererred to by their names prepended
		with the dollar sign and enclosed in curly braces (i.e. <tt>${<i>factor</i>}</tt>).  For example:
<pre>
    0.4*${Frequency_Normalized} + 0.6*${AmountSpent_Normalized}
</pre>
		is a valid formula. As a convenience, clicking on the factor's name will append the factor to the formula
		input field. <i>The formula is not checked for validity! It it is incorrectly supplied, the values in 
		the last column will show errors!</i>
		</ul>
	</li>
	
</ul>
</div>
</td>
</tr>
</table>
</body>
</html>
<%
}
%>
