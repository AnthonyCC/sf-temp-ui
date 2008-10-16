<%@		page import="com.freshdirect.fdstore.*"
%><%@	page import="com.freshdirect.fdstore.content.*"
%><%@	page import="com.freshdirect.fdstore.attributes.*"
%><%@	page import="com.freshdirect.fdstore.dcpd.*"
%><%@	page import="com.freshdirect.fdstore.FDResourceException"
%><%@	page import="com.freshdirect.fdstore.FDSkuNotFoundException"
%><%@   page import="com.freshdirect.cms.ContentKey"
%><%@	page import="com.freshdirect.fdstore.customer.adapter.OrderPromotionHelper"
%><%@   page import="java.io.*"
%><%@	page import="java.util.*"
%><%
boolean renderCSV = "csv".equals(request.getParameter("action"));
boolean skipUnavailableItems = "1".equals(request.getParameter("availablesonly"));
boolean prodsOnlyView = "1".equals(request.getParameter("prodsonly"));

String query = request.getQueryString();
String query2; // 'complementer' string to prodsonly parameter
if (prodsOnlyView) {
	query2 = query.replaceAll("\\&prodsonly=\\w*", "");
} else {
	query2 = query + "&prodsonly=1";
}


/// List contentKeys = new ArrayList();
List deptKeyz = Collections.EMPTY_LIST;
List catKeyz = Collections.EMPTY_LIST;
List recipeKeyz = Collections.EMPTY_LIST;
DCPDReportQuery q = null;



if (request.getParameter("keyz1") != null) {
	deptKeyz = Arrays.asList(request.getParameter("keyz1").trim().split(",\\s*"));
}
if (request.getParameter("keyz2") != null) {
	catKeyz = Arrays.asList(request.getParameter("keyz2").trim().split(",\\s*"));
}
if (request.getParameter("keyz3") != null) {
	recipeKeyz = Arrays.asList(request.getParameter("keyz3").trim().split(",\\s*"));
}

if (deptKeyz.size() > 0 || catKeyz.size() > 0 || recipeKeyz.size() > 0) {
	q = new DCPDReportQuery(deptKeyz, catKeyz, recipeKeyz);
	q.doQuery();
}

if (!renderCSV) {
    Iterator it;
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>DCPD Report</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="segabor">
	<!-- Date: 2007-12-06 -->
    <!--  link rel="stylesheet" type="text/css" media="all" href="dcpd.css" / -->
    <style media="all">
		body {
		    font-family: "Trebuchet MS";
		}

		h1 {
		    text-align: center;
		}

		.msg {
		    color: gray;
		    font-size: 12px;
		}

		.ibutton {
		    color: white;
		    background-color: black;
		    padding: 3px 30px;
		    font: 12px "Trebuchet MS";
		}

		.errmsg {
		    font-size: 12px;
		}

		.qTable th {
		   font: 12px "Trebuchet MS";
		   text-align: left;
		}

		.dcpd {
		   border-left: 1px solid #3c3;
		}

		table.dcpd {
		   width: 100%;
		}

		td.dcpd {
		   background-color: #090;
		   padding: 5px 5px;
		   margin-right: 5px;
		   width: none;
		   border-right: 2px solid #006600;
		   white-space: nowrap;
		
		   font: 11px "Trebuchet MS";
		   color: white;
		}

		td.dcpd a {
		   text-decoration: none;
		   color: white;
		   border: 0;
		}

		.dcpd th {
		   background-color: #090;
		   color: white;
		   text-align: left;
		   padding: 5px 7px;
		   /* font-size: 14px; */
		   font: 14px "Trebuchet MS";
		}

		.dcpd td {
		   border-bottom: 1px solid #ccc;
		   border-right: 1px solid #3c3;
		   padding: 7px 7px;
		   font: 12px "Trebuchet MS";
		}

		.qTable th {
		   font: 12px "Trebuchet MS";
		   text-align: left;
		}

		.dcpdpo {
		   border-left: 1px solid #3c3;
		}

		table.dcpdpo {
		   width: 100%;
		}

		td.dcpdpo {
		   background-color: #ff9900;
		   padding: 5px 5px;
		   margin-right: 5px;
		   border: 0;
		   width: none;
		   border-right: 2px solid #cc6600;
		   white-space: nowrap;
		
		   font: 11px "Trebuchet MS";
		   color: #a94300;
		}

		td.dcpdpo a {
		   text-decoration: none;
		   color: #a94300;
		   border: 0;
		}

		.dcpdpo th {
		   background-color: #ff9900;
		   color: #a94300;
		   text-align: left;
		   padding: 5px 7px;
		   /* font-size: 14px; */
		   font: 14px "Trebuchet MS";
		}

		.dcpdpo td {
		   border-bottom: 1px solid #ccc;
		   border-right: 1px solid #3c3;
		   padding: 7px 7px;
		   font: 12px "Trebuchet MS";
		}



		.deptcat {
		   color: #960;
		   font: 12px bold "Trebuchet MS";
		}

		#csv-anchor {
		  font: 12px bold "Trebuchet MS";
		  border: 1px solid #ff9900;
		  color: #ff9900;
		  background-color: yellow;
		  text-decoration: none;
		  padding: 5px 5px;
		  display: inline;
		  /* float: right; */
		}

		.err-span {
		  color: red;
		  font: 12px "Trebuchet MS";
		}

		#legend {
		  color: grey;
		  font: 10px "Trebuchet MS";
		}

		span.text12 {
		  font: 12px "Trebuchet MS";
		}
    </style>
    <style media="screen">
        #scrollable-viewport {
            height: 400px;
            overflow: auto;
        }
    </style>
</head>
<body>
<!-- input form -->
<h1>DCPD Products Report</h1>
<form action="dcpd.jsp" method="GET">
	<table class="qTable" border="0" cellspacing="4" cellpadding="0">
		<tr>
			<th>Find eligible products based on the following IDs&nbsp;</th>
			<th>Department ID</th>
            <th>Category ID</th>
            <th>Recipe ID</th>
			<th>&nbsp;</th>
		</tr>
		<tr>
			<td valign="top" align="right"><span class="msg">enter comma separated<br/>department or category id(s)<br/>no whitespace</span></td>
			<td><textarea name="keyz1" rows="8" cols="20"><% it=deptKeyz.iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? "," : "") %><% } %></textarea><br/><span class="msg">eg. mea</span></td>
            <td><textarea name="keyz2" rows="8" cols="20"><% it=catKeyz.iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? "," : "") %><% } %></textarea><br/><span class="msg">eg. fstk</span></td>
            <td><textarea name="keyz3" rows="8" cols="20"><% it=recipeKeyz.iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? "," : "") %><% } %></textarea><br/><span class="msg">eg. rec_ingrdnt</span></td>
			<td class="errmsg" valign="top"><% if (q != null && q.hasBadKeys()) {
			     %><span class="err-span">Error<br/><% if (q.getBadDepartmentKeys().size() > 0) { %>
			     Dept Id invalid: <% it=q.getBadDepartmentKeys().iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? ", " : "") %><% } %><br/><%
			     } // q.getBadDeptKeys().size() > 0
			     if (q.getBadCategoryKeys().size() > 0) { %>
                 Cat Id invalid: <% it=q.getBadCategoryKeys().iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? ", " : "") %><% } %><br/><%
                 } // q.getBadCatKeys().size() > 0
                 if (q.getBadRecipeKeys().size() > 0) { %>
                 Rec Id invalid: <% it=q.getBadRecipeKeys().iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? ", " : "") %><% } %><br/><%
                 } // q.getBadRcpKeys().size() > 0 %>
<%			} // q != null && q.hasBadKeys()
			%></span></td>
		</tr>
        <tr>
            <td valign="top" align="right"></td>
            <td colspan="3"><input type="checkbox" name="availablesonly" value="1" <%= (skipUnavailableItems ? "checked" : null ) %>></input> <span class="text12">Exclude unavailable products</span></td>
        </tr>
		<tr>
			<td valign="top" align="right"></td>
			<td colspan="3"><button class="ibutton" type="submit">FIND PRODUCTS</button></td>
		</tr>
	</table>
</form>
<!-- result -->
<% if (q != null) { 
    // create and initialize context object
	DCPDReportQueryContext ctx = new DCPDReportQueryContext(q);
	ctx.setSkipUnavailableItems(skipUnavailableItems);
	ctx.setRenderCSV(renderCSV);
	ctx.setProductsOnlyView(prodsOnlyView);
	
    DCPDReportGenerator report = new DCPDReportGenerator(out, ctx);

    if (q.hasBadKeys()) {%>
<b>Query failed due to one ore more bad content keys.</b>
<%  } else if (q.resultCount() == 0) {%>
<b>No content node found.</b>
<%	} else { %>
<!-- Info bar -->
<div style="position: relative">
    <span><span style="font: 16px bold 'Trebuchet MS'">Results: <span style="color: #090"><%= q.resultCount() %></span> node<%= (q.resultCount()>1?"s":"") %> and <span id="prd_cnt-id" style="color: #ff9900">...</span> products</span></span>
    <a id="csv-anchor" style="position: absolute; right: 0px" href="dcpd.jsp?action=csv&<%= request.getQueryString() %>">Export Results to Excel file</a>
</div><br/>
<!-- Results table -->
<table cellpadding="0" cellspacing="0" width="100%">
<tr>
    <td style="width: 4px">&nbsp;</td>
	<td class="dcpd">
	    <% if (prodsOnlyView) { %><a href="dcpd.jsp?<%= query2 %>">Full Hierarchy</a><% } else { %>Full Hierarchy<% } %>
	</td>
	<td style="width: 4px">&nbsp;</td>
	<td class="dcpdpo">
        <% if (prodsOnlyView) { %>Products Only<% } else { %><a href="dcpd.jsp?<%= query2 %>">Products Only</a><% } %>
	</td>
    <td style="width: 4px">&nbsp;</td>
    <td style="width: 100%; padding-left: 50px;">
	    <span id="legend"><i>Prefix:</i> D: Department | C: Category | S: Section | V: Variant<br/>
	    <i>Suffix:</i> (V): Virtual | (A): Alias | (v): Virtual Attribute | (a): Alias Attribute</span>
    </td>
</tr>
</table>
<div id="scrollable-viewport">
	<table class="<%= (prodsOnlyView ? "dcpdpo" : "dcpd") %>" cellpadding="0" cellspacing="0">
		<tr>
			<% if (!prodsOnlyView) { %><th style='text-align: center'>Product / Folder ID</th><% } %>
	        <th>Full Name</th>
			<th>SKU</th>
			<th>Rating</th>
			<th>Material</th>
			<th>Eligible</th>
            <th>Price</th>
            <th>BasePrice</th>
            <th>Deal</th>
		</tr>
<%
	report.setNodeSeparator("    <tr><td colspan=\"6\" style=\"padding: 0; height: 6px; width: 100%; background-color: #cccc99\"></td></tr>");
	report.generate(q.getNodes());
%>
    </table>
</div>
<script type="text/javascript">
    document.getElementById('prd_cnt-id').innerHTML = "<%= ctx.getProductsCount() %>";
</script>
<%	}
}
%>
</body>
</html><%
} else {
	/*
	 * Generate CSV output
	 */
	if (q != null && q.resultCount() > 0) {
	    // create and initialize context object
	    DCPDReportQueryContext ctx = new DCPDReportQueryContext(q);
	    ctx.setSkipUnavailableItems(skipUnavailableItems);
	    ctx.setRenderCSV(renderCSV);
	    ctx.setProductsOnlyView(prodsOnlyView);

	    DCPDReportGenerator report = new DCPDReportGenerator(out, ctx);
	    
	    // set response header
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-Disposition", "inline; filename=\"dcpd-promo.csv\"");

	    report.generate(q.getNodes());
	}
}
%>
