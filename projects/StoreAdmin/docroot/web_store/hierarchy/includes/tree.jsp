<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String currentDir = pageURI.substring(0, pageURI.lastIndexOf("/")+1);
String folderLink = currentDir;
String productLink = currentDir;

if ( pageURI.toLowerCase().indexOf("/pop_") > -1 ) {
		productLink = "#";
		folderLink = "#";
	} else {
	
	if ( pageURI.toLowerCase().indexOf("/attributes") > -1 ) {
		folderLink += "attributes.jsp";
	} else {
		folderLink += "contents.jsp";
	}
	
	if ( pageURI.toLowerCase().indexOf("/sku_info") > -1 ) {
		productLink += "sku_info.jsp";
	} else {
		productLink += "product_info.jsp";
	}
}
	
//get current selected
String thisDept = request.getParameter("dept");
String thisCat = request.getParameter("cat");
String thisSubCat = request.getParameter("subcat");
String thisProd = request.getParameter("prod");

String selected = "style='background-color:#FFFFFF;'";
%>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td class="tree"> 
<div class="dept">
<div class="dept" <% if ("department1".equalsIgnoreCase(thisDept)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c1'); return false">&curren;</a> <a href="<%= folderLink %>?dept=department1">Department 1</a></div>
<div id="c1" class="cat">
<div class="cat" <% if ("category1".equalsIgnoreCase(thisCat)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c1sc1'); return false">&curren;</a> <a href="<%= folderLink %>?cat=category1">Category 1</a></div>
<div id="c1sc1" class="subcat">
<div class="subcat" <% if ("subcategory1".equalsIgnoreCase(thisSubCat)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c1sc1p'); return false">&curren;</a> <a href="<%= folderLink %>?subcat=subcategory1">Subcategory 1</a></div>
<div id="c1sc1p" class="prod">
<div class="prod" <% if ("product1a".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product1a">Product 1a</a></div>
<div class="prod" <% if ("product1b".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product1b">Product 1b</a></div>
<div class="prod" <% if ("product1c".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product1c">Product 1c</a></div>
</div>
</div>
</div>
</div>
</td>
</tr>

<tr>
<td class="tree"> 
<div class="dept">
<div class="dept" <% if ("department2".equalsIgnoreCase(thisDept)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c2'); return false">&curren;</a> <a href="<%= folderLink %>?dept=department2">Department 2</a></div>

<div id="c2" style="display: none;" class="cat">
<div class="cat" <% if ("category2".equalsIgnoreCase(thisCat)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c2sc'); return false">&curren;</a> <a href="<%= folderLink %>?cat=category2">Category 2</a></div>
<div id="c2sc" style="display: none;" class="subcat">
<div class="subcat" <% if ("subcategory2".equalsIgnoreCase(thisSubCat)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c2sc1p'); return false">&curren;</a> <a href="<%= folderLink %>?subcat=subcategory2">Subcategory 2</a></div>
<div id="c2sc1p" style="display: none;" class="prod">
<div class="prod" <% if ("product2a".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product2a">Product 2a</a></div>
<div class="prod" <% if ("product2b".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product2b">Product 2b</a></div>
<div class="prod" <% if ("product2c".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product2c">Product 2c</a></div>
</div>

<div class="subcat" <% if ("subcategory2a".equalsIgnoreCase(thisSubCat)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c2sc2p'); return false">&curren;</a> <a href="<%= folderLink %>?subcat=subcategory2a">Subcategory 2a</a></div>
<div id="c2sc2p" style="display: none;" class="prod">
<div class="prod" <% if ("product2aa".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product2aa">Product 2aa</a></div>
<div class="prod" <% if ("product2ab".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product2ab">Product 2ab</a></div>
<div class="prod" <% if ("product2ac".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product2ac">Product 2ac</a></div>
</div>

</div><!-- subcat -->

<div class="cat"><a href="#" onclick="toggleDisplay('c3sc'); return false">&curren;</a> <a href="<%= folderLink %>?cat=category3" <% if ("category3".equalsIgnoreCase(thisCat)) { %><%=selected%><% } %>>Category 3</a></div>
<div id="c3sc" style="display: none;" class="subcat">
<div class="subcat" <% if ("subcategory3".equalsIgnoreCase(thisSubCat)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c3sc1p'); return false">&curren;</a> <a href="<%= folderLink %>?subcat=subcategory3">Subcategory 3</a></div>
<div id="c3sc1p" style="display: none;" class="prod">
<div class="prod" <% if ("product3a".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product3a">Product 3a</a></div>
<div class="prod" <% if ("product3b".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product3b">Product 3b</a></div>
<div class="prod" <% if ("product3c".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product3c">Product 3c</a></div>
</div>

<div class="subcat" <% if ("subcategory3a".equalsIgnoreCase(thisSubCat)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c3sc2p'); return false">&curren;</a> <a href="<%= folderLink %>?subcat=subcategory">Subcategory 3a</a></div>
<div id="c3sc2p" style="display: none;" class="prod">
<div class="prod" <% if ("product3aa".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product3aa">Product 3aa</a></div>
<div class="prod" <% if ("product3ab".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product3ab">Product 3ab</a></div>
<div class="prod" <% if ("product3ac".equalsIgnoreCase(thisProd)) { %><%=selected%><% } %>><a href="<%= productLink %>?prod=product3ac">Product 3ac</a></div>
</div>

</div><!-- subcat -->

</div><!-- cat -->
</div><!-- dept -->
</td>
</tr>

</table>
<br>