<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.storeapi.*'%>
<%@ page import='com.freshdirect.storeapi.application.*'%>
<%@ page import='com.freshdirect.storeapi.node.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<% //expanded page dimensions
final int W_RECIPE_SEARCH_TOTAL = 601;
%>

<fd:CheckLoginStatus guestAllowed="true" />
<%
//RecipeDepartment recipeDepartment = (RecipeDepartment) RecipeDepartment.getDefault();
RecipeSearchPage searchPage = RecipeSearchPage.getDefault();
request.setAttribute("listPos", "SystemMessage,SideCartBottom");
%>
<tmpl:insert template='/common/template/recipe_DLRnavs.jsp'>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Recipe Search</tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="FreshDirect - Recipe Search"></fd:SEOMetaTag>
    </tmpl:put>
	<tmpl:put name='leftnav' direct='true'>
		<a href="/recipe_search.jsp?trk=snav"><img src="/media_stat/recipe/recipes_advsearch_catnav.gif" alt="Advanced recipe search" width="118" height="64" vspace="17" border="0"></a>
	</tmpl:put>
	<tmpl:put name='pageType' direct='true'>recipe_search</tmpl:put>
    <tmpl:put name='content' direct='true'>

<style type="text/css">
.W_RECIPE_SEARCH_TOTAL { width: <%= W_RECIPE_SEARCH_TOTAL %>px; }
.section {
 border-bottom: 1px solid #f93;
 padding-bottom: 1em;
 margin: 14px;
 margin-top: 0em;
 text-align: left;
}

fieldset {
 border: 0;
 padding: 0;
 margin: 0;
 margin-bottom: 1em;
}

label.legend, .dropdown_recipe label, label.keyword {
 display: block;
 font-size: 10pt;
 font-weight: bold;
 text-transform: uppercase;
 color: #f93;
 padding-bottom: 0.1em;
 font-family:Arial, Verdana, Helvetica, sans-serif;
}

input.submit {
 background-color: #f93;
 color: #fff;
 font-family: Verdana, sans-serif;
 font-weight: bold;
 border: 0;
 -moz-border-radius: 4px;
 padding: 4px;
}

.dropdown_recipe select {
 font-family: Verdana, sans-serif;
 font-size: 7pt;
 width: 210px;
 margin-bottom: 1em;
}

.resultsHeading {
 text-align: left;
 border-bottom: 1px solid #f93;
 margin: 0 1em 1em 1em;
 padding-bottom: 1em;
}

.resultsHeading h1 {
 text-transform: uppercase;
 font-weight: normal;
 font-size: 13pt;
 font-family: Arial, sans-serif;
 color: #f93;
 margin-top: 0em;
 margin-bottom: 0.25em;
}

h2 {
 font-weight: bold;
 font-size: 12pt;
 font-family: Verdana, Arial, sans-serif;
 margin-top: 0.25em;
 margin-bottom: 0.75em;
}

.resultsHeading .selected {
 font-weight: bold;
}

.resultsHeading .separator {
 color: #999;
}


h1.leftnav {
 text-transform: uppercase;
 font-weight: normal;
 font-size: 13pt;
 font-family: Arial, sans-serif;
}

.searchBottom {
 margin: 1em;
 padding: 1em;
 border-top: 1px solid #f93;
 font-weight: bold;
}
</style>

<%
boolean searchPerformed = false;



%>
<fd:FindRecipes id="recipes">
	<%
	searchPerformed = true;
	String unfilteredQuery = request.getQueryString();
	
	StringTokenizer unfilteredQueryTokn  = new StringTokenizer(unfilteredQuery,"&");
	String cleanedUnfilteredQuery = "";
	int paramCount = 0;
	
	for (;unfilteredQueryTokn.hasMoreTokens();) {
		String aParam = unfilteredQueryTokn.nextToken();
		if (aParam.startsWith("x=") || aParam.startsWith("y=") || aParam.indexOf("=")==-1 || aParam.startsWith("trk=") || aParam.startsWith("filter=")) { 
			continue; //bogus param
		} else if (aParam.length() == aParam.indexOf("=")+1) {
			continue;  //param has no value
		} else {
			if (paramCount > 0) {
			cleanedUnfilteredQuery += "&";
			}
			cleanedUnfilteredQuery += aParam;
			paramCount++;
		}
	}
	
	unfilteredQuery = cleanedUnfilteredQuery;
	
	if (recipes.isEmpty()) {
		%>
		<h2>No matching recipes found</h2>
		<a href="?<%= unfilteredQuery %>&edit&trk=rsrch">Edit your search.</a>
		<%
	} else {
		StringTokenizer qryStringTokn = new StringTokenizer(request.getQueryString(),"&");
		StringBuffer aNewURL = new StringBuffer(200);
		int pcount=0;
		String resultHeading="MATCHING RECIPES";
		
		for (;qryStringTokn.hasMoreTokens() && pcount<2;) {
			String aParam=qryStringTokn.nextToken();
			
			if(aParam.startsWith("x=") || aParam.startsWith("y=") || aParam.indexOf("=")==-1 || aParam.startsWith("trk=") || aParam.startsWith("filter=")) continue; //bogus param
			
			if (aParam.length() == aParam.indexOf("=")+1) continue;  //param has no value
			
			if (aParam.startsWith("recipeSource") && aParam.length() > "recipeSource=".length()) {
			    RecipeSource rs = (RecipeSource) PopulatorUtil.getContentNode(request.getParameter("recipeSource"));
			    resultHeading="RECIPES FROM \""+rs.getName()+"\"";
			}
			
			if (aParam.startsWith("recipeAuthor") && aParam.length() > "recipeAuthor=".length()) {
			    RecipeAuthor ra = (RecipeAuthor) PopulatorUtil.getContentNode(request.getParameter("recipeAuthor"));
			    resultHeading="RECIPES BY "+ra.getName();
			}
			
			pcount++;
			if (pcount>1) {
		            resultHeading="MATCHING RECIPES";
			}
		}
		
		%>
		<div class="resultsHeading" class="W_RECIPE_SEARCH_TOTAL">
		<h1><%=resultHeading%></h1>
		<%
        List classifications = (List) pageContext.getAttribute("classifications");
        if (classifications != null) {
			String filter = NVL.apply(request.getParameter("filter"), "");
			if ("".equals(filter)) {
				%><span class="selected">ALL</span><%
			} else {
				%><a href="?<%=unfilteredQuery%>&trk=filt">ALL</a><%
			}
			
            for (Iterator it = classifications.iterator(); it.hasNext(); ) {
                DomainValue dv = (DomainValue) it.next();
				String id = dv.getContentKey().getId();
                %>
                <span class="dot_separator">&#149;</span>
				<%
				if (filter.equals(id)) {
					%><span class="selected"><%= dv.getLabel() %></span><%
				} else {
					%><a href="?<%=unfilteredQuery%>&filter=<%= id %>&trk=filt"><%= dv.getLabel() %></a><%
				}
            }
        }
		%>
		</div>
		<div style="width: <%=W_RECIPE_SEARCH_TOTAL%>px;">
		<%

		for (Iterator i = recipes.iterator(); i.hasNext(); ) {
			Recipe recipe = (Recipe) i.next();
			%>
			<a href="<%=response.encodeURL("recipe.jsp?recipeId="+recipe)%>&trk=rsrch" class="recipe_name"><b><%=recipe.getName()%></b></a>
			<br>
			<span class="recipe_author"><%=recipe.getDisplayableSource()%></span>
			<br><br>
			<%
		}
		%>
		</div>
		
		<div class="searchBottom">
			Didn't find what you were looking for?<br>
			<a href="?<%= unfilteredQuery %>&edit&trk=rsrch">Edit your search.</a>
		</div>
		<%
	}
	%>
	
</fd:FindRecipes>

<%
if (!searchPerformed) {
	%>
	<table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><form name="recipeSearch"><td valign="top">
	<div class="section" style="padding-bottom: 2em;">
		<label for="keyword" class="keyword" style="font-size: 9pt; font-family:Verdana, Helvetica, sans-serif;"><b>Search for:</b></label>
		<input type="text" id="keyword" name="keyword" class="search" style="width:245px;" value="<%= request.getParameter("keyword") %>"/>
	</div>

	<div class="section" style="padding: 0px 104px 0px 0px;">
		<%
		List dropdownCriteria = searchPage.getCriteriaBySelectionType(RecipeSearchCriteria.TYPE_ONE);	
		int countDropdown = 0;
		for (Iterator i = dropdownCriteria.iterator(); i.hasNext(); ) {
			RecipeSearchCriteria crit = (RecipeSearchCriteria) i.next();
			String id = crit.getContentName();
			countDropdown++;
			%>
			
			<div class="dropdown_recipe" style="float:<%= countDropdown%2==0 ? "right" :"left" %>;">
			<label for="<%= id %>"><%= crit.getName() %></label>
			<select id="<%= id %>" name="<%= id %>" class="text9" style="width:200px;">
				<option value="">All</option>
				<% 
				for (Iterator j = crit.getCriteriaDomainValues().iterator(); j.hasNext(); ) {
					DomainValue dv = (DomainValue) j.next();
					boolean sel = NVL.apply(request.getParameter(id), "").equals(dv.getContentName());
					%>
					<option value="<%= dv.getContentName() %>" <%= sel ? "selected" : "" %>><%= dv.getLabel() %></option>
					<%
				}
				%>
			</select>
			</div>
			<%= countDropdown%2==0 ? "<br clear =\"all\">":""%>
			<%
		}
		countDropdown++;
		%>
		
		<div class="dropdown_recipe" style="float:<%= countDropdown%2==0 ? "right" :"left" %>;">
		<label for="recipeSource">Cookbooks</label>
		<select id="recipeSource" name="recipeSource" class="text9" style="width:200px;">
			<option value="">All</option>
			<%
			for (Iterator i = RecipeSource.findAllAvailable().iterator(); i.hasNext(); ) {
				RecipeSource recipeSource = (RecipeSource) i.next();
				String id = recipeSource.getContentName();
				boolean sel = NVL.apply(request.getParameter("recipeSource"), "").equals(id);
				%><option value="<%= id %>" <%= sel ? "selected" : "" %>><%=recipeSource.getName()%></option><%
			}
			%>
		</select>
		</div>
		
		<%= countDropdown%2==0 ? "<br clear =\"all\">":""%>
		<% countDropdown++; %>
		
		<div class="dropdown_recipe" style="float:<%= countDropdown%2==0 ? "right" :"left" %>;">
		<label for="recipeAuthor">Authors</label>
		<select name="recipeAuthor" id="recipeAuthor" class="text9" style="width:200px;">
			<option value="">All</option>
			<%
			for (Iterator i = RecipeAuthor.findAllAvailable().iterator(); i.hasNext(); ) {
				RecipeAuthor recipeAuthor = (RecipeAuthor) i.next();
				String id = recipeAuthor.getContentName();
				boolean sel = NVL.apply(request.getParameter("recipeAuthor"), "").equals(id);
				%><option value="<%= id %>" <%= sel ? "selected" : "" %>><%=recipeAuthor.getName()%></option><%
			}
			%>
		</select>
		</div>
		<br clear="all">
		<%-- leave this div here, it fixes the layout --%>
			<div></div>
		<%-- leave this div here, it fixes the layout --%>
	</div>

	
		<%
		List checkboxCriteria = searchPage.getCriteriaBySelectionType(RecipeSearchCriteria.TYPE_MANY);	
		for (Iterator i = checkboxCriteria.iterator(); i.hasNext(); ) {
			RecipeSearchCriteria crit = (RecipeSearchCriteria) i.next();
			%>
			<div class="section">
				<fieldset><legend><label class="legend" style="padding-bottom: 3px;"><%= crit.getName() %></legend></label>
				<% 
				String[] params = request.getParameterValues(crit.getContentName());
				List paramList = params==null ? Collections.EMPTY_LIST : Arrays.asList(params);
				
				int maxRow = (int)(crit.getCriteriaDomainValues().size() / 3);
				maxRow += crit.getCriteriaDomainValues().size()%3 != 0 ? 1:0;
 
				int colCount = 0;
				int itemCount = 0;
				for (Iterator j = crit.getCriteriaDomainValues().iterator(); j.hasNext(); ) {
					DomainValue dv = (DomainValue) j.next();
					String id = crit.getContentName() + "." + dv.getContentName();
					boolean sel = paramList.contains(dv.getContentName());
					colCount++;
					itemCount++;
					
					%>
					
					<div style="float:left; width:146px;">
					<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr valign="top">
						<td width="20"><input type="checkbox" id="<%= id %>" name="<%= crit.getContentName() %>" value="<%= dv.getContentName() %>" <%= sel ? "checked" : "" %> /></td>
						<td width="126" style="padding-top:0.35em;"><label for="<%= id %>"><%= dv.getLabel() %></label></td>
					</tr>
					<tr><td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt=""></td><td><img src="/media_stat/images/layout/clear.gif" width="124" height="1" alt=""></td></tr></table>
					</div>
					<% 
					
					if (colCount%4==0) { 
						colCount = 0;
					%>
						<br clear ="all">
					<%
					}
						if (itemCount == (crit.getCriteriaDomainValues().size())) {
							if (colCount == 1) { 
							%>
								<div style="float:left; width:146px;"></div><div style="float:left; width:146px;"></div><div style="float:left; width:146px;"></div><br clear ="all">
							<%
							} else if (colCount == 2) { 
							%>
								<div style="float:left; width:146px;"></div><div style="float:left; width:146px;"></div><br clear ="all">
							<%
							} else if (colCount == 3) {
								%>
								<div style="float:left; width:146px;"></div><br clear ="all">
							<%
								
							}
						}
				}
				%>
				<div></div>
				</fieldset>
				</div>
			<%
		}
		%>
	

	<div align="center"><input type="image" src="/media_stat/recipe/adv_srch_butt_find.gif" width="62" height="23" border="0" alt="FIND"> &nbsp;&nbsp; <a href="javascript:document.recipeSearch.reset();"><img src="/media_stat/recipe/adv_srch_butt_clear.gif" width="62" height="23" border="0" alt=""></a></div>
	</td>
	<input type="hidden" name="trk" value="rsrch">
	</form></tr></table>
	<%
}
%>

    </tmpl:put>

</tmpl:insert>
