
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>


<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.SearchResultUtil' %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />

<%
String searchParams = request.getParameter("searchParams");
// Uncomment these two lines below if trk=dym needs to be propagated to search links
//String trk = request.getParameter("trk");
//if (trk == null) trk = "srch";
String trk = "srch";
%>

<tmpl:insert template='/common/template/search_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Search<%= searchParams != null && searchParams.length() > 0 ?  (" - " + searchParams) : ""%></tmpl:put>
<tmpl:put name='content' direct='true'>

<oscache:cache time="1" key='<%= "search_"+searchParams %>'>
<%
try {
	ContentFactory contentFactory = ContentFactory.getInstance(); 
%>
<fd:ParseSearchTerms searchParams='<%= searchParams %>' isBulkSearch="false" searchFor="criteria" searchList="termList">
<table width="520" border="0" cellspacing="0" cellpadding="2">
<tr><td width="520" class="text11">

<% if ( criteria == null || "".equals(criteria.trim()) ) { %>
	<%@ include file="/includes/search/search_tips.jspf"%>
<% } else { %>

<fd:Search searchFor="<%= criteria %>" searchResults="results" errorPage="/index.jsp">

<%
	if (results.numberOfResults()==0) {
%>
	<%@ include file="/includes/search/no_results.jspf"%>
<%
	} else {
		if (results.isSuggestionMoreRelevant()) {
			String sug = results.getSpellingSuggestion();
		%>
		<div class="text15" style="line-height: 3em">
			<b><%=results.numberOfResults() == 1 ? "One match was " : ("" + results.numberOfResults() + " matches were ")%> found for <i>"<%=criteria%>"</i></b>
		</div>
		<div class="text15">
		Did you mean <a href="/search.jsp?searchParams=<%=StringUtil.escapeUri(sug)%>&trk=dym"><b><%=sug%></b></a>?<br/><br/>
		</div>
		<%
		}

		List exactProducts = results.getExactProducts();
		if (exactProducts.size()>0) {
			Collections.sort(exactProducts, SearchResultUtil.RECURSIVE_NODE_COMPARATOR);
%><FONT CLASS="title12"><%= exactProducts.size() %> exact product match<%= exactProducts.size() > 1 ? "es":""%> "<font class="text12orbold"><%=searchParams%></font>"</FONT><br>
<img src="/media_stat/images/layout/ff9933.gif" width="520" height="1" border="0" vspace="4"><br><br>
<div style="padding-left: 1em">
<logic:iterate id="prod" collection="<%= exactProducts %>" type="com.freshdirect.fdstore.content.ProductModel">
<%= SearchResultUtil.getPathDisplay(prod,trk) %><br>
</logic:iterate>
</div>
<br><br>
<%
		}

		List recipes = results.getRecipes();
		if (recipes.size()>0) {
			%>
			<div class="title12">
				<span style="color: #c00">NEW!</span> <%= recipes.size() %> recipe<%= recipes.size() > 1 ? "s were":" was" %> found
			<%
				if (recipes.size() > 5) {
			%>
				(<b>Show <a href="#" onclick="toggleRecipes(); return false;" id="recipe_label">All</a></b>)
			<%
				}
			%>
			</div>
			<img src="/media_stat/images/layout/ff9933.gif" width="520" height="1" border="0" vspace="4"><br><br>

<%
			Iterator ri = recipes.iterator();
			StringBuffer allRecipeLines = new StringBuffer();
			StringBuffer fiveRecipes = new StringBuffer();
			for(int c= 0; ri.hasNext(); ++c) {
				Recipe recipe = (Recipe)ri.next();
				StringBuffer line = new StringBuffer();

				if (c == 5) {
%>
				<div id="all_link"> </div>
<%
				}

				line.append("<b><a href=\"/recipe.jsp?recipeId=").
					append(recipe.getContentName()).append("&trk=").append(trk).append("\">").
					append(recipe.getName()).append("</a></b>");
				if (recipe.getSource() != null) { 
					line.append(" from <a href=\"javascript:popup(\'/recipe_source.jsp?recipeId=").
						append(recipe.getContentName()).append("&trk=").append(trk).append("\',\'large_long\')\">").
						append(recipe.getSource().getName()).append("</a>");
				}
				line.append("<br/>");
					
				if (c >= 5) allRecipeLines.append(line);
				else {
%>
					<%= line %>
<%
				}

%>

			<%-- <div style="display: none" id='a'> .... <div style="display: block" id='a');

			<%-- <logic:iterate id="recipe" collection="<%= recipes %>" type="com.freshdirect.fdstore.content.Recipe"> 
			<b><a href="/recipe.jsp?recipeId=<%= recipe.getContentName() %>&trk=<%=trk%>"><%= recipe.getName() %></a></b>
			<% if (recipe.getSource()!=null) { %>
				from <a href="javascript:popup('/recipe_source.jsp?recipeId=<%= recipe.getContentName() %>&trk=<%=trk%>','large_long')"><%= recipe.getSource().getName() %></a>
			<% } %><br>
			</logic:iterate> --%>

<%			
			}

%>
			<script language="javascript">
			var hiddenRecipeLines = '<%=StringUtil.escapeJavaScript(allRecipeLines.toString())%>';
			var allShown = false;

			function toggleRecipes() {
				var contents = document.getElementById('all_link');
				var label = document.getElementById('recipe_label');

				if (allShown) {
					contents.innerHTML = "";
					label.innerHTML = "All";
				} else {
					contents.innerHTML = hiddenRecipeLines ;
					label.innerHTML = "First 5";
				}

				allShown = !allShown;
			}
			</script>

			<br>
			For more, <a href="<%=response.encodeURL("department.jsp?deptId="+RecipeDepartment.getDefault().getContentName())%>&trk=<%=trk%>">click here to Shop By Recipe!</a>
			<br>
			<br><br>
			<%
		}

		List exactCategories = results.getExactCategories();
		if (exactCategories.size()>0) {
			Collections.sort(exactCategories, SearchResultUtil.RECURSIVE_NODE_COMPARATOR);
%><FONT CLASS="title12"><%= exactCategories.size() %> categor<%= exactCategories.size() > 1 ? "ies were":"y was" %> found</FONT><br>
<IMG src="/media_stat/images/layout/ff9933.gif" width="520" height="1" border="0" vspace="4"><br>
<logic:iterate id="cat" collection="<%= exactCategories %>" type="com.freshdirect.fdstore.content.CategoryModel">
&nbsp;&nbsp;&nbsp;<%= SearchResultUtil.getPathDisplay(cat,trk) %><br>
</logic:iterate>
<br><br>
<%
		}

		List fuzzyProducts = results.getFuzzyProducts();
		System.err.println("fuzzyProducts: "+fuzzyProducts);
		if (fuzzyProducts.size()>0) {
			Map nodeTree = SearchResultUtil.buildNodeTree(fuzzyProducts);
			System.err.println("nodeTree: "+nodeTree);

%><FONT CLASS="title12"><%= fuzzyProducts.size() %> product<%= fuzzyProducts.size() > 1 ? "s were":" was"%> found</FONT><br>
<IMG src="/media_stat/images/layout/ff9933.gif" width="520" height="1" border="0" vspace="4"><br>
<FONT CLASS="text11orbold">Jump to results in department:</FONT><br>
<%
			Collection depts = (Collection) nodeTree.get( ((TreeSet)nodeTree.get(null)).first() );
%>
<logic:iterate id="dept" collection="<%= depts %>" type="com.freshdirect.fdstore.content.DepartmentModel" indexId="idx">
<%= idx.intValue()>0 ? "." : "" %> <a href="#<%= dept.getContentName() %>"><b><%= dept.getFullName() %></b></a>
</logic:iterate>
<IMG src="/media_stat/images/layout/999999.gif" width="520" height="1" border="0" vspace="6"><br>
<br>
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>	
			<td width=10><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
			<td><% SearchResultUtil.displayNodeTree( out, nodeTree, trk );%></td>
		</tr>
	</table>

<%
		}
    }
%>
</fd:Search>
<% } // end if criteria == null or criteria is empty String %>

	</TD>
</TR>
</TABLE>


</fd:ParseSearchTerms>

<% } catch (Exception ex) {
		ex.printStackTrace();
%>
	<oscache:usecached />
<% } %>
</oscache:cache>

</tmpl:put>
</tmpl:insert>
