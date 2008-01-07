<%@		page import="com.freshdirect.fdstore.*"
%><%@	page import="com.freshdirect.fdstore.content.*"
%><%@	page import="com.freshdirect.fdstore.attributes.*"
%><%@	page import="com.freshdirect.fdstore.dcpd.*"
%><%@	page import="com.freshdirect.fdstore.FDResourceException"
%><%@	page import="com.freshdirect.fdstore.FDSkuNotFoundException"
%><%@   page import="com.freshdirect.cms.ContentKey"
%><%@   page import="java.io.*"
%><%@	page import="java.util.*"
%><%!

void renderDepartmentNode(DepartmentModel deptNode, int level, JspWriter out) throws IOException, FDResourceException, FDSkuNotFoundException {
	// I. RENDER DEPARTMENT
	out.println("<tr>");
	out.println("<td style='color: #960; font-weight: bold;'>D: " + deptNode.getContentName() + "</td>");
    out.println("<td>" + deptNode.getFullName() + "</td>");
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
	ContentKey alias = catNode.getAliasAttributeValue();
	
	out.println("<tr>");
	out.println("<td style='color: #960; font-weight: bold; padding-left: " + (level*15) + "px'>C: " + catNode.getContentName() + "</td>");
    out.println("<td>" + catNode.getFullName() + "</td>");
	out.println("<td>&nbsp;</td>");
	out.println("<td>&nbsp;</td>");
	out.println("</tr>");

	// I. RENDER CATEGORY
	++level;
	
    // RENDER ALIAS GROUP
    if (alias != null) {
        com.freshdirect.cms.ContentNodeI ct = alias.getContentNode();
        out.println("<tr>");
        out.println("<td style='padding-left: " + (level*15) + "px'>C: " + alias.getId() + " (A)</td>");
        out.println("<td>" + ct.getLabel() + "</td>");
        out.println("<td>&nbsp;</td>");
        out.println("<td>&nbsp;</td>");
        out.println("</tr>");
    }

	// I/a. RENDER PRODUCTS
	Iterator pit = catNode.getPrivateProducts().iterator();
	while(pit.hasNext()) {
		ProductModel prodNode = (ProductModel) pit.next();
		renderSKUs(prodNode.getSkus(), prodNode.getContentName(), level, out);
	}
	

	// RENDER VIRTUAL GROUP ITEMS
    List virtualGroup = catNode.getVirtualGroupRefs();
    if (virtualGroup != null) {
    	Iterator vit = virtualGroup.iterator();
    	while(vit.hasNext()) {
    		CategoryRef cref = (CategoryRef) vit.next();
    		CategoryModel vcatNode = cref.getCategory();

    	    out.println("<tr>");
    	    out.println("<td style='padding-left: " + (level*15) + "px'>C: " + vcatNode.getContentName() + " (V)</td>");
    	    out.println("<td>" + vcatNode.getFullName() + "</td>");
    	    out.println("<td>&nbsp;</td>");
    	    out.println("<td>&nbsp;</td>");
    	    out.println("</tr>");
    	}
    }
	

    // RENDER SUBCATEGORIES
	Iterator cit = catNode.getSubcategories().iterator();
	while(cit.hasNext()) {
		CategoryModel subCatNode = (CategoryModel) cit.next();
		renderCategoryNode(subCatNode, level, out);
	}
}
%><%!

void renderSKUs(List skuNodes, String parentCName, int level, JspWriter out) throws IOException, FDResourceException, FDSkuNotFoundException {
    Iterator sit = skuNodes.iterator();
    while(sit.hasNext()) {
        SkuModel skuNode = (SkuModel) sit.next();
        boolean isUna = skuNode.isUnavailable();
        String cs = (isUna ? "color: grey;" : " ");

        out.println("<tr>");
        out.println("<td style='padding-left: " + (level*15) + "px; "+cs+"'>"+parentCName+"</td>");
        out.println("<td style='"+cs+"'>" + skuNode.getFullName() + "</td>");
        out.println("<td style='"+cs+"'>" + skuNode.getContentName() + "</td>");
        try {
            if (skuNode.getProduct() != null) {
                String m = skuNode.getProduct().getMaterial().getMaterialNumber();
                out.println("<td style='"+cs+"'>" + (m!=null ? m : "N/A") + "</td>");
            } else {
                out.println("<td style='"+cs+"'>N/A</td>");
            }
        } catch (FDSkuNotFoundException e) {
            out.println("<td style='"+cs+"'>N/A</td>");
        }
        out.println("</tr>");
    }
}
%><%!

void renderRecipeNode(Recipe recipeNode, int level, JspWriter out) throws IOException, FDResourceException, FDSkuNotFoundException {
    // I. RENDER RECIPE
    out.println("<tr>");
    out.println("<td style='padding-left: " + (level*15) + "px'>R: " + recipeNode.getContentName() + "</td>");
    out.println("<td>" + recipeNode.getFullName() + "</td>");
    out.println("<td>&nbsp;</td>");
    out.println("<td>&nbsp;</td>");
    out.println("</tr>");
    
    ++level;

    // II. ITERATE VARIANTS
    Iterator cit = recipeNode.getVariants().iterator();
    while(cit.hasNext()) {
    	RecipeVariant vNode = (RecipeVariant) cit.next();
        renderVariantNode(vNode, level, out);
    }
}

%><%!

void renderVariantNode(RecipeVariant vNode, int level, JspWriter out) throws IOException, FDResourceException, FDSkuNotFoundException {
    // I. RENDER VARIANT
    out.println("<tr>");
    out.println("<td style='padding-left: " + (level*15) + "px'><i><b>V:</b>" + vNode.getContentName() + "</i></td>");
    out.println("<td>&nbsp;</td>");
    out.println("<td>&nbsp;</td>");
    out.println("<td>&nbsp;</td>");
    out.println("</tr>");
    
    ++level;

    // II. ITERATE SECTIONS
    Iterator cit = vNode.getSections().iterator();
    while(cit.hasNext()) {
    	RecipeSection sNode = (RecipeSection) cit.next();
        renderSectionNode(sNode, level, out);
    }
}

%><%!

void renderSectionNode(RecipeSection rNode, int level, JspWriter out) throws IOException, FDResourceException, FDSkuNotFoundException {
    // I. RENDER SECTION
    out.println("<tr>");
    out.println("<td style='padding-left: " + (level*15) + "px'><i><b>S:</b>" + rNode.getContentName() + "</i></td>");
    out.println("<td>&nbsp;</td>");
    out.println("<td>&nbsp;</td>");
    out.println("<td>&nbsp;</td>");
    out.println("</tr>");
    
    ++level;

    // II. ITERATE SKU's
    Iterator cit = rNode.getIngredients().iterator();
    while(cit.hasNext()) {
        ConfiguredProduct cpNode = (ConfiguredProduct) cit.next();
        renderSKUs(cpNode.getSkus(), cpNode.getContentName(), level, out);
    }
}

%><%
List contentKeys = new ArrayList();
List deptKeyz = Collections.EMPTY_LIST;
List catKeyz = Collections.EMPTY_LIST;
List recipeKeyz = Collections.EMPTY_LIST;
DCPDQuery q = null;
Iterator it;







if (request.getParameter("keyz1") != null) {
	deptKeyz = Arrays.asList(request.getParameter("keyz1").trim().split(",\\s*"));
	contentKeys.addAll(deptKeyz);
}
if (request.getParameter("keyz2") != null) {
	catKeyz = Arrays.asList(request.getParameter("keyz2").trim().split(",\\s*"));
    contentKeys.addAll(catKeyz);
}
if (request.getParameter("keyz3") != null) {
	recipeKeyz = Arrays.asList(request.getParameter("keyz3").trim().split(",\\s*"));
    contentKeys.addAll(recipeKeyz);
}

if (contentKeys.size() > 0) {
	q = new DCPDQuery(contentKeys);
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
	
    .qTable th {
       font: 12px "Trebuchet MS";
       text-align: left;
    }

	.dcpd {
	   /* border: 1px solid #ccc; */
       border-left: 1px solid #3c3;
       width: 100%;
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
	
	.deptcat {
	   color: #960;
       font: 12px bold "Trebuchet MS";
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
<h2>Found <%= q.resultCount() %> node(s)</h2>
<table class="dcpd" cellpadding="0" cellspacing="0">
	<tr>
		<th style='text-align: center'>Product / Folder ID</th>
        <th>Full Name</th>
		<th>SKU</th>
		<th>Material</th>
	</tr>
<% it=q.getNodes().iterator(); while(it.hasNext()) {
	// Use Breadth-first search algorithm
	// INIT
	ContentNodeModel rootNode = (ContentNodeModel) it.next();
	
	if (rootNode instanceof DepartmentModel) {
		renderDepartmentNode( (DepartmentModel) rootNode, 1, out);
	} else if (rootNode instanceof CategoryModel) {
		renderCategoryNode( (CategoryModel) rootNode, 1, out);
	} else if (rootNode instanceof Recipe) {
		renderRecipeNode( (Recipe) rootNode, 1, out);
	} else {
		// TODO
		System.out.println("Nothing to do with " + rootNode.getClass() + "/" + rootNode);
	}
%>
    <tr><td colspan="4" style="padding: 0; height: 6px; width: 100%; background-color: #cccc99"></td></tr><%
} //it %>
</table>
<%	}
}
%>
</body>
</html>
