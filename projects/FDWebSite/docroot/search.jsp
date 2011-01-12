<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import="com.freshdirect.fdstore.content.DomainValue"%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>
<%@ page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@ page import="com.freshdirect.fdstore.util.SearchNavigator"%>
<%@ page import="com.freshdirect.fdstore.*"%>
<%@ page import="com.freshdirect.cms.*"%>
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@ page import="com.freshdirect.fdstore.content.*"%>
<%@ page import='com.freshdirect.fdstore.attributes.*'%>
<%@ page import='com.freshdirect.webapp.util.SearchResultUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='oscache' prefix='oscache'%>

<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" noRedirect="true" />

<%
final String SEPARATOR = "&nbsp;<span class=\"text12\" style=\"color: #CCCCCC\">&bull;</span>&nbsp;";
final String trk = "srch"; // tracking code

String criteria = request.getParameter("searchParams");
String upc = request.getParameter("upc");
String displayCriteria = upc != null && !upc.trim().equals("") ? upc : criteria;

// OAS AD settings
request.setAttribute("sitePage", "www.freshdirect.com/search.jsp");
request.setAttribute("listPos", "SystemMessage,LittleRandy,CategoryNote");

SearchNavigator nav = new SearchNavigator(request);
if (SearchNavigator.VIEW_DEFAULT == nav.getView() && request.getParameter("refinement") == null) {
	// view is not set yet (== default)
	//   let's get the latest used one from the session
	if (session.getAttribute(SessionName.SMART_SEARCH_VIEW) != null) {
		boolean wasDefaultSort = nav.isDefaultSort();
		nav.setView(SearchNavigator.convertToView(( String)session.getAttribute(SessionName.SMART_SEARCH_VIEW) ));
		if (wasDefaultSort && nav.isTextView()) {
			// reset default sort which was adjusted by changing to text view
			nav.setSortBy(SearchSortType.DEF4TEXT);
		}
	}
} else {
	// record custom view type
	if (SearchNavigator.VIEW_DEFAULT == nav.getView()) {
		session.removeAttribute(SessionName.SMART_SEARCH_VIEW);
	} else {
		session.setAttribute(SessionName.SMART_SEARCH_VIEW, SearchNavigator.convertToViewName(nav.getView()));	
	}
}

request.setAttribute("sx.navigator", nav);
request.setAttribute("recipes.show_all", new Boolean(nav.isRecipesDeptSelected())); // -> recipes.jspf
%>


<fd:SmartSearch searchResults="results" productList="productList" categorySet="categorySet" brandSet="brandSet" categoryTree="categoryTree" filteredCategoryTreeName="filteredCategoryTree">
<tmpl:insert template='/common/template/search_nav.jsp'>
<%
//special case

boolean jumpToRecipes = (results!=null) && ((results.getProducts().size() == 0 && results.getRecipes().size() > 0) || nav.isRecipesDeptSelected());
if (jumpToRecipes && !nav.isRecipesDeptSelected()) {
	nav.setDepartment(SearchNavigator.RECIPES_DEPT);
	nav.setSortBy(SearchSortType.DEF4RECIPES);

	request.setAttribute("recipes.show_all", Boolean.TRUE); // -> recipes.jspf
}

// show category panel if found products (recipes don't count)
if (results != null && results.numberOfResults() > 0) {

%>
<tmpl:put name="categoryPanel" direct="true">
<%-- CATEGORY TREE NAVIGATOR --%>
<%
	if ( categoryTree != null ) {
%><%@ include file="/includes/search/treenav.jspf" %><%
	}
%>
</tmpl:put>
<%
} else {
	if (FDStoreProperties.isAdServerEnabled()) { %>
<tmpl:put name="categoryPanel" direct="true">
<div style="width:155px; margin-top: 1em">
<script type="text/javascript">
	OAS_AD('LittleRandy');
</script>
</div>
</tmpl:put>
<%
	}
}



%>
<tmpl:put name='title' direct='true'>FreshDirect - Search<%= displayCriteria != null && displayCriteria.length() > 0 ?  (" - " + displayCriteria) : ""%></tmpl:put>
<tmpl:put name='content' direct='true'>

<%

if (FDStoreProperties.isAdServerEnabled()) { %>
<div style="width: 529px; border: 0; margin-top: 15px; padding: 0;">
<style type="text/css">
#OAS_CategoryNote {
text-align: center;
}
</style>
<script type="text/javascript">
	OAS_AD('CategoryNote');
</script>
</div>
<%
	}


if ( results == null) {
	// No results, show search tips
	%><%@ include file="/includes/search/search_tips.jspf"%><%
} else { 
	if (results.numberOfResults() == 0) {
		%><%@ include file="/includes/search/no_results.jspf"%><%
	} else {
		int resultSize = results.getProductsSize();

		if (!jumpToRecipes) {
			//
			// SHOW RESULTS
			//
		    %>
<div class="text10" style="text-align:right; font-weight: bold">
	View:
</div>
<%		
			if (nav.getCategory() != null) {
				ContentNodeModel node = categoryTree.getTreeElement(nav.getCategory()).getModel();
				displayCriteria = "<a href=\"" + FDURLUtil.getCategoryURI(node.getContentKey().getId(), trk) + "\">" + node.getFullName() + "</a>";				
			} else if (nav.getDepartment() != null) {
				ContentNodeModel node = categoryTree.getTreeElement(nav.getDepartment()).getModel();
				displayCriteria = "<a href=\"" + FDURLUtil.getDepartmentURI(node.getContentKey().getId(), trk) + "\">" + node.getFullName() + "</a>";
			}
%><div style="font-size:18px;font-weight:normal">

<%-- view selectors --%>
<span style="float:right"><%
	if (nav.isListView()) {
%><img src="/media_stat/images/template/search/search_view_list_on.gif" style="border: none" alt="List View"> <a href="<%= nav.getSwitchViewAction(SearchNavigator.VIEW_GRID) %>"><img src="/media_stat/images/template/search/search_view_grid_off.gif" style="border: none" alt="Grid View"></a> <a href="<%= nav.getSwitchViewAction(SearchNavigator.VIEW_TEXT) %>"><img src="/media_stat/images/template/search/search_view_text_off.gif" style="border: none" alt="Text View"></a>
<%
	} else if (nav.isGridView()) {
%><a href="<%= nav.getSwitchViewAction(SearchNavigator.VIEW_LIST) %>"><img src="/media_stat/images/template/search/search_view_list_off.gif" style="border: none" alt="List View"></a> <img src="/media_stat/images/template/search/search_view_grid_on.gif" alt="Grid View" style="border: none"> <a href="<%= nav.getSwitchViewAction(SearchNavigator.VIEW_TEXT) %>"><img src="/media_stat/images/template/search/search_view_text_off.gif" style="border: none" alt="Text View"></a>
<%
	} else if (nav.isTextView()) {
%><a href="<%= nav.getSwitchViewAction(SearchNavigator.VIEW_LIST) %>"><img src="/media_stat/images/template/search/search_view_list_off.gif" style="border: none" alt="List View"></a> <a href="<%= nav.getSwitchViewAction(SearchNavigator.VIEW_GRID) %>"><img src="/media_stat/images/template/search/search_view_grid_off.gif" style="border: none" alt="Grid View"></a> <img src="/media_stat/images/template/search/search_view_text_on.gif" style="border: none" alt="Text View">
<%
	}
%>
</span>

<%= resultSize %> product<%= resultSize > 1 ? "s":""%> found <%= displayCriteria.equals(upc) ? "for barcode" : (displayCriteria.equals(criteria) ? "for" : "in") %> <span style="font-weight:bold"><%= displayCriteria %></span>
</div>
<%
	if (results.isSuggestionMoreRelevant()) {
				String sug = results.getSpellingSuggestion();
		%>		
		<div class="text15" style="margin-top:15px; margin-bottom: 15px">
		Did you mean <a href="?searchParams=<%=StringUtil.escapeUri(sug)%>&trk=dym"><b><%=sug%></b></a>?
		</div>
<%
	}
%>
<table cellpadding="0" cellspacing="0" style="width: 529px; border: 0; background-color: #E0E3D0; padding:2px;margin-top: 10px;line-height: 25px;">
<tr>
<td style="width: 100%"><%--

  ************
  * Sort Bar *
  ************
  
--%><span class="text11bold">Sort:</span>
<%
	SearchNavigator.SortDisplay[] sbar = nav.getSortBar();

	for (int i=0; i<sbar.length; i++) {
		%><a href="<%= nav.getChangeSortAction(sbar[i].sortType) %>" class="<%= sbar[i].isSelected ? "text11bold" : "text11" %>"><%= sbar[i].text %></a><%
		if (i < sbar.length-1) {
			%><%= SEPARATOR %><%
		}
	}
%>
</td>
<td>
	<form name="form_brand_filter" id="form_brand_filter" method="GET" style="margin: 0;">
		<input type="hidden" name="searchParams" value="<%= criteria %>">
		<input type="hidden" name="upc" value="<%= upc %>">
<% nav.appendToBrandForm(out); %>
		<select name="brandValue" class="text9" style="width: 140px;" onchange="document.getElementById('form_brand_filter').submit()">
			<option value=""><%= nav.getBrand() == null ? "Filter by brand" : "SHOW ALL PRODUCTS"%></option>
<% 		if (brandSet != null) {
			for (Iterator iter = brandSet.iterator(); iter.hasNext();) {
				BrandModel cm = (BrandModel) iter.next();  
%>			<option value="<%= cm.getContentKey().getId() %>" <%
				if (cm.getContentKey().getId().equals(nav.getBrand())) {%>
					selected="true"
			<%	}
		%>><%= cm.getFullName() %></option>
<%			} // for (brandSet...)
		} // if brandSet
%>
	   </select>
	</form>
</td>
</tr>
</table>

<%-- result list--%>
<div style="margin-top: 5px"><%
		if (nav.isListView()) {
			%><%@ include file="/includes/search/sx_listview.jspf" %><%
		} else if (nav.isGridView()) {
			%><%@ include file="/includes/search/sx_gridview.jspf" %><%
		} else if (nav.isTextView()) {
			if (nav.isTextViewDefault() ) {
				%><%@ include file="/includes/search/sx_textview.jspf" %><%
			} else {
				%><%@ include file="/includes/search/sx_textview_flat.jspf" %><%
			}
		}

		/* unfiltered case*/
		if (nav.getCategory() == null && nav.getDepartment() == null) {
			List recipes = results.getFilteredRecipes();

%>	<div style="width: 100%; border-top:4px solid #ff9933"></div>
<%@ include file="/includes/search/recipes.jspf" %><%
		} else {
%>	<div style="width: 100%; border-top:4px solid #ff9933"></div>
<%
		}
%>
</div>
<%

			// Don't show pager for text view!
			if (!nav.isTextView()) {
%>
<%@ include file="/includes/search/pager.jspf" %>
<%
	    	} // view != 'text'
		} // (resultSize>0)




		if (jumpToRecipes) {
			List recipes = results.getFilteredRecipes();
			String selectedRecipeClassification = nav.getRecipeFilter();
			DomainValue selDv = null;
			
			if (selectedRecipeClassification != null) {
				selDv = (DomainValue) ContentFactory.getInstance().getContentNodeByKey(ContentKey.create(FDContentTypes.DOMAINVALUE, selectedRecipeClassification));
			}
			
%><div class="text10" style="text-align:right;margin-top:15px;font-weight: bold">&nbsp;</div>
	<div style="font-size:18px;font-weight:normal"><%= recipes.size() %> recipe<%= recipes.size() > 1 ? "s":""%> found <% if (selDv != null) { %>in <span style="font-weight:bold"><%= selDv.getLabel() %><% } %></span></div>

	<div class="text13" style="background-color: #E0E3D0;padding:2px;margin-top: 10px;line-height: 25px;">
		<div style="float:left"><span class="text11bold">Sort:</span><%
		boolean descName = nav.isSortByName() && !nav.isSortOrderingAscending();
%>
		<a href="<%= nav.getChangeSortAction(SearchSortType.BY_NAME) %>" class="<%= nav.isSortByName() ? "text11bold" : "text11" %>"><%= descName ? "Name (Z-A)" : "Name (A-Z)" %></a>
	</div>
	<div style="clear: both;"></div>
</div>
<%@ include file="/includes/search/recipes_textview.jspf" %><%
		}
	}
}
%>

</tmpl:put>
</tmpl:insert>

</fd:SmartSearch>
