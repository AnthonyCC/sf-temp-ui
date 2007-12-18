<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String URL = request.getRequestURI() + "?" + request.getQueryString();
String folderLink = pageURI.substring(0, pageURI.lastIndexOf("/")+1) + "contents.jsp";
boolean pop = false;
	if (pageURI.indexOf("pop_") > -1 ) {
		pop = true;
		if (pageURI.indexOf("view_edit") > -1 ) {
		folderLink = URL;
			if (request.getQueryString().indexOf("state") < 0 ) {
				folderLink += "&state=preview";
			}
		}
	}
	
//get current selected
String thisDept = request.getParameter("dept");
String thisCat = request.getParameter("cat");
//String thisSubCat = request.getParameter("subcat");
//String thisProd = request.getParameter("prod");

String selected = "style='background-color:#FFFFFF;'";
%>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td class="tree"> 
<div class="dept">

<div class="dept" <% if ("department1".equalsIgnoreCase(thisDept)) { %><%=selected%><% } %>>
<a href="#" onclick="toggleDisplay('c1'); return false">&curren;</a> <a href="<%= folderLink %><% if (!pop){%>?dept=department1<%}%>">Department 1</a>
</div>

<div id="c1" class="cat">
<div class="cat" <% if ("category1".equalsIgnoreCase(thisCat)) { %><%=selected%><% } %>>&nbsp;&nbsp;<a href="<%= folderLink %>?cat=category1">Category 1</a></div>
<div class="cat" <% if ("category1a".equalsIgnoreCase(thisCat)) { %><%=selected%><% } %>>&nbsp;&nbsp;<a href="<%= folderLink %>?cat=category1a">Category 1a</a></div>
</div>

<div class="dept" <% if ("department2".equalsIgnoreCase(thisDept)) { %><%=selected%><% } %>><a href="#" onclick="toggleDisplay('c2'); return false">&curren;</a> <a href="<%= folderLink %><% if (!pop){%>?dept=department2<%}%>">Department 2</a></div>

<div id="c2" class="cat" style="display:none;">
<div class="cat" <% if ("category2".equalsIgnoreCase(thisCat)) { %><%=selected%><% } %>>&nbsp;&nbsp;<a href="<%= folderLink %>?cat=category2">Category 2</a></div>
<div class="cat" <% if ("category2a".equalsIgnoreCase(thisCat)) { %><%=selected%><% } %>>&nbsp;&nbsp;<a href="<%= folderLink %>?cat=category2a">Category 2a</a></div>
</div>

</div>
</td>
</tr>

</table>