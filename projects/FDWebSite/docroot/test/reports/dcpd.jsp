<%@		page import="com.freshdirect.fdstore.*"
%><%@	page import="com.freshdirect.fdstore.content.*"
%><%@	page import="com.freshdirect.fdstore.attributes.*"
%><%@	page import="com.freshdirect.fdstore.dcpd.*"
%><%@	page import="com.freshdirect.fdstore.FDResourceException"
%><%@	page import="com.freshdirect.fdstore.FDSkuNotFoundException"
%><%@	page import="java.util.*"
%><%!

void renderDepartmentNode(DepartmentModel deptNode, int level, JspWriter out) throws IOException, FDResourceException, FDSkuNotFoundException {
	// I. RENDER DEPARTMENT
	out.println("<tr>");
	out.println("<td><i><b>D:</b>" + deptNode.getContentName() + "</i></td>");
	out.println("<td>&nbsp;</td>");
	out.println("<td>&nbsp;</td>");
	out.println("</tr>");
	
	++level;

	// II. ITERATE CATEGORIES
	Iterator cit = deptNode.getCategories().iterator();
	while(cit.hasNext()) {
		CategoryModel catNode = (CategoryModel) cit.next();
		renderCategoryNode(catNode, level, out);
	}
}

%><%!

void renderCategoryNode(CategoryModel catNode, int level, JspWriter out) throws IOException, FDResourceException, FDSkuNotFoundException {
	// II. ITERATE SUBCATEGORIES
	// I. RENDER DEPARTMENT
	out.println("<tr>");
	out.println("<td style='padding-left: " + (level*15) + "px'><i><b>C:</b>" + catNode.getContentName() + "</i></td>");
	out.println("<td>&nbsp;</td>");
	out.println("<td>&nbsp;</td>");
	out.println("</tr>");

	// I. RENDER CATEGORY
	++level;
	
	// I/a. RENDER PRODUCTS
	Iterator pit = catNode.getProducts().iterator();
	while(pit.hasNext()) {
		ProductModel prodNode = (ProductModel) pit.next();
		Iterator sit = prodNode.getSkus().iterator();
		while(sit.hasNext()) {
			SkuModel skuNode = (SkuModel) sit.next();
			out.println("<tr>");
			// out.println("<td style='padding-left: " + (level*10) + "px'>" + catNode.getContentName() + "</td>");
			out.println("<td style='padding-left: " + (level*15) + "px'>" + prodNode.getContentName() + "</td>");
			// out.println("<td>&nbsp;</td>");
			// out.println("<td align='center'>" + prodNode.getContentName() + "</td>");
			// // out.println("<td>&nbsp;</td>");
			out.println("<td align='center'>" + skuNode.getContentName() + "</td>");
			try {
				if (skuNode.getProduct() != null) {
					String m = skuNode.getProduct().getMaterial().getMaterialNumber();
					out.println("<td align='center'>" + (m!=null ? m : "N/A") + "</td>");
				} else {
					out.println("<td align='center'>N/A</td>");
				}
			} catch (FDSkuNotFoundException e) {
				out.println("<td align='center'>N/A</td>");
			}
			out.println("</tr>");
		}
	}
	
	Iterator cit = catNode.getSubcategories().iterator();
	while(cit.hasNext()) {
		CategoryModel subCatNode = (CategoryModel) cit.next();
		renderCategoryNode(subCatNode, level, out);
	}
}
%><%
List contentKeys = Collections.EMPTY_LIST;
DCPDQuery q = null;
Iterator it;







if (request.getParameter("keyz") != null) {
	contentKeys = Arrays.asList(request.getParameter("keyz").trim().split(",\\s*"));
	q = new DCPDQuery(contentKeys);
	
	// put it into an XMLHttpRequest
	q.doQuery();
}
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>DCPD Report</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="segabor">
	<!-- Date: 2007-12-06 -->
	<style>
	.msg {
		color: gray;
		font-size: 12px;
	}
	
	.ibutton {
		color: white;
		background-color: black;
		font-size: 18px;
		padding: 5px 5px;
	}

	.errmsg {
		font-size: 12px;
	}
	</style>
</head>
<body>
<!-- input form -->
<form action="dcpd.jsp" method="GET">
	<table border="0">
		<tr>
			<th>LOOKUP</th>
			<th>Content ID</th>
			<th>&nbsp;</th>
		</tr>
		<tr>
			<td valign="top" align="right"><span class="msg">enter comma separated<br/>department or category id(s)<br/>no whitespace</span></td>
			<td><textarea name="keyz" rows="8" cols="20"><% it=contentKeys.iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? "," : "") %><% } %></textarea><br/><span class="msg">eg. mea</span></td>
			<td class="errmsg" valign="top"><% if (q != null && q.getBadKeys().size() > 0) { %><span style="color: red; text-decoration: underline;">The following key<%= q.getBadKeys().size()>1 ? "s are" : " is"  %> invalid:</span><br/>
			<% it=q.getBadKeys().iterator(); while(it.hasNext()) { %><%= it.next()+(it.hasNext() ? ", " : "") %><% } %><% } // badKeys.size
			%></td>
		</tr>
		<tr>
			<td valign="top" align="right"></td>
			<td><button class="ibutton" type="submit">SUBMIT</button></td>
			<td>&nbsp;</td>
		</tr>
	</table>
</form>
<!-- result -->
<% if (q != null) { 
	if (q.resultCount() == 0) {%>
	Not found...
<%	} else { %>
<h2>Found <%= q.resultCount() %> nodes</h2>
<div id="result-div" style="height: 400px; overflow: auto">
<table boder="0">
	<tr>
		<th>Product</th>
		<th>SKU</th>
		<th>Material</th>
	</tr>
<% it=q.getNodes().iterator(); while(it.hasNext()) {
	// Use Breadth-first search algorithm
	// INIT
	ContentNodeModel rootNode = (ContentNodeModel) it.next();
	
	if (rootNode instanceof DepartmentModel) {
		renderDepartmentNode( (DepartmentModel) rootNode, 0, out);
	} else if (rootNode instanceof CategoryModel) {
		renderCategoryNode( (CategoryModel) rootNode, 0, out);
	} else if (rootNode instanceof Recipe) {
		// TODO
		System.out.println("Nothing to do with recipe " + rootNode);
	} else {
		// TODO
		System.out.println("Nothing to do with " + rootNode.getClass() + "/" + rootNode);
	}

} //it %>
</table>
</div>
<%	}
}
%>
</body>
</html>
